package Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class Deck<T> {

    private ArrayList<T> deck;
    private ArrayList<T> discarded;


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
