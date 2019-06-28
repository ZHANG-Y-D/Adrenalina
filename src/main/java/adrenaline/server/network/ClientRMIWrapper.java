package adrenaline.server.network;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

import java.rmi.ConnectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

public class ClientRMIWrapper implements Client {
    private String clientID;
    private String nickname = null;
    private ClientAPI thisClient;
    private ServerCommands serverCommands;
    private Lobby inLobby;
    private boolean active;

    public ClientRMIWrapper(ClientAPI newClient, ServerCommands serverCommands) {
        clientID = UUID.randomUUID().toString();
        thisClient = newClient;
        this.serverCommands = serverCommands;
        active = true;
    }

    public void setClientID(String ID) { clientID = ID; }

    public String getClientID() {
        return clientID;
    }

    public String getNickname(){ return nickname; }

    public boolean setNicknameInternal(String nickname) {
        if(this.nickname != null){
            return false;
        }
        this.nickname = nickname;
        setNickname(nickname);
        return true;
    }

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

    public void setActive(boolean active) { this.active = active; }

    public boolean isActive() { return active; }

    public void setLobby(Lobby lobby, ArrayList<String> nicknames) {
        inLobby = lobby;
        setLobby(lobby.getID(), nicknames);
    }

    public void setLobby(String lobbyID, ArrayList<String> nicknames){
        if(active) {
            try {
                thisClient.setLobby(lobbyID, nicknames);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
                inLobby.detachClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setPlayerColorInternal(String nickname, Color color) {
        setPlayerColor(nickname, color);
    }

    public void setPlayerColor(String nickname, Color color){
        if(active) {
            try {
                thisClient.setPlayerColor(nickname, color);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
                inLobby.detachClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void timerStarted(Integer duration, String comment) {
        if(active) {
            try {
                thisClient.timerStarted(duration, comment);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
                inLobby.detachClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void validSquaresInfo(ArrayList<Integer> validSquares) {
        if(active) {
            try {
                thisClient.validSquaresInfo(validSquares);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
                inLobby.detachClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(UpdateMessage updatemsg) {
        if(active) {
            try {
                thisClient.update(updatemsg);
            } catch (RemoteException e) {
                serverCommands.unregisterClient(clientID);
                inLobby.detachClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void kickClient() {
        serverCommands.unregisterClient(clientID);
        kick();
    }

    public void kick() {
        try{
            thisClient.kick();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
