package adrenaline;

import adrenaline.client.controller.Controller;

import java.io.Serializable;

public interface UpdateMessage extends Serializable {
    void applyUpdate(Controller clientController);
}
