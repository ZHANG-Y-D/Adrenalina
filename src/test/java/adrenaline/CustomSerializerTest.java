package adrenaline;

import adrenaline.server.model.Map;
import adrenaline.server.model.Square;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

public class CustomSerializerTest {

    @Test
    void serializerTest(){
        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(Square.class, new CustomSerializer());
        Gson gson = gsonBld.create();
        try {
            JsonReader fileReader = new JsonReader(new FileReader("src/main/resources/Jsonsrc/Map1.json"));
            Map deserializedMap = gson.fromJson(fileReader, Map.class);
            String serializedMap = gson.toJson(deserializedMap);
            System.out.println(serializedMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void messageSerializeTest(){
        GsonBuilder gsonBld = new GsonBuilder();
        gsonBld.registerTypeAdapter(UpdateMessage.class, new CustomSerializer());
        Gson gson = gsonBld.create();
        MapUpdateMessage msg = new MapUpdateMessage(new Map());
        String jsonMsg = gson.toJson(msg, UpdateMessage.class);
        System.out.println(jsonMsg);
    }
}
