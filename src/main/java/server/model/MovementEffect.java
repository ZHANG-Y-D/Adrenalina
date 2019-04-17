package server.model;

import server.model.constraints.RangeConstraint;

public class MovementEffect {

    public enum Timing{
        PRE,
        POST,
        CHOICE
    }

    private int movementRange;
    private boolean forced;
    private boolean self;
    private Timing timing;
    private RangeConstraint destinationConstraint;

    public MovementEffect(int mvRange, boolean frc, boolean slf, Timing tmng, RangeConstraint rngConst){
        this.movementRange = mvRange;
        this.forced = frc;
        this.self = slf;
        this.timing = tmng;
        this.destinationConstraint = rngConst;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public boolean isForced() {
        return forced;
    }

    public boolean isSelf() {
        return self;
    }

    public RangeConstraint getDestinationConstraint() {
        return destinationConstraint;
    }

    public Timing getTiming() { return timing;}
}
