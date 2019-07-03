package adrenaline.client.view.GuiView;

public enum Position {
    TOP(50,5), LEFT(0,50), CENTER(50,50), RIGHT(95,50), DOWN(50,95);

    private int x;
    private int y;

    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){ return x; }

    public int getY(){ return y; }
}
