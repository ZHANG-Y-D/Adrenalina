package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class SelectActionState implements GameState {

    private Lobby lobby;

    public SelectActionState(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public String runAction() {
        if (lobby.getExecutedActions() >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new RunState(lobby));
            return "OK";
        }
    }

    @Override
    public String grabAction() {
        if (lobby.getExecutedActions() >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new GrabState(lobby));
            return "OK";
        }
    }

    @Override
    public String shootAction() {
        if (lobby.getExecutedActions() >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new ShootState(lobby));
            return "OK";
        }
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select an action!";
    }

    @Override
    public String selectSquare(int index) {
        return "Select an action!";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
         return lobby.usePowerup(powerUp);

    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select an action!";
    }

    @Override
    public String selectFiremode(int firemode) {
        return null;
    }

    @Override
    public String moveSubAction() {
        return "Select an action!";
    }

    @Override
    public String endOfTurnAction() {
        lobby.setState(new ReloadState(lobby));
        return "OK";
    }

    @Override
    public String goBack() {
        return "You can't go back now!";
    }

    @Override
    public String selectAvatar(Color color) { return "KO"; }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) { return "KO"; }

}
