package server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class DeckWeapon extends Deck<WeaponCard>{

    public DeckWeapon() {

        deck = new ArrayList<>();

        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/WeaponCard.json");

            WeaponCard[] weaponCards = gson.fromJson(fileReader,WeaponCard[].class);

            for (int i=0;i<weaponCards.length;i++)
                deck.add(weaponCards[i]);

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
                "deck=" + deck +
                '}';
    }
}
