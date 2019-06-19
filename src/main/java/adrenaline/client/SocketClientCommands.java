package adrenaline.client;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.client.controller.GameController;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

public class SocketClientCommands  implements ClientAPI{

    private SocketHandler client;
    private GameController gameController;

    SocketClientCommands(SocketHandler client, GameController gameController){
        this.client = client;
        this.gameController = gameController;
    }

    public void setNickname(String nickname) {
        gameController.setOwnNickname(nickname);
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

    public void validSquaresInfo(ArrayList<Integer> validSquares) {
        ArrayList<Integer> typesafeValidSquare = new ArrayList<>();
        for(int i=0; i< validSquares.size(); i++) typesafeValidSquare.add(((Number)validSquares.get(i)).intValue());
        gameController.validSquaresInfo(typesafeValidSquare);
    }

    public void update(UpdateMessage updatemsg){
        updatemsg.applyUpdate(gameController);
    }


}
