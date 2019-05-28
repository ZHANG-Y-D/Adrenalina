package adrenaline.server.network;

import adrenaline.client.ClientAPI;
import adrenaline.server.ServerAPI;
import adrenaline.server.GameServer;

import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;

public class RMIServerCommands extends UnicastRemoteObject implements ServerAPI {

    private final GameServer mainServer;

    public RMIServerCommands(GameServer mainServer) throws RemoteException{
        this.mainServer = mainServer;
    }

    public String registerRMIClient(ClientAPI clientAPI) {
        ClientRMIWrapper wrapper = new ClientRMIWrapper(clientAPI);
        this.mainServer.registerClient(wrapper);
        System.out.println("Client connected through RMI");
        return wrapper.getClientID();
    }

    public String setNickname(String clientID, String nickname) throws RemoteException {
        if(nickname.length()<1) return "Nickname must contain at least 1 character!";
        if(mainServer.setNickname(clientID, nickname)) return "/OK";
        else return "This nickname is already taken!";
    }

    public String unregisterClient(String clientID) {
        this.mainServer.unregisterClient(clientID);
        System.out.println("Client closed his RMI session");
        return "OK";
    }

}
