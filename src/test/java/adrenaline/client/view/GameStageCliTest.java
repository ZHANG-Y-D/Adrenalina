package adrenaline.client.view;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.CliView.GameStageCli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStageCliTest {

    GameStageCli gameStageCli;

    @BeforeEach
    void setUp() {

         gameStageCli= new GameStageCli(new GameController());

    }

    @Test
    void newChatTest() {

        gameStageCli.newChatMessage("zhang", Color.YELLOW,"abcdefghijklmnopqrstuvwxyz");

    }
}
