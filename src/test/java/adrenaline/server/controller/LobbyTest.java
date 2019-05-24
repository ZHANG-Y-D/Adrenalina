package adrenaline.server.controller;

import org.junit.jupiter.api.Test;

public class LobbyTest {

    @Test
    void getDeckWeaponTest() {

        Lobby lobby = null;
        lobby = new Lobby(null);
        for (int i=0;i<=1;i++)
            System.out.println(lobby.getDeckWeapon().draw());
        System.out.println(lobby.getDeckWeapon().toString());
    }

    @Test
    void readMapTest() {

        Lobby lobby = null;
        lobby = new Lobby(null);
        lobby.chooseAndNewAMap(4);
        for (int i = 0; i < lobby.getMap().getRows(); i++) {
            for (int j = 0; j < lobby.getMap().getColumns(); j++) {
                System.out.println(lobby.getMap().getSquare(i,j).toString());
            }
        }
        System.out.println(lobby.getMap().toString());
    }

}



