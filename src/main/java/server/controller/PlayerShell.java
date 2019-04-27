package server.controller;

import server.model.Color;
import server.model.PlayerCore;


/*
 *
 * Responsible: Zhang YueDong
 *
 * Attention from Zhang
 *
 * About Class PlayerCore and Class PlayerShell
 *
 *      The PlayerShell is made for index the actual player,
 *      but the PlayerCore is in the level model for index the status of the player,
 *      when someone is dead, this class will be free. When it is resurrected,
 *      the class PlayerShell have to renew it.
 *
 * Why I have created these two different class
 *
 *      For Better distinguish its functionality and information
 *
 * How to use them
 *
 *      When a player(Bob) has joined, new a class PlayerShell.
 *      When Bob begins his turn, new a class PlayerCore.
 *      When Bob has dead, free PlayerCore.
 *      When Bob is resurrected, new PlayerCore.
 *      However the PlayerShell for information, the PlayerCore for functionality
 *
 *
 *
 *
 *
 */

public class PlayerShell {

    private String name;
    private Color color;       //For index the color of Avatar
    private int score;
    private int numberOfDeaths;
    private boolean isStatusDead;
    private PlayerCore playerCore;
    private int modeOfGame;    //  1:normal mode  2:final frenzy mode  3:to be defined





    public PlayerShell(String name, Color color,int modeOfGame) {
        this.name = name;
        this.color = color;
        score = 0;
        numberOfDeaths = 0;
        isStatusDead = true;
        playerCore = null;
        this.modeOfGame = modeOfGame;
    }

    public void setStatusDead(boolean statusDead) {
        isStatusDead = statusDead;
    }


    public void addScore(int score) {

        this.score= this.score+score;

    }

    public PlayerCore getPlayerCore() {
        return playerCore;
    }
}
