package server.network;

import server.LobbyAPI;
import server.controller.Lobby;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LobbyExportable extends UnicastRemoteObject implements LobbyAPI {
    private Lobby lobbyRelay;

    public LobbyExportable(Lobby lobby) throws RemoteException {
        lobbyRelay = lobby;
    }
}
