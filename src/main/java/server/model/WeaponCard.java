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

    private String name;
    private int[] ammoCost;     //Seq : red blue yellow powerup,
                                //Attention the position powerup just for become same array with AmmoCard
    private Color gratisAmmo;
    private String manual;
    private boolean loaded;     //true:loaded, falso: not ready loaded. Initial state is true.
    private int numWeaponCard;
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
        for(Firemode fm : firemodes) {
            string += "\n\t" + fm.toString();
        }
        string += "\n}";
        return string;
    }



}
