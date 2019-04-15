package server.model;

import server.model.constraints.RangeConstraint;

public class MovementEffect {
    private int movementRange;
    private boolean forced;
    private boolean self;
    /* Values for timing: -1 = pre, 1 = post, 0 = choice*/
    private int timing;
    private RangeConstraint destinationConstraint;

    public int getMovementRange() {
        return movementRange;
    }

    public boolean isForced() {
        return forced;
    }

    public boolean isSelf() {
        return self;
    }

    public int getTiming() {
        return timing;
    }

    public RangeConstraint getDestinationConstraint() {
        return destinationConstraint;
    }
}
