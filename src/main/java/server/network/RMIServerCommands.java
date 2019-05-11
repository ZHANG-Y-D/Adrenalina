package server.network;

import client.ClientAPI;
import server.ServerAPI;
import server.GameServer;

import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;

public class RMIServerCommands extends UnicastRemoteObject implements ServerAPI {

    private final GameServer mainServer;
    private HashMap<String,String> clientLobbyMap;

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

    public void exampleMethod(String clientID) throws RemoteException {
        //mainServer.getLobby(clientLobbyMap.get(clientID)).foo();
    }

}
