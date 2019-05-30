package adrenaline.client;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.client.controller.GameController;
import javafx.application.Platform;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class SocketClientCommands  implements ClientAPI{

    private SocketHandler client;
    private GameController gameController;

    public SocketClientCommands(SocketHandler client, GameController gameController){
        this.client = client;
        this.gameController = gameController;
    }

    @Override
    public void setLobby(String lobbyID, ArrayList<String> nicknames){
        client.setMyLobby(lobbyID);
        gameController.changeStage();
        gameController.initPlayersNicknames(nicknames);
    }

    @Override
    public void setPlayerColor(String nickname, Color color) throws RemoteException {
        gameController.setPlayerColor(nickname, color);
    }

    @Override
    public void update(UpdateMessage updatemsg){
        updatemsg.applyUpdate(gameController);
    }


}
