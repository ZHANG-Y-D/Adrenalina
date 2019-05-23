package server.controller.states;

import server.controller.Lobby;
import server.exceptions.InvalidCardException;
import server.model.Color;

import java.util.ArrayList;

public class RespawnState implements GameState {

    private Lobby lobby;

    public RespawnState(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public String runAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String grabAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String shootAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectSquare(int index) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        try {
            lobby.respawnWithPowerup(powerUpID);
            lobby.endTurn();
            return "OK";
        }catch(InvalidCardException ice){
            return "Invalid selection! Please select a valid card";
        }
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String endOfTurnAction() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String goBack() {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectAvatar(Color color) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }

    @Override
    public String selectMap(int mapID, String voterID) {
        return "You are dead! Discard a powerup card to choose your spawn point";
    }
}
