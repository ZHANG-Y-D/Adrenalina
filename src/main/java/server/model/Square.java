package server.model;

import java.util.ArrayList;

public class Square {

    private Color color;
    private boolean spawn;
    private AmmoCard card;
    private ArrayList<WeaponCard> weaponCardDeck;

    public boolean isSpawn() { return spawn; }

    public ArrayList<WeaponCard> getWeaponCardDeck() {
        return weaponCardDeck;
    }

    public Color getColor(){ return color; }

    public AmmoCard getAmmoCard() {
        return card;
    }

    public void setAmmoCard(AmmoCard card) { this.card = card; }

    @Override
    public String toString() {
        return "Square{" +
                "color=" + color +
                ", spawn=" + spawn +
                ", card=" + card +
                ", weaponCardDeck=" + weaponCardDeck +
                '}';
    }
}
