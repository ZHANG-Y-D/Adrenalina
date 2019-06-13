package adrenaline.client.view;

public enum Position {
    TOP(50,0), LEFT(0,50), CENTER(50,50), RIGHT(100,50), DOWN(100,50);

    private int x;
    private int y;

    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){ return x; }

    public int getY(){ return y; }
}
