package adrenaline.client;

import adrenaline.UpdateMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientAPI extends Remote {
    void setLobby(String lobbyID) throws RemoteException;
    void update(UpdateMessage updatemsg) throws RemoteException;
}
