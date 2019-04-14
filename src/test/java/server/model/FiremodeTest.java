package Server.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import server.model.constraints.*;

import static org.junit.jupiter.api.Assertions.*;

class FiremodeTest {

    @Test
    void getRangeTest() {
        Map map = new Map(1,3,4);

        // flamethrower used as reference for range constraints
        ArrayList<RangeConstraint> rngConstList = new ArrayList<>();
        RangeConstraint rngConst = new CardinalDirectionConstraint();
        rngConstList.add(rngConst);
        rngConst = new InRadiusConstraint(2);
        rngConstList.add(rngConst);
        rngConst = new ExcRadiusConstraint(0);
        rngConstList.add(rngConst);
        Firemode firemode = new Firemode(0, 0, null, rngConstList, null, null);

        ArrayList<Integer> expected = new ArrayList<>();
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

        //lock rifle used as reference for range constraints
        rngConstList = new ArrayList<>();
        rngConst = new SightConstraint();
        rngConstList.add(rngConst);
        firemode = new Firemode(0, 0, null, rngConstList, null, null);

        expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(4);
        expected.add(5);
        expected.add(6);
        assertEquals(expected, firemode.getRange(0,map));

        //whisper used as reference for range constraints
        rngConstList = new ArrayList<>();
        rngConst = new SightConstraint();
        rngConstList.add(rngConst);
        rngConst = new ExcRadiusConstraint(1);
        rngConstList.add(rngConst);
        firemode = new Firemode(0, 0, null, rngConstList, null, null);

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
    }
}