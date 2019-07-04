package adrenaline;

import adrenaline.client.controller.GameController;
import adrenaline.server.model.ScoreBoard;

import java.util.HashMap;


/**
 *
 * To update scoreboard massage.The realize of observer pattern
 *
 */
public class ScoreboardUpdateMessage implements UpdateMessage {
    private adrenaline.client.model.ScoreBoard clientsideScoreboard = null;
    private HashMap<Color,Integer> finalfrenzyMode;

    /**
     *
     *
     * For update the message
     * @param serversideScoreboard
     */
    public ScoreboardUpdateMessage(ScoreBoard serversideScoreboard){
        clientsideScoreboard = new adrenaline.client.model.ScoreBoard(serversideScoreboard.getScoreMap(),serversideScoreboard.getDiminValues(),
                serversideScoreboard.getKillshotTrack(), serversideScoreboard.getOverkillFlags(), serversideScoreboard.getMaxKills(),
                serversideScoreboard.getFinalPlayerPositions());
        finalfrenzyMode = serversideScoreboard.getFinalfrenzyModePlayers();
    }

    /**
     *
     *
     * For update the message
     * @param clientGameController The game controller if client
     */
    @Override
    public void applyUpdate(GameController clientGameController) {
        clientGameController.setOwnFinalFrenzyMode(finalfrenzyMode.getOrDefault(clientGameController.getOwnColor(),0));
        clientGameController.updateScoreboard(clientsideScoreboard);
    }
}
