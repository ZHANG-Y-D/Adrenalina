package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Avatar;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;
import java.util.Random;

public class AvatarSelectionState implements GameState {

    private Lobby lobby;
    private ArrayList<Avatar> avatars;

    public AvatarSelectionState (Lobby lobby, ArrayList<Avatar> avatars){
        this.lobby = lobby;
        this.avatars = avatars;
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
    public String selectPlayers(ArrayList<Color> playersColor) { return "KO"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSquare(int index) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) { return "KO"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() { return "KO"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    /**
     * The client can't do this at current time
     */
    @Override
    public String goBack() {
        return "KO";
    }

    /**
     *
     * To do the select Avatar action request which received from client terminal
     *
     * @param color The avatar's color
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String selectAvatar(Color color) {
        for(Avatar a : avatars) {
            if(a.getColor().equals(color)){
                Avatar chosen = a;
                avatars.remove(a);
                lobby.initCurrentPlayer(chosen, false);
                return "OK Avatar selected";
            }
        }
        return "Someone else already picked this avatar!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }

    /**
     * For random select avatar after client passed the time
     */
    public void selectRandom(){
        Avatar randomized = avatars.get(new Random().nextInt(avatars.size()));
        avatars.remove(randomized);
        lobby.initCurrentPlayer(randomized, true);
    }
}
