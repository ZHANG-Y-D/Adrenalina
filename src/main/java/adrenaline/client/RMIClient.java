package adrenaline.client;

import adrenaline.server.LobbyAPI;
import adrenaline.server.ServerAPI;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    private String clientID;
    private ServerAPI myServer;
    private LobbyAPI myLobby;
    private RMIClientCommands thisClient;
    private Registry registry;

    public RMIClient(String serverIP, int port) throws RemoteException, NotBoundException, ConnectException {
        registry = LocateRegistry.getRegistry(serverIP, port);
        String remoteObjectName = "AdrenalineServer";
        this.myServer = (ServerAPI) registry.lookup(remoteObjectName);
        System.out.println("Connection through RMI was succesful!");

        thisClient = new RMIClientCommands(this);
        clientID = myServer.registerRMIClient(thisClient);
        System.out.println(clientID);
    }

    public void setMyLobby(String myLobby) throws RemoteException{
        String remoteObjectName = "Game;"+myLobby;
        try {
            this.myLobby = (LobbyAPI) registry.lookup(remoteObjectName);
            System.out.println(this.myLobby);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
