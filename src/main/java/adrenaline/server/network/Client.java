package adrenaline.server.network;

import adrenaline.Color;
import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

import java.util.ArrayList;

public interface Client extends ClientAPI {
    void setClientID(String ID);
    String getClientID();
    String getNickname();
    boolean setNicknameInternal(String nickname);
    void setActive(boolean active);
    boolean isActive();
    void setLobby(Lobby lobby, ArrayList<String> nicknames);
    void setPlayerColorInternal(String nickname, Color color);
    void kickClient();
}
