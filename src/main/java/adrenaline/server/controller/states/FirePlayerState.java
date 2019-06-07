package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.constraints.CardinalDirectionConstraint;
import adrenaline.server.model.constraints.InRadiusConstraint;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsGenerator;

import java.util.ArrayList;

public class FirePlayerState implements FiremodeSubState {

    private Lobby lobby;
    private Firemode thisFiremode;

    private TargetsGenerator targetsGenerator;
    private int targetsLimit;
    private int pushRange;
    private ArrayList<int[]> dmgmrkEachTarget;
    private Color selectedTarget=null;
    private ArrayList<Integer> targetValidSquares;

    public void setContext(Lobby lobby, Firemode firemode) {
        this.lobby = lobby;
        this.thisFiremode = firemode;
        lobby.sendCurrentPlayerValidSquares(firemode);
    }

    public String runAction() { return "Select your target(s) or GO BACK."; }

    public String grabAction() {
        return "Select your target(s) or GO BACK.";
    }

    public String shootAction() {
        return "Select your target(s) or GO BACK.";
    }

    public String selectPlayers(ArrayList<Color> playersColor) {
        playersColor = new ArrayList<>(playersColor.subList(0, targetsLimit));
        //targetsGenerate
        try {
            lobby.applyFire(thisFiremode, playersColor);
            selectedTarget = playersColor.get(1);
            lobby.incrementExecutedActions();
        } catch (InvalidTargetsException e) { return "Invalid targets!"; }
        if(pushRange<1){
            FiremodeSubState nextStep = thisFiremode.getNextStep();
            nextStep.setContext(lobby, thisFiremode);
            lobby.setState(nextStep);
        }
        else{
            ArrayList<Color> selectedTargets = new ArrayList<>();
            ArrayList<RangeConstraint> moveConstraints = new ArrayList<>();
            selectedTargets.add(selectedTarget);
            moveConstraints.add(new InRadiusConstraint(pushRange));
            moveConstraints.add(new CardinalDirectionConstraint());
            targetValidSquares = lobby.sendTargetValidSquares(selectedTargets, moveConstraints);
        }
        return "OK";
    }

    public String selectSquare(int index) {
        if(pushRange<1) return "Select your target(s) or GO BACK.";
        if(selectedTarget==null) return "Select your target(s) first.";
        if(!targetValidSquares.contains(index)) return "You can't move your target there!";
        lobby.movePlayer(index, selectedTarget);
        FiremodeSubState nextStep = thisFiremode.getNextStep();
        nextStep.setContext(lobby, thisFiremode);
        lobby.setState(nextStep);
        return "OK";
    }

    public String selectPowerUp(PowerupCard powerUp) {
        return null;
    }

    public String selectWeapon(int weaponID) {
        return "Select your target(s) or GO BACK.";
    }

    public String selectFiremode(int firemode) {
        return "Select your target(s) or GO BACK.";
    }

    public String moveSubAction() {
        //TODO
        return null;
    }


    public String endOfTurnAction() {
        return "Select your target(s) or GO BACK.";
    }

    public String goBack() {
        lobby.setState(new ShootState(lobby));
        return "OK";
    }

    public String selectAvatar(Color color) {
        return "KO";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
