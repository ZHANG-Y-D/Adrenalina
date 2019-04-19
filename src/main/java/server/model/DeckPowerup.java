

package server.model;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.*;
import java.util.ArrayList;



/*
    Autor:Zhang Yuedong
    Function:This class for construct deck of Powerup card,the original file
             name is PowerupCard.json in resource
 */


public class DeckPowerup extends Deck<PowerupCard> {



    public DeckPowerup(){


        deck = new ArrayList<>();

        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/PowerupCard.json");

            PowerupCard[] powerupCards = gson.fromJson(fileReader,PowerupCard[].class);

            for (int i=0;i<powerupCards.length;i++)
                deck.add(powerupCards[i]);

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
                "deck=" + deck +
                '}';
    }
}