package adrenaline.server;

import adrenaline.client.controller.GameController;

import java.io.Serializable;

public interface UpdateMessage extends Serializable {
    void applyUpdate(GameController clientGameController);
}
