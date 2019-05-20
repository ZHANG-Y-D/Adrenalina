package server.controller.states;

import server.controller.Lobby;
import server.model.Color;

import java.util.ArrayList;

public class WeaponSwapState implements GameState {
    private Lobby lobby;
    private int actionNumber;
    private int chosenWeapon;
    private GrabState restoreState;

    public WeaponSwapState(Lobby lobby, int actionNumber, int chosenWeapon, GrabState restoreState){
        this.lobby = lobby;
        this.actionNumber = actionNumber;
        this.chosenWeapon = chosenWeapon;
        this.restoreState = restoreState;
    }

    @Override
    public String runAction() {
        return "Select a weapon from your hand to drop it or GO BACK";
    }

    @Override
    public String grabAction() {
        return "Select a weapon from your hand to drop it or GO BACK";
    }

    @Override
    public String shootAction() {
        return "Select a weapon from your hand to drop it or GO BACK";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select a weapon from your hand to drop it or GO BACK";
    }

    @Override
    public String selectSquare(int index) {
        return "Select a weapon from your hand to drop it or GO BACK";
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        return "Select a weapon from your hand to drop it or GO BACK";
    }

    @Override
    public String selectWeapon(int weaponID) {
        //TODO swap weapons
        lobby.setState(new SelectActionState(lobby, actionNumber));
        return "OK";
    }

    @Override
    public String endOfTurnAction() {
        return "Select a weapon from your hand to drop it or GO BACK";
    }

    @Override
    public String goBack() {
        return null;
    }

    @Override
    public String selectAvatar(Color color) {
        return"KO";
    }

    @Override
    public String selectMap(int mapID, String voterID) {
        lobby.setState(restoreState);
        return "KO";
    }
}
