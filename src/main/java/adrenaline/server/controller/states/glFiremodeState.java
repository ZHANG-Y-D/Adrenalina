package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;
import adrenaline.server.model.constraints.InRadiusConstraint;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.SameSquareConstraint;

import java.util.ArrayList;

public class glFiremodeState implements FiremodeSubState {

    private Lobby lobby;
    private WeaponCard weapon;
    private Firemode thisFiremode;

    private ArrayList<int[]> dmgmrkEach;
    private ArrayList<Integer> validSquares;
    private ArrayList<Integer> targetValidSquares;
    private ArrayList<Color> selectedTarget = null;
    private boolean pushing = false;
    private boolean areaCompleted = false;
    private boolean playerCompleted = false;

    @Override
    public void setContext(Lobby lobby, WeaponCard weapon, Firemode firemode, boolean actionExecuted) {
        this.lobby = lobby;
        this.weapon = weapon;
        this.thisFiremode = firemode;
        validSquares = lobby.sendCurrentPlayerValidSquares(firemode);
    }

    @Override
    public String runAction() {return "Select your target(s) or GO BACK."; }

    @Override
    public String grabAction() {return "Select your target(s) or GO BACK."; }

    @Override
    public String shootAction() {return "Select your target(s) or GO BACK."; }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        if(playerCompleted) return "You can't target any more players! Select a square or GO BACK.";
        ArrayList<Player> targets = lobby.generateTargets(null, new ArrayList<>(playersColor.subList(0,1)));
        try {
            lobby.applyFire(thisFiremode, targets, dmgmrkEach);
            selectedTarget = new ArrayList<>();
            selectedTarget.add(playersColor.get(0));
            ArrayList<RangeConstraint> moveConstraints = new ArrayList<>();
            moveConstraints.add(new InRadiusConstraint(1));
            pushing=true;
            if(!areaCompleted) {
                playerCompleted = true;
                lobby.incrementExecutedActions();
                lobby.payCost(thisFiremode.getExtraCost());
                weapon.setLoaded(false);
            }
            targetValidSquares = lobby.sendTargetValidSquares(selectedTarget, moveConstraints);
            return "OK";
        } catch (InvalidTargetsException e) { return "Invalid targets!"; }
    }

    @Override
    public String selectSquare(int index) {
        String completedmsg = "";
        if(pushing) {
            if(!targetValidSquares.contains(index)) return "You can't move your target there!";
            lobby.movePlayer(index, selectedTarget.get(0));
            if(areaCompleted){
                lobby.clearTempAmmo();
                lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
                completedmsg = " HIT!";
            }
            else{
                pushing=false;
                lobby.sendCurrentPlayerValidSquares(thisFiremode);
            }
            return "OK"+completedmsg;
        }else {
            if(areaCompleted) return "You can't target any more squares! Select a player or GO BACK.";
            if(!validSquares.contains(index)) return "You can't shoot there!";
            ArrayList<Player> targets = lobby.generateTargets(new SameSquareConstraint(), index);
            try {
                lobby.applyFire(thisFiremode, targets, dmgmrkEach);
                if(playerCompleted){
                    lobby.clearTempAmmo();
                    lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
                    completedmsg = " HIT!";
                }
                else{
                    lobby.incrementExecutedActions();
                    weapon.setLoaded(false);
                    lobby.payCost(thisFiremode.getExtraCost());
                    areaCompleted=true;
                }
                lobby.sendCurrentPlayerValidSquares(thisFiremode);
                return "OK"+completedmsg;
            } catch (InvalidTargetsException e) { return "Invalid targets!"; }
        }
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {return "Select your target(s) or GO BACK."; }

    @Override
    public String selectWeapon(int weaponID) {return "Select your target(s) or GO BACK."; }

    @Override
    public String selectFiremode(int firemode) {return "Select your target(s) or GO BACK."; }

    @Override
    public String selectAmmo(Color color) {
        return "Select your target(s) or GO BACK.";
    }

    @Override
    public String moveSubAction() { return "You can't do that now!"; }

    @Override
    public String endOfTurnAction() {return "Select your target(s) or GO BACK."; }

    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    @Override
    public String goBack() {
        lobby.setState(new SelectActionState(lobby));
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) { return "KO";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) { return "KO"; }
}
