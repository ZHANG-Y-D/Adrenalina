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

    public MoveSelfState(int allowedMovement){
        this.allowedMovement = allowedMovement;
    }

    public void setContext(Lobby lobby, WeaponCard weapon, Firemode firemode, boolean actionExecuted, FiremodeSubState callBackState) {
        this.callBackState = callBackState;
        setContext(lobby, weapon, firemode, actionExecuted);
    }

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

    @Override
    public String runAction() {
        return "Select the square you want to move in";
    }

    @Override
    public String grabAction() {
        return "Select the square you want to move in";
    }

    @Override
    public String shootAction() {
        return "Select the square you want to move in";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select the square you want to move in";
    }

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

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "Select the square you want to move in";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select the square you want to move in";
    }

    @Override
    public String selectFiremode(int firemode) {
        return "Select the square you want to move in";
    }

    @Override
    public String selectAmmo(Color color) { return "Select the square you want to move in"; }

    @Override
    public String moveSubAction() {
        return "Select which square you want to move in";
    }

    @Override
    public String endOfTurnAction() {
        return "Select the square you want to move in";
    }

    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

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

    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
