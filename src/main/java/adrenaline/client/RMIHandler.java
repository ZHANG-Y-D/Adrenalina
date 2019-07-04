package adrenaline.client;


import adrenaline.Color;
import adrenaline.server.LobbyAPI;
import adrenaline.server.ServerAPI;
import adrenaline.client.controller.GameController;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 *
 * The RMI Handler class implements ConnectionHandler, For Rmi connect with server
 * send message from client to server,and received the return value
 *
 */
public class RMIHandler implements ConnectionHandler {

    private String clientID;
    private ServerAPI myServer;
    private String myLobbyID;
    private LobbyAPI myLobby;
    private RMIClientCommands thisClient;
    private Registry registry;
    private GameController gameController;

    /**
     *
     * The constructor of this class and it try to getRegistry rmi connect with game server.
     *
     * @param serverIP The server ip address
     * @param port The port value, Initial value for RMI is 1099
     * @param gameController The reference of current gameController
     * @throws IOException If IO problem occur,it will throws IOException
     * @throws NotBoundException If Rmi bound problem occur,it will throws IOException
     */
    public RMIHandler(String serverIP, int port, GameController gameController) throws IOException, NotBoundException {
        registry = LocateRegistry.getRegistry(serverIP, port);
        String remoteObjectName = "AdrenalineServer";
        this.myServer = (ServerAPI) registry.lookup(remoteObjectName);
        this.gameController = gameController;
        thisClient = new RMIClientCommands(this, gameController);
        clientID = myServer.registerRMIClient(thisClient);
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader("reconnection.txt"));
            if (fileReader.readLine().equals(serverIP)) {
                String reconnID = fileReader.readLine();
                String result = myServer.reconnectClient(clientID, reconnID);
                if (result.substring(0, 2).equals("OK")) clientID = reconnID;
            }
        } catch (FileNotFoundException e) {
        }finally { if(fileReader!=null) fileReader.close(); }
        PrintWriter fileWriter = new PrintWriter("reconnection.txt", "UTF-8");
        fileWriter.println(serverIP);
        fileWriter.println(clientID);
        fileWriter.close();
        System.out.println("Succesfully connected through RMI!");
    }


    /**
     *
     * To set the lobby from client terminal to server terminal
     *
     * @param myLobbyID The lobbyID string
     */
    public void setMyLobby(String myLobbyID){
        this.myLobbyID = myLobbyID;
        String remoteObjectName = "Game;"+myLobbyID;
        try {
            this.myLobby = (LobbyAPI) registry.lookup(remoteObjectName);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To unregister for client
     *
     */
    public void unregister() {
        try {
            gameController.handleReturn(myServer.unregisterClient(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To set nickname of player from client terminal to server terminal
     *
     * @param nickname The nickname string
     */
    public void setNickname(String nickname) {
        try {
            gameController.handleReturn(myServer.setNickname(clientID, nickname));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To select avatar operation for player
     * at AvatarSelectionState from client terminal to server terminal
     *
     * @param color The color of avatar witch the player selected
     */
    public void selectAvatar(Color color){
        try {
            gameController.handleReturn(myLobby.selectAvatar(clientID, color));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To select powerup card for player
     * from client terminal to server terminal
     *
     * @param powerupID The powerupId witch the player selected
     */
    public void selectPowerUp(int powerupID) {
        try {
            gameController.handleReturn(myLobby.selectPowerUp(clientID, powerupID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To select weapon for player from client terminal to server terminal
     *
     * @param weaponID The  weaponID witch the player selected
     */
    public void selectWeapon(int weaponID) {
        try {
            gameController.handleReturn(myLobby.selectWeapon(clientID, weaponID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To select ammo for player from client terminal to server terminal
     *
     * @param color The ammo color witch the player selected
     */
    public void selectAmmo(Color color) {
        try {
            gameController.handleReturn(myLobby.selectAmmo(clientID, color));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send Map(from 1 to 4) and skull(from 5 to 8) set from client terminal to server terminal
     *
     * @param selectedMap The map number from 1 to 4
     * @param selectedSkull The number of Skull from 5 to 8
     *
     */
    public void sendSettings(int selectedMap, int selectedSkull) {
        try {
            gameController.handleReturn(myLobby.selectSettings(clientID, selectedMap, selectedSkull));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send a chat message from client terminal to server terminal
     *
     * @param message The message string witch wait for be send
     *
     */
    public void sendChatMessage(String message) {
        try{
            gameController.handleReturn(myLobby.sendChatMessage(clientID, message));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send the run action for player from client terminal to server terminal
     *
     */
    public void run() {
        try {
            gameController.handleReturn(myLobby.runAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send the grab action for player from client terminal to server terminal
     */
    public void grab() {
        try {
            gameController.handleReturn(myLobby.grabAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send the shoot action for player from client terminal to server terminal
     */
    public void shoot() {
        try {
            gameController.handleReturn(myLobby.shootAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send the go back action for player from client terminal to server terminal
     */
    public void back() {
        try {
            gameController.handleReturn(myLobby.goBack(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send the end turn action for player from client terminal to server terminal
     *
     */
    public void endTurn() {
        try {
            gameController.handleReturn(myLobby.endOfTurnAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * To send the square selection from client terminal to server terminal
     *
     * @param index The square index from 0 to 11
     *
     */
    public void selectSquare(int index) {
        try {
            gameController.handleReturn(myLobby.selectSquare(clientID,index));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * To send the firemode selection from client terminal to server terminal
     *
     * @param firemode The firemode index from 0 to 2
     *
     */
    public void selectFiremode(int firemode) {
        try {
            gameController.handleReturn(myLobby.selectFiremode(clientID,firemode));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * To send the player selection from client terminal to server terminal
     *
     * @param targets The ArrayList witch contain players' color
     */
    public void selectPlayers(ArrayList<Color> targets) {
        try {
            gameController.handleReturn(myLobby.selectPlayers(clientID,targets));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send especial move action at MoveEnemyState or MoveSelfState from client terminal to server terminal
     *
     *
     */
    public void moveSubAction() {
        try {
            gameController.handleReturn(myLobby.moveSubAction(clientID));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * To send selection final frenzy action from client terminal to server terminal
     * <p>
     *     The action index range depending on the mode
     *     In mode 0, action index from 0 to 2
     *     In mode 1, action index from 0 to 1
     * </p>
     *
     *
     * @param action The action index range depending on the mode from 0 to 1(or 2)
     *
     */
    public void selectFinalFrenzyAction(int action) {
        try {
            gameController.handleReturn(myLobby.selectFinalFrenzyAction(clientID,action));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * The getter for ClientID
     *
     * @return The clientID string
     */
    @Override
    public String getClientID() {
        return clientID;
    }

    /**
     *
     * The getter for LobbyID
     *
     * @return The lobbyID string
     */
    @Override
    public String getMyLobbyID() {
        return myLobbyID;
    }

    /**
     *
     * For close the connection from server to client
     *
     *
     */
    @Override
    public void closeConnection() {
        myServer = null;
        myLobby = null;
    }
}
