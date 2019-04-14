/*
    Author:Zhang Yuedong
 */
package server.model;


public abstract class PowerupCard {

    private String name;
    private char color;
    private String manual;
    private int useTime;

    public PowerupCard(String name,char color, String manual, int useTime) {
        this.name = name;
        this.color = color;
        this.manual = manual;
        this.useTime = useTime;
    }

    public void PlayFunction(){
        //ToDo
    }
}
