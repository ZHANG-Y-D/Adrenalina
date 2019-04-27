/*

    Responsible：ZHANG YUEDONG


 */



package server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DeckPowerupTest {



    @Test
    void PrintDeckPowerup() {

        DeckPowerup deckPowerup = new DeckPowerup();
        System.out.print((deckPowerup.toString()));

        //Test total
        assertEquals(12,deckPowerup.cardsDeck.size());


    }


}
