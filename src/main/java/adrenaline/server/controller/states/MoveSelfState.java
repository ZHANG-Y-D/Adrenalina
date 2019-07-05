package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;
import adrenaline.server.model.constraints.CardinalDirectionConstraint;
import adrenaline.server.model.constraints.ExcRadiusConstraint;
import adrenaline.server.model.constraints.RangeConstraint;

import java.util.ArrayList;

/**
 * Allows the player to move during a fire action, for weapons that permit it.
 */
public class MoveSelfState implements FiremodeSubState {

    private int allowedMovement;
    private boolean forced = false;
    private boolean charge = false;
    private Lobby lobby = null;
    private WeaponCard weapon = null;
    private Firemode thisFiremode = null;
    private boolean actionExecuted;
    private FiremodeSubState callBackState = null;
    private ArrayList<Integer> validSquares = null;

    /**
     *
     * The constructor for init allowedMovement attitude
     *
     * @param allowedMovement
     */
    public MoveSelfState(int allowedMovement){
        this.allowedMovement = allowedMovement;
    }

    /**
     *
     * To set the operation context
     *
     * @param lobby The current lobby
     * @param weapon The current weapon card
     * @param firemode The current firemode
     * @param actionExecuted A boolean value index if this action is executed
     *
     */
    public void setContext(Lobby lobby, WeaponCard weapon, Firemode firemode, boolean actionExecuted, FiremodeSubState callBackState) {
        this.callBackState = callBackState;
        setContext(lobby, weapon, firemode, actionExecuted);
    }

    /**
     *
     * To set the operation context
     *
     * @param lobby The current lobby
     * @param weapon The current weapon card
     * @param firemode The current firemode
     * @param actionExecuted A boolean value index if this action is executed
     */
    @Override
    public void setContext(Lobby lobby, WeaponCard weapon, Firemode firemode, boolean actionExecuted) {
        this.lobby = lobby;
        this.weapon = weapon;
        thisFiremode = firemode;
        this.actionExecuted = actionExecuted;
        ArrayList<RangeConstraint> constraints = new ArrayList<>();
        if(forced) constraints.add(new ExcRadiusConstraint(0));
        if(charge) constraints.add(new CardinalDirectionConstraint());
        validSquares = lobby.sendCurrentPlayerValidSquares(allowedMovement, constraints);
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "Select the square you want to move in";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "Select the square you want to move in";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "Select the square you want to move in";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select the square you want to move in";
    }

    /**
     *
     * Moves the player to the selected squares, if it's a valid movement.
     *
     * @param index The square index from 0 to 11
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String selectSquare(int index) {
        if(!validSquares.contains(index)) return "You can't move there! Select a valid square or GO BACK.";
        else{
            lobby.movePlayer(index);
            if(!actionExecuted){
                lobby.incrementExecutedActions();
                lobby.payCost(thisFiremode.getExtraCost());
                weapon.setLoaded(false);
                actionExecuted=true;
            }
            if(callBackState==null){
                FiremodeSubState nextStep = thisFiremode.getNextStep();
                if(nextStep!=null){
                    nextStep.setContext(lobby, weapon, thisFiremode, actionExecuted);
                    lobby.setState(nextStep);
                }else lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
            }else {
                callBackState.setContext(lobby, weapon, thisFiremode, true);
                lobby.setState(callBackState);
            }
            return "OK";
        }
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "Select the square you want to move in";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "Select the square you want to move in";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "Select the square you want to move in";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "Select the square you want to move in"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        return "Select which square you want to move in";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "Select the square you want to move in";
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
        if(callBackState==null){
            lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        }else {
            callBackState.setContext(lobby, weapon, thisFiremode, true);
            lobby.setState(callBackState);
        }
        return "OK";
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
