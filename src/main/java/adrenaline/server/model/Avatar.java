package adrenaline.server.model;

import adrenaline.Color;

public class Avatar {
    private final String name;
    private final Color color;

    public Avatar(String name, Color color){
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
