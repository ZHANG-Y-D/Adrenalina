package adrenaline.client.view;


import adrenaline.client.controller.GameController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class ClientCli implements ViewInterface{


    private GameController controller;
    private String returnValueFromServer ="null";


    public ClientCli() {
        controller = new GameController();
        controller.setViewController(this);

    }


    public void InitialClientCli(){

        int connectingType = 0; //1 for socket ,2 for rmi,0 for error

        printIniTxtFile();
        System.out.println("\nDo you want to use Socket or Rmi (Remote Method Invocation)?");
        while (connectingType == 0)
            connectingType = chooseConnectingType();

        while (!connectingToServer(connectingType))
            System.out.println("Please reinsert");

        setNickname();
        while (!ListenerReturnValueIsOK())
            setNickname();
    }

    private boolean ListenerReturnValueIsOK() {


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


    private void printIniTxtFile(){

        try(FileReader fileReader = new FileReader("src/main/resources/ForCli/CliBegin.txt")) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String string = bufferedReader.readLine();
            while (string != null){
                System.out.println(string);
                string=bufferedReader.readLine();
            }
        }catch (FileNotFoundException e){
            System.out.println("\nsrc/main/resources/ForCli/CliBegin.txt  File Not Found ");
        }catch (IOException e){
            System.out.println("\n IOException ");
        }

    }


    private int chooseConnectingType(){

        Scanner scanner = new Scanner(System.in);

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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert host ip");
        host = scanner.nextLine();
        System.out.println("Insert port");
        try {
            port = scanner.nextInt();
        }catch (InputMismatchException e){
            System.out.println("You have to insert a number for port");
            return false;
        }

        if (connectingType == 1)
            return controller.connectSocket(host, port);
        else
            return controller.connectRMI(host, port);

    }


    private void setNickname(){

        Scanner scanner = new Scanner(System.in);


        System.out.println("Please insert your Nickname");
        String input = scanner.nextLine();

        controller.setNickname(input);

    }



}
