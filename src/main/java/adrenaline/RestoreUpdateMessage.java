package adrenaline;

import adrenaline.client.controller.GameController;
import adrenaline.server.model.Map;
import adrenaline.server.model.Player;
import adrenaline.server.model.ScoreBoard;

import java.util.ArrayList;

public class RestoreUpdateMessage implements UpdateMessage {
    MapUpdateMessage restoreMap = null;
    ArrayList<PlayerUpdateMessage> restorePlayers = new ArrayList<>();
    ScoreboardUpdateMessage restoreScoreboard;

    public RestoreUpdateMessage(Map serversideMap, ArrayList<Player> serversidePlayers, ScoreBoard serversideScoreboard){
        if(serversideMap!=null) restoreMap = new MapUpdateMessage(serversideMap);
        serversidePlayers.forEach(x -> restorePlayers.add(new PlayerUpdateMessage(x)));
        restoreScoreboard = new ScoreboardUpdateMessage(serversideScoreboard);
    }

    @Override
    public void applyUpdate(GameController clientGameController) {
        if(restoreMap!=null) restoreMap.applyUpdate(clientGameController);
        restorePlayers.forEach(x -> x.applyUpdate(clientGameController));
        restoreScoreboard.applyUpdate(clientGameController);
    }
}
