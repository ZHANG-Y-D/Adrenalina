package server.model;

public class MovementEffect {

    public enum Timing{
        PRE,
        POST,
        CHOICE
    }

    private int movementRange;
    private boolean self;
    private Timing timing;

    public MovementEffect(int mvRange, boolean frc, boolean slf, Timing tmng){
        this.movementRange = mvRange;
        this.self = slf;
        this.timing = tmng;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public boolean isSelf() {
        return self;
    }

    public Timing getTiming() { return timing;}
}
