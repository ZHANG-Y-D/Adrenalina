package server;

import client.ClientAPI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAPI extends Remote {
    void registerClient(ClientAPI clientAPI)throws  RemoteException;
    void unregisterClient(ClientAPI clientAPI)throws RemoteException;
}
