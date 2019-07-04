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

/**
 *
 * The Socket Handler class implements ConnectionHandler, For socket connect with server
 * send message from client to server,and received the return value
 *
 */
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

    /**
     *
     *
     * The constructor creates the socket and try to build a TCP connect with game server
     *
     * @param serverIP The server ip address
     * @param port The port value, Initial value for Socket is 1100
     * @param gameController The reference of current gameController
     *
     * @throws IOException If IO problem occur,it will throws IOException
     *
     */
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
                sendMessage("reconnectClient;ARGSIZE=2;java.lang.String;" + clientID + ";java.lang.String;" + reconnID);
                String result = inputFromServer.nextLine();
                while (!result.contains("RETURN")) {
                    String[] readSplit = result.split(";");
                    switch(readSplit[0]) {
                        case("setNickname"):
                            thisClient.setNickname(gson.fromJson(readSplit[3], String.class));
                            break;
                        case("setLobby"):
                            thisClient.setLobby(gson.fromJson(readSplit[3], String.class), gson.fromJson(readSplit[5], ArrayList.class));
                            break;
                        case("setPlayerColor"):
                            thisClient.setPlayerColor(gson.fromJson(readSplit[3], String.class), gson.fromJson(readSplit[5], Color.class));
                            break;
                        case("update"):
                            thisClient.update(gson.fromJson(readSplit[3], UpdateMessage.class));
                            break;
                    }
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

    /**
     *
     * The ServerListener at client side , for received package from server,
     * and it can parser package and call right method.
     *
     *
     * @param inputFromServer The package from server
     *
     */
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
                    closeConnection();
                    System.out.println("DISCONNECTED FROM SERVER");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    /**
     *
     * For use socket to send game flow message from client to server
     *
     * @param msg The msg string witch wait to be send
     *
     */
    private synchronized void sendMessage(String msg){
        outputToServer.println(msg);
        outputToServer.flush();
    }

    /**
     *
     * To unregister for client
     *
     */
    public void unregister() {
        String unregisterMsg = "unregisterClient;ARGSIZE=1;java.lang.String;";
        unregisterMsg += gson.toJson(clientID);
        sendMessage(unregisterMsg);
        active = false;
    }

    /**
     *
     * To set nickname of player from client terminal to server terminal
     *
     * @param nickname The nickname string
     */
    public void setNickname(String nickname) {
        String nicknameMsg = "setNickname;ARGSIZE=2;java.lang.String;";
        nicknameMsg += gson.toJson(clientID)+";";
        nicknameMsg += "java.lang.String;";
        nicknameMsg += gson.toJson(nickname);
        sendMessage(nicknameMsg);
    }

    /**
     *
     * To set the lobby from client terminal to server terminal
     *
     * @param lobbyID The lobbyID string
     */
    public void setMyLobby(String lobbyID) {
        myLobbyID = lobbyID;
    }

    /**
     *
     * To select avatar operation for player
     * at AvatarSelectionState from client terminal to server terminal
     *
     * @param color The color of avatar witch the player selected
     */
    public void selectAvatar(Color color) {
        String avatarMsg = "selectAvatar;ARGSIZE=2;java.lang.String;";
        avatarMsg += gson.toJson(clientID)+";";
        avatarMsg += "adrenaline.Color;";
        avatarMsg += gson.toJson(color);
        sendMessage(avatarMsg);
    }

    /**
     *
     * To select powerup card for player
     * from client terminal to server terminal
     *
     * @param powerupID The powerupId witch the player selected
     */
    public void selectPowerUp(int powerupID) {
        String powerupMsg = "selectPowerUp;ARGSIZE=2;java.lang.String;";
        powerupMsg += gson.toJson(clientID)+";";
        powerupMsg += "java.lang.Integer;";
        powerupMsg += gson.toJson(powerupID);
        sendMessage(powerupMsg);
    }

    /**
     *
     * To select weapon for player from client terminal to server terminal
     *
     * @param weaponID The  weaponID witch the player selected
     */
    public void selectWeapon(int weaponID) {
        String weaponMsg = "selectWeapon;ARGSIZE=2;java.lang.String;";
        weaponMsg += gson.toJson(clientID)+";";
        weaponMsg += "java.lang.Integer;";
        weaponMsg += gson.toJson(weaponID);
        sendMessage(weaponMsg);
    }

    /**
     *
     * To select ammo for player from client terminal to server terminal
     *
     * @param color The ammo color witch the player selected
     */
    @Override
    public void selectAmmo(Color color) {
        String ammoMsg = "selectAmmo;ARGSIZE=2;java.lang.String;";
        ammoMsg += gson.toJson(clientID)+";";
        ammoMsg += "adrenaline.Color;";
        ammoMsg += gson.toJson(color);
        sendMessage(ammoMsg);
    }

    /**
     *
     * To send Map(from 1 to 4) and skull(from 5 to 8) set from client terminal to server terminal
     *
     * @param selectedMap The map number from 1 to 4
     * @param selectedSkull The number of Skull from 5 to 8
     *
     */
    public void sendSettings(int selectedMap, int selectedSkull) {
        String settingsMsg = "selectSettings;ARGSIZE=3;java.lang.String;";
        settingsMsg += gson.toJson(clientID) + ";";
        settingsMsg += "java.lang.Integer;";
        settingsMsg += gson.toJson(selectedMap) + ";";
        settingsMsg += "java.lang.Integer;";
        settingsMsg += gson.toJson(selectedSkull);
        sendMessage(settingsMsg);
    }

    /**
     *
     * To send a chat message from client terminal to server terminal
     *
     * @param message The message string witch wait for be send
     *
     */
    public void sendChatMessage(String message) {
        String chatMsg = "sendChatMessage;ARGSIZE=2;java.lang.String;";
        chatMsg += gson.toJson(clientID) + ";";
        chatMsg += "java.lang.String;";
        chatMsg += gson.toJson(message);
        sendMessage(chatMsg);
    }

    /**
     *
     * To send the run action for player from client terminal to server terminal
     *
     */
    @Override
    public void run() {
        String runMsg = "runAction;ARGSIZE=1;java.lang.String;";
        runMsg += gson.toJson(clientID);
        sendMessage(runMsg);
    }

    /**
     *
     * To send the end turn action for player from client terminal to server terminal
     *
     */
    @Override
    public void endTurn() {
        String endMsg = "endOfTurnAction;ARGSIZE=1;java.lang.String;";
        endMsg += gson.toJson(clientID);
        sendMessage(endMsg);
    }

    /**
     *
     *
     * To send the square selection from client terminal to server terminal
     *
     * @param index The square index from 0 to 11
     *
     */
    @Override
    public void selectSquare(int index) {
        String squareMsg = "selectSquare;ARGSIZE=2;java.lang.String;";
        squareMsg += gson.toJson(clientID)+";";
        squareMsg += "java.lang.Integer;";
        squareMsg += gson.toJson(index);
        sendMessage(squareMsg);
    }


    /**
     *
     * To send the firemode selection from client terminal to server terminal
     *
     * @param firemode The firemode index from 0 to 2
     *
     */
    @Override
    public void selectFiremode(int firemode) {
        String firemodeMsg = "selectFiremode;ARGSIZE=2;java.lang.String;";
        firemodeMsg += gson.toJson(clientID)+";";
        firemodeMsg += "java.lang.Integer;";
        firemodeMsg += gson.toJson(firemode);
        sendMessage(firemodeMsg);
    }

    /**
     *
     *
     * To send the player selection from client terminal to server terminal
     *
     * @param targets The ArrayList witch contain players' color
     */
    @Override
    public void selectPlayers(ArrayList<Color> targets) {
        String  playersMsg = "selectPlayers;ARGSIZE=2;java.lang.String;";
        playersMsg += gson.toJson(clientID)+";";
        playersMsg += "java.util.ArrayList;";
        playersMsg += gson.toJson(targets);
        sendMessage(playersMsg);
    }

    /**
     *
     * To send especial move action at MoveEnemyState or MoveSelfState from client terminal to server terminal
     *
     *
     */
    @Override
    public void moveSubAction() {
        String moveMsg = "moveSubAction;ARGSIZE=1;java.lang.String;";
        moveMsg += gson.toJson(clientID);
        sendMessage(moveMsg);
    }

    /**
     *
     * To send selection final frenzy action from client terminal to server terminal
     * <p>
     *     The action index range depending on the mode
     *     In mode 0, action index from 0 to 2
     *     In mode 1, action index from 0 to 1
     * </p>
     *
     *
     * @param action The action index range depending on the mode from 0 to 1(or 2)
     *
     */
    @Override
    public void selectFinalFrenzyAction(int action) {
        String  frenzyMsg = "selectFinalFrenzyAction;ARGSIZE=2;java.lang.String;";
        frenzyMsg += gson.toJson(clientID)+";";
        frenzyMsg += "java.lang.Integer;";
        frenzyMsg += gson.toJson(action);
        sendMessage(frenzyMsg);
    }

    /**
     *
     * To send the grab action for player from client terminal to server terminal
     */
    @Override
    public void grab() {
        String grabMsg = "grabAction;ARGSIZE=1;java.lang.String;";
        grabMsg += gson.toJson(clientID);
        sendMessage(grabMsg);
    }

    /**
     *
     * To send the shoot action for player from client terminal to server terminal
     */
    @Override
    public void shoot() {
        String shootMsg = "shootAction;ARGSIZE=1;java.lang.String;";
        shootMsg += gson.toJson(clientID);
        sendMessage(shootMsg);
    }

    /**
     *
     * To send the go back action for player from client terminal to server terminal
     */
    @Override
    public void back() {
        String backMsg = "goBack;ARGSIZE=1;java.lang.String;";
        backMsg += gson.toJson(clientID);
        sendMessage(backMsg);
    }

    /**
     *
     *
     * The getter for ClientID
     *
     * @return The clientID string
     */
    @Override
    public String getClientID() {
        return clientID;
    }

    /**
     *
     * The getter for LobbyID
     *
     * @return The lobbyID string
     */
    @Override
    public String getMyLobbyID() {
        return myLobbyID;
    }

    /**
     *
     * For close the connection from server to client
     *
     *
     */
    @Override
    public void closeConnection() {
        active = false;
        try {
            myServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
