package adrenaline.server.network;

import adrenaline.server.controller.Lobby;
import adrenaline.server.LobbyAPI;
import adrenaline.Color;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class LobbyExportable extends UnicastRemoteObject implements LobbyAPI {
    private Lobby lobbyRelay;

    public LobbyExportable(Lobby lobby) throws RemoteException {
        lobbyRelay = lobby;
    }

    @Override
    public String runAction(String clientID) {
        return lobbyRelay.runAction(clientID);
    }

    @Override
    public String grabAction(String clientID) {
        return lobbyRelay.grabAction(clientID);
    }

    @Override
    public String shootAction(String clientID) {
        return lobbyRelay.shootAction(clientID);
    }

    @Override
    public String selectPlayers(String clientID, ArrayList<Color> playersColor) {
        return lobbyRelay.selectPlayers(clientID,playersColor);
    }

    @Override
    public String selectSquare(String clientID, Integer index) {
        return lobbyRelay.selectSquare(clientID,index);
    }

    @Override
    public String selectPowerUp(String clientID, Integer powerupID) {
        return lobbyRelay.selectPowerUp(clientID,powerupID);
    }

    @Override
    public String selectWeapon(String clientID, Integer weaponID) {
        return lobbyRelay.selectWeapon(clientID,weaponID);
    }

    @Override
    public String selectFiremode(String clientID, Integer firemode) {
        return lobbyRelay.selectFiremode(clientID, firemode);
    }

    @Override
    public String moveSubAction(String clientID) {
        return lobbyRelay.moveSubAction(clientID);
    }

    @Override
    public String goBack(String clientID) { return lobbyRelay.goBack(clientID); }

    @Override
    public String endOfTurnAction(String clientID) {
        return lobbyRelay.endOfTurnAction(clientID);
    }

    @Override
    public String selectAvatar(String clientID, Color color) {
        return lobbyRelay.selectAvatar(clientID,color);
    }

    @Override
    public String selectSettings(String clientID, Integer mapID, Integer skulls) {
        return lobbyRelay.selectSettings(clientID,mapID, skulls );
    }

    @Override
    public String sendChatMessage(String clientID, String message)  {
        return lobbyRelay.sendChatMessage(clientID, message);
    }
}
