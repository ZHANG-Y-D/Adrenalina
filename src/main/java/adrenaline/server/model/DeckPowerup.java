

package adrenaline.server.model;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;



/**
 *
 *
 *Function:This class for construct cardsDeck of Powerup card,the original file name is PowerupCard.json in resource
 *
 *
 */


public class DeckPowerup extends Deck<PowerupCard> {



    public DeckPowerup(){


        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/PowerupCard.json");


            //There are 24 powerup cards in total.So read two times
            PowerupCard[] powerupCards = gson.fromJson(fileReader, PowerupCard[].class);
            cards.addAll(Arrays.asList(powerupCards));
            cards.addAll(Arrays.asList(powerupCards));

        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard.json file not found");
        }
    }



    //Just for test, Put here a little times
    @Override
    public String toString() {

        return "DeckPowerup{" +
                "cardsDeck=" + cards +
                '}';
    }
}