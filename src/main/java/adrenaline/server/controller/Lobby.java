package adrenaline.server.controller;

import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.controller.states.*;
import adrenaline.server.exceptions.*;
import adrenaline.server.model.*;
import adrenaline.server.model.Map;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import adrenaline.server.LobbyAPI;
import adrenaline.server.controller.states.GameState;
import adrenaline.server.network.Client;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class Lobby implements Runnable, LobbyAPI {

    private final String lobbyID;
    private final Integer TURN_TIMEOUT_IN_SECONDS = 60;

    private LinkedHashMap<String, Client> clientMap;
    private HashMap <String, Player> playersMap;
    private HashMap <Color, Player> playersColor;
    private GameState currentState;
    private String currentTurnPlayer;
    private String nextTurnPlayer;
    private int executedActions;

    private Map map=null;
    private ScoreBoard scoreBoard;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;
    private ArrayList<String> deadPlayers;
    private Set<Color> damagedThisTurn;
    private Chat chat;

    private ScheduledExecutorService turnTimer;
    private Future scheduledTimeout;


    public Lobby(ArrayList<Client> clients) {
        lobbyID = UUID.randomUUID().toString();
        clientMap = new LinkedHashMap<>();
        clients.forEach(x -> clientMap.put(x.getClientID(), x));
        playersMap = new HashMap<>();
        playersColor = new HashMap<>();
        scoreBoard = new ScoreBoard(clients);
        clients.forEach(scoreBoard::attach);
        deckWeapon = new DeckWeapon();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        deadPlayers = new ArrayList<>();
        damagedThisTurn = new HashSet<>();
        chat = new Chat(clients);
        currentTurnPlayer = clients.get(0).getClientID();
        nextTurnPlayer = clients.get(1).getClientID();
        turnTimer = Executors.newScheduledThreadPool(1);
        System.out.println("NEW LOBBY STARTED WITH "+ clients.size()+" USERS.");
    }


    public String getID() {
        return this.lobbyID;
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


    public Map getMap() {
        return map;
    }

    public DeckWeapon getDeckWeapon() {
        return deckWeapon;
    }

    @Override
    public void run() {
        avatarSelection();
        initMap();
        playersMap.get(currentTurnPlayer).addPowerupCard(deckPowerup.draw());
        playersMap.get(currentTurnPlayer).addPowerupCard(deckPowerup.draw());
        playersMap.get(currentTurnPlayer).setFirstRound();
        currentState = new RespawnState(this, true);
        //TODO handles the game flow
    }


    private synchronized void avatarSelection(){
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Avatar.json");
            Avatar[] avatarsGson= gson.fromJson(fileReader,Avatar[].class);
            ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));
            AvatarSelectionState avatarSelectionState = new AvatarSelectionState(this, avatars);
            currentState = avatarSelectionState;
            while(clientMap.size()>playersMap.size()) {
                scheduledTimeout = turnTimer.schedule(new AvatarTimer(avatarSelectionState), TURN_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
                clientMap.values().forEach(x -> {
                    try {
                        x.timerStarted(TURN_TIMEOUT_IN_SECONDS, clientMap.get(currentTurnPlayer).getNickname()+"'s turn to select.");
                    } catch (RemoteException e) {
                    }
                });
                wait();
            }
        }catch (JsonIOException | FileNotFoundException e) {
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
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

    public String selectSquare(String clientID, Integer index) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectSquare(index);
        else return "You can only do that during your turn!";
    }

    public String selectPowerUp(String clientID, Integer powerupID) {
        PowerupCard puc = playersMap.get(clientID).getPowerupCard(powerupID);
        if(puc==null) return "You cannot use that powerup!";
        if(clientID.equals(currentTurnPlayer)) {
            return currentState.selectPowerUp(puc);
        }
        else{
            if(puc.isUsableOutsideTurn()){
                System.out.println("venom");
                useGrenadePowerup(clientID, puc);
                return "OK";
            }
            else return "You can only do that during your turn!";
        }
    }

    public String selectWeapon(String clientID, Integer weaponID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectWeapon(weaponID);
        else return "You can only do that during your turn!";
    }

    public String selectFiremode(String clientID, Integer firemode) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectFiremode(firemode);
        else return "You can only do that during your turn!";
    }

    public String moveSubAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.moveSubAction();
        else return "You can only do that during your turn!";
    }
    public String goBack(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.goBack();
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

    public String selectSettings(String clientID, Integer mapID, Integer skulls) {
        if(clientMap.keySet().contains(clientID)){
            return currentState.selectSettings(mapID, skulls, clientID);
        }
        //else: user not part of the lobby
        return "You should not be here!";
    }

    public String sendChatMessage(String clientID, String message) {
        try{
            String senderName = clientMap.get(clientID).getNickname();
            Color senderColor = playersMap.get(clientID).getColor();
            if(message.length()<1){
                return "";
            }
            else {
                chat.addMessage(senderName, senderColor, message);
                return "OK";
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            return "You should not be here!";}
    }

    public int getCurrentPlayerAdrenalineState(){
        return playersMap.get(currentTurnPlayer).getAdrenalineState();
    }

    public void incrementExecutedActions(){ executedActions++;}

    public int getExecutedActions(){ return executedActions; }

    public synchronized void endTurn(boolean timeoutReached){
        if(!timeoutReached) scheduledTimeout.cancel(false);
        executedActions=0;
        setMapCards();
        checkDeadPlayers();
        if(!deadPlayers.isEmpty()){
            String dead = deadPlayers.get(0);
            for(int i = 1; i<deadPlayers.size();i++) deadPlayers.set(i-1, deadPlayers.get(i));
            deadPlayers.remove(deadPlayers.size()-1);
            //NOTE: this is to preserve the same order of players between respawns and regular turns
            playersMap.get(dead).addPowerupCard(deckPowerup.draw());
            currentTurnPlayer = dead;
            currentState = new RespawnState(this);
        }else {
            nextPlayer();
            Player currentPlayer = playersMap.get(currentTurnPlayer);
            if(currentPlayer.isFirstRound()){
                currentPlayer.addPowerupCard(deckPowerup.draw());
                currentPlayer.addPowerupCard(deckPowerup.draw());
                currentState = new RespawnState(this, true);
                currentPlayer.setFirstRound();
            }else currentState = new SelectActionState(this);
        }
        scheduledTimeout = turnTimer.schedule(new TurnTimer(this), TURN_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        clientMap.values().forEach(x -> {
            try {
                x.timerStarted(TURN_TIMEOUT_IN_SECONDS, clientMap.get(currentTurnPlayer).getNickname()+"'s turn.");
            } catch (RemoteException e) { }
        });
    }

    private synchronized void nextPlayer(){
        currentTurnPlayer = nextTurnPlayer;
        Iterator<String> itr = clientMap.keySet().iterator();
        String temp = itr.next();
        while (!temp.equals(currentTurnPlayer)) temp= itr.next();
        if (itr.hasNext()) nextTurnPlayer = itr.next();
        else nextTurnPlayer = clientMap.keySet().iterator().next();
    }

    public synchronized void initCurrentPlayer(Avatar chosen, boolean timeoutReached){
        if(!timeoutReached) scheduledTimeout.cancel(false);
        Player newPlayer = new Player(chosen, clientMap.get(currentTurnPlayer).getNickname(), new ArrayList<>(clientMap.values()));
        playersMap.put(currentTurnPlayer, newPlayer);
        playersColor.put(chosen.getColor(), newPlayer);
        nextPlayer();
        notifyAll();
    }

    private void initMap(){
        MapSelectionState mapSelectionState = new MapSelectionState(this, new ArrayList<>(clientMap.keySet()));
        currentState = mapSelectionState;
        clientMap.values().forEach(x -> {
            try { x.timerStarted(mapSelectionState.getTimeoutDuration(), "Vote match settings.");
            } catch (RemoteException e) { }
        });
        int votes[] = mapSelectionState.startTimer();
        try{
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map"+ votes[0] +".json");
            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(Square.class, new CustomSerializer());
            Gson gson = gsonBld.create();
            map = gson.fromJson(fileReader,Map.class);
            map.setSquaresContext();
            setMapCards();
            map.setObservers(new ArrayList<>(clientMap.values()));
            fileReader.close();
        } catch (JsonIOException | IOException e){
            e.printStackTrace();
        }
    }

    private void checkDeadPlayers(){
        for(String s : clientMap.keySet()){
            Player player = playersMap.get(s);
            if(!player.isAlive() && !player.isFirstRound()) deadPlayers.add(s);
        }
    }

    public ArrayList<Integer> sendCurrentPlayerValidSquares(int range){
        ArrayList<Integer> validSquares = map.getValidSquares(playersMap.get(currentTurnPlayer).getPosition(), range);
        try {
            clientMap.get(currentTurnPlayer).validSquaresInfo(validSquares);
        } catch (RemoteException e) { }
        return validSquares;
    }

    public ArrayList<Integer> sendCurrentPlayerValidSquares(int range, ArrayList<RangeConstraint> constraints){
        ArrayList<Integer> validSquares = map.getValidSquares(playersMap.get(currentTurnPlayer).getPosition(), range);
        constraints.forEach(x -> validSquares.retainAll(x.checkConst(playersMap.get(currentTurnPlayer).getPosition(), map)));
        try {
            clientMap.get(currentTurnPlayer).validSquaresInfo(validSquares);
        } catch (RemoteException e) { }
        return validSquares;
    }

    public ArrayList<Integer> sendCurrentPlayerValidSquares(Firemode firemode) {
        ArrayList<Integer> validSquares = firemode.getRange(playersMap.get(currentTurnPlayer).getPosition(), map);
        try {
            clientMap.get(currentTurnPlayer).validSquaresInfo(validSquares);
        } catch (RemoteException e) { }
        return validSquares;
    }

    public ArrayList<Integer> sendTargetValidSquares(ArrayList<Color> selectedTargets, ArrayList<RangeConstraint> constraints) {
        ArrayList<Integer> validSquares = new ArrayList<>();
        for(int i = 0; i<= map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        for(Player target : selectedTargets.stream().map(x->playersColor.get(x)).collect(Collectors.toList())){
            constraints.forEach(y -> validSquares.retainAll(y.checkConst(target.getPosition(), map)));
        }
        try {
            clientMap.get(currentTurnPlayer).validSquaresInfo(validSquares);
        } catch (RemoteException e) { }
        return validSquares;
    }

    public void respawnWithPowerup(PowerupCard powerup) throws InvalidCardException {
        Player currPlayer = playersMap.get(currentTurnPlayer);
        currPlayer.setPosition(map.getSpawnIndex(powerup.getColor()));
        currPlayer.removePowerupCard(powerup);
        deckPowerup.addToDiscarded(powerup);
    }

    public void movePlayer(int squareIndex){
        if(playersMap.get(currentTurnPlayer).getPosition()!= squareIndex) playersMap.get(currentTurnPlayer).setPosition(squareIndex);
    }

    public void movePlayer(int squareIndex, Color playerColor){
        if(playersColor.get(playerColor).getPosition()!= squareIndex) playersColor.get(playerColor).setPosition(squareIndex);
    }

    public ArrayList<Player> tryMovePlayers(ArrayList<Color> colors, int squareIndex, int moveRange){
        Set<Player> players = new HashSet<>();
        colors.forEach(x -> players.add(playersColor.get(x)));
        ArrayList<Integer> intersectValidSquares = new ArrayList<>(map.getMaxSquare()+1);
        for(int i=0; i<=map.getMaxSquare(); i++) intersectValidSquares.add(i);
        players.stream().map(x -> map.getValidSquares(x.getPosition(), moveRange)).forEach(intersectValidSquares::retainAll);
        if(!intersectValidSquares.contains(squareIndex)) return null;
        else players.forEach(x -> x.setPosition(squareIndex));
        return new ArrayList<>(players);
    }

    private void setMapCards(){
        if(map!=null) {
            for (int i = 0; i <= map.getMaxSquare(); i++) {
                Square square = map.getSquare(i);
                if(square != null)  square.setCard(this);
            }
        }
    }

    public void setAmmoCard(SquareAmmo squareAmmo) {
        squareAmmo.setAmmoTile(deckAmmo.draw());
    }

    public void setWeaponCard(SquareSpawn squareSpawn, int needed) {
        while(needed>0){
            squareSpawn.addCard(deckWeapon.draw());
            needed--;
        }
    }

    public void grabFromSquare(int squareIndex){
        map.getSquare(squareIndex).acceptGrab(this);
    }

    public void grabFromSquare(SquareAmmo square){
        Player currentPlayer = playersMap.get(currentTurnPlayer);
        AmmoCard grabbedAmmoTile= square.getAmmoTile();
        if (grabbedAmmoTile!=null) {
            int[] grabbedAmmoContent = grabbedAmmoTile.getAmmoContent();
            //Discard the tile. && Remove the ammo tile.
            deckAmmo.addToDiscarded(grabbedAmmoTile);
            //Move the depicted cubes into your ammo box.
            currentPlayer.addAmmoBox(grabbedAmmoContent);
            //If the tile depicts a powerup card, draw one.
            if (grabbedAmmoContent[3] != 0 && currentPlayer.getPowerupHandSize()<3) currentPlayer.addPowerupCard(deckPowerup.draw());
        }
        setState(new SelectActionState(this));
    }

    public void grabFromSquare(SquareSpawn square){
        setState(new WeaponGrabState(this, square));
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

    public void consumePowerup(PowerupCard powerup){
        playersMap.get(currentTurnPlayer).consumePowerup(powerup);
        deckPowerup.addToDiscarded(powerup);
    }

    public void clearTempAmmo(){
        playersMap.get(currentTurnPlayer).clearTempAmmo();
    }

    public String usePowerup(PowerupCard powerup) {
        return powerup.acceptUse(this);
    }

    public String usePowerup(NewtonPowerup newton){
       return "NOT IMPLEMENTED YET";
    }

    public String usePowerup(ScopePowerup scope){
        setState(new ScopeState(this, scope, damagedThisTurn));
        return "Select the target you want to give extra damage to.";
    }

    public String usePowerup(TeleporterPowerup teleporter){
        setState(new TeleportState(this, teleporter));
        return "Select the square you want to teleport in.";
    }

    public String usePowerup(GrenadePowerup grenade){
        return "You can't use this powerup during your own turn!";
    }

    private String useGrenadePowerup(String userID, PowerupCard powerup) {
        Player user = playersMap.get(userID);
        if(!damagedThisTurn.contains(user.getColor())) return "You have not been damaged during this turn!";
        playersMap.get(currentTurnPlayer).addMarks(user.getColor(), 1);
        removePowerup(powerup);
        return "OK";
    }

    public void removePowerup(PowerupCard puc){
        playersMap.get(currentTurnPlayer).removePowerupCard(puc);
        deckPowerup.addToDiscarded(puc);
    }

    public void reloadWeapon(int weaponID) throws InvalidCardException,AlreadyLoadedException, NotEnoughAmmoException{
        Player currPlayer = playersMap.get(currentTurnPlayer);
        WeaponCard wc = currPlayer.getWeaponCard(weaponID);
        if(wc==null) throw new InvalidCardException();
        if(wc.isLoaded()) throw new AlreadyLoadedException();
        int[] ammoCost = wc.getAmmoCost();
        if(currPlayer.canPayCost(ammoCost)){
            currPlayer.payCost(ammoCost);
            wc.setLoaded(true);
        }else throw new NotEnoughAmmoException();
    }

    public WeaponCard useWeapon(int weaponID){
        WeaponCard wc = playersMap.get(currentTurnPlayer).getWeaponCard(weaponID);
        if(wc == null || !wc.isLoaded()) return null;
        else return wc;
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

    public ArrayList<Player> generateTargets(TargetsGenerator generator, ArrayList<Color> selected){
        Set<Player> targets = new LinkedHashSet<>();
        selected.forEach(x -> targets.add(playersColor.get(x)));
        if(generator==null) return new ArrayList<>(targets);
        ArrayList<Integer> validSqr = new ArrayList<>();
        for(int i=0; i<=map.getMaxSquare(); i++) validSqr.add(i);
        targets.forEach(x -> validSqr.retainAll(generator.generateRange(playersMap.get(currentTurnPlayer).getPosition(), x.getPosition(),map)));
        validSqr.forEach(x -> playersMap.values().stream().filter(y -> y.getPosition() == x).forEach(targets::add));
        return new ArrayList<>(targets);
    }

    public ArrayList<Player> generateTargets(TargetsGenerator generator, int selectedSquare){
        ArrayList<Player> targets = new ArrayList<>();
        generator.generateRange(playersMap.get(currentTurnPlayer).getPosition(), selectedSquare,map)
                .forEach(x -> playersMap.values().stream().filter(y -> y.getPosition() == x).forEach(targets::add));
        return targets;
    }

    public void applyFire(Firemode firemode, List<Player> targets, List<int[]> dmgmrkEachTarget) throws InvalidTargetsException {
        if(!firemode.checkTargets(playersMap.get(currentTurnPlayer), (ArrayList) targets, map)) throw new InvalidTargetsException();

        int kills = 0;
        for(int i=0; i<targets.size(); i++){
            Player target = targets.get(i);
            int[] dmgMrk = dmgmrkEachTarget.get( (i<dmgmrkEachTarget.size() ? i : dmgmrkEachTarget.size()-1) );
            kills = target.applyDamage(playersMap.get(currentTurnPlayer).getColor(), dmgMrk[0], false)
                    ? kills+1 : kills;
            target.addMarks(playersMap.get(currentTurnPlayer).getColor(), dmgMrk[1]);
            if(dmgMrk[0] > 0) damagedThisTurn.add(target.getColor());
        }
        //if kills >= 2 add double kill score to scoreboard
    }

    public void applyExtraDamage(Color color) {
       playersColor.get(color).applyDamage(playersMap.get(currentTurnPlayer).getColor(), 1, true);
       //count kills
    }
}

