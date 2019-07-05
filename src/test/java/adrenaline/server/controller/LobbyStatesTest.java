package adrenaline.server.controller;

import adrenaline.ClientStub;
import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.GameServer;
import adrenaline.server.controller.states.SelectActionState;
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

import static org.junit.jupiter.api.Assertions.*;

class LobbyStatesTest {

    private static Lobby lobby;
    private static Player player1;
    private static Map map;

    @BeforeAll
    static void initLobby() {
        ArrayList<Client> clients = new ArrayList<>();
        clients.add(new ClientStub("client1"));
        clients.add(new ClientStub("client2"));
        clients.add(new ClientStub("client3"));
        lobby = new Lobby(clients, new GameServer());
        lobby.setCurrentTurnPlayer("client1");
        lobby.initCurrentPlayer(new Avatar("SPROG", Color.GREEN), true);
        lobby.setCurrentTurnPlayer("client2");
        lobby.initCurrentPlayer(new Avatar("BANSHEE", Color.BLUE), true);
        lobby.setCurrentTurnPlayer("client3");
        lobby.initCurrentPlayer(new Avatar("VIOLET", Color.PURPLE), true);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(Square.class, new CustomSerializer());
        Gson gson = gsonBld.create();
        map = gson.fromJson(fileReader, Map.class);
        map.setSquaresContext();
        map.setObservers(clients);
        lobby.setMap(map);
        try {
            Method method = Lobby.class.getDeclaredMethod("setMapCards");
            method.setAccessible(true);
            method.invoke(lobby);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        }
        player1 = lobby.getPlayersMap().get("client1");
    }

    @Test
    void changeStateTest(){
        lobby.setCurrentTurnPlayer("client1");
        lobby.setState(new SelectActionState(lobby));
        assertEquals("OK Select the square you want to move in", lobby.runAction("client1"));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("OK Select something to grab", lobby.grabAction("client1"));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("OK Select a weapon or a powerup to gain ammo", lobby.shootAction("client1"));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("OK Select a weapon to reload", lobby.endOfTurnAction("client1"));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("Select an action!", lobby.selectPlayers("client1",new ArrayList<>()));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("Select an action!", lobby.selectSquare("client1",0));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("Select an action!", lobby.selectWeapon("client1",0));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("Select an action!", lobby.selectFiremode("client1",0));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("Select an action!", lobby.selectAmmo("client1",Color.BLUE));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("Select an action!", lobby.moveSubAction("client1"));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("KO", lobby.selectFinalFrenzyAction("client1",0));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("You can't go back now!", lobby.goBack("client1"));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("KO", lobby.selectAvatar("client1", Color.BLUE));
        lobby.setState(new SelectActionState(lobby));
        assertEquals("KO", lobby.selectSettings("client1",0,0));
    }

}