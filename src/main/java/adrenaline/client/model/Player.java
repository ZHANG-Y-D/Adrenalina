package adrenaline.client.model;

import adrenaline.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 *
 *
 *
 */
public class Player implements Serializable {

    private Color color;
    private int position = -1;
    private int oldPosition = -1;
    private int[] ammoBox = new int[]{0,0,0};
    private ArrayList<Color> damage = new ArrayList<>();
    private ArrayList<Color> marks = new ArrayList<>();
    private ArrayList<Integer> weaponCards = new ArrayList<>();
    private ArrayList<Integer> powerupCards = new ArrayList<>();

    /**
     *
     * Constructor of score board
     *
     * @param color
     * @param position
     * @param oldPosition
     * @param ammoBox
     * @param damage
     * @param marks
     * @param weaponCards
     * @param powerupCards
     */
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

    /**
     *
     *
     *
     *
     */
    public Player(){ }

    /**
     *
     *
     * @return
     *
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     *
     *@return
     *
     */
    public int getPosition() { return position; }

    /**
     *
     *
     *@return
     *
     */
    public int getOldPosition() { return oldPosition; }

    /**
     *
     *
     *@return
     *
     */
    public int[] getAmmoBox() { return ammoBox; }

    /**
     *
     *
     *@return
     *
     */
    public ArrayList<Integer> getPowerupCards() { return powerupCards; }

    /**
     *
     *
     *@return
     *
     */
    public ArrayList<Integer> getWeaponCards() { return weaponCards; }

    /**
     *
     *
     *@return
     *
     */
    public ArrayList<Color> getDamage(){ return damage; }

    /**
     *
     *
     *@return
     *
     */
    public ArrayList<Color> getMarks() { return marks; }
}
