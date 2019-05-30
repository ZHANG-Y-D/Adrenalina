package adrenaline.client.view;


import adrenaline.server.controller.states.GameState;

import java.util.ArrayList;
import java.util.List;
import adrenaline.client.controller.GameController;

public interface ViewInterface {

    void showError(String error);
    void changeStage();
    void setGameController(GameController gameController);
    void notifyView();
}
