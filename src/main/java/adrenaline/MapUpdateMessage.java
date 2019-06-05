package adrenaline;


import adrenaline.client.controller.GameController;
import adrenaline.client.model.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MapUpdateMessage implements UpdateMessage {
    private Map clientsideMap;
    private HashMap<Color, ArrayList<Integer>> weaponIDs = new HashMap<>();
    private  HashMap<Integer, Integer> ammoIDs = new HashMap<>();

    public MapUpdateMessage(adrenaline.server.model.Map serversideMap){
        /*for(int i=0; i<=serversideMap.getMaxSquare();i++){
            if(!serversideMap.isEmptySquare(i)) serversideMap.getSquare(i).acceptConvertInfo(this, i);
        }*/

        clientsideMap = new Map(weaponIDs, ammoIDs, serversideMap.getMapID());
    }

    public void addAmmoInfo(int index, int ammoID){
        ammoIDs.put(index, ammoID);
    }

    public void addWeaponInfo(Color squareColor, ArrayList<Integer> weaponID){
        weaponIDs.put(squareColor, weaponID);
    }

    @Override
    public void applyUpdate(GameController clientGameController) {
        clientGameController.updateMap(clientsideMap);
    }
}
