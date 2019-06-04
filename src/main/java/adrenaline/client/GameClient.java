package adrenaline.client;


import adrenaline.client.view.CliView.InitialCli;
import adrenaline.client.view.ClientGui;
import javafx.application.Application;

import java.util.Scanner;

public class GameClient {



    /**
     *
     * The main method, The player have to choose view mode at this time
     *
     * @param args Get input stream
     *
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Adrenalina!");
        System.out.println("Do you want to play with CLI (Command Line Interface) or GUI (Graphic User Interface)?");
        String input = scanner.nextLine();
        while (!chooseTheViewMode(input)) {
            System.out.println("Please input \"cli\" or \"gui\":");
            input = scanner.nextLine();
        }
    }




    /**
     *
     *
     * This method to exec choose from Player,Cli or Gui
     *
     * @param input The input of Player
     * @return Return true if the input is right.
     *
     */

    private static boolean chooseTheViewMode(String input) {

        if (input.equalsIgnoreCase("cli") ||
                input.equalsIgnoreCase("c")) {
            InitialCli initialCli = new InitialCli();
            initialCli.initialStageCli();
        } else if (input.equalsIgnoreCase("gui") ||
                input.equalsIgnoreCase("g")) {
            Application.launch(ClientGui.class);
        } else
            return false;

        return true;
    }


}
