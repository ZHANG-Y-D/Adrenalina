package adrenaline.client.model;

import adrenaline.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreBoard implements Serializable {

    private HashMap<Color,String> nicknameMap;
    private HashMap<Color,Integer> scoreMap;
    private HashMap<Color,Integer> skullMap;
    private ArrayList<Color>[] killCount;
}
