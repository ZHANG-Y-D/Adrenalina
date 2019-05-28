package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Avatar;
import adrenaline.Color;

import java.util.ArrayList;

public class AvatarSelectionState implements GameState {

    private Lobby lobby;
    private ArrayList<Avatar> avatars;

    public AvatarSelectionState (Lobby lobby, ArrayList<Avatar> avatars){
        this.lobby = lobby;
        this.avatars = avatars;
    }

    @Override
    public String runAction() {
        return "KO";
    }

    @Override
    public String grabAction() {
        return "KO";
    }

    @Override
    public String shootAction() {
        return "KO";
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) { return "KO"; }

    @Override
    public String selectSquare(int index) {
        return "KO";
    }

    @Override
    public String selectPowerUp(int powerUpID) {
        return "KO";
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "KO";
    }

    @Override
    public String selectFiremode(int firemode) {
        return null;
    }

    @Override
    public String endOfTurnAction() {
        return "KO";
    }

    @Override
    public String goBack() {
        return "KO";
    }

    @Override
    public String selectAvatar(Color color) {
        for(Avatar a : avatars) {
            if(a.getColor().equals(color)){
                Avatar chosen = a;
                avatars.remove(a);
                lobby.initCurrentPlayer(chosen);
                return "OK";
            }
        }
        return "Someone else already picked this avatar!";
    }

    @Override
    public String selectMap(int mapID, String voterID) {
        return "KO";
    }
}
