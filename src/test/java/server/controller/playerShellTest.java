package server.controller;

import org.junit.jupiter.api.Test;
import server.model.Color;

public class playerShellTest {

    @Test
    void PrintPlayerInfo(){


        PlayerShell playerShell = new PlayerShell("ZHANG", Color.WHITE,1);
        playerShell.newPlayerCore();
        System.out.print(playerShell.getPlayerCore().toString());


    }

}
