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


public class GameController {

    private LinkedHashMap<String, Color> playersNicknames = new LinkedHashMap<>();
    private String ownNickame;
    private Color ownColor;

    private HashMap<Color, Player> playersMap = new HashMap<>();
    private ScoreBoard scoreBoard;
    private Map map;
    private ViewInterface view;
    private ConnectionHandler connectionHandler;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);


    public LinkedHashMap<String, Color> getPlayersNicknames() { return playersNicknames; }

    public HashMap<Color, Player> getPlayersMap() { return playersMap; }

    public Map getMap(){ return map; }

    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public void setOwnNickname(String name) {
        ownNickame = name;
    }

    public String getOwnNickname() { return ownNickame; }

    public Color getOwnColor() { return  ownColor; }

    public void setViewController(ViewInterface viewController){
        this.view = viewController;
    }


    public boolean connectRMI(String host, int port){
        try {
            connectionHandler = new RMIHandler(host,port,this);
            return true;
        } catch (Exception e) {
            view.showError("Wrong host/port");
            return false;
        }
    }


    public boolean connectSocket(String host, int port){
        try {
            connectionHandler = new SocketHandler(host,port,this);
            return true;
        } catch (Exception e) {
            view.showError("Wrong host/port");
            return false;
        }
    }

    public void setNickname(String nickname){ connectionHandler.setNickname(nickname);}

    public void initPlayersNicknames(ArrayList<String> nicknames){
        nicknames.forEach(x -> playersNicknames.put(x, Color.WHITE));
    }

    public void setPlayerColor(String nickname, Color color){
        if(nickname.equals(ownNickame)) ownColor = color;
        playersMap.put(color, new Player());
        changes.firePropertyChange("nicknamesColor", playersNicknames, playersNicknames.put(nickname, color));
    }

    public void selectAvatar(Color color){ connectionHandler.selectAvatar(color); }

    public void selectPowerUp(int powerupID){ connectionHandler.selectPowerUp(powerupID); }

    public void selectWeapon(int weaponID) { connectionHandler.selectWeapon(weaponID); }

    public void selectSquare(int index) { connectionHandler.selectSquare(index); }

    public void cleanExit(){
        if(connectionHandler != null) connectionHandler.unregister();
    }

    public void handleReturn(String returnMsg){
        if(!returnMsg.contains("OK")) view.showError(returnMsg);
        else if(returnMsg.length() > 2) view.showMessage(returnMsg.substring(3));
    }

    public void changeStage(){ view.changeStage(); }

    public synchronized void updatePlayer(Player newPlayer){
        HashMap<Color, Player> oldPlayersMap = playersMap;
        HashMap<Color, Player> newPlayersMap = new HashMap<>(playersMap);
        newPlayersMap.put(newPlayer.getColor(), newPlayer);
        playersMap = newPlayersMap;
        changes.firePropertyChange("player", oldPlayersMap, newPlayersMap);
    }

    public synchronized void updateMap(Map newMap){
        Map oldMap = map;
        map = newMap;
        changes.firePropertyChange("map", oldMap, newMap);
    }

    public synchronized void updateChat(String nickname, Color senderColor, String message){
        view.newChatMessage(nickname, senderColor, message);
    }

    public void sendChatMessage(String message) {
        connectionHandler.sendChatMessage(message);
    }

    public void addPropertyChangeListener(PropertyChangeListener l){
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l){
        changes.removePropertyChangeListener(l);
    }

    public synchronized void updateScoreBoard(ScoreBoard newScoreBoard){
        scoreBoard = newScoreBoard;
    }

    public void timerStarted(Integer duration, String comment) {
        view.notifyTimer(duration, comment);
    }

    public void validSquaresInfo(ArrayList<Integer> validSquares){
        ArrayList<Integer> validSquaresInt = new ArrayList<>(validSquares.size());
        for(int i =0; i<validSquares.size(); i++){
            validSquaresInt.add(Math.round(validSquares.get(i)));
        }
        view.showValidSquares(validSquaresInt);
    }

    public void sendSettings(int selectedMap, int selectedSkull) {
        connectionHandler.sendSettings(selectedMap, selectedSkull);
    }

    public void run() { connectionHandler.run(); }

    public void grab() { connectionHandler.grab(); }

    public void shoot() { connectionHandler.shoot(); }

    public void back() { connectionHandler.back(); }

    public void endTurn(){
        connectionHandler.endTurn();
    }

}
