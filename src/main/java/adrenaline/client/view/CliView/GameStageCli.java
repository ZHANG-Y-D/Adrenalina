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
 * Game flow stage for cli
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
    private AtomicBoolean isOutOfMoves = new AtomicBoolean(false);



    /**
     *
     *
     * Constructor for game stage cli
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




    /**
     *
     * For open chat when out of turn
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
                }else if (chat.contains("usepowerup"))
                    usePowerupCard();


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
     * Show error from server
     *
     */
    @Override
    public void showError(String error) {

        Runnable runnable = () -> {



            if (error.contains("You have run out of moves")) {
                finishActionAndWakeupWaitThread();
                isOutOfMoves.set(true);
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
     * Show message from server
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
     * For reload and end turn
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
                sleep(500);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }


    /**
     *
     * For pay ammo whit powerup card
     *
     */
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
     * To finish this turn
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
     * For first turn set
     *
     */
    private void firstTurnSet() {

        isFirstTurn = false;
        selectPowerupCard();

    }

    /**
     *
     * Start turn from server timer
     * @param duration Time duration
     * @param comment Timer comment
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
     * For start turn controller thread
     *
     */
    private void startTurnControllerThread() {


        Runnable turnControllerRunable = ()->{

            if (isFirstTurn) {
                firstTurnSet();
                isFirstTurn = false;
            }

            while (isInTurn.get() && gameController.getFinalFrenzyMode()==0){
                selectAction();
            }

            while (isInTurn.get() && gameController.getFinalFrenzyMode()!=0){

                if (gameController.getFinalFrenzyMode()==1){

                    selectFinalFrenzyMode1();

                }else if (gameController.getFinalFrenzyMode()==2){

                    selectFinalFrenzyMode2();
                }


            }

        };

        turnControllerThread = new Thread(turnControllerRunable);
        turnControllerThread.start();
    }

    private void selectFinalFrenzyMode1() {

        printSrcFile("FinalFrenzyMode1Action.txt");
        System.out.println("To do: ");
        int num = readANumber(1, 9);
        switch (num) {
            case 1:
                shootAction();
                break;
            case 2:
                runAction();
                break;
            case 3:
                grabAction();
                break;
            case 4:
                reloadWeaponAndEndTurn();
                break;
            case 5:
                usePowerupCard();
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

    private void selectFinalFrenzyMode2() {

        printSrcFile("FinalFrenzyMode2Action.txt");
        System.out.println("To do: ");
        int num = readANumber(1, 8);
        switch (num) {
            case 1:
                shootAction();
                break;
            case 2:
                grabAction();
                break;
            case 3:
                reloadWeaponAndEndTurn();
                break;
            case 4:
                usePowerupCard();
                break;
            case 5:
                sendChat();
                break;
            case 6:
                printPlayerOwnPowerupAndWeaponInfo();
                break;
            case 7:
                printPlayerStateInfo();
                break;
            case 8:
                printMapAndMapWeaponAmmoInfo();
                break;
            default:
                System.out.println("Invalid Action");
        }
    }



    /**
     *
     * For select action
     *
     */
    private void selectAction() {

        printSrcFile("SelectAction.txt");
        System.out.println("To do: ");
        int num = readANumber(1, 10);
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
            case 10:
                selectPowerupCard();
                break;
            default:
                System.out.println("Invalid Action");
        }

    }




    /**
     *
     *
     * For use powerup card
     *
     */
    private void usePowerupCard() {

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
                selectSquare();
            } else if (num == 3)
                return;

            try {
                sleep(500);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

        }

    }





    /**
     *
     * For shoot action
     *
     */
    private void shootAction() {

        if (gameController.getFinalFrenzyMode()==0)
            gameController.shoot();
        else
            gameController.selectFinalFrenzyAction(0);

        try {
            sleep(500);
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
                    selectPowerupCard();
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


    /**
     *
     * For judge go back action from server
     * @return turn for already at select action state
     */
    private boolean shootGoBackToSelectAction() {

        isCanNotGoBack.set(false);
        gameController.back();
        try {
            sleep(1500);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return isCanNotGoBack.get();

    }

    /**
     *
     * For finish shoot action
     *
     */
    private void finishShootAction() {

        isCanNotGoBack.set(false);
        while (!isCanNotGoBack.get()){
            gameController.back();
            try {
                sleep(1500);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

    }

    /**
     *
     * For select players
     *
     */
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

    /**
     *
     * For select square
     *
     */
    private void selectSquare() {


        System.out.println("Select a square:");
        gameController.selectSquare(readANumber(0,11));

    }


    /**
     *
     * For select firemode
     *
     */
    private void selectFiremode() {

        System.out.println("Select a fire mode: ");
        gameController.selectFiremode(readANumber(0,2));

    }

    /**
     *
     * For select weapon
     *
     */
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
     * For select powerup card
     *
     */
    private void selectPowerupCard() {


        int selected;
        ArrayList<Integer> powerupList;

        System.out.println("Here is your powerup cards.");

        powerupList = gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards();

        printPowerupInfo(powerupList);

        System.out.println("Which you want to select? (Tips:If the time is ok, you can input -1 to go back)");

        selected = readANumber(powerupList);
        if (selected == -1)
            return;
        gameController.selectPowerUp(selected);

    }


    /**
     *
     * For run action
     *
     */
    private void runAction() {

        isOutOfMoves.set(false);
        isShowedSquare.set(false);

        if (gameController.getFinalFrenzyMode()==0)
            gameController.run();
        else if (gameController.getFinalFrenzyMode()==1)
            gameController.selectFinalFrenzyAction(1);

        waitProcessCompleted();


        if (!isOutOfMoves.get()) {
            System.out.println(" ");
            System.out.println("Choose one to run: Input -1 to go back.");

            int num = readANumber(validSquareArray);

            if (num == -1)
                gameController.back();
            else
                gameController.selectSquare(num);
        }
    }


    /**
     *
     * For wait process complete
     *
     */
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
     * For grab action
     *
     */
    private void grabAction() {

        Color grabbedColor;
        ArrayList<Integer> weaponList;

        isOutOfMoves.set(false);
        isShowedSquare.set(false);
        if (gameController.getFinalFrenzyMode()==0)
            gameController.grab();
        else if (gameController.getFinalFrenzyMode()==1)
            gameController.selectFinalFrenzyAction(2);
        else if (gameController.getFinalFrenzyMode()==2)
            gameController.selectFinalFrenzyAction(1);

        waitProcessCompleted();

        if (!isOutOfMoves.get()) {
            System.out.println(" ");
            System.out.println("Choose one to grab: Input -1 to go back.");

            int num = readANumber(validSquareArray);

            if (num == -1)
                gameController.back();
            else
                gameController.selectSquare(num);


            if (num == 2 || num == 4 || num == 11) {

                System.out.println("You can choose a weapon.");


                switch (num) {
                    case 2:
                        grabbedColor = Color.BLUE;
                        break;
                    case 4:
                        grabbedColor = Color.RED;
                        break;
                    case 11:
                        grabbedColor = Color.YELLOW;
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
    }





    /**
     *
     * Received valid square from server
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


    /**
     *
     * To weak up wait thread
     *
     */
    private void finishActionAndWakeupWaitThread() {

        synchronized (subActionLock) {
            isShowedSquare.set(true);
            subActionLock.notifyAll();
        }

    }

    /**
     *
     * For send chat
     *
     */
    private void sendChat() {

        System.out.print("Input your message: ");
        gameController.sendChatMessage(readAString());

    }

    /**
     *
     * Received chat message from server
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
     * Not valid at this stage
     *
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        //Not valid at this stage

    }

    /**
     *
     * Not valid at this stage
     *
     */
    @Override
    public void changeStage() {
        //Not valid at this stage
    }


    /**
     *
     * Not server for GameStageCli
     *
     */
    @Override
    public void setGameController(GameController gameController) {
        //Not server for GameStageCli
    }

}
