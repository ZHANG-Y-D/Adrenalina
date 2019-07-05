package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.WeaponCard;


/**
 *
 * The FiremodeSubState interface, made for handling the different weapon effects
 *
 */
public interface FiremodeSubState extends GameState{

    /**
     *
     * To set the operation context
     *
     * @param lobby The current lobby
     * @param weapon The current weapon card
     * @param firemode The current firemode
     * @param actionExecuted A boolean value index if this action is executed
     */
    void setContext(Lobby lobby, WeaponCard weapon, Firemode firemode, boolean actionExecuted);
}
