package server.model.constraints;

import org.junit.jupiter.api.Test;
import server.controller.PlayerShell;
import server.model.Color;
import server.model.Map;
import server.model.PlayerCore;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TargetsConstraintTest {
    @Test
    void checkConstTest(){
        Map map = new Map(1,3,4);
        PlayerShell Tester = new PlayerShell("Tester", Color.BLACK ,1);
        PlayerCore shooter = new PlayerCore(Tester);
        PlayerShell Red= new PlayerShell("Red",Color.RED,1);
        PlayerShell Yellow= new PlayerShell("Yellow",Color.YELLOW,1);
        PlayerShell Blue= new PlayerShell("Blue",Color.BLUE,1);

        /* Different Squares */
        TargetsConstraint constraint = new DifferentSquaresConstraint();
        ArrayList<PlayerCore> targets = new ArrayList<>();
        targets.add(new PlayerCore(Red));
        targets.add(new PlayerCore(Yellow));
        targets.add(new PlayerCore(Blue));
        targets.get(0).setPosition(0);
        targets.get(1).setPosition(7);
        targets.get(2).setPosition(11);
        shooter.setPosition(0);
        assertTrue(constraint.checkConst(shooter, targets, map));
        targets.get(0).setPosition(0);
        targets.get(1).setPosition(7);
        targets.get(2).setPosition(7);
        assertFalse(constraint.checkConst(shooter, targets, map));
        /*Same Square */
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

        Yellow= new PlayerShell("Yellow",Color.YELLOW,1);
        Blue= new PlayerShell("Blue",Color.BLUE,1);
        targets.add(new PlayerCore(Yellow));
        targets.add(new PlayerCore(Blue));
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
        shooter.setOldPosition(4);
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
        shooter.setOldPosition(1);
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