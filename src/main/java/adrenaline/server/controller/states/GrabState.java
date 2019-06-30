package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class GrabState implements GameState {

    private Lobby lobby;
    private ArrayList<Integer> validSquares;

    public GrabState(Lobby lobby, int moveRange) {
        this.lobby = lobby;
        this.validSquares = lobby.sendCurrentPlayerValidSquares(moveRange);
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
        lobby.grabFromSquare(index);
        lobby.incrementExecutedActions();
        lobby.clearTempAmmo();
        return "OK";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "You can't do that now! To use the card for paying an ammo cost, please select the square you want to move in first";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select the square you want to move in first. You can select your current square if you want to stay there";
    }

    @Override
    public String selectFiremode(int firemode) {
        return "You can't do that now!";
    }

    @Override
    public String selectAmmo(Color color) {
        return "You can't do that now!";
    }

    @Override
    public String moveSubAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    @Override
    public String endOfTurnAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    @Override
    public String goBack() {
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        lobby.clearTempAmmo();
        return "OK Select an action";
    }

    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
