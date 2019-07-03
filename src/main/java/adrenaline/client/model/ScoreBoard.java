package adrenaline.client.model;

import adrenaline.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * For score board
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
    private java.util.Map<Color,Integer> finalPlayersPosition;


    /**
     *
     * Constructor of score board
     *
     * @param scoreMap
     * @param diminValues
     * @param killshotTrack
     * @param overkillFlags
     */
    public ScoreBoard(HashMap<Color,Integer> scoreMap, HashMap<Color,Integer> diminValues, List<Color> killshotTrack, List<Boolean> overkillFlags, int maxKills,
                      java.util.Map<Color,Integer> finalPlayersPosition){
        this.scoreMap = scoreMap;
        this.diminValues = diminValues;
        this.killshotTrack = killshotTrack;
        this.overkillFlags = overkillFlags;
        this.maxKills = maxKills;
        this.finalPlayersPosition = finalPlayersPosition;
    }

    /**
     * For get players' score
     *
     * @return a hash map color is players' color, Integer is score
     */
    public HashMap<Color,Integer> getScoreMap() { return scoreMap; }


    /**
     *
     * For get players' diminishing values
     *
     * @return a hash map color is players' color, Integer is score
     */
    public HashMap<Color,Integer> getDiminValues() { return diminValues; }

    /**
     *
     * Get kill shot track
     *
     * @return a list, color is players' color
     */
    public List<Color> getKillshotTrack() { return killshotTrack; }


    /**
     *
     * Get over kill flags
     *
     * @return a boolean list
     */
    public List<Boolean> getOverkillFlags() { return overkillFlags; }

    /**
     *
     * Get the max kills
     *
     * @return The max kills
     */
    public int getMaxKills() { return maxKills; }

    /**
     *
     * Get the players' ranking
     *
     * @return A map color for players' color,Integer is ranking of this player
     */
    public java.util.Map<Color,Integer> getFinalPlayersPosition() { return finalPlayersPosition; }
}
