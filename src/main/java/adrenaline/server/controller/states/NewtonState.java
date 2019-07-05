package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.constraints.CardinalDirectionConstraint;
import adrenaline.server.model.constraints.InRadiusConstraint;
import adrenaline.server.model.constraints.RangeConstraint;

import java.util.ArrayList;

/**
 *
 * The new state to implement NewTon power up card's action
 */
public class NewtonState implements GameState {
    private Lobby lobby;
    private PowerupCard thisPowerup;
    private Color currPlayer;
    private Color selectedPlayer;
    private ArrayList<Integer> validSquares;


    /**
     *
     * The constructor init  lobby powerup and currPlayer attitudes
     *
     *
     * @param lobby The current lobby
     * @param powerup The server side PowerupCard reference
     * @param currPlayer The current player who requires this action
     */
    public NewtonState(Lobby lobby, PowerupCard powerup, Color currPlayer){
        this.lobby = lobby;
        thisPowerup = powerup;
        this.currPlayer = currPlayer;
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() { return "Select the target you want to move."; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() { return "Select the target you want to move."; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() { return "Select the target you want to move."; }

    /**
     *
     * To do the select player action request which received from client terminal
     *
     * @param playersColor The ArrayList of players' color
     * @return The result of this request to client
     *
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        if(playersColor.get(0).equals(currPlayer)) return "You can't use this powerup on yourself!";
        playersColor.subList(1, playersColor.size()).clear();
        selectedPlayer = playersColor.get(0);
        ArrayList<RangeConstraint> moveConstraints = new ArrayList<>();
        moveConstraints.add(new InRadiusConstraint(2));
        moveConstraints.add(new CardinalDirectionConstraint());
        validSquares = lobby.sendTargetValidSquares(playersColor, moveConstraints);
        return "OK";
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
        if(validSquares==null) return "Select a target first!";
        if(validSquares.contains(index)){
            lobby.movePlayer(index, selectedPlayer);
            lobby.removePowerup(thisPowerup);
            lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
            return "OK Target moved.";
        }else return "You can't move your target there! Please select a valid square.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) { return "Select the target you want to move."; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) { return "Select the target you want to move."; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) { return "Select the target you want to move."; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "You can't do that now!"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() { return "Select the target you want to move."; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() { return "Select the target you want to move."; }

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
