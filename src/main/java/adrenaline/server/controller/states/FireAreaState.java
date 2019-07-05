package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;
import adrenaline.server.model.constraints.TargetsGenerator;

import java.util.ArrayList;

/**
 * Handles an area target fire action. The range of effect is generated from the selection of a single "root" square.
 *
 */
public class FireAreaState implements FiremodeSubState {

    private Lobby lobby = null;
    private WeaponCard weapon = null;
    private Firemode thisFiremode = null;
    private boolean actionExecuted = false;


    private TargetsGenerator targetsGenerator = null;
    private ArrayList<int[]> dmgmrkEachSquare;
    private ArrayList<Integer> validSquares;

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
        validSquares = lobby.sendCurrentPlayerValidSquares(firemode);
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() { return "Select your target area or GO BACK."; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "Select your target area or GO BACK.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() { return "Select your target area or GO BACK."; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return null;
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
        if(!validSquares.contains(index)) return "You can't shoot there!";
        ArrayList<Player> targets = lobby.generateTargets(targetsGenerator, index);
        ArrayList<int[]> dmgmrkEachTarget = new ArrayList<>();
        int k=0;
        dmgmrkEachTarget.add(dmgmrkEachSquare.get(k));
        for(int i=1; i<targets.size(); i++){
            if(targets.get(i).getPosition() != targets.get(i-1).getPosition()) k++;
            dmgmrkEachTarget.add(dmgmrkEachSquare.get(k<dmgmrkEachSquare.size() ? k : dmgmrkEachSquare.size()-1));
        }
        try {
            lobby.applyFire(thisFiremode, targets, dmgmrkEachTarget);
            if(!actionExecuted) {
                lobby.incrementExecutedActions();
                lobby.payCost(thisFiremode.getExtraCost());
                weapon.setLoaded(false);
                actionExecuted = true;
            }
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
            return "OK HIT!";
        } catch (InvalidTargetsException e) { return "You can't shoot there!"; }
    }



    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return null;
    }



    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "Select your target area or GO BACK.";
    }


    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) { return "Select your target area or GO BACK."; }


    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "Select your target area or GO BACK."; }

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
    @Override
    public String endOfTurnAction() {
        return "Select your target area or GO BACK.";
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
    public String selectAvatar(Color color) { return "KO"; }

    /**
     * The client can't do this at current time
     */
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
