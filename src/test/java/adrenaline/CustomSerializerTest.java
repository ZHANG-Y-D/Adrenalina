package adrenaline;

import adrenaline.server.model.Map;
import adrenaline.server.model.Square;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
