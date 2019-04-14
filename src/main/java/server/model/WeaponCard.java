package Server.Model;

import java.util.ArrayList;

public class WeaponCard {
    private String name;
    private String ammoCost;
    private boolean loaded;
    private ArrayList<Firemode> firemodes;

    public WeaponCard() {
        //TODO
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
