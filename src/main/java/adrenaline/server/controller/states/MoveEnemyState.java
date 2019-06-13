package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class MoveEnemyState implements FiremodeSubState {

    private int targetsLimit;
    private int allowedMovement;
    private ArrayList<int[]> dmgmrkEachTarget;
    private Lobby lobby = null;
    private Firemode thisFiremode = null;
    private boolean actionExecuted;
    private ArrayList<Integer> validSquares = null;
    private ArrayList<Color> selectedPlayers = null;

    @Override
    public void setContext(Lobby lobby, Firemode firemode, boolean actionExecuted) {
        this.lobby = lobby;
        this.thisFiremode = firemode;
        this.actionExecuted = actionExecuted;
        validSquares = lobby.sendCurrentPlayerValidSquares(firemode);
    }
    @Override
    public String runAction() {
        return "Select your target(s) or GO BACK.";
    }

    @Override
    public String grabAction() {
        return "Select your target(s) or GO BACK.";
    }

    @Override
    public String shootAction() {
        return "Select your target(s) or GO BACK.";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        this.selectedPlayers = new ArrayList<>(playersColor.subList(0, targetsLimit));
        return "OK";
    }

    @Override
    public String selectSquare(int index) {
        if(selectedPlayers==null) return "Select your targets first.";
        if(!validSquares.contains(index)) return "You can't target that square! Select a valid square.";
        ArrayList<Player> targets = lobby.tryMovePlayers(selectedPlayers, index, allowedMovement);
        if(targets!=null){
            try {
                lobby.incrementExecutedActions();
                actionExecuted = true;
                lobby.applyFire(thisFiremode, targets, dmgmrkEachTarget);
                FiremodeSubState nextStep = thisFiremode.getNextStep();
                if(nextStep==null) lobby.setState(new SelectActionState(lobby));
                else{
                    nextStep.setContext(lobby, thisFiremode, actionExecuted);
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

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "Select your target(s) or GO BACK.";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select your target(s) or GO BACK.";
    }

    @Override
    public String selectFiremode(int firemode) {
        return "Select your target(s) or GO BACK.";
    }

    @Override
    public String moveSubAction() {
        MoveSelfState moveStep = thisFiremode.getMoveSelfStep();
        if(moveStep==null) return "You can't do that!";
        else{
            moveStep.setContext(lobby, thisFiremode, actionExecuted, this);
            lobby.setState(moveStep);
            return "OK";
        }
    }

    @Override
    public String endOfTurnAction() {
        return "Select your target(s) or GO BACK.";
    }

    @Override
    public String goBack() {
        lobby.setState(new ShootState(lobby));
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
