package adrenaline.client.view.CliView;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Random;

import static java.util.UUID.randomUUID;

/**
 *
 * The first stage of game flow,for connect the internet and set nickname.
 *
 */
public class InitialStageCli extends ControllerCli implements ViewInterface {


    /**
     *
     * The constructor,for initial set of current stage
     *
     */
    public InitialStageCli() {
        gameController = new GameController();
        gameController.setViewController(this);
        returnValueIsOk.set(0);
        initialStageCli();
    }



    /**
     *
     * This method for initial this stage
     *
     */
    @Override
    protected void initialStageCli(){


        int connectingType = 0; //1 for socket ,2 for rmi,0 for error

        printSrcFile("CliBegin.txt");
        System.out.println("\nYou want to use Socket or Rmi (Remote Method Invocation)?");



        while (connectingType == 0)
            connectingType = chooseConnectingType();

        while (!connectingToServer(connectingType))
            System.out.println("Please reinsert");



        do {
            setNickname();
        }while (!listenerReturnValueIsOK());


        System.out.println("\nWait for other player join...");


    }


    /**
     *
     * For print LobbyID and ClientID when th
     *
     */
    private void printGameID() {

        try {
            System.out.println("\nThe Lobby is OK! Your LobbyID is "+gameController.getConnectionHandler().getMyLobbyID());
            System.out.println("Your ClientID is "+gameController.getConnectionHandler().getClientID());
        }catch (NullPointerException e){
            System.err.println("NullPointerException!!!");
        }



    }

    /**
     *
     * For print error message from server
     *
     * @param error The error message
     *
     */
    @Override
    public void showError(String error) {

        Runnable runnable = () -> {

            System.err.println(error);
            returnValueIsOk.set(2);

        };

        Thread changeThread = new Thread(runnable);
        changeThread.start();


    }


    /**
     *
     * For print ok message from server
     *
     * @param message The ok message
     *
     */
    @Override
    public void showMessage(String message) {

        Runnable runnable = () -> {

            if (message.contains("NICKNAME;")) {
                System.out.println(message.replace("NICKNAME;","Your nickname is : "));
                returnValueIsOk.set(1);
            }

        };

        Thread changeThread = new Thread(runnable);
        changeThread.start();

    }

    /**
     *
     * For change stage,the next stage is SelectionStageCli
     *
     */
    @Override
    public void changeStage() {

        Runnable runnable = () -> {

            printGameID();
            new SelectionStageCli(gameController);


        };

        Thread changeThread = new Thread(runnable);
        changeThread.start();

    }


    /**
     *
     * For choose connecting type,socket or rmi.
     *
     * @return 1 for socket, 2 for rmi
     */
    private int chooseConnectingType(){

        String  input;

        input = readAString();

        //for quickly test
        if (input.equals(""))
            return 1;


        if (input.equalsIgnoreCase("socket") || input.equalsIgnoreCase("s"))
            return 1;
        else if (input.equalsIgnoreCase("rmi") || input.equalsIgnoreCase("r"))
            return 2;
        else {
            System.out.println("Please input \"Socket\" or \"Rmi\" ");
            return 0;
        }
    }



    /**
     *
     * For connecting to server.
     *
     * @param connectingType 1 for socket,2 for rmi
     * @return True for successful.
     *
     *
     */
    private boolean connectingToServer(int connectingType){

        String host;
        int port;

        System.out.println("Insert host ip");
        host = readAString();

        //for quickly test
        if (host.equals(""))
            return gameController.connectSocket("127.0.0.1", 1100);


        System.out.println("Insert port");
        port = readANumber(0,999999);



        returnValueIsOk.set(0);
        if (connectingType == 1)
            return gameController.connectSocket(host, port);
        else
            return gameController.connectRMI(host, port);
    }



    /**
     *
     * For set nickname of the player
     *
     */
    private void setNickname(){


        returnValueIsOk.set(0);
        System.out.println("\nPlease insert your Nickname");
        String input = readAString();


        //for quickly test
        if (input.equals("")) {
            gameController.setNickname(new Random().toString());
            return;
        }


        gameController.setNickname(input);

    }

    /**
     *
     *  Not supported for Cli
     *
     */
    @Override
    public void setGameController(GameController gameController) {

    }

    /**
     *
     * Not supported at this stage
     *
     */
    public void notifyTimer(Integer duration, String comment) {
        //operation not supported at this stage
    }


    /**
     *
     * Not supported at this stage
     *
     */
    public void newChatMessage(String nickname, Color senderColor, String message) {
        //operation not supported at this stage
    }


    /**
     *
     * Not supported at this stage
     *
     */
    public void showValidSquares(ArrayList<Integer> validSquares) {
        //operation not supported at this stage
    }

}