package server.controller;

import client.ClientAPI;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


public class Lobby implements Runnable{

    private final String lobbyID;
    private Map map;
    private ScoreBoard scoreBoard;
    private ArrayList<PlayerShell> deckOfPlayers;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;
    private int mapNumber;



    public Lobby(ArrayList<ClientAPI> players) {

        lobbyID = UUID.randomUUID().toString();

        scoreBoard = new ScoreBoard();
        deckOfPlayers = new ArrayList<>();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        deckWeapon = new DeckWeapon();

    }



    public void chooseAndNewAMap(int num){

        this.mapNumber=num;
        this.map= new Map();


        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Map"+this.mapNumber+".json");
            this.map=gson.fromJson(fileReader,Map.class);

        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard.json file not found");
        }
        complementAllMapResource();
    }



    public void complementAllMapResource(){

        for (int i=0;i<this.map.getRows();i++){
            for (int j=0;j<this.map.getColumns();j++){

                if(!this.map.getMapSquares()[i][j].isSpawn() &&
                        this.map.getMapSquares()[i][j].getColor()!=Color.BLACK &&
                        this.map.getMapSquares()[i][j].getAmmoCard()==null)
                    this.map.getMapSquares()[i][j].setAmmoCard(getDeckAmmo().draw());

                if (this.map.getMapSquares()[i][j].isSpawn() &&
                        this.map.getMapSquares()[i][j].getWeaponCardsDeck()==null)
                    this.map.getMapSquares()[i][j].newWeaponCardsDeck();

                while (this.map.getMapSquares()[i][j].isSpawn() &&
                        this.map.getMapSquares()[i][j].getWeaponCardsDeck().size()<3) {
                    this.map.getMapSquares()[i][j].getWeaponCardsDeck().add(getDeckWeapon().draw());
                }

            }
        }
    }




    public Map getMap() {
        return map;
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
