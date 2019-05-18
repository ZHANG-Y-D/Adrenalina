package server.model;

import org.junit.jupiter.api.Test;
import server.controller.Lobby;

import static org.junit.jupiter.api.Assertions.*;

public class playerTest {

    @Test
    void PrintPlayerInfo(){

        Lobby lobby =new Lobby(null);
        Player player = new Player("ZHANG", Color.WHITE,lobby);
        System.out.print(player.toString());


    }


    @Test
    void sufferDamageTest() {

        //Test 1 damege
        Lobby lobby = new Lobby(null);
        Player damageOrigin = new Player("Anna",Color.RED,lobby);


        Player Target = new Player("Bob",Color.YELLOW,lobby);


        //Anna attack Bob amount = 1
        Target.sufferDamage(damageOrigin,1);

        assertEquals(damageOrigin,Target.getDamageTrack().get(0));
        assertEquals(1,damageOrigin.getScore());

        //Test 3 damage
        Target.sufferDamage(damageOrigin,2);
        int[] num1 = new int[]{3,2,0};
        assertArrayEquals(Target.getAdrenalineState(), num1);

        //Test 6 damage
        Target.sufferDamage(damageOrigin,3);
        int[] num2 = new int[]{3,2,1};
        assertArrayEquals(Target.getAdrenalineState(),num2);
        assertEquals(Target.getDamageTrack().size(),6);


    }

    @Test
    void addMarkTest() {

        Lobby lobby = new Lobby(null);
        Player markOrigin = new Player("Ceci",Color.WHITE,lobby);
        Player markTarget = new Player("Daniele",Color.BLACK,lobby);


        //Test mark adder
        for (int i=0;i<=4;i++) {
            markTarget.addMark(markOrigin);
            if(i<3) {
                assertEquals(markOrigin, markTarget.getMark().get(i));
            }
        }

        //Test putMarkToDamageTrackAndClearThem
        assertEquals(3,markTarget.getMark().size());
        markTarget.sufferDamage(markOrigin,1);
        assertEquals(4,markTarget.getDamageTrack().size());
        assertEquals(markOrigin,markTarget.getDamageTrack().get(3));
        assertTrue(markTarget.getMark().isEmpty());



    }

    @Test
    void killAndOverkillTest() {
    }

}
