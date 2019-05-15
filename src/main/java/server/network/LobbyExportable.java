package server.network;

import server.LobbyAPI;
import server.controller.Lobby;
import server.model.Color;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class LobbyExportable extends UnicastRemoteObject implements LobbyAPI {
    private Lobby lobbyRelay;

    public LobbyExportable(Lobby lobby) throws RemoteException {
        lobbyRelay = lobby;
    }

    @Override
    public void runAction(String clientID) {

    }

    @Override
    public void grabAction(String clientID) {

    }

    @Override
    public void shootAction(String clientID) {

    }

    @Override
    public void selectPlayers(String clientID, ArrayList<Color> playersColor) {

    }

    @Override
    public void selectSquare(String clientID, int index) {

    }

    @Override
    public void selectPowerUp(String clientID, int powerupID) {

    }

    @Override
    public void selectWeapon(String clientID, int weaponID) {

    }

    @Override
    public void endOfTurnAction(String clientID) {

    }

    @Override
    public void selectAvatar(String clientID, Color color) {

    }

    @Override
    public void selectMap(String clientID, int mapID) {

    }
}
