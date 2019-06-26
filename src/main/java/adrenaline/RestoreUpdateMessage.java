package adrenaline;

import adrenaline.client.controller.GameController;
import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;

public class RestoreUpdateMessage implements UpdateMessage {
    MapUpdateMessage restoreMap = null;
    ArrayList<PlayerUpdateMessage> restorePlayers = new ArrayList<>();
    //TODO scoreboard too

    public RestoreUpdateMessage(Map serversideMap, ArrayList<Player> serversidePlayers){
        if(serversideMap!=null) restoreMap = new MapUpdateMessage(serversideMap);
        serversidePlayers.forEach(x -> restorePlayers.add(new PlayerUpdateMessage(x)));
    }

    @Override
    public void applyUpdate(GameController clientGameController) {
        if(restoreMap!=null) restoreMap.applyUpdate(clientGameController);
        restorePlayers.forEach(x -> x.applyUpdate(clientGameController));
    }
}
