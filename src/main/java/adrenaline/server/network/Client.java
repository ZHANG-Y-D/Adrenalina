package adrenaline.server.network;

import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

public interface Client extends ClientAPI {
    String getClientID();
    void setLobby(Lobby lobby);
}
