package server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import server.exceptions.InvalidWeaponException;
import server.LobbyAPI;
import server.exceptions.NotEnoughAmmoException;
import server.exceptions.WeaponHandFullException;
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

    private Map map;
    private ScoreBoard scoreBoard;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;



    public Lobby(ArrayList<Client> clients) {
        lobbyID = UUID.randomUUID().toString();
        clientMap = new LinkedHashMap<>();

        //ZHANG has added this try-catch for NullPointerException
        //If not necessary，Delete it and retest Test code,add clients parameter
        try {
            for(Client c : clients){
                clientMap.put(c.getClientID(),c);
            }
        }catch (NullPointerException e){
            System.err.println("NullPointerException of clients ArrayList");
        }
        playersMap = new HashMap<>();
        playersColor = new HashMap<>();
        playersList = new ArrayList<>();

        scoreBoard = new ScoreBoard();
        deckWeapon = new DeckWeapon();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Avatar.json");
            Avatar[] avatarsGson= gson.fromJson(fileReader,Avatar[].class);
            ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));
            currentState = new AvatarSelectionState(this, avatars);
        }catch (JsonIOException e){
        }catch (FileNotFoundException e) {}
    }



    //MERGED INTO initMap, TO BE SAFELY REMOVED
    public void chooseAndNewAMap(int num){
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map"+ num +".json");
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
                    while(this.map.getSquare(i,j).getWeaponCards().size() < 3) {
                        WeaponCard weaponCard=getDeckWeapon().draw();
                        if (weaponCard!=null)
                            this.map.getSquare(i, j).getWeaponCards().add(weaponCard);
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

    public DeckPowerup getDeckPowerup() {
        return deckPowerup;
    }

    public DeckAmmo getDeckAmmo() {
        return deckAmmo;
    }


    public DeckWeapon getDeckWeapon() {
        return deckWeapon;
    }

    @Override
    public void run() {
        while(clientMap.size()>playersMap.size()); //waits for state to change from AvatarSelectionState
        initMap();
        currentState = new SelectActionState(this, 0);
        //TODO handles the game flow
    }

    public String getID() {
        return this.lobbyID;
    }

    public void setState(GameState newState){ currentState = newState; }

    public void runAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) currentState.runAction();
    }

    public void grabAction(String clientID) { if(clientID.equals(currentTurnPlayer)) currentState.grabAction(); }

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

    public int getCurrentPlayerAdrenalineState(){
        return playersMap.get(currentTurnPlayer).getAdrenalineState();
    }

    public void endTurn(){
        currentState = new SelectActionState(this, 0);
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

    public ArrayList<Integer> sendCurrentPlayerValidSquares(int range){
        ArrayList<Integer> validSquares = map.getValidSquares(playersMap.get(currentTurnPlayer).getPosition(), range);
        //TODO sends list to client
        return validSquares;
    }

    public void movePlayer(int squareIndex){
        if(playersMap.get(currentTurnPlayer).getPosition()!= squareIndex) playersMap.get(currentTurnPlayer).setPosition(squareIndex);
    }

    public void movePlayer(int squareIndex, Color playerColor){
        if(playersColor.get(playerColor).getPosition()!= squareIndex) playersColor.get(playerColor).setPosition(squareIndex);
    }

    public boolean grabAmmo(){
        if(map.isSpawnSquare(playersMap.get(currentTurnPlayer).getPosition())) return false;
        //TODO
        return true;
    }

    public void grabWeapon(int ID) throws NotEnoughAmmoException, WeaponHandFullException, InvalidWeaponException {
        int currentPos = playersMap.get(currentTurnPlayer).getPosition();
        if(map.isSpawnSquare(currentPos)){
            boolean found = false;
            for(WeaponCard c : map.getSquareWeapons(currentPos)){
                //TODO
            }
        }
    }
}

