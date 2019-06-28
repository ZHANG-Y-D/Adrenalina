package adrenaline;

import adrenaline.client.controller.GameController;
import adrenaline.server.model.ScoreBoard;

public class ScoreboardUpdateMessage implements UpdateMessage {
    adrenaline.client.model.ScoreBoard clientsideScoreboard = null;

    public ScoreboardUpdateMessage(ScoreBoard serversideScoreboard){
        clientsideScoreboard = new adrenaline.client.model.ScoreBoard(serversideScoreboard.getDiminValues(),
                serversideScoreboard.getKillshotTrack(), serversideScoreboard.getOverkillFlags());
    }

    @Override
    public void applyUpdate(GameController clientGameController) { clientGameController.updateScoreboard(clientsideScoreboard);}
}
