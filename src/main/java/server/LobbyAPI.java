 package server;

import server.model.Color;

import java.rmi.Remote;
import java.util.ArrayList;

public interface LobbyAPI extends Remote {
    void runAction();
    void grabAction();
    void shootAction();
    void selectPlayers(ArrayList<Color> playersColor);
    void selectSquare(int index);
    void selectPowerUp();
    void selectWeapon();
    void endOfTurnAction();
}
