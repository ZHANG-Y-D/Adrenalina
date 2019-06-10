package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.server.model.Firemode;

public interface FiremodeSubState extends GameState{
    void setContext(Lobby lobby, Firemode firemode, boolean actionExecuted);
}
