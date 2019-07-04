package adrenaline.server.model;

import adrenaline.Color;

/**
 *
 * The server side model Avatar
 *
 *
 */
public class Avatar {
    private final String name;
    private final Color color;

    /**
     *
     * The constructor of avatar
     *
     * @param name The avatar name string
     * @param color The avatar's color
     */
    public Avatar(String name, Color color){
        this.name = name;
        this.color = color;
    }

    /**
     *
     * The getter of avatar's name
     *
     * @return The avatar name string
     */
    public String getName() {
        return name;
    }

    /**
     *
     * The getter of Avatar color
     *
     * @return The avatar's color
     */
    public Color getColor() {
        return color;
    }
}
