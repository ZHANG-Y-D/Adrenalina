package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

public class FreneticActionState implements GameState {

    private Lobby lobby;
    private ArrayList<Integer> validSquares;
    private boolean moveExecuted = false;
    private WeaponCard selectedWeapon = null;

    public FreneticActionState(Lobby lobby, int runrange){
        this.lobby = lobby;
        validSquares = lobby.sendCurrentPlayerValidSquares(runrange);
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select which weapon you want to use first";
    }

    /**
     *
     * To do the select square request which received from client terminal
     *
     * @param index The square index from 0 to 11
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String selectSquare(int index) {
        if(!moveExecuted && validSquares.contains(index)){
            lobby.movePlayer(index);
            moveExecuted=true;
            lobby.incrementExecutedActions();
            return "OK";

        }else return "You can't move there!";
    }

    /**
     *
     * To do the select PowerUp request which received from client terminal
     *
     * @param powerUp The powerupID which the player selected
     * @return The result of this request to client
     *
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        lobby.consumePowerup(powerUp);
        return "OK";
    }

    /**
     *
     * To do the select Weapon request which received from client terminal
     *
     * @param weaponID The weaponID which the player selected
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String selectWeapon(int weaponID) {
        selectedWeapon = lobby.useWeapon(weaponID, true);
        if(selectedWeapon==null) return "You can't use that weapon!";
        else return "OK Select the firemode";
    }

    /**
     *
     * To do the select Firemode request which received from client terminal
     *
     * @param firemode The number of firemode from 0 to 2
     * @return The result of this request to client
     *
     */
    @Override
    public String selectFiremode(int firemode) {
        if(selectedWeapon == null) return "No weapon is selected! Please select a weapon first";
        try {
            Firemode selectedFiremode = lobby.getFiremode(selectedWeapon.getWeaponID(), firemode);
            if(selectedFiremode==null) return "This weapon does not have such firemode!";
            else {
                FiremodeSubState nextStep = selectedFiremode.getNextStep();
                nextStep.setContext(lobby, selectedWeapon, selectedFiremode, false);
                lobby.setState(nextStep);
            }
            return "OK Firemode accepted, choose your next move";
        } catch (NotEnoughAmmoException e) {
            return "You can't pay the ammo price for that firemode! HINT: powerups can be expended too";
        }
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "Select a weapon or GO BACK to action selection!"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        return "Select a weapon or GO BACK to action selection!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "Select a weapon or GO BACK to action selection!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFinalFrenzyAction(Integer action) {
        return "Select a weaopon or GO BACK to action selection!";
    }


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
        lobby.setState(new SelectFreneticActionState(lobby));
        lobby.clearTempAmmo();
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
