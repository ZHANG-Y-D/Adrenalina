package adrenaline.client.view.CliView;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import org.fusesource.jansi.Ansi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fusesource.jansi.Ansi.ansi;

public abstract class ControllerCli{

    protected GameController gameController;
    protected AtomicInteger returnValueIsOk = new AtomicInteger(0); //0:initial 1:/OK 2:other
    protected Scanner scanner = new Scanner(System.in);

    protected abstract void initialStageCli();


    protected static void printSrcFile(String srcFileName){

        try(FileReader fileReader = new FileReader("src/main/resources/ForCli/" + srcFileName)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String string = bufferedReader.readLine();
            while (string != null){
                System.out.println(ansi().eraseScreen().render(string) );
                string=bufferedReader.readLine();
            }
        }catch (FileNotFoundException e){
            System.out.println("\nsrc/main/resources/ForCli/"+srcFileName+"  File Not Found ");
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



    protected boolean listenerReturnValueIsOK() {


        while (true) {
            if (returnValueIsOk.get()!=0)
                break;
        }


        if (returnValueIsOk.get()==1){
            returnValueIsOk.set(0);
            return true;
        }
        else {
            returnValueIsOk.set(0);
            return false;
        }

    }



    protected void printPlayerInfo() {

        int num=1;

        System.out.println("\nThese players are in the lobby:");
        for (Map.Entry<String, Color> players : gameController.getPlayersNicknames().entrySet()) {

            Color color = players.getValue();


            switch (color) {
                case YELLOW:
                    System.out.println(ansi().eraseScreen().bold().fg(Ansi.Color.YELLOW).a(num+"."+players.getKey()));
                    break;
                case BLUE:
                    System.out.println(ansi().eraseScreen().bold().fg(Ansi.Color.BLUE).a(num+"."+players.getKey()));
                    break;
                case PURPLE:
                    System.out.println(ansi().eraseScreen().bold().fg(Ansi.Color.MAGENTA).a(num+"."+players.getKey()));
                    break;
                case GRAY:
                    System.out.println(ansi().eraseScreen().bold().fg(Ansi.Color.WHITE).a(num+"."+players.getKey()));
                    break;
                case GREEN:
                    System.out.println(ansi().eraseScreen().bold().fg(Ansi.Color.GREEN).a(num+"."+players.getKey()));
                    break;
                default:
                    System.out.println(ansi().eraseScreen().bold().fgDefault().a(num+"."+players.getKey()));
                    break;
            }
            num++;
        }
    }

    protected void printMapInfo() {



    }

    protected String readAString(){

        String input = scanner.nextLine();
        isQuit(input);
        return input;

    }


    protected void isQuit(String input){

        if (input.equals("Quit")){
            System.out.println("Ok, Game Exit, Thank you for your participation!");
            gameController.cleanExit();
            System.exit(0);
        }

    }
}

