package adrenaline.client;

import adrenaline.Color;

public interface ConnectionHandler {
    void unregister();
    void setNickname(String nickname);
    void setMyLobby(String LobbyID);
    void selectAvatar(Color color);
    void sendSettings(int selectedMap, int selectedSkull);
    void sendChatMessage(String message);
    String getClientID();
    String getMyLobbyID();
}
