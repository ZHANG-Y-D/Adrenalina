package server.controller;

import org.junit.jupiter.api.Test;
import server.model.Color;

public class playerShellTest {

    @Test
    void PrintPlayerInfo(){

        Lobby lobby =new Lobby(0,1,1);
        PlayerShell playerShell = new PlayerShell("ZHANG", Color.WHITE,1,lobby);
        playerShell.newPlayerCore();
        System.out.print(playerShell.getPlayerCore().toString());


    }

}
