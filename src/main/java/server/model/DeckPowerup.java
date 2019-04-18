

package server.model;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.*;
import java.util.ArrayList;


/*
    Autor:Zhang Yuedong
    Function:This class for construct deck of Powerup card,the original file
             name is PowerupCard2.json in FILE

 */
public class DeckPowerup extends Deck<PowerupCard> {



    public DeckPowerup(){

        int i;

        deck = new ArrayList<>();

        try{
            Gson gson = new Gson();
            for (i = 2; i <= 13; i++) {

                PowerupCard powerupCard = gson.fromJson(new
                        FileReader("resource/PowerupCard/PowerupCard" + i + ".json"), PowerupCard.class);

                PowerupCard powerupArray = powerupCard;
                deck.add(powerupArray);
            }

        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard file not found");
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