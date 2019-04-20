package server.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmmoCardTest {

    @Test
    void ConstructorTest() {

        int[] a= new int[]{0,1,1,0};
        int b = 5;
        AmmoCard ammoCard = new AmmoCard(new int[]{0,1,1,0},5);
        System.out.println(Arrays.toString(ammoCard.getAmmoContent())+ ' ' +ammoCard.getNumAmmoCard());

        assertArrayEquals(a,ammoCard.getAmmoContent());
        assertEquals(b,ammoCard.getNumAmmoCard());

    }
}
