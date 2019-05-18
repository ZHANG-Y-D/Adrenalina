package server.network;

import client.ClientAPI;
import server.controller.Lobby;

import java.rmi.RemoteException;

public interface Client extends ClientAPI {
    String getClientID();
    void setLobby(Lobby lobby) throws RemoteException;
}
