

/*
 *  This is class for single AmmoCard.
 *
 *  Author Zhang YueDong
 *
 *
 *
 *  Attention from Zhang Yuedong:
 *
 *     Because the Gson or Other Json API,read object is the mode
 *     "no-argument (default) construtor to instantiate an object"
 *     SO now we just memory AmmoCard Like a char array, when use time
 *     we can use the getter method  getAmmoContent to get a int array
 *
 *
 *
 */



package server.model;


import java.util.Arrays;

public class AmmoCard {

    private int[] ammoContent;   //Seq: r b y p   r=red b=blue y=yellow p=powerup
    private int numAmmoCard;      //This num for read the graphic of AmmoCard


    public AmmoCard(int[] ammoContent, int numAmmoCard) {
        this.ammoContent = ammoContent;
        this.numAmmoCard = numAmmoCard;
    }


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




