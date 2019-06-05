package adrenaline.client.model;

import adrenaline.Color;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Map implements Serializable {

    private int mapID;
    private HashMap<Color, ArrayList<Integer>> weaponMap = new HashMap<>();
    private HashMap<Integer, Integer> ammoMap;

    public Map(HashMap<Color, ArrayList<Integer>> weaponMap, HashMap<Integer, Integer> ammoMap, int mapID){
        this.weaponMap = weaponMap;
        this.ammoMap = ammoMap;
        this.mapID = mapID;
    }

    public Map(){
        ArrayList<Integer> weapons = new ArrayList<>();
        weapons.add(1);
        weapons.add(10);
        weaponMap.put(Color.RED, weapons);
    }

    public int getMapID(){ return mapID;}

    public HashMap<Color, ArrayList<Integer>> getWeaponMap(){
        return weaponMap;
    }

    public void setWeaponMap(HashMap<Color, ArrayList<Integer>> weaponMap){
        this.weaponMap = weaponMap;
    }
}
