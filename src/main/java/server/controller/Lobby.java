package server.controller;

import client.ClientAPI;
import server.model.*;
import java.util.ArrayList;
import java.util.UUID;


public class Lobby implements Runnable{

    private final String lobbyID;
    private Map map;
    private ScoreBoard scoreBoard;
    private ArrayList<PlayerShell> deckOfPlayers;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;



    public Lobby(ArrayList<ClientAPI> players) {
        lobbyID = UUID.randomUUID().toString();

        map = new Map(this);
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

    public DeckAmmo getDeckAmmo() {
        return deckAmmo;
    }

    public DeckWeapon getDeckWeapon() {
        return deckWeapon;
    }

    //It will return how much Players have already entered
    public int getNumOfPlayers(){

        return this.getDeckOfPlayers().size();

    }


    //Use this method to add every player
    public void addNewPlayerToDeck(PlayerShell newPlayer) {

        this.getDeckOfPlayers().add(newPlayer);

    }


    @Override
    public void run() {
        //TODO handles the game flow
    }
}
