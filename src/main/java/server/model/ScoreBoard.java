package server.model;

import server.controller.PlayerShell;

import java.util.ArrayList;

public class ScoreBoard {

    private ArrayList<PlayerShell> scoreBoard;


    public ScoreBoard() {
        this.scoreBoard = new ArrayList<>();
    }

    public ArrayList<PlayerShell> getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(PlayerShell playerShell) {


        //Case of overkill
        this.getScoreBoard().add(playerShell);
    }

}
