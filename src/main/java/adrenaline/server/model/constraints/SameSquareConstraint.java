package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;

/**
 * This is used both as a targets constraint and as a targets generator.
 * Checks whether the targets are on the same square or generates the single square additional target.
 */
public class SameSquareConstraint implements TargetsConstraint, TargetsGenerator{

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSpecialRange() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        Integer square = targets.get(0).getPosition();
        for(Player trg : targets){
            if(square!=trg.getPosition()) return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<>();
        validSquares.add(root);
        return validSquares;
    }
}
