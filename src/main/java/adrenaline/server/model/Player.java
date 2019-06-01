package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.server.Observable;
import adrenaline.server.controller.Lobby;
import adrenaline.server.network.Client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;




/**
 *
 * This class is for new a Player
 *
 */

public class Player extends Observable{

    private Avatar avatar;
    private Lobby lobby;
    private int[] ammoBox;
    private int[] tempAmmoBox;
    private ArrayList<Color> damage;    //use a ArrayList for index the source of damage
    private ArrayList<Color> mark;
    private ArrayList<PowerupCard> powerupCards;
    private ArrayList<WeaponCard> weaponCards;
    private int score;


    private int position;
    private int oldPosition;  //Last position
    private int adrenalineState;  //This's an attribute for index how much steps this player can move
                                //The first element is for steps,second is for index how much steps can move before grab
                                //The third element is for index how much steps can move before shoot

    private int numOfActions; //This's for index the times of action the player can choose.Max is 2.
                                // When his turn is finished, This value will be reload at 2.

    private boolean alive; //For index is this player still alive. It can help shooter count score




    //costruttore temporaneo per fare buildare il progetto
    public Player(String avatar, Color color, Lobby lobby){
        damage = new ArrayList<>();
        powerupCards = new ArrayList<>();
        weaponCards = new ArrayList<>();
        mark = new ArrayList<>();
        adrenalineState = 0;
        ammoBox = new int[]{0,0,0};
        numOfActions = 2;
        score = 0;
        this.lobby = lobby;
    }


