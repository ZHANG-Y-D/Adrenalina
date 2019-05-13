package server.network;

import client.ClientAPI;
import server.model.Color;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

public class ClientSocketWrapper implements ClientAPI {
    private final String clientID;
    private final String nickname;
    private Socket thisClient;
    private boolean active;
    private String inLobbyID;

    public ClientSocketWrapper(Socket newClient, String nickname){
        this.thisClient = newClient;
        this.clientID = UUID.randomUUID().toString();
        this.nickname = nickname;
        this.active = true;
        this.inLobbyID = null;
    }

    public void setLobby(String lobbyID){
        // inLobbyID is effectively final
        if(inLobbyID==null) inLobbyID = lobbyID;
    }

    public void showLobbyDetails(ArrayList<Color> availableColors, ArrayList<String> playersNicknames){

    }

    public String getNickname() throws RemoteException {
        return nickname;
    }
}
