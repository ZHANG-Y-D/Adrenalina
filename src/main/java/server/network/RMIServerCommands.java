package server.network;

import client.ClientAPI;
import server.ServerAPI;
import server.GameServer;

import java.rmi.*;
import java.rmi.server.*;

public class RMIServerCommands extends UnicastRemoteObject implements ServerAPI {

    private final GameServer mainServer;

    public RMIServerCommands(GameServer mainServer) throws RemoteException{
        this.mainServer = mainServer;
    }

    public void registerClient(ClientAPI clientAPI, String nickname) {
        this.mainServer.registerClient(new ClientRMIWrapper(clientAPI, nickname));
        System.out.println("Client connected through RMI!");
    }

    public void unregisterClient(ClientAPI clientAPI) {
        this.mainServer.unregisterClient(clientAPI);
        System.out.println("Client closed his RMI session!");
    }

}
