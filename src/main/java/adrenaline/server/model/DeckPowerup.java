

package adrenaline.server.model;

import adrenaline.CustomSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;



/**
 *
 *
 *Function:This class for construct cardsDeck of Powerups card,the original file name is PowerupCard.json in resource
 *
 *
 */


public class DeckPowerup extends Deck<PowerupCard> {

    public DeckPowerup(){
        try{
            GsonBuilder gsonBld = new GsonBuilder();
            gsonBld.registerTypeAdapter(PowerupCard.class, new CustomSerializer());
            Gson gson = gsonBld.create();
            PowerupCard[] powerupCards = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/Jsonsrc/PowerupCard.json")), PowerupCard[].class);
            cards.addAll(Arrays.asList(powerupCards));
            shuffle();
        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
    }

    @Override
    public String toString() {

        return "DeckPowerup{" +
                "cardsDeck=" + cards +
                '}';
    }
}