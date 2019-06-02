package adrenaline.server.controller;

import java.util.TimerTask;

public class TurnTimer  implements Runnable {
    private Lobby callBackLobby;

    TurnTimer(Lobby lobby){
        callBackLobby = lobby;
    }

    @Override
    public void run() {
        callBackLobby.endTurn();
    }
}
