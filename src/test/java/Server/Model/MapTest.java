package Server.Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    private static Map map;

    @BeforeAll
    static void initMap(){
        map = new Map(1,3,4);
    }

    @Test
    void testGetValidSquares() {
        ArrayList<Integer> validSquares = new ArrayList<>();
        validSquares = map.getValidSquares(0,2);
        ArrayList<Integer> tempList = new ArrayList<Integer>(Arrays.asList(0, 1, 4, 2, 5));
        assertTrue(validSquares.equals(tempList));
    }

    @Test
    void testIsWall() {

    }
}