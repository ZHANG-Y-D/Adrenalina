package server.controller;

import client.ClientAPI;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import server.model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;


public class Lobby implements Runnable{

    private final String lobbyID;
    private Map map;
    private ScoreBoard scoreBoard;
    private ArrayList<PlayerShell> deckOfPlayers;
    private HashMap <String, PlayerShell> players;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;
    private int mapNumber;



    public Lobby(ArrayList<ClientAPI> players) {
        lobbyID = UUID.randomUUID().toString();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Avatar.json");
            Avatar[] avatarsGson= gson.fromJson(fileReader,Avatar[].class);
            ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));
            for(ClientAPI c : players){
                c.setLobby(lobbyID);
                c.showLobbyDetails((ArrayList<Color>) avatars.stream().map(Avatar::getColor).collect(Collectors.toList()));
            }
        }catch (JsonIOException e){

        }catch (FileNotFoundException e) {

        }catch(RemoteException e) {

        }
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
        setSquaresCards();
    }



    public void setSquaresCards(){

        for (int i=0;i<this.map.getRows();i++){
            for (int j=0;j<this.map.getColumns();j++){

                if(!this.map.getSquare(i,j).isSpawn() &&
                        this.map.getSquare(i,j).getColor()!= Color.BLACK &&
                        this.map.getSquare(i,j).getAmmoCard() == null)
                    this.map.getSquare(i,j).setAmmoCard(getDeckAmmo().draw());

                if (this.map.getSquare(i,j).isSpawn())
                    while(this.map.getSquare(i,j).getWeaponCardDeck().size() < 3)
                        this.map.getSquare(i,j).getWeaponCardDeck().add(getDeckWeapon().draw());
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
