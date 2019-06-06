package adrenaline.client.view.CliView;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;
import org.fusesource.jansi.Ansi;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class InitialCli extends ControllerCli implements ViewInterface{



    public InitialCli() {
        gameController = new GameController();
        gameController.setViewController(this);
        scanner = new Scanner(System.in);
    }


    public void initialStageCli(){


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
        listenerReturnValueIsOK();

        printGameInfo();


        do {
            selectAvatar();
        }while (!listenerReturnValueIsOK());


    }


    private void printGameInfo() {

        System.out.println("\nThe Lobby is OK! Your LobbyID is "+gameController.getConnectionHandler().getMyLobbyID());
        System.out.println("Your ClientID is "+gameController.getConnectionHandler().getClientID());

        printPlayerInfo();

    }



    private void printPlayerInfo() {


        System.out.println("\nThese players are in the lobby:");
        for (Map.Entry<String, Color> players : gameController.getPlayersNicknames().entrySet()) {

            Color color = players.getValue();

            switch (color) {
                case YELLOW:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.YELLOW).a(players.getKey()));
                    break;
                case BLUE:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.BLUE).a(players.getKey()));
                    break;
                case PURPLE:
                    System.out.println(ansi().eraseScreen().fgBright(Ansi.Color.MAGENTA).a(players.getKey()));
                    break;
                case GRAY:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.WHITE).a(players.getKey()));
                    break;
                case GREEN:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.GREEN).a(players.getKey()));
                    break;
                default:
                    System.out.println(ansi().eraseScreen().fgDefault().a(players.getKey()));
                    break;
            }
        }
    }


    private void selectAvatar() {

        int num;
        Color color = Color.BLACK;

        printSrcFile("Avatar.txt");

        try{
            num = scanner.nextInt();
            scanner.nextLine();
        }catch (InputMismatchException e){
            showError("\nPlease answer with a number from 1 to 5.");
            iSQuit(scanner.nextLine());
            return;
        }



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
        gameController.selectAvatar(color);

    }


    private boolean listenerReturnValueIsOK() {

        while (true) {
            if (!returnValueFromServer.equals("null"))
                break;
        }

        if (returnValueFromServer.equals("/OK")){
            returnValueFromServer = "null";
            return true;
        }
        else {
            returnValueFromServer = "null";
            return false;
        }

    }



    @Override
    public void showError(String error) {

        this.returnValueFromServer = error;

        if (!error.equals("/OK"))
            System.err.println(error);

    }



    @Override
    public void changeStage() {

        showError("/OK");



    }



    @Override
    public void setGameController(GameController gameController) {

    }


    public void notifyView() {


        printPlayerInfo();

    }



    @Override
    public void notifyTimer(Integer duration) {

        System.out.println("You still have "+duration+" Secondi");

    }

    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {
        //called when server notifies client of a new message in chat
    }


    private int chooseConnectingType(){


        String input = scanner.nextLine();
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
        host = scanner.nextLine();
        System.out.println("Insert port");
        try {
            port = scanner.nextInt();
            scanner.nextLine();
        }catch (InputMismatchException e){
            System.err.println("\nYou have to insert a number for port!");
            scanner.nextLine();
            return false;
        }

        if (connectingType == 1)
            return gameController.connectSocket(host, port);
        else
            return gameController.connectRMI(host, port);
    }


    private void setNickname(){


        System.out.println("\nPlease insert your Nickname");
        String input = scanner.nextLine();
        iSQuit(input);
        gameController.setNickname(input);

    }




}