package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class TeleportState implements GameState {

    private Lobby lobby;
    private PowerupCard thisCard;
    private ArrayList<Integer> squares = null;

    public TeleportState(Lobby lobby, PowerupCard powerupCard){
        this.lobby = lobby;
        thisCard = powerupCard;
        squares = lobby.sendCurrentPlayerValidSquares(10);
    }

    @Override
    public String runAction() {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String grabAction() {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String shootAction() {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String selectSquare(int index) {
        if(!squares.contains(index)) return "Invalid selection! Select a valid square.";
        lobby.movePlayer(index);
        lobby.removePowerup(thisCard);
        lobby.setState(new SelectActionState(lobby));
        return "OK";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String selectFiremode(int firemode) {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String selectAmmo(Color color) { return "You can't do that now!"; }

    @Override
    public String moveSubAction() {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String endOfTurnAction() {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String goBack() {
        lobby.setState(new SelectActionState(lobby));
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) {
        return "Select the square you want to teleport in.";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "Select the square you want to teleport in.";
    }
}
