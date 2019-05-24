package adrenaline.server;

import adrenaline.client.ClientAPI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAPI extends Remote {
    String registerRMIClient(ClientAPI clientAPI, String nickname)throws  RemoteException;
    void unregisterClient(String clientID)throws RemoteException;
}
