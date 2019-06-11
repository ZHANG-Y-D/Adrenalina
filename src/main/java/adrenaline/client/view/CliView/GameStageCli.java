package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.fusesource.jansi.Ansi.ansi;

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

        printSrcFile("GameStareTitle.txt");



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
    public void notifyTimer(Integer duration) {



    }



    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {

        Runnable runnable = () -> System.out.println("                                                 ||CHAT >>>"
                            +ansi().eraseScreen().fg(trasnferColorToAnsiColor(senderColor)).a(nickname)
                            +": "+message);

        Thread changeThread = new Thread(runnable);
        changeThread.start();


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {



    }




}
