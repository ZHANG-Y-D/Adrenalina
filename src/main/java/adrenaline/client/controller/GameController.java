package adrenaline.client.controller;

import adrenaline.Color;
import adrenaline.client.ConnectionHandler;
import adrenaline.client.RMIHandler;
import adrenaline.client.SocketHandler;
import adrenaline.client.model.Map;
import adrenaline.client.model.Player;
import adrenaline.client.model.ScoreBoard;
import adrenaline.client.view.ViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class GameController {

    private int mapID;

    private LinkedHashMap<String, Color> playersNicknames;
    private ScoreBoard scoreBoard;
    private Map map;
    private HashMap<Color, Player> playersMap;
    private ViewInterface view;
    private ConnectionHandler connectionHandler;


    public HashMap<String, Color> getPlayersNicknames(){ return playersNicknames; }

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
        playersNicknames = new LinkedHashMap<>();
        nicknames.forEach(x -> playersNicknames.put(x, Color.WHITE));
    }

    public void setPlayerColor(String nickname, Color color){
        playersNicknames.put(nickname, color);
        view.notifyView();
    }

    public void selectAvatar(Color color){ connectionHandler.selectAvatar(color); }

    public void cleanExit(){
        if(connectionHandler != null) connectionHandler.unregister();
    }


    public void handleReturn(String returnMsg){
        view.setReturnValueFromServer(returnMsg);
        if(!returnMsg.equals("OK")) view.showError(returnMsg);
    }

    public void changeStage(){ view.changeStage(); }

    public void updatePlayer(Player newPlayer){
        playersMap.put(newPlayer.getColor(), newPlayer);
    }

    public void updateMap(Map newMap){
        map = newMap;
    }

    public void updateScoreBoard(ScoreBoard newScoreBoard){
        scoreBoard = newScoreBoard;
    }
}
