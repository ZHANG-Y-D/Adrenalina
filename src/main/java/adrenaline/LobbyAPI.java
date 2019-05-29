 package adrenaline;

import adrenaline.Color;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface LobbyAPI extends Remote {
    String runAction(String clientID) throws RemoteException;
    String grabAction(String clientID) throws RemoteException;
    String shootAction(String clientID) throws RemoteException;
    String selectPlayers(String clientID, ArrayList<Color> playersColor) throws RemoteException;
    String selectSquare(String clientID, int index) throws RemoteException;
    String selectPowerUp(String clientID, int powerupID) throws RemoteException;
    String selectWeapon(String clientID, int weaponID) throws RemoteException;
    String selectFiremode(String clientID, int firemode);
    String moveSubAction(String clientID);
    String fireSubAction(String clientID);
    String endOfTurnAction(String clientID) throws RemoteException;
    String selectAvatar(String clientID, Color color) throws RemoteException;
    String selectMap(String clientID, int mapID) throws RemoteException;
}
