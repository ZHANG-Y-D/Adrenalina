package server.model;

import java.util.ArrayList;

public class WeaponCard {
    private String name;
    private int[] ammoCost;
    private boolean loaded;
    private ArrayList<Firemode> firemodes;

    public WeaponCard(String name, int[] ammoCost, ArrayList<Firemode> firemodes) {
        this.name = name;
        this.ammoCost = ammoCost;
        this.firemodes = firemodes;
        this.loaded = true;

    }

    public void reload() /*throws AlreadyLoadedException*/ {
        //TODO
    }

    public void FiremodeShoot() {
        //invoked by the controller
        // handles the selection of the desired firemode and returns it to the controller for use
        //TODO
    }


}
