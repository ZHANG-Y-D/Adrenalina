

package server.model;




import com.google.gson.Gson;

import java.io.*;


/*
    Autor:Zhang Yuedong
    Function:This class for construct deck of Powerup card,the original file
             name is PowerupCard2.json in FILE

 */
public class DeckPowerup extends Deck {


    //TODO
    public DeckPowerup(){

        int i;

        try{
            Gson gson = new Gson();
            for (i = 2; i <= 13; i++) {
                PowerupCard powerupCard = gson.fromJson(new
                        FileReader("PowerupCard/PowerupCard" + i + ".json"), PowerupCard.class);

                PowerupCard powerupArray = powerupCard;
                deck.add(powerupArray);
            }
        }catch (FileNotFoundException e) {
            System.out.println("PowerupCard file not found");
        }
    }
}
