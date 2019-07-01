package adrenaline;

import adrenaline.client.controller.GameController;
import adrenaline.server.model.ScoreBoard;

import java.util.HashMap;

public class ScoreboardUpdateMessage implements UpdateMessage {
    private adrenaline.client.model.ScoreBoard clientsideScoreboard = null;
    private HashMap<Color,Integer> finalfrenzyMode = null;

    public ScoreboardUpdateMessage(ScoreBoard serversideScoreboard){
        clientsideScoreboard = new adrenaline.client.model.ScoreBoard(serversideScoreboard.getScoreMap(),serversideScoreboard.getDiminValues(),
                serversideScoreboard.getKillshotTrack(), serversideScoreboard.getOverkillFlags());
        if(serversideScoreboard.isFinalFrenzy()) finalfrenzyMode = serversideScoreboard.getFinalfrenzyModePlayers();
    }

    @Override
    public void applyUpdate(GameController clientGameController) {
        clientGameController.setOwnFinalfrenzyMode(finalfrenzyMode.getOrDefault(clientGameController.getOwnColor(),0));
        clientGameController.updateScoreboard(clientsideScoreboard);
    }
}
