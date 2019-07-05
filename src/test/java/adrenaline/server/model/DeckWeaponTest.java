package adrenaline.server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckWeaponTest {

    @Test
    void deckWeaponBuildTest() {

        DeckWeapon deckWeapon =new DeckWeapon();
        //Test cards total
        assertEquals(21,deckWeapon.cards.size());

        //Test deck contains all index
        ArrayList<Integer> index = new ArrayList<>();
        for(int i = 1; i <= deckWeapon.cards.size(); i++) index.add(i);
        for(WeaponCard wc : deckWeapon.cards){
            assertTrue(index.contains(wc.getWeaponID()));
        }
    }

}
