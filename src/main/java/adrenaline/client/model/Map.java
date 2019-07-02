package adrenaline.client.model;

import adrenaline.Color;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 *
 *
 *
 */
public class Map implements Serializable {

    private int mapID;
    private HashMap<Color, ArrayList<Integer>> weaponMap = new HashMap<>();
    private HashMap<Integer, Integer> ammoMap;

    /**
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
     *
     *
     *
     */
    public synchronized void setMap(){
        weaponMap.clear();
        ArrayList<Integer> weapons = new ArrayList<>();
        weapons.add(5);
        weaponMap.put(Color.RED, weapons);
    }

    /**
     *
     *
     * @return
     *
     */
    public int getMapID(){ return mapID;}

    /**
     *
     *
     * @return
     *
     */
    public HashMap<Color, ArrayList<Integer>> getWeaponMap(){
        return weaponMap;
    }

    /**
     *
     *
     *
     * @return
     */
    public HashMap<Integer,Integer> getAmmoMap() { return  ammoMap; }

    /**
     *
     *
     *
     * @param weaponMap
     */
    public void setWeaponMap(HashMap<Color, ArrayList<Integer>> weaponMap){
        this.weaponMap = weaponMap;
    }
}
