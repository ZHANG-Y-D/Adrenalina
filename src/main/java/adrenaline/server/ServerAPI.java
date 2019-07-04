package adrenaline.server;

import adrenaline.client.ClientAPI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * The serverAPI interface, for internet operation
 *
 */
public interface ServerAPI extends Remote {


    /**
     *
     * For RMI register
     *
     * @param clientAPI The ClientAPI reference
     * @return The clientID string
     * @throws RemoteException If have remote problem, it will throws RemoteException
     */
    String registerRMIClient(ClientAPI clientAPI)throws  RemoteException;

    /**
     *
     * To to reconnectClient
     *
     * @param tempClientID tempClientID string
     * @param oldClientID oldClientID string
     * @return The result of the reconnect operation
     * @throws RemoteException If have remote problem, it will throws RemoteException
     */
    String reconnectClient(String tempClientID, String oldClientID) throws RemoteException;

    /**
     *
     * To set nickname of the player
     *
     * @param clientID The string value of clientID
     * @param nickname The string value of nickname
     * @return The result of this operation.
     * @throws RemoteException If have remote problem, it will throws RemoteException
     */
    String setNickname(String clientID, String nickname) throws RemoteException;

    /**
     *
     * To unregister the Client
     *
     * @param clientID The clientID string
     * @return The result of this operation.
     * @throws RemoteException If have remote problem, it will throws RemoteException
     */
    String unregisterClient(String clientID)throws RemoteException;
}
