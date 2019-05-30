package adrenaline.client;

import adrenaline.UpdateMessage;
import adrenaline.client.controller.GameController;

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
    public void update(UpdateMessage updatemsg){
        updatemsg.applyUpdate(gameController);
    }


}
