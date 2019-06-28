package adrenaline.server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void deckTest(){
        DeckPowerup deck = new DeckPowerup();

        //Test shuffle
        ArrayList<PowerupCard> testDeck = new ArrayList<>(deck.cards);
        deck.shuffle();
        assertTrue(testDeck.containsAll(deck.cards));
        assertNotEquals(testDeck, deck.cards);

        //Test draw
        int size = deck.cards.size();
        PowerupCard card = deck.cards.get(size-1);
        deck.draw();
        assertEquals(size - 1 , deck.cards.size());
        assertNotEquals(card,deck.cards.get(deck.cards.size()-1));

        //Test add to discarded
        deck.addToDiscarded(card);
        assertEquals(card,deck.discarded.get(0));

        //Test refill deck
        size = deck.cards.size();
        deck.addToDiscarded(deck.cards.get(2));
        for(int i = 0; i < size; i++) deck.draw();
        assertEquals(0, deck.cards.size());
        deck.draw();
        assertFalse(deck.cards.isEmpty());
        assertEquals(0,deck.discarded.size());
    }
}