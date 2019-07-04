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
import java.util.*;


/**
 *
 *
 * The client socket wrapper class implements Client interface,it will send package from server to client via socket
 *
 *
 */
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


    /**
     *
     * The constructor of ClientSocketWrapper
     *
     * @param newClient The new client socket reference
     * @param serverCommands The ServerCommands reference
     * @throws IOException It will throws IOException
     */
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

    /**
     *
     * The ServerListener at server side , for received package from client,
     * and it can parser package and call right method.
     *
     *
     * @param inputFromClient The package from client
     *
     */
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
                } catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                    System.out.println("MESSAGE RECEIVED: "+ readFromClient);
                }finally{ if(active) sendMessage(sendToClient);}
            }
        }).start();
    }

    /**
     *
     * For send a message to a client
     *
     * @param sendToClient The message string
     */
    private synchronized void sendMessage(String sendToClient){
        if(active) {
            outputToClient.println(sendToClient);
            outputToClient.flush();
        }
    }

    /**
     *
     * The setter of ClientID
     * @param ID The ClientID string
     */
    public void setClientID(String ID) {
        clientID = ID;
    }

    /**
     *
     * The getter of ClientID
     *
     * @return The ClientID string
     */
    public String getClientID() {
        return clientID;
    }


    /**
     *
     * The getter of nick name
     *
     * @return The nickname string
     */
    public String getNickname(){
        return nickname;
    }

    /**
     *
     * Call the setNickname method to send the current player's nickname
     * from server terminal to client terminal
     *
     * @param nickname The nickname string
     * @return The set operation if is successful
     */
    public boolean setNicknameInternal(String nickname) {
        if(this.nickname != null) return false;
        this.nickname = nickname;
        setNickname(nickname);
        return true;
    }

    /**
     *
     * To send the current player's nickname from server terminal to client terminal
     *
     * @param nickname The nickname string
     *
     */
    public void setNickname(String nickname){
        sendMessage("setNickname;ARGSIZE=1;java.lang.String;"+gson.toJson(nickname));
    }

    /**
     *
     * Setter of active status
     *
     * @param active True for still active
     */
    public void setActive(boolean active) { this.active = active; }


    /**
     *
     * The getter of active status
     *
     * @return True for still active
     */
    public boolean isActive() { return active; }

    /**
     *
     * To send the lobbyID and players' nickname ArrayList from server terminal to client terminal
     *
     * @param lobby The lobby id string
     * @param nicknames The players' nickname ArrayList
     */
    public void setLobby(Lobby lobby, ArrayList<String> nicknames)  {
        this.inLobby=lobby;
        for(Method m : inLobby.getClass().getDeclaredMethods()) methodsMap.put(m.getName(), inLobby);
        setLobby(inLobby.getID(), nicknames);
    }

    /**
     *
     * To send the lobbyID and players' nickname ArrayList from server terminal to client terminal
     *
     * @param lobbyID The lobby id string
     * @param nicknames The players' nickname ArrayList
     */
    public void setLobby(String lobbyID, ArrayList<String> nicknames) {
        sendMessage("setLobby;ARGSIZE=2;java.lang.String;"+gson.toJson(lobbyID)+";java.util.ArrayList;"+gson.toJson(nicknames));
    }

    /**
     *
     * Call setPlayerColor method to send the player color set value from server terminal to client terminal
     *
     * @param nickname The nickname of players
     * @param color The color of players
     */
    public void setPlayerColorInternal(String nickname, Color color) {
        setPlayerColor(nickname, color);
    }


    /**
     *
     *
     * Send the player color set value from server terminal to client terminal
     *
     * @param nickname The nickname of players
     * @param color The color of players
     */
    public void setPlayerColor(String nickname, Color color) {
        sendMessage("setPlayerColor;ARGSIZE=2;java.lang.String;"+gson.toJson(nickname)+";adrenaline.Color;"+gson.toJson(color));
    }

    /**
     *
     *
     * To remind the client timer start.
     *
     * @param duration The duration of timer in seconds
     * @param comment The comment for this timer
     */
    public void timerStarted(Integer duration, String comment) {
        sendMessage("timerStarted;ARGSIZE=2;java.lang.Integer;"+gson.toJson(duration)+";java.lang.String;"+gson.toJson(comment));
    }

    /**
     *
     * To send the valid squares from server to client terminal
     *
     * @param validSquares The ArrayList of the valid squares
     *
     */
    public void validSquaresInfo(ArrayList<Integer> validSquares) {
        sendMessage("validSquaresInfo;ARGSIZE=1;java.util.ArrayList;"+gson.toJson(validSquares));
    }

    /**
     *
     *
     * To send the update message from server terminal to client terminal
     *
     * @param updatemsg The UpdateMessage reference
     *
     */
    public void update(UpdateMessage updatemsg) {
        sendMessage("update;ARGSIZE=1;adrenaline.UpdateMessage;"+gson.toJson(updatemsg, UpdateMessage.class));
    }

    /**
     *
     * To kick a client when he did nothing during the whole turn
     * he have to do the reconnection to reconnect the server
     */
    public void kick() {
        serverCommands.unregisterClient(clientID);
        kick();
    }

    /**
     *
     * To kick a client when he did nothing during the whole turn
     * he have to do the reconnection to reconnect the server
     */
    public void kickClient() {
        sendMessage("kick;ARGSIZE=0;");
    }
}
