
package server.model;

/*
 *
 *
 *   Responsible：ZHANG YUEDONG
 *
 */

import org.junit.jupiter.api.Test;

class PlayerCoreTest {


    @Test
    void PrintPlayInfo(){

        PlayerCore player = new PlayerCore("ZHANG",Color.BLUE);
        System.out.println(player.toString());

    }


    @Test
    void addDamegeTest() {


        PlayerCore playerDamegaOrigin = new PlayerCore("Anna",Color.PURPLE);
        PlayerCore playerDamageTarget =new PlayerCore("Bob",Color.BLUE);

        playerDamageTarget.sufferDamage(playerDamegaOrigin,1);

    }
}
