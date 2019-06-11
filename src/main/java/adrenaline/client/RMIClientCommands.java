package adrenaline.client;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.client.controller.GameController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIClientCommands extends UnicastRemoteObject implements ClientAPI{

    private RMIHandler client;
    private GameController gameController;

    RMIClientCommands(RMIHandler client, GameController gameController) throws RemoteException {
        this.client = client;
        this.gameController = gameController;
    }

    public void setLobby(String lobbyID, ArrayList<String> nicknames) {
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

    public void update(UpdateMessage updatemsg) {
        updatemsg.applyUpdate(gameController);
    }


}
