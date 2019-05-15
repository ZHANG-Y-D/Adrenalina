 package server;

import server.model.Color;

import java.rmi.Remote;
import java.util.ArrayList;

public interface LobbyAPI extends Remote {
    void runAction(String clientID);
    void grabAction(String clientID);
    void shootAction(String clientID);
    void selectPlayers(String clientID, ArrayList<Color> playersColor);
    void selectSquare(String clientID, int index);
    void selectPowerUp(String clientID, int powerupID);
    void selectWeapon(String clientID, int weaponID);
    void endOfTurnAction(String clientID);
    void selectAvatar(String clientID, Color color);
    void selectMap(String clientID, int mapID);
}
