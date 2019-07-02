package adrenaline.client.model;

import adrenaline.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 *
 *
 *
 */
public class ScoreBoard implements Serializable {
    private HashMap<Color,Integer> scoreMap;
    private HashMap<Color,Integer> diminValues;
    private List<Color> killshotTrack;
    private List<Boolean> overkillFlags;
    private int maxKills;
    /**
     *
     *
     *
     *
     */
    public ScoreBoard(HashMap<Color,Integer> scoreMap, HashMap<Color,Integer> diminValues, List<Color> killshotTrack, List<Boolean> overkillFlags, int maxKills){
        this.scoreMap = scoreMap;
        this.diminValues = diminValues;
        this.killshotTrack = killshotTrack;
        this.overkillFlags = overkillFlags;
        this.maxKills = maxKills;
    }

    public HashMap<Color,Integer> getScoreMap() { return scoreMap; }

    public HashMap<Color,Integer> getDiminValues() { return diminValues; }

    public List<Color> getKillshotTrack() { return killshotTrack; }

    public List<Boolean> getOverkillFlags() { return overkillFlags; }
}
