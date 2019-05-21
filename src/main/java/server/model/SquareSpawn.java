package server.model;

import server.controller.Lobby;

import java.util.ArrayList;

public class SquareSpawn extends Square {

    private ArrayList<WeaponCard> weaponCards;

    @Override
    public void acceptGrab(Lobby lobby, int actionNumber) {
        lobby.grabFromSquare(this, actionNumber);
    }

    public ArrayList<WeaponCard> getWeaponCards() {
        return weaponCards;
    }
}
