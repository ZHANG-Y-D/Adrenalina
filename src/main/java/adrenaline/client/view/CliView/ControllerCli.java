package adrenaline.client.view.CliView;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.model.Player;
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
                printAString("ansi",string);
                string=bufferedReader.readLine();
            }
        }catch (FileNotFoundException e){
            System.err.println("\nsrc/main/resources/ForCli/"+srcFileName+"  File Not Found ");
        }catch (IOException e){
            System.err.println("\n printSrcFile IOException ");
        }
    }

    protected synchronized int readANumber(int down,int up) {

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

    protected synchronized int readANumber(ArrayList<Integer> rangeList){


        int num;
        try{
            num = scanner.nextInt();
            scanner.nextLine();
        }catch (InputMismatchException e){
            System.out.println("\nPlease answer with a number");
            isQuit(scanner.nextLine());
            num = readANumber(rangeList);
        }

        if (!rangeList.contains(num) && num!=0) {
            System.err.println("Invalid number, Retry:");
            num = readANumber(rangeList);
        }

        return num;

    }


    protected synchronized static void printAString(String printState,String printString){

        if (printState.equals("err"))
            System.err.println(printString);
        else if (printState.equals("OutWithOutNewLine"))
            System.out.print(printString);
        else if (printState.equals("ansi")){
            System.out.println(ansi().eraseScreen().render(printString));
        }else
            System.out.println(printString);

        return;
    }


    protected synchronized String readAString(){

        String input = scanner.nextLine();
        isQuit(input);
        return input;

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



    protected void printAllPlayerInfo() {

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
        printMap();

        System.out.println("\n-------");
        System.out.println("Weapon Info...");
        System.out.println(" ");
        System.out.println(ansi().bold().fg(Ansi.Color.BLUE).a("At map square  NO.2 █").fgDefault());
        printWeaponInfo(gameController.getMap().getWeaponMap().get(Color.BLUE));
        System.out.println(" ");
        System.out.println(ansi().bold().fg(Ansi.Color.RED).a("At map square  NO.4 █").fgDefault());
        printWeaponInfo(gameController.getMap().getWeaponMap().get(Color.RED));
        System.out.println(" ");
        System.out.println(ansi().bold().fg(Ansi.Color.YELLOW).a("At map square  NO.11 █").fgDefault());
        printWeaponInfo(gameController.getMap().getWeaponMap().get(Color.YELLOW));

        printPlayerSelfInfo();

        printSrcFile("GameInfoEnd.txt");

    }

    protected void printMap() {

        System.out.println("\n-------");
        System.out.println("Map...");
        printSrcFile("Map"+gameController.getMap().getMapID()+".txt");

    }


    protected static void printPowerupInfo(ArrayList<Integer> powerupList){

        if (powerupList==null) {
            System.err.println("You don't have powerup card now");
            return;
        }

        for (int powerup:powerupList){
            printSrcFile("Powerup"+powerup+".txt");
        }

    }

    protected void printPlayerSelfInfo() {

        Player player = gameController.getPlayersMap().get(gameController.getOwnColor());

        System.out.println("\n-------");
        System.out.println("Your own powerup cards...");
        printPowerupInfo(player.getPowerupCards());
        System.out.println("Your own weapon cards...");
        printWeaponInfo(player.getWeaponCards());
    }


    protected static void printWeaponInfo(ArrayList<Integer> weaponList){

        if (weaponList==null) {
            System.err.println("Don't have weapon card now");
            return;
        }

        for (int weapon:weaponList){
            printSrcFile("Weapon"+weapon+".txt");
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

