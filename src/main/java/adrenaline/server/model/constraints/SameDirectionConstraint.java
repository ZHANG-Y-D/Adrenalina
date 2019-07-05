package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is used both as a targets constraint and as a targets generator.
 * Checks whether the targets are in the same direction (coming from the player executing the action) or generates all the targets that are in the same direction.
 */
public class SameDirectionConstraint implements TargetsConstraint, TargetsGenerator {
    private boolean specialRange;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSpecialRange() {
        return specialRange;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        for(Player trg1 : targets){
            for(Player trg2 : targets){
                if(!map.areAligned(trg1.getPosition(), trg2.getPosition()) ||
                    (trg1.getPosition()> shooter.getPosition() && trg2.getPosition()< shooter.getPosition())||
                        (trg1.getPosition()< shooter.getPosition() && trg2.getPosition()> shooter.getPosition())) return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map) {
        Set<Integer> validSquares = new LinkedHashSet<>();
        validSquares.add(root);
        for(int i=root; 0<=i && i<=map.getMaxSquare(); i= (root<shooterPos)? i-1 : i+1){
            if(map.areAligned(shooterPos,i) && map.areAligned(root, i) && !map.isWall(root,i))
                validSquares.add(i);
        }
        return new ArrayList<>(validSquares);
    }
}
