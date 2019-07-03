package adrenaline.server;

import adrenaline.server.network.*;
import adrenaline.server.controller.Lobby;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GameServer {

    private final ServerSettings SERVER_SETTINGS;
    private final HashMap<String, Client> clients;
    private final ArrayList<Client> clientsWaitingList;
    private final HashMap<String, Lobby> activeLobbies;
    private final HashMap<String, String> clientsLobbiesMap;
    private ArrayList<String> usedNicknames;

    public static void main(String args[]){
        new GameServer().lifeCycle();
    }

    public GameServer(){
        Gson gson = new Gson();
        SERVER_SETTINGS = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/Jsonsrc/server-settings.json")), ServerSettings.class);
        try {
            System.out.println("Setting up RMI Server...");
            ServerCommands RMIAdrenalineServer = new ServerCommands(this);
            System.setProperty("java.rmi.server.hostname", SERVER_SETTINGS.getRMI_HOSTNAME());
            try {
                LocateRegistry.createRegistry(SERVER_SETTINGS.getRMI_PORT()).bind("AdrenalineServer", RMIAdrenalineServer);
            }catch (ExportException e){
                System.err.println(" Rmi Port already in use: 1099. "+ e.detail);
            }

            System.out.println("Setting up Socket Server...");
            ServerCommands SocketAdrenalineServer = new ServerCommands(this);
            new Thread(() -> {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(SERVER_SETTINGS.getSOCKET_PORT());
                }catch (BindException e){
                    System.err.println("Address already in use (Bind failed) "+ e.getCause());
                }catch (IOException e) {
                    e.printStackTrace();
                }
                while(true) {
                    try {
                        Socket client = serverSocket.accept();
                        registerClient(new ClientSocketWrapper(client, SocketAdrenalineServer));
                    }catch (NullPointerException e){
                        e.getCause();
                    }catch (IOException e) { e.printStackTrace(); }
                }
            }).start();

            System.out.println("Server listening on ports\n\t"+ SERVER_SETTINGS.getRMI_PORT() + " (RMI service)\n\t" + SERVER_SETTINGS.getSOCKET_PORT() +" (Socket service)");

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
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("3 OR MORE PLAYERS WAITING FOR GAME: SETTING TIMER FOR NEW LOBBY.");
                long timestart = System.currentTimeMillis();
                while(clientsWaitingList.size() < 5){
                    if(clientsWaitingList.size() < 3) break;
                    long timeremaining = timestart + SERVER_SETTINGS.getWAITLIST_TIMEOUT_IN_SECONDS() * 1000 - System.currentTimeMillis();
                    if(timeremaining <= 0) break;
                    else{
                        try {
                            clientsWaitingList.wait(timeremaining);
                        } catch (InterruptedException e) {
                            //add logger
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                if(clientsWaitingList.size() >= 3) {
                    Lobby newLobby = new Lobby(clientsWaitingList, this);
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
            if(clientsWaitingList.remove(c)) clientsWaitingList.notifyAll();
        }
        String assignedLobby = clientsLobbiesMap.get(cID);
        if(assignedLobby!=null) activeLobbies.get(assignedLobby).detachClient(c);
    }

    public boolean reconnectClient(String newClientID, String oldClientID){
        Client oldC = clients.get(oldClientID);
        Client newC = clients.get(newClientID);
        if(oldC==null || oldC.isActive() || newC.getNickname()!=null) return false;
        newC.setClientID(oldClientID);
        newC.setNicknameInternal(oldC.getNickname());
        clients.put(oldClientID, clients.remove(newClientID));
        String assignedLobby = clientsLobbiesMap.get(oldClientID);
        if(assignedLobby!=null){
            ArrayList<String> playersNicknames = new ArrayList<>();
            clientsLobbiesMap.forEach((x,y) -> {
                if(y.equals(assignedLobby)) playersNicknames.add(clients.get(x).getNickname());
            });
            Lobby lobby = activeLobbies.get(assignedLobby);
            newC.setLobby(lobby, playersNicknames);
            lobby.updateClient(oldClientID, newC);
        }else{
            synchronized (clientsWaitingList){
                clientsWaitingList.remove(oldC);
                clientsWaitingList.add(newC);
                clientsWaitingList.notifyAll();
            }
        }
        return true;
    }

    public boolean setNickname(String cID, String nickname){
        if(usedNicknames.contains(nickname)) return false;
        Client c = clients.get(cID);
        if(c.setNicknameInternal(nickname)) {
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
            LocateRegistry.getRegistry(SERVER_SETTINGS.getRMI_PORT()).bind("Game;"+lobby.getID(), new LobbyExportable(lobby));
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public void closeLobby(String lobbyID){
        activeLobbies.remove(lobbyID);
        clientsLobbiesMap.entrySet().removeIf(e -> lobbyID.equals(e.getValue()));
        try {
            LocateRegistry.getRegistry(SERVER_SETTINGS.getRMI_PORT()).unbind("Game;"+lobbyID);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}

class ServerSettings{
    private final int RMI_PORT;
    private final int SOCKET_PORT;
    private final String RMI_HOSTNAME;
    private final int WAITLIST_TIMEOUT_IN_SECONDS;

    ServerSettings(int rmi_port, int socket_port, String rmi_hostname, int waitlist_timeout_in_seconds) {
        RMI_PORT = rmi_port;
        SOCKET_PORT = socket_port;
        RMI_HOSTNAME = rmi_hostname;
        WAITLIST_TIMEOUT_IN_SECONDS = waitlist_timeout_in_seconds;
    }

    public int getRMI_PORT() { return RMI_PORT; }

    public int getSOCKET_PORT() { return SOCKET_PORT; }

    public String getRMI_HOSTNAME() { return RMI_HOSTNAME; }

    public int getWAITLIST_TIMEOUT_IN_SECONDS() { return WAITLIST_TIMEOUT_IN_SECONDS; }
}
