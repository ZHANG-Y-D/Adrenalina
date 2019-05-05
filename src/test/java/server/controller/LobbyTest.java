package server.controller;

import org.junit.jupiter.api.Test;

public class LobbyTest {

    @Test
    void getDeckWeaponTest() {

        Lobby lobby = new Lobby(null);
        for (int i=0;i<=1;i++)
            System.out.println(lobby.getDeckWeapon().draw());
        System.out.println(lobby.getDeckWeapon().toString());
    }

    @Test
    void readMapTest() {

        Lobby lobby = new Lobby(null);
        lobby.chooseAndNewAMap(1);
        for (int i = 0; i < lobby.getMap().getRows(); i++) {
            for (int j = 0; j < lobby.getMap().getColumns(); j++) {
                System.out.println(lobby.getMap().getMapSquares()[i][j].toString());
            }
        }
        System.out.println(lobby.getMap().toString());
    }

}



