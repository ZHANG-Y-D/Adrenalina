package adrenaline.client;

import adrenaline.client.controller.Controller;
import adrenaline.server.LobbyAPI;
import adrenaline.server.ServerAPI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIHandler implements ConnectionHandler {

    private String clientID;
    private ServerAPI myServer;
    private LobbyAPI myLobby;
    private RMIClientCommands thisClient;
    private Registry registry;
    private Controller controller;

    public RMIHandler(String serverIP, int port, Controller controller) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(serverIP, port);
        String remoteObjectName = "AdrenalineServer";
        this.myServer = (ServerAPI) registry.lookup(remoteObjectName);
        System.out.println("Connection through RMIHandler was succesful!");
        this.controller = controller;
        thisClient = new RMIClientCommands(this);
        clientID = myServer.registerRMIClient(thisClient);
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

    public void unregister() {
        try {
            controller.handleReturn(myServer.unregisterClient(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setNickname(String nickname) {
        try {
            controller.handleReturn(myServer.setNickname(clientID, nickname));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
