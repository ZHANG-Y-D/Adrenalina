package adrenaline.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.ArrayList;

import adrenaline.server.model.constraints.*;

import static org.junit.jupiter.api.Assertions.*;

class FiremodeTest {

    private static Map map;

    @BeforeAll
    static void initMap(){
        try {
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");

            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(Square.class, new CustomDeserializer());
            Gson gson = gsonBld.create();
            map = gson.fromJson(fileReader,Map.class);
        }catch(Exception e){System.out.println("ERROR!");}
    }

    @Test
    void getRangeTest() {

        //lock rifle used as reference for range constraints
        ArrayList<RangeConstraint> rngConstList = new ArrayList<>();
        RangeConstraint rngConst = new InSightConstraint();
        rngConstList.add(rngConst);
        Firemode firemode = new Firemode("",new int[]{0,0,0}, 0, null, rngConstList, null, null);

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(4);
        expected.add(5);
        expected.add(6);
        assertEquals(expected, firemode.getRange(0,map));

        // flamethrower used as reference for range constraints
        rngConstList = new ArrayList<>();
        rngConst = new CardinalDirectionConstraint();
        rngConstList.add(rngConst);
        rngConst = new InRadiusConstraint(2);
        rngConstList.add(rngConst);
        rngConst = new ExcRadiusConstraint(0);
        rngConstList.add(rngConst);
        firemode = new Firemode("",new int[]{0,0,0}, 0,null, rngConstList, null, null);

        expected = new ArrayList<>();
        expected.add(4);
        expected.add(6);
        expected.add(7);
        expected.add(9);
        assertEquals(expected, firemode.getRange(5,map));

        expected = new ArrayList<>();
        expected.add(0);
        expected.add(5);
        expected.add(6);
        assertEquals(expected, firemode.getRange(4,map));

        //whisper used as reference for range constraints
        rngConstList = new ArrayList<>();
        rngConst = new InSightConstraint();
        rngConstList.add(rngConst);
        rngConst = new ExcRadiusConstraint(1);
        rngConstList.add(rngConst);
        firemode = new Firemode("", new int[]{0,0,0}, 0, null, rngConstList, null, null);

        expected = new ArrayList<>();
        expected.add(2);
        expected.add(5);
        expected.add(6);
        assertEquals(expected, firemode.getRange(0,map));

        expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(6);
        assertEquals(expected, firemode.getRange(4,map));

        //electroscythe used as reference for range constraints
        rngConstList = new ArrayList<>();
        rngConst = new InRadiusConstraint(0);
        rngConstList.add(rngConst);
        firemode = new Firemode("", new int[]{0,0,0}, 0, null, rngConstList, null, null);

        expected = new ArrayList<>();
        expected.add(5);
        assertEquals(expected, firemode.getRange(5,map));

        //heatseeker used as reference for range constraints
        rngConstList = new ArrayList<>();
        rngConst = new ExcSightConstraint();
        rngConstList.add(rngConst);
        firemode = new Firemode("", new int[]{0,0,0}, 0, null, rngConstList, null, null);

        expected = new ArrayList<>();
        expected.add(7);
        expected.add(9);
        expected.add(10);
        expected.add(11);
        assertEquals(expected, firemode.getRange(0,map));

        expected = new ArrayList<>();
        expected.add(4);
        expected.add(5);
        expected.add(6);
        expected.add(7);
        expected.add(9);
        expected.add(10);
        expected.add(11);
        assertEquals(expected, firemode.getRange(1,map));

        expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(7);
        expected.add(11);
        assertEquals(expected, firemode.getRange(5,map));

        //furnace used as reference for range constraints
        rngConstList = new ArrayList<>();
        rngConst = new ExcRoomConstraint();
        rngConstList.add(rngConst);
        rngConst = new InSightConstraint();
        rngConstList.add(rngConst);
        firemode = new Firemode("", new int[]{0,0,0}, 0, null, rngConstList, null, null);

        expected = new ArrayList<>();
        expected.add(4);
        expected.add(5);
        expected.add(6);
        assertEquals(expected, firemode.getRange(0,map));

        expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(7);
        expected.add(11);
        assertEquals(expected, firemode.getRange(6,map));
    }
}