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
    public void runAction() {

    }

    @Override
    public void grabAction() {

    }

    @Override
    public void shootAction() {

    }

    @Override
    public void selectPlayers(ArrayList<Color> playersColor) {

    }

    @Override
    public void selectSquare(int index) {

    }

    @Override
    public void selectPowerUp() {

    }

    @Override
    public void selectWeapon() {

    }

    @Override
    public void endOfTurnAction() {

    }
}
