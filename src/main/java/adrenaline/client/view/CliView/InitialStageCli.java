package adrenaline.client.view.CliView;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;

import java.util.Scanner;

public class InitialStageCli extends ControllerCli implements ViewInterface{



    public InitialStageCli() {
        gameController = new GameController();
        gameController.setViewController(this);
        scanner = new Scanner(System.in);
        returnValueFromServer = "null";
        initialStageCli();
    }


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


    private void printGameInfo() {

        System.out.println("\nThe Lobby is OK! Your LobbyID is "+gameController.getConnectionHandler().getMyLobbyID());
        System.out.println("Your ClientID is "+gameController.getConnectionHandler().getClientID());

    }



    @Override
    public void showError(String error) {

        this.returnValueFromServer = error;

        if (!error.equals("/OK"))
            System.err.println(error);

    }


    @Override
    public void changeStage() {

        printGameInfo();
        new SelectionStageCli(gameController);

    }


    @Override
    public void setGameController(GameController gameController) {

    }

    @Override
    public void notifyTimer(Integer duration) {

    }

    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {

    }


    private int chooseConnectingType(){

        String  input;
        input = readAString();

        if (input.equalsIgnoreCase("socket") || input.equalsIgnoreCase("s"))
            return 1;
        else if (input.equalsIgnoreCase("rmi") || input.equalsIgnoreCase("r"))
            return 2;
        else {
            System.out.println("Please input \"Socket\" or \"Rmi\" ");
            return 0;
        }
    }




    private boolean connectingToServer(int connectingType){

        String host;
        int port;

        System.out.println("Insert host ip");
        host = readAString();
        System.out.println("Insert port");
        port = readANumber();

        if (connectingType == 1)
            return gameController.connectSocket(host, port);
        else
            return gameController.connectRMI(host, port);
    }


    private void setNickname(){


        System.out.println("\nPlease insert your Nickname");
        String input = readAString();
        gameController.setNickname(input);

    }


}