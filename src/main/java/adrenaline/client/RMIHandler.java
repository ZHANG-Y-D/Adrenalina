package adrenaline.client;


import adrenaline.Color;
import adrenaline.server.LobbyAPI;
import adrenaline.server.ServerAPI;
import adrenaline.client.controller.GameController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIHandler implements ConnectionHandler {

    private String clientID;
    private ServerAPI myServer;
    private String myLobbyID;
    private LobbyAPI myLobby;
    private RMIClientCommands thisClient;
    private Registry registry;
    private GameController gameController;

    public RMIHandler(String serverIP, int port, GameController gameController) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(serverIP, port);
        String remoteObjectName = "AdrenalineServer";
        this.myServer = (ServerAPI) registry.lookup(remoteObjectName);
        System.out.println("Connection through RMI was succesful!");
        this.gameController = gameController;
        thisClient = new RMIClientCommands(this, gameController);
        clientID = myServer.registerRMIClient(thisClient);
    }



    public void setMyLobby(String myLobbyID){
        this.myLobbyID = myLobbyID;
        String remoteObjectName = "Game;"+myLobbyID;
        try {
            this.myLobby = (LobbyAPI) registry.lookup(remoteObjectName);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregister() {
        try {
            gameController.handleReturn(myServer.unregisterClient(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setNickname(String nickname) {
        try {
            gameController.handleReturn(myServer.setNickname(clientID, nickname));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void selectAvatar(Color color){
        try {
            gameController.handleReturn(myLobby.selectAvatar(clientID, color));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendSettings(int selectedMap, int selectedSkull) {
        try {
            gameController.handleReturn(myLobby.selectSettings(clientID, selectedMap, selectedSkull));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendChatMessage(String message) {
        try{
            gameController.handleReturn(myLobby.sendChatMessage(clientID, message));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getClientID() {
        return clientID;
    }

    @Override
    public String getMyLobbyID() {
        return myLobbyID;
    }
}
