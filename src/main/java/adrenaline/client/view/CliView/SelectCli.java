package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.util.Scanner;


public class SelectCli extends ControllerCli implements ViewInterface {




    @Override
    public void showError(String error) {

    }

    @Override
    public void changeStage() {

    }

    @Override
    public void setGameController(GameController gameController) {

    }

    public void notifyView() {

    }

    @Override
    public void notifyTimer(Integer duration) {

    }

    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {
        //called when server notifies client of a new message in chat
    }
}
