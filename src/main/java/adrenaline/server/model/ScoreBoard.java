package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.ScoreboardUpdateMessage;
import adrenaline.server.Observable;
import adrenaline.server.network.Client;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreBoard extends Observable {
    private HashMap<Color,Integer> scoreMap = new HashMap<>();
    private HashMap<Color,Integer> diminValues = new HashMap<>();
    private Color[] killshotTrack;
    private Boolean[] overkillFlags;
    private int killCount;

    public ScoreBoard(ArrayList<Client> clients){
        clients.forEach(this::attach);
    }

    public void initPlayerScore(Color color){
        scoreMap.put(color, 0);
        diminValues.put(color, 8);
    }

    public void initKillshotTrack(int skulls){
        killshotTrack = new Color[skulls];
        overkillFlags = new Boolean[skulls];
        killCount=0;
    }

    public void scoreKill(Color dead, ArrayList<Color> damageTrack){
        int firstBlood = scoreMap.get(damageTrack.get(0));
        scoreMap.put(damageTrack.get(0), firstBlood+1);

        List<Integer> frequencies = damageTrack.stream().map(x -> Collections.frequency(damageTrack, x)).distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        List<Color> attackers = damageTrack.stream().distinct().collect(Collectors.toList());
        int points = diminValues.get(dead);
        for (int x : frequencies) {
            for (Color c : attackers) {
                if (Collections.frequency(damageTrack, c) == x) {
                    int score = scoreMap.get(c);
                    scoreMap.put(c, score + points);
                    points = (points - 2) < 1 ? 1 : points - 2;
                }
            }
        }
        points = diminValues.get(dead);
        diminValues.put(dead, (points-2)<1 ? 1 : points-2);
        killshotTrack[killCount] = damageTrack.get(10);
        overkillFlags[killCount] = damageTrack.size()>=12;
        killCount++;
        System.out.println(scoreMap.toString()+"\n"+diminValues.toString());
        notifyObservers(new ScoreboardUpdateMessage(this));
    }

    public void scoreDoubleKill(Color killer){
        int score = scoreMap.get(killer);
        scoreMap.put(killer, score+1);
    }

    public boolean gameEnded(){
        return (killCount==killshotTrack.length);
    }

    public HashMap<Color, Integer> getDiminValues() {
        return diminValues;
    }

    public Color[] getKillshotTrack() {
        return killshotTrack;
    }

    public Boolean[] getOverkillFlags() {
        return overkillFlags;
    }

    public HashMap<Color,Integer> getScoreMap() { return scoreMap; }

    public int getKillCount() { return killCount; }
}
