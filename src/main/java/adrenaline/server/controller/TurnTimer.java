package adrenaline.server.controller;

public class TurnTimer  implements Runnable {
    private Lobby callBackLobby;

    TurnTimer(Lobby lobby){
        callBackLobby = lobby;
    }

    @Override
    public void run() {
        System.out.println("tempo scaduto");
        callBackLobby.endTurn(true);
    }
}
