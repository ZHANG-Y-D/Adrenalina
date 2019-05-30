package adrenaline.server.network;

import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

import java.util.ArrayList;

public interface Client extends ClientAPI {
    String getClientID();
    String getNickname();
    boolean setNickname(String nickname);
    void setActive(boolean active);
    void setLobby(Lobby lobby, ArrayList<String> nicknames);
}
