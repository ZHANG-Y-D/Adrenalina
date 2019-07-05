package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

/**
 *
 * The SelectFreneticActionState to do the select Frenetic mode action operations
 *
 */
public class SelectFreneticActionState implements GameState {
    private boolean firstplayerFF;
    private Lobby lobby;

    /**
     *
     *
     * The constructor init lobby attitude
     *
     * @param lobby The current lobby
     */
    public SelectFreneticActionState(Lobby lobby){
        this.lobby = lobby;
        this.firstplayerFF = lobby.isFirstPlayerFF();
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
        return "Select an action!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSquare(int index) {
        return "Select an action!";
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
        return lobby.usePowerup(powerUp);
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "Select an action!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "Select an action!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) {
        return "Select an action!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        return "Select an action!";
    }

    /**
     *
     * To do the end Of Turn Action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String endOfTurnAction() {
        lobby.endTurn(false);
        return "OK";
    }


    /**
     *
     * To do the select Final Frenzy Action request which received from client terminal
     *
     * @param action The frenzy action index from 0 to 2
     *
     * @return The result of this request to client
     *
     */
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



    /**
     * The client can't do this at current time
     */
    @Override
    public String goBack() {
        return "You can't go back now!";
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
