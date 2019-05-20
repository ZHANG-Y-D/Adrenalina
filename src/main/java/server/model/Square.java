package server.model;

import java.util.ArrayList;

public class Square {

    private Color color;
    private boolean spawn;
    private AmmoCard ammoTile = null;
    private ArrayList<WeaponCard> weaponCards = null;

    public boolean isSpawn() { return spawn; }

    public ArrayList<WeaponCard> getWeaponCards() {
        return weaponCards;
    }

    public void removeWeaponCardFromDeck(int indexToBeRemoved) {
        getWeaponCards().remove(indexToBeRemoved);
    }

    public WeaponCard getWeaponCardFromDeck(int index){
        if (index < 0 || index >= weaponCards.size())
            return null;
        return getWeaponCards().get(index);
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
                ", weaponCards=" + weaponCards +
                '}';
    }
}
