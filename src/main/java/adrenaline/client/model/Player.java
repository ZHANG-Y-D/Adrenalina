package adrenaline.client.model;

import adrenaline.Color;

import java.util.ArrayList;

public class Player {

    private Color color;
    private int position;
    private int[] ammoBox;
    private ArrayList<Color> damage;
    private ArrayList<Color> marks;
    private ArrayList<Integer> weaponCards;
    private ArrayList<Integer> powerupCards;

    public Player(Color color, int position, int[] ammoBox, ArrayList<Color> damage, ArrayList<Color> marks, ArrayList<Integer> weaponCards, ArrayList<Integer> powerupCards){
        this.color = color;
        this.position = position;
        this.ammoBox = ammoBox;
        this.damage = damage;
        this.marks = marks;
        this.weaponCards = weaponCards;
        this.powerupCards = powerupCards;
    }

    public Color getColor() {
        return color;
    }
}
