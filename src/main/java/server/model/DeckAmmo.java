package server.model;

/*
 *
 *  When implement time, copy mix and use.
 *
 *  Author Zhang YueDong
 */

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class DeckAmmo extends Deck<AmmoCard>{


    public DeckAmmo() {


        try {

            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Ammo.json");

            AmmoCard[] ammoCards = gson.fromJson(fileReader,AmmoCard[].class);
            cards.addAll(Arrays.asList(ammoCards));



        }catch (FileNotFoundException e){
            System.out.println("Ammo.json file not found");
        }

    }




    //Just for test, Put here a little times
    @Override
    public String toString() {
        return "DeckAmmo{" +
                "cardsDeck=" + cards +
                '}';
    }


}
