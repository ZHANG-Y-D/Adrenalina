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

public class FireAreaState implements FiremodeSubState {

    private Lobby lobby = null;
    private WeaponCard weapon = null;
    private Firemode thisFiremode = null;
    private boolean actionExecuted = false;


    private TargetsGenerator targetsGenerator = null;
    private ArrayList<int[]> dmgmrkEachSquare;
    private ArrayList<Integer> validSquares;

    @Override
    public void setContext(Lobby lobby, WeaponCard weapon, Firemode firemode, boolean actionExecuted) {
        this.lobby = lobby;
        this.weapon = weapon;
        this.thisFiremode = firemode;
        this.actionExecuted = actionExecuted;
        validSquares = lobby.sendCurrentPlayerValidSquares(firemode);
    }

    @Override
    public String runAction() { return "Select your target area or GO BACK."; }

    @Override
    public String grabAction() {
        return "Select your target area or GO BACK.";
    }

    @Override
    public String shootAction() { return "Select your target area or GO BACK."; }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return null;
    }

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
                    lobby.setState(new SelectActionState(lobby));
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

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return null;
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select your target area or GO BACK.";
    }

    @Override
    public String selectFiremode(int firemode) { return "Select your target area or GO BACK."; }

    public String moveSubAction() {
        MoveSelfState moveStep = thisFiremode.getMoveSelfStep();
        if(moveStep==null) return "You can't do that!";
        else{
            moveStep.setContext(lobby, weapon, thisFiremode, actionExecuted, this);
            lobby.setState(moveStep);
            return "OK";
        }
    }

    @Override
    public String endOfTurnAction() {
        return "Select your target area or GO BACK.";
    }

    @Override
    public String goBack() {
        lobby.setState(new ShootState(lobby));
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) { return "KO"; }


    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
