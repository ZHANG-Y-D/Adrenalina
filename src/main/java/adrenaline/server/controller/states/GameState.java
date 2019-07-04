package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

/**
 *
 * The game state interface made to realize State pattern at game flow
 *
 */
public interface GameState {

    /**
     *
     * To do the run action request which received from client terminal
     *
     * @return The result of this request to client
     *
     */
    String runAction();

    /**
     *
     * To do the grab action request which received from client terminal
     *
     * @return The result of this request to client
     *
     */
    String grabAction();

    /**
     *
     * To do the shoot action request which received from client terminal
     *
     * @return The result of this request to client
     *
     */
    String shootAction();

    /**
     *
     * To do the select player action request which received from client terminal
     *
     * @param playersColor The ArrayList of players' color
     * @return The result of this request to client
     *
     */
    String selectPlayers(ArrayList<Color> playersColor);

    /**
     *
     * To do the select square request which received from client terminal
     *
     * @param index The square index from 0 to 11
     *
     * @return The result of this request to client
     *
     */
    String selectSquare(int index);

    /**
     *
     * To do the select PowerUp request which received from client terminal
     *
     * @param powerUp The powerupID which the player selected
     * @return The result of this request to client
     *
     */
    String selectPowerUp(PowerupCard powerUp);

    /**
     *
     * To do the select Weapon request which received from client terminal
     *
     * @param weaponID The weaponID which the player selected
     *
     * @return The result of this request to client
     *
     */
    String selectWeapon(int weaponID);

    /**
     *
     * To do the select Firemode request which received from client terminal
     *
     * @param firemode The number of firemode from 0 to 2
     * @return The result of this request to client
     *
     */
    String selectFiremode(int firemode);

    /**
     *
     * To do the select Ammo request which received from client terminal
     *
     * @param color The ammo color
     *
     * @return The result of this request to client
     *
     */
    String selectAmmo(Color color);

    /**
     *
     * To do the move Sub Action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    String moveSubAction();

    /**
     *
     * To do the end Of Turn Action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    String endOfTurnAction();

    /**
     *
     * To do the select Final Frenzy Action request which received from client terminal
     *
     * @param action The frenzy action index from 0 to 2
     *
     * @return The result of this request to client
     *
     */
    String selectFinalFrenzyAction(Integer action);

    /**
     *
     * To do the go Back action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    String goBack();

    /**
     *
     * To do the select Avatar action request which received from client terminal
     *
     * @param color The avatar's color
     *
     * @return The result of this request to client
     *
     */
    String selectAvatar(Color color);

    /**
     *
     * To do the select Settings action request which received from client terminal
     *
     * @param mapID The id of map which select from client from 1 to 4
     * @param skulls The skulls selected from client from 5 to 8
     * @return The result of this request to client
     *
     */
    String selectSettings(int mapID, int skulls, String voterID);
}
