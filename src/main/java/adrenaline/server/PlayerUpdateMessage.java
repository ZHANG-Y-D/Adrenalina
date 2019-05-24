package adrenaline.server;

import adrenaline.client.controller.Controller;
import adrenaline.client.model.Player;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

public class PlayerUpdateMessage implements UpdateMessage {
    Player clientsidePlayer;

    public PlayerUpdateMessage(adrenaline.server.model.Player serversidePlayer){
        ArrayList<Integer> weaponIDs = new ArrayList<>();
        ArrayList<Integer> powerupIDs = new ArrayList<>();

        for(WeaponCard wc : serversidePlayer.getWeaponCards()){
            weaponIDs.add(wc.getWeaponID());
        }
        for(PowerupCard puc : serversidePlayer.getPowerupCards()){
            powerupIDs.add(puc.getPowerupId());
        }

        clientsidePlayer = new Player(serversidePlayer.getColor(), serversidePlayer.getPosition(),
                                        serversidePlayer.getAmmoBox(), serversidePlayer.getDamageTrack(),
                                        serversidePlayer.getMarks(), weaponIDs, powerupIDs);
    }

    @Override
    public void applyUpdate(Controller clientController) {
        clientController.updatePlayer(clientsidePlayer);
    }
}
