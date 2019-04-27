package server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckWeaponTest {

    @Test
    void PrintDeckWeapon() {

        DeckWeapon deckWeapon =new DeckWeapon();
        System.out.print((deckWeapon.toString()));

        //Test total
        assertEquals(21,deckWeapon.cardsDeck.size());

    }
}
