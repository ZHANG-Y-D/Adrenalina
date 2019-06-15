package adrenaline.client;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.client.controller.GameController;

import java.util.ArrayList;

public class SocketClientCommands  implements ClientAPI{

    private SocketHandler client;
    private GameController gameController;

    SocketClientCommands(SocketHandler client, GameController gameController){
        this.client = client;
        this.gameController = gameController;
    }

    public void setLobby(String lobbyID, ArrayList<String> nicknames){
        client.setMyLobby(lobbyID);
        gameController.changeStage();
        gameController.initPlayersNicknames(nicknames);
    }

    public void setPlayerColor(String nickname, Color color) {
        gameController.setPlayerColor(nickname, color);
    }

    public void timerStarted(Integer duration, String comment) {
        gameController.timerStarted(duration, comment);
    }

    public void validSquaresInfo(ArrayList<Integer> validSquares) { gameController.validSquaresInfo(validSquares); }

    public void update(UpdateMessage updatemsg){
        updatemsg.applyUpdate(gameController);
    }


}
