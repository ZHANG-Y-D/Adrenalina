package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientAPI extends Remote {
    void setLobby(String lobbyID) throws RemoteException;
}
