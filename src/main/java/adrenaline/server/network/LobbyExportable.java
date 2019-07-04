package adrenaline.server.network;

import adrenaline.server.controller.Lobby;
import adrenaline.server.LobbyAPI;
import adrenaline.Color;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * The LobbyExportable extends UnicastRemoteObject implements LobbyAPI,
 * to received action request from client terminal and return the result of operation
 *
 */
public class LobbyExportable extends UnicastRemoteObject implements LobbyAPI {
    private Lobby lobbyRelay;

    /**
     *
     * The constructor of LobbyExportable,init the lobbyRelay attitude
     *
     * @param lobby The lobby reference
     *
     */
    public LobbyExportable(Lobby lobby) throws RemoteException {
        lobbyRelay = lobby;
    }

    /**
     *
     * To do the run action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String runAction(String clientID) {
        return lobbyRelay.runAction(clientID);
    }

    /**
     *
     * To do the grab action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String grabAction(String clientID) {
        return lobbyRelay.grabAction(clientID);
    }

    /**
     *
     * To do the shoot action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String shootAction(String clientID) {
        return lobbyRelay.shootAction(clientID);
    }

    /**
     *
     * To do the select player action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param playersColor The ArrayList of players' color
     * @return The result of this request to client
     *
     */
    @Override
    public String selectPlayers(String clientID, ArrayList<Color> playersColor) {
        return lobbyRelay.selectPlayers(clientID,playersColor);
    }

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
    @Override
    public String selectSquare(String clientID, Integer index) {
        return lobbyRelay.selectSquare(clientID,index);
    }

    /**
     *
     * To do the select PowerUp request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param powerupID The powerupID which the player selected
     * @return The result of this request to client
     *
     */
    @Override
    public String selectPowerUp(String clientID, Integer powerupID) {
        return lobbyRelay.selectPowerUp(clientID,powerupID);
    }

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
    @Override
    public String selectWeapon(String clientID, Integer weaponID) {
        return lobbyRelay.selectWeapon(clientID,weaponID);
    }

    /**
     *
     * To do the select Firemode request which received from client terminal
     *
     * @param clientID The id of client who did this request
     * @param firemode The number of firemode from 0 to 2
     * @return The result of this request to client
     *
     */
    @Override
    public String selectFiremode(String clientID, Integer firemode) {
        return lobbyRelay.selectFiremode(clientID, firemode);
    }

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
    @Override
    public String selectAmmo(String clientID, Color color) {
        return lobbyRelay.selectAmmo(clientID, color);
    }

    /**
     *
     * To do the move Sub Action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String moveSubAction(String clientID) {
        return lobbyRelay.moveSubAction(clientID);
    }

    /**
     *
     * To do the go Back action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String goBack(String clientID) { return lobbyRelay.goBack(clientID); }

    /**
     *
     * To do the end Of Turn Action request which received from client terminal
     *
     * @param clientID The id of client who did this request
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String endOfTurnAction(String clientID) {
        return lobbyRelay.endOfTurnAction(clientID);
    }

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
    @Override
    public String selectFinalFrenzyAction(String clientID, Integer action) { return lobbyRelay.selectFinalFrenzyAction(clientID, action); }

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
    @Override
    public String selectAvatar(String clientID, Color color) {
        return lobbyRelay.selectAvatar(clientID,color);
    }

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
    @Override
    public String selectSettings(String clientID, Integer mapID, Integer skulls) {
        return lobbyRelay.selectSettings(clientID,mapID, skulls );
    }

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
    @Override
    public String sendChatMessage(String clientID, String message)  {
        return lobbyRelay.sendChatMessage(clientID, message);
    }
}
