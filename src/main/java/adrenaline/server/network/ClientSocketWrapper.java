package adrenaline.server.network;

import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.UpdateMessage;
import adrenaline.server.controller.Lobby;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;

public class ClientSocketWrapper implements Client {
    private String clientID;
    private String nickname = null;
    private Socket thisClient;
    private volatile boolean active;

    private final ServerCommands serverCommands;
    private Lobby inLobby;
    private HashMap<String,Object> methodsMap = new HashMap<>();
    private PrintWriter outputToClient;
    private Gson gson;

    public ClientSocketWrapper(Socket newClient, ServerCommands serverCommands) throws IOException {
        this.clientID = UUID.randomUUID().toString();
        this.thisClient = newClient;
        this.active = true;
        this.serverCommands = serverCommands;
        for(Method m : serverCommands.getClass().getDeclaredMethods())
            if(!m.getName().equals("registerRMIClient") && !m.getName().equals("reconnectRMIClient")) methodsMap.put(m.getName(), serverCommands);
        Scanner inputFromClient = new Scanner(thisClient.getInputStream());
        outputToClient = new PrintWriter(thisClient.getOutputStream());
        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(UpdateMessage.class, new CustomSerializer());
        gson = gsonBld.create();
        createListener(inputFromClient);
        sendMessage(clientID);
    }

    private void createListener(Scanner inputFromClient) {
        new Thread(() -> {
            String readFromClient = null, sendToClient;
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
                    System.out.println("MESSAGE RECEIVED: "+readFromClient);
                    e.printStackTrace();
                    sendToClient += "SERVER ERROR!";
                } catch (NullPointerException | NoSuchMethodException |
                            IllegalAccessException e) {
                    System.out.println("MESSAGE RECEIVED: " + readFromClient);
                    e.printStackTrace();
                    sendToClient += "ERROR! Invalid command request";
                } catch (NoSuchElementException fatal){
                    serverCommands.unregisterClient(clientID);
                    if(inLobby!=null) inLobby.detachClient(this);
                }finally{ if(active) sendMessage(sendToClient);}
            }
        }).start();
    }

    private synchronized void sendMessage(String sendToClient){
        if(active) {
            outputToClient.println(sendToClient);
            outputToClient.flush();
        }
    }

    public void setClientID(String ID) {
        clientID = ID;
    }

    public String getClientID() {
        return clientID;
    }

    public String getNickname(){
        return nickname;
    }

    public boolean setNicknameInternal(String nickname) {
        if(this.nickname != null) return false;
        this.nickname = nickname;
        setNickname(nickname);
        return true;
    }

    public void setNickname(String nickname){
        sendMessage("setNickname;ARGSIZE=1;java.lang.String;"+gson.toJson(nickname));
    }

    public void setActive(boolean active) { this.active = active; }

    public boolean isActive() { return active; }

    public void setLobby(Lobby lobby, ArrayList<String> nicknames)  {
        this.inLobby=lobby;
        for(Method m : inLobby.getClass().getDeclaredMethods()) methodsMap.put(m.getName(), inLobby);
        setLobby(inLobby.getID(), nicknames);
    }

    public void setLobby(String lobbyID, ArrayList<String> nicknames) {
        sendMessage("setLobby;ARGSIZE=2;java.lang.String;"+gson.toJson(lobbyID)+";java.util.ArrayList;"+gson.toJson(nicknames));
    }

    public void setPlayerColorInternal(String nickname, Color color) {
        setPlayerColor(nickname, color);
    }

    public void setPlayerColor(String nickname, Color color) {
        sendMessage("setPlayerColor;ARGSIZE=2;java.lang.String;"+gson.toJson(nickname)+";adrenaline.Color;"+gson.toJson(color));
    }

    public void timerStarted(Integer duration, String comment) {
        sendMessage("timerStarted;ARGSIZE=2;java.lang.Integer;"+gson.toJson(duration)+";java.lang.String;"+gson.toJson(comment));
    }

    public void validSquaresInfo(ArrayList<Integer> validSquares) {
        sendMessage("validSquaresInfo;ARGSIZE=1;java.util.ArrayList;"+gson.toJson(validSquares));
    }

    public void update(UpdateMessage updatemsg) {
        sendMessage("update;ARGSIZE=1;adrenaline.UpdateMessage;"+gson.toJson(updatemsg, UpdateMessage.class));
    }

    public void kick() {
        inLobby.detachClient(this);
        kick();
    }

    public void kickClient() {
        sendMessage("kick;ARGSIZE=0;");
        serverCommands.unregisterClient(clientID);
    }
}
