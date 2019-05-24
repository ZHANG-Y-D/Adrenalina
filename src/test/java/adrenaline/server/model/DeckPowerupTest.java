
package adrenaline.server.model;

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
        assertEquals(24,deckPowerup.cards.size());

    }

    @Test
    void DrawAndDiscardTest(){

        int numDraw=100;
        DeckPowerup deckPowerup = new DeckPowerup();
        assertEquals(24,deckPowerup.cards.size());
        PowerupCard powerupCard;
        for (int i=0;i<numDraw;i++) {
            powerupCard = deckPowerup.draw();
            System.out.println(powerupCard.toString());
            deckPowerup.addToDiscarded(powerupCard);
            System.out.println(deckPowerup.cards.size());
            System.out.println(deckPowerup.discarded.size());
            assertEquals(24,deckPowerup.cards.size()+deckPowerup.discarded.size());
        }
    }

}
