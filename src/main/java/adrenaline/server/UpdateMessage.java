package adrenaline.server;

import adrenaline.client.controller.Controller;

public interface UpdateMessage {
    void applyUpdate(Controller clientController);
}
