package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import static org.fusesource.jansi.Ansi.ansi;

public class GameStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {


    private Scanner chatScanner = new Scanner(System.in);
    private Boolean isFirstTurn = true;
    private Lock chatLock = new ReentrantLock();
    private AtomicBoolean isInTurn = new AtomicBoolean(false);
    private Thread turnControllerThread;
    private Thread timeThread;
    private final Object objectLock = new Object();


    public GameStageCli(GameController gameController) {

        this.gameController = gameController;
        gameController.setViewController(this);
        gameController.addPropertyChangeListener(this);
        initialStageCli();

    }


    @Override
    protected void initialStageCli() {

        printSrcFile("GameStareTitle.txt");
        openChat();
        if (getPlayerTurnNumber() != 1)
            System.out.println("Wait for your turn...");

    }


    @SuppressWarnings("InfiniteLoopStatement")
    private void openChat() {
        Runnable runnableChat = () -> {

            String chat;


            while (true) {

                chatLock.lock();
                chat = chatScanner.nextLine();
                isQuit(chat);
                if (chat.contains("chat:")) {
                    gameController.sendChatMessage(chat.replace("chat:", ""));
                }
                chatLock.unlock();

                try {
                    sleep(3000);
                }catch (InterruptedException ignored){

                }
            }

        };
        Thread chatThread = new Thread(runnableChat);
        chatThread.start();
    }



    @Override
    public void showError(String error) {

        Runnable runnable = () -> System.err.println(error);

        Thread showErrorThread = new Thread(runnable);
        showErrorThread.start();

    }


    @Override
    public void showMessage(String message) {

        Runnable runnable = () -> System.out.println(message);

        Thread showMessageThread = new Thread(runnable);
        showMessageThread.start();
    }

    private void reloadWeaponAndEndTurn() {

        int weaponID;

        gameController.endTurn();

        while (isInTurn.get()){

            printWeaponInfo();

            System.out.println("Choose a weapon card, input 0 to pass and finish this turn:");

            weaponID = readANumber(0,3);

            if (weaponID==0){
                gameController.endTurn();
                finishThisTurn();
                return;
            }else
                gameController.selectWeapon(readANumber(1,3));

            try {
                sleep(3000);
            }catch (InterruptedException ignored){
                Thread.currentThread().interrupt();

            }

        }
    }


    private void finishThisTurn() {

        isInTurn.set(false);

        System.out.println("Your turn is finished");
        if (timeThread.isAlive())
            timeThread.interrupt();
        if (turnControllerThread.isAlive())
            turnControllerThread.interrupt();

    }

    @Override
    public void changeStage() {

    }

    @Override
    public void setGameController(GameController gameController) {



    }


    @Override
    public void notifyTimer(Integer duration, String comment) {



        Runnable turnCentralRunnable = () -> {

            Runnable turnControllerRunable = ()->{

                int actionTimes=2;

                printGameInfo();
                if (isFirstTurn) {
                    firstTurnSet();
                    isFirstTurn = false;
                }
                System.out.println("You can choose actions two times.");

                while (actionTimes>0 && isInTurn.get()){

                    actionTimes--;
                    selectAction();

                    synchronized (objectLock){
                        try {
                            objectLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                }
                reloadWeaponAndEndTurn();
            };

            Runnable timeRunnable = () -> {

                Timer timer = new Timer();

                TimerTask deadline = new TimerTask() {
                    @Override
                    public void run() {
                        finishThisTurn();
                    }
                };
                long delay = (long) duration * 1000;
                timer.schedule(deadline, delay);

            };

            if (comment.contains(gameController.getOwnNickname())) {

                timeThread = new Thread(timeRunnable);
                timeThread.start();

                isInTurn.set(true);
                System.out.println("Your turn is arrived,you have " + duration + " seconds,input anything to start!");

                chatLock.lock();
                turnControllerThread = new Thread(turnControllerRunable);
                turnControllerThread.start();

                while (true){
                    if (!isInTurn.get()){
                        chatLock.unlock();
                        break;
                    }
                }

            }

        };

        Thread turnCentralThread = new Thread(turnCentralRunnable);
        turnCentralThread.start();

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
            gameController.selectPowerUp(powerupList.get(readANumber(1, 2) - 1));



    }



    private void selectAction() {

            System.out.println("Witch action you want to do? \n1.RUN AROUND \n2.GRAB STUFF \n3.SHOOT PEOPLE \n4.END TURN ");
            switch (readANumber(1, 4)) {
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
                        reloadWeaponAndEndTurn();
                        break;
            }


    }


    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {

        Runnable runnable = () ->
                System.out.println(ansi().eraseScreen().fgDefault().
                        a("                                                          ||CHAT >>> ").
                        eraseScreen().bold().fg(transferColorToAnsiColor(senderColor)).
                        a(nickname + ": " + message).eraseScreen().fgDefault());

        Thread newChatThread = new Thread(runnable);
        newChatThread.start();

    }



    @Override
    public void showValidSquares(ArrayList<Integer> validSquares) {


        Runnable runnable = () ->{

            System.out.println("You can go to these Squares:");

            synchronized (this) {
                for (int i = 0; i < validSquares.size(); i++)
                    System.out.print(validSquares.get(i) + " ");
            }

            System.out.println(" ");

                do {

                    int num = readANumber(0,11);
                    if (!validSquares.contains(num)) {
                        System.err.println("You can't go to here,retry: ");
                    }else {
                        gameController.selectSquare(num);
                        synchronized (objectLock) {
                            objectLock.notify();
                        }
                        break;
                    }

                }while (true);

        };


        Thread showValidSquareThread = new Thread(runnable);
        showValidSquareThread.start();

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {




    }




}
