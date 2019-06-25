package adrenaline.client.model;

import adrenaline.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private Color color;
    private int position = -1;
    private int oldPosition = -1;
    private int[] ammoBox;
    private ArrayList<Color> damage = new ArrayList<>();
    private ArrayList<Color> marks = new ArrayList<>();
    private ArrayList<Integer> weaponCards = new ArrayList<>();
    private ArrayList<Integer> powerupCards = new ArrayList<>();

    public Player(Color color, int position, int oldPosition, int[] ammoBox, ArrayList<Color> damage, ArrayList<Color> marks, ArrayList<Integer> weaponCards, ArrayList<Integer> powerupCards){
        this.color = color;
        this.position = position;
        this.oldPosition = oldPosition;
        this.ammoBox = ammoBox;
        this.damage = damage;
        this.marks = marks;
        this.weaponCards = weaponCards;
        this.powerupCards = powerupCards;
    }

    public Player(){ }

    public Color getColor() {
        return color;
    }

    public int getPosition() { return position; }

    public int getOldPosition() { return oldPosition; }

    public int[] getAmmoBox() { return ammoBox; }

    public ArrayList<Integer> getPowerupCards() { return powerupCards; }

    public ArrayList<Integer> getWeaponCards() { return weaponCards; }

    public ArrayList<Color> getDamage(){ return damage; }

    public ArrayList<Color> getMarks() { return marks; }
}
