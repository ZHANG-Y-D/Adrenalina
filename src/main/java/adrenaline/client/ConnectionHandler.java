package adrenaline.client;

public interface ConnectionHandler {
    void unregister();
    void setNickname(String nickname);
    void setMyLobby(String LobbyID);
}
