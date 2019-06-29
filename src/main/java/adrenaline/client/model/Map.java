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
     *
     *
     *
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
    public Map(){
        mapID = 2;
        ArrayList<Integer> weapons = new ArrayList<>();
        weapons.add(1);
        weapons.add(10);
        weapons.add(19);
        weaponMap.put(Color.RED, weapons);
        ArrayList<Integer> weapons2 = new ArrayList<>();
        weapons2.add(7);
        weapons2.add(14);
        weapons2.add(21);
        weaponMap.put(Color.BLUE, weapons2);
        ArrayList<Integer> weapons3 = new ArrayList<>();
        weapons3.add(8);
        weapons3.add(18);
        weapons3.add(3);
        weaponMap.put(Color.YELLOW, weapons3);
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
     *
     *
     */
    public int getMapID(){ return mapID;}

    /**
     *
     *
     *
     *
     */
    public HashMap<Color, ArrayList<Integer>> getWeaponMap(){
        return weaponMap;
    }

    /**
     *
     *
     *
     *
     */
    public HashMap<Integer,Integer> getAmmoMap() { return  ammoMap; }

    /**
     *
     *
     *
     *
     */
    public void setWeaponMap(HashMap<Color, ArrayList<Integer>> weaponMap){
        this.weaponMap = weaponMap;
    }
}
