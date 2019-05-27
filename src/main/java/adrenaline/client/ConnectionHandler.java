package adrenaline.client;

import adrenaline.server.ServerAPI;

public interface ConnectionHandler {
    void unregister();
    void setNickname(String nickname);
}
