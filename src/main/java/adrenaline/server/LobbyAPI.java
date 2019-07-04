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

    /**
     *
     * To do the run action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    String runAction(String clientID) throws RemoteException;

    /**
     *
     * To do the grab action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    String grabAction(String clientID) throws RemoteException;

    /**
     *
     * To do the shoot action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    String shootAction(String clientID) throws RemoteException;

    /**
     *
     * To do the select player action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param playersColor The ArrayList of players' color
     * @return The result of this request to client
     *
     */
    String selectPlayers(String clientID, ArrayList<Color> playersColor) throws RemoteException;

    /**
     *
     * To do the select square request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param index The square index from 0 to 11
     *
     * @return The result of this request to client
     *
     */
    String selectSquare(String clientID, Integer index) throws RemoteException;

    /**
     *
     * To do the select PowerUp request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param powerupID The powerupID which the player selected
     * @return The result of this request to client
     *
     */
    String selectPowerUp(String clientID, Integer powerupID) throws RemoteException;

    /**
     *
     * To do the select Weapon request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param weaponID The weaponID which the player selected
     *
     * @return The result of this request to client
     *
     */
    String selectWeapon(String clientID, Integer weaponID) throws RemoteException;

    /**
     *
     * To do the select Firemode request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param firemode The number of firemode from 0 to 2
     * @return The result of this request to client
     *
     */
    String selectFiremode(String clientID, Integer firemode) throws RemoteException;

    /**
     *
     * To do the select Ammo request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param color The ammo color
     *
     * @return The result of this request to client
     *
     */
    String selectAmmo(String clientID, Color color) throws RemoteException;

    /**
     *
     * To do the move Sub Action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    String moveSubAction(String clientID) throws RemoteException;

    /**
     *
     * To do the go Back action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    String goBack(String clientID) throws RemoteException;

    /**
     *
     * To do the end Of Turn Action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    String endOfTurnAction(String clientID) throws RemoteException;

    /**
     *
     * To do the select Final Frenzy Action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param action The frenzy action index from 0 to 2
     *
     * @return The result of this request to client
     *
     */
    String selectFinalFrenzyAction(String clientID, Integer action) throws RemoteException;

    /**
     *
     * To do the select Avatar action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param color The avatar's color
     *
     * @return The result of this request to client
     *
     */
    String selectAvatar(String clientID, Color color) throws RemoteException;


    /**
     *
     * To do the select Settings action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param mapID The id of map which select from client from 1 to 4
     * @param skulls The skulls selected from client from 5 to 8
     * @return The result of this request to client
     *
     */
    String selectSettings(String clientID, Integer mapID, Integer skulls) throws RemoteException;


    /**
     *
     * Received chat message from client
     *
     * @param clientID The id of client who did this request
     * @param message The chat message
     *
     * @return The result of this request to client
     *
     */
    String sendChatMessage(String clientID, String message) throws RemoteException;
}
