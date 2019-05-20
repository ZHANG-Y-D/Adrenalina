 package server;

import server.model.Color;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface LobbyAPI extends Remote {
    void runAction(String clientID) throws RemoteException;
    void grabAction(String clientID) throws RemoteException;
    void shootAction(String clientID) throws RemoteException;
    void selectPlayers(String clientID, ArrayList<Color> playersColor) throws RemoteException;
    void selectSquare(String clientID, int index) throws RemoteException;
    void selectPowerUp(String clientID, int powerupID) throws RemoteException;
    void selectWeapon(String clientID, int weaponID) throws RemoteException;
    void endOfTurnAction(String clientID) throws RemoteException;
    void selectAvatar(String clientID, Color color) throws RemoteException;
    void selectMap(String clientID, int mapID) throws RemoteException;
}
