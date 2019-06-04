package adrenaline.client;

import adrenaline.Color;

public interface ConnectionHandler {
    void unregister();
    void setNickname(String nickname);
    void setMyLobby(String LobbyID);
    void selectAvatar(Color color);
    String getClientID();
    String getMyLobbyID();
}
