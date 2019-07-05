package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

/**
 *
 * The MoveEnemyState can move the other players in the same game
 *
 */
public class MoveEnemyState implements FiremodeSubState {

    private int targetsLimit;
    private int allowedMovement;
    private ArrayList<int[]> dmgmrkEachTarget;
    private Lobby lobby = null;
    private WeaponCard weapon = null;
    private Firemode thisFiremode = null;
    private boolean actionExecuted;
    private ArrayList<Integer> validSquares = null;
    private ArrayList<Color> selectedPlayers = null;

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
        this.thisFiremode = firemode;
        this.actionExecuted = actionExecuted;
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "Select your target(s) or GO BACK.";
    }


    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "Select your target(s) or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "Select your target(s) or GO BACK.";
    }

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
        this.selectedPlayers = new ArrayList<>(playersColor.size()>targetsLimit ? playersColor.subList(0, targetsLimit) : playersColor.subList(0, playersColor.size()));
        validSquares = lobby.sendCurrentPlayerValidSquares(thisFiremode);
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
        if(selectedPlayers==null) return "Select your targets first.";
        if(!validSquares.contains(index)) return "You can't target that square! Select a valid square.";
        ArrayList<Player> targets = lobby.tryMovePlayers(selectedPlayers, index, allowedMovement);
        if(targets!=null){
            try {
                lobby.incrementExecutedActions();
                lobby.payCost(thisFiremode.getExtraCost());
                weapon.setLoaded(false);
                actionExecuted = true;
                lobby.applyFire(thisFiremode, targets, dmgmrkEachTarget);
                FiremodeSubState nextStep = thisFiremode.getNextStep();
                if(nextStep==null){
                    MoveSelfState mvSelStep=thisFiremode.getMoveSelfStep();
                    if(mvSelStep==null){
                        lobby.clearTempAmmo();
                        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
                    }
                    else{
                        mvSelStep.setContext(lobby, weapon, thisFiremode, actionExecuted);
                        lobby.setState(mvSelStep);
                    }
                }
                else{
                    nextStep.setContext(lobby, weapon, thisFiremode, actionExecuted);
                    lobby.setState(nextStep);
                }
                return "OK";
            } catch (InvalidTargetsException e) {
                return "Invalid targets!";
            }
        }else{
            selectedPlayers = null;
            return "You can't target that square!";
        }
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "Select your target(s) or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "Select your target(s) or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "Select your target(s) or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "Select your target(s) or GO BACK."; }

    /**
     *
     * To do the move Sub Action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String moveSubAction() {
        MoveSelfState moveStep = thisFiremode.getMoveSelfStep();
        if(moveStep==null) return "You can't do that!";
        else{
            moveStep.setContext(lobby, weapon, thisFiremode, actionExecuted, this);
            lobby.setState(moveStep);
            return "OK";
        }
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "Select your target(s) or GO BACK.";
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
        lobby.setState(new ShootState(lobby));
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
