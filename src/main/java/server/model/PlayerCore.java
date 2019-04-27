package server.model;


import server.controller.PlayerShell;

import java.util.ArrayList;
import java.util.Arrays;


/*
 *
 * Responsible: Zhang YueDong
 *
 * Attention from Zhang
 *
 *
 * About Class PlayerCore and Class PlayerShell
 *
 *      The PlayerShell is made for index the actual player,
 *      but the PlayerCore is in the level model for index the status of the player,
 *      when someone is dead, this class will be free. When it is resurrected,
 *      the class PlayerShell have to renew it.
 *
 *
 * Why I have created these two different class
 *
 *      For Better distinguish its functionality and information
 *      So that,when someone is dead,just free PlayerCore instead of reset a lot of parameter in the Player
 *      More modular and more stable,and improve scalability
 *
 *
 * How to use them
 *
 *
 *      When a player(Bob) has joined, new a class PlayerShell.
 *      When Bob begins his turn, use method newPlayerCore in the PlayerShell,
 *          and use method getPlayerCore to get this class;
 *      When Bob has dead, free PlayerCore.
 *      When Bob is resurrected, reuse method newPlayerCore.
 *      However the PlayerShell for information, the PlayerCore for functionality
 *
 *      PlayerShell always exist,PlayCore only exist when this player is still alive
 *
 */


public class PlayerCore {

    private PlayerShell playerShell;
    private int[] ammoBox;
    private ArrayList<PlayerShell> damage;    //use a ArrayList for index the source of damage
    private ArrayList<PowerupCard> powerup;
    private ArrayList<WeaponCard> weaponCard;
    private ArrayList<PlayerShell> mark;
    private int[] scoreBoard;    //For index if this players is died, how much score the other people can get.
                            //Normal mode is [8,6,4,2,1,1]. The Final Frenzy rules is [5,1,1,1]
    private int position;
    private int oldPosition;  //Last position
    private int[] runable;  //This's an attribute for index how much steps this player can move
                            //The first element is for steps,second is for index how much steps can move before grab
                            //The third element is for index how much steps can move before shoot

    private int numOfActions; //This's for index the times of action the player can choose.Max is 2.
                                // When his turn is finished, This value will be reload at 2.




    public PlayerCore(PlayerShell playerShell) {

        this.playerShell=playerShell;
        ammoBox = new int[]{0, 0, 0};
        damage = new ArrayList<>();
        mark = new ArrayList<>();
        powerup = new ArrayList<>();
        weaponCard = new ArrayList<>();
        position = 0;
        oldPosition = 0;



        //Use a int array per index Death,For put Skeleton,write '0'
        try {
            if (playerShell.getModeOfGame() == 1) {
                scoreBoard = new int[]{8, 6, 4, 2, 1, 1};
                numOfActions = 2;
                this.runable=new int[]{3,1,0};
            }
            else if (playerShell.getModeOfGame() == 2) {

                scoreBoard = new int[]{2, 1, 1, 1};
                //still to be defined
            }
            else
                //other mode
                ;

        }catch (NullPointerException e){
            System.err.println("PlayerShell Not initialized");
        }

    }




    public void setAmmoBox(int[] ammoBox) {
        this.ammoBox = ammoBox;
    }


    public void setNumOfActions(int numOfActions) {
        this.numOfActions = numOfActions;
    }



    //It will return a boolean value,this value is for index,if the this play is already died.
    public boolean sufferDamage(PlayerShell damageOrigin, int amount) {

        boolean sufferDamegeNotFinished;

        for (;amount>0;amount--) {
            if (amount>1 || !this.mark.isEmpty())
                sufferDamegeNotFinished = true;
            else
                sufferDamegeNotFinished = false;
            if(addDamegaToTrack(damageOrigin, sufferDamegeNotFinished))
                return true;
        }

        //If  this player have mark, Put all of them to damage track
        if (putMarkToDamageTrackAndClearThem())
            return true;

        return false;

    }



    public void addMark(PlayerShell markOrigin) {

        if (this.mark.size()<3)
            mark.add(markOrigin);
        else
            //Remind the mark board can have up to 3 marks from each other player
            // so this mark is wasted
            ;

    }

    public void clearMark() {
        this.mark.clear();
    }

    public ArrayList<PlayerShell> getMark() {
        return mark;
    }

    //This function is for add damage to damage track.
    //It will return a boolean value,this value is for index,if the this play is already died.
    //Attention: this function is Private, if other class want to add damage,please call class public "sufferDageme"
    private boolean addDamegaToTrack(PlayerShell damageOrigin, boolean addDamegeNotFinishe){

            this.damage.add(damageOrigin);


            //Attention: This is only available for mode 1.
            //First blood
            if (this.damage.size() == 1)
                damageOrigin.addScore(1);


            //judgment for upgrade
            if (this.damage.size() >= 3)
                this.runable[1] = 2;

            if (this.damage.size() >= 6)
                this.runable[2] = 1;


            //kill
            if (this.damage.size() == 11 && !addDamegeNotFinishe) {
                //set score kill
                

                //set status dead
                this.playerShell.setStatusDead(true);
                return true;
            }

            //overkill
            if (this.damage.size() == 12) {
                //overkill and break


                //set status dead
                this.playerShell.setStatusDead(true);
                //If you overkill a player, that player will give you a mark representing his or her desire for revenge
                damageOrigin.getPlayerCore().addMark(this.playerShell);
                return true;
            }

            return false;
    }



    //It will return a boolean value,this value is for index,if the this play is already died.
    //Attention: this function is Private, it only can be called by sufferDamage"
    private boolean putMarkToDamageTrackAndClearThem(){

        boolean markNotFinished;

        if (!this.mark.isEmpty()){
            for (int i=0;i<this.mark.size();i++){
                if (i < this.mark.size()-1)
                    markNotFinished = true;
                else
                    markNotFinished =false;

                if(addDamegaToTrack(this.mark.get(i),markNotFinished))
                    return true;
            }
            clearMark();
        }
        return false;
    }


    public void addPowerup(PowerupCard powerupcard) {

        this.powerup.add(powerupcard);

    }


    public void addWeaponCard(ArrayList weaponCard) {

        this.weaponCard = weaponCard;

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

    public int[] getRunable() {
        return runable;
    }

    public int getOldPosition() {

        return this.oldPosition;

    }

    public ArrayList<PlayerShell> getDamageTrack() {
        return damage;
    }

    @Override
    public String toString() {
        return "PlayerCore{" +
                "playerShell=" + playerShell +
                ", ammoBox=" + Arrays.toString(ammoBox) +
                ", damage=" + damage +
                ", powerup=" + powerup +
                ", weaponCard=" + weaponCard +
                ", mark=" + mark +
                ", scoreBoard=" + Arrays.toString(scoreBoard) +
                ", position=" + position +
                ", oldPosition=" + oldPosition +
                ", runable=" + Arrays.toString(runable) +
                ", numOfActions=" + numOfActions +
                '}';
    }

}
