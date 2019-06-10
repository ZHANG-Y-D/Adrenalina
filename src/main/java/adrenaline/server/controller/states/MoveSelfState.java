package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public class MoveSelfState implements FiremodeSubState {

    private int allowedMovement;
    private Lobby lobby = null;
    private Firemode thisFiremode = null;
    private boolean actionExecuted;
    private FiremodeSubState callBackState = null;
    private ArrayList<Integer> validSquares = null;

    public MoveSelfState(int allowedMovement){
        this.allowedMovement = allowedMovement;
    }

    public void setContext(Lobby lobby, Firemode firemode, boolean actionExecuted, FiremodeSubState callBackState) {
        this.callBackState = callBackState;
        setContext(lobby, firemode, actionExecuted);
    }

    @Override
    public void setContext(Lobby lobby, Firemode firemode, boolean actionExecuted) {
        this.lobby = lobby;
        thisFiremode = firemode;
        this.actionExecuted = actionExecuted;
        validSquares = lobby.sendCurrentPlayerValidSquares(allowedMovement);
    }

    @Override
    public String runAction() {
        return null;
    }

    @Override
    public String grabAction() {
        return null;
    }

    @Override
    public String shootAction() {
        return null;
    }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return null;
    }

    @Override
    public String selectSquare(int index) {
        if(!validSquares.contains(index)) return "You can't move there! Select a valid square or GO BACK.";
        else{
            lobby.movePlayer(index);
            if(!actionExecuted){
                lobby.incrementExecutedActions();
                actionExecuted=true;
            }
            callBackState.setContext(lobby, thisFiremode, true);
            lobby.setState(callBackState);
            return "OK";
        }
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return null;
    }

    @Override
    public String selectWeapon(int weaponID) {
        return null;
    }

    @Override
    public String selectFiremode(int firemode) {
        return null;
    }

    @Override
    public String moveSubAction() {
        return "Select which square you want to move in";
    }

    @Override
    public String endOfTurnAction() {
        return null;
    }

    @Override
    public String goBack() {
        callBackState.setContext(lobby, thisFiremode, actionExecuted);
        lobby.setState(callBackState);
        return "OK";
    }

    @Override
    public String selectAvatar(Color color) {
        return null;
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return null;
    }
}
