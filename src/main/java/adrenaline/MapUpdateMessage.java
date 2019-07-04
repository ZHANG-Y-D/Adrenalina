package adrenaline;


import adrenaline.client.controller.GameController;
import adrenaline.client.model.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * To update map massage.The realize of observer pattern
 *
 */
public class MapUpdateMessage implements UpdateMessage {
    private Map clientsideMap;
    private HashMap<Color, ArrayList<Integer>> weaponIDs = new HashMap<>();
    private  HashMap<Integer, Integer> ammoIDs = new HashMap<>();

    /**
     *
     * For update the message
     * @param serversideMap The map of server side
     */
    public MapUpdateMessage(adrenaline.server.model.Map serversideMap){
        for(int i=0; i<=serversideMap.getMaxSquare();i++){
            if(!serversideMap.isEmptySquare(i)) serversideMap.getSquare(i).acceptConvertInfo(this, i);
        }

        clientsideMap = new Map(weaponIDs, ammoIDs, serversideMap.getMapID());
    }

    /**
     *
     * For add ammo info
     * @param index The int value index
     * @param ammoID The ammo ID
     */
    public void addAmmoInfo(int index, int ammoID){
        ammoIDs.put(index, ammoID);
    }

    public void addWeaponInfo(Color squareColor, ArrayList<Integer> weaponID){
        weaponIDs.put(squareColor, weaponID);
    }

    /**
     *
     *
     * To apply update
     * @param clientGameController The game controller if client
     */
    @Override
    public void applyUpdate(GameController clientGameController) {
        clientGameController.updateMap(clientsideMap);
    }
}
