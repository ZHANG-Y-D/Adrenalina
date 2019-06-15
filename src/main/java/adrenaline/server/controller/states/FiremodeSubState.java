package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Firemode;
import adrenaline.server.model.WeaponCard;

public interface FiremodeSubState extends GameState{
    void setContext(Lobby lobby, WeaponCard weapon, Firemode firemode, boolean actionExecuted);
}
