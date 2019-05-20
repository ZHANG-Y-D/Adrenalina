package server.controller;

import server.model.*;

import java.util.ArrayList;

public class Grab {



     /**
      * For Grab AmmoTile in Map Square
      *
      * @param grabber The player who wants to do Grab Action
      *
      * @return true:The action is successful,
      * false:unsuccessful cause of the ammotile have already got
      *     or this square not is Weapon Square instead of Ammo Square
      *
      */

    public static boolean grabAmmoCard(Player grabber){

        AmmoCard grabbedAmmoTile;
        int[] grabbedAmmoContent;
        int[] oldAmmoContent;

        grabbedAmmoTile=grabber.getLobby().getMap().getSquare(grabber.getPosition()).getAmmoTile();
        if (grabbedAmmoTile==null)
            return false;

        grabbedAmmoContent=grabbedAmmoTile.getAmmoContent();
        oldAmmoContent=grabber.getAmmoBox();


        //4. Discard the tile. &&   1. Remove the ammo tile.
        grabber.getLobby().getDeckAmmo().addToDiscarded(grabbedAmmoTile);
        grabber.getLobby().getMap().getSquare(grabber.getPosition()).setAmmoTile(null);

        //2. Move the depicted cubes into your ammo box.
        for (int i=0;i<3;i++){
            //Your ammo box never holds more than 3 cubes of each color. Excess ammo depicted on the tile is wasted.
            oldAmmoContent[i]=grabbedAmmoContent[i]+oldAmmoContent[i];
            if (oldAmmoContent[i]>3)
                oldAmmoContent[i]=3;
        }
        grabber.setAmmoBox(oldAmmoContent);

        //3. If the tile depicts a powerup card, draw one.
        if (grabbedAmmoContent[3]!=0){
            grabPowerup(grabber);
        }

        return true;

    }




     /**
      * For Grab WeaponCard in Map Square,Only for this player do not have to Switch
      *
      * @param grabber The player who wants to do Grab Action
      * @param numWeapon Which weapon Card the Player wants to Grab from 1 to 3.
      * @param discardPowerup A arrayList include the seq num of Powerup Card that player wants to discard for pay Weapon Card
      *
      * @return true:Action successful,
      * false: Action unsuccessful cause of the weapon position is empty,
      * AmmoBox not enough or this player already has 3 Weapon Card,he have to add numWeaponSwitch parameter
      */

    public static boolean grabWeaponCard(Player grabber, int numWeapon, ArrayList<PowerupCard> discardPowerup){

        //1. Choose 1 of the spawnpoint's 3 weapons. And
        //Take Weapon and deleted from Map
        WeaponCard gotWeaponCard;
        gotWeaponCard=grabber.getLobby().getMap().getSquare(grabber.getPosition()).getWeaponCardFromDeck(numWeapon-1);
        if (gotWeaponCard != null &&
                grabber.getWeaponCard().size()<3 &&
                payForWeapon(grabber,gotWeaponCard,discardPowerup)) {
            grabber.addWeaponCard(gotWeaponCard);
            grabber.getLobby().getMap().getSquare(grabber.getPosition()).removeWeaponCardFromDeck(numWeapon-1);
            return true;
        }
        else
            return false;
    }




     /**
      * For Grab WeaponCard in Map Square
      * Only for this player has to Switch cause of he already has 3 weapon cards
      *
      * @param grabber The player who wants to do Grab Action
      * @param numWeapon Which weapon Card the Player wants to Grab from 1 to 3
      * @param discardPowerup A arrayList include the seq num of Powerup Card that player wants to discard for pay Weapon Card
      * @param numWeaponSwitch Which Weapon Card the player wants to switch from 1 to 3
      *
      * @return true:Action successful,
      * false: Action unsuccessful cause of the weapon position is empty or AmmoBox not enough
      */

    public static boolean grabWeaponCard(Player grabber, int numWeapon, ArrayList<PowerupCard> discardPowerup,int numWeaponSwitch){

        //Exchange WeaponCard
        WeaponCard gotWeaponCard;
        WeaponCard exchangeWeapon;

        gotWeaponCard=grabber.getLobby().getMap().getSquare(grabber.getPosition()).getWeaponCardFromDeck(numWeapon);
        if ( gotWeaponCard != null &&
                payForWeapon(grabber,gotWeaponCard,discardPowerup)){
            exchangeWeapon=grabber.getWeaponCard().remove(numWeaponSwitch);
            grabber.getLobby().getMap().getSquare(grabber.getPosition()).removeWeaponCardFromDeck(numWeapon);
            grabber.getLobby().getMap().getSquare(grabber.getPosition()).getWeaponCards().add(exchangeWeapon);
            grabber.getWeaponCard().add(gotWeaponCard);
            return true;
        }
        else
            return false;

    }




     /**
      *
      * Use to grabWeaponCard Class, Pay AmmoBox for this Weapon card
      *
      * @param grabber The player who wants to do Grab Action
      * @param gotWeaponCard Which weapon Card the Player has got.
      * @param discardPowerup A arrayList include the seq num of Powerup Card that player wants to discard for pay Weapon Card
      *
      * @return true:AmmoBox is enough to pay, false AmmoBox is not enough to pay
      *
      */

    private static boolean payForWeapon(Player grabber, WeaponCard gotWeaponCard, ArrayList<PowerupCard> discardPowerup){

        int[] ammoCost;
        int[] ownAmmo;
        Color gratisColor;


        //2. Pay the cost.
        ammoCost=gotWeaponCard.getAmmoCost().clone();
        gratisColor=gotWeaponCard.getGratisAmmo();
        ownAmmo=grabber.getAmmoBox();

        switch (gratisColor){
            case RED: ammoCost[0]--;
                    break;
            case BLUE:ammoCost[1]--;
                    break;
            case YELLOW:ammoCost[2]--;
                    break;
        }


        //Pay with powerupCard
        if (discardPowerup!=null) {
            for (int i = 0; i < discardPowerup.size(); i++) {
                switch (discardPowerup.get(i).getColor()) {
                    case RED:
                        if (ammoCost[0] > 0) {
                            ammoCost[0]--;
                            discardPowerup.get(i).discardPowerupCard(grabber);
                        }
                        break;
                    case BLUE:
                        if (ammoCost[1] > 0) {
                            ammoCost[1]--;
                            discardPowerup.get(i).discardPowerupCard(grabber);
                        }
                        break;
                    case YELLOW:
                        if (ammoCost[2] > 0) {
                            ammoCost[2]--;
                            discardPowerup.get(i).discardPowerupCard(grabber);
                        }
                        break;
                }
            }
        }


        for (int j=0;j<3;j++){

            ownAmmo[j]=ownAmmo[j]-ammoCost[j];
            if (ownAmmo[j]<0)
                return false;

        }
        grabber.setAmmoBox(ownAmmo);
        return true;
    }


     /**
      *
      * Use to grab powerup card
      *
      * @param grabber The player who wants to do Grab Action
      *
      */

    private static void grabPowerup(Player grabber){

        if (grabber.getPowerup().size()<3)
            grabber.addPowerup(grabber.getLobby().getDeckPowerup().draw());

    }


}
