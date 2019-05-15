package server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import server.LobbyAPI;
import server.controller.states.GameState;
import server.controller.states.*;
import server.model.Map;
import server.network.Client;
import server.model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Lobby implements Runnable, LobbyAPI {

    private final String lobbyID;
    private LinkedHashMap<String, Client> clientMap;
    private HashMap <String, Player> playersMap;
    private HashMap <Color, Player> playersColor;
    private ArrayList<Player> playersList;
    private String currentTurnPlayer;
    private GameState currentState;
    private HashMap<String, GameState> gameStates;

    private Map map;
    private ScoreBoard scoreBoard;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;


    public Lobby(ArrayList<Client> clients) {
        lobbyID = UUID.randomUUID().toString();
        clientMap = new LinkedHashMap<>();
        for(Client c : clients){
            clientMap.put(c.getClientID(),c);
        }
        scoreBoard = new ScoreBoard();
        playersList = new ArrayList<>();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        deckWeapon = new DeckWeapon();
    }



    //MERGED INTO initMap, TO BE SAFELY REMOVED
    public void chooseAndNewAMap(int num){
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

    public ArrayList<Player> getPlayersList() {
        return playersList;
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

        return this.getPlayersList().size();

    }


    //Use this method to add every player
    public void addNewPlayerToDeck(Player newPlayer) {

        this.getPlayersList().add(newPlayer);

    }


    @Override
    public void run() {
        initStates();
        currentState = gameStates.get("AvatarSelectionState");
        while(clientMap.size()>playersMap.size());
        initMap();
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
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Avatar.json");
            Avatar[] avatarsGson= gson.fromJson(fileReader,Avatar[].class);
            ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));
            gameStates.put("AvatarSelectionState", new AvatarSelectionState(this, avatars));
        }catch (JsonIOException e){
        }catch (FileNotFoundException e) {}
    }

    public void setState(String state){ currentState = gameStates.get(state);
    }

    public void runAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) currentState.runAction();
    }

    public void grabAction(String clientID) {

        if(clientID.equals(currentTurnPlayer)) currentState.grabAction();
    }

    public void shootAction(String clientID) {

        if(clientID.equals(currentTurnPlayer)) currentState.shootAction();
    }

    public void selectPlayers(String clientID, ArrayList<Color> playersColor) {
        if(clientID.equals(currentTurnPlayer)) currentState.selectPlayers(playersColor);
    }

    public void selectSquare(String clientID, int index) {

        if(clientID.equals(currentTurnPlayer)) currentState.selectSquare(index);
    }

    public void selectPowerUp(String clientID, int powerupID) {

        if(clientID.equals(currentTurnPlayer)) currentState.selectPowerUp(powerupID);
    }

    public void selectWeapon(String clientID, int weaponID) {

        if(clientID.equals(currentTurnPlayer)) currentState.selectWeapon(weaponID);
    }

    public void endOfTurnAction(String clientID) {

        if(clientID.equals(currentTurnPlayer)) currentState.endOfTurnAction();
    }

    public void selectAvatar(String clientID, Color color) {
        if(clientID.equals(currentTurnPlayer)) currentState.selectAvatar(color);
    }

    public void selectMap(String clientID, int mapID) {
        if(clientMap.keySet().contains(clientID)){
            currentState.selectMap(mapID, clientID);
        }
        //else: user not part of the lobby
    }

    public void endTurn(){
        currentState = gameStates.put("SelectActionState", new SelectActionState(this));
        nextPlayer();
    }

    public void nextPlayer(){
        Iterator<String> itr = clientMap.keySet().iterator();
        String temp = itr.next();
        while (!temp.equals(currentTurnPlayer)) temp= itr.next();
        if (itr.hasNext()) currentTurnPlayer = itr.next();
        else currentTurnPlayer = clientMap.keySet().iterator().next();
    }

    public synchronized void initCurrentPlayer(Avatar chosen){
        Player newPlayer = new Player(chosen);
        playersMap.put(currentTurnPlayer, newPlayer);
        playersColor.put(chosen.getColor(), newPlayer);
        nextPlayer();
    }

    private void initMap(){
        MapSelectionState mapSelectionState = new MapSelectionState(this, new ArrayList<>(clientMap.keySet()));
        currentState = mapSelectionState;
        int mapID = mapSelectionState.startTimer();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Map"+ mapID +".json");
            this.map=gson.fromJson(fileReader,Map.class);
        }catch (JsonIOException e){
        }
        catch (FileNotFoundException e) {
        }

    }

}

