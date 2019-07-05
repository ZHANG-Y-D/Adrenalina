package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;
import adrenaline.server.model.constraints.CardinalDirectionConstraint;
import adrenaline.server.model.constraints.InRadiusConstraint;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsGenerator;

import java.util.ArrayList;

/**
 *
 * The fire players state,to do fire player action
 *
 */
public class FirePlayerState implements FiremodeSubState {

    private Lobby lobby = null;
    private WeaponCard weapon = null;
    private Firemode thisFiremode = null;
    private boolean actionExecuted = false;

    private TargetsGenerator targetsGenerator = null;
    private int targetsLimit;
    private int pushRange;
    private ArrayList<int[]> dmgmrkEachTarget;
    private Color selectedTarget=null;
    private ArrayList<Integer> targetValidSquares;

    /**
     *
     * To set the operation context
     *
     * @param lobby The current lobby
     * @param weapon The current weapon card
     * @param firemode The current firemode
     * @param actionExecuted A boolean value index if this action is executed
     */
    public void setContext(Lobby lobby, WeaponCard weapon, Firemode firemode, boolean actionExecuted) {
        this.lobby = lobby;
        this.weapon = weapon;
        this.thisFiremode = firemode;
        this.actionExecuted = actionExecuted;
        lobby.sendCurrentPlayerValidSquares(firemode);
    }

    /**
     * The client can't do this at current time
     */
    public String runAction() { return "Select your target(s) or GO BACK."; }

    /**
     * The client can't do this at current time
     */
    public String grabAction() {
        return "Select your target(s) or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
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
    public String selectPlayers(ArrayList<Color> playersColor) {
        playersColor = new ArrayList<>(playersColor.size()>targetsLimit ? playersColor.subList(0, targetsLimit) : playersColor.subList(0, playersColor.size()));
        ArrayList<Player> targets = lobby.generateTargets(targetsGenerator, playersColor);
        try {
            lobby.applyFire(thisFiremode, targets, dmgmrkEachTarget);
            selectedTarget = playersColor.get(0);
            if(!actionExecuted){
                lobby.incrementExecutedActions();
                lobby.payCost(thisFiremode.getExtraCost());
                weapon.setLoaded(false);
                actionExecuted=true;
            }
        } catch (InvalidTargetsException e) { return "Invalid targets!"; }
        if(pushRange<1){
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
                nextStep.setContext(lobby,weapon , thisFiremode, actionExecuted);
                lobby.setState(nextStep);
            }
        }
        else{
            ArrayList<Color> selectedTargets = new ArrayList<>();
            ArrayList<RangeConstraint> moveConstraints = new ArrayList<>();
            selectedTargets.add(selectedTarget);
            moveConstraints.add(new InRadiusConstraint(pushRange));
            moveConstraints.add(new CardinalDirectionConstraint());
            targetValidSquares = lobby.sendTargetValidSquares(selectedTargets, moveConstraints);
        }
        return "OK HIT!";
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
    public String selectSquare(int index) {
        if(pushRange<1) return "Select your target(s) or GO BACK.";
        if(selectedTarget==null) return "Select your target(s) first.";
        if(!targetValidSquares.contains(index)) return "You can't move your target there!";
        lobby.movePlayer(index, selectedTarget);
        FiremodeSubState nextStep = thisFiremode.getNextStep();
        if(nextStep==null){
            MoveSelfState mvSelStep=thisFiremode.getMoveSelfStep();
            if(mvSelStep==null){
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
    }

    /**
     * The client can't do this at current time
     */
    public String selectPowerUp(PowerupCard powerUp) {
        return null;
    }

    /**
     * The client can't do this at current time
     */
    public String selectWeapon(int weaponID) {
        return "Select your target(s) or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
    public String selectFiremode(int firemode) {
        return "Select your target(s) or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
    public String selectAmmo(Color color) {
        return "Select your target(s) or GO BACK.";
    }

    /**
     *
     * To do the move Sub Action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
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
    public String endOfTurnAction() {
        return "Select your target(s) or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    /**
     *
     * To do the go Back action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    public String goBack() {
        lobby.setState(new ShootState(lobby));
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    public String selectAvatar(Color color) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
