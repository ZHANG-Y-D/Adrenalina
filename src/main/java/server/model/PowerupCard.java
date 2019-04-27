
package server.model;


/*
 *
 *
 *
 *  Responsible:Zhang Yuedong
 *
 *
 */

import server.controller.PlayerShell;

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





    //Attention：to Playit,the Caller have to judgment good the condition,and then call it
    //fromPlayer is me, targetPlayer is who I want to attack mark or change position
    public void Playit(PlayerShell fromPlayer, PlayerShell targetPlayer, int position){

       switch (this.name){


           case "GRNATA VANOM":
                targetPlayer.getPlayerCore().addMark(fromPlayer);
               break;


           case "MIRINO":
               targetPlayer.getPlayerCore().sufferDamage(fromPlayer,1);
               break;


           case "RAGGIO CINETICO":
                targetPlayer.getPlayerCore().setPosition(position);
               break;

           case "TELETRASPORTO":
                fromPlayer.getPlayerCore().setPosition(position);
               break;


           default:
                System.out.print("Illegal execution！！！"); //Also can put Exception

       }

    }

    public String getName() {
        return name;
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
