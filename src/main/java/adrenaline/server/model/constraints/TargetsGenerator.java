package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;

/**
 * Represents a rule for generating additional targets based on a root selection.
 */
public interface TargetsGenerator {

    /**
     * @param shooterPos The position of the player executing the action
     * @param root       The root square selected
     * @param map        The map
     * @return           A collection of integers representing the squares that should be targeted.
     */
    ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map );
}
