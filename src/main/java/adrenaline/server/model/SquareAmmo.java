package adrenaline.server.model;

import adrenaline.server.controller.Lobby;
import adrenaline.MapUpdateMessage;

public class SquareAmmo extends Square{

    private AmmoCard ammoTile;

    @Override
    public void acceptGrab(Lobby lobby) {
        lobby.grabFromSquare(this);
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
