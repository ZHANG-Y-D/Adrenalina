package adrenaline.client.view.CliView;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import org.fusesource.jansi.Ansi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

    protected int readANumber(int down,int up) {

        int num;
        try{
            num = scanner.nextInt();
            scanner.nextLine();
        }catch (InputMismatchException e){
            System.out.println("\nPlease answer with a number");
            isQuit(scanner.nextLine());
            num = readANumber(down,up);
        }

        if (num < down || num > up) {
            System.err.println("Please answer with a number from "+down+" to "+up+"! Retry:");
            num = readANumber(down, up);
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

            Ansi.Color ansiColor = transferColorToAnsiColor(players.getValue());

            System.out.println(ansi().eraseScreen().bold().fg(ansiColor).a(num+"."+players.getKey()));

            num++;
        }
    }



    protected int getPlayerTurnNumber(){

        int num=1;

        for (Map.Entry<String, Color> players : gameController.getPlayersNicknames().entrySet()) {

            if (players.getKey().equals(gameController.getOwnNickname()))
                break;

            num++;
        }
        return num;
    }



    protected Ansi.Color transferColorToAnsiColor(Color value) {

        switch (value) {
            case YELLOW:
                return Ansi.Color.YELLOW;
            case BLUE:
                return Ansi.Color.BLUE;
            case PURPLE:
                return Ansi.Color.MAGENTA;
            case GRAY:
                return Ansi.Color.WHITE;
            case GREEN:
                return Ansi.Color.GREEN;
            case RED:
                return Ansi.Color.RED;
            case WHITE:
                return Ansi.Color.BLACK;
            default:
                return Ansi.Color.DEFAULT;
        }
    }



    protected synchronized void printGameInfo() {

        printSrcFile("GameInfo.txt");
        System.out.println("Map...");
        printSrcFile("Map"+gameController.getMap().getMapID()+".txt");
        System.out.println("Weapon Info...");
        printWeaponInfo();


    }

    private void printWeaponInfo(){


        for (Map.Entry<Color, ArrayList<Integer>> weaponInfo : gameController.getMap().getWeaponMap().entrySet()) {



            switch (weaponInfo.getKey()){
                case BLUE:
                    System.out.print(ansi().bold().fg(Ansi.Color.BLUE).a("█ 3:").fgDefault());
                    break;
                case RED:
                    System.out.print(ansi().bold().fg(Ansi.Color.RED).a("█ 5:").fgDefault());
                    break;
                case YELLOW:
                    ansi().bold().fg(Ansi.Color.YELLOW).a("█ 12:").fgDefault();
                    break;
                default:
                    break;
            }


        }



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

