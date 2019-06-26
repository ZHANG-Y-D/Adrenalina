package adrenaline.server.model;

import adrenaline.Color;
import org.junit.jupiter.api.Test;
import adrenaline.server.controller.Lobby;
import adrenaline.server.network.Client;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class playerTest {



    @Test
    void sufferDamageTest() {
        Color damageOrigin =  Color.RED;
        Player Target = new Player(new Avatar("TESTER", Color.YELLOW),"Bob", new ArrayList<Client>());

        Target.applyDamage(damageOrigin,1, false);
        assertEquals(damageOrigin,Target.getDamageTrack().get(0));

        Target.applyDamage(damageOrigin,2, false);
        assertEquals(Target.getAdrenalineState(), 1);

        Target.applyDamage(damageOrigin,3, false);
        assertEquals(Target.getAdrenalineState(),2);
        assertEquals(Target.getDamageTrack().size(),6);


    }

    @Test
    void addMarkTest() {
        Color markOrigin = Color.WHITE;
        Player markTarget = new Player(new Avatar("TESTER", Color.YELLOW),"Bob", new ArrayList<Client>());
        markTarget.addMarks(markOrigin, 1);
        assertEquals(markOrigin, markTarget.getMarks().get(0));

        markTarget.addMarks(markOrigin, 2);
        assertEquals(3,markTarget.getMarks().size());
        markTarget.applyDamage(markOrigin,1, false);
        assertEquals(4,markTarget.getDamageTrack().size());
        assertEquals(markOrigin,markTarget.getDamageTrack().get(3));
        assertTrue(markTarget.getMarks().isEmpty());
    }
}
