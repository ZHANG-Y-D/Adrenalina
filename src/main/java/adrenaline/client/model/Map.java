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
        weapons.add(19);
        weaponMap.put(Color.RED, weapons);
        weapons.clear();
        weapons.add(7);
        weapons.add(14);
        weapons.add(21);
        weaponMap.put(Color.BLUE, weapons);
        weapons.clear();
        weapons.add(8);
        weapons.add(18);
        weapons.add(3);
        weaponMap.put(Color.YELLOW, weapons);
    }

    public void setMap(){
        weaponMap = null;
        ammoMap = null;
    }

    public int getMapID(){ return mapID;}

    public HashMap<Color, ArrayList<Integer>> getWeaponMap(){
        return weaponMap;
    }

    public void setWeaponMap(HashMap<Color, ArrayList<Integer>> weaponMap){
        this.weaponMap = weaponMap;
    }
}
