package adrenaline.server.model;
import adrenaline.ClientStub;
import adrenaline.CustomSerializer;
import adrenaline.server.GameServer;
import adrenaline.server.controller.Lobby;
import adrenaline.server.network.Client;
import adrenaline.server.network.ClientRMIWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    private static Map map;
    private static Lobby lobby;

    @BeforeAll
    static void init() {
        try {
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");

            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(Square.class, new CustomSerializer());
            Gson gson = gsonBld.create();
            map = gson.fromJson(fileReader, Map.class);
            ArrayList<Client> clients = new ArrayList<>();
            clients.add(new ClientStub("test"));
            map.setObservers(clients);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*ArrayList<Client> clients = new ArrayList<>();
        clients.add(new ClientStub());
        lobby = new Lobby(clients, new GameServer());*/
    }

    @Test
    void setCardTest(){
        /*SquareAmmo squareAmmo = (SquareAmmo) map.getSquare(0);
        DeckAmmo deckAmmo = new DeckAmmo();
        AmmoCard ammoCard = deckAmmo.draw();
        squareAmmo.setAmmoTile(ammoCard);
        assertEquals(ammoCard, squareAmmo.getAmmoTile());*/
    }

}