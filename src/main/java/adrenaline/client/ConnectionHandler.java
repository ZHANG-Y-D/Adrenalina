package adrenaline.client;

import adrenaline.Color;

public interface ConnectionHandler {
    void unregister();
    void setNickname(String nickname);
    void setMyLobby(String LobbyID);
    void selectAvatar(Color color);
    void selectPowerUp(int powerupID);
    void selectWeapon(int weaponID);
    void sendSettings(int selectedMap, int selectedSkull);
    void sendChatMessage(String message);
    void run();
    void grab();
    void shoot();
    void back();
    void endTurn();
    void selectSquare(int index);
    String getClientID();
    String getMyLobbyID();
}
