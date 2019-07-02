package adrenaline.client.model;

import adrenaline.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 *
 *
 *
 *
 */
public class ScoreBoard implements Serializable {
    private HashMap<Color,Integer> scoreMap;
    private HashMap<Color,Integer> diminValues;
    private Color[] killshotTrack;
    private Boolean[] overkillFlags;

    

    /**
     *
     * Constructor of score board
     *
     * @param scoreMap
     * @param diminValues
     * @param killshotTrack
     * @param overkillFlags
     */
    public ScoreBoard(HashMap<Color,Integer> scoreMap,HashMap<Color,Integer> diminValues, Color[] killshotTrack, Boolean[] overkillFlags){
        this.scoreMap = scoreMap;
        this.diminValues = diminValues;
        this.killshotTrack = killshotTrack;
        this.overkillFlags = overkillFlags;
    }

    public HashMap<Color,Integer> getScoreMap() { return scoreMap; }

    public HashMap<Color,Integer> getDiminValues() { return diminValues; }

    public Color[] getKillshotTrack() { return killshotTrack; }

    public Boolean[] getOverkillFlags() { return overkillFlags; }
}
