package adrenaline;

import adrenaline.server.controller.Lobby;
import adrenaline.server.network.Client;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientStub implements Client {

    private String clientID;
    private String nickname;
    private boolean active;
    private Lobby lobby;

    public ClientStub(String clientID){
        this.clientID = clientID;
        active = true;
        nickname = clientID;
    }

    public void setClientID(String ID) {
        this.clientID = clientID;
        active = true;
    }

    @Override
    public String getClientID() {
        return clientID;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean setNicknameInternal(String nickname) {
        return false;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setLobby(Lobby lobby, ArrayList<String> nicknames) {

    }

    @Override
    public void setPlayerColorInternal(String nickname, Color color) {

    }

    @Override
    public void kickClient() {

    }

    @Override
    public void setNickname(String nickname) throws RemoteException {

    }

    @Override
    public void setLobby(String lobbyID, ArrayList<String> nicknames) throws RemoteException {

    }

    @Override
    public void setPlayerColor(String nickname, Color color) throws RemoteException {

    }

    @Override
    public void timerStarted(Integer duration, String comment) throws RemoteException {

    }

    @Override
    public void validSquaresInfo(ArrayList<Integer> validSquares) throws RemoteException {

    }

    @Override
    public void update(UpdateMessage updatemsg) throws RemoteException {

    }

    @Override
    public void kick() throws RemoteException {

    }
}
