package adrenaline.server.model;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class DeckAmmo extends Deck<AmmoCard>{

    public DeckAmmo() {
        Gson gson = new Gson();
        AmmoCard[] ammoCards = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/Jsonsrc/Ammo.json")),AmmoCard[].class);
        cards.addAll(Arrays.asList(ammoCards));
        shuffle();
    }

    @Override
    public String toString() {
        return "DeckAmmo{" +
                "cardsDeck=" + cards +
                '}';
    }


}
