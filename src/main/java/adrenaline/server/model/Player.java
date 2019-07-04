package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.PlayerUpdateMessage;
import adrenaline.server.Observable;
import adrenaline.server.network.Client;

import java.util.ArrayList;
import java.util.Collections;




/**
 *
 * This class is for new a Player
 *
 */

public class Player extends Observable{

    private Avatar avatar;
    private int[] ammoBox;
    private int[] tempAmmoBox;
    private ArrayList<Color> damage;
    private ArrayList<Color> marks;
    private ArrayList<PowerupCard> powerupCards;
    private ArrayList<WeaponCard> weaponCards;
    private boolean firstRound = true;


    private int position;
    private int oldPosition;  //Last position
    private int adrenalineState;


    private boolean alive; //For index is this player still alive. It can help shooter count score

    public Player(Avatar avatar, String clientNickname, ArrayList<Client> clients){
        this.avatar = avatar;
        damage = new ArrayList<>();
        powerupCards = new ArrayList<>();
        weaponCards = new ArrayList<>();
        marks = new ArrayList<>();
        adrenalineState = 0;
        ammoBox = new int[]{1,1,1};
        tempAmmoBox = new int[]{0,0,0};
        alive = false;
        position = -1;
        clients.forEach(this::attach);
        observers.forEach(x -> x.setPlayerColorInternal(clientNickname, avatar.getColor()));
    }


    public void setFirstRound() { firstRound = false; }

    public boolean isFirstRound() { return  firstRound; }




    /**
     * Add damage for this player
     * @return This boolean is for index if the this play is already died.
     */

    public boolean applyDamage(Color damageOrigin, int amount, boolean extra) {
        if(!extra && amount>0 && marks.contains(damageOrigin)){
            amount += Collections.frequency(marks,damageOrigin);
            marks.removeIf(damageOrigin::equals);
        }
        for(int i=0; i<amount && (damage.size() < 12); i++) damage.add(damageOrigin);

        if (damage.size() >= 11) {
            alive = false;
            adrenalineState=0;
        }
        else if (damage.size() >= 6) adrenalineState = 2;
        else if (damage.size() >= 3) adrenalineState = 1;
        notifyObservers(new PlayerUpdateMessage(this));
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
        notifyObservers(new PlayerUpdateMessage(this));
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
            if(ammoCost[i] - tempAmmoBox[i] > 0){
                ammoBox[i] -= (ammoCost[i] - tempAmmoBox[i]);
                tempAmmoBox[i] = 0;
            }else{
                tempAmmoBox[i] -= ammoCost[i];
            }
        }
        notifyObservers(new PlayerUpdateMessage(this));
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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive){ this.alive = alive; }

    public Color getColor() {
        return avatar.getColor();
    }

    public int[] getTempAmmoBox() { return tempAmmoBox; }

    public void clearTempAmmo() {
        tempAmmoBox[0] = 0;
        tempAmmoBox[1] = 0;
        tempAmmoBox[2] = 0;

    }

    public void clearDamage(){
        damage.clear();
        notifyObservers(new PlayerUpdateMessage(this));
    }
}
