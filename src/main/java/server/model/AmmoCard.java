
package server.model;

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
    private int numAmmoCard;      //This num for read the graphic of AmmoCard



    public int[] getAmmoContent() {
        return ammoContent;
    }

    public int getNumAmmoCard() {
        return numAmmoCard;
    }

    @Override
    public String toString() {
        return "AmmoCard{" +
                "ammoContent=" + Arrays.toString(ammoContent) +
                ", numAmmoCard=" + numAmmoCard +
                '}';
    }
}




