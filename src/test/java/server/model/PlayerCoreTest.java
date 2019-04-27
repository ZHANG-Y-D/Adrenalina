
package server.model;

/*
 *
 *
 *   Responsible：ZHANG YUEDONG
 *
 */

import org.junit.jupiter.api.Test;
import server.controller.PlayerShell;

class PlayerCoreTest {


    @Test
    void PrintPlayInfo(){

        PlayerShell playerShell = new PlayerShell("ZHANG",Color.WHITE,1);
        PlayerCore player = new PlayerCore(playerShell);
        System.out.println(player.toString());

    }

}
