package server.network;

import client.ClientAPI;
import server.controller.Lobby;

import java.rmi.RemoteException;
import java.util.UUID;

public class ClientRMIWrapper implements Client {
    private final String clientID;
    private String nickname;
    private ClientAPI thisClient;
    private boolean active;

    public ClientRMIWrapper(ClientAPI newClient, String nickname) {
        clientID = UUID.randomUUID().toString();
        thisClient = newClient;
        this.nickname = nickname;
        active = true;
    }

    public String getClientID() {
        return clientID;
    }


    public void setLobby(Lobby lobby) throws RemoteException{
        setLobby(lobby.getID());
    }

    public void setLobby(String lobbyID) throws RemoteException{
        thisClient.setLobby(lobbyID);
    }
}
