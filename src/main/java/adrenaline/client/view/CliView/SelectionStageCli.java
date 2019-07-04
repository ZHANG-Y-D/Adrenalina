package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * This class for select Avatar
 *
 */
public class SelectionStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {


    private AtomicInteger turnNumber = new AtomicInteger(0);
    private Thread mainThread;
    private AtomicInteger numPlayerSelectedAvatar = new AtomicInteger(0);


    /**
     *
     * Constructor of this stage
     *
     */
    public SelectionStageCli(GameController gameController) {

        this.gameController = gameController;
        gameController.setViewController(this);
        gameController.addPropertyChangeListener(this);
        returnValueIsOk.set(0);
        initialStageCli();
        numPlayerSelectedAvatar.set(gameController.getPlayersNicknames().size());

    }


    /**
     *
     * For initial set of current stage
     *
     */
    @Override
    protected void initialStageCli() {

        turnNumber.set(getPlayerTurnNumber());
        printLobbyInfo();
        printSrcFile("Avatar.txt");
        if (turnNumber.get()!=1)
            System.out.println("Wait for your turn...");


        Runnable runnable = () -> {
            while (true) {
                if (turnNumber.get()==1) {
                    do {
                        returnValueIsOk.set(0);
                        selectAvatar();
                    } while (!listenerReturnValueIsOK());
                    break;
                }
            }


            System.out.println("Wait for other players select avatar...");
            while (numPlayerSelectedAvatar.get()!=0)
                ;

            printLobbyInfo();
            selectMapAndSkulls();

            System.out.println("Wait for Game Start...");
        };
        mainThread = new Thread(runnable);
        mainThread.start();

    }


    /**
     *
     * For select map and skull
     *
     */
    private void selectMapAndSkulls() {

        int mapNum;
        int skulls;


        printSrcFile("MapSelection.txt");
        System.out.println("Which map do you want to play?");

        mapNum = readANumber(1,4);

        System.out.println("How many skulls you want to play? From 5 to 8");

        skulls = readANumber(5,8);


        gameController.sendSettings(mapNum,skulls);

    }


    /**
     *
     * For select avatar
     *
     */
    private void selectAvatar() {

        int num;
        Color color = Color.BLACK;

        System.out.println("Which you like? From 1 to 5: ");
        num = readANumber(1,5);


        switch (num){
            case 1:
                color = Color.YELLOW;
                break;
            case 2:
                color = Color.BLUE;
                break;
            case 3:
                color = Color.GRAY;
                break;
            case 4:
                color = Color.PURPLE;
                break;
            case 5:
                color = Color.GREEN;
                break;
            default:
                System.err.println("Please answer with a number from 1 to 5.");
                selectAvatar();
        }

        returnValueIsOk.set(0);
        gameController.selectAvatar(color);

    }


    /**
     *
     * For show error from server
     *
     */
    @Override
    public void showError(String error) {

        Runnable runnable = () -> {

            if (error.equals("KO"))
                returnValueIsOk.set(1);
            else {
                if (!gameController.getPlayersNicknames().get(gameController.getOwnNickname()).equals(Color.WHITE))
                    returnValueIsOk.set(1);
                else {
                    System.err.println(error);
                    returnValueIsOk.set(2);
                }
            }
        };

        Thread changeThread = new Thread(runnable);
        changeThread.start();

    }


    /**
     *
     * For show Massage from server
     *
     */
    @Override
    public void showMessage(String message) {

        returnValueIsOk.set(1);

    }


    /**
     *
     * For change stage
     *
     */
    @Override
    public synchronized void changeStage() {


        Runnable runnable = () -> {

            mainThread.interrupt();
            gameController.removePropertyChangeListener(this);
            new GameStageCli(gameController);

        };

        Thread changeThread = new Thread(runnable);
        changeThread.start();

    }

    /**
     *
     * Not valid at this stage
     *
     */
    @Override
    public void setGameController(GameController gameController) {
        //Not valid at this stage
    }

    /**
     *
     * Not valid at this stage
     *
     */
    public void notifyTimer(Integer duration, String comment) {
        //operation not supported at this stage
    }


    /**
     *
     * Not valid at this stage
     *
     */
    public void newChatMessage(String nickname, Color senderColor, String message) {
        //operation not supported at this stage
    }


    /**
     *
     * Not valid at this stage
     *
     */
    public void showValidSquares(ArrayList<Integer> validSquares) {
        //operation not supported at this stage
    }


    /**
     *
     * For listen property Change from server
     * @param evt Property Change Event
     *
     */
    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {

        Runnable runnable = () -> {

            if(evt.getPropertyName().equals("map"))
                changeStage();
            else if (evt.getPropertyName().equals("nicknamesColor")) {
                turnNumber.getAndDecrement();
                numPlayerSelectedAvatar.decrementAndGet();
            }
        };

        Thread changeThread = new Thread(runnable);
        changeThread.start();

    }
}
