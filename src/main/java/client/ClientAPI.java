package client;

import java.rmi.Remote;

public interface ClientAPI extends Remote {
    void setLobby(String lobbyID);
}
