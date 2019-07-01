package adrenaline.server.model.constraints;

import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.model.Avatar;
import adrenaline.server.model.Map;
import adrenaline.server.model.Player;
import adrenaline.server.model.Square;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RangeConstraintTest {
    private static Map map;
    private static ArrayList<Integer> expected ;

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
    void checkConstTest(){
        ArrayList<Integer> actual;

        /* Sight */
        RangeConstraint constraint = new  InSightConstraint();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(4);
        expected.add(5);
        expected.add(6);
        actual = constraint.checkConst(0,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));

        /* Cardinal Direction */
        constraint = new CardinalDirectionConstraint();
        expected.clear();
        expected.add(1);
        expected.add(4);
        expected.add(5);
        expected.add(6);
        expected.add(7);
        expected.add(9);
        actual = constraint.checkConst(5,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));

        /* Exclude radius */
        constraint = new ExcRadiusConstraint(1);
        expected.clear();
        expected.add(2);
        expected.add(5);
        expected.add(6);
        expected.add(7);
        expected.add(9);
        expected.add(10);
        expected.add(11);
        actual = constraint.checkConst(0,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));

        /* In radius */
        constraint = new InRadiusConstraint(1);
        expected.clear();
        expected.add(4);
        expected.add(5);
        expected.add(6);
        expected.add(9);
        actual = constraint.checkConst(5,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));

        /* Exclude Sight */
        constraint = new ExcSightConstraint();
        expected.clear();
        expected.add(7);
        expected.add(9);
        expected.add(10);
        expected.add(11);
        actual = constraint.checkConst(0,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));

        /* Exclude Room */
        constraint = new ExcRoomConstraint();
        expected.clear();
        expected.add(4);
        expected.add(5);
        expected.add(6);
        expected.add(7);
        expected.add(9);
        expected.add(10);
        expected.add(11);
        actual = constraint.checkConst(0,map);
        assertTrue(expected.size()==actual.size() && actual.containsAll(expected));
    }

}
