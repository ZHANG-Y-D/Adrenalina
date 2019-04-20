package server.model;


import java.util.ArrayList;
import java.util.Arrays;

public class WeaponCard {

    private String name;
    private int[] ammoCost;     //Seq : red blue yellow powerup,
                                //Attention the position powerup just for become same array with AmmoCard
    private Color freeAmmo;
    private String manual;
    private boolean loaded;     //true:loaded, falso: not ready loaded. Initial state is true.
    private int numWeaponCard;
    private ArrayList<Firemode> firemodes;


    public WeaponCard(String name, int[] ammoCost, Color freeAmmo, String manual, int numWeaponCard, ArrayList<Firemode> firemodes) {
        this.name = name;
        this.ammoCost = ammoCost;
        this.freeAmmo = freeAmmo;
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
    public java.lang.String toString() {
        return "WeaponCard{" +
                "name=" + name +
                ", ammoCost=" + Arrays.toString(ammoCost) +
                ", freeAmmo=" + freeAmmo +
                ", manual=" + manual +
                ", loaded=" + loaded +
                ", numWeaponCard=" + numWeaponCard +
                '}';
    }
}
