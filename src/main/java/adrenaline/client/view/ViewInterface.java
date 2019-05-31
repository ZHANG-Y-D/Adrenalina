package adrenaline.client.view;


import adrenaline.client.controller.GameController;

public interface ViewInterface {

    void showError(String error);
    void changeStage();
    void setGameController(GameController gameController);
    void notifyView();

}
