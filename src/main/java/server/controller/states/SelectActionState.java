package server.controller.states;

import server.controller.Lobby;
import server.model.Color;

import java.util.ArrayList;

public class SelectActionState implements GameState {

    private Lobby lobby;
    private int remainingActions;

    public SelectActionState(Lobby lobby){
        this.lobby = lobby;
        this.remainingActions = 2;
    }

    @Override
    public String runAction() {
        if (remainingActions == 0) return "You have run out of moves!";
        else {
            remainingActions--;
            lobby.setState("RunState");
            return "OK";
        }
    }

    @Override
    public String grabAction() {
        if (remainingActions == 0) return "You have run out of moves!";
        else {
            remainingActions--;
            lobby.setState("GrabState");
            return "OK";
        }
    }

    @Override
    public String shootAction() {
        if (remainingActions == 0) return "You have run out of moves!";
        else {
            remainingActions--;
            lobby.setState("ShootState");
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
    public String selectPowerUp() {
        return "Select an action!";
    }

    @Override
    public String selectWeapon() {
        return "Select an action!";
    }

    @Override
    public String endOfTurnAction() {
        lobby.setState("ReloadState");
        return "OK";
    }
}
