package adrenaline.server.model;


/*
 *
 *
 *
 *  Responsible:Zhang Yuedong
 *
 *
 */


import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class DeckWeapon extends Deck<WeaponCard>{

    public DeckWeapon() {


        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/WeaponCards.json");

            WeaponCard[] weaponCards = gson.fromJson(fileReader,WeaponCard[].class);

            cards.addAll(Arrays.asList(weaponCards));

        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard.json file not found");
        }
    }


    @Override
    public String toString() {
        return "DeckWeapon{" +
                "cardsDeck=" + cards +
                '}';
    }
}
