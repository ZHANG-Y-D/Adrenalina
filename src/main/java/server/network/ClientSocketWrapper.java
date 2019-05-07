package server.network;

import client.ClientAPI;

import java.net.Socket;
import java.util.UUID;

public class ClientSocketWrapper implements ClientAPI {
    private final String clientID;
    private Socket thisClient;
    private boolean active;
    private String inLobbyID;

    public ClientSocketWrapper(Socket newClient){
        this.thisClient = newClient;
        this.clientID = UUID.randomUUID().toString();
        this.active = true;
        this.inLobbyID = null;
    }

    public void setLobby(String lobbyID){
        // inLobbyID is effectively final
        if(inLobbyID==null) inLobbyID = lobbyID;
    }

    public void showLobbyDetails(){

    }
}
