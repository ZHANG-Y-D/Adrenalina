package client;

import server.ServerAPI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    private String clientID;
    private ServerAPI myServer;
    private RMIClientCommands thisClient;

    public RMIClient(String serverIP, int port, String nickname) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(serverIP, port);
        String remoteObjectName = "AdrenalineServer";
        this.myServer = (ServerAPI) registry.lookup(remoteObjectName);
        System.out.println("Connection through RMI was succesful!");

        thisClient = new RMIClientCommands();
        clientID = myServer.registerRMIClient(thisClient, nickname);
        System.out.println(clientID);
    }
}
