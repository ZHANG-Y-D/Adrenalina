package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.PlayerUpdateMessage;
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
    private ArrayList<Color> marks;
    private ArrayList<PowerupCard> powerupCards;
    private ArrayList<WeaponCard> weaponCards;
    private int score;
    private boolean firstRound = true;


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
        marks = new ArrayList<>();
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
        marks = new ArrayList<>();
        adrenalineState = 0;
        ammoBox = new int[]{1,1,1};
        tempAmmoBox = new int[]{0,0,0};
        numOfActions = 2;
        alive = true;
        position = -1;
        clients.forEach(this::attach);
        observers.forEach(x -> {
            try {
                x.setPlayerColor(clientNickname, avatar.getColor());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }


    public void setFirstRound() { firstRound = false; }

    public boolean isFirstRound() { return  firstRound; }

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

    public boolean applyDamage(Color damageOrigin, int amount, boolean extra) {
        if(!extra && amount>0 && marks.contains(damageOrigin)){
            amount += Collections.frequency(marks,damageOrigin);
            marks.removeIf(damageOrigin::equals);
        }
        for(int i=0; i<amount && (damage.size() < 12); i++) this.damage.add(damageOrigin);

        if (this.damage.size() >= 11) alive = false;
        else if (this.damage.size() >= 6) this.adrenalineState = 2;
        else if (this.damage.size() >= 3) this.adrenalineState = 1;
        return alive;
    }


    /**
     *
     *
     * Add a marks from other to this Player
     *
     * @param markOrigin The Origin player of this marks
     *
     */

    public void addMarks(Color markOrigin, int amount) {
        for(int i=0; i<amount && Collections.frequency(marks,markOrigin)<3; i++) marks.add(markOrigin);
    }


    /**
     *
     *
     * Return the marks arrayList of this player
     *
     * @return Return ArrayList of marks of this Player.
     *
     */

    public ArrayList<Color> getMarks() {
        return marks;
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


    public ArrayList<WeaponCard> getWeaponCards() {
        return weaponCards;
    }

    public WeaponCard getWeaponCard(int weaponID){
        for(WeaponCard wc : weaponCards){
            if(wc.getWeaponID() == weaponID) return wc;
        }
        return null;
    }

    public void addWeaponCard(WeaponCard weaponCard) {
        weaponCards.add(weaponCard);
        notifyObservers(new PlayerUpdateMessage(this));
    }

    public boolean removeWeaponCard(WeaponCard weaponCard) {
        boolean result = weaponCards.remove(weaponCard);
        if(result) notifyObservers(new PlayerUpdateMessage(this));
        return result;
    }

    public int getPosition(){ return this.position;}

    public void setPosition(int position) {
        this.oldPosition = this.position;
        this.position = position;
        notifyObservers(new PlayerUpdateMessage(this));
    }

    public int getOldPosition() { return this.oldPosition; }

    public int getAdrenalineState() { return adrenalineState;}

    public int[] getAmmoBox() {
        return ammoBox;
    }

    public void addAmmoBox(int[] grabbedAmmoBox) {
        for (int i = 0; i < 3; i++) {
            ammoBox[i] = (ammoBox[i] + grabbedAmmoBox[i] <= 3)? ammoBox[i] + grabbedAmmoBox[i] : 3;
        }
        notifyObservers(new PlayerUpdateMessage(this));
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

    public void consumePowerup(PowerupCard powerup){
        switch (powerup.getColor()){
            case RED: tempAmmoBox[0]++; break;
            case BLUE: tempAmmoBox[1]++; break;
            case YELLOW: tempAmmoBox[2]++; break;
        }
        powerupCards.remove(powerup);
        notifyObservers(new PlayerUpdateMessage(this));
    }


    public PowerupCard getPowerupCard(int powerupID){
        for(PowerupCard puc : powerupCards){
            if(puc.getPowerupID() == powerupID) return puc;
        }
        return null;
    }

    public void addPowerupCard(PowerupCard powerupCard) {
        powerupCards.add(powerupCard);
        notifyObservers(new PlayerUpdateMessage(this));
    }

    public boolean removePowerupCard(PowerupCard powerupCard) {
        boolean result = powerupCards.remove(powerupCard);
        if(result) notifyObservers(new PlayerUpdateMessage(this));
        return result;
    }

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
                ", marks=" + marks +
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

    public void clearTempAmmo() {
        tempAmmoBox[0] = 0;
        tempAmmoBox[1] = 0;
        tempAmmoBox[2] = 0;

    }
}
