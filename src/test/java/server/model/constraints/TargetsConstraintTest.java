package server.model.constraints;

import org.junit.jupiter.api.Test;
import server.model.Map;
import server.model.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TargetsConstraintTest {
    @Test
    void checkConstTest(){
        Map map = new Map(1,3,4);
        /* Different Squares */
        TargetsConstraint constraint = new DifferentSquaresConstraint();
        ArrayList<Player> targets = new ArrayList<>();
        targets.add(new Player("Red", 'R'));
        targets.add(new Player("Yellow", 'Y'));
        targets.add(new Player("Blue", 'B'));
        targets.get(0).setPosition(0);
        targets.get(1).setPosition(7);
        targets.get(2).setPosition(11);
        assertTrue(constraint.checkConst(targets, map));
        targets.get(0).setPosition(0);
        targets.get(1).setPosition(7);
        targets.get(2).setPosition(7);
        assertFalse(constraint.checkConst(targets, map));
        /*Same Square */
        constraint = new SameSquareConstraint();
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(9);
        assertFalse(constraint.checkConst(targets, map));
        targets.get(0).setPosition(5);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(5);
        assertTrue(constraint.checkConst(targets, map));
        /* Same Direction */
        constraint = new SameDirectionConstraint();
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(9);
        assertTrue(constraint.checkConst(targets, map));
        targets.get(0).setPosition(1);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(10);
        assertFalse(constraint.checkConst(targets, map));
        targets.get(0).setPosition(9);
        targets.get(1).setPosition(1);
        targets.get(2).setPosition(1);
        assertTrue(constraint.checkConst(targets, map));
        targets.remove(1);
        targets.remove(1);
        assertTrue(constraint.checkConst(targets, map));
        targets.add(new Player("Yellow", 'Y'));
        targets.add(new Player("Blue", 'B'));
        /* Same Room */
        constraint = new SameRoomConstraint();
        targets.get(0).setPosition(4);
        targets.get(1).setPosition(6);
        targets.get(2).setPosition(6);
        assertTrue(constraint.checkConst(targets, map));
        targets.get(0).setPosition(4);
        targets.get(1).setPosition(6);
        targets.get(2).setPosition(7);
        assertFalse(constraint.checkConst(targets, map));
        /* Thor */
        constraint = new ThorConstraint();
        targets.get(0).setPosition(2);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(9);
        assertTrue(constraint.checkConst(targets, map));
        targets.get(0).setPosition(2);
        targets.get(1).setPosition(5);
        targets.get(2).setPosition(11);
        assertFalse(constraint.checkConst(targets, map));
        targets.get(0).setPosition(2);
        targets.get(1).setPosition(9);
        targets.get(2).setPosition(11);
        assertFalse(constraint.checkConst(targets, map));

    }
}