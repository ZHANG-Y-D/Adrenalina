package adrenaline.server.controller;

import adrenaline.ClientStub;
import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.GameServer;
import adrenaline.server.exceptions.AlreadyLoadedException;
import adrenaline.server.exceptions.InvalidCardException;
import adrenaline.server.exceptions.NotEnoughAmmoException;
import adrenaline.server.exceptions.WeaponHandFullException;
import adrenaline.server.model.*;
import adrenaline.server.network.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LobbyPlayerTest {

    private static Lobby lobby;
    private static Player player;
    private static Map map;

    @BeforeAll
    static void initLobby(){
        ArrayList<Client> clients = new ArrayList<>();
        clients.add(new ClientStub("client1"));
        clients.add(new ClientStub("client2"));
        clients.add(new ClientStub("client3"));
        lobby = new Lobby(clients, new GameServer());
        lobby.setCurrentTurnPlayer("client1");
        lobby.initCurrentPlayer(new Avatar("SPROG",Color.GREEN),true);
        lobby.setCurrentTurnPlayer("client2");
        lobby.initCurrentPlayer(new Avatar("BANSHEE",Color.BLUE),true);
        lobby.setCurrentTurnPlayer("client3");
        lobby.initCurrentPlayer(new Avatar("VIOLET",Color.PURPLE),true);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(Square.class, new CustomSerializer());
        Gson gson = gsonBld.create();
        map = gson.fromJson(fileReader,Map.class);
        map.setSquaresContext();
        map.setObservers(clients);
        lobby.setMap(map);
        try {
            Method method = Lobby.class.getDeclaredMethod("setMapCards");
            method.setAccessible(true);
            method.invoke(lobby); } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) { }
        player = lobby.getPlayersMap().get("client1");
    }

    @Test
    void playerMoveTest(){
        DeckPowerup deckPowerup = new DeckPowerup();
        PowerupCard powerupCard;
        do {
            powerupCard = deckPowerup.draw();
        } while (powerupCard.getColor() != Color.RED);
        lobby.setCurrentTurnPlayer("client1");
        //test respawn with powerup
        lobby.respawnWithPowerup(powerupCard);
        assertEquals(4,player.getPosition());
        //test try move players
        ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.GREEN));
        ArrayList<Player> result = lobby.tryMovePlayers(colors,5,3);
        assertEquals(1, result.size());

        //test move player
        lobby.movePlayer(6);
        assertEquals(6, player.getPosition());
        lobby.movePlayer(6);
        assertEquals(6,player.getPosition());
        lobby.movePlayer(7,Color.GREEN);
        assertEquals(7, player.getPosition());
        lobby.movePlayer(7,Color.GREEN);
        assertEquals(7,player.getPosition());
    }

    @Test
    void playerGrabTest(){
        lobby.setCurrentTurnPlayer("client1");
        //test grab weapon from map
        DeckWeapon deckWeapon = new DeckWeapon();
        WeaponCard weaponCard;
        int[] cost = new int[]{2,0,0};
        do {
            weaponCard = deckWeapon.draw();
        } while (!Arrays.equals(weaponCard.getAmmoCost(), cost));
        final WeaponCard weaponCard1 = weaponCard;
        cost = new int[]{1,0,0};
        player.payCost(cost);
        assertThrows(NotEnoughAmmoException.class, () -> lobby.grabWeapon(weaponCard1));
        assertTrue(player.getWeaponCards().isEmpty());
        cost = new int[]{3,3,3};
        player.addAmmoBox(cost);
        deckWeapon = new DeckWeapon();
        player.addWeaponCard(deckWeapon.draw());
        player.addWeaponCard(deckWeapon.draw());
        player.addWeaponCard(deckWeapon.draw());
        assertThrows(WeaponHandFullException.class, () -> lobby.grabWeapon(weaponCard1));

        //test swap weapon with a weapon on the square
        WeaponCard weapon = player.getWeaponCards().get(0);
        try { assertEquals(weapon, lobby.swapWeapon(deckWeapon.draw(),weapon.getWeaponID())); } catch (InvalidCardException e) {}
        assertThrows(InvalidCardException.class, () -> lobby.swapWeapon(new DeckWeapon().draw(),50));

        //test grab powerup and ammo from square
        SquareAmmo squareAmmo = (SquareAmmo) map.getSquare(6);
        AmmoCard ammoCard = squareAmmo.getAmmoTile();
        squareAmmo.setAmmoTile(ammoCard);
        int oldSize = player.getPowerupHandSize();
        cost = new int[]{3,3,3};
        player.payCost(cost);
        cost = player.getAmmoBox();
        cost[0] += ammoCard.getAmmoContent()[0];
        cost[1] += ammoCard.getAmmoContent()[1];
        cost[2] += ammoCard.getAmmoContent()[2];
        lobby.grabFromSquare(6);
        if(ammoCard.getAmmoContent()[3] == 1) assertEquals(oldSize+1, player.getPowerupHandSize());
        else assertArrayEquals(cost, player.getAmmoBox());
        player.getWeaponCards().clear();
    }

    @Test
    void playerWeaponTest(){
        lobby.setCurrentTurnPlayer("client1");
        DeckWeapon deckWeapon = new DeckWeapon();
        WeaponCard weaponCard = deckWeapon.draw();

        //test get the firemode from player weapon
        player.addWeaponCard(weaponCard);
        try {
            assertNotNull(lobby.getFiremode(weaponCard.getWeaponID(),0));
            assertNull(lobby.getFiremode(70,0));
        } catch (NotEnoughAmmoException e) { }
        int[] cost = new int[]{3,3,3};
        player.addAmmoBox(cost);
        player.payCost(cost);
        assertThrows(NotEnoughAmmoException.class,() -> {
            DeckWeapon deckWeapon1 = new DeckWeapon();
            WeaponCard weaponCard1;
            do{
                weaponCard1 = deckWeapon1.draw();
            } while (weaponCard1.getWeaponID() != 3);
            player.addWeaponCard(weaponCard1);
            lobby.getFiremode(weaponCard1.getWeaponID(),1);
        });

        //test use wepaon
        assertNull(lobby.useWeapon(70,true));
        assertEquals(weaponCard, lobby.useWeapon(weaponCard.getWeaponID(),true));
        player.getWeaponCard(weaponCard.getWeaponID()).setLoaded(false);
        player.addAmmoBox(cost);
        assertEquals(weaponCard, lobby.useWeapon(weaponCard.getWeaponID(),true));

        //test reload weapon
        assertThrows(InvalidCardException.class, () -> lobby.reloadWeapon(70));
        assertThrows(AlreadyLoadedException.class, () -> lobby.reloadWeapon(weaponCard.getWeaponID()));
        player.addAmmoBox(cost);
        player.payCost(cost);
        player.getWeaponCard(weaponCard.getWeaponID()).setLoaded(false);
        assertThrows(NotEnoughAmmoException.class, () -> lobby.reloadWeapon(weaponCard.getWeaponID()));
        try {
            lobby.reloadWeapon(weaponCard.getWeaponID());
            assertTrue(player.getWeaponCard(weaponCard.getWeaponID()).isLoaded());
        } catch (InvalidCardException | AlreadyLoadedException | NotEnoughAmmoException e) { }

        player.getWeaponCards().clear();
    }
}
