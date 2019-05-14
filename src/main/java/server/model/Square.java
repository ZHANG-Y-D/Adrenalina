package server.model;

import java.util.ArrayList;

public class Square {

    private Color color;
    private boolean spawn;
    private AmmoCard ammoTile;
    private ArrayList<WeaponCard> weaponCardDeck;

    public boolean isSpawn() { return spawn; }

    public ArrayList<WeaponCard> getWeaponCardDeck() {
        return weaponCardDeck;
    }

    public WeaponCard removeWeaponCardFromDeck(int indexToBeRemove) {
        if (indexToBeRemove < 0 || indexToBeRemove >= weaponCardDeck.size())
            return null;
        return getWeaponCardDeck().remove(indexToBeRemove);
    }

    public Color getColor(){ return color; }

    public AmmoCard getAmmoTile() {
        return ammoTile;
    }


    public void setAmmoTile(AmmoCard card) { this.ammoTile = card; }

    @Override
    public String toString() {
        return "Square{" +
                "color=" + color +
                ", spawn=" + spawn +
                ", ammoTile=" + ammoTile +
                ", weaponCardDeck=" + weaponCardDeck +
                '}';
    }
}
