package server.network;

import client.ClientAPI;
import server.model.Color;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

public class ClientRMIWrapper implements ClientAPI {
    private final String clientID;
    private final String nickname;
    private ClientAPI thisClient;
    private boolean active;
    private String inLobbyID;

    public ClientRMIWrapper(ClientAPI newClient, String nickname) {
        thisClient = newClient;
        clientID = UUID.randomUUID().toString();
        this.nickname = nickname;
        active = true;
        inLobbyID = null;
    }

    public void setLobby(String lobbyID){
        // inLobbyID is effectively final
        if(inLobbyID==null) inLobbyID = lobbyID;
    }

    public void showLobbyDetails(ArrayList<Color> availableColors, ArrayList<String> playersNicknames) throws RemoteException {
            thisClient.showLobbyDetails(availableColors, playersNicknames );
    }

    public String getNickname() throws RemoteException {
        return nickname;
    }
}
