
package adrenaline.server.model;


import adrenaline.Color;
import adrenaline.server.controller.Lobby;

public abstract class PowerupCard {
    protected int powerupID;
    protected Color color;

    public int getPowerupID(){return powerupID;}

    public Color getColor() { return color; }

    public abstract boolean isUsableOutsideTurn();

    @Override
    public String toString() {
        return "PowerupCard{" +
                ", color='" + color + '\'' +
                ", powerupID=" + powerupID +
                '}';
    }

    public abstract String acceptUse(Lobby lobby);
}
