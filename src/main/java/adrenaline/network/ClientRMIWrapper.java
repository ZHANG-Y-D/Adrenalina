package adrenaline.network;

import adrenaline.UpdateMessage;
import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

import java.rmi.RemoteException;
import java.util.UUID;

public class ClientRMIWrapper implements Client {
    private final String clientID;
    private String nickname = null;
    private ClientAPI thisClient;
    private boolean active;

    public ClientRMIWrapper(ClientAPI newClient) {
        clientID = UUID.randomUUID().toString();
        thisClient = newClient;
        active = true;
    }

    public String getClientID() {
        return clientID;
    }

    public String getNickname(){ return nickname; }

    public boolean setNickname(String nickname) {
        if(this.nickname != null) return false;
        this.nickname = nickname;
        return true;
    }

    public void setActive(boolean active) { this.active = active; }

    public void setLobby(Lobby lobby) { setLobby(lobby.getID()); }

    public void setLobby(String lobbyID){
        try {
            thisClient.setLobby(lobbyID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void update(UpdateMessage updatemsg) throws RemoteException {
        thisClient.update(updatemsg);
    }

}
