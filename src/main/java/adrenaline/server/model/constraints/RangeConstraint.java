package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;

/**
 * Represents a specific constraint on the range of an action.
 */
public interface RangeConstraint {

    /**
     * @param position  Represents the root square on which the constraint is applied.
     * @param map       The map needed to calculate the valid squares.
     * @return          A collection of integers representing the squares on the map that are allowed by the constraint.
     */
    ArrayList<Integer> checkConst(int position, Map map);
}
