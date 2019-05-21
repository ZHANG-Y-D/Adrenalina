package server.model;

import server.controller.Lobby;

public abstract class Square {

    protected Color color;

    public abstract void acceptGrab(Lobby lobby, int actionNumber);

    public Color getColor(){ return color; }
}
