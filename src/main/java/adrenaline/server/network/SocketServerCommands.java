package adrenaline.server.network;

import adrenaline.client.ClientAPI;
import adrenaline.server.GameServer;
import adrenaline.server.ServerAPI;

import java.rmi.RemoteException;
import java.util.HashMap;

public class SocketServerCommands implements ServerAPI {

    private final GameServer mainServer;
    private HashMap<String,String> clientLobbyMap;

    public SocketServerCommands(GameServer mainServer){
        this.mainServer = mainServer;
    }

    public String registerRMIClient(ClientAPI clientAPI, String nickname) throws RemoteException {
        //operation not available for socket
        return null;
    }


    public void unregisterClient(String clientID) throws RemoteException {

    }

}
