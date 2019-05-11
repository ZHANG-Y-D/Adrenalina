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

    public String registerRMIClient(ClientAPI clientAPI, String nickname) {
        ClientRMIWrapper wrapper = new ClientRMIWrapper(clientAPI, nickname);
        this.mainServer.registerClient(wrapper);
        System.out.println("Client connected through RMI!");
        return wrapper.getClientID();
    }

    public void unregisterClient(String clientID) {
        this.mainServer.unregisterClient(clientID);
        System.out.println("Client closed his RMI session!");
    }

}
