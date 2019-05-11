package server.network;

import server.controller.Lobby;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class ClientSocketWrapper implements Client {
    private final String clientID;
    private String nickname = null;
    private Socket thisClient;
    private boolean active;

    private final SocketServerCommands serverCommands;
    private Lobby inLobby;

    public ClientSocketWrapper(Socket newClient, SocketServerCommands serverCommands){
        this.clientID = UUID.randomUUID().toString();
        this.thisClient = newClient;
        this.active = true;
        createListener();
        this.serverCommands = serverCommands;
    }

    public void createListener() {
        new Thread(() -> {
            try {
                DataInputStream inputFromClient = new DataInputStream(new BufferedInputStream(thisClient.getInputStream()));
                while(nickname==null){
                    inputFromClient.read();
                    //TODO cant proceed until client sends his nickname. note:nickname becomes effectively final
                }
                while(true){
                    inputFromClient.read();
                    //TODO parsing and "pre-validating"
                }
            }catch (IOException e) {}
        }
        ).start();
    }

    public String getClientID() {
        return clientID;
    }

    public void setLobby(Lobby lobby) {
        this.inLobby=lobby;
        setLobby(inLobby.getID());
    }

    public void setLobby(String lobbyID) {
    }
}
