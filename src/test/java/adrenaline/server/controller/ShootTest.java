package adrenaline.server.controller;

import adrenaline.ClientStub;
import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.GameServer;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.*;
import adrenaline.server.model.constraints.SameSquareConstraint;
import adrenaline.server.model.constraints.TargetsGenerator;
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

class ShootTest {

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
    void targetsTest(){
        //test generate the target from given players
        Player player2 = lobby.getPlayersMap().get("client2");
        Player player3 = lobby.getPlayersMap().get("client3");
        player1.setPosition(4);
        player2.setPosition(4);
        player3.setPosition(4);
        TargetsGenerator generator = null;
        ArrayList<Color> targetsColor = new ArrayList<>(Arrays.asList(Color.BLUE));
        ArrayList<Player> targets = new ArrayList<>(Arrays.asList(player1,player2,player3));
        assertEquals(player2,lobby.generateTargets(generator,targetsColor).get(0));
        generator = new SameSquareConstraint();
        assertEquals(targets.size(), lobby.generateTargets(generator,targetsColor).size());

        //test generate the target from given square index
        assertEquals(targets.size(), lobby.generateTargets(generator,4).size());
    }

    @Test
    void applyFireTest(){
        Player player2 = lobby.getPlayersMap().get("client2");
        Player player3 = lobby.getPlayersMap().get("client3");
        DeckWeapon deckWeapon = new DeckWeapon();
        WeaponCard weaponCard;
        do{
            weaponCard = deckWeapon.draw();
        }while (weaponCard.getWeaponID() != 12);
        player1.addWeaponCard(weaponCard);
        player1.setPosition(4);
        player2.setPosition(4);
        player3.setPosition(4);
        Firemode firemode = player1.getWeaponCard(weaponCard.getWeaponID()).getFiremode(0);
        ArrayList<int[]> damageList = new ArrayList<>();
        int[] damage1 = new int[]{1,0};
        int[] damage2 = new int[]{1,0};
        damageList.add(damage1);
        damageList.add(damage2);
        assertThrows(InvalidTargetsException.class, () -> lobby.applyFire(firemode,new ArrayList<Player>(), damageList));
        ArrayList<Player> targets = new ArrayList<>(Arrays.asList(player1,player2,player3));
        int player2damage = player2.getDamageTrack().size();
        try {
            lobby.applyFire(firemode,targets,damageList);
            assertEquals(player2damage + 1, player2.getDamageTrack().size());
        } catch (InvalidTargetsException e) { }
    }
}
