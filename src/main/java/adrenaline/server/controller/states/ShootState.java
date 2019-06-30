package adrenaline.server.controller.states;

import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

public class ShootState implements GameState {

    private Lobby lobby;
    private WeaponCard selectedWeapon = null;
    private ArrayList<Integer> validSquares;

    public ShootState(Lobby lobby){
        this.lobby = lobby;
        if(lobby.getCurrentPlayerAdrenalineState()>1) validSquares = lobby.sendCurrentPlayerValidSquares(1);
    }

    @Override
    public String runAction() {
        return "Select a weapon or GO BACK to action selection!";
    }

    @Override
    public String grabAction() {
        return "Select a weapon or GO BACK to action selection!";
    }

    @Override
    public String shootAction() {
        return "Select a weapon or GO BACK to action selection!";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select which weapon you want to use first";
    }

    @Override
    public String selectSquare(int index) {
        if(validSquares==null) return "Select which weapon you want to use first";
        if(validSquares.contains(index)){
            lobby.movePlayer(index);
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
        selectedWeapon = lobby.useWeapon(weaponID);
        if(selectedWeapon==null) return "You can't shoot with that weapon! Please select a valid weapon";
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
    public String goBack() {
        lobby.setState(new SelectActionState(lobby));
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
