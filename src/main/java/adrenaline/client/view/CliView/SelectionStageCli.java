package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class SelectionStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {


    private AtomicInteger turnNumber = new AtomicInteger(0);
    Thread mainThread;


    public SelectionStageCli(GameController gameController) {

        this.gameController = gameController;
        gameController.setViewController(this);
        gameController.addPropertyChangeListener(this);
        returnValueIsOk.set(0);
        initialStageCli();

    }



    @Override
    protected void initialStageCli() {

        turnNumber.set(getPlayerTurnNumber());
        printPlayerInfo();
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

            printPlayerInfo();

            selectMapAndSkulls();

            System.out.println("Wait for Game Start...");
        };
        mainThread = new Thread(runnable);
        mainThread.start();

    }

    private void selectMapAndSkulls() {

        int mapNum;
        int skulls;


        printSrcFile("MapSelection.txt");
        System.out.println("Which map do you want to play?");

        mapNum = readANumber();

        System.out.println("How many skulls you want to play? From 5 to 8");

        skulls = readANumber();

        if (mapNum < 1 || mapNum > 4 || skulls < 5 || skulls > 8){

            System.err.println("Wrong range！！！ Map from 1 to 4, skull from 5 to 8!");
            System.err.println("Please retry.\n");
            selectMapAndSkulls();

        }

        gameController.sendSettings(mapNum,skulls);

    }



    private void selectAvatar() {

        int num;
        Color color = Color.BLACK;

        System.out.println("Which you like? From 1 to 5: ");
        num = readANumber();


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


    @Override
    public void showError(String error) {

        Runnable runnable = () -> {

            if (error.equals("/OK") || error.equals("KO"))
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


    @Override
    public void setGameController(GameController gameController) {

    }


    public void notifyTimer(Integer duration, String comment) {
        //operation not supported at this stage
    }

    public void newChatMessage(String nickname, Color senderColor, String message) {
        //operation not supported at this stage
    }

    public void showValidSquares(ArrayList<Integer> validSquares) {
        //operation not supported at this stage
    }



    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {

        Runnable runnable = () -> {

            if(evt.getPropertyName().equals("map"))
                changeStage();
            else if (evt.getPropertyName().equals("nicknamesColor"))
                turnNumber.getAndDecrement();
        };

        Thread changeThread = new Thread(runnable);
        changeThread.start();

    }
}
