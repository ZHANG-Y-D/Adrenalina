package adrenaline.client;

import adrenaline.UpdateMessage;
import adrenaline.client.controller.GameController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientCommands extends UnicastRemoteObject implements ClientAPI{

    private RMIHandler client;
    private GameController gameController;

    public RMIClientCommands(RMIHandler client, GameController gameController) throws RemoteException {
        this.client = client;
        this.gameController = gameController;
    }
    @Override
    public void setLobby(String lobbyID) throws RemoteException {
        client.setMyLobby(lobbyID);
        gameController.changeStage();
    }

    @Override
    public void update(UpdateMessage updatemsg) throws RemoteException {
        updatemsg.applyUpdate(gameController);
    }


}
