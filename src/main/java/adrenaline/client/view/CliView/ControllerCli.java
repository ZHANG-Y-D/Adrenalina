package adrenaline.client.view.CliView;


import adrenaline.client.controller.GameController;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public abstract class ControllerCli{

    protected GameController gameController;
    protected volatile String returnValueFromServer ="null";
    protected Scanner scanner;




    protected abstract void initialStageCli();

    protected void printSrcFile(String srcFileName){

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

    protected int readANumber() {

        int num;
        try{
            num = scanner.nextInt();
            scanner.nextLine();
        }catch (InputMismatchException e){
            System.out.println("\nPlease answer with a number");
            isQuit(scanner.nextLine());
            num = readANumber();
        }
        return num;

    }

    protected String readAString(){

        String input = scanner.nextLine();
        isQuit(input);
        return input;

    }


    protected boolean listenerReturnValueIsOK() {


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



    protected void isQuit(String input){

        if (input.equals("Quit")){
            System.out.println("Ok, Game Exit, Thank you for your participation!");
            gameController.cleanExit();
            System.exit(0);
        }

    }
}

