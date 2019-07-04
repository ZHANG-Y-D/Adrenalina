package adrenaline.server.network;

import adrenaline.client.ClientAPI;
import adrenaline.server.ServerAPI;
import adrenaline.server.GameServer;

import java.rmi.*;
import java.rmi.server.*;

/**
 *
 * The ServerCommands class to do some internet operations extends UnicastRemoteObject implements ServerAPI
 *
 */
public class ServerCommands extends UnicastRemoteObject implements ServerAPI {

    private final GameServer mainServer;

    /**
     *
     * The constructor of ServerCommands class
     *
     * @param mainServer The GameServer reference
     * @throws RemoteException If have Remote problem, it will throws RemoteException
     */
    public ServerCommands(GameServer mainServer) throws RemoteException{
        this.mainServer = mainServer;
    }


    /**
     *
     * For RMI register
     *
     * @param clientAPI The ClientAPI reference
     * @return The clientID string
     * @throws RemoteException If have remote problem, it will throws RemoteException
     */
    public String registerRMIClient(ClientAPI clientAPI) {
        ClientRMIWrapper wrapper = new ClientRMIWrapper(clientAPI, this);
        mainServer.registerClient(wrapper);
        return wrapper.getClientID();
    }

    /**
     *
     * To to reconnectClient
     *
     * @param tempClientID tempClientID string
     * @param oldClientID oldClientID string
     * @return The result of the reconnect operation
     * @throws RemoteException If have remote problem, it will throws RemoteException
     */
    public String reconnectClient(String tempClientID, String oldClientID) {
        if(mainServer.reconnectClient(tempClientID, oldClientID)) return "OK";
        else return "KO";
    }


    /**
     *
     * To set nickname of the player
     *
     * @param clientID The string value of clientID
     * @param nickname The string value of nickname
     * @return The result of this operation.
     * @throws RemoteException If have remote problem, it will throws RemoteException
     */
    public String setNickname(String clientID, String nickname) {
        if(nickname.length()<1) return "Nickname must contain at least 1 character!";
        if(mainServer.setNickname(clientID, nickname)) return "OK Nickname selected";
        else return "This nickname is already taken!";
    }

    /**
     *
     * To unregister the Client
     *
     * @param clientID The clientID string
     * @return The result of this operation.
     * @throws RemoteException If have remote problem, it will throws RemoteException
     */
    public String unregisterClient(String clientID) {
        this.mainServer.unregisterClient(clientID);
        System.out.println("Client closed his session");
        return "OK";
    }


}
