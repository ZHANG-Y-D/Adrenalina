package adrenaline.server;

import adrenaline.server.network.*;
import adrenaline.server.controller.Lobby;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GameServer {

    private final int rmiPort = 1099;
    private final int socketPort = 1100;
    private final int TIMEOUT_IN_SECONDS = 10;

    private final HashMap<String, Client> clients;
    private final ArrayList<Client> clientsWaitingList;
    private final HashMap<String, Lobby> activeLobbies;
    private final HashMap<String, String> clientsLobbiesMap;
    private ArrayList<String> usedNicknames;

    public static void main(String args[]){
        new GameServer().lifeCycle();
    }

    public GameServer(){
        try {
            System.out.println("Setting up RMI Server...");
            RMIServerCommands RMIAdrenalineServer = new RMIServerCommands(this);
            try {
                LocateRegistry.createRegistry(rmiPort).bind("AdrenalineServer", RMIAdrenalineServer);
            }catch (ExportException e){
                System.err.println(" Rmi Port already in use: 1099. "+ e.detail);
            }


            System.out.println("Setting up Socket Server...");
            SocketServerCommands SocketAdrenalineServer = new SocketServerCommands(this);
            new Thread(() -> {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(socketPort);
                }catch (BindException e){
                    System.err.println("Address already in use (Bind failed) "+ e.getCause());
                }catch (IOException e) {
                    e.printStackTrace();
                }
                while(true) {
                    try {
                        Socket client = serverSocket.accept();
                        registerClient(new ClientSocketWrapper(client, SocketAdrenalineServer));
                        System.out.println("Client connected through Socket");
                    }catch (NullPointerException e){
                        e.getCause();
                    }catch (IOException e) { e.printStackTrace(); }
                }
            }).start();

            System.out.println("Server listening on ports\n\t"+ rmiPort + " (RMI service)\n\t" + socketPort +" (Socket service)");

        }catch(Exception e){
            System.out.println("Error setting up server.");
            e.printStackTrace();
        }
        clients = new HashMap<>();
        clientsWaitingList = new ArrayList<>();
        clientsLobbiesMap = new HashMap<>();
        activeLobbies = new HashMap<>();
        usedNicknames = new ArrayList<>();
    }

    private void lifeCycle(){
        while(true){
            synchronized(clientsWaitingList) {
                while (clientsWaitingList.size() < 3) {
                    try {
                        clientsWaitingList.wait();
                    } catch (InterruptedException e) { }
                }
                System.out.println("3 OR MORE PLAYERS WAITING FOR GAME: SETTING TIMER FOR NEW LOBBY.");
                long timestart = System.currentTimeMillis();
                while(clientsWaitingList.size() < 5){
                    if(clientsWaitingList.size() < 3) break;
                    long timeremaining = timestart + TIMEOUT_IN_SECONDS * 1000 - System.currentTimeMillis();
                    if(timeremaining <= 0) break;
                    else{
                        try {
                            clientsWaitingList.wait(timeremaining);
                        } catch (InterruptedException e) { }
                    }
                }
                if(clientsWaitingList.size() >= 3) {
                    Lobby newLobby = new Lobby(clientsWaitingList);
                    activeLobbies.put(newLobby.getID(), newLobby);
                    RMIexportLobby(newLobby);
                    ArrayList<String> nicknames = (ArrayList<String>) clientsWaitingList.stream().map(Client::getNickname).collect(Collectors.toList());
                    for (Client c : clientsWaitingList) {
                        clientsLobbiesMap.put(c.getClientID(), newLobby.getID());
                        c.setLobby(newLobby, nicknames);
                    }
                    new Thread(newLobby).start();
                    clientsWaitingList.clear();
                }
            }
        }
    }


    public void registerClient(Client c){
        clients.put(c.getClientID(), c);
    }

    public void unregisterClient(String cID) {
        Client c = clients.get(cID);
        c.setActive(false);
        synchronized (clientsWaitingList){
            this.clientsWaitingList.remove(c);
            clientsWaitingList.notifyAll();
        }
    }

    public boolean setNickname(String cID, String nickname){
        if(usedNicknames.contains(nickname)) return false;
        Client c = clients.get(cID);
        if(c.setNickname(nickname)) {
            usedNicknames.add(nickname);
            synchronized (clientsWaitingList) {
                this.clientsWaitingList.add(c);
                clientsWaitingList.notifyAll();
            }
        }
        return true;
    }



    private void RMIexportLobby(Lobby lobby){
        try {
            LocateRegistry.getRegistry(rmiPort).bind("Game;"+lobby.getID(), new LobbyExportable(lobby));
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
