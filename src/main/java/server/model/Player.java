package server.model;


import java.util.ArrayList;
import java.util.Arrays;

/*
 *  Use "new" for add a player
 *
 *  Responsible: Zhang YueDong
 */


public class Player {
    private String name;
    private Color color;       //For index the color of Avatar
    private int score;
    private int[] ammoBox;
    private ArrayList<Player> damage;    //use a ArrayList for index the source of damage
    private ArrayList<PowerupCard> powerup;
    private ArrayList<WeaponCard> weaponCard;
    private ArrayList<Player> mark;
    private int[] deaths;
    private int position;
    private int oldPosition;  //Last position
    private int[] runable;  //This's an attribute for index how much steps this player can move
                            //The first element is for steps,second is for index how much steps can move before grab
                            //The third element is for index how much steps can move before shoot

    private int numOfActions; //This's for index the times of action the player can choose.Max is 2.
                                // When his turn is finished, This value will be reload at 2.



    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        score = 0;

        //for ammoBox,seq: Red Yellow Blue
        ammoBox = new int[]{0, 0, 0};

        damage = new ArrayList<>();
        mark = new ArrayList<>();
        powerup = new ArrayList<>();
        weaponCard = new ArrayList<>();


        //Use a int array per index Death,For put Skeleton,write '0'
        deaths = new int[]{8,6,4,2,1,1};

        position = 0;
        oldPosition = 0;

        runable = new int[]{3,1,0};
        numOfActions = 2;

    }




    public void setScore(int score) {
        this.score = score;
    }


    public void setAmmoBox(int[] ammoBox) {
        this.ammoBox = ammoBox;
    }

    public void setNumOfActions(int numOfActions) {
        this.numOfActions = numOfActions;
    }




    public void sufferDamage(Player damageOrigin, int amount) {

        boolean damegeNotFinished = false;

        for (;amount>0;amount--) {
            if (amount>1)
                damegeNotFinished = true;
            else
                damegeNotFinished = false;
            addDamegaToTrack(damageOrigin, damegeNotFinished);
        }

        //If  this player have mark, Put all of them to damage track
        if (!this.mark.isEmpty()) {
            putMarkToDamageTrackAndClearThem();
        }

    }

    public void addMark(Player markOrigin) {

        mark.add(markOrigin);
        //judgment size of mark not more than 3
    }



    //This function is for add damage to damage track.
    //It will return a boolean value,this value is for index,if the this play is already died.
    //Attention: this function is Private, if other class want to add damage,please call class public "sufferDageme"
    private boolean addDamegaToTrack(Player damageOrigin, boolean damegeNotFinished){


            this.damage.add(damageOrigin);

            //First blood
            if (this.damage.size() == 1)
                damageOrigin.score++;

            //judgment for upgrade
            if (this.damage.size() >= 3)
                this.runable[1] = 2;

            if (this.damage.size() >= 6)
                this.runable[2] = 1;

            //kill
            if (this.damage.size() == 11 && !damegeNotFinished) {
                //kill
                return true;
            }

            //overkill
            if (this.damage.size() == 12) {
                //overkill and break
                return true;
            }

            return false;
    }



    private void putMarkToDamageTrackAndClearThem(){

        boolean markNotFinished = false;

        if (!this.mark.isEmpty()){
            for (int i=0;i<this.mark.size();i++){
                if (i < this.mark.size()-1)
                    markNotFinished = true;
                else
                    markNotFinished =false;

                if(addDamegaToTrack(this.mark.get(i),markNotFinished))
                    break;
            }
            this.mark.clear();
        }
    }


    public void addPowerup(PowerupCard powerupcard) {

        this.powerup.add(powerupcard);
    }

    public void setWeaponCard(ArrayList weaponCard) {
        this.weaponCard = weaponCard;
    }

    public void setDeaths(int[] deaths) {
        this.deaths = deaths;
    }

    public void setPosition(int position) {

        setOldPosition(this.position);
        this.position = position;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public int getPosition(){
        return this.position;
    }

    public int getOldPosition() {
        return oldPosition;
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
                ", weaponCard=" + weaponCard +
                ", mark=" + mark +
                ", deaths=" + Arrays.toString(deaths) +
                ", position=" + position +
                ", oldPosition=" + oldPosition +
                ", runable=" + Arrays.toString(runable) +
                ", numOfActions=" + numOfActions +
                '}';
    }
}
