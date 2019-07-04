package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SettingsVoteState implements GameState {

    private final int MAPSELECTION_TIMEOUT_IN_SECONDS = 40;
    private final int EXISTING_MAPS_NUMBER = 4;

    private Lobby lobby;
    private ArrayList<Integer> mapVotes;
    private ArrayList<Integer> skullsVotes;
    private ArrayList<String> leftToVote;

    public SettingsVoteState(Lobby lobby, ArrayList<String> clientIDs){
        this.lobby = lobby;
        leftToVote = clientIDs;
        mapVotes = new ArrayList<>();
        skullsVotes = new ArrayList<>();
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSquare(int index) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "KO"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String goBack() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    /**
     *
     * To do the select Settings action request which received from client terminal
     *
     * @param mapID The id of map which select from client from 1 to 4
     * @param skulls The skulls selected from client from 5 to 8
     * @return The result of this request to client
     *
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        synchronized (leftToVote) {
            if (leftToVote.contains(voterID)) {
                if (mapID > EXISTING_MAPS_NUMBER || skulls < 5 || skulls > 8) return "Invalid send!";
                else {
                    mapVotes.add(mapID);
                    skullsVotes.add(skulls);
                    leftToVote.remove(voterID);
                    leftToVote.notifyAll();
                    return "OK";
                }
            } else {
                return "You can only send once!";
            }
        }
    }

    public int[] startTimer(){
        synchronized(leftToVote) {
            long timestart = System.currentTimeMillis();
            while (!leftToVote.isEmpty()) {
                long timeremaining = timestart + MAPSELECTION_TIMEOUT_IN_SECONDS * 1000 - System.currentTimeMillis();
                if (timeremaining <= 0) break;
                try {
                    leftToVote.wait(timeremaining);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        int[] results = new int[2];
        ArrayList<Integer> mapDraw = new ArrayList<>();
        ArrayList<Integer> skullsDraw = new ArrayList<>();
        int maxMapVotes=0;
        int maxSkullsVotes=0;
        for(int i=1; i<=EXISTING_MAPS_NUMBER; i++){
            mapDraw.add(i);
            if(maxMapVotes < Collections.frequency(mapVotes,i)) maxMapVotes = Collections.frequency(mapVotes,i);
        }
        for(int i=5; i<=8; i++){
            skullsDraw.add(i);
            if(maxSkullsVotes < Collections.frequency(skullsVotes,i)) maxSkullsVotes = Collections.frequency(skullsVotes,i);
        }
        final int finalMaxMapVotes = maxMapVotes;
        final int finalMaxSkullsVote = maxSkullsVotes;
        mapDraw.removeIf(x -> Collections.frequency(mapVotes,x) < finalMaxMapVotes);
        skullsDraw.removeIf(x -> Collections.frequency(skullsVotes,x) < finalMaxSkullsVote);
        results[0] = mapDraw.get(new Random().nextInt(mapDraw.size()));
        results[1] = skullsDraw.get(new Random().nextInt(skullsDraw.size()));
        return results;
    }

    public int getTimeoutDuration(){
        return MAPSELECTION_TIMEOUT_IN_SECONDS;
    }
}
