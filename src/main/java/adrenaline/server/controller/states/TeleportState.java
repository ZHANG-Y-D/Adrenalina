package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;


/**
 *
 * The TeleportState made for Teleport powerup card
 *
 */
public class TeleportState implements GameState {

    private Lobby lobby;
    private PowerupCard thisCard;
    private ArrayList<Integer> squares = null;

    /**
     *
     * The constructor init all attitudes
     *
     * @param lobby The current lobby
     * @param powerupCard The server side PowerupCard reference
     */
    public TeleportState(Lobby lobby, PowerupCard powerupCard){
        this.lobby = lobby;
        thisCard = powerupCard;
        squares = lobby.sendCurrentPlayerValidSquares(10);
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "Select the square you want to teleport in.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "Select the square you want to teleport in.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "Select the square you want to teleport in.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select the square you want to teleport in.";
    }

    /**
     *
     * To do the select square request which received from client terminal
     *
     * @param index The square index from 0 to 11
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String selectSquare(int index) {
        if(!squares.contains(index)) return "Invalid selection! Select a valid square.";
        lobby.movePlayer(index);
        lobby.removePowerup(thisCard);
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "Select the square you want to teleport in.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "Select the square you want to teleport in.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "Select the square you want to teleport in.";
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
        return "Select the square you want to teleport in.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "Select the square you want to teleport in.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    /**
     *
     * To do the go Back action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String goBack() {
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAvatar(Color color) {
        return "Select the square you want to teleport in.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "Select the square you want to teleport in.";
    }
}
