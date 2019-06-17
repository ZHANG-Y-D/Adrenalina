package adrenaline.server.controller.states;

import adrenaline.server.exceptions.InvalidCardException;
import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.exceptions.WeaponHandFullException;
import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.SquareSpawn;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

public class WeaponGrabState implements GameState {

    private Lobby lobby;
    private SquareSpawn weaponSquare;
    private boolean swapping = false;
    private WeaponCard selectedCard;

    public WeaponGrabState(Lobby lobby, SquareSpawn weaponSquare){
        this.lobby = lobby;
        this.weaponSquare = weaponSquare;
        System.out.println("dentro state");
    }

    @Override
    public String runAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String grabAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String shootAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) { return "Select a weapon to grab it or GO BACK to terminate your action"; }

    @Override
    public String selectSquare(int index) {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        lobby.consumePowerup(powerUp);
        return "OK";
    }

    @Override
    public String selectWeapon(int weaponID) {
        if(!swapping) {
            WeaponCard weaponCard = weaponSquare.getWeaponCard(weaponID);
            if(weaponCard==null) return "Invalid card selection!";
            try {
                lobby.grabWeapon(weaponCard);
                weaponSquare.removeCard(weaponCard);
                lobby.clearTempAmmo();
                lobby.setState(new SelectActionState(lobby));
                return "OK";
            } catch (NotEnoughAmmoException e) {
                return "You can't pay the ammo price for that weapon! HINT: powerups can be expended too";
            } catch (WeaponHandFullException e) {
                selectedCard = weaponCard;
                swapping = true;
                return "You can't hold more than 3 weapons! Please select a card from your hand to swap it";
            }
        }else{
            try{
                weaponSquare.addCard(lobby.swapWeapon(selectedCard, weaponID));
                weaponSquare.removeCard(selectedCard);
                lobby.clearTempAmmo();
                lobby.setState(new SelectActionState(lobby));
                return "OK";
            } catch (InvalidCardException e) {
                return "Invalid card selection!";
            }
        }
    }

    @Override
    public String selectFiremode(int firemode) {
        return "You can't do that now!";
    }

    public String moveSubAction() {
        return "You can't do that now!";
    }

    @Override
    public String endOfTurnAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
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
