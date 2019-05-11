package server.network;

import client.ClientAPI;
import server.GameServer;
import server.ServerAPI;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;

public class SocketServerCommands implements ServerAPI {

    private final GameServer mainServer;
    private HashMap<String,String> clientLobbyMap;

    public SocketServerCommands(GameServer mainServer){
        this.mainServer = mainServer;
    }

    public void registerClient(ClientAPI clientAPI, String nickname) throws RemoteException {
        mainServer.registerClient(clientAPI);
    }

    public void registerClient(Socket client, String nickname) throws RemoteException {
        registerClient(new ClientSocketWrapper(client, nickname), null);
    }

    public void unregisterClient(ClientAPI clientAPI) throws RemoteException {

    }

    public void exampleMethod(String clientID) throws RemoteException {
        //mainServer.getLobby(clientLobbyMap.get(clientID)).foo();
    }

    public void createListener(Socket client) {
        new Thread(() -> {
            try {
                DataInputStream inputFromClient = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                while(true){
                    inputFromClient.read();
                    //TODO parsing and "pre-validating"
                }
            }catch (IOException e) {}
        }
        ).start();

    }
}
