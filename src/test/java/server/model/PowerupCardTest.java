/*

    Responsible：ZHANG YUEDONG


 */



package server.model;

import org.junit.jupiter.api.Test;
import server.controller.PlayerShell;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PowerupCardTest {




    @Test
    void playitTest() {


        PlayerShell ownerPlayer = new PlayerShell("Anna",Color.BLACK,1);
        ownerPlayer.newPlayerCore();
        PlayerShell targetPlayer = new PlayerShell("Bob",Color.WHITE,1);
        targetPlayer.newPlayerCore();

        ownerPlayer.getPlayerCore().addPowerup(new PowerupCard("GRNATA VANOM",Color.BLUE,"For test",true,1));
        ownerPlayer.getPlayerCore().addPowerup(new PowerupCard("MIRINO", Color.RED, "For test", true, 1));
        ownerPlayer.getPlayerCore().addPowerup(new PowerupCard("RAGGIO CINETICO", Color.YELLOW, "For test", true, 1));
        ownerPlayer.getPlayerCore().addPowerup(new PowerupCard("ABC", Color.RED, "For test", true, 1));

        //Test max numbers of PowerUp Cards
        assertEquals(3,ownerPlayer.getPlayerCore().getPowerup().size());

        //Test for PowerupCard GRNATA VANOM
        ownerPlayer.getPlayerCore().getPowerup().get(0).playIt(ownerPlayer,targetPlayer,0);
        assertEquals(ownerPlayer,targetPlayer.getPlayerCore().getMark().get(0));


        //Test for PowerupCard MIRINO
        ownerPlayer.getPlayerCore().getPowerup().get(0).playIt(ownerPlayer,targetPlayer,0);
        assertEquals(ownerPlayer,targetPlayer.getPlayerCore().getDamageTrack().get(0));

        //Test for PowerupCard RAGGIO CINETICO
        ownerPlayer.getPlayerCore().getPowerup().get(0).playIt(ownerPlayer,targetPlayer,3);
        assertEquals(3,targetPlayer.getPlayerCore().getPosition());

        //Test for PowerupCard TELETRASPORTO
        ownerPlayer.getPlayerCore().addPowerup(new PowerupCard("TELETRASPORTO",Color.RED,"For test",true,1));
        ownerPlayer.getPlayerCore().getPowerup().get(0).playIt(ownerPlayer,null,4);
        assertEquals(4,ownerPlayer.getPlayerCore().getPosition());

    }
}
