
package adrenaline.server.model;

/*
 *
 *
 *
 *  Responsible:Zhang Yuedong
 *
 *
 */

import java.util.Arrays;

public class AmmoCard {

    private int[] ammoContent;   //Seq: r b y p   r=red b=blue y=yellow p=powerup
    private int ammoID;      //This num for read the graphic of AmmoCard



    public int[] getAmmoContent() {
        return ammoContent;
    }

    public int getAmmoID() {
        return ammoID;
    }

    @Override
    public String toString() {
        return "AmmoCard{" +
                "ammoContent=" + Arrays.toString(ammoContent) +
                ", ammoID=" + ammoID +
                '}';
    }
}




