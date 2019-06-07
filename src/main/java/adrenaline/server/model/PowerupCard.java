
package adrenaline.server.model;


import adrenaline.Color;
import adrenaline.server.controller.Lobby;

public abstract class PowerupCard {


    protected int powerupID;
    protected Color color;
    protected boolean usableOutsideTurn;


    public int getPowerupID(){return powerupID;}

    public Color getColor() { return color; }

    public boolean isUsableOutsideTurn() { return usableOutsideTurn; }

    @Override
    public String toString() {
        return "PowerupCard{" +
                ", color='" + color + '\'' +
                ", usableOutsideTurn=" + usableOutsideTurn +
                ", powerupID=" + powerupID +
                '}';
    }

    public abstract String acceptUse(Lobby lobby);
}
