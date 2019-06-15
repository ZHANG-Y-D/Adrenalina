package adrenaline.client.view;


import adrenaline.Color;
import adrenaline.client.controller.GameController;

import java.util.ArrayList;

public interface ViewInterface {
    void showError(String error);
    void changeStage();
    void setGameController(GameController gameController);
    void notifyTimer(Integer duration, String comment);
    void newChatMessage(String nickname, Color senderColor, String message);
    void showValidSquares(ArrayList<Integer> validSquares);
}
