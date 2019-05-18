package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientCommands extends UnicastRemoteObject implements ClientAPI {

    public RMIClientCommands() throws RemoteException {
    }

    @Override
    public void setLobby(String lobbyID) throws RemoteException{

    }
}
