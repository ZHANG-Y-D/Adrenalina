package adrenaline.server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DeckAmmoTest {

    @Test
    void deckAmmoBuildTest() {
        DeckAmmo deckAmmo = new DeckAmmo();

        //Test cards total
        assertEquals(36,deckAmmo.cards.size());

        //Test deck contains all index
        ArrayList<Integer> index = new ArrayList<>();
        for(int i = 1; i <= deckAmmo.cards.size(); i++) index.add(i);
        for(AmmoCard ammo : deckAmmo.cards){
            assertTrue(index.contains(ammo.getAmmoID()));
        }

        //Test contains a content
        int[] content = {0,1,2,0};
        int[] testContent = new int[4];
        for(AmmoCard ammo : deckAmmo.cards){
            if(ammo.getAmmoID() == 11) testContent = ammo.getAmmoContent();
        }
        assertArrayEquals(content,testContent);

    }


}
