package server.model;

import org.junit.jupiter.api.Test;
import server.controller.Lobby;
import server.network.Client;

import java.util.ArrayList;

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
        Lobby lobby = new Lobby(new ArrayList<Client>());
        Player damageOrigin = new Player("Anna",Color.RED,lobby);


        Player Target = new Player("Bob",Color.YELLOW,lobby);


        //Anna attack Bob amount = 1
        Target.sufferDamage(damageOrigin,1);

        assertEquals(damageOrigin,Target.getDamageTrack().get(0));

        //Test 3 damage
        Target.sufferDamage(damageOrigin,2);
        assertEquals(Target.getAdrenalineState(), 1);

        //Test 6 damage
        Target.sufferDamage(damageOrigin,3);
        assertEquals(Target.getAdrenalineState(),2);
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
