
package server.model;

/*
 *
 *
 *   Responsible：ZHANG YUEDONG
 *
 *
 *
 */

import org.junit.jupiter.api.Test;
import server.controller.PlayerShell;

import static org.junit.jupiter.api.Assertions.*;

class PlayerCoreTest {


    @Test
    void PrintPlayerInfo(){

        PlayerCore playerCore = new PlayerCore(null);
        System.out.print(playerCore.toString());

    }

    @Test
    void sufferDamageTest() {

        //Test 1 damege
        PlayerShell damageOrigin = new PlayerShell("Anna",Color.RED,1);
        damageOrigin.newPlayerCore();

        PlayerShell Target = new PlayerShell("Bob",Color.YELLOW,1);
        Target.newPlayerCore();

        //Anna attack Bob amount = 1
        Target.getPlayerCore().sufferDamage(damageOrigin,1);

        assertEquals(damageOrigin,Target.getPlayerCore().getDamageTrack().get(0));
        assertEquals(1,damageOrigin.getScore());

        //Test 3 damage
        Target.getPlayerCore().sufferDamage(damageOrigin,2);
        int[] num1 = new int[]{3,2,0};
        assertArrayEquals(Target.getPlayerCore().getRunable(), num1);

        //Test 6 damage
        Target.getPlayerCore().sufferDamage(damageOrigin,3);
        int[] num2 = new int[]{3,2,1};
        assertArrayEquals(Target.getPlayerCore().getRunable(),num2);
        assertEquals(Target.getPlayerCore().getDamageTrack().size(),6);


    }

    @Test
    void addMarkTest() {

        PlayerShell markOrigin = new PlayerShell("Ceci",Color.WHITE,1);
        PlayerShell markTarget = new PlayerShell("Daniele",Color.BLACK,1);
        markOrigin.newPlayerCore();
        markTarget.newPlayerCore();

        //Test mark adder
        for (int i=0;i<=4;i++) {
            markTarget.getPlayerCore().addMark(markOrigin);
            if(i<3) {
                assertEquals(markOrigin, markTarget.getPlayerCore().getMark().get(i));
            }
        }

        //Test putMarkToDamageTrackAndClearThem
        assertEquals(3,markTarget.getPlayerCore().getMark().size());
        markTarget.getPlayerCore().sufferDamage(markOrigin,1);
        assertEquals(4,markTarget.getPlayerCore().getDamageTrack().size());
        assertEquals(markOrigin,markTarget.getPlayerCore().getDamageTrack().get(3));
        assertTrue(markTarget.getPlayerCore().getMark().isEmpty());



    }

    @Test
    void killAndOverkillTest() {



    }



}
