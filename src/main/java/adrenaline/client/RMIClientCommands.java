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

    public RMIClientCommands(RMIHandler client, GameController gameController) throws RemoteException {
        this.client = client;
        this.gameController = gameController;
    }
    @Override
    public void setLobby(String lobbyID, ArrayList<String> nicknames) throws RemoteException {
        client.setMyLobby(lobbyID);
        gameController.changeStage();
        gameController.initPlayersNicknames(nicknames);
    }

    @Override
    public void setPlayerColor(String nickname, Color color) throws RemoteException {
        gameController.setPlayerColor(nickname, color);
    }

    @Override
    public void update(UpdateMessage updatemsg) throws RemoteException {
        updatemsg.applyUpdate(gameController);
    }


}
