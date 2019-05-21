package server.model;



/*
 *
 *
 *   Responsible： RICCARDO for functionnality
 *                 ZHANG YUEDONG for Informations
 *
 *
 */


import java.util.ArrayList;
import java.util.Arrays;

public class WeaponCard {

    private final String name;
    private final int[] ammoCost;     //Seq : red blue yellow powerup
    private final Color gratisAmmo;
    private final String manual;
    private boolean loaded;     //true:loaded, false: not ready loaded. Initial state is true.
    private final int numWeaponCard;
    private ArrayList<Firemode> firemodes;


    public WeaponCard(String name, int[] ammoCost, Color gratisAmmo, String manual, int numWeaponCard, ArrayList<Firemode> firemodes) {
        this.name = name;
        this.ammoCost = ammoCost;
        this.gratisAmmo = gratisAmmo;
        this.manual = manual;
        this.loaded = true;
        this.numWeaponCard = numWeaponCard;
        this.firemodes = firemodes;
    }

    public void reload() /*throws AlreadyLoadedException*/ {
        //TODO
    }

    public int[] getAmmoCost() {
        return ammoCost;
    }

    public Color getGratisAmmo() {
        return gratisAmmo;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void FiremodeShoot() {
        //invoked by the controller
        // handles the selection of the desired firemode and returns it to the controller for use
        //TODO
    }



    @Override
    public String toString() {
        String string = "WeaponCard{" +
                "name='" + name + '\'' +
                ", ammoCost=" + Arrays.toString(ammoCost) +
                ", gratisAmmo=" + gratisAmmo +
                ", manual='" + manual + '\'' +
                ", loaded=" + loaded +
                ", numWeaponCard=" + numWeaponCard;

        /* Temporarily closed because it is not completed
        for(Firemode fm : firemodes) {
            string += "\n\t" + fm.toString();
        }
         */

        string += "\n}";
        return string;
    }



}
