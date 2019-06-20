package adrenaline.server.model;

import adrenaline.server.controller.Lobby;

public class GrenadePowerup extends PowerupCard {

    public boolean isUsableOutsideTurn() { return true; }

    public String acceptUse(Lobby lobby) {
        return lobby.usePowerup(this);
    }
}
