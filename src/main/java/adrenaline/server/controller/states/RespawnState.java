package adrenaline.server.controller.states;

import adrenaline.server.exceptions.InvalidCardException;
import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class RespawnState implements GameState {

    private Lobby lobby;
    private boolean firstRound=false;

    public RespawnState(Lobby lobby){
        this.lobby = lobby;
    }
    public RespawnState(Lobby lobby, boolean firstRound){
        this.lobby = lobby;
        this.firstRound = firstRound;
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSquare(int index) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    /**
     *
     * To do the select PowerUp request which received from client terminal
     *
     * @param powerUp The powerupID which the player selected
     * @return The result of this request to client
     *
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        lobby.respawnWithPowerup(powerUp);
        if(firstRound){
            lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        }else {
            lobby.endTurn(false);
        }
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "You are dead! Discard a powerup card to choose your spawn point";
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
    public String selectAmmo(Color color) { return "You are dead! Discard a powerup card to choose your spawn point"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
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
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAvatar(Color color) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }
}
