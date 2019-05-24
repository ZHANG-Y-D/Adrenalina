package adrenaline.server.model.constraints;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TargetsConstraintTest {

    private static Map map;

    @BeforeAll
    static void initMap(){
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map1.json");

            map = gson.fromJson(fileReader,Map.class);

        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard.json file not found");
        }
    }

    @Test
    void checkConstTest(){

        Lobby lobby = new Lobby(null);
        Player shooter = new Player("Anna", Color.YELLOW,lobby);


        /* Different Squares */
        TargetsConstraint constraint = new DifferentSquaresConstraint();
        ArrayList<Player> targets = new ArrayList<>();
        targets.add(new Player("Bob",Color.RED,lobby));
        targets.add(new Player("Ceci",Color.WHITE,lobby));
        targets.add(new Player("Devie", Color.BLUE,lobby));
        targets.get(0).setPosition(0);
        targets.get(1).setPosition(7);
        targets.get(2).setPosition(11);
        shooter.setPosition(0);
        assertTrue(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(0);
        targets.get(1).setPosition(7);
        targets.get(2).setPosition(7);
        assertFalse(constraint.checkConst(shooter, targets, map));
        /*Same SquareAmmoTest */
        constraint = new SameSquareConstraint();
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(9);
        assertFalse(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(5);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(5);
        assertTrue(constraint.checkConst(shooter, targets, map));
        /* Same Direction */
        constraint = new SameDirectionConstraint();
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(9);
        shooter.setPosition(1);
        assertTrue(constraint.checkConst(shooter, targets, map));
        shooter.setPosition(5);
        assertFalse(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(10);
        shooter.setPosition(1);
        assertFalse(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(9);
        targets.get(1).setPosition(1);
        targets.get(2).setPosition(1);
        assertTrue(constraint.checkConst(shooter, targets, map));
        targets.remove(1);
        targets.remove(1);
        assertTrue(constraint.checkConst(shooter, targets, map));


        targets.add(new Player("Emila", Color.BLACK,lobby));
        targets.add(new Player("Frank",Color.PURPLE,lobby));
        /* Same Room */
        constraint = new SameRoomConstraint();
        targets.get(0).setPosition(4);
        targets.get(1).setPosition(6);
        targets.get(2).setPosition(6);
        shooter.setPosition(0);
        assertTrue(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(4);
        targets.get(1).setPosition(6);
        targets.get(2).setPosition(7);
        assertFalse(constraint.checkConst(shooter, targets, map));
        /* Trajectory */
        constraint = new ChargeConstraint();
        shooter.setPosition(4);
        shooter.setPosition(6);
        targets.get(0).setPosition(4);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(6);
        assertTrue(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(4);
        targets.get(1).setPosition(6);
        targets.get(2).setPosition(7);
        assertFalse(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(2);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(6);
        assertFalse(constraint.checkConst(shooter, targets, map));
        shooter.setPosition(1);
        shooter.setPosition(9);
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(9);
        assertTrue(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(4);
        targets.get(2).setPosition(9);
        assertFalse(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(10);
        assertFalse(constraint.checkConst(shooter, targets, map));
        /* Thor */
        constraint = new ThorConstraint();
        targets.get(0).setPosition(2);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(9);
        assertTrue(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(2);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(11);
        assertFalse(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(2);
        targets.get(1).setPosition(9);
        targets.get(2).setPosition(11);
        assertFalse(constraint.checkConst(shooter, targets, map));

    }
}