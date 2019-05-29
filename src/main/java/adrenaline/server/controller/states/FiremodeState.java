package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Firemode;

import java.util.ArrayList;

public class FiremodeState implements GameState {

    private Lobby lobby;
    private Firemode thisFiremode;

    public FiremodeState(Lobby lobby, Firemode thisFiremode){
        this.lobby = lobby;
        this.thisFiremode = thisFiremode;
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
    public String selectFiremode(int firemode) {
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
    public String selectMap(int mapID, String voterID) {
        return null;
    }
}