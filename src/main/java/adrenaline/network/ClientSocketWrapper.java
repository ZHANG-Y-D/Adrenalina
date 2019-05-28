package adrenaline.network;

import adrenaline.server.controller.Lobby;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class ClientSocketWrapper implements Client {
    private final String clientID;
    private String nickname = null;
    private Socket thisClient;
    private boolean active;

    private final SocketServerCommands serverCommands;
    private Lobby inLobby;
    private HashMap<String,Object> methodsMap = new HashMap<>();
    private PrintWriter outputToClient;

    public ClientSocketWrapper(Socket newClient, SocketServerCommands serverCommands) throws IOException {
        this.clientID = UUID.randomUUID().toString();
        this.thisClient = newClient;
        this.active = true;
        this.serverCommands = serverCommands;
        for(Method m : serverCommands.getClass().getDeclaredMethods())methodsMap.put(m.getName(), serverCommands);
        Scanner inputFromClient = new Scanner(thisClient.getInputStream());
        outputToClient = new PrintWriter(thisClient.getOutputStream());
        createListener(inputFromClient);
        sendMessage(clientID);
    }

    private void createListener(Scanner inputFromClient) {
        new Thread(() -> {
            Gson gson = new Gson();
            String readFromClient, sendToClient;
            String[] readSplit;
            Method requestedMethod;

            while(true){
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
                } catch (NullPointerException | NoSuchMethodException | IllegalAccessException e) {
                    sendToClient += "ERROR! Invalid command request";
                } finally{ sendMessage(sendToClient);}
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

    public void setNickname(String nickname) { this.nickname = nickname; }

    public void setActive(boolean active) { this.active = active; }

    public void setLobby(Lobby lobby) {
        this.inLobby=lobby;
        for(Method m : inLobby.getClass().getDeclaredMethods()) methodsMap.put(m.getName(), inLobby);
        setLobby(inLobby.getID());
    }

    public void setLobby(String lobbyID) {
    }
}
