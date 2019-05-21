package server.model;

import server.controller.Lobby;

import java.util.ArrayList;
import java.util.Optional;

public class SquareAmmo extends Square{

    private AmmoCard ammoTile;

    @Override
    public void acceptGrab(Lobby lobby, int actionNumber) {
        lobby.grabFromSquare(this, actionNumber);
    }

    public AmmoCard getAmmoTile() {
        return ammoTile;
    }

    public void setAmmoTile(AmmoCard card) { this.ammoTile = card; }

    @Override
    public String toString() {
        return "SquareAmmo{" +
                "color=" + color +
                '}';
    }
}
