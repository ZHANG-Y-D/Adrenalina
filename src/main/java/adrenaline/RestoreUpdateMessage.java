package adrenaline;

import adrenaline.client.controller.GameController;
import adrenaline.server.model.Map;
import adrenaline.server.model.Player;
import adrenaline.server.model.ScoreBoard;

import java.util.ArrayList;


/**
 *
 * To update restore massage.The realize of observer pattern
 *
 */
public class RestoreUpdateMessage implements UpdateMessage {
    MapUpdateMessage restoreMap = null;
    ArrayList<PlayerUpdateMessage> restorePlayers = new ArrayList<>();
    ScoreboardUpdateMessage restoreScoreboard;

    /**
     *
     * For update the message
     * @param serversideMap The server side map
     * @param serversidePlayers The server side player
     * @param serversideScoreboard The score board of server side
     */
    public RestoreUpdateMessage(Map serversideMap, ArrayList<Player> serversidePlayers, ScoreBoard serversideScoreboard){
        if(serversideMap!=null) restoreMap = new MapUpdateMessage(serversideMap);
        serversidePlayers.forEach(x -> restorePlayers.add(new PlayerUpdateMessage(x)));
        restoreScoreboard = new ScoreboardUpdateMessage(serversideScoreboard);
    }

    /**
     *
     * For update the message
     * @param clientGameController The game controller if client
     */
    @Override
    public void applyUpdate(GameController clientGameController) {
        if(restoreMap!=null) restoreMap.applyUpdate(clientGameController);
        restorePlayers.forEach(x -> x.applyUpdate(clientGameController));
        restoreScoreboard.applyUpdate(clientGameController);
    }
}
