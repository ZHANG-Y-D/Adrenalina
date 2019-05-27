package adrenaline.server.network;

import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

public interface Client extends ClientAPI {
    String getClientID();
    String getNickname();
    void setNickname(String nickname);
    void setActive(boolean active);
    void setLobby(Lobby lobby);
}
