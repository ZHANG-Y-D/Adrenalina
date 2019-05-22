package server.controller.states;

import server.controller.Lobby;
import server.model.Color;

import java.util.ArrayList;

public class GrabState implements GameState {

    private Lobby lobby;
    private int actionNumber;
    private ArrayList<Integer> validSquares;

    public GrabState(Lobby lobby, int actionNumber) {
        this.lobby = lobby;
        this.actionNumber = actionNumber;
        this.validSquares = lobby.sendCurrentPlayerValidSquares(lobby.getCurrentPlayerAdrenalineState() > 0 ? 2 : 1);
    }

    @Override
    public String runAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    @Override
    public String grabAction() {
        return "Select a square to grab from or a weapon card";
    }

    @Override
    public String shootAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You can't do that now!";
    }

    @Override
    public String selectSquare(int index) {
        if(!validSquares.contains(index)) return "You can't grab from that square! Please select a valid square" ;
        lobby.movePlayer(index);
        lobby.grabFromSquare(index, actionNumber);
        return "OK";
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        return "You can't do that now! To use the card for paying an ammo cost, please select the square you want to move in first";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select the square you want to move in first. You can select your current square if you want to stay there";
    }

    @Override
    public String endOfTurnAction() {
        return "Select something to grab or GO BACK to action selection!";
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
