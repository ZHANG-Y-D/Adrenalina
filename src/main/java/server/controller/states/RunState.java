package server.controller.states;

import server.controller.Lobby;
import server.model.Color;

import java.util.ArrayList;

public class RunState implements GameState {

    private Lobby lobby;

    public RunState(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public String runAction() {
        return null;
    }

    @Override
    public String grabAction() {
        return null;
    }

    @Override
    public String shootAction() {
        return null;
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return null;
    }

    @Override
    public String selectSquare(int index) {
        return null;
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        return null;
    }

    @Override
    public String selectWeapon(int weaponID) {
        return null;
    }

    @Override
    public String endOfTurnAction() {
        return null;
    }

    @Override
    public String selectAvatar(Color color) {
        return null;
    }

    @Override
    public String selectMap(int mapID, String voterID) {
        return null;
    }
}
