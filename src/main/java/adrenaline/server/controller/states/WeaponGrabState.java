package adrenaline.server.controller.states;

import adrenaline.server.exceptions.InvalidCardException;
import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.exceptions.WeaponHandFullException;
import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.SquareSpawn;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

public class WeaponGrabState implements GameState {

    private Lobby lobby;
    private SquareSpawn weaponSquare;
    private boolean swapping = false;
    private WeaponCard selectedCard;

    public WeaponGrabState(Lobby lobby, SquareSpawn weaponSquare){
        this.lobby = lobby;
        this.weaponSquare = weaponSquare;
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) { return "Select a weapon to grab it or GO BACK to terminate your action"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSquare(int index) {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    /**
     * The client can't do this at current time
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
        if(!swapping) {
            WeaponCard weaponCard = weaponSquare.getWeaponCard(weaponID);
            if(weaponCard==null) return "Invalid card selection!";
            try {
                lobby.grabWeapon(weaponCard);
                weaponSquare.removeCard(weaponCard);
                lobby.clearTempAmmo();
                lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
                return "OK";
            } catch (NotEnoughAmmoException e) {
                return "You can't pay the ammo price for that weapon! HINT: powerups can be expended too";
            } catch (WeaponHandFullException e) {
                selectedCard = weaponCard;
                swapping = true;
                return "You can't hold more than 3 weapons! Please select a card from your hand to swap it";
            }
        }else{
            try{
                weaponSquare.addCard(lobby.swapWeapon(selectedCard, weaponID));
                weaponSquare.removeCard(selectedCard);
                lobby.clearTempAmmo();
                lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
                return "OK";
            } catch (InvalidCardException e) {
                return "Invalid card selection!";
            }
        }
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "You can't do that now!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "You can't do that now!"; }

    /**
     * The client can't do this at current time
     */
    public String moveSubAction() {
        return "You can't do that now!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
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
