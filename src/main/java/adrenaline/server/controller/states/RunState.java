package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

/**
 *
 * The run state to do all ran actions
 *
 */
public class RunState implements GameState {

    private Lobby lobby;
    private ArrayList<Integer> validSquares;

    /**
     *
     * The constructor init all attitudes
     *
     * @param lobby The current lobby
     * @param range The range of the player can move
     */
    public RunState(Lobby lobby, int range){
        this.lobby = lobby;
        this.validSquares = lobby.sendCurrentPlayerValidSquares(range);
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() { return "Select a square or GO BACK to action selection!"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "Select a square or GO BACK to action selection!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() { return "Select a square or GO BACK to action selection!"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You can't do that now!";
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
        if(!validSquares.contains(index)) return "You can't move there! Please select a valid square";
        lobby.movePlayer(index);
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        lobby.incrementExecutedActions();
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "You can't do that now!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "You can't do that now!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "You can't do that now!";
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
    public String moveSubAction() { return "You can't do that now!"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "Select a square or GO BACK to action selection!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFinalFrenzyAction(Integer action) { return "You can't do that now!"; }


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
        return "OK Select an action";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
