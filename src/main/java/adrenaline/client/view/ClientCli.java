package adrenaline.client.view;


import adrenaline.Color;
import adrenaline.client.controller.GameController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class ClientCli implements ViewInterface{


    private GameController gameController;
    private volatile String returnValueFromServer ="null";
    private Scanner scanner;


    public ClientCli() {
        gameController = new GameController();
        gameController.setViewController(this);
        scanner = new Scanner(System.in);
    }


    public void InitialClientCli(){

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

        //TODO wait for other players in

        do {
            selectAvatar();
        }while (!listenerReturnValueIsOK());


    }

    private void selectAvatar() {

        int num=0;
        Color color = Color.BLACK;

        printSrcFile("Avatar.txt");
        try{
            num = scanner.nextInt();
        }catch (InputMismatchException e){
            showError("\nPlease answer with a number from 1 to 5.");
            scanner.nextLine();
            return;
        }


        switch (num){
            case 1:color = Color.YELLOW;
                    break;
            case 2:color = Color.BLUE;
                    break;
            case 3:color = Color.GRAY;
                    break;
            case 4:color = Color.PURPLE;
                    break;
            case 5:color = Color.GREEN;
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
            System.out.println(error);

    }



    @Override
    public void changeStage() {

    }



    @Override
    public void setGameController(GameController gameController) {

    }


    public void notifyView() {

    }



    @Override
    public void notifyTimer(Integer duration) {

    }



    private void printSrcFile(String srcFileName){

        try(FileReader fileReader = new FileReader("src/main/resources/ForCli/" + srcFileName)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String string = bufferedReader.readLine();
            while (string != null){
                System.out.println(ansi().eraseScreen().render(string) );
                string=bufferedReader.readLine();
            }
        }catch (FileNotFoundException e){
            System.out.println("\nsrc/main/resources/ForCli/CliBegin.txt  File Not Found ");
        }catch (IOException e){
            System.out.println("\n IOException ");
        }

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


        System.out.println("Please insert your Nickname");
        String input = scanner.nextLine();
        iSQuit(input);
        gameController.setNickname(input);

    }


    private void iSQuit(String input){

        if (input.equals("Quit")){
            System.out.println("Ok, Game Exit, Thank you for your participation!");
            gameController.cleanExit();
            System.exit(0);
        }

    }

}