package adrenaline.client;

import adrenaline.client.controller.GameController;
import adrenaline.server.UpdateMessage;

import java.rmi.RemoteException;

public class SocketClientCommands  implements ClientAPI{

    private SocketHandler client;
    private GameController gameController;

    public SocketClientCommands(SocketHandler client, GameController gameController){
        this.client = client;
        this.gameController = gameController;
    }

    @Override
    public void setLobby(String lobbyID){
        client.setMyLobby(lobbyID);
        gameController.changeStage();
    }

    @Override
    public void update(UpdateMessage updatemsg) throws RemoteException {
        updatemsg.applyUpdate(gameController);
    }
}
