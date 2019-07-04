package adrenaline.server.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 *
 * The abstract deck class, ammo/powerup/weapon deck will extent it
 *
 * @param <T> The generic type.
 */
public abstract class Deck<T> {

    protected ArrayList<T> cards;
    protected ArrayList<T> discarded;


    /**
     *
     * The constructor of deck,init card and discard attitude.
     *
     */
    public Deck() {
        cards = new ArrayList<>();
        discarded = new ArrayList<>();
    }

    /**
     *
     * To shuffle the card ArrayList
     *
     */
    public void shuffle(){
        Collections.shuffle(cards);
    }


    /**
     *
     *
     * Draw the first element of the ArrayList
     *
     *
     * @return The concrete type of the ArrayList's element
     */
    public T draw(){
        if(cards.isEmpty()){
            cards = new ArrayList<>(discarded);
            discarded.clear();
            shuffle();
        }
        return cards.remove(cards.size()-1);
    }

    /**
     *
     * Add a element to discard
     *
     * @param card The concrete type of the ArrayList's element
     */
    public void addToDiscarded(T card){
        discarded.add(card);
    }

}
