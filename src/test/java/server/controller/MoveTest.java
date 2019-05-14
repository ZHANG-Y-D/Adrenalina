package server.controller;

import org.junit.jupiter.api.Test;
import server.model.Color;
import server.model.Player;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveTest {


    @Test
    void MoveInTurnTest() {

        Lobby lobby = new Lobby(null);
        lobby.chooseAndNewAMap(1);
        Player player = new Player("Anna", Color.PURPLE,lobby);

        player.setPosition(0);
        Move.moveInturn(player,2,3);
        assertEquals(2,player.getPosition());


        assertEquals(false,Move.moveInturn(player,10,3));

    }
}
