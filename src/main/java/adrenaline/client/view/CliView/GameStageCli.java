package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.model.Player;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import static org.fusesource.jansi.Ansi.ansi;


/**
 *
 *
 *
 */
public  class GameStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {


    private Scanner chatScanner = new Scanner(System.in);
    private Boolean isFirstTurn = true;
    private Lock chatLock = new ReentrantLock();
    private AtomicBoolean isInTurn = new AtomicBoolean(false);
    private Thread turnControllerThread;
    private final Object subActionLock = new Object();
    private AtomicBoolean isShowedSquare = new AtomicBoolean(false);
    private volatile ArrayList<Integer> validSquareArray;
    private AtomicBoolean isCanNotGoBack = new AtomicBoolean(false);


    /**
     *
     *
     *
     */
    public GameStageCli(GameController gameController) {

        this.gameController = gameController;
        gameController.setViewController(this);
        gameController.addPropertyChangeListener(this);
        initialStageCli();

    }




    /**
     *
     * For initial set of current stage
     *
     */
    @Override
    protected void initialStageCli() {

        printSrcFile("GameStareTitle.txt");
        openChat();
        if (getPlayerTurnNumber() != 1)
            System.out.println("Wait for your turn...");
        else
            notifyTimer(90,gameController.getOwnNickname());
    }


    //TODO respwan

    /**
     *
     *
     *
     */
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



    /**
     *
     *
     *
     */
    @Override
    public void showError(String error) {

        Runnable runnable = () -> {



            if (error.contains("You have run out of moves")) {
                finishActionAndWakeupWaitThread();
            }
            else if (error.contains("You can only do that during your turn"))
                finishThisTurn();
            else if (error.contains("You can't go back now")) {
                isCanNotGoBack.set(true);
                return;
            }

            printAString("err",error);

        };

        Thread showErrorThread = new Thread(runnable);
        showErrorThread.start();

    }


    /**
     *
     *
     *
     */
    @Override
    public void showMessage(String message) {

        Runnable runnable = () -> {

            printAString(message);

        };

        Thread showMessageThread = new Thread(runnable);
        showMessageThread.start();
    }


