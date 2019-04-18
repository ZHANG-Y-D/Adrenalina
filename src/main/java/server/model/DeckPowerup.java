

package server.model;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;




/*
    Autor:Zhang Yuedong
    Function:This class for construct deck of Powerup card,the original file
             name is PowerupCard.json in FILE

 */
public class DeckPowerup extends Deck<PowerupCard> {


    //TODO
    public DeckPowerup(){

        int i;

        try{
            FileInputStream fileStream = new FileInputStream("FILE/PowerupCard.json");
            ObjectInputStream powerupStream = new ObjectInputStream(fileStream);
            for (i = 0; i < 12; i++) {
                PowerupCard powerupobject = (PowerupCard) powerupStream.readObject();
                PowerupCard powerupcard = powerupobject;
                deck.add(powerupcard);
            }
            powerupStream.close();
        }catch (FileNotFoundException e) {
            System.out.println("PowerupCard file not found");
        }catch (ClassNotFoundException e){
            System.out.println("Class not found exception");
        }
        catch (IOException e){
            System.out.println("I/O Error");
        }

    }
}
