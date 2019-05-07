package server.controller;

import client.ClientAPI;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import server.model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;


public class Lobby implements Runnable{


    private final String lobbyID;
    private Map map;
    private ScoreBoard scoreBoard;
    private ArrayList<Player> deckOfPlayers;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;



    public Lobby(ArrayList<ClientAPI> players) {

        lobbyID = UUID.randomUUID().toString();

        scoreBoard = new ScoreBoard();
        deckOfPlayers = new ArrayList<>();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        deckWeapon = new DeckWeapon();

    }


    //For new Map,It has to ensure the map number entry 1~4
    public void chooseAndNewAMap(int num){

        this.map= new Map();


        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Map"+ num +".json");
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

    public ArrayList<Player> getDeckOfPlayers() {
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
    public void addNewPlayerToDeck(Player newPlayer) {

        this.getDeckOfPlayers().add(newPlayer);

    }


    @Override
    public void run() {
        //TODO handles the game flow
    }
}
