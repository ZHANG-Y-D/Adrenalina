package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.constraints.TargetsGenerator;

import java.util.ArrayList;

public class FireAreaState implements FiremodeSubState {

    private Lobby lobby;
    private Firemode thisFiremode;

    private TargetsGenerator targetsGenerator;
    private ArrayList<int[]> dmgmrkEachSquare;
    private ArrayList<Integer> validSquares;

    @Override
    public void setContext(Lobby lobby, Firemode firemode) {
        this.lobby = lobby;
        this.thisFiremode = firemode;
        validSquares = lobby.sendCurrentPlayerValidSquares(firemode);
    }

    @Override
    public String runAction() { return "Select your target area or GO BACK."; }

    @Override
    public String grabAction() {
        return "Select your target area or GO BACK.";
    }

    @Override
    public String shootAction() { return "Select your target area or GO BACK."; }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return null;
    }

    @Override
    public String selectSquare(int index) {
        if(!validSquares.contains(index)) return "You can't shoot there!";
        ArrayList<Color> targets = new ArrayList<>();
        //targets generation
        try {
            lobby.applyFire(thisFiremode, targets);
            lobby.incrementExecutedActions();
            lobby.setState(thisFiremode.getNextStep());
            return "OK";
        } catch (InvalidTargetsException e) { return "You can't shoot there!"; }
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        return null;
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select your target area or GO BACK.";
    }

    @Override
    public String selectFiremode(int firemode) { return "Select your target area or GO BACK."; }

    @Override
    public String moveSubAction() {
        return null;
    }

    @Override
    public String endOfTurnAction() {
        return "Select your target area or GO BACK.";
    }

    @Override
    public String goBack() {
        lobby.setState(new ShootState(lobby));
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) { return "KO"; }


    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
