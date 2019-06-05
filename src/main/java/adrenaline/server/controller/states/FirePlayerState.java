package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.constraints.TargetsGenerator;

import java.util.ArrayList;

public class FirePlayerState implements FiremodeSubState {

    private Lobby lobby;
    private Firemode thisFiremode;
    private TargetsGenerator targetsGenerator;

    @Override
    public void setContext(Lobby lobby, Firemode firemode) {
        this.lobby = lobby;
        this.thisFiremode = firemode;
        lobby.sendCurrentPlayerValidSquares(firemode);
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

        return "OK";
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
    public String moveSubAction() {
        //TODO
        return null;
    }


    @Override
    public String endOfTurnAction() {
        return null;
    }

    @Override
    public String goBack() {
        lobby.setState(new ShootState(lobby));
        return "OK";
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
