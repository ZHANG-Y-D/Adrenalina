package server.model;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Deck<T> {

    protected ArrayList<T> deck;
    protected ArrayList<T> discarded;


    public T draw(){
        return deck.remove(deck.size() -1);
    }

    public void addDiscarded(T card){
        discarded.add(card);
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

}
