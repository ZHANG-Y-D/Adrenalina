package adrenaline.server.model.constraints;

import adrenaline.CustomSerializer;
import adrenaline.server.model.Map;
import adrenaline.server.model.Square;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TargetsGeneratorTest {
    private static Map map;
    private static TargetsGenerator generator;
    private static ArrayList<Integer> expected;

    @BeforeAll
    static void initMap(){
        try{
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");

            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(Square.class, new CustomSerializer());
            Gson gson = gsonBld.create();
            map = gson.fromJson(fileReader,Map.class);

        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard.json file not found");
        }
        expected = new ArrayList<>();
    }

    @Test
    void generateTest(){
        ArrayList<Integer> actual;

        /* In Radius */
        generator = new InRadiusConstraint(1);
        expected.add(4);
        expected.add(6);
        expected.add(9);
        actual = generator.generateRange(5,5,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));

        /* Same Direction */
        generator = new SameDirectionConstraint();
        expected.clear();
        expected.add(5);
        expected.add(6);
        expected.add(7);
        actual = generator.generateRange(4,5,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));
        expected.clear();
        expected.add(4);
        expected.add(5);
        actual = generator.generateRange(6,5,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));

        /* Same Room */
        generator = new SameRoomConstraint();
        expected.clear();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        actual = generator.generateRange(4,0,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));

        /* Same Square */
        generator = new SameSquareConstraint();
        expected.clear();
        expected.add(5);
        actual = generator.generateRange(4,5,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));
    }
}
