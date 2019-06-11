package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class GameStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {


    Scanner chatScanner = new Scanner(System.in);


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
        openChat();


    }

    private void openChat() {
        Runnable runnableChat = () -> {

            String chat;
            do {
                chat=chatScanner.nextLine();
                isQuit(chat);
                if (chat.contains("chat:") || chat.contains("CHAT:")) {
                    gameController.sendChatMessage(chat.replace("CHAT:",""));
                }
            } while (true);

        };
        Thread chatThread = new Thread(runnableChat);
        chatThread.start();
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



    }



    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {

        Runnable runnable = () -> System.out.println(ansi().eraseScreen().fgDefault().a("                                                     ||CHAT >>>").
                            eraseScreen().bold().fg(trasnferColorToAnsiColor(senderColor)).
                                a(nickname+": "+message).eraseScreen().fgDefault());

        Thread changeThread = new Thread(runnable);
        changeThread.start();


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {



    }




}
