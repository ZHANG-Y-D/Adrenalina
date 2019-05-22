package server.model;

import server.controller.Lobby;

import java.util.ArrayList;

public class SquareSpawn extends Square {

    private ArrayList<WeaponCard> weaponCards;

    @Override
    public void acceptGrab(Lobby lobby, int actionNumber) {
        lobby.grabFromSquare(this, actionNumber);
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
