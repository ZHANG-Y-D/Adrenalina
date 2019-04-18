package server.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.model.constraints.InSightConstraint;

import static org.junit.jupiter.api.Assertions.*;

class MovementEffectTest {
    private static MovementEffect movementEffect;

    @BeforeAll
    static void testMovementEffect(){
        movementEffect = new MovementEffect(2, false, false, MovementEffect.Timing.PRE);
    }

    @Test
    void getMovementRangeTest(){
        assertEquals(2, movementEffect.getMovementRange());
    }
    @Test
    void isSelfTest(){
        assertEquals(false, movementEffect.isSelf());
    }
    @Test
    void getTimingTest(){
        assertEquals(MovementEffect.Timing.PRE, movementEffect.getTiming());
    }
}