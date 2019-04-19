package server.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class AmmoCardTest {

    @Test
    void getAmmoContent() {

        int[] a;
        AmmoCard ammoCard = new AmmoCard("rbb",2);
        System.out.println(Arrays.toString(ammoCard.getAmmoContent()));

        a=new int[]{1,2,0,0};
        assertArrayEquals(a,ammoCard.getAmmoContent());
    }
}
