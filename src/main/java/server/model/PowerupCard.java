
package server.model;


/*
 *
 *
 *
 *  Responsible:Zhang Yuedong
 *
 *
 */

public class PowerupCard {

    private String name;
    private Color color;
    private String manual;
    private boolean isUseInTurn;  //ture: you can use is in your turn; false:you can't use it in your turn
    private int numPowerup;


    public PowerupCard(String name, Color color, String manual, boolean isUseInTurn, int numPowerup) {
        this.name = name;
        this.color = color;
        this.manual = manual;
        this.isUseInTurn = isUseInTurn;
        this.numPowerup = numPowerup;
    }

    //Attention：to playIt,the Caller have to judgment good the condition,and then call it
    //fromPlayer is me, targetPlayer is who I want to attack mark or change position
    public void playIt(Player thisPowerupCardOwner, Player targetPlayer, int position){

       switch (this.name){


           case "GRNATA VANOM":
                targetPlayer.addMark(thisPowerupCardOwner);
               break;


           case "MIRINO":
               targetPlayer.sufferDamage(thisPowerupCardOwner,1);
               break;


           case "RAGGIO CINETICO":
                targetPlayer.setPosition(position);
               break;

           case "TELETRASPORTO":
                thisPowerupCardOwner.setPosition(position);
               break;


           default:
                System.out.print("Illegal execution！！！"); //Also can put Exception

       }

       thisPowerupCardOwner.deletePowerup(this);

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
