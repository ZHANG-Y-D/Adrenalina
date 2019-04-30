package server.controller;

import server.model.Color;
import server.model.PlayerCore;


/*
 *
 * Responsible: Zhang YueDong
 *
 * Attention from Zhang
 *
 *
 * About Class PlayerCore and Class PlayerShell
 *
 *      The PlayerShell is made for index the actual player,
 *      but the PlayerCore is in the level model for index the status of the player,
 *      when someone is dead, this class will be free. When it is resurrected,
 *      the class PlayerShell have to renew it.
 *
 *
 * Why I have created these two different class
 *
 *      For Better distinguish its functionality and information
 *      So that,when someone is dead,just free PlayerCore instead of reset a lot of parameter in the Player
 *      More modular and more stable,and improve scalability
 *
 *
 * How to use them
 *
 *
 *      When a player(Bob) has joined, new a class PlayerShell.
 *      When Bob begins his turn, use method newPlayerCore in the PlayerShell,
 *          and use method getPlayerCore to get this class;
 *      When Bob has dead, free PlayerCore.
 *      When Bob is resurrected, reuse method newPlayerCore.
 *      However the PlayerShell for information, the PlayerCore for functionality
 *
 *      PlayerShell always exist,PlayCore only exist when this player is still alive
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
    private Lobby lobby;





    public PlayerShell(String name, Color color,int modeOfGame,Lobby lobby) {
        this.name = name;
        this.color = color;
        score = 0;
        numberOfDeaths = 0;
        isStatusDead = true;
        playerCore = null;
        this.modeOfGame = modeOfGame;
        this.lobby=lobby;
    }


    public void newPlayerCore(){

        if (!isStatusDead){
            System.err.print("You are still alive,can't not do this");
            return;
        }
        this.playerCore=new PlayerCore(this);
        isStatusDead = false;

    }

    public void setStatusDead(boolean statusDead) {
        isStatusDead = statusDead;
    }

    public int getNumberOfDeaths() {
        return numberOfDeaths;
    }

    public int getModeOfGame() {
        return modeOfGame;
    }

    public void addScore(int score) {

        this.score= this.score+score;

    }

    public int getScore() {
        return score;
    }

    public PlayerCore getPlayerCore() {
        return playerCore;
    }

    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public String toString() {
        return "PlayerShell{" +
                "name='" + name + '\'' +
                ", color=" + color +
                ", score=" + score +
                ", numberOfDeaths=" + numberOfDeaths +
                ", isStatusDead=" + isStatusDead +
                ", modeOfGame=" + modeOfGame +
                '}';
    }
}
