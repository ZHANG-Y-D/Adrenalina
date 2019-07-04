package adrenaline.server.network;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * The client socket wrapper class implements Client interface,it will send package from server to client via RMI
 *
 */
public class ClientRMIWrapper implements Client {
    private String clientID;
    private String nickname = null;
    private ClientAPI thisClient;
    private ServerCommands serverCommands;
    private Lobby inLobby;
    private boolean active;

    /**
     *
     *
     * The constructor of client rmi wrapper
     *
     *
     * @param newClient The new client's ClientAPI
     * @param serverCommands The server commands
     *
     */
    public ClientRMIWrapper(ClientAPI newClient, ServerCommands serverCommands) {
        clientID = UUID.randomUUID().toString();
        thisClient = newClient;
        this.serverCommands = serverCommands;
        active = true;
    }

    /**
     *
     * The setter of ClientID
     * @param ID The ClientID string
     */
    public void setClientID(String ID) { clientID = ID; }


    /**
     *
     * The getter of ClientID
     *
     * @return The ClientID string
     */
    public String getClientID() {
        return clientID;
    }

    /**
     *
     * The getter of nick name
     *
     * @return The nickname string
     */
    public String getNickname(){ return nickname; }

    /**
     *
     * Call the setNickname method to send the current player's nickname from server terminal to client terminal
     *
     * @param nickname The nickname string
     * @return The set operation if is successful
     */
    public boolean setNicknameInternal(String nickname) {
        if(this.nickname != null){
            return false;
        }
        this.nickname = nickname;
        setNickname(nickname);
        return true;
    }

    /**
     *
     * To send the current player's nickname from server terminal to client terminal
     *
     * @param nickname The nickname string
     *
     */
    public void setNickname(String nickname) {
        if(active) {
            try {
                thisClient.setNickname(nickname);
            } catch (RemoteException e) {
                active = false;
                serverCommands.unregisterClient(clientID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * Setter of active status
     *
     * @param active True for still active
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     *
     * The getter of active status
     *
     * @return True for still active
     */
    public boolean isActive() { return active; }


    /**
     *
     * Call setLobby to send the lobbyID and players' nickname ArrayList from server terminal to client terminal
     *
     * @param lobby The lobby id string
     * @param nicknames The players' nickname ArrayList
     */
    public void setLobby(Lobby lobby, ArrayList<String> nicknames) {
        inLobby = lobby;
        setLobby(lobby.getID(), nicknames);
    }

    /**
     *
     * To send the lobbyID and players' nickname ArrayList from server terminal to client terminal
     *
     * @param lobbyID The lobby id string
     * @param nicknames The players' nickname ArrayList
     */
    public void setLobby(String lobbyID, ArrayList<String> nicknames){
        if(active) {
            try {
                thisClient.setLobby(lobbyID, nicknames);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * Call setPlayerColor method to send the player color set value from server terminal to client terminal
     *
     * @param nickname The nickname of players
     * @param color The color of players
     */
    public void setPlayerColorInternal(String nickname, Color color) {
        setPlayerColor(nickname, color);
    }

    /**
     *
     *
     * Send the player color set value from server terminal to client terminal
     *
     * @param nickname The nickname of players
     * @param color The color of players
     */
    public void setPlayerColor(String nickname, Color color){
        if(active) {
            try {
                thisClient.setPlayerColor(nickname, color);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     *
     * To remind the client timer start.
     *
     * @param duration The duration of timer in seconds
     * @param comment The comment for this timer
     */
    public void timerStarted(Integer duration, String comment) {
        if(active) {
            try {
                thisClient.timerStarted(duration, comment);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * To send the valid squares from server to client terminal
     *
     * @param validSquares The ArrayList of the valid squares
     *
     */
    public void validSquaresInfo(ArrayList<Integer> validSquares) {
        if(active) {
            try {
                thisClient.validSquaresInfo(validSquares);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     *
     * To send the update message from server terminal to client terminal
     *
     * @param updatemsg The UpdateMessage reference
     *
     */
    public void update(UpdateMessage updatemsg) {
        if(active) {
            try {
                thisClient.update(updatemsg);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * To kick a client when he did nothing during the whole turn,
     * he have to do the reconnection to reconnect the server
     *
     */
    public void kickClient() {
        serverCommands.unregisterClient(clientID);
        kick();
    }


    /**
     *
     * To kick a client when he did nothing during the whole turn
     * he have to do the reconnection to reconnect the server
     */
    public void kick() {
        try{
            thisClient.kick();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
