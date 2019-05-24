package adrenaline.server.controller;

import org.junit.jupiter.api.Test;
import adrenaline.Color;
import adrenaline.server.model.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MoveTest {


    @Test
    void MoveInTurnTest() {

        Lobby lobby = new Lobby(null);
        lobby.chooseAndNewAMap(1);
        Player player = new Player("Anna", Color.PURPLE,lobby);

        player.setPosition(0);
        Move.moveInturn(player,2,3);
        assertEquals(2,player.getPosition());

        assertFalse(Move.moveInturn(player, 10, 3));

    }
}
