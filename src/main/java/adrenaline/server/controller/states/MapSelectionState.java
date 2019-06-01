package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MapSelectionState implements GameState {

    private final int MAPSELECTION_TIMEOUT_IN_SECONDS = 20;
    private final int EXISTING_MAPS_NUMBER = 4;

    private Lobby lobby;
    private ArrayList<Integer> votes;
    private ArrayList<String> leftToVote;

    public MapSelectionState(Lobby lobby, ArrayList<String> clientIDs){
        this.lobby = lobby;
        leftToVote = clientIDs;
        votes = new ArrayList<>();
    }

    @Override
    public String runAction() {
        return "KO";
    }

    @Override
    public String grabAction() {
        return "KO";
    }

    @Override
    public String shootAction() {
        return "KO";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "KO";
    }

    @Override
    public String selectSquare(int index) {
        return "KO";
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        return "KO";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "KO";
    }

    @Override
    public String selectFiremode(int firemode) {
        return null;
    }

    @Override
    public String moveSubAction() {
        return "KO";
    }

    @Override
    public String endOfTurnAction() {
        return "KO";
    }

    @Override
    public String goBack() {
        return "KO";
    }

    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    @Override
    public synchronized String selectMap(int mapID, String voterID) {
        if(leftToVote.contains(voterID)) {
            if(mapID>EXISTING_MAPS_NUMBER) return "Invalid vote!";
            else {
                votes.add(mapID);
                leftToVote.remove(voterID);
                return "OK";
            }
        }else{
            return "You can only vote once!";
        }
    }

    public int startTimer(){
        long timestart = System.currentTimeMillis();
        while(!leftToVote.isEmpty() &&(System.currentTimeMillis() - timestart < MAPSELECTION_TIMEOUT_IN_SECONDS*1000));
        ArrayList<Integer> draw = new ArrayList<>();
        int maxVotes=0;
        for(int i=1; i<=EXISTING_MAPS_NUMBER; i++){
            draw.add(i);
            if(maxVotes < Collections.frequency(votes,i)) maxVotes = Collections.frequency(votes,i);
        }
        for(int i : draw) if(Collections.frequency(votes,i)< maxVotes) draw.remove(i);
        return draw.get(new Random().nextInt(draw.size()));
    }

    public int getTimeoutDuration(){
        return MAPSELECTION_TIMEOUT_IN_SECONDS;
    }
}
