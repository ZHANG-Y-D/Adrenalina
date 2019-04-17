package server.model;

import java.util.ArrayList;
import java.util.Arrays;

/*
 *  Use "new" for add a player
 *
 *  Author Zhang YueDong
 */

public class Player {
    private String name;
    private char color;       //For index the color of Avatar
    private int score;
    private int[] ammoBox;
    private ArrayList damage;    //use a ArrayList for index the source of damage
    private ArrayList powerup;
    private ArrayList weaponCard;
    private int[] deaths;
    private int position;


    public Player(String name, char color) {
        this.name = name;
        this.color = color;
        score = 0;

        //for ammoBox,seq: Red Yellow Blue
        ammoBox = new int[]{0, 0, 0};

        damage = new ArrayList<Player>();
        powerup = new ArrayList<Player>();
        weaponCard = new ArrayList<Player>();

        //Use a int array per index Death,For put Skeleton,write '0'
        deaths = new int[]{8,6,4,2,1,1};

        position = 0;

    }



    public void setScore(int score) {
        this.score = score;
    }


    public void setAmmoBox(int[] ammoBox) {
        this.ammoBox = ammoBox;
    }


    public void setDamage(ArrayList damage) {
        this.damage = damage;
    }

    public void setPowerup(ArrayList powerup) {
        this.powerup = powerup;
    }

    public void setWeaponCard(ArrayList weaponCard) {
        this.weaponCard = weaponCard;
    }

    public void setDeaths(int[] deaths) {
        this.deaths = deaths;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition(){
        return this.position;
    }

    /*
    //For test
    public static void main(String[] args) {
        Player zhang = new Player("zhang", 'b');
        System.out.println(zhang);
        zhang.setScore(8);
        System.out.println(zhang);
        zhang.setDamage(zhang.damage);
    }


    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", color=" + color +
                ", score=" + score +
                ", ammoBox=" + Arrays.toString(ammoBox) +
                ", damage=" + damage +
                ", powerup=" + powerup +
                ", server.model.weaponCard=" + weaponCard +
                ", deaths=" + Arrays.toString(deaths) +
                ", position=" + position +
                '}';
    }
    */
}
