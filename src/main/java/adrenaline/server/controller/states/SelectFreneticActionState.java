package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class SelectFreneticActionState implements GameState {
    private boolean firstplayerFF;
    private Lobby lobby;

    public SelectFreneticActionState(Lobby lobby){
        this.lobby = lobby;
        this.firstplayerFF = lobby.isFirstPlayerFF();
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
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "Select an action!";
    }

    @Override
    public String selectSquare(int index) {
        return "Select an action!";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return lobby.usePowerup(powerUp);
    }

    @Override
    public String selectWeapon(int weaponID) {
        return "Select an action!";
    }

    @Override
    public String selectFiremode(int firemode) {
        return "Select an action!";
    }

    @Override
    public String selectAmmo(Color color) {
        return "Select an action!";
    }

    @Override
    public String moveSubAction() {
        return "Select an action!";
    }

    @Override
    public String endOfTurnAction() {
        lobby.endTurn(false);
        return "OK";
    }



    @Override
    public String selectFinalFrenzyAction(Integer action) {
        if(!firstplayerFF && lobby.getExecutedActions()<2){
            switch(action){
                case 0: lobby.setState(new FreneticActionState(lobby, 1)); break;
                case 1: lobby.setState(new RunState(lobby, 4)); break;
                case 2: lobby.setState(new GrabState(lobby, 2)); break;
                default: return "KO";
            }
            return "OK";
        }else if(lobby.getExecutedActions()<1){
            switch(action){
                case 0: lobby.setState(new FreneticActionState(lobby, 2)); break;
                case 1: lobby.setState(new GrabState(lobby, 3)); break;
                default: return "KO";
            }
            return "OK";
        }
        return "You have run out of moves!";
    }




    @Override
    public String goBack() {
        return "You can't go back now!";
    }

    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
