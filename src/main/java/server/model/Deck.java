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

    protected ArrayList<T> cardsDeck;   //Sonar reminds that cannot use name "deck"
    protected ArrayList<T> discarded;


    public T draw(){
        return cardsDeck.remove(cardsDeck.size() -1);
    }

    public void addDiscarded(T card){
        discarded.add(card);
    }

    public void shuffle(){
        Collections.shuffle(cardsDeck);
    }

}
