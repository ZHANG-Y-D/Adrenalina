

package server.model;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;



/*
    Autor:Zhang Yuedong
    Function:This class for construct cardsDeck of Powerup card,the original file
             name is PowerupCard.json in resource
 */


public class DeckPowerup extends Deck<PowerupCard> {



    public DeckPowerup(){


        cardsDeck = new ArrayList<>();

        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/PowerupCard.json");

            PowerupCard[] powerupCards = gson.fromJson(fileReader,PowerupCard[].class);

            cardsDeck.addAll(Arrays.asList(powerupCards));

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
                "cardsDeck=" + cardsDeck +
                '}';
    }
}