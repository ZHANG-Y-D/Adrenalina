package adrenaline.server.network;

import adrenaline.client.ClientAPI;
import adrenaline.server.ServerAPI;
import adrenaline.server.GameServer;

import java.rmi.*;
import java.rmi.server.*;

public class ServerCommands extends UnicastRemoteObject implements ServerAPI {

    private final GameServer mainServer;

    public ServerCommands(GameServer mainServer) throws RemoteException{
        this.mainServer = mainServer;
    }

    public String registerRMIClient(ClientAPI clientAPI) {
        ClientRMIWrapper wrapper = new ClientRMIWrapper(clientAPI);
        mainServer.registerClient(wrapper);
        return wrapper.getClientID();
    }

    public String reconnectRMIClient(ClientAPI clientAPI, String oldClientID) {
        ClientRMIWrapper wrapper = new ClientRMIWrapper(clientAPI);
        mainServer.registerClient(wrapper);
        if(mainServer.reconnectClient(wrapper.getClientID(), oldClientID)) return "OK";
        else return "KO";
    }

    public String reconnectSocketClient(String tempClientID, String oldClientID) {
        if(mainServer.reconnectClient(tempClientID, oldClientID)) return "OK";
        else return "KO";
    }


    public String setNickname(String clientID, String nickname) {
        if(nickname.length()<1) return "Nickname must contain at least 1 character!";
        if(mainServer.setNickname(clientID, nickname)) return "OK Nickname selected";
        else return "This nickname is already taken!";
    }

    public String unregisterClient(String clientID) {
        this.mainServer.unregisterClient(clientID);
        System.out.println("Client closed his session");
        return "OK";
    }


}
