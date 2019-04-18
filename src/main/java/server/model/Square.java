package server.model;

public class Square {
    private Color color;
    private boolean spawn;
    private AmmoCard card;

    public Square(String color, boolean spawn){
        this.color = Color.valueOf(color);
        this.spawn = spawn;
    }

    public boolean isSpawn() {
        return spawn;
    }

    public Color getColor(){
        return color;
    }

    public AmmoCard getCard() {
        return card;
    }

    public void setCard(AmmoCard card) {
        this.card = card;
    }
}
