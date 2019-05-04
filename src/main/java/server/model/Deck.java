package server.model;


/*
 *
 *
 *  Responsible:Zhang Yuedong
 *
 *
 */


import java.util.ArrayList;
import java.util.Collections;

public abstract class Deck<T> {

    ArrayList<T> cardsDeck;   //Sonar reminds that cannot use name "deck"
    private ArrayList<T> usingDeckCard;


    public T draw(){

        if (this.usingDeckCard.isEmpty()) {
            this.usingDeckCard = (ArrayList<T>)cardsDeck.clone();
            Collections.shuffle(usingDeckCard);
        }
        return this.usingDeckCard.remove(this.usingDeckCard.size()-1);

    }


}
