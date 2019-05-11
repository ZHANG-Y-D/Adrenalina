package server;

import client.ClientAPI;
import server.controller.Lobby;
import server.network.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

public class GameServer {
    private final int rmiPort = 1099;
    private final int socketPort = 1100;
    private final int TIMEOUT_IN_SECONDS = 60;

    private ArrayList<ClientAPI> clients;
    private ArrayList<ClientAPI> clientsWaitingList;
    private HashMap<String, Lobby> activeLobbies;

    public static void main(String args[]){
        new GameServer().lifeCycle();
    }

    public GameServer(){
        try {
            System.out.println("Setting up RMI Server...");
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            RMIServerCommands RMIAdrenalineServer = new RMIServerCommands(this);
            registry.bind("AdrenalineServer", RMIAdrenalineServer);

            System.out.println("Setting up Socket Server...");
            SocketServerCommands SocketAdrenalineServer = new SocketServerCommands(this);
            new Thread(() -> {
                try {
                    ServerSocket serverSocket = new ServerSocket(socketPort);
                    while(true){
                        Socket client = serverSocket.accept();
                        SocketAdrenalineServer.createListener(client);
                        System.out.println("Client connected through Socket!");
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("Server listening on ports\n\t"+ rmiPort + " (RMI service)\n\t" + socketPort +" (Socket service)");

        }catch(Exception e){
            System.out.println("Error setting up server!");
            e.printStackTrace();
        }
        clients = new ArrayList<>();
        clientsWaitingList = new ArrayList<>();
        activeLobbies = new HashMap<>();
    }

    private void lifeCycle(){
        while(true){
            if(clientsWaitingList.size()<3);
            else{
                long timestart = System.currentTimeMillis();
                while(clientsWaitingList.size()<5 && (System.currentTimeMillis() - timestart < TIMEOUT_IN_SECONDS*1000));
                // starts new lobby and assigns players to it
                Lobby newLobby = new Lobby(clientsWaitingList);
                activeLobbies.put(newLobby.getID(), newLobby);
                new Thread(newLobby).start();
                clientsWaitingList.clear();
            }
        }

    }

    public void registerClient(ClientAPI c){
        this.clients.add(c);
    }

    public void unregisterClient(ClientAPI c) { this.clients.remove(c);}

    public Lobby getLobby(String lobbyID) {
        return activeLobbies.get(lobbyID);
    }
}
