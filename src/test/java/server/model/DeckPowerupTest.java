
package server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 *
 * Responsible：ZHANG YUEDONG
 *
 *
 */



public class DeckPowerupTest {



    @Test
    void PrintDeckPowerup() {

        DeckPowerup deckPowerup = new DeckPowerup();
        System.out.print((deckPowerup.toString()));

        //Test total
        assertEquals(12,deckPowerup.getCardsDeck().size());


    }

    @Test
    void DrawTest(){

        DeckPowerup deckPowerup = new DeckPowerup();
        for (int i=0;i<=50;i++)
            System.out.println(deckPowerup.draw().toString());

    }


}
