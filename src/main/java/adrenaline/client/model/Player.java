package adrenaline.client.model;

import adrenaline.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * The client side Player Model
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
     * Constructor of score board,init all attitude
     *
     * @param color This player's color
     * @param position  This player's position
     * @param oldPosition This player's old position
     * @param ammoBox This player's ammo box
     * @param damage An ArrayList of this player's damage
     * @param marks An ArrayList of this player's marks
     * @param weaponCards An ArrayList of this player's weapon cards
     * @param powerupCards An ArrayList of this player's powerup cards
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
     * The construct for select stage,before the game stage started
     *
     *
     */
    public Player(){ }


    /**
     *
     *
     * The getter of this player's color
     *
     * @return The color of this player
     *
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * The getter of this player's position
     *
     *@return The position of this player
     *
     */
    public int getPosition() { return position; }

    /**
     *
     * The getter of this player's old position
     *
     *@return The old position of this player
     *
     */
    public int getOldPosition() { return oldPosition; }

    /**
     *
     * The getter of this player's ammobox
     *
     *@return ammobox array
     *
     */
    public int[] getAmmoBox() { return ammoBox; }

    /**
     *
     * The getter of this player's
     *
     *@return powerupCards ArrayList
     *
     */
    public ArrayList<Integer> getPowerupCards() { return powerupCards; }

    /**
     *
     * The getter of this player's weaponCards
     *
     *@return weaponCards ArrayList
     *
     */
    public ArrayList<Integer> getWeaponCards() { return weaponCards; }

    /**
     *
     * The getter of this player's damage track
     *
     *@return damage track ArrayList
     *
     */
    public ArrayList<Color> getDamage(){ return damage; }

    /**
     *
     * The getter of this player's marks track
     *
     *@return marks track ArrayList
     *
     */
    public ArrayList<Color> getMarks() { return marks; }
}
