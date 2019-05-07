package server.model;

import java.util.ArrayList;

public class ScoreBoard {

    private ArrayList<Player> scoreBoard;


    public ScoreBoard() {
        this.scoreBoard = new ArrayList<>();
    }

    public ArrayList<Player> getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(Player player) {


        //Case of overkill
        this.getScoreBoard().add(player);
    }

}
