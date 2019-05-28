package adrenaline;

import adrenaline.client.ClientAPI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAPI extends Remote {
    String registerRMIClient(ClientAPI clientAPI)throws  RemoteException;
    String setNickname(String clientID, String nickname) throws RemoteException;
    String unregisterClient(String clientID)throws RemoteException;
}
