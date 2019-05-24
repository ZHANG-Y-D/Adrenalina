package adrenaline.client.model;

import adrenaline.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

    private HashMap<Color, ArrayList<Integer>> weaponMap;
    private HashMap<Integer, Integer> ammoMap;

    public Map(HashMap<Color, ArrayList<Integer>> weaponMap, HashMap<Integer, Integer> ammoMap){
        this.weaponMap = weaponMap;
        this.ammoMap = ammoMap;
    }

}
