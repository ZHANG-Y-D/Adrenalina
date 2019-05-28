package adrenaline.server.controller;

import adrenaline.Color;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

public class Grab {



     /**
      * For Grab AmmoTile in Map SquareAmmo
      *
      * @param grabber The player who wants to do Grab Action
      *
      * @return true:The action is successful,
      * false:unsuccessful cause of the ammotile have already got
      *     or this square not is Weapon SquareAmmo instead of Ammo SquareAmmo
      *
      */


     /**
      * For Grab WeaponCard in Map SquareAmmo,Only for this player do not have to Switch
      *
      * @param grabber The player who wants to do Grab Action
      * @param numWeapon Which weapon Card the Player wants to Grab from 0 to 2.
      * @param discardPowerup A arrayList include the seq num of Powerup Card that player wants to discard for pay Weapon Card
      *
      * @return true:Action successful,
      * false: Action unsuccessful cause of the weapon position is empty,
      * AmmoBox not enough or this player already has 3 Weapon Card,he have to add numWeaponSwitch parameter
      */

    /*public static boolean grabWeaponCard(Player grabber, int numWeapon, ArrayList<PowerupCard> discardPowerup){

        //1. Choose 1 of the spawnpoint's 3 weapons. And
        //Take Weapon and deleted from Map
        WeaponCard gotWeaponCard;
        gotWeaponCard=grabber.getLobby().getMap().getSquare(grabber.getPosition()).getWeaponCardFromDeck(numWeapon);
        if (gotWeaponCard != null &&
                grabber.getWeaponCard().size()<3 &&
                payForWeapon(grabber,gotWeaponCard,gotWeaponCard.getFreeAmmo(),discardPowerup)) {
            grabber.addWeaponCard(gotWeaponCard);
            grabber.getLobby().getMap().getSquare(grabber.getPosition()).removeWeaponCardFromDeck(numWeapon);
            return true;
        }
        else
            return false;
    }*/

     /**
      * For Grab WeaponCard in Map SquareAmmo
      * Only for this player has to Switch cause of he already has 3 weapon cards
      *
      * @param grabber The player who wants to do Grab Action
      * @param numWeapon Which weapon Card the Player wants to Grab from 0 to 2
      * @param discardPowerup A arrayList include the seq num of Powerup Card that player wants to discard for pay Weapon Card
      * @param numWeaponSwitch Which Weapon Card the player wants to switch from 0 to 2
      *
      * @return true:Action successful,
      * false: Action unsuccessful cause of the weapon position is empty or AmmoBox not enough
      */

    /*public static boolean grabWeaponCard(Player grabber, int numWeapon, ArrayList<PowerupCard> discardPowerup,int numWeaponSwitch){

        //Exchange WeaponCard
        WeaponCard gotWeaponCard;
        WeaponCard exchangeWeapon;

        gotWeaponCard=grabber.getLobby().getMap().getSquare(grabber.getPosition()).getWeaponCardFromDeck(numWeapon);
        if ( gotWeaponCard != null &&
                payForWeapon(grabber,gotWeaponCard,gotWeaponCard.getFreeAmmo(),discardPowerup)){
            exchangeWeapon=grabber.getWeaponCard().remove(numWeaponSwitch);
            grabber.getLobby().getMap().getSquare(grabber.getPosition()).removeWeaponCardFromDeck(numWeapon);
            grabber.getLobby().getMap().getSquare(grabber.getPosition()).getWeaponCard().add(exchangeWeapon);
            grabber.getWeaponCard().add(gotWeaponCard);
            return true;
        }
        else
            return false;

    }*/





     /**
      *
      * Use to grabWeaponCard Class, Pay AmmoBox for this Weapon card.
      * This is a package-private class
      *
      * @param grabber The player who wants to do Grab Action
      * @param gotWeaponCard Which weapon Card the Player has got.
      * @param gratisColor Input gratisColor, if it's case of reload,input null
      * @param discardPowerup A arrayList include the seq num of Powerup Card that player wants to discard for pay Weapon Card
      *
      * @return true:AmmoBox is enough to pay, false AmmoBox is not enough to pay
      *
      */

    static boolean payForWeapon(Player grabber, WeaponCard gotWeaponCard, Color gratisColor, ArrayList<PowerupCard> discardPowerup){

        int[] ammoCost;
        int[] ownAmmo;


        //2. Pay the cost.
        ammoCost=gotWeaponCard.getAmmoCost().clone();
        ownAmmo=grabber.getAmmoBox();

        if (gratisColor != null) {
            switch (gratisColor) {
                case RED:
                    ammoCost[0]--;
                    break;
                case BLUE:
                    ammoCost[1]--;
                    break;
                case YELLOW:
                    ammoCost[2]--;
                    break;
            }
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
        grabber.addAmmoBox(ownAmmo);
        return true;
    }


     /**
      *
      * Use to grab powerup card
      *
      * @param grabber The player who wants to do Grab Action
      *
      */
/*
    private static void grabPowerup(Player grabber){

        if (grabber.getPowerupCards().size()<3)
            grabber.addPowerup(grabber.getLobby().getDeckPowerup().draw());

    }
*/

}
