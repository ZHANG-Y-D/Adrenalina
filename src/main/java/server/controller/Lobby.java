package server.controller;

import server.model.*;
import java.util.ArrayList;


public class Lobby {

    private Map map;
    private ScoreBoard scoreBoard;
    private ArrayList<PlayerShell> deckOfPlayers;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;


    public Lobby(int num, int rows, int columns) {

        map = new Map(num,rows,columns);
        scoreBoard = new ScoreBoard();
        deckOfPlayers = new ArrayList<>();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        deckWeapon = new DeckWeapon();

    }

    public ArrayList<PlayerShell> getDeckOfPlayers() {
        return deckOfPlayers;
    }

    public ScoreBoard getScoreBoard() {

        return scoreBoard;

    }


}
