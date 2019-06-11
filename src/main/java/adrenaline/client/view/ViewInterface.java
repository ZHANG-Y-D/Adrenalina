package adrenaline.client.view;


import adrenaline.Color;
import adrenaline.client.controller.GameController;

public interface ViewInterface {
    void showError(String error);
    void changeStage();
    void setGameController(GameController gameController);
    void notifyTimer(Integer duration, String comment);
    void newChatMessage(String nickname, Color senderColor, String message);
}
