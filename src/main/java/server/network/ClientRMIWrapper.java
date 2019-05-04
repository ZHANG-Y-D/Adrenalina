package server.network;

import client.ClientAPI;

import java.util.UUID;

public class ClientRMIWrapper implements ClientAPI {
    private final String clientID;
    private ClientAPI thisClient;
    private boolean active;
    private String inLobbyID;

    public ClientRMIWrapper(ClientAPI newClient) {
        thisClient = newClient;
        clientID = UUID.randomUUID().toString();
        active = true;
        inLobbyID = null;
    }

    public void setLobby(String lobbyID){
        // inLobbyID is effectively final
        if(inLobbyID==null) inLobbyID = lobbyID;
    }
}
