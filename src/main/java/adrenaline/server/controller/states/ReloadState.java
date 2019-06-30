package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.exceptions.AlreadyLoadedException;
import adrenaline.server.exceptions.InvalidCardException;
import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class ReloadState implements GameState{
    private Lobby lobby;

    public ReloadState(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public String runAction() {
        return "You can't select an action now";
    }

    @Override
    public String grabAction() {
        return "You can't select an action now";
    }

    @Override
    public String shootAction() {
        return "You can't select an action now";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You can't select players now";
    }

    @Override
    public String selectSquare(int index) {
        return "You can't select squares now";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        lobby.consumePowerup(powerUp);
        return "OK";
    }

    @Override
    public String selectWeapon(int weaponID) {
        try {
            lobby.reloadWeapon(weaponID);
        } catch (InvalidCardException e) {
            return "Invalid selection! Select a valid card.";
        } catch (AlreadyLoadedException e) {
            return "That weapon is already loaded!";
        } catch (NotEnoughAmmoException e) {
            return "You can't pay the ammo cost for that weapon! HINT: powerups can be expended too";
        }
        return "OK Weapon reloaded!";
    }

    @Override
    public String selectFiremode(int firemode) {
        return null;
    }

    @Override
    public String selectAmmo(Color color) { return "You can't do that now!"; }

    @Override
    public String moveSubAction() {
        return "You can't do that now!";
    }

    @Override
    public String endOfTurnAction() {
        lobby.endTurn(false);
        return "OK";
    }

    @Override
    public String goBack() {
        return "You can't go back now!";
    }

    @Override
    public String selectAvatar(Color color) {
        return null;
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return null;
    }
}
