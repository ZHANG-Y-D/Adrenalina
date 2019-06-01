package adrenaline.server.network;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.server.controller.Lobby;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

public class ClientSocketWrapper implements Client {
    private final String clientID;
    private String nickname = null;
    private Socket thisClient;
    private volatile boolean active;

    private final SocketServerCommands serverCommands;
    private Lobby inLobby;
    private HashMap<String,Object> methodsMap = new HashMap<>();
    private PrintWriter outputToClient;
    private Gson gson;

    public ClientSocketWrapper(Socket newClient, SocketServerCommands serverCommands) throws IOException {
        this.clientID = UUID.randomUUID().toString();
        this.thisClient = newClient;
        this.active = true;
        this.serverCommands = serverCommands;
        for(Method m : serverCommands.getClass().getDeclaredMethods())methodsMap.put(m.getName(), serverCommands);
        Scanner inputFromClient = new Scanner(thisClient.getInputStream());
        outputToClient = new PrintWriter(thisClient.getOutputStream());
        gson = new Gson();
        createListener(inputFromClient);
        sendMessage(clientID);
    }

    private void createListener(Scanner inputFromClient) {
        new Thread(() -> {
            String readFromClient, sendToClient;
            String[] readSplit;
            Method requestedMethod;

            while(active){
                sendToClient="RETURN;";
                try {
                    readFromClient = inputFromClient.nextLine();
                    readSplit = readFromClient.split(";");
                    String methodName = readSplit[0];
                    int argSize = Integer.parseInt(readSplit[1].substring(readSplit[1].indexOf("=") + 1).trim());
                    Class[] argClasses = new Class[argSize];
                    Object[] argObjects = new Object[argSize];
                    for (int i = 0; i < argSize; i++) {
                        argClasses[i] = Class.forName(readSplit[2 + 2 * i]);
                        argObjects[i] = gson.fromJson(readSplit[3 + 2 * i], argClasses[i]);
                    }
                    requestedMethod = methodsMap.get(methodName).getClass().getMethod(methodName, argClasses);
                    sendToClient += requestedMethod.invoke(methodsMap.get(methodName), argObjects).toString();
                } catch (InvocationTargetException | ClassNotFoundException e) {
                    sendToClient += "SERVER ERROR!";
                } catch (NullPointerException | NoSuchMethodException |
                            IllegalAccessException | NoSuchElementException e) {
                    sendToClient += "ERROR! Invalid command request";
                }finally{ sendMessage(sendToClient);}
            }
        }).start();
    }

    private synchronized void sendMessage(String sendToClient){
        outputToClient.println(sendToClient);
        outputToClient.flush();
    }

    public String getClientID() {
        return clientID;
    }

    public String getNickname(){
        return nickname;
    }

    public boolean setNickname(String nickname) {
        if(this.nickname != null) return false;
        this.nickname = nickname;
        return true;
    }

    public void setActive(boolean active) { this.active = active; }

    public void setLobby(Lobby lobby, ArrayList<String> nicknames)  {
        this.inLobby=lobby;
        for(Method m : inLobby.getClass().getDeclaredMethods()) methodsMap.put(m.getName(), inLobby);
        setLobby(inLobby.getID(), nicknames);
    }

    public void setLobby(String lobbyID, ArrayList<String> nicknames) {
        sendMessage("setLobby;ARGSIZE=2;java.lang.String;"+gson.toJson(lobbyID)+";java.util.ArrayList;"+gson.toJson(nicknames));
    }

    public void setPlayerColor(String nickname, Color color) {
        sendMessage("setPlayerColor;ARGSIZE=2;java.lang.String;"+gson.toJson(nickname)+";adrenaline.Color;"+gson.toJson(color));
    }

    public void timerStarted(Integer duration) {
        sendMessage("timerStarted;ARGSIZE=1;java.lang.Integer;"+gson.toJson(duration));
    }

    public void update(UpdateMessage updatemsg) { sendMessage("update:ARGSZIE=1;adrenaline.UpdateMessage:"+gson.toJson(updatemsg));}


}
