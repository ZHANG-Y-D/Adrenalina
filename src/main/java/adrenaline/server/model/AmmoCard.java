
package adrenaline.server.model;

/**
 *
 * The server side model AmmoCard
 * Read attitude from json file
 *
 *
 */
public class AmmoCard {

    private int[] ammoContent;   //Seq: r b y p   r=red b=blue y=yellow p=powerup
    private int ammoID;      //This num for read the graphic of AmmoCard

    /**
     *
     * The getter of ammoContent
     *
     * @return The array of ammoContent
     */
    public int[] getAmmoContent() {
        return ammoContent;
    }

    /**
     *
     * The getter of ammoID
     *
     * @return ammoID value
     */
    public int getAmmoID() {
        return ammoID;
    }
}




