package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.constraints.CardinalDirectionConstraint;
import adrenaline.server.model.constraints.InRadiusConstraint;
import adrenaline.server.model.constraints.RangeConstraint;

import java.util.ArrayList;

public class NewtonState implements GameState {
    private Lobby lobby;
    private PowerupCard thisPowerup;
    private Color currPlayer;
    private Color selectedPlayer;
    private ArrayList<Integer> validSquares;

    public NewtonState(Lobby lobby, PowerupCard powerup, Color currPlayer){
        this.lobby = lobby;
        thisPowerup = powerup;
        this.currPlayer = currPlayer;
    }

    @Override
    public String runAction() { return "Select the target you want to move."; }

    @Override
    public String grabAction() { return "Select the target you want to move."; }

    @Override
    public String shootAction() { return "Select the target you want to move."; }

    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        if(playersColor.get(0).equals(currPlayer)) return "You can't use this powerup on yourself!";
        playersColor.subList(1, playersColor.size()).clear();
        selectedPlayer = playersColor.get(0);
        ArrayList<RangeConstraint> moveConstraints = new ArrayList<>();
        moveConstraints.add(new InRadiusConstraint(2));
        moveConstraints.add(new CardinalDirectionConstraint());
        validSquares = lobby.sendTargetValidSquares(playersColor, moveConstraints);
        return "OK";
    }

    @Override
    public String selectSquare(int index) {
        if(validSquares==null) return "Select a target first!";
        if(validSquares.contains(index)){
            lobby.movePlayer(index, selectedPlayer);
            lobby.removePowerup(thisPowerup);
            lobby.setState(new SelectActionState(lobby));
            return "OK Target moved.";
        }else return "You can't move your target there! Please select a valid square.";
    }

    @Override
    public String selectPowerUp(PowerupCard powerUp) { return "Select the target you want to move."; }

    @Override
    public String selectWeapon(int weaponID) { return "Select the target you want to move."; }

    @Override
    public String selectFiremode(int firemode) { return "Select the target you want to move."; }

    @Override
    public String selectAmmo(Color color) { return "You can't do that now!"; }

    @Override
    public String moveSubAction() { return "Select the target you want to move."; }

    @Override
    public String endOfTurnAction() { return "Select the target you want to move."; }

    @Override
    public String goBack() { return "Select the target you want to move."; }

    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
