package adrenaline;

import adrenaline.client.controller.GameController;

import java.io.Serializable;


/**
 *
 *
 * To update massage.The realize of observer pattern
 *
 */
public interface UpdateMessage extends Serializable {

    /**
     *
     * To apply update
     * @param clientGameController The game controller if client
     */
    void applyUpdate(GameController clientGameController);
}