    public Player(Avatar avatar, String clientNickname, ArrayList<Client> clients){
        this.avatar = avatar;
        damage = new ArrayList<>();
        powerupCards = new ArrayList<>();
        weaponCards = new ArrayList<>();
        mark = new ArrayList<>();
        adrenalineState = 0;
        ammoBox = new int[]{0,0,0};
        numOfActions = 2;
        clients.forEach(this::attach);
        observers.forEach(x -> {
            try {
                x.setPlayerColor(clientNickname, avatar.getColor());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }



    /**
     *
     * This is for add score of player
     *
     * @param point Deposit the points got by the player at this time
     *
     */

    public void addScore(int point) {

        this.score= this.score+point;

    }


    /**
     *
     * Get the score of this player
     *
     * @return The score of this player
     *
     */

    public int getScore() {
        return score;

    }



    /**
     *
     *
     * This is a getter, It can get the lobby of this player
     *
     * @return The reference of lobby of this player
     *
     *
     */

    public Lobby getLobby() {
        return lobby;
    }



    /**
     *
     *
     * Add damage for this player
     *
     * @return This boolean is for index if the this play is already died.
     *
     *
     */

    public boolean sufferDamage(Player damageOrigin, int amount) {

        boolean sufferDamageNotFinished;

        for (;amount>0;amount--) {
            if (amount>1 || !this.mark.isEmpty())
                sufferDamageNotFinished = true;
            else
                sufferDamageNotFinished = false;
            if(addDamageToTrack(damageOrigin.getColor(), sufferDamageNotFinished))
                return true;
        }

        //If  this player have mark, Put all of them to damage track
        return putMarkToDamageTrackAndClearThem();

    }


    /**
     *
     *
     * Add a mark from other to this Player
     *
     * @param markOrigin The Origin player of this mark
     *
     */

    public void addMark(Color markOrigin) {

        if (this.mark.size()<3)
            mark.add(markOrigin);
        else
            //Remind the mark board can have up to 3 marks from each other player
            // so this mark is wasted
            ;

    }


    /**
     *
     *
     * Clear all mark for this player
     *
     *
     */

    private void clearMark() {

        this.mark.clear();
    }



    /**
     *
     *
     * Return the mark arrayList of this player
     *
     * @return Return ArrayList of mark of this Player.
     *
     */

    public ArrayList<Color> getMarks() {
        return mark;
    }




    /**
     *
     *
     * A private method,for add damage to damage track
     *
     * @param damageOrigin The damage origin player
     * @param addDamageNotFinished The boolean value for index if the damage process is finished
     *
     */

    private boolean addDamageToTrack(Color damageOrigin, boolean addDamageNotFinished){


        this.damage.add(damageOrigin);


        /*Attention: This is only available for mode 1.
        //First blood
        if (this.damage.size() == 1)
            damageOrigin.addScore(1);
        */

        //judgment for upgrade
        if (this.damage.size() >= 3)
            this.adrenalineState = 1;

        if (this.damage.size() >= 6)
            this.adrenalineState = 2;


        //kill
        if (this.damage.size() == 11 && !addDamageNotFinished) {

            //set score kill
            killAndOverkillScoreCount();

            //set status dead
            return true;
        }



        /*overkill
        if (this.damage.size() == 12) {

            //overkill and break
            killAndOverkillScoreCount();

            //set status dead
            //If you overkill a player, that player will give you a mark representing his or her desire for revenge
            damageOrigin.addMark(avatar.getColor());

            return true;
        }*/

        return false;
    }


    /**
     *
     *
     * This is a private class, for count the points,when someone is dead.
     *
     *
     */

    private void killAndOverkillScoreCount(){


        ArrayList<Player> playerToBeSort;

        Comparator comparator = (Comparator<Player>) (Player o1, Player o2) -> {

            if (Collections.frequency(this.damage,o1)>Collections.frequency(this.damage,o2))
                return 1;
            else if (Collections.frequency(this.damage,o1)==Collections.frequency(this.damage,o2)
                    && Collections.frequency(this.damage,o1)!=0){
                if (this.damage.indexOf(o1)<this.damage.indexOf(o2))
                    return 1;
                else
                    return -1;
            }
            else
                return -1;
        };


        //playerToBeSort = (ArrayList<Player>) getLobby().getPlayersList().clone();

        //playerToBeSort.sort(comparator);


        /*
        int maxElement=0;
        int a;
        Player maxPlayer=null;

        //Collections.frequency(this.damage,damageOrigin);


        for (int i=0;i<this.player.getLobby().getPlayersList().size();i++){
            a = Collections.frequency(this.damage,this.player.getLobby().getPlayersList().get(i));
            if (a>maxElement){
                maxElement=a;
                maxPlayer=this.player.getLobby().getPlayersList().get(i);
            }
            else if(a==maxElement && maxPlayer!=null && maxElement!=0){
                if (this.damage.indexOf(this.player.getLobby().getPlayersList().get(i))
                        < this.damage.indexOf(maxPlayer)){
                    maxElement=a;
                    maxPlayer=this.player.getLobby().getPlayersList().get(i);
                }
            }
        }
        maxPlayer.addScore(this.scoreBoard[this.player.getNumberOfDeaths()]);
         */

    }




    /**
     *
     *
     * This is a private class, To put mark to damage track,and clean make track
     *
     * @return It will return a boolean value,this value is for index,if the this play is already died.
     */

    private boolean putMarkToDamageTrackAndClearThem(){

        boolean markNotFinished;

        if (!this.mark.isEmpty()){
            for (int i=0;i<this.mark.size();i++){
                if (i < this.mark.size()-1)
                    markNotFinished = true;
                else
                    markNotFinished =false;

                if(addDamageToTrack(this.mark.get(i),markNotFinished))
                    return true;
            }
            clearMark();
        }
        return false;
    }




    public ArrayList<WeaponCard> getWeaponCards() {
        return weaponCards;
    }

    public WeaponCard getWeaponCard(int weaponID){
        for(WeaponCard wc : weaponCards){
            if(wc.getWeaponID() == weaponID) return wc;
        }
        return null;
    }

    public void addWeaponCard(WeaponCard weaponCard) { weaponCards.add(weaponCard); }

    public boolean removeWeaponCard(WeaponCard weaponCard) { return weaponCards.remove(weaponCard);}

    public int getPosition(){ return this.position;}

    public void setPosition(int position) {
        this.oldPosition = this.position;
        this.position = position;
    }

    public int getOldPosition() { return this.oldPosition; }

    public int getAdrenalineState() { return adrenalineState;}

    public int[] getAmmoBox() {
        return ammoBox;
    }

    public void addAmmoBox(int[] grabbedAmmoBox) {
        for (int i = 0; i < 3; i++) {
            //Your ammo box never holds more than 3 cubes of each color. Excess ammo depicted on the tile is wasted.
            ammoBox[i] = (ammoBox[i] + grabbedAmmoBox[i] <= 3)? ammoBox[i] + grabbedAmmoBox[i] : 3;
        }
    }

    public boolean canPayCost(int[] ammoCost){
        for(int i=0; i<3; i++){
            if(ammoCost[i] > (ammoBox[i] + tempAmmoBox[i])) return false;
        }
        return true;
    }

    public void payCost(int[] ammoCost){
        for(int i=0; i<3; i++){
            if(tempAmmoBox[i] - ammoCost[i] < 0){
                ammoBox[i] -= (tempAmmoBox[i] - ammoCost[i]);
                tempAmmoBox[i] = 0;
            }else{
                tempAmmoBox[i] -= ammoCost[i];
            }
        }
    }

    public PowerupCard consumePower(int powerUpID){
        for(PowerupCard pwc : powerupCards){
            if(pwc.getPowerupId() == powerUpID){
                switch (pwc.getColor()){
                    case RED: tempAmmoBox[0]++; break;
                    case BLUE: tempAmmoBox[1]++; break;
                    case YELLOW: tempAmmoBox[2]++; break;
                }
                powerupCards.remove(pwc);
                return pwc;
            }
        }
        return null;
    }


    public PowerupCard getPowerupCard(int powerupID){
        for(PowerupCard puc : powerupCards){
            if(puc.getPowerupId() == powerupID) return puc;
        }
        return null;
    }

    public void addPowerupCard(PowerupCard powerupCard) { powerupCards.add(powerupCard); }

    public boolean removePowerupCard(PowerupCard powerupCard) { return powerupCards.remove(powerupCard);}

    public int getPowerupHandSize(){return powerupCards.size();}

    public int getWeaponHandSize(){return weaponCards.size();}

    public ArrayList<Color> getDamageTrack() {
        return damage;
    }


    public ArrayList<PowerupCard> getPowerupCards() {
        return powerupCards;
    }


    @Override
    public String toString() {
        return "Player{" +
                ", lobby=" + lobby +
                ", ammoBox=" + Arrays.toString(ammoBox) +
                ", damage=" + damage +
                ", powerupCards=" + powerupCards +
                ", weaponCards=" + weaponCards +
                ", mark=" + mark +
                ", position=" + position +
                ", oldPosition=" + oldPosition +
                ", adrenalineState=" + adrenalineState +
                ", numOfActions=" + numOfActions +
                '}';
    }

    public boolean isAlive() {
        return alive;
    }

    public Color getColor() {
        return avatar.getColor();
    }
}
