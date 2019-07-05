package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;

/**
 * Represents a specific constraint that applies on a collection of targets so that they may be considered valid.
 */
public interface TargetsConstraint {

    /**
     * @return A boolean telling whether this targets constraint should apply on targets that don't comply the range constraints.
     */
    boolean isSpecialRange();

    /**
     * @param shooter The player executing the action.
     * @param targets The collection of intended targets.
     * @param map     The map needed to calculate the constraint application.
     * @return      <code>true</code> if the targets comply this constraint;
     *              <code>false</code> otherwise
     */
    boolean checkConst(Player shooter, ArrayList<Player> targets, Map map);
}
