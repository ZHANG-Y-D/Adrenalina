package adrenaline.server.model;
import adrenaline.CustomSerializer;
import adrenaline.server.controller.Lobby;
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

    @BeforeAll
    static void init() {
        try {
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");

            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(Square.class, new CustomSerializer());
            Gson gson = gsonBld.create();
            map = gson.fromJson(fileReader, Map.class);
            map.setObservers(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}