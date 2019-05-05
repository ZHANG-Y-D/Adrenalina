package server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckAmmoTest {

    @Test
    void PrintDeckAmmo() {


        DeckAmmo deckAmmo = new DeckAmmo();
        System.out.println(deckAmmo.toString());


        //Test total
        assertEquals(deckAmmo.getCardsDeck().size(),36);


        //Test the first element
        int[] num= new int[]{0,2,1,0};
        assertArrayEquals(num,deckAmmo.getCardsDeck().get(0).getAmmoContent());
        assertEquals(2,deckAmmo.getCardsDeck().get(0).getNumAmmoCard());


    }


}
