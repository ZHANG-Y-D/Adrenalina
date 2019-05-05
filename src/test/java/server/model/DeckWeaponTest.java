package server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckWeaponTest {

    @Test
    void PrintDeckWeapon() {

        DeckWeapon deckWeapon =new DeckWeapon();
        System.out.print((deckWeapon.toString()));

        //Test total
        assertEquals(21,deckWeapon.getCardsDeck().size());

    }

    @Test
    void DrawTest(){

        DeckWeapon deckWeapon = new DeckWeapon();
        for (int i=0;i<=100;i++)
            System.out.println(deckWeapon.draw().toString());

    }
}
