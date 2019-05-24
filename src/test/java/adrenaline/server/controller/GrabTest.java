package adrenaline.server.controller;

import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrabTest {

    private static Lobby lobby;
    private static Player player;
    private static ArrayList<PowerupCard> discardPowerup;


   /* @BeforeAll
    static void IniLobbyAndPlayer(){

        lobby = new Lobby(null);
        lobby.chooseAndNewAMap(1);
        player = new Player("Anna", adrenaline.Color.PURPLE,lobby);
        player.setPosition(0);
        discardPowerup = new ArrayList<>();

    }


    @Test
    void grabAmmoTileTest() {

        int[] oldAmmoBox;

        System.out.println("\n\n --------For grabAmmoTileTest------    ");

        player.setPosition(0);

        for (int i=0;i<=10;i++) {
            oldAmmoBox = player.getAmmoBox();
            System.out.println(Arrays.toString(oldAmmoBox));

            Grab.grabAmmoCard(player);
            System.out.println(Arrays.toString(player.getAmmoBox()));
            System.out.println(player.getPowerupCards().size());

            Grab.grabAmmoCard(player);
            assertFalse(Grab.grabAmmoCard(player));
            lobby.setSquaresCards();

            System.out.println(" \n");
        }

        player.setPosition(3);
        assertFalse(Grab.grabAmmoCard(player));

        player.setPosition(4);
        assertFalse(Grab.grabAmmoCard(player));

        System.out.println("-------For grabAmmoTileTest------    \n\n");


    }

    @Test
    void grabWeaponWithoutSwitchTest() {


        System.out.println("\n\n --------For grabWeaponWithoutSwitchTest------     \n");

        int[] ammoBox;

        player.setPosition(0);
        ammoBox = new int[]{3,3,3};
        player.addAmmoBox(ammoBox);
        player.setPosition(4);


        assertTrue(Grab.grabWeaponCard(player,0,discardPowerup));
        System.out.println(player.getWeaponCard().toString());
        System.out.println(Arrays.toString(player.getAmmoBox()));
        assertEquals(2,player.getLobby().getMap().getSquare(player.getPosition()).getWeaponCard().size());
        assertEquals(1,player.getWeaponCard().size());

        assertTrue(Grab.grabWeaponCard(player,1,discardPowerup));
        System.out.println(player.getWeaponCard().toString());
        System.out.println(Arrays.toString(player.getAmmoBox()));
        assertEquals(1,player.getLobby().getMap().getSquare(player.getPosition()).getWeaponCard().size());
        assertEquals(2,player.getWeaponCard().size());

        assertFalse(Grab.grabWeaponCard(player,1,discardPowerup));

        assertTrue(Grab.grabWeaponCard(player,0,discardPowerup));
        System.out.println(player.getWeaponCard().toString());
        System.out.println(Arrays.toString(player.getAmmoBox()));
        assertEquals(0,player.getLobby().getMap().getSquare(player.getPosition()).getWeaponCard().size());
        assertEquals(3,player.getWeaponCard().size());


        assertFalse(Grab.grabWeaponCard(player,0,discardPowerup));

        lobby.setSquaresCards();
        assertEquals(3,player.getLobby().getMap().getSquare(player.getPosition()).getWeaponCard().size());
        assertFalse(Grab.grabWeaponCard(player,0,discardPowerup));


        ammoBox = new int[]{0,0,0};
        player = new Player("Bob",adrenaline.Color.BLACK,lobby);
        player.addAmmoBox(ammoBox);
        player.setPosition(4);
        lobby.setSquaresCards();

        for (int i=0;i<=4;i++) {
            if (Grab.grabWeaponCard(player,i,discardPowerup))
                System.out.println(player.getWeaponCard().toString());
            else
                System.out.println("False case: i= " + i);

        }

        System.out.println("\n  -------For grabWeaponWithoutSwitchTest------   \n\n");

    }*/
}
