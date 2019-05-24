package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.server.MapUpdateMessage;
import adrenaline.server.controller.Lobby;

public abstract class Square {

    protected Color color;

    public abstract void acceptGrab(Lobby lobby, int actionNumber);
    public abstract void acceptConvertInfo(MapUpdateMessage updatemsg, int index);

    public Color getColor(){ return color; }
}
