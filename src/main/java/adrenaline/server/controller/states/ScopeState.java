package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * The Scope State to do all scope action
 *
 */
public class ScopeState implements GameState {
    private final Set<Color> damagedThisTurn;
    private PowerupCard thisPowerup;
    private Lobby lobby;
    private int[] ammoSelected = null;
    private Player user;

    /**
     *
     *
     * The constructor init all attitudes except ammoSelected array
     *
     * @param lobby The current lobby
     * @param powerup The server side PowerupCard reference
     * @param damagedThisTurn The damage Color set
     * @param user The server side Player reference index who requires this action
     */
    public ScopeState(Lobby lobby, PowerupCard powerup, Set<Color> damagedThisTurn, Player user) {
        this.lobby = lobby;
        thisPowerup = powerup;
        this.damagedThisTurn = damagedThisTurn;
        this.user = user;
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }


    /**
     *
     * To do the select player action request which received from client terminal
     *
     * @param playersColor The ArrayList of players' color
     * @return The result of this request to client
     *
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        if(ammoSelected==null) return "You must pay 1 ammo of any color first!";
        if(!damagedThisTurn.contains(playersColor.get(0))) return "You can't target enemies who have not been damaged! Please select a valid target.";
        lobby.applyExtraDamage(playersColor.get(0));
        user.payCost(ammoSelected);
        lobby.removePowerup(thisPowerup);
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        return "OK Damage added with the scope";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSquare(int index) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     *
     * To do the select Ammo request which received from client terminal
     *
     * @param color The ammo color
     *
     * @return The result of this request to client
     *
     */
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

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }


    /**
     *
     * To do the go Back action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String goBack() {
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAvatar(Color color) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        if(ammoSelected==null) return "Select 1 ammo of any color to pay the cost.";
        return "Select which target you want to give extra damage to.";
    }
}
