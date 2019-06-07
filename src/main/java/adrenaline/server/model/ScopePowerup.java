package adrenaline.server.model;

import adrenaline.server.controller.Lobby;

public class ScopePowerup extends PowerupCard {

    public String acceptUse(Lobby lobby) {
        return lobby.usePowerup(this);
    }
}
