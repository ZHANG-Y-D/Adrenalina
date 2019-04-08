import java.util.ArrayList;
import java.util.Arrays;

/*
 *  Use "new" for add a player
 *
 *  Author Zhang YueDong
 */

public class Player {
    private String Name;
    private char Color;       //For index the color of Avatar
    private int Score;
    private int[] AmmoBox;
    private ArrayList Damage;    //use a ArrayList for index the source of damage
    private ArrayList Powerups;
    private ArrayList WeaponCard;
    //For avatar private
    private int[] Deaths;
    private int Position;


    public Player(String name, char color) {
        Name = name;
        Color = color;
        Score = 0;

        //for AmmoBox,seq: Red Yellow Blue
        AmmoBox = new int[]{0, 0, 0};

        Damage = new ArrayList<Player>();
        Powerups = new ArrayList<Player>();
        WeaponCard = new ArrayList<Player>();

        //Use a int array per index Death,For put Skeleton,write '0'
        Deaths = new int[]{8,6,4,2,1,1};

        Position = 0;

    }



    public void setScore(int score) {
        Score = score;
    }


    public void setAmmoBox(int[] ammoBox) {
        AmmoBox = ammoBox;
    }


    public void setDamage(ArrayList damage) {
        Damage = damage;
    }

    public void setPowerups(ArrayList powerups) {
        Powerups = powerups;
    }

    public void setWeaponCard(ArrayList weaponCard) {
        WeaponCard = weaponCard;
    }

    public void setDeaths(int[] deaths) {
        Deaths = deaths;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public int getPosition(){
        return this.Position;
    }

    //For test
    public static void main(String[] args) {
        Player zhang = new Player("zhang", 'b');
        System.out.println(zhang);
        zhang.setScore(8);
        System.out.println(zhang);
        zhang.setDamage(zhang.Damage);
    }


    @Override
    public String toString() {
        return "Player{" +
                "Name='" + Name + '\'' +
                ", Color=" + Color +
                ", Score=" + Score +
                ", AmmoBox=" + Arrays.toString(AmmoBox) +
                ", Damage=" + Damage +
                ", Powerups=" + Powerups +
                ", WeaponCard=" + WeaponCard +
                ", Deaths=" + Arrays.toString(Deaths) +
                ", Position=" + Position +
                '}';
    }
}
