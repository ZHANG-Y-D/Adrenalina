package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.server.Observable;
import adrenaline.server.network.Client;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreBoard extends Observable {
    private HashMap<String,Color> clientColorMap = new HashMap<>();
    private HashMap<Color,Integer> scoreMap;
    private HashMap<Color,Integer> skullMap;
    private ArrayList<Color>[] killCount;

    public ScoreBoard(ArrayList<Client> clients){
        clients.stream().forEach(x -> clientColorMap.put(x.getClientID(), Color.WHITE));
    }

}
