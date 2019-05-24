package adrenaline.server.model;

import adrenaline.server.MapUpdateMessage;
import adrenaline.server.controller.Lobby;

public class SquareAmmo extends Square{

    private AmmoCard ammoTile;

    @Override
    public void acceptGrab(Lobby lobby, int actionNumber) {
        lobby.grabFromSquare(this, actionNumber);
    }

    @Override
    public void acceptConvertInfo(MapUpdateMessage updatemsg, int index) { updatemsg.addAmmoInfo(index, ammoTile.getAmmoID()); }

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
