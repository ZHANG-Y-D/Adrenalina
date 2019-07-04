package adrenaline.client.model;

import adrenaline.Color;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * The client side Map Model
 *
 *
 */
public class Map implements Serializable {

    private int mapID;
    private HashMap<Color, ArrayList<Integer>> weaponMap;
    private HashMap<Integer, Integer> ammoMap;

    /**
     *
     * The constructor of this class, To set all attitude
     *
     * @param weaponMap
     * @param ammoMap
     * @param mapID
     */
    public Map(HashMap<Color, ArrayList<Integer>> weaponMap, HashMap<Integer, Integer> ammoMap, int mapID){
        this.weaponMap = weaponMap;
        this.ammoMap = ammoMap;
        this.mapID = mapID;
    }





    /**
     *
     * The getter of mapID
     *
     * @return The mapID from 1 to 4
     *
     */
    public int getMapID(){ return mapID;}

    /**
     *
     * The getter of weaponMap
     *
     * @return A HashMap of weaponMap
     *
     */
    public HashMap<Color, ArrayList<Integer>> getWeaponMap(){
        return weaponMap;
    }

    /**
     *
     * The getter of ammoMap
     *
     * @return A HashMap of ammoMap
     */
    public HashMap<Integer,Integer> getAmmoMap() { return  ammoMap; }


}
