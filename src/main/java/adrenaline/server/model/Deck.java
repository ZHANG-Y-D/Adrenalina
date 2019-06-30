package adrenaline.server.model;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Deck<T> {

    protected ArrayList<T> cards;
    protected ArrayList<T> discarded;


    public Deck() {
        cards = new ArrayList<>();
        discarded = new ArrayList<>();
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }


    public T draw(){
        if(cards.isEmpty()){
            cards = new ArrayList<>(discarded);
            discarded.clear();
            shuffle();
        }
        return cards.remove(cards.size()-1);
    }

    public void addToDiscarded(T card){
        discarded.add(card);
    }

}
