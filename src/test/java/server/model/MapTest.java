package server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    void getSquare() {
    }

    @Test
    void getValidSquares() {
    }

    @Test
    void isWall() {

    }

    @Test
    void getRoomSquaresTest(){
        Map map = new Map(1,3,4);
        ArrayList<Integer> expected;

        expected = new ArrayList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        assertEquals(expected,map.getRoomSquares(0));
        assertEquals(expected,map.getRoomSquares(1));
        assertEquals(expected,map.getRoomSquares(2));

        expected = new ArrayList<>();
        expected.add(4);
        expected.add(5);
        expected.add(6);
        assertEquals(expected,map.getRoomSquares(4));
        assertEquals(expected,map.getRoomSquares(5));
        assertEquals(expected,map.getRoomSquares(6));

        expected = new ArrayList<>();
        expected.add(7);
        expected.add(11);
        assertEquals(expected,map.getRoomSquares(7));
        assertEquals(expected,map.getRoomSquares(11));

        expected = new ArrayList<>();
        expected.add(9);
        expected.add(10);
        assertEquals(expected,map.getRoomSquares(9));
        assertEquals(expected,map.getRoomSquares(10));
    }
}