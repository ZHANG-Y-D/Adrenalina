package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;


import java.util.ArrayList;

/**
 * {@inheritDoc}
 * This constraint checks whether the targets are in a position between the current and last positions of the player executing the action (in a straight line).
 */
public class ChargeConstraint implements TargetsConstraint {

    /**
     * {@inheritDoc}
     */
    public boolean isSpecialRange() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        if(!map.areAligned(shooter.getPosition(), shooter.getOldPosition())) return false;
        for(Player trg : targets) {
            if (!map.areAligned(shooter.getOldPosition(), trg.getPosition()) || (!map.areAligned(shooter.getPosition(), trg.getPosition())) ||
                    (shooter.getPosition()>= shooter.getOldPosition() && (trg.getPosition()> shooter.getPosition() || trg.getPosition() < shooter.getOldPosition())) ||
                    (shooter.getPosition()<= shooter.getOldPosition() && (trg.getPosition()< shooter.getPosition() || trg.getPosition() > shooter.getOldPosition()))) return false;
        }
        return true;
    }
}
