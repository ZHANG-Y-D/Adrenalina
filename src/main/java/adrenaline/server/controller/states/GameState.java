package adrenaline.server.controller.states;

import adrenaline.Color;

import java.util.ArrayList;

public interface GameState {
    String runAction();
    String grabAction();
    String shootAction();
    String selectPlayers(ArrayList<Color> playersColor);
    String selectSquare(int index);
    String selectPowerUp(int powerUpID);
    String selectWeapon(int weaponID);
    String selectFiremode(int firemode);
    String moveSubAction();
    String fireSubAction();
    String endOfTurnAction();
    String goBack();
    String selectAvatar(Color color);
    String selectMap(int mapID, String voterID);
}
