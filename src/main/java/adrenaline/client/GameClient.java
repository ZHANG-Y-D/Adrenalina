package adrenaline.client;


import adrenaline.client.view.cli.InitialStageCli;
import adrenaline.client.view.gui.ClientGui;
import javafx.application.Application;

import java.util.Scanner;


/**
 *
 * Represents the client application, contains the main method
 *
 *
 */
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
        while (!chooseViewMode(input)) {
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

    private static boolean chooseViewMode(String input) {

        if (input.equalsIgnoreCase("cli") ||
                input.equalsIgnoreCase("c")) {
            new InitialStageCli();
        } else if (input.equalsIgnoreCase("gui") ||
                input.equalsIgnoreCase("g")) {
            Application.launch(ClientGui.class);
        } else
            return false;

        return true;
    }


}
