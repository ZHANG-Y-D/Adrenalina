package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.CustomSerializer;
import adrenaline.server.controller.states.FiremodeSubState;
import adrenaline.server.model.constraints.*;
import adrenaline.server.network.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FiremodeTest {

    private static Map map;
    private static Firemode firemode1;
    private static Firemode firemode2;

    @BeforeAll
    static void initMap() {
        try {
            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(Square.class, new CustomSerializer())
                    .registerTypeAdapter(RangeConstraint.class, new CustomSerializer())
                    .registerTypeAdapter(TargetsConstraint.class, new CustomSerializer())
                    .registerTypeAdapter(FiremodeSubState.class, new CustomSerializer())
                    .registerTypeAdapter(TargetsGenerator.class, new CustomSerializer());;
            Gson gson = gsonBld.create();

            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");
            map = gson.fromJson(fileReader, Map.class);

            fileReader = new FileReader("src/test/testResource/testJsonsrc/TestWeaponCard.json");
            WeaponCard weaponCard = gson.fromJson(fileReader, WeaponCard.class);
            firemode1 = weaponCard.getFiremode(0);
            firemode2 = weaponCard.getFiremode(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void extraCostTest(){
        int[] expected;
        expected = new int[]{0,0,0};
        assertArrayEquals(firemode1.getExtraCost(), expected);
        expected = new int[]{0,1,0};
        assertArrayEquals(firemode2.getExtraCost(), expected);
    }

    @Test
    void moveSelfStateTest(){
        assertNull(firemode1.getMoveSelfStep());
        assertNotNull(firemode2.getMoveSelfStep());
    }

    @Test
    void checkTargetsTest(){
        Avatar avatar = new Avatar("A", Color.YELLOW);
        Player shooter = new Player(avatar, "B", new ArrayList<>());
        shooter.setPosition(0);
        ArrayList<Player> targets = new ArrayList<>();
        targets.add(new Player(avatar,"C", new ArrayList<>()));
        targets.add(new Player(avatar,"D", new ArrayList<>()));
        targets.add(new Player(avatar,"E", new ArrayList<>()));
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(4);
        targets.get(2).setPosition(5);
        assertTrue(firemode1.checkTargets(shooter,targets,map));
        targets.get(0).setPosition(0);
        assertFalse(firemode1.checkTargets(shooter,targets,map));

        shooter.setPosition(1);
        targets.get(0).setPosition(2);
        targets.get(1).setPosition(2);
        targets.get(2).setPosition(0);
        assertFalse(firemode2.checkTargets(shooter,targets,map));
    }

}