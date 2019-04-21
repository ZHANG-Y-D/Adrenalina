
package server.model;

/*
 *
 *
 *   Responsible：ZHANG YUEDONG
 *
 */

import org.junit.jupiter.api.Test;

class PlayerTest {


    @Test
    void PrintPlayInfo(){

        Player player = new Player("ZHANG",Color.BLUE);
        System.out.println(player.toString());

    }


    @Test
    void addDamegeTest() {


        Player playerDamegaOrigin = new Player("Anna",Color.PURPLE);
        Player playerDamageTarget =new Player("Bob",Color.BLUE);

        playerDamageTarget.addDamage(playerDamegaOrigin,1);

    }
}
