package adrenaline.client;

import adrenaline.Color;
import adrenaline.UpdateMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientAPI extends Remote {
    void setLobby(String lobbyID, ArrayList<String> nicknames) throws RemoteException;
    void setPlayerColor(String nickname, Color color) throws  RemoteException;
    void timerStarted(Integer duration, String comment) throws RemoteException;
    void validSquaresInfo(ArrayList<Integer> validSquares) throws RemoteException;
    void update(UpdateMessage updatemsg) throws RemoteException;
}
