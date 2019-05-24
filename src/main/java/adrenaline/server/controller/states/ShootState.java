package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;

import java.util.ArrayList;

public class ShootState implements GameState {

    private Lobby lobby;
    private int actionNumber;

    public ShootState(Lobby lobby, int actionNumber){

        this.lobby = lobby;
        this.actionNumber = actionNumber;
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
    public String goBack() {
        lobby.setState(new SelectActionState(lobby, actionNumber-1));
        return "OK";
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
