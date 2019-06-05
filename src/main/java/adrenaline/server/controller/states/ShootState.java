package adrenaline.server.controller.states;

import adrenaline.server.exceptions.InvalidCardException;
import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.Firemode;

import java.util.ArrayList;

public class ShootState implements GameState {

    private Lobby lobby;
    private Integer selectedWeapon = null;

    public ShootState(Lobby lobby){
        this.lobby = lobby;
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
        return "Select which weapon you want to use first";
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        try {
            lobby.consumePowerup(powerUpID);
            return "OK";
        } catch (InvalidCardException e) {
            return "Invalid card selection!";
        }
    }

    @Override
    public String selectWeapon(int weaponID) {
        if(lobby.canUseWeapon(weaponID)){
            selectedWeapon = weaponID;
            return "OK";
        }
        else return "You can't shoot with that weapon! Please select a valid weapon";
    }

    @Override
    public String selectFiremode(int firemode) {
        if(selectedWeapon == null) return "No weapon is selected! Please select a weapon first";
        try {
            Firemode selectedFiremode = lobby.getFiremode(selectedWeapon, firemode);
            if(selectedFiremode==null) return "This weapon does not have such firemode!";
            else {
                lobby.setState(selectedFiremode.getNextStep(lobby));
            }
            return "OK";
        } catch (NotEnoughAmmoException e) {
            return "You can't pay the ammo price for that firemode! HINT: powerups can be expended too";
        }
    }

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
