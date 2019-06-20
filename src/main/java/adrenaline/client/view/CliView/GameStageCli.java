package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.fusesource.jansi.Ansi.ansi;

public class GameStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {


    private Scanner chatScanner = new Scanner(System.in);
    private Boolean isFirstTurn = true;
    private AtomicBoolean chatBlock = new AtomicBoolean(true);


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
        //openChat();    //TODO for chat Cli
        if (getPlayerTurnNumber() != 1)
            System.out.println("Wait for your turn...");

    }


    private void openChat() {
        Runnable runnableChat = () -> {

            String chat;
            do {

                while (!chatBlock.get())
                    ;

                chat=chatScanner.nextLine();
                isQuit(chat);
                if (chat.contains("chat:")) {
                    gameController.sendChatMessage(chat.replace("chat:",""));
                }
            } while (true);

        };
        Thread chatThread = new Thread(runnableChat);
        chatThread.start();
    }



    @Override
    public void showError(String error) {

        Runnable runnable = () ->{

            System.err.println(error);

        };


        Thread showErrorThread = new Thread(runnable);
        showErrorThread.start();

    }


    @Override
    public void showMessage(String message) {
        Runnable runnable = () ->{


            System.out.println(message);

        };


        Thread showMessageThread = new Thread(runnable);
        showMessageThread.start();
    }

    @Override
    public void changeStage() {

    }

    @Override
    public void setGameController(GameController gameController) {



    }


    @Override
    public void notifyTimer(Integer duration, String comment) {


        Runnable runnableChat = () -> {

            if (comment.contains(gameController.getOwnNickname())){


                chatBlock.set(false);
                printGameInfo();
                System.out.println("It's your turn,you have "+ duration + " seconds.");
                if (isFirstTurn) {
                    firstTurnSet();
                    isFirstTurn = false;
                }
                selectAction();

            }

        };
        Thread notifyTimer = new Thread(runnableChat);
        notifyTimer.start();

    }

    private void firstTurnSet() {


        ArrayList<Integer> powerupList;

        isFirstTurn = false;
        powerupList = gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards();
        System.out.println("These are two Powerup cards.");
        printSrcFile("Powerup"+powerupList.get(0)+".txt");
        printSrcFile("Powerup"+powerupList.get(1)+".txt");
        System.out.println("You can choose one for put your figure on the spawnpoint with that color.");
        System.out.println("Witch you like? 1 or 2?");


        gameController.selectPowerUp(powerupList.get(readANumber(1,2)-1));

    }



    private void selectAction() {

        System.out.println("Witch action you want to do? \n1.RUN AROUND \n2.GRAB STUFF \n3.SHOOT PEOPLE \n4.END TURN ");
        switch (readANumber(1,4)){
            case 1:
                    gameController.run();
                    break;
            case 2:
                    gameController.grab();
                    break;
            case 3:
                    gameController.shoot();
                    break;
            case 4:
                    gameController.endTurn();
                    chatBlock.set(true);
                    break;
        }

    }



    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {

        Runnable runnable = () ->
                System.out.println(ansi().eraseScreen().fgDefault().
                        a("                                                     ||CHAT >>> ").
                        eraseScreen().bold().fg(transferColorToAnsiColor(senderColor)).
                        a(nickname + ": " + message).eraseScreen().fgDefault());

        Thread newChatThread = new Thread(runnable);
        newChatThread.start();

    }



    @Override
    public void showValidSquares(ArrayList<Integer> validSquares) {

        Runnable runnable = () ->{

            System.out.println("You can go to these Square. Witch you want to go???");
            for (int i=0;i<validSquares.size();i++)
                System.out.print(validSquares.get(i)+" ");

            System.out.println(" ");
            System.out.println("Go to:");
            gameController.selectSquare(readANumber(1,12));

        };


        Thread showValidSquareThread = new Thread(runnable);
        showValidSquareThread.start();

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {



    }




}
