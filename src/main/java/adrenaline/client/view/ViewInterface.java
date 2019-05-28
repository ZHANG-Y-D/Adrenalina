package adrenaline.client.view;

import adrenaline.client.controller.Controller;
import adrenaline.server.controller.states.GameState;

import java.util.List;

public interface ViewInterface {

    void showError(String error);

    void setController(Controller controller);

    void changeState(List<GameState> gameStateList);



}
