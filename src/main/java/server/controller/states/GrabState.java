package server.controller.states;

import server.exceptions.InvalidWeaponException;
import server.exceptions.NotEnoughAmmoException;
import server.controller.Lobby;
import server.exceptions.WeaponHandFullException;
import server.model.Color;

import java.util.ArrayList;

public class GrabState implements GameState {

    private Lobby lobby;
    private int actionNumber;
    private ArrayList<Integer> validSquares;
    private boolean grabbingWeapon = false;

    public GrabState(Lobby lobby, int actionNumber) {
        this.lobby = lobby;
        this.actionNumber = actionNumber;
        this.validSquares = lobby.sendCurrentPlayerValidSquares(lobby.getCurrentPlayerAdrenalineState() > 0 ? 2 : 1);
    }

    @Override
    public String runAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    @Override
    public String grabAction() {
        return "Select a square to grab from or a weapon card";
    }

    @Override
    public String shootAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You can't do that now!";
    }

    @Override
    public String selectSquare(int index) {
        if(grabbingWeapon) return "Select the weapon you want to grab or GO BACK to terminate your action";
        if(!validSquares.contains(index)) return "You can't grab from that square! Please select a valid square" ;
        lobby.movePlayer(index);
        if(!lobby.grabAmmo()){
            this.grabbingWeapon = true;
            return "Select the weapon you want to grab";
        }else{
            lobby.setState(new SelectActionState(lobby, actionNumber));
            return "OK";
        }
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        if(!grabbingWeapon) return "You can't do that now! To use the card for paying an ammo cost, please select the square you want to move in first";
        //TODO use powerup as ammo
        return null;
    }

    @Override
    public String selectWeapon(int weaponID) {
        if(!grabbingWeapon) return "Select the square you want to move in first. You can select your current square if you want to stay there";
        try {
            lobby.grabWeapon(weaponID);
            lobby.setState(new SelectActionState(lobby, actionNumber));
            return "OK";
        }catch(InvalidWeaponException iwe){
            return "You can't grab that weapon! Please select a valid weapon to grab";
        }catch(NotEnoughAmmoException nae){
            return "You can't pay the ammo price for this weapon! Remember: powerups can be expended too...";
        }catch(WeaponHandFullException whfe){
            lobby.setState(new WeaponSwapState(lobby, actionNumber, weaponID,this));
            return "You can't hold more than 3 weapons! Please select a weapon to drop it";
        }
    }

    @Override
    public String endOfTurnAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    @Override
    public String goBack() {
        if(grabbingWeapon) lobby.setState(new SelectActionState(lobby, actionNumber));
        else lobby.setState(new SelectActionState(lobby, actionNumber-1));
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    @Override
    public String selectMap(int mapID, String voterID) {
        return "KO";
    }
}
