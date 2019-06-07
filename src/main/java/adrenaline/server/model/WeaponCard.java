package adrenaline.server.model;


import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsConstraint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class WeaponCard {

    private int weaponID;
    private final String name;
    private final int[] ammoCost;     //Seq : red blue yellow
    private final Color freeAmmo;
    private final String manual;
    private boolean loaded;     //true:loaded, false: not ready loaded. Initial state is true.
    private final int numWeaponCard;
    private ArrayList<Firemode> firemodes;


    public WeaponCard(String name, int[] ammoCost, Color gratisAmmo, String manual, int numWeaponCard, ArrayList<Firemode> firemodes) {
        this.name = name;
        this.ammoCost = ammoCost;
        this.freeAmmo = gratisAmmo;
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

    public Color getFreeAmmo() {
        return freeAmmo;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public Firemode getFiremode(int index) throws NullPointerException{
        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(RangeConstraint.class, new CustomSerializer())
                .registerTypeAdapter(TargetsConstraint.class, new CustomSerializer());
        Gson gson = gsonBld.create();
        Firemode deepCopy = gson.fromJson(gson.toJson(firemodes.get(index)), Firemode.class);
        return deepCopy;
    }



    @Override
    public String toString() {
        String string = "WeaponCard{" +
                "name='" + name + '\'' +
                ", ammoCost=" + Arrays.toString(ammoCost) +
                ", freeAmmo=" + freeAmmo +
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


    public int getWeaponID() { return weaponID; }
}
