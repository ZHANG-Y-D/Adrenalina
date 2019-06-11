package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {





    public GameStageCli(GameController gameController) {

        this.gameController = gameController;
        gameController.setViewController(this);
        gameController.addPropertyChangeListener(this);
        returnValueIsOk.set(0);
        initialStageCli();

    }


    @Override
    protected void initialStageCli() {

        System.err.println("Successful!!!!");

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void changeStage() {

    }

    @Override
    public void setGameController(GameController gameController) {

    }


    @Override
    public void notifyTimer(Integer duration, String comment) {

        System.out.println("You still have "+duration+" Secondi");

    }


    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {



    }


}
