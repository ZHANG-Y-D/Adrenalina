package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class SelectActionState implements GameState {

    private Lobby lobby;

    public SelectActionState(Lobby lobby){
        this.lobby = lobby;
    }

    /**
     *
     * To do the run action request which received from client terminal
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String runAction() {
        if (lobby.getExecutedActions() >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new RunState(lobby,3));
            return "OK Select the square you want to move in";
        }
    }

    /**
     *
     * To do the grab action request which received from client terminal
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String grabAction() {
        if (lobby.getExecutedActions() >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new GrabState(lobby, lobby.getCurrentPlayerAdrenalineState() > 0 ? 2 : 1));
            return "OK Select something to grab";
        }
    }

    /**
     *
     * To do the shoot action request which received from client terminal
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String shootAction() {
        if (lobby.getExecutedActions() >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new ShootState(lobby));
            return "OK Select a weapon or a powerup to gain ammo";
        }
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select an action!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSquare(int index) {
        return "Select an action!";
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
         return lobby.usePowerup(powerUp);
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) { return "Select an action!"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "Select an action!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "Select an action!"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        return "Select an action!";
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
        lobby.setState(new ReloadState(lobby));
        return "OK Select a weapon to reload";
    }

    /**
     * The client can't do this at current time
     */
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
    public String selectAvatar(Color color) { return "KO"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) { return "KO"; }

}
