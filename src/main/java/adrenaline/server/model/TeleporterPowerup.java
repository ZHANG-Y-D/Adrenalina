package adrenaline.server.model;

import adrenaline.server.controller.Lobby;

public class TeleporterPowerup extends PowerupCard {
    public boolean isUsableOutsideTurn() { return false; }
    public String acceptUse(Lobby lobby) {
        return lobby.usePowerup(this);
    }
}
