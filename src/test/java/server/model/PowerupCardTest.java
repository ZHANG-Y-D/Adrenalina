/*

    Responsible：ZHANG YUEDONG


 */



package server.model;

import org.junit.jupiter.api.Test;

public class PowerupCardTest {


    @Test
    void playitTest() {

        PowerupCard powerupCard = new PowerupCard("GRNATA VANOM","white","For test",true,1);
        powerupCard.Playit(null,null,0);
        powerupCard = new PowerupCard("MIRINO", "white", "For test", true, 1);
        powerupCard.Playit(null,null,0);
        powerupCard = new PowerupCard("ABC", "white", "For test", true, 1);
        powerupCard.Playit(null,null,0);

    }
}
