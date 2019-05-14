package server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import server.controller.states.GameState;
import server.controller.states.*;
import server.network.Client;
import server.model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;


public class Lobby implements Runnable {

    private final String lobbyID;
    private HashMap <String, Player> players;
    private Map map;
    private ScoreBoard scoreBoard;
    private ArrayList<Player> listOfPlayers;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;
    private GameState currentState;
    private HashMap<String, GameState> gameStates;


    public Lobby(ArrayList<Client> players) {
        lobbyID = UUID.randomUUID().toString();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Avatar.json");
            Avatar[] avatarsGson= gson.fromJson(fileReader,Avatar[].class);
            ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));
        }catch (JsonIOException e){

        }catch (FileNotFoundException e) {
        }
        scoreBoard = new ScoreBoard();
        listOfPlayers = new ArrayList<>();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        deckWeapon = new DeckWeapon();
        //initStates();
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
        setSquaresCards();
    }



    public void setSquaresCards(){

        for (int i=0;i<this.map.getRows();i++){
            for (int j=0;j<this.map.getColumns();j++){

                if(!this.map.getSquare(i,j).isSpawn() &&
                        this.map.getSquare(i,j).getColor()!= Color.BLACK &&
                        this.map.getSquare(i,j).getAmmoTile() == null)
                    this.map.getSquare(i,j).setAmmoTile(getDeckAmmo().draw());

                if (this.map.getSquare(i,j).isSpawn())
                    while(this.map.getSquare(i,j).getWeaponCardDeck().size() < 3) {
                        WeaponCard weaponCard=getDeckWeapon().draw();
                        if (weaponCard!=null)
                            this.map.getSquare(i, j).getWeaponCardDeck().add(weaponCard);
                    }
            }
        }
    }



    public Map getMap() {
        return map;
    }

    public ArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public ScoreBoard getScoreBoard() {

        return scoreBoard;

    }

    public DeckPowerup getDeckPowerup() {
        return deckPowerup;
    }

    public DeckAmmo getDeckAmmo() {
        return deckAmmo;
    }


    public DeckWeapon getDeckWeapon() {
        return deckWeapon;
    }


    //It will return how much Players have already entered
    public int getNumOfPlayers(){

        return this.getListOfPlayers().size();

    }


    //Use this method to add every player
    public void addNewPlayerToDeck(Player newPlayer) {

        this.getListOfPlayers().add(newPlayer);

    }


    @Override
    public void run() {
        //TODO handles the game flow
    }

    public String getID() {
        return this.lobbyID;
    }

    private void initStates(){
        gameStates.put("SelectAtionState", new SelectActionState(this));
        gameStates.put("RunState", new RunState(this));
        gameStates.put("GrabState", new GrabState(this));
        gameStates.put("ShootState", new ShootState(this));
        gameStates.put("RealoadState", new SelectActionState(this));
        currentState = gameStates.get("SelectActionState");
    }

    public void setState(String state){
        currentState = gameStates.get(state);
    }

    public void endTurn(){
        currentState = gameStates.put("SelectActionState", new SelectActionState(this));
        //TODO cicla sui giocatori
    }

    public void nextPlayer(){
    }
}

