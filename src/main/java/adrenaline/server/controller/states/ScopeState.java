package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;
import java.util.Set;

public class ScopeState implements GameState {
    private final Set<Color> damagedThisTurn;
    private PowerupCard thisPowerup;
    private Lobby lobby;
    private int[] ammoSelected = null;
    private Player user;

    public ScopeState(Lobby lobby, PowerupCard powerup, Set<Color> damagedThisTurn, Player user) {
        this.lobby = lobby;
        thisPowerup = powerup;
        this.damagedThisTurn = damagedThisTurn;
        this.user = user;
    }

    @Override
    public String runAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String grabAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String shootAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        if(ammoSelected==null) return "You must pay 1 ammo of any color first!";
        if(!damagedThisTurn.contains(playersColor.get(0))) return "You can't target enemies who have not been damaged! Please select a valid target.";
        lobby.applyExtraDamage(playersColor.get(0));
        user.payCost(ammoSelected);
        lobby.removePowerup(thisPowerup);
        lobby.setState(new SelectActionState(lobby));
        return "OK Damage added with the scope";
    }

    @Override
    public String selectSquare(int index) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectWeapon(int weaponID) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectFiremode(int firemode) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectAmmo(Color color) {
        int[] cost = {0,0,0};
        switch(color){
            case RED: cost[0]++; break;
            case BLUE: cost[1]++; break;
            case YELLOW: cost[2]++; break;
        }
        if(user.canPayCost(cost)){
            ammoSelected=cost;
            return "OK Select which target you want to give extra damage to.";
        }else{
            return "You don't have enough ammo! Select a valid ammo.";
        }
    }

    @Override
    public String moveSubAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String endOfTurnAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String goBack() {
        lobby.setState(new SelectActionState(lobby));
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }
}
