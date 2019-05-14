package server.controller.states;

import server.controller.Lobby;
import server.model.Color;

import java.util.ArrayList;

public interface GameState {
    String runAction();
    String grabAction();
    String shootAction();
    String selectPlayers(ArrayList<Color> playersColor);
    String selectSquare(int index);
    String selectPowerUp();
    String selectWeapon();
    String endOfTurnAction();
}
