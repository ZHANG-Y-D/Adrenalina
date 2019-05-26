package adrenaline.server;

import adrenaline.server.controller.Lobby;
import adrenaline.server.network.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;

public class GameServer {
    private final int rmiPort = 1099;
    private final int socketPort = 1100;
    private final int TIMEOUT_IN_SECONDS = 60;

    private ArrayList<Client> clients;
    private ArrayList<Client> clientsWaitingList;
    private HashMap<String, Lobby> activeLobbies;
    private HashMap<String, String> clientsLobbiesMap;

    public static void main(String args[]){
        new GameServer().lifeCycle();
    }

    public GameServer(){
        try {
            System.out.println("Setting up RMI Server...");
            RMIServerCommands RMIAdrenalineServer = new RMIServerCommands(this);
            LocateRegistry.createRegistry(rmiPort).bind("AdrenalineServer", RMIAdrenalineServer);

            System.out.println("Setting up Socket Server...");
            SocketServerCommands SocketAdrenalineServer = new SocketServerCommands(this);
            new Thread(() -> {
                try {
                    ServerSocket serverSocket = new ServerSocket(socketPort);
                    while(true){
                        Socket client = serverSocket.accept();
                        registerClient(new ClientSocketWrapper(client, SocketAdrenalineServer));
                        System.out.println("Client connected through Socket!");
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("Server listening on ports\n\t"+ rmiPort + " (RMI service)\n\t" + socketPort +" (Socket service)");

        }catch(Exception e){
            System.out.println("Error setting up adrenaline.server!");
            e.printStackTrace();
        }
        clients = new ArrayList<>();
        clientsWaitingList = new ArrayList<>();
        clientsLobbiesMap = new HashMap<>();
        activeLobbies = new HashMap<>();
    }

    private void lifeCycle(){
        while(true){
            if(clientsWaitingList.size()<3);
            else{
                long timestart = System.currentTimeMillis();
                while(clientsWaitingList.size()<5 && (System.currentTimeMillis() - timestart < TIMEOUT_IN_SECONDS*1000));

                synchronized(clientsWaitingList) {
                    Lobby newLobby = new Lobby(clientsWaitingList);
                    activeLobbies.put(newLobby.getID(), newLobby);
                    RMIexportLobby(newLobby);
                    for (Client c : clientsWaitingList) {
                        clientsLobbiesMap.put(c.getClientID(), newLobby.getID());
                        c.setLobby(newLobby);
                    }
                    new Thread(newLobby).start();
                    clientsWaitingList.clear();
                }
            }
        }
    }

    public void registerClient(Client c){
        this.clients.add(c);
        this.clientsWaitingList.add(c);
    }

    public void unregisterClient(String c) { }

    private void RMIexportLobby(Lobby lobby){
        try {
            LocateRegistry.getRegistry(rmiPort).bind("Game;"+lobby.getID(), new LobbyExportable(lobby));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
