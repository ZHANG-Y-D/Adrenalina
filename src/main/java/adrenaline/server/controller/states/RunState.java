package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class RunState implements GameState {

    private Lobby lobby;
    private ArrayList<Integer> validSquares;

    public RunState(Lobby lobby, int range){
        this.lobby = lobby;
        this.validSquares = lobby.sendCurrentPlayerValidSquares(range);
    }

    @Override
    public String runAction() { return "Select a square or GO BACK to action selection!"; }

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
        System.out.println("STATE");
        if(!validSquares.contains(index)) return "You can't move there! Please select a valid square";
        lobby.movePlayer(index);
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        lobby.incrementExecutedActions();
        return "OK";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "You can't do that now!";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "You can't do that now!";
    }

    @Override
    public String selectFiremode(int firemode) {
        return "You can't do that now!";
    }

    @Override
    public String selectAmmo(Color color) { return "You can't do that now!"; }

    @Override
    public String moveSubAction() { return "You can't do that now!"; }

    @Override
    public String endOfTurnAction() {
        return "Select a square or GO BACK to action selection!";
    }

    @Override
    public String selectFinalFrenzyAction(Integer action) { return "You can't do that now!"; }

    @Override
    public String goBack() {
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
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
