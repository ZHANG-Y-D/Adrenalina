package adrenaline.client.view.gui;

/**
 *
 * For build the position Gui
 *
 */
public enum Position {
    TOP(50,5), LEFT(0,50), CENTER(50,50), RIGHT(95,50), DOWN(50,95);

    private int x;
    private int y;

    /**
     *
     * Constructor of position
     * @param x x-coordinate
     * @param y y-coordinate
     */
    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     * The getter of x-coordinate
     * @return x-coordinate
     */
    public int getX(){ return x; }

    /**
     *
     * The getter of y-coordinate
     * @return y-coordinate
     */
    public int getY(){ return y; }
}
