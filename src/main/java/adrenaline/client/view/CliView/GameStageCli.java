package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;
import org.fusesource.jansi.Ansi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import static org.fusesource.jansi.Ansi.ansi;

public  class GameStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {


    private Scanner chatScanner = new Scanner(System.in);
    private Boolean isFirstTurn = true;
    private Lock chatLock = new ReentrantLock();
    private AtomicBoolean isInTurn = new AtomicBoolean(false);
    private Thread turnControllerThread;
    private Thread timeThread;
    private final Object actionLock = new Object();
    private final Object subActionLock = new Object();
    private AtomicInteger isSelectedSquare = new AtomicInteger(-1);


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
        else
            notifyTimer(60,gameController.getOwnNickname());

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

        Runnable runnable = () -> printAString("err",error);

        Thread showErrorThread = new Thread(runnable);
        showErrorThread.start();

    }


    @Override
    public void showMessage(String message) {

        Runnable runnable = () -> {

            //TODO for Powerup
            printAString("out",message);

        };

        Thread showMessageThread = new Thread(runnable);
        showMessageThread.start();
    }

    private void reloadWeaponAndEndTurn() {

        int weaponID;

        gameController.endTurn();

        while (isInTurn.get()){


            printPlayerSelfInfo();

            System.out.println("Choose a weapon card, input 0 to pass and finish this turn:");

            weaponID = readANumber(0,24);
            if (weaponID==0) {
                gameController.endTurn();
                finishThisTurn();
                return;
            }

            System.out.println("You want pay ammo with your powerup cards? Input yes to do:");
            if (readAString().equalsIgnoreCase("y")
                    || readAString().equalsIgnoreCase("yes")){
                System.out.println("How much powerups do you want to use?");
                int powers = readANumber(1,3);
                for (;powers>0;powers--){
                    System.out.println("Input number of powerup:");
                    gameController.selectPowerUp(readANumber(1,24));
                }
            }

            gameController.selectWeapon(weaponID);

            try {
                sleep(2000);
            }catch (InterruptedException ignored){
                Thread.currentThread().interrupt();

            }
        }
    }




    private void finishThisTurn() {

        isInTurn.set(false);

        System.out.println("Your turn is finished");
        try {
            if (timeThread.isAlive())
                timeThread.interrupt();
            if (turnControllerThread.isAlive())
                turnControllerThread.interrupt();
        }catch (NullPointerException ignored){

        }

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


                    int actionNum = selectAction();
                    if (actionNum == 1 || actionNum == 2 || actionNum == 3)
                        actionTimes--;


                    synchronized (actionLock){
                        try {
                            //TODO not wait
                            actionLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                    }


                }

                if (isInTurn.get())
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



            if (comment.contains(gameController.getOwnNickname()) && !isInTurn.get()) {

                isInTurn.set(true);
                timeThread = new Thread(timeRunnable);
                timeThread.start();


                System.out.println("Your turn is arrived,you have " + duration + " seconds,input anything to start!");


                chatLock.lock();

                if (!isInTurn.get()){
                    chatLock.unlock();
                    return;
                }

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



    private int selectAction() {


            System.out.println("Witch action you want to do? " +
                    "\n1.RUN AROUND \n2.GRAB STUFF \n3.SHOOT PEOPLE \n4.END TURN \n5.Use Powerup Cards");
            int num = readANumber(1, 5);
            switch (num) {
                    case 1:
                        runAction();
                        break;
                    case 2:
                        grabAction();
                        break;
                    case 3:
                        gameController.shoot();
                        //TODO for  shoot
                        break;
                    case 4:
                        reloadWeaponAndEndTurn();
                        break;
                    case 5:
                        usePowerupCard();
                        break;
                    default:
                        break;
            }

            return num;
    }

    private void usePowerupCard() {

        //TODO for complete powerup
        int selected;
        ArrayList<Integer> powerupList;

        System.out.println("Here is your powerup cards.");

        powerupList = gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards();
        for (int powerup:powerupList) {
            printPowerupInfo(powerup);
        }
        System.out.println("Whitch you want to use? If you don't want to use,input 0");
        while (true){
            selected = readANumber(1,24);
            if (selected == 0)
                return;
            if (powerupList.contains(selected)){
                gameController.selectPowerUp(selected);
                break;
            }
            else {
                System.out.println("You don't have this powerup card,retry:");
            }
        }
    }

    private void runAction() {

        isSelectedSquare.set(-1);
        gameController.run();


        while (isSelectedSquare.get()==-1) {
            synchronized (subActionLock) {
                try {
                    subActionLock.wait();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }


        synchronized (actionLock) {
            actionLock.notifyAll();
        }


    }

    private void grabAction() {

        isSelectedSquare.set(-1);
        gameController.grab();

        while (isSelectedSquare.get()==-1) {
            synchronized (subActionLock) {
                try {
                    subActionLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        }

        if (isSelectedSquare.get()==2 ||
                isSelectedSquare.get()==4 ||
                    isSelectedSquare.get()==11){

            System.out.println("You can choose a weapon.");


            switch (isSelectedSquare.get()){
                case 2:
                    for (int num:gameController.getMap().getWeaponMap().get(Color.BLUE)) {
                        printWeaponInfo(num);
                    }
                    break;
                case 4:
                    for (int num:gameController.getMap().getWeaponMap().get(Color.RED)) {
                        printWeaponInfo(num);
                    }
                    break;
                case 11:
                    for (int num:gameController.getMap().getWeaponMap().get(Color.YELLOW)) {
                        printWeaponInfo(num);
                    }
                    break;

                    default:
                        break;
            }
            System.out.println("Witch you want: ");
            gameController.selectWeapon(readANumber(1,21));


        }

        synchronized (actionLock) {
            actionLock.notifyAll();
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

            //TODO print map

            System.out.println("You can go to these Squares:");

            try {
                sleep(500);
            }catch (InterruptedException ignored){
                Thread.currentThread().interrupt();
            }

            synchronized (this) {
                for (Integer validSquare : validSquares)
                    printAString("OutWithOutNewLine", validSquare + " ");
            }


            System.out.println(" ");

                do {

                    int num = readANumber(0,11);
                    if (!validSquares.contains(num)) {
                        System.err.println("You can't go to here,retry: ");
                    }else {
                        gameController.selectSquare(num);
                        synchronized (subActionLock) {
                            isSelectedSquare.set(num);
                            subActionLock.notifyAll();
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
