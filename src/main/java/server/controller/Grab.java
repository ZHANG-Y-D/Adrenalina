package server.controller;

import server.model.AmmoCard;
import server.model.Color;
import server.model.Player;
import server.model.WeaponCard;

 public class Grab {


     /**
      * For Grab AmmoTile in Map Square
      *
      * @param grabber The player who wants to do Grab Action
      *
      * @return true:The action is successful, false:unsuccessful cause of the ammotile have already got
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
            if (grabbedAmmoContent[i]<3)
                grabbedAmmoContent[i]=grabbedAmmoContent[i]+oldAmmoContent[i];
        }
        grabber.setAmmoBox(grabbedAmmoContent);

        //3. If the tile depicts a powerup card, draw one.
        if (grabbedAmmoContent[3]!=0){
            grabPowerup(grabber);
        }

        return true;

    }

     /**
      * For Grab WeaponCard in Map Square,Only for this player do not have to Switch
      *
      * @param graber The player who wants to do Grab Action
      * @param numWeapon Which weapon Card the Player wants to Grab from 1 to 3
      *
      * @return true:Action successful,
      * false: Action unsuccessful cause of the weapon position is empty or AmmoBox not enough
      */

    public static boolean grabWeaponCard(Player graber,int numWeapon){

        //1. Choose 1 of the spawnpoint's 3 weapons. And
        //Take Weapon and deleted from Map
        WeaponCard gotWeaponCard;
        gotWeaponCard=graber.getLobby().getMap().getSquare(graber.getPosition()).removeWeaponCardFromDeck(numWeapon);
        if (gotWeaponCard==null)
            return false;

        if (payForWeapon(graber,gotWeaponCard)) {
            graber.addWeaponCard(gotWeaponCard);
            return true;
        }
        else
            return false;

    }


     /**
      * For Grab WeaponCard in Map Square
      * Only for this player has to Switch cause of he already has 3 weapon cards
      *
      * @param graber The player who wants to do Grab Action
      * @param numWeapon Which weapon Card the Player wants to Grab from 1 to 3
      * @param numWeaponSwitch Which Weapon Card the player wants to switch from 1 to 3
      *
      * @return true:Action successful,
      * false: Action unsuccessful cause of the weapon position is empty or AmmoBox not enough
      */

    public static boolean grabWeaponCard(Player graber,int numWeapon,int numWeaponSwitch){


        //Exchange WeaponCard
        WeaponCard gotWeapon;
        WeaponCard exchangeWeapon;

        gotWeapon=graber.getLobby().getMap().getSquare(graber.getPosition()).removeWeaponCardFromDeck(numWeapon);
        if (gotWeapon==null)
            return false;
        exchangeWeapon=graber.getWeaponCard().remove(numWeapon);
        graber.getLobby().getMap().getSquare(graber.getPosition()).getWeaponCardDeck().add(exchangeWeapon);

        if (payForWeapon(graber,gotWeapon)) {
            graber.addWeaponCard(gotWeapon);
            return true;
        }
        else
            return false;

    }

     /**
      *
      * Use to grabWeaponCard Class, Pay AmmoBox for this Weapon card
      *
      * @param graber The player who wants to do Grab Action
      * @param gotWeaponCard Which weapon Card the Player has got.
      *
      *
      * @return true:AmmoBox is enough to pay, false AmmoBox is not enough to pay
      *
      */

    private static boolean payForWeapon(Player graber, WeaponCard gotWeaponCard){

        int[] ammoCost;
        int[] ownAmmo;
        Color gratisColor;
        int i=-1;

        //2. Pay the cost.
        ammoCost=gotWeaponCard.getAmmoCost();
        gratisColor=gotWeaponCard.getGratisAmmo();
        ownAmmo=graber.getAmmoBox();

        switch (gratisColor){
            case RED: i=0;
                    break;
            case BLUE:i=1;
                    break;
            case YELLOW:i=2;
                    break;
        }

        for (int j=0;j<3;j++){
            if (i==j)
                ownAmmo[j]=ownAmmo[j]-ammoCost[j]+1;
            else
                ownAmmo[j]=ownAmmo[j]-ammoCost[j];

            if (ownAmmo[j]<0)
                return false;
        }
        graber.setAmmoBox(ownAmmo);
        return true;
    }


     /**
      *
      * Use to grab powerup card
      *
      * @param graber The player who wants to do Grab Action
      *
      * @return void
      *
      */
    private static void grabPowerup(Player graber){

        if (graber.getPowerup().size()<3)
            graber.addPowerup(graber.getLobby().getDeckPowerup().draw());

    }


}
