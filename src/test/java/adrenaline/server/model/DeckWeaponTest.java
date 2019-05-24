package adrenaline.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckWeaponTest {

    @Test
    void PrintDeckWeapon() {

        DeckWeapon deckWeapon =new DeckWeapon();
        System.out.print((deckWeapon.toString()));

        //Test total
        assertEquals(21,deckWeapon.cards.size());

    }

    @Test
    void DrawTest(){

        DeckWeapon deckWeapon = new DeckWeapon();
        WeaponCard weaponCard;
        int numDraw=100;
        for (int i=0;i<=numDraw;i++) {
            weaponCard = deckWeapon.draw();
            System.out.println(weaponCard.toString());
            deckWeapon.addToDiscarded(weaponCard);
            System.out.println(deckWeapon.cards.size());
            System.out.println(deckWeapon.discarded.size());
            assertEquals(21, deckWeapon.cards.size() + deckWeapon.discarded.size());
        }

    }
}
