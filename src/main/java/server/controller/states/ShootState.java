package server.controller.states;

import server.controller.Lobby;
import server.model.Color;

import java.util.ArrayList;

public class ShootState implements GameState {

    private Lobby lobby;

    public ShootState(Lobby lobby){
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
    public String selectPowerUp() {
        return null;
    }

    @Override
    public String selectWeapon() {
        return null;
    }

    @Override
    public String endOfTurnAction() {
        return null;
    }
}
