package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.server.controller.Lobby;
import adrenaline.MapUpdateMessage;

public abstract class Square {

    protected Color color;
    protected Map map;

    public abstract void acceptGrab(Lobby lobby);
    public abstract void acceptConvertInfo(MapUpdateMessage updatemsg, int index);
    public abstract void setCard(Lobby lobby);

    public Color getColor(){ return color; }
    public void setMap(Map map){ this.map = map; }
}
