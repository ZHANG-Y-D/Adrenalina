package server.model;

import server.model.constraints.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class FiremodeTest {

    @Test
    public void getRangeTest() {
        // flamethrower used as reference for range constraints
        ArrayList<RangeConstraint> rngConstList = new ArrayList<>();
        RangeConstraint rngConst = new CardinalDirectionConstraint();
        rngConstList.add(rngConst);
        rngConst = new InRadiusConstraint(2);
        rngConstList.add(rngConst);
        rngConst = new ExcRadiusConstraint(0);
        rngConstList.add(rngConst);
        Firemode firemode = new Firemode(0, 0, null, rngConstList, null, null);

        Map map = new Map(1,3,4);

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(4);
        expected.add(6);
        expected.add(7);
        expected.add(9);

        assertEquals(expected, firemode.getRange(5,map));

    }
}