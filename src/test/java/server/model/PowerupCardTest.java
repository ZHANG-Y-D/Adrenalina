package server.model;

import org.junit.jupiter.api.Test;
import server.controller.Lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PowerupCardTest {




    @Test
    void playitTest() {

        Lobby lobby = new Lobby(null);
        Player ownerPlayer = new Player("Anna",Color.BLACK,lobby);
        Player targetPlayer = new Player("Bob",Color.WHITE,lobby);

        ownerPlayer.addPowerupCard(new PowerupCard("GRNATA VANOM",Color.BLUE,"For test",true,1));
        ownerPlayer.addPowerupCard(new PowerupCard("MIRINO", Color.RED, "For test", true, 1));
        ownerPlayer.addPowerupCard(new PowerupCard("RAGGIO CINETICO", Color.YELLOW, "For test", true, 1));
        ownerPlayer.addPowerupCard(new PowerupCard("ABC", Color.RED, "For test", true, 1));

        //Test max numbers of PowerUp Cards
        assertEquals(3,ownerPlayer.getPowerupCards().size());

        //Test for PowerupCard GRNATA VANOM
        ownerPlayer.getPowerupCards().get(0).playIt(ownerPlayer,targetPlayer,0);
        assertEquals(ownerPlayer,targetPlayer.getMark().get(0));


        //Test for PowerupCard MIRINO
        ownerPlayer.getPowerupCards().get(0).playIt(ownerPlayer,targetPlayer,0);
        assertEquals(ownerPlayer,targetPlayer.getDamageTrack().get(0));

        //Test for PowerupCard RAGGIO CINETICO
        ownerPlayer.getPowerupCards().get(0).playIt(ownerPlayer,targetPlayer,3);
        assertEquals(3,targetPlayer.getPosition());

        //Test for PowerupCard TELETRASPORTO
        ownerPlayer.addPowerupCard(new PowerupCard("TELETRASPORTO",Color.RED,"For test",true,1));
        ownerPlayer.getPowerupCards().get(0).playIt(ownerPlayer,null,4);
        assertEquals(4,ownerPlayer.getPosition());

    }
}
