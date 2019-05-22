package server.controller.states;

import server.controller.Lobby;
import server.model.Color;
import server.model.WeaponCard;

import java.util.ArrayList;

public class WeaponGrabState implements GameState {

    private Lobby lobby;
    private int actionNumber;
    private ArrayList<WeaponCard> weaponCards;

    public WeaponGrabState(Lobby lobby, int actionNumber, ArrayList<WeaponCard> weaponCards){
        this.lobby = lobby;
        this.actionNumber = actionNumber;
        this.weaponCards = weaponCards;
    }

    @Override
    public String runAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String grabAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String shootAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) { return "Select a weapon to grab it or GO BACK to terminate your action"; }

    @Override
    public String selectSquare(int index) {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        lobby.consumePowerup(powerUpID);
        return "OK";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return null;
    }

    @Override
    public String endOfTurnAction() {
        return "Select a weapon to grab it or GO BACK to terminate your action";
    }

    @Override
    public String goBack() {
        lobby.setState(new SelectActionState(lobby, actionNumber));
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
