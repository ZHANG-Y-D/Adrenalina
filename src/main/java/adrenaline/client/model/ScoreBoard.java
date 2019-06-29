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
 */
public class ScoreBoard implements Serializable {
    private HashMap<Color,Integer> diminValues;
    private Color[] killshotTrack;
    private Boolean[] overkillFlags;

    /**
     *
     *
     *
     *
     */
    public ScoreBoard(HashMap<Color,Integer> diminValues, Color[] killshotTrack, Boolean[] overkillFlags){
        this.diminValues = diminValues;
        this.killshotTrack = killshotTrack;
        this.overkillFlags = overkillFlags;
    }
}