    /**
     *
     *
     *
     */
    private void reloadWeaponAndEndTurn() {

        int weaponID;

        Player playerSelf = gameController.getPlayersMap().get(gameController.getOwnColor());

        gameController.endTurn();

        while (isInTurn.get()){

            printPlayerOwnPowerupAndWeaponInfo();

            System.out.println("Select actions is finished.");
            System.out.println("Choose a weapon card for reload, input -1 to pass and finish this turn:");

            weaponID = readANumber(playerSelf.getWeaponCards());
            if (weaponID==-1) {
                gameController.endTurn();
                finishThisTurn();
                return;
            }

            payWithPowerup();


            gameController.selectWeapon(weaponID);

            try {
                sleep(2000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }



    private void payWithPowerup() {

        Player playerSelf = gameController.getPlayersMap().get(gameController.getOwnColor());

        System.out.println("You want pay ammo with your powerup cards? Input yes to do:");
        if (readAString().equalsIgnoreCase("y")
                || readAString().equalsIgnoreCase("yes")){

            System.out.println("How much powerups do you want to use?You have "
                    +playerSelf.getPowerupCards().size()+" powerup cards");
            int powers = readANumber(1,3);
            for (;powers>0 && playerSelf.getPowerupCards().size()>=powers;powers--){
                System.out.println("Input number of powerup:");
                gameController.selectPowerUp(readANumber(playerSelf.getPowerupCards()));
            }
        }

    }


    /**
     *
     *
     *
     */
    private void finishThisTurn() {

        isInTurn.set(false);

        System.out.println("Your turn is finished");
        try {
            if (turnControllerThread.isAlive())
                turnControllerThread.interrupt();
        }catch (NullPointerException ignored){

        }
    }


    /**
     *
     *
     *
     */
    @Override
    public void changeStage() {


    }


    /**
     *
     *
     *
     */
    @Override
    public void setGameController(GameController gameController) {
        //Not server for GameStageCli
    }



    /**
     *
     *
     *
     */
    @Override
    public void notifyTimer(Integer duration, String comment) {


        Runnable turnCentralRunnable = () -> {

            System.out.println(comment);
            if (comment.contains(gameController.getOwnNickname()) && !isInTurn.get()) {

                isInTurn.set(true);

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

    /**
     *
     *
     *
     */
    private void startTurnControllerThread() {


        Runnable turnControllerRunable = ()->{




            if (isFirstTurn) {
                firstTurnSet();
                isFirstTurn = false;
            }

            while (isInTurn.get()){
                selectAction();
            }

        };

        turnControllerThread = new Thread(turnControllerRunable);
        turnControllerThread.start();
    }





    /**
     *
     *
     *
     */
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


    /**
     *
     *
     *
     */
    private void selectAction() {



        printSrcFile("SelectAction.txt");
        System.out.println("To do: ");
        int num = readANumber(1, 9);
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
                UsePowerupCard();
                break;
            case 6:
                sendChat();
                break;
            case 7:
                printPlayerOwnPowerupAndWeaponInfo();
                break;
            case 8:
                printPlayerStateInfo();
                break;
            case 9:
                printMapAndMapWeaponAmmoInfo();
                break;
            default:
                System.out.println("Invalid Action");
        }

    }

    private void UsePowerupCard() {

        ArrayList<Integer> powerup=gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards();

        printPowerupInfo(powerup);
        System.out.println("These are your power up card. Witch you want to use?");
        gameController.selectPowerUp(readANumber(powerup));

        try {
            sleep(1000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

        while (isInTurn.get()){

            System.out.println("Select your action.\n1.Select target \n2.Select Square \n3.Finish use powerup");
            int num = readANumber(1,3);

            if (num == 1) {
                selectSomePlayers();
            } else if (num == 2) {
                gameController.selectSquare(readANumber(0, 11));
            } else if (num == 3)
                return;

            try {
                sleep(1000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

        }

    }


    private void sendChat() {

        System.out.print("Input your message: ");
        gameController.sendChatMessage(readAString());

    }


    /**
     *
     *
     *
     */
    private void shootAction() {

        gameController.shoot();
        try {
            sleep(3000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }


        while (isInTurn.get()) {
            printSrcFile("ShootAction.txt");
            System.out.println("To do: ");
            int num = readANumber(1, 11);
            switch (num) {
                case 1:
                    selectWeapon();
                    break;
                case 2:
                    selectFiremode();
                    break;
                case 3:
                    selectSquare();
                    break;
                case 4:
                    selectSomePlayers();
                    break;
                case 5:
                    SelectPowerupCard();
                    break;
                case 6:
                    if (shootGoBackToSelectAction())
                        return;
                    break;
                case 7:
                    finishShootAction();
                    return;
                case 8:
                    sendChat();
                    break;
                case 9:
                    printPlayerOwnPowerupAndWeaponInfo();
                    break;
                case 10:
                    printPlayerStateInfo();
                    break;
                case 11:
                    printMapAndMapWeaponAmmoInfo();
                    break;
                default:
                    System.out.println("Invalid Action");
            }
        }
    }

    private boolean shootGoBackToSelectAction() {

        isCanNotGoBack.set(false);
        gameController.back();
        try {
            sleep(2000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return isCanNotGoBack.get();

    }

    private void finishShootAction() {

        isCanNotGoBack.set(false);
        while (!isCanNotGoBack.get()){
            gameController.back();
            try {
                sleep(2000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

    }


    private void selectSomePlayers() {

        ArrayList<Color> arrayListColor = new ArrayList<>();

        System.out.println("Those are players in lobby.");
        printPlayerStateInfo();
        System.out.println("How many players you want select?");
        int num = readANumber(1,gameController.getPlayersMap().size());
        printAString("ansi","Players' color: @|bold,yellow [1.Yellow] |@ " +
                "@|bold,blue [2.Blue] |@" +
                "@|bold,white [3.Gray] |@" +
                "@|bold,magenta [4.Purple] |@" +
                "@|bold,green [5.Green] |@");

        for (;num>0;num--){

            System.out.println("Input color number of Players:");
            int colorNum = readANumber(1,5);
            Color color;
            switch (colorNum){
                case 1:
                    color=Color.YELLOW;
                    break;
                case 2:
                    color=Color.BLUE;
                    break;
                case 3:
                    color=Color.GRAY;
                    break;
                case 4:
                    color=Color.PURPLE;
                    break;
                case 5:
                    color=Color.GREEN;
                    break;
                    default:
                        color=Color.WHITE;
                        break;
            }
            arrayListColor.add(color);
        }
        gameController.selectPlayers(arrayListColor);
    }

    private void selectSquare() {


        System.out.println("Select a square:");
        gameController.selectSquare(readANumber(0,11));

    }

    private void selectFiremode() {

        System.out.println("Select a fire mode: ");
        gameController.selectFiremode(readANumber(0,2));

    }

    private void selectWeapon() {

        ArrayList<Integer> ownWeaponList =
                gameController.getPlayersMap().get(gameController.getOwnColor()).getWeaponCards();

        System.out.println("These are the weapons you own: ");
        printWeaponInfo(ownWeaponList);

        System.out.println("Select one : ");
        gameController.selectWeapon(readANumber(ownWeaponList));

    }


    /**
     *
     *
     *
     */
    private void SelectPowerupCard() {


        int selected;
        ArrayList<Integer> powerupList;

        System.out.println("Here is your powerup cards.");

        powerupList = gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards();

        printPowerupInfo(powerupList);

        System.out.println("Which you want to select? If you don't want to select now,input -1");

        selected = readANumber(powerupList);
        if (selected == -1)
            return;
        gameController.selectPowerUp(selected);

    }


    /**
     *
     *
     *
     */
    private void runAction() {

        isShowedSquare.set(false);
        gameController.run();

        waitProcessCompleted();

        System.out.println(" ");
        System.out.println("Choose one: Input -1 to go back.");

        int num = readANumber(validSquareArray);

        if (num==-1)
            gameController.back();
        else
            gameController.selectSquare(num);


    }

    private void waitProcessCompleted() {

        while (!isShowedSquare.get()) {
            synchronized (subActionLock) {
                try {
                    subActionLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

    }


    /**
     *
     *
     *
     */
    private void grabAction() {

        Color grabbedColor;
        ArrayList<Integer> weaponList;

        isShowedSquare.set(false);
        gameController.grab();

        waitProcessCompleted();

        System.out.println(" ");
        System.out.println("Choose one: Input -1 to go back.");

        int num = readANumber(validSquareArray);

        if (num==-1)
            gameController.back();
        else
            gameController.selectSquare(num);


        if (num==2 || num==4 || num==11){

            System.out.println("You can choose a weapon.");


            switch (num){
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



    /**
     *
     *
     *
     */
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



    /**
     *
     *
     *
     */
    @Override
    public void showValidSquares(ArrayList<Integer> validSquares) {


        Runnable runnable = () ->{

            printMap();
            System.out.println("You can go to these Squares:");

            validSquareArray=validSquares;

            try {
                sleep(250);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

            synchronized (this) {
                for (Integer validSquare : validSquares)
                    printAString("OutWithOutNewLine", validSquare + " ");
            }

            finishActionAndWakeupWaitThread();
        };
        Thread showValidSquareThread = new Thread(runnable);
        showValidSquareThread.start();

    }

    private void finishActionAndWakeupWaitThread() {

        synchronized (subActionLock) {
            isShowedSquare.set(true);
            subActionLock.notifyAll();
        }

    }


    /**
     *
     *
     *
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        


    }

}
