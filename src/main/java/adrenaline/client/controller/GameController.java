package adrenaline.client.controller;

import adrenaline.Color;
import adrenaline.client.ConnectionHandler;
import adrenaline.client.RMIHandler;
import adrenaline.client.SocketHandler;
import adrenaline.client.model.Map;
import adrenaline.client.model.Player;
import adrenaline.client.model.ScoreBoard;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * The game controller of the current client.The main controller of client terminal.
 *
 *
 */
public class GameController {

    private LinkedHashMap<String, Color> playersNicknames = new LinkedHashMap<>();
    private String ownNickname;
    private Color ownColor;
    private Integer finalFrenzyMode = 0;

    private HashMap<Color, Player> playersMap = new HashMap<>();
    private ScoreBoard scoreBoard;
    private Map map;
    private ViewInterface view;
    private ConnectionHandler connectionHandler;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);


    /**
     *
     * The getter of playersNicknames
     *
     * @return A reference of playersNicknames(Type LinkedHashMap)
     */
    public LinkedHashMap<String, Color> getPlayersNicknames() { return playersNicknames; }

    /**
     *
     * The getter of playersMap
     *
     * @return A reference of playersMap(Type HashMap)
     */
    public HashMap<Color, Player> getPlayersMap() { return playersMap; }

    /**
     *
     * The getter of client side map model
     *
     * @return The reference of map(Type client side Map)
     */
    public Map getMap(){ return map; }

    /**
     *
     * The getter of scoreBoard
     *
     * @return The reference of scoreBoard(Type client side ScoreBoard)
     */
    public ScoreBoard getScoreBoard() { return scoreBoard; }

    /**
     *
     * The getter of connectionHandler
     *
     * @return The reference of connectionHandler(Type ConnectionHandler)
     */
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    /**
     *
     * To set the current player's nickname
     *
     * @param name The nickname string
     */
    public void setOwnNickname(String name) {
        ownNickname = name;
        view.showMessage("NICKNAME;"+name);
    }

    /**
     *
     * The getter of ownNickname
     *
     * @return The ownNickname string
     */
    public String getOwnNickname() { return ownNickname; }

    /**
     *
     * The getter of ownColor
     *
     * @return The ownColor(Type Color)
     */
    public Color getOwnColor() { return  ownColor; }

    /**
     *
     * The setter of current viewController,when change stage,
     * view have to set the new viewController via this method
     *
     * @param viewController The viewController reference
     */
    public void setViewController(ViewInterface viewController){
        this.view = viewController;
    }

    /**
     *
     * A api for View to connect with server via RMI
     *
     * @param host The server ip address
     * @param port The port value, Initial value for RMI is 1099
     * @return If the host and port are right, return turn.
     * Attention it is not means the connect successful
     */
    public boolean connectRMI(String host, int port){
        try {
            connectionHandler = new RMIHandler(host,port,this);
            return true;
        } catch (Exception e) {
            view.showError("Wrong host/port");
            return false;
        }
    }

    /**
     *
     * A api for View to connect with server via Socket
     *
     * @param host The server ip address
     * @param port The port value, Initial value for Socket is 1100
     * @return If the host and port are right, return turn.
     *      * Attention it is not means the connect successful
     */
    public boolean connectSocket(String host, int port){
        try {
            connectionHandler = new SocketHandler(host,port,this);
            return true;
        } catch (Exception e) {
            view.showError("Wrong host/port");
            return false;
        }
    }

    /**
     *
     * A api for View to send  to server the nickname from player input
     *
     *
     * @param nickname The nickname string
     */
    public void setNickname(String nickname){ connectionHandler.setNickname(nickname);}

    /**
     *
     * To initial every Players' Nicknames to White for per-view
     *
     *
     * @param nicknames The players' nicknames
     */
    public void initPlayersNicknames(ArrayList<String> nicknames){
        nicknames.forEach(x -> playersNicknames.put(x, Color.WHITE));
    }

    /**
     *
     *
     * A api for View to send  to server the Avatar selection from player input
     *
     * @param color The Avatar's color
     */
    public void selectAvatar(Color color){ connectionHandler.selectAvatar(color); }

    /**
     *
     * A api for View to send to server the powerup selection from player input
     *
     *
     * @param powerupID The powerup card's number
     */
    public void selectPowerUp(int powerupID){ connectionHandler.selectPowerUp(powerupID); }

    /**
     *
     * A api for View to send to server the powerup selection from player input
     *
     *
     * @param weaponID The weapon card's number
     */
    public void selectWeapon(int weaponID) { connectionHandler.selectWeapon(weaponID); }

    /**
     *
     * A api for View to send to server the square selection from player input
     *
     * @param index The index of square from 0 to 11
     */
    public void selectSquare(int index) { connectionHandler.selectSquare(index); }


    /**
     * A api for View to send to server the Ammo selection from player input
     *
     * @param color The ammo box color
     */
    public void selectAmmo(Color color) { connectionHandler.selectAmmo(color); }

    /**
     *
     * A api for View to close the connect with server
     *
     *
     */
    public void cleanExit(){
        if(connectionHandler != null) connectionHandler.unregister();
    }

    /**
     *
     * Received the return value from server
     *
     * <p>
     *     If the return value contain OK, it will only show rest field via showMessage
     *     If the return value not contain OK,it will show via showError
     * </p>
     *
     *
     * @param returnMsg The return message value
     */
    public void handleReturn(String returnMsg){
        if(!returnMsg.contains("OK")) view.showError(returnMsg);
        else if(returnMsg.length() > 2) view.showMessage(returnMsg.substring(3));
    }

    /**
     *
     * Remind the view to change stage
     *
     *
     */
    public void changeStage(){
        view.changeStage();
    }

    /**
     *
     * The implement of observer pattern. Received and set a player's color set from server
     *
     *
     * @param nickname The players' nickname
     * @param color The player's color
     */
    public void setPlayerColor(String nickname, Color color){
        if(nickname.equals(ownNickname)) ownColor = color;
        playersMap.put(color, new Player());
        changes.firePropertyChange("nicknamesColor", playersNicknames, playersNicknames.put(nickname, color));
    }


    /**
     *
     * The implement of observer pattern. Received and set a player's status map set from server
     *
     * @param newPlayer The reference of client side Player
     */
    public synchronized void updatePlayer(Player newPlayer){
        HashMap<Color, Player> oldPlayersMap = playersMap;
        HashMap<Color, Player> newPlayersMap = new HashMap<>(playersMap);
        newPlayersMap.put(newPlayer.getColor(), newPlayer);
        playersMap = newPlayersMap;
        changes.firePropertyChange("player", oldPlayersMap, newPlayersMap);
    }

    /**
     *
     * The implement of observer pattern. Received and set a Map set from server
     *
     *
     * @param newMap The reference of client side Map
     */
    public synchronized void updateMap(Map newMap){
        Map oldMap = map;
        map = newMap;
        changes.firePropertyChange("map", oldMap, newMap);
    }

    /**
     *
     * The implement of observer pattern. Received and set a scoreboard set from server
     *
     * @param newScoreboard The reference of client side Scoreboard
     */
    public void updateScoreboard(ScoreBoard newScoreboard) {
        ScoreBoard oldScoreboard = scoreBoard;
        scoreBoard = newScoreboard;

        if(scoreBoard.getFinalPlayersPosition() == null) changes.firePropertyChange("scoreboard", oldScoreboard, newScoreboard);
        else view.changeStage();
    }



    /**
     *
     * The setter of finalFrenzyMode
     *
     * @param mode The mode number of finalFrenzyMode from 0 to 2
     */
    public void setOwnFinalFrenzyMode(Integer mode){
        finalFrenzyMode = mode;
    }


    /**
     *
     * Remind view to update chat message
     *
     * @param nickname The nickname of the player who send this chat
     * @param senderColor The color of the player who send this chat
     * @param message The chat message string
     */
    public synchronized void updateChat(String nickname, Color senderColor, String message){
        view.newChatMessage(nickname, senderColor, message);
    }

    /**
     *
     * A api for View to send the chat message toi server
     *
     * @param message The chat message string
     */
    public void sendChatMessage(String message) {
        connectionHandler.sendChatMessage(message);
    }

    /**
     *
     * A api for View to send Map and skull set
     *
     * @param selectedMap The map number from 1 to 4
     * @param selectedSkull The number of skull from 5 to 8
     *
     *
     */
    public void sendSettings(int selectedMap, int selectedSkull) {
        connectionHandler.sendSettings(selectedMap, selectedSkull);
    }

    /**
     *
     * A api for View to add itself to PropertyChangeListener
     *
     * @param l The PropertyChangeListener reference
     */
    public void addPropertyChangeListener(PropertyChangeListener l){
        changes.addPropertyChangeListener(l);
    }

    /**
     *
     * A api for View to remove itself from PropertyChangeListener
     *
     * @param l The PropertyChangeListener reference
     */
    public void removePropertyChangeListener(PropertyChangeListener l){
        changes.removePropertyChangeListener(l);
    }

    public PropertyChangeListener[] getProperyChangeListeners() { return changes.getPropertyChangeListeners(); }

    /**
     *
     * Remind the view the server's timer started
     *
     * @param duration The duration of the timer
     * @param comment The comment, it will remind now it's witch player's turn
     */
    public void timerStarted(Integer duration, String comment) {
        view.notifyTimer(duration, comment);
    }

    /**
     *
     * Remind the view to show valid Squares
     *
     * @param validSquares A reference of an ArrayList<Integer> witch contain all valid square
     */
    public void validSquaresInfo(ArrayList<Integer> validSquares){
        ArrayList<Integer> validSquaresInt = new ArrayList<>(validSquares.size());
        for(int i =0; i<validSquares.size(); i++){
            validSquaresInt.add(Math.round(validSquares.get(i)));
        }
        view.showValidSquares(validSquaresInt);
    }

    /**
     *
     * A api for View to send to server the run action
     *
     *
     */
    public void run() { connectionHandler.run(); }

    /**
     *
     *
     * A api for View to send to server the grab action
     *
     */
    public void grab() { connectionHandler.grab(); }

    /**
     *
     *
     * A api for View to send to server the shoot action
     *
     */
    public void shoot() { connectionHandler.shoot(); }

    /**
     *
     *
     * A api for View to send to server the go back action
     *
     */
    public void back() { connectionHandler.back(); }

    /**
     *
     * A api for View to send to server the end turn action
     *
     *
     */
    public void endTurn(){
        connectionHandler.endTurn();
    }

    /**
     *
     * A api for View to send to server the firemode selection from player's input
     *
     * @param firemode The number of firemode from 0 to 2
     *
     *
     */
    public void selectFiremode(int firemode) {
        connectionHandler.selectFiremode(firemode);
    }

    /**
     *
     * A api for View to send to server the player selection from player's input
     *
     * @param targets A reference of an ArrayList<COLOR> witch contain all target players' color
     */
    public void selectPlayers(ArrayList<Color> targets) { connectionHandler.selectPlayers(targets); }



    /**
     *
     *
     * A api for View to send to server the move sub action
     *
     */
    public void moveSubAction() { connectionHandler.moveSubAction(); }

    /**
     *
     * Remind view the connection is lost,It have to reconnect
     *
     */
    public void notifyDisconnect() {
        view.showError("Connection to the server has been lost!");
        connectionHandler=null;
    }


    /**
     *
     * The getter of FinalFrenzyMode
     *
     * @return The FinalFrenzy mode number from 0 to 2
     */
    public Integer getFinalFrenzyMode() { return finalFrenzyMode; }


    /**
     *
     * A api for View to send to server the Final Frenzy Action selection from player's input
     *
     * @param action The FinalFrenzy Action number from 0 to 2
     */
    public void selectFinalFrenzyAction(int action) { connectionHandler.selectFinalFrenzyAction(action); }
}
