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
 *
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
     *
     *
     * @return
     */
    public LinkedHashMap<String, Color> getPlayersNicknames() { return playersNicknames; }

    /**
     *
     *
     *
     * @return
     */
    public HashMap<Color, Player> getPlayersMap() { return playersMap; }

    /**
     *
     *
     *
     * @return
     */
    public Map getMap(){ return map; }

    /**
     *
     *
     *
     * @return
     */
    public ScoreBoard getScoreBoard() { return scoreBoard; }

    /**
     *
     *
     *
     * @return
     */
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    /**
     *
     *
     *
     * @param name
     */
    public void setOwnNickname(String name) {
        ownNickname = name;
        view.showMessage("NICKNAME;"+name);
    }

    /**
     *
     *
     *
     * @return
     */
    public String getOwnNickname() { return ownNickname; }

    /**
     *
     *
     *
     * @return
     */
    public Color getOwnColor() { return  ownColor; }

    /**
     *
     *
     *
     * @param viewController
     */
    public void setViewController(ViewInterface viewController){
        this.view = viewController;
    }

    /**
     *
     *
     *
     * @param host
     * @param port
     * @return
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
     *
     *
     * @param host
     * @param port
     * @return
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
     *
     *
     *
     * @param nickname
     */
    public void setNickname(String nickname){ connectionHandler.setNickname(nickname);}

    /**
     *
     *
     *
     *
     * @param nicknames
     */
    public void initPlayersNicknames(ArrayList<String> nicknames){
        nicknames.forEach(x -> playersNicknames.put(x, Color.WHITE));
    }

    /**
     *
     *
     *
     *
     * @param nickname
     * @param color
     */
    public void setPlayerColor(String nickname, Color color){
        if(nickname.equals(ownNickname)) ownColor = color;
        playersMap.put(color, new Player());
        changes.firePropertyChange("nicknamesColor", playersNicknames, playersNicknames.put(nickname, color));
    }

    /**
     *
     *
     *
     *
     * @param color
     */
    public void selectAvatar(Color color){ connectionHandler.selectAvatar(color); }

    /**
     *
     *
     *
     *
     * @param powerupID
     */
    public void selectPowerUp(int powerupID){ connectionHandler.selectPowerUp(powerupID); }

    /**
     *
     *
     *
     * @param weaponID
     */
    public void selectWeapon(int weaponID) { connectionHandler.selectWeapon(weaponID); }

    /**
     *
     *
     *
     * @param index
     */
    public void selectSquare(int index) { connectionHandler.selectSquare(index); }


    /**
     * For set ammo box
     *
     * @param color The ammo box color
     */
    public void selectAmmo(Color color) { connectionHandler.selectAmmo(color); }

    /**
     *
     *
     *
     *
     */
    public void cleanExit(){
        if(connectionHandler != null) connectionHandler.unregister();
    }

    /**
     *
     *
     *
     *
     * @param returnMsg
     */
    public void handleReturn(String returnMsg){
        if(!returnMsg.contains("OK")) view.showError(returnMsg);
        else if(returnMsg.length() > 2) view.showMessage(returnMsg.substring(3));
    }

    /**
     *
     *
     *
     *
     */
    public void changeStage(){
        view.changeStage();
    }

    /**
     *
     *
     *
     * @param newPlayer
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
     *
     *
     *
     * @param newMap
     */
    public synchronized void updateMap(Map newMap){
        Map oldMap = map;
        map = newMap;
        changes.firePropertyChange("map", oldMap, newMap);
    }

    /**
     *
     *
     * @param newScoreboard
     */
    public void updateScoreboard(ScoreBoard newScoreboard) {
        ScoreBoard oldScoreboard = scoreBoard;
        scoreBoard = newScoreboard;
        changes.firePropertyChange("scoreboard", oldScoreboard, newScoreboard);
    }

    /**
     *
     *
     *
     * @param mode
     */
    public void setOwnFinalFrenzyMode(Integer mode){
        finalFrenzyMode = mode;
    }


    /**
     *
     *
     * @param nickname
     * @param senderColor
     * @param message
     */
    public synchronized void updateChat(String nickname, Color senderColor, String message){
        view.newChatMessage(nickname, senderColor, message);
    }

    /**
     *
     *
     *
     * @param message
     */
    public void sendChatMessage(String message) {
        connectionHandler.sendChatMessage(message);
    }

    /**
     *
     *
     *
     * @param l
     */
    public void addPropertyChangeListener(PropertyChangeListener l){
        changes.addPropertyChangeListener(l);
    }

    /**
     *
     *
     * @param l
     */
    public void removePropertyChangeListener(PropertyChangeListener l){
        changes.removePropertyChangeListener(l);
    }

    /**
     *
     * @param newScoreBoard
     */
    public synchronized void updateScoreBoard(ScoreBoard newScoreBoard){
        scoreBoard = newScoreBoard;
    }

    /**
     *
     * @param duration
     * @param comment
     */
    public void timerStarted(Integer duration, String comment) {
        view.notifyTimer(duration, comment);
    }

    /**
     *
     *
     * @param validSquares
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
     *
     *
     *
     */
    public void sendSettings(int selectedMap, int selectedSkull) {
        connectionHandler.sendSettings(selectedMap, selectedSkull);
    }

    /**
     *
     *
     *
     *
     */
    public void run() { connectionHandler.run(); }

    /**
     *
     *
     *
     *
     */
    public void grab() { connectionHandler.grab(); }

    /**
     *
     *
     *
     *
     */
    public void shoot() { connectionHandler.shoot(); }

    /**
     *
     *
     *
     *
     */
    public void back() { connectionHandler.back(); }

    /**
     *
     *
     *
     *
     */
    public void endTurn(){
        connectionHandler.endTurn();
    }

    /**
     *
     *
     *
     *
     */
    public void selectFiremode(int firemode) {
        connectionHandler.selectFiremode(firemode);
    }

    /**
     *
     *
     *
     *
     */
    public void selectPlayers(ArrayList<Color> targets) { connectionHandler.selectPlayers(targets); }

    /**
     *
     *
     *
     *
     */
    public void moveSubAction() { connectionHandler.moveSubAction(); }

    /**
     *
     *
     *
     *
     */
    public void notifyDisconnect() {
        view.showError("Connection to the server has been lost!");
        connectionHandler=null;
    }


    /**
     *
     *
     * @return
     */
    public Integer getFinalFrenzyMode() { return finalFrenzyMode; }


    /**
     *
     *
     *
     * @param action
     */
    public void selectFinalFrenzyAction(int action) { connectionHandler.selectFinalFrenzyAction(action); }
}
