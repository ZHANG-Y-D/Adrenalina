package adrenaline.server.controller;

import adrenaline.Color;
import adrenaline.server.controller.states.*;
import adrenaline.exceptions.InvalidCardException;
import adrenaline.exceptions.NotEnoughAmmoException;
import adrenaline.exceptions.WeaponHandFullException;
import adrenaline.server.model.*;
import adrenaline.server.model.Map;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import adrenaline.LobbyAPI;
import adrenaline.server.controller.states.GameState;
import adrenaline.network.Client;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Lobby implements Runnable, LobbyAPI {

    private final String lobbyID;
    private LinkedHashMap<String, Client> clientMap;
    private HashMap <String, Player> playersMap;
    private HashMap <Color, Player> playersColor;
    private GameState currentState;
    private String currentTurnPlayer;
    private String nextTurnPlayer;

    private Map map;
    private ScoreBoard scoreBoard;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;
    private ArrayList<String> deadPlayers;



    public Lobby(ArrayList<Client> clients) {
        lobbyID = UUID.randomUUID().toString();
        clientMap = new LinkedHashMap<>();
        try {
            for(Client c : clients){
                clientMap.put(c.getClientID(),c);
            }
        }catch (NullPointerException e){
            System.err.println("NullPointerException of clients ArrayList");
        }
        playersMap = new HashMap<>();
        playersColor = new HashMap<>();

        scoreBoard = new ScoreBoard();
        deckWeapon = new DeckWeapon();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        deadPlayers = new ArrayList<>();
        currentTurnPlayer = clients.get(0).getClientID();
        nextTurnPlayer = clients.get(1).getClientID();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Avatar.json");
            Avatar[] avatarsGson= gson.fromJson(fileReader,Avatar[].class);
            ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));
            currentState = new AvatarSelectionState(this, avatars);
        }catch (JsonIOException e){
        }catch (FileNotFoundException e) {}
        System.out.println("NEW LOBBY STARTED WITH "+ clients.size()+" USERS.");
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
        //setSquaresCards();
    }

    /*
    public void setSquaresCards(){

        for (int i=0;i<this.map.getRows();i++){
            for (int j=0;j<this.map.getColumns();j++){

                if(!this.map.getSquare(i,j).isSpawn() &&
                        this.map.getSquare(i,j).getColor()!= adrenaline.Color.BLACK &&
                        this.map.getSquare(i,j).getAmmoTile() == null)
                    this.map.getSquare(i,j).setAmmoTile(getDeckAmmo().draw());

                if (this.map.getSquare(i,j).isSpawn())
                    while(this.map.getSquare(i,j).getWeaponCard().size() < 3) {
                        WeaponCard weaponCard=getDeckWeapon().draw();
                        if (weaponCard!=null)
                            this.map.getSquare(i, j).getWeaponCard().add(weaponCard);
                    }
            }
        }
    }
    */



    public Map getMap() {
        return map;
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

    public String runAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.runAction();
        else return "You can only do that during your turn!";
    }

    public String grabAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.grabAction();
        else return "You can only do that during your turn!";
    }

    public String shootAction(String clientID) {

        if(clientID.equals(currentTurnPlayer)) return currentState.shootAction();
        else return "You can only do that during your turn!";
    }

    public String selectPlayers(String clientID, ArrayList<Color> playersColor) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectPlayers(playersColor);
        else return "You can only do that during your turn!";
    }

    public String selectSquare(String clientID, int index) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectSquare(index);
        else return "You can only do that during your turn!";
    }

    public String selectPowerUp(String clientID, int powerupID) {
        //TODO granata venom during opponent's turn
        if(clientID.equals(currentTurnPlayer)) return currentState.selectPowerUp(powerupID);
        else return "You can only do that during your turn!";
    }

    public String selectWeapon(String clientID, int weaponID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectWeapon(weaponID);
        else return "You can only do that during your turn!";
    }

    public String selectFiremode(String clientID, int firemode) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectFiremode(firemode);
        else return "You can only do that during your turn!";
    }

    public String moveSubAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.moveSubAction();
        else return "You can only do that during your turn!";
    }

    public String fireSubAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.fireSubAction();
        else return "You can only do that during your turn!";
    }

    public String endOfTurnAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.endOfTurnAction();
        else return "You can only do that during your turn!";
    }

    public String selectAvatar(String clientID, Color color) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectAvatar(color);
        else return "You can only do that during your turn!";
    }

    public String selectMap(String clientID, int mapID) {
        if(clientMap.keySet().contains(clientID)){
            return currentState.selectMap(mapID, clientID);
        }
        //else: user not part of the lobby
        return "You shouldn't be here!";
    }

    public int getCurrentPlayerAdrenalineState(){
        return playersMap.get(currentTurnPlayer).getAdrenalineState();
    }

    public synchronized void endTurn(){
        checkDeadPlayers();
        if(deadPlayers.size() > 0){
            String dead = deadPlayers.get(0);
            for(int i = 1; i<deadPlayers.size();i++) deadPlayers.set(i-1, deadPlayers.get(i));
            deadPlayers.remove(deadPlayers.size()-1);
            //NOTE: this is to preserve the same order of players between respawns and regular turns
            playersMap.get(dead).addPowerupCard(deckPowerup.draw());
            currentTurnPlayer = dead;
            currentState = new RespawnState(this);
        }else {
            currentState = new SelectActionState(this, 0);
            nextPlayer();
        }
    }

    public synchronized void nextPlayer(){
        currentTurnPlayer = nextTurnPlayer;

        Iterator<String> itr = clientMap.keySet().iterator();
        String temp = itr.next();
        while (!temp.equals(currentTurnPlayer)) temp= itr.next();
        if (itr.hasNext()) nextTurnPlayer = itr.next();
        else nextTurnPlayer = clientMap.keySet().iterator().next();
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
        }catch (JsonIOException | FileNotFoundException e){
        }
    }

    private void checkDeadPlayers(){
        for(String s : clientMap.keySet()){
            if(!playersMap.get(s).isAlive()) deadPlayers.add(s);
        }
    }

    public ArrayList<Integer> sendCurrentPlayerValidSquares(int range){
        ArrayList<Integer> validSquares = map.getValidSquares(playersMap.get(currentTurnPlayer).getPosition(), range);
        //TODO sends list to adrenaline.client
        return validSquares;
    }

    public void respawnWithPowerup(int powerupID) throws InvalidCardException {
        Player currPlayer = playersMap.get(currentTurnPlayer);
        PowerupCard discardedPowerup = currPlayer.getPowerupCard(powerupID);
        if(discardedPowerup == null) throw new InvalidCardException();
        currPlayer.setPosition(map.getSpawnIndex(discardedPowerup.getColor()));
        currPlayer.removePowerupCard(discardedPowerup);
        deckPowerup.addToDiscarded(discardedPowerup);
    }

    public void movePlayer(int squareIndex){
        if(playersMap.get(currentTurnPlayer).getPosition()!= squareIndex) playersMap.get(currentTurnPlayer).setPosition(squareIndex);
    }

    public void movePlayer(int squareIndex, Color playerColor){
        if(playersColor.get(playerColor).getPosition()!= squareIndex) playersColor.get(playerColor).setPosition(squareIndex);
    }

    public void grabFromSquare(int squareIndex, int actionNumber){
        map.getSquare(squareIndex).acceptGrab(this, actionNumber);
    }

    public void grabFromSquare(SquareAmmo square, int actionNumber){
        Player currentPlayer = playersMap.get(currentTurnPlayer);
        AmmoCard grabbedAmmoTile= square.getAmmoTile();
        if (grabbedAmmoTile!=null) {
            int[] grabbedAmmoContent = grabbedAmmoTile.getAmmoContent();
            //Discard the tile. && Remove the ammo tile.
            deckAmmo.addToDiscarded(grabbedAmmoTile);
            square.setAmmoTile(null);
            //Move the depicted cubes into your ammo box.
            currentPlayer.addAmmoBox(grabbedAmmoContent);
            //If the tile depicts a powerup card, draw one.
            if (grabbedAmmoContent[3] != 0 && currentPlayer.getPowerupHandSize()<3) currentPlayer.addPowerupCard(deckPowerup.draw());
        }
        setState(new SelectActionState(this, actionNumber));
    }

    public void grabFromSquare(SquareSpawn square, int actionNumber){
        setState(new WeaponGrabState(this, actionNumber, square));
    }

    public void grabWeapon(WeaponCard weaponCard) throws NotEnoughAmmoException, WeaponHandFullException {
        Player currPlayer = playersMap.get(currentTurnPlayer);
        int[] cost = weaponCard.getAmmoCost();
        switch(weaponCard.getFreeAmmo()){
            case RED: cost[0]--; break;
            case BLUE: cost[1]--; break;
            case YELLOW: cost[2]--; break;
        }
        if(!currPlayer.canPayCost(cost)) throw new NotEnoughAmmoException();
        if(!(currPlayer.getWeaponHandSize()<3)) throw new WeaponHandFullException();
        currPlayer.payCost(cost);
        currPlayer.addWeaponCard(weaponCard);
    }

    public WeaponCard swapWeapon(WeaponCard grabbedWeapon, int droppedWeaponID) throws InvalidCardException {
        WeaponCard droppedWeapon = playersMap.get(currentTurnPlayer).getWeaponCard(droppedWeaponID);
        if(droppedWeapon == null) throw new InvalidCardException();
        playersMap.get(currentTurnPlayer).removeWeaponCard(droppedWeapon);
        try { grabWeapon(grabbedWeapon); } catch (Exception e) {}
        return droppedWeapon;
    }

    public void consumePowerup(int powerUpID) throws InvalidCardException{
        PowerupCard card = playersMap.get(currentTurnPlayer).consumePower(powerUpID);
        if(card == null) throw new InvalidCardException();
        else deckPowerup.addToDiscarded(card);
    }

    public boolean canUseWeapon(int weaponID){
        WeaponCard wc = playersMap.get(currentTurnPlayer).getWeaponCard(weaponID);
        if(wc == null || !wc.isLoaded()) return false;
        else return true;
    }

    public Firemode getFiremode(int weaponID, int firemode) throws NotEnoughAmmoException{
        try {
            Player currPlayer = playersMap.get(currentTurnPlayer);
            Firemode selectedFiremode = currPlayer.getWeaponCard(weaponID).getFiremode(firemode);
            if(currPlayer.canPayCost(selectedFiremode.getExtraCost())) return selectedFiremode;
            else throw new NotEnoughAmmoException();
        }catch(NullPointerException e){
            return null;
        }
    }
}

