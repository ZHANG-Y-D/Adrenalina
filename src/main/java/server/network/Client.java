package server.network;

import client.ClientAPI;
import server.controller.Lobby;

public interface Client extends ClientAPI {
    String getClientID();
    void setLobby(Lobby lobby);
}
