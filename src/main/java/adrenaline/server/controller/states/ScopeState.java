package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;
import java.util.Set;

public class ScopeState implements GameState {
    private final Set<Color> damagedThisTurn;
    private PowerupCard thisPowerup;
    private Lobby lobby;

    public ScopeState(Lobby lobby, PowerupCard powerup, Set<Color> damagedThisTurn) {
        this.lobby = lobby;
        thisPowerup = powerup;
        this.damagedThisTurn = damagedThisTurn;
        System.out.println(damagedThisTurn.toString());
    }

    @Override
    public String runAction() {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String grabAction() {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String shootAction() {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        if(!damagedThisTurn.contains(playersColor.get(0))) return "You can't target enemies who have not been damaged! Please select a valid target.";
        lobby.applyExtraDamage(playersColor.get(0));
        lobby.removePowerup(thisPowerup);
        lobby.setState(new SelectActionState(lobby));
        return "OK Damage added with the scope";
    }

    @Override
    public String selectSquare(int index) {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectFiremode(int firemode) {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String moveSubAction() {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String endOfTurnAction() {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String goBack() {
        lobby.setState(new SelectActionState(lobby));
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) {
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "Select which target you want to give extra damage to.";
    }
}
