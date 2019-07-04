 package adrenaline.server;

import adrenaline.Color;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


 /**
  *
  * The lobbyAPI, for action of lobby's state pattern.
  *
  */
 public interface LobbyAPI extends Remote {
    String runAction(String clientID) throws RemoteException;
    String grabAction(String clientID) throws RemoteException;
    String shootAction(String clientID) throws RemoteException;
    String selectPlayers(String clientID, ArrayList<Color> playersColor) throws RemoteException;
    String selectSquare(String clientID, Integer index) throws RemoteException;
    String selectPowerUp(String clientID, Integer powerupID) throws RemoteException;
    String selectWeapon(String clientID, Integer weaponID) throws RemoteException;
    String selectFiremode(String clientID, Integer firemode) throws RemoteException;
    String selectAmmo(String clientID, Color color) throws RemoteException;
    String moveSubAction(String clientID) throws RemoteException;
    String goBack(String clientID) throws RemoteException;
    String endOfTurnAction(String clientID) throws RemoteException;
    String selectFinalFrenzyAction(String clientID, Integer action) throws RemoteException;
    String selectAvatar(String clientID, Color color) throws RemoteException;
    String selectSettings(String clientID, Integer mapID, Integer skulls) throws RemoteException;
    String sendChatMessage(String clientID, String message) throws RemoteException;
}
