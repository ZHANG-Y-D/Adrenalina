package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
                    Thread.currentThread().interrupt();
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

            System.out.println("Select actions is finished.");
            System.out.println("Choose a weapon card for reload, input 0 to pass and finish this turn:");

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
            }catch (InterruptedException e){
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
        //Not server for GameStageCli
    }


    @Override
    public void notifyTimer(Integer duration, String comment) {


        Runnable turnCentralRunnable = () -> {

            if (comment.contains(gameController.getOwnNickname()) && !isInTurn.get()) {

                isInTurn.set(true);
                startTimer(duration);

                System.out.println("Your turn is arrived,you have " +
                        duration + " seconds,input anything to start!");

                chatLock.lock();

                if (!isInTurn.get()){
                    chatLock.unlock();
                    return;
                }

                startTurnControllerThread();

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


    private void startTurnControllerThread() {


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

            }

            if (isInTurn.get())
                reloadWeaponAndEndTurn();
        };

        turnControllerThread = new Thread(turnControllerRunable);
        turnControllerThread.start();
    }



    private void startTimer(Integer duration) {

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

        timeThread = new Thread(timeRunnable);
        timeThread.start();
    }



    private void firstTurnSet() {


        ArrayList<Integer> powerupList;

        isFirstTurn = false;
        powerupList = gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards();

        System.out.println("These are two Powerup cards.");

        printPowerupInfo(powerupList);

        System.out.println("You can choose one for put your figure on the spawnpoint with that color.");

        System.out.println("Witch you like? ");

        gameController.selectPowerUp(readANumber(powerupList));

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
                        shootAction();
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


    private void shootAction() {

        ArrayList<Integer> ownWeaponList =
                gameController.getPlayersMap().get(gameController.getOwnColor()).getWeaponCards();

        gameController.shoot();

        System.out.println("These are the weapons you own: ");
        printWeaponInfo(ownWeaponList);

        System.out.println("Select one : ");
        gameController.selectWeapon(readANumber(ownWeaponList));

        System.out.println("Select a fire mode: ");
        gameController.selectFiremode(readANumber(0,2));

    }


    private void usePowerupCard() {

        //TODO for complete powerup
        int selected;
        ArrayList<Integer> powerupList;

        System.out.println("Here is your powerup cards.");

        powerupList = gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards();

        printPowerupInfo(powerupList);

        System.out.println("Whitch you want to use? If you don't want to use,input 0");

        selected = readANumber(powerupList);
        if (selected == 0)
            return;
        gameController.selectPowerUp(selected);
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

    }



    private void grabAction() {

        Color grabbedColor;
        ArrayList<Integer> weaponList;

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
                    grabbedColor=Color.BLUE;
                    break;
                case 4:
                    grabbedColor=Color.RED;
                    break;
                case 11:
                    grabbedColor=Color.YELLOW;
                    break;
                    default:
                        return;
            }

            weaponList = gameController.getMap().getWeaponMap().get(grabbedColor);
            printWeaponInfo(weaponList);
            System.out.println("Witch you want: ");
            gameController.selectWeapon(readANumber(weaponList));

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

            printMap();
            System.out.println("You can go to these Squares:");

            try {
                sleep(250);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

            synchronized (this) {
                for (Integer validSquare : validSquares)
                    printAString("OutWithOutNewLine", validSquare + " ");
            }

            System.out.println(" ");

            int num = readANumber(validSquares);

            gameController.selectSquare(num);

            synchronized (subActionLock) {
                isSelectedSquare.set(num);
                subActionLock.notifyAll();
            }

        };


        Thread showValidSquareThread = new Thread(runnable);
        showValidSquareThread.start();

    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        


    }

}
