package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * {@inheritDoc}
 * This constraint checks whether the targets are all in different squares.
 */
public class DifferentSquaresConstraint implements TargetsConstraint {

    /**
     * {@inheritDoc}
     */
    public boolean isSpecialRange() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        HashSet<Integer> helper = new HashSet<>();
        for(Player trg : targets){
            if(!helper.add(trg.getPosition())) return false;
        }
        return true;
    }
}
