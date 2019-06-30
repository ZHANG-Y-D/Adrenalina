package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

public class FreneticActionState implements GameState {

    private Lobby lobby;
    private ArrayList<Integer> validSquares;
    private boolean moveExecuted = false;
    private WeaponCard selectedWeapon = null;

    public FreneticActionState(Lobby lobby, int runrange){
        this.lobby = lobby;
        validSquares = lobby.sendCurrentPlayerValidSquares(runrange);
    }
    @Override
    public String runAction() {
        return "KO";
    }

    @Override
    public String grabAction() {
        return "KO";
    }

    @Override
    public String shootAction() {
        return "KO";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select which weapon you want to use first";
    }

    @Override
    public String selectSquare(int index) {
        if(!moveExecuted && validSquares.contains(index)){
            lobby.movePlayer(index);
            moveExecuted=true;
            lobby.incrementExecutedActions();
            return "OK";

        }else return "You can't move there!";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        lobby.consumePowerup(powerUp);
        return "OK";
    }

    @Override
    public String selectWeapon(int weaponID) {
        selectedWeapon = lobby.useWeapon(weaponID, true);
        if(selectedWeapon==null) return "You can't use that weapon!";
        else return "OK Select the firemode";
    }

    @Override
    public String selectFiremode(int firemode) {
        if(selectedWeapon == null) return "No weapon is selected! Please select a weapon first";
        try {
            Firemode selectedFiremode = lobby.getFiremode(selectedWeapon.getWeaponID(), firemode);
            if(selectedFiremode==null) return "This weapon does not have such firemode!";
            else {
                FiremodeSubState nextStep = selectedFiremode.getNextStep();
                nextStep.setContext(lobby, selectedWeapon, selectedFiremode, false);
                lobby.setState(nextStep);
            }
            return "OK Firemode accepted, choose your next move";
        } catch (NotEnoughAmmoException e) {
            return "You can't pay the ammo price for that firemode! HINT: powerups can be expended too";
        }
    }

    @Override
    public String selectAmmo(Color color) { return "Select a weapon or GO BACK to action selection!"; }

    @Override
    public String moveSubAction() {
        return "Select a weapon or GO BACK to action selection!";
    }

    @Override
    public String endOfTurnAction() {
        return "Select a weapon or GO BACK to action selection!";
    }

    @Override
    public String selectFinalFrenzyAction(Integer action) {
        return "Select a weaopon or GO BACK to action selection!";
    }

    @Override
    public String goBack() {
        lobby.setState(new SelectFreneticActionState(lobby));
        lobby.clearTempAmmo();
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
