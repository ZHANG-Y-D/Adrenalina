package client;

import server.model.Color;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientAPI extends Remote {
    void setLobby(String lobbyID) throws RemoteException;
    void showLobbyDetails(ArrayList<Color> availableColors) throws RemoteException;
}
