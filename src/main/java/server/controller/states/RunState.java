package server.controller.states;

import server.controller.Lobby;
import server.model.Color;

import java.util.ArrayList;

public class RunState implements GameState {

    private Lobby lobby;
    private int actionNumber;
    private ArrayList<Integer> validSquares;

    public RunState(Lobby lobby, int actionNumber){
        this.lobby = lobby;
        this.actionNumber = actionNumber;
        this.validSquares = lobby.sendCurrentPlayerValidSquares(3);
    }

    @Override
    public String runAction() { return "Select the square you want to move in"; }

    @Override
    public String grabAction() {
        return "Select a square or GO BACK to action selection!";
    }

    @Override
    public String shootAction() { return "Select a square or GO BACK to action selection!"; }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You can't do that now!";
    }

    @Override
    public String selectSquare(int index) {
        if(!validSquares.contains(index)) return "You can't move there! Please select a valid square";
        lobby.movePlayer(index);
        lobby.setState(new SelectActionState(lobby, actionNumber));
        return "OK";
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        return "You can't do that now!";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "You can't do that now!";
    }

    @Override
    public String endOfTurnAction() {
        return "Select a square or GO BACK to action selection!";
    }

    @Override
    public String goBack() {
        lobby.setState(new SelectActionState(lobby, actionNumber-1));
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    @Override
    public String selectMap(int mapID, String voterID) {
        return "KO";
    }
}
