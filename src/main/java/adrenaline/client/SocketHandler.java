package adrenaline.client;


import adrenaline.*;
import adrenaline.client.controller.GameController;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
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

    public SocketHandler(String serverIP, int port, GameController gameController) throws IOException {
        myServer = new java.net.Socket(serverIP, port);
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
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("reconnection.txt"));
            if(fileReader.readLine().equals(serverIP)) {
                String reconnID = fileReader.readLine();
                sendMessage("reconnectSocketClient;ARGSIZE=2;java.lang.String;" + clientID + ";java.lang.String;" + reconnID);
                String result = inputFromServer.nextLine();
                while (!result.contains("RETURN")) {
                    String[] readSplit = result.split(";");
                    if(readSplit[0].equals("setNickname")) thisClient.setNickname(gson.fromJson(readSplit[3],String.class));
                    else if(readSplit[0].equals("setLobby")) thisClient.setLobby(gson.fromJson(readSplit[3],String.class), gson.fromJson(readSplit[5], ArrayList.class));
                    result = inputFromServer.nextLine();
                }
                if(result.substring(7,9).equals("OK")) clientID = reconnID;
            }
        }catch(FileNotFoundException e){}
        PrintWriter fileWriter = new PrintWriter("reconnection.txt", "UTF-8");
        fileWriter.println(serverIP);
        fileWriter.println(clientID);
        fileWriter.close();
        System.out.println("Connection through Socket was succesful!");
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
                    } else {
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
                } catch(NoSuchElementException nee){
                    active = false;
                    nee.printStackTrace();
                    System.out.println("DISCONNECTED FROM SERVER");
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

    public void selectPowerUp(int powerupID) {
        String powerupMsg = "selectPowerUp;ARGSIZE=2;java.lang.String;";
        powerupMsg += gson.toJson(clientID)+";";
        powerupMsg += "java.lang.Integer;";
        powerupMsg += gson.toJson(powerupID);
        sendMessage(powerupMsg);
    }

    public void selectWeapon(int weaponID) {
        String weaponMsg = "selectWeapon;ARGSIZE=2;java.lang.String;";
        weaponMsg += gson.toJson(clientID)+";";
        weaponMsg += "java.lang.Integer;";
        weaponMsg += gson.toJson(weaponID);
        sendMessage(weaponMsg);
    }

    public void sendSettings(int selectedMap, int selectedSkull) {
        String settingsMsg = "selectSettings;ARGSIZE=3;java.lang.String;";
        settingsMsg += gson.toJson(clientID) + ";";
        settingsMsg += "java.lang.Integer;";
        settingsMsg += gson.toJson(selectedMap) + ";";
        settingsMsg += "java.lang.Integer;";
        settingsMsg += gson.toJson(selectedSkull);
        sendMessage(settingsMsg);
    }

    public void sendChatMessage(String message) {
        String chatMsg = "sendChatMessage;ARGSIZE=2;java.lang.String;";
        chatMsg += gson.toJson(clientID) + ";";
        chatMsg += "java.lang.String;";
        chatMsg += gson.toJson(message);
        sendMessage(chatMsg);
    }

    @Override
    public void run() {
        String runMsg = "runAction;ARGSIZE=1;java.lang.String;";
        runMsg += gson.toJson(clientID);
        sendMessage(runMsg);
    }

    @Override
    public void endTurn() {
        String endMsg = "endOfTurnAction;ARGSIZE=1;java.lang.String;";
        endMsg += gson.toJson(clientID);
        sendMessage(endMsg);
    }

    @Override
    public void selectSquare(int index) {
        String squareMsg = "selectSquare;ARGSIZE=2;java.lang.String;";
        squareMsg += gson.toJson(clientID)+";";
        squareMsg += "java.lang.Integer;";
        squareMsg += gson.toJson(index);
        sendMessage(squareMsg);
    }

    @Override
    public void selectFiremode(int firemode) {
        String firemodeMsg = "selectFiremode;ARGSIZE=2;java.lang.String;";
        firemodeMsg += gson.toJson(clientID)+";";
        firemodeMsg += "java.lang.Integer;";
        firemodeMsg += gson.toJson(firemode);
        sendMessage(firemodeMsg);
    }

    @Override
    public void selectPlayers(ArrayList<Color> targets) {
        String  playersMsg = "selectPlayers;ARGSIZE=2;java.lang.String;";
        playersMsg += gson.toJson(clientID)+";";
        playersMsg += "java.util.ArrayList;";
        playersMsg += gson.toJson(targets);
        sendMessage(playersMsg);
    }

    @Override
    public void grab() {
        String grabMsg = "grabAction;ARGSIZE=1;java.lang.String;";
        grabMsg += gson.toJson(clientID);
        sendMessage(grabMsg);
    }

    @Override
    public void shoot() {
        String shootMsg = "shootAction;ARGSIZE=1;java.lang.String;";
        shootMsg += gson.toJson(clientID);
        sendMessage(shootMsg);
    }

    @Override
    public void back() {
        String backMsg = "goBack;ARGSIZE=1;java.lang.String;";
        backMsg += gson.toJson(clientID);
        sendMessage(backMsg);
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
