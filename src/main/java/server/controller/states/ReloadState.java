package server.controller.states;

import server.controller.Lobby;
import server.model.Color;

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
    public String selectPowerUp(int powerUpID) {
        return "You can't select power ups now";
    }

    @Override
    public String selectWeapon(int weaponID) {
        //TODO add method to select weapon to reload
        return "OK";
    }

    @Override
    public String endOfTurnAction() {
        lobby.endTurn();
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
