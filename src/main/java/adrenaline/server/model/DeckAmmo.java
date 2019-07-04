package adrenaline.server.model;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *
 * The DeckAmmo class extends Deck<AmmoCard>
 *
 *
 */
public class DeckAmmo extends Deck<AmmoCard>{

    /**
     *
     * The constructor of the DeckAmmo,read the json file to build a Ammo object from resource
     *
     */
    public DeckAmmo() {
        Gson gson = new Gson();
        AmmoCard[] ammoCards = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/Jsonsrc/Ammo.json")),AmmoCard[].class);
        cards.addAll(Arrays.asList(ammoCards));
        shuffle();
    }
}
