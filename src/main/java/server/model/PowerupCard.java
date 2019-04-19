/*
    Author:Zhang Yuedong
 */
package server.model;


public class PowerupCard {

    private String name;
    private String color;
    private String manual;
    private boolean isUseInTurn; //ture: you can use is in your turn; false:you can't use it in your turn
    private int numPowerup;


    public PowerupCard(String name, String color, String manual, boolean isUseInTurn, int numPowerup) {
        this.name = name;
        this.color = color;
        this.manual = manual;
        this.isUseInTurn = isUseInTurn;
        this.numPowerup = numPowerup;
    }

    public void PlayFunction(){
       // if (this.name==)
    }

    @Override
    public String toString() {
        return "PowerupCard{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", manual='" + manual + '\'' +
                ", isUseInTurn=" + isUseInTurn +
                ", numPowerup=" + numPowerup +
                '}';
    }
}
