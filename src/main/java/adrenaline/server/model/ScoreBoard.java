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
    private ArrayList<Color> finalfrenzyDeadPlayers = new ArrayList<>();

    private LinkedHashMap<Color, Integer> finalPlayerPositions = null;

    private HashMap<Color,Integer> finalfrenzyModePlayers = new HashMap<>();

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
        notifyObservers(new ScoreboardUpdateMessage(this));
    }

    public void scoreKill(Color dead, ArrayList<Color> damageTrack){
        if(!finalfrenzyDeadPlayers.contains(dead)) {
            int firstBlood = scoreMap.get(damageTrack.get(0));
            scoreMap.put(damageTrack.get(0), firstBlood + 1);
        }

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

    public java.util.Map<Color, Integer> getFinalPlayerPositions() { return finalPlayerPositions; }

    public int getKillCount() { return killCount; }

    public void setFinalFrenzyMode(Color player, boolean firstPlayerFF){
        finalfrenzyModePlayers.put(player, firstPlayerFF? 2 : 1);
        notifyObservers(new ScoreboardUpdateMessage(this));
    }

    public void setFinalFrenzyValues(Color player){
        diminValues.put(player,2);
        finalfrenzyDeadPlayers.add(player);
        notifyObservers(new ScoreboardUpdateMessage(this));
    }

    //public boolean isFinalFrenzy(){ return finalfrenzyModePlayers.isEmpty();}

    public HashMap<Color, Integer> getFinalfrenzyModePlayers() {
        return finalfrenzyModePlayers;
    }

    public void scoreKillshotTrack() {
        finalPlayerPositions = new LinkedHashMap<>();
        HashMap<Color,Integer> pointsFromKillshoTrack = new HashMap<>();
        HashMap<Color,Integer> tokensOnKillshotTrack = new HashMap<>();
        scoreMap.keySet().forEach(x -> {
            pointsFromKillshoTrack.put(x,0);
            tokensOnKillshotTrack.put(x,0);
        });

        for(int i=0;i<killshotTrack.length;i++){
            int tokens = tokensOnKillshotTrack.get(killshotTrack[i]);
            tokensOnKillshotTrack.put(killshotTrack[i], tokens+(overkillFlags[i]? 2 : 1));
        }
        List<Integer> orderedList = tokensOnKillshotTrack.values().stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int points = 8;
        for(Integer x : orderedList) {
            for (Color c : killshotTrack) {
                if(tokensOnKillshotTrack.get(c).equals(x)){
                    pointsFromKillshoTrack.put(c, (points-2 < 1)? 1 : points);
                    points-=2;
                }
            }
        }
        pointsFromKillshoTrack.forEach((x,y)-> {
            int score = scoreMap.get(x);
            scoreMap.put(x,score+y);
        });
        orderedList = scoreMap.values().stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int position=1;
        for(Integer x : orderedList){
            for(java.util.Map.Entry<Color,Integer> entry : scoreMap.entrySet()){
                if(entry.getValue().equals(x)) finalPlayerPositions.put(entry.getKey(),position);
            }
            position++;
        }
        Iterator itr = finalPlayerPositions.keySet().iterator();
        Color c1 = (Color) itr.next();
        while(itr.hasNext()){
            Color c2 = (Color) itr.next();
            if(finalPlayerPositions.get(c1).equals(finalPlayerPositions.get(c2))){
                if(pointsFromKillshoTrack.get(c1)>pointsFromKillshoTrack.get(c2)){
                    position = finalPlayerPositions.get(c2);
                    finalPlayerPositions.put(c2, position+1);
                }else if(pointsFromKillshoTrack.get(c1)<pointsFromKillshoTrack.get(c2)){
                    position = finalPlayerPositions.get(c1);
                    finalPlayerPositions.put(c1, position+1);
                }
            }
            c1=c2;
        }
        notifyObservers(new ScoreboardUpdateMessage(this));
    }
}

