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
    public void acceptConvertInfo(MapUpdateMessage updatemsg, int index) {
        int ammoID;
        if(ammoTile == null) ammoID = 0;
        else ammoID = ammoTile.getAmmoID();
        updatemsg.addAmmoInfo(index, ammoID);
    }

    @Override
    public void setCard(Lobby lobby) {
        if(ammoTile==null) lobby.setAmmoCard(this);
    }

    public AmmoCard getAmmoTile() {
        AmmoCard ammoTile = this.ammoTile;
        this.ammoTile = null;
        map.notifyObservers(new MapUpdateMessage(map));
        return ammoTile;
    }

    public void setAmmoTile(AmmoCard card) {
        this.ammoTile = card;
        if(map.anyObserver()) map.notifyObservers(new MapUpdateMessage(map));
    }
}
