package server.model;

import org.junit.jupiter.api.Test;

public class SquareTest {


    @Test
    void getWeaponCardTest() {


        Square square =new Square();
        square.setAmmoCard(new AmmoCard());
        System.out.print(square.toString());


    }
}
