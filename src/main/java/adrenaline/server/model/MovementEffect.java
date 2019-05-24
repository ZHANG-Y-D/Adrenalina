package adrenaline.server.model;

public class MovementEffect {

    public enum Timing{
        PRE,
        POST,
        CHOICE
    }

    private int movementRange;
    private boolean self;

    private boolean forced;
    private Timing timing;

    public MovementEffect(int mvRange, boolean slf, boolean frc, Timing tmng){
        this.movementRange = mvRange;
        this.self = slf;
        this.forced = frc;
        this.timing = tmng;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public boolean isSelf() {
        return self;
    }

    public boolean isForced() { return forced; }

    public Timing getTiming() { return timing;}
}
