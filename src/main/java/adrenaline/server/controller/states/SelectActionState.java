package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;

import java.util.ArrayList;

public class SelectActionState implements GameState {

    private Lobby lobby;
    private int executedActions;

    public SelectActionState(Lobby lobby, int executedActions){
        this.lobby = lobby;
        this.executedActions = executedActions;
    }

    @Override
    public String runAction() {
        if (executedActions >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new RunState(lobby, executedActions+1));
            return "OK";
        }
    }

    @Override
    public String grabAction() {
        if (executedActions >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new GrabState(lobby, executedActions+1));
            return "OK";
        }
    }

    @Override
    public String shootAction() {
        if (executedActions >= 2) return "You have run out of moves!";
        else {
            lobby.setState(new ShootState(lobby,executedActions+1 ));
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
    public String selectPowerUp(int powerUpID) { //TODO
         return null; }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select an action!";
    }

    @Override
    public String selectFiremode(int firemode) {
        return null;
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
    public String selectMap(int mapID, String voterID) { return "KO"; }

}
