package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class MoveEnemyState implements FiremodeSubState {

    private int targetsLimit;
    private int allowedMovement;
    private ArrayList<int[]> dmgmrkEachTarget;
    private Lobby lobby = null;
    private Firemode thisFiremode = null;
    private boolean actionExecuted;
    private ArrayList<Integer> validSquares = null;
    private ArrayList<Color> selectedPlayers = null;

    @Override
    public void setContext(Lobby lobby, Firemode firemode, boolean actionExecuted) {
        this.lobby = lobby;
        this.thisFiremode = firemode;
        this.actionExecuted = actionExecuted;
        validSquares = lobby.sendCurrentPlayerValidSquares(firemode);
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
        this.selectedPlayers = playersColor;
        return "OK";
    }

    @Override
    public String selectSquare(int index) {
        if(selectedPlayers==null) return "Select your targets first.";
        
        return "OK";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return null;
    }

    @Override
    public String selectWeapon(int weaponID) {
        return null;
    }

    @Override
    public String selectFiremode(int firemode) {
        return null;
    }

    @Override
    public String moveSubAction() {
        return null;
    }

    @Override
    public String endOfTurnAction() {
        return null;
    }

    @Override
    public String goBack() {
        return null;
    }

    @Override
    public String selectAvatar(Color color) {
        return null;
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return null;
    }
}
