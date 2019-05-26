package adrenaline.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientCommands extends UnicastRemoteObject implements ClientAPI {

    private RMIClient client;

    public RMIClientCommands(RMIClient client) throws RemoteException {
        this.client = client;
    }
    @Override
    public void setLobby(String lobbyID) throws RemoteException {
        client.setMyLobby(lobbyID);
    }
}
