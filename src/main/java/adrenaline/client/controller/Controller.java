package adrenaline.client.controller;

import adrenaline.Color;
import adrenaline.client.model.*;
import adrenaline.server.UpdateMessage;

import java.util.HashMap;

public class Controller {
    private int mapID;

    private ScoreBoard scoreBoard;
    private Map map;
    private HashMap<Color, Player> playersMap;

    public void update(UpdateMessage updatemsg){
        updatemsg.applyUpdate(this);
    }

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
