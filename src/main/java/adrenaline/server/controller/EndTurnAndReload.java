package adrenaline.server.controller;

import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class EndTurnAndReload {


    /**
     *
     * Use to reload Weapon Card
     *
     * @param player The player who wants to do this Action
     * @param numWeapon The weapon Card index from 0 to 2 which the Player want reload
     * @param discardPowerup A arrayList include the seq num of Powerup Card that player wants to discard for pay Weapon Card
     *
     * @return True:this action is successful, False:Error,cause of the weapon card has already reload or no enough ammo
     *
     */

    /*public static boolean reloadWeapon(Player player, int numWeapon, ArrayList<PowerupCard> discardPowerup){

        if (player.getWeaponCards().get(numWeapon).isLoaded()
               || numWeapon>=3 ||
                !Grab.payForWeapon(player,player.getWeaponCards().get(numWeapon),null,discardPowerup)){
            return false;
        }
        else {
            player.getWeaponCards().get(numWeapon).setLoaded(true);
            return true;
        }
    }*/


    /**
     *
     *
     * Use to end this Turn, Use it when every time the turn is finished.
     * It will Replace any stuff you took.
     *
     * <p>
     * Replace ammo tiles with new tiles from the ammo stacks.
     * Replace weapons by drawing from the weapons deck. If the deck is empty, no new weapons will appear for the rest of the game.
     * </p>
     *
     * @param player The player who wants to do this Action
     *
     *
     */

    /*public static void playerEndTurn(Player player){

        scoreKillshotPlayer();
        player.getLobby().setSquaresCards();

    }


    /**
     *
     *
     * This is a private class,For Score all player boards that received a killshot.
     *
     *
     * <p>
     * Pose rules.
     * If you receive a killshot, tip your figure over. At the end of the active player's turn:
     * 1. Hand out points to everyone who shot you, as explained above.
     * 2. Remove all damage (and hand the tokens back).
     * 3. Pick up your figure. You are dead.
     * 4. Draw one powerup card, even if you already have three.
     * 5. Discard one of your powerup cards with no effect and place your figure on the spawnpoint indicated by it. You are alive again.
     * If you have any marks (see next page) those will remain on your player board.
     * You keep all your weapons and ammo. Loaded weapons remain loaded, and unloaded weapons remain unloaded.
     * Your damage is reset to zero, so you no longer have access to adrenaline actions. But don't worry. You'll get shot again soon
     * </p>
     *
     *
     *
     */

    private static void scoreKillshotPlayer(){

        //TODO

    }






}
