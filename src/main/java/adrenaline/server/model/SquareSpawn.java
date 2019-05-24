package adrenaline.server.model;

import adrenaline.server.MapUpdateMessage;
import adrenaline.server.controller.Lobby;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SquareSpawn extends Square {

    private ArrayList<WeaponCard> weaponCards;

    @Override
    public void acceptGrab(Lobby lobby, int actionNumber) {
        lobby.grabFromSquare(this, actionNumber);
    }

    @Override
    public void acceptConvertInfo(MapUpdateMessage updatemsg, int index) {
        updatemsg.addWeaponInfo(color, (ArrayList<Integer>) weaponCards.stream().map(WeaponCard::getWeaponID).collect(Collectors.toList()));
    }

    public WeaponCard getWeaponCard(int weaponID) {
        for(WeaponCard wc : weaponCards){
            if(wc.getWeaponID() == weaponID) return wc;
        }
        return null;
    }

    public boolean removeCard(WeaponCard card){ return weaponCards.remove(card);}
    public void addCard(WeaponCard card){weaponCards.add(card);}
}
