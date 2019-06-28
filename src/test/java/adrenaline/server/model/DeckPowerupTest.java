
package adrenaline.server.model;

import adrenaline.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeckPowerupTest {

    private static DeckPowerup deckPowerup;

    @BeforeAll
    static void initDeckPowerup(){
        deckPowerup = new DeckPowerup();
    }

    @Test
    void deckPowerupBuildTest() {

        //Test cards total
        assertEquals(24,deckPowerup.cards.size());

        //Test deck contains all index
        ArrayList<Integer> index = new ArrayList<>();
        for(int i = 1; i <= deckPowerup.cards.size(); i++) index.add(i);
        for(PowerupCard pwc : deckPowerup.cards){
            assertTrue(index.contains(pwc.getPowerupID()));
        }

        //Test deck contains all colors
        ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.RED,Color.YELLOW,Color.BLUE));
        for(PowerupCard pwc : deckPowerup.cards){
            colors.removeIf(x -> x.equals(pwc.getColor()));
        }
        assertEquals(0, colors.size());
    }

    @Test
    void isUsableOutsideTurnTest(){
        for(PowerupCard pwc : deckPowerup.cards){
            if (pwc.getPowerupID() > 0 && pwc.getPowerupID() < 7) assertTrue(pwc.isUsableOutsideTurn());
            else assertFalse(pwc.isUsableOutsideTurn());
        }
    }

    /*@Test
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
    }*/

}
