package adrenaline.server.model;

import adrenaline.Color;
import org.junit.jupiter.api.Test;
import adrenaline.server.controller.Lobby;
import adrenaline.server.network.Client;

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
        Color damageOrigin =  Color.RED;


        Player Target = new Player("Bob", Color.YELLOW,lobby);


        //Anna attack Bob amount = 1
        Target.applyDamage(damageOrigin,1, false);

        assertEquals(damageOrigin,Target.getDamageTrack().get(0));

        //Test 3 damage
        Target.applyDamage(damageOrigin,2, false);
        assertEquals(Target.getAdrenalineState(), 1);

        //Test 6 damage
        Target.applyDamage(damageOrigin,3, false);
        assertEquals(Target.getAdrenalineState(),2);
        assertEquals(Target.getDamageTrack().size(),6);


    }

    @Test
    void addMarkTest() {

        Lobby lobby = new Lobby(null);
        Color markOrigin = Color.WHITE;
        Player markTarget = new Player("Daniele", Color.BLACK,lobby);


        //Test mark adder
        for (int i=0;i<=4;i++) {
            markTarget.addMarks(markOrigin, 0);
            if(i<3) {
                assertEquals(markOrigin, markTarget.getMarks().get(i));
            }
        }

        //Test putMarkToDamageTrackAndClearThem
        assertEquals(3,markTarget.getMarks().size());
        markTarget.applyDamage(markOrigin,1, false);
        assertEquals(4,markTarget.getDamageTrack().size());
        assertEquals(markOrigin,markTarget.getDamageTrack().get(3));
        assertTrue(markTarget.getMarks().isEmpty());



    }

    @Test
    void killAndOverkillTest() {
    }

}
