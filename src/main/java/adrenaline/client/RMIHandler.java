package adrenaline.client;


import adrenaline.Color;
import adrenaline.server.LobbyAPI;
import adrenaline.server.ServerAPI;
import adrenaline.client.controller.GameController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

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

    public void selectPowerUp(int powerupID) {
        try {
            gameController.handleReturn(myLobby.selectPowerUp(clientID, powerupID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void selectWeapon(int weaponID) {
        try {
            gameController.handleReturn(myLobby.selectWeapon(clientID, weaponID));
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

    public void run() {
        try {
            gameController.handleReturn(myLobby.runAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void grab() {
        try {
            gameController.handleReturn(myLobby.grabAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void shoot() {
        try {
            gameController.handleReturn(myLobby.shootAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void back() {
        try {
            gameController.handleReturn(myLobby.goBack(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void endTurn() {
        try {
            gameController.handleReturn(myLobby.endOfTurnAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void selectSquare(int index) {
        try {
            gameController.handleReturn(myLobby.selectSquare(clientID,index));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void selectFiremode(int firemode) {
        try {
            gameController.handleReturn(myLobby.selectFiremode(clientID,firemode));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void selectPlayers(ArrayList<Color> targets) {
        try {
            gameController.handleReturn(myLobby.selectPlayers(clientID,targets));
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
