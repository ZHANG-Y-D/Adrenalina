package adrenaline.server.controller.states;

import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

public interface GameState {
    String runAction();
    String grabAction();
    String shootAction();
    String selectPlayers(ArrayList<Color> playersColor);
    String selectSquare(int index);
    String selectPowerUp(PowerupCard powerUp);
    String selectWeapon(int weaponID);
    String selectFiremode(int firemode);
    String selectAmmo(Color color);
    String moveSubAction();
    String endOfTurnAction();
    String goBack();
    String selectAvatar(Color color);
    String selectSettings(int mapID, int skulls, String voterID);
}
