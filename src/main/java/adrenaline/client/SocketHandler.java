package adrenaline.client;


import adrenaline.*;
import adrenaline.client.controller.GameController;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.GsonBuilder;

public class SocketHandler implements ConnectionHandler {

    private String clientID;
    private volatile boolean active;
    private java.net.Socket myServer;
    private String myLobbyID;
    private SocketClientCommands thisClient;
    private PrintWriter outputToServer;
    private HashMap<String, Object> methodsMap = new HashMap<>();
    private Gson gson;
    private GameController gameController;

    public SocketHandler(String serverIp, int port, GameController gameController) throws IOException {
        myServer = new java.net.Socket(serverIp, port);
        System.out.println("Connection through socket was successful!");
        this.gameController = gameController;
        thisClient = new SocketClientCommands(this, gameController);
        for(Method m : thisClient.getClass().getDeclaredMethods()) methodsMap.put(m.getName(), thisClient);
        Scanner inputFromServer = new Scanner(myServer.getInputStream());
        outputToServer = new PrintWriter(myServer.getOutputStream());
        clientID = inputFromServer.nextLine();
        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(UpdateMessage.class, new CustomSerializer());
        gson = gsonBld.create();
        active = true;
        createServerListener(inputFromServer);
    }


    private void createServerListener(Scanner inputFromServer) {
        new Thread(() -> {
            String readFromServer;
            String[] readSplit;
            Method requestedMethod;

            while (active) {
                try {
                    readFromServer = inputFromServer.nextLine();
                    readSplit = readFromServer.split(";");
                    if (readSplit[0].equals("RETURN")) {
                        gameController.handleReturn(readSplit[1]);
                    }else {
                        String methodName = readSplit[0];
                        int argSize = Integer.parseInt(readSplit[1].substring(readSplit[1].indexOf("=") + 1).trim());
                        Class[] argClasses = new Class[argSize];
                        Object[] argObjects = new Object[argSize];
                        for (int i = 0; i < argSize; i++) {
                            argClasses[i] = Class.forName(readSplit[2 + 2 * i]);
                            argObjects[i] = gson.fromJson(readSplit[3 + 2 * i], argClasses[i]);
                        }
                        requestedMethod = methodsMap.get(methodName).getClass().getMethod(methodName, argClasses);
                        requestedMethod.invoke(methodsMap.get(methodName), argObjects);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    private synchronized void sendMessage(String msg){
        outputToServer.println(msg);
        outputToServer.flush();
    }

    public void unregister() {
        String unregisterMsg = "unregisterClient;ARGSIZE=1;java.lang.String;";
        unregisterMsg += gson.toJson(clientID);
        sendMessage(unregisterMsg);
        active = false;
    }

    public void setNickname(String nickname) {
        String nicknameMsg = "setNickname;ARGSIZE=2;java.lang.String;";
        nicknameMsg += gson.toJson(clientID)+";";
        nicknameMsg += "java.lang.String;";
        nicknameMsg += gson.toJson(nickname);
        sendMessage(nicknameMsg);
    }


    public void setMyLobby(String lobbyID) {
        myLobbyID = lobbyID;
    }

    public void selectAvatar(Color color) {
        String avatarMsg = "selectAvatar;ARGSIZE=2;java.lang.String;";
        avatarMsg += gson.toJson(clientID)+";";
        avatarMsg += "adrenaline.Color;";
        avatarMsg += gson.toJson(color);
        sendMessage(avatarMsg);
    }

    public void sendSettings(int selectedMap, int selectedSkull) {
        String settingsMsg = "selectSettings;ARGSIZE=3;java.lang.String;";
        settingsMsg += gson.toJson(clientID) + ";";
        settingsMsg += "java.lang.Integer;";
        settingsMsg += gson.toJson(selectedMap) + ";";
        settingsMsg += "java.lang.Integer;";
        settingsMsg += gson.toJson(selectedSkull) + ";";
        sendMessage(settingsMsg);
    }

    @Override
    public String getClientID() {
        return clientID;
    }

    @Override
    public String getMyLobbyID() {
        return myLobbyID;
    }
}
