package adrenaline.server;

import adrenaline.server.controller.Lobby;
import adrenaline.server.network.Client;
import adrenaline.server.network.ClientSocketWrapper;
import adrenaline.server.network.LobbyExportable;
import adrenaline.server.network.ServerCommands;
import com.google.gson.Gson;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;


/**
 *
 *
 * The game server main class
 *
 */
public class GameServer {

    private final ServerSettings SERVER_SETTINGS;
    private final HashMap<String, Client> clients;
    private final ArrayList<Client> clientsWaitingList;
    private final HashMap<String, Lobby> activeLobbies;
    private final HashMap<String, String> clientsLobbiesMap;
    private ArrayList<String> usedNicknames;

    /**
     *
     * The main method for run game server
     */
    public static void main(String[] args){
        new GameServer().lifeCycle();
    }

    /**
     *
     * The constructor of game server
     *
     */
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


    /**
     *
     * The life cycle of game server
     *
     */
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


    /**
     *
     * For register client
     *
     * @param c The reference of the client who have to be register
     */
    public void registerClient(Client c){
        clients.put(c.getClientID(), c);
    }

    /**
     *
     * For unregister client
     *
     * @param cID The reference of the client who have to be unregister
     */
    public void unregisterClient(String cID) {
        Client c = clients.get(cID);
        c.setActive(false);
        synchronized (clientsWaitingList){
            if(clientsWaitingList.remove(c)) clientsWaitingList.notifyAll();
        }
        String assignedLobby = clientsLobbiesMap.get(cID);
        if(assignedLobby!=null) activeLobbies.get(assignedLobby).detachClient(c);
    }


    /**
     *
     * For reconnect client
     * @param newClientID The client id now
     * @param oldClientID The client id before
     * @return The boolean if the reconnect is completed
     */
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

    /**
     *
     * For set nickname
     * @param cID The clientID
     * @param nickname The nickname string
     * @return If this operation is successful
     */
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


    /**
     *
     *
     *
     * @param lobby
     */
    private void RMIexportLobby(Lobby lobby){
        try {
            LocateRegistry.getRegistry(SERVER_SETTINGS.getRMI_PORT()).bind("Game;"+lobby.getID(), new LobbyExportable(lobby));
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * For close a lobby
     *
     * @param lobbyID The lobby id witch have to be close
     */
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


/**
 *
 * To do thw server set
 *
 */
class ServerSettings{
    private final int RMI_PORT;
    private final int SOCKET_PORT;
    private final String RMI_HOSTNAME;
    private final int WAITLIST_TIMEOUT_IN_SECONDS;

    /**
     * To do the server set
     * @param rmi_port The rmi port,The default value is 1099
     * @param socket_port The socket port, The default value is 1100
     * @param rmi_hostname The rmi host name
     * @param waitlist_timeout_in_seconds The wait list time for build a lobby
     */
    ServerSettings(int rmi_port, int socket_port, String rmi_hostname, int waitlist_timeout_in_seconds) {
        RMI_PORT = rmi_port;
        SOCKET_PORT = socket_port;
        RMI_HOSTNAME = rmi_hostname;
        WAITLIST_TIMEOUT_IN_SECONDS = waitlist_timeout_in_seconds;
    }

    /**
     * The getter of rmi port
     * @return The int value of rmi port
     */
    public int getRMI_PORT() { return RMI_PORT; }

    /**
     *
     *
     * The getter of socket port
     *
     * @return The socket port value
     */
    public int getSOCKET_PORT() { return SOCKET_PORT; }

    /**
     *
     * The getter of rmi hostname
     *
     * @return The rmi hostname
     */
    public String getRMI_HOSTNAME() { return RMI_HOSTNAME; }

    /**
     *
     *
     * The getter of wait list time
     *
     * @return The wait list time in seconds
     */
    public int getWAITLIST_TIMEOUT_IN_SECONDS() { return WAITLIST_TIMEOUT_IN_SECONDS; }
}
