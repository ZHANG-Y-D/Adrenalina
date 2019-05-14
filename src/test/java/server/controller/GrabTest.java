package server.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.model.Color;
import server.model.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrabTest {

    Lobby lobby;
    Player player;

    @BeforeAll
    void IniLobbyandPlayer(){

        lobby = new Lobby(null);
        lobby.chooseAndNewAMap(1);
        player = new Player("Anna", Color.PURPLE,lobby);
        player.setPosition(0);

    }

    @Test
    void grabAmmoTest() {
        Grab.grabAmmoCard(player);
        System.out.println(player.getAmmoBox().toString());
        assertEquals(false,Grab.grabAmmoCard(player));

    }
}
