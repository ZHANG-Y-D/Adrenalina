package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.exceptions.AlreadyLoadedException;
import adrenaline.server.exceptions.InvalidCardException;
import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

/**
 *
 * The reload state to do reload action
 *
 */
public class ReloadState implements GameState{
    private Lobby lobby;

    /**
     *
     * The constructor init lobby attitude
     * @param lobby The current lobby
     */
    public ReloadState(Lobby lobby){
        this.lobby = lobby;
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "You can't select an action now";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "You can't select an action now";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "You can't select an action now";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You can't select players now";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSquare(int index) {
        return "You can't select squares now";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        lobby.consumePowerup(powerUp);
        return "OK";
    }

    /**
     *
     * To do the select Weapon request which received from client terminal
     *
     * @param weaponID The weaponID which the player selected
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String selectWeapon(int weaponID) {
        try {
            lobby.reloadWeapon(weaponID);
        } catch (InvalidCardException e) {
            return "Invalid selection! Select a valid card.";
        } catch (AlreadyLoadedException e) {
            return "That weapon is already loaded!";
        } catch (NotEnoughAmmoException e) {
            return "You can't pay the ammo cost for that weapon! HINT: powerups can be expended too";
        }
        return "OK Weapon reloaded!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return null;
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "You can't do that now!"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        return "You can't do that now!";
    }

    /**
     *
     * To do the end Of Turn Action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String endOfTurnAction() {
        lobby.endTurn(false);
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String goBack() {
        return "You can't go back now!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAvatar(Color color) {
        return null;
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return null;
    }
}
