package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is used both as a targets constraint and as a targets generator.
 * Checks whether the targets are in the same room or generates all targets in the same room.
 */
public class SameRoomConstraint implements TargetsConstraint, TargetsGenerator {
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
        ArrayList<Integer> room = map.getRoomSquares(targets.get(0).getPosition());
        for(Player trg : targets){
            if(!(room.contains(trg.getPosition()))) return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map) {
        Set<Integer> validSquares = new LinkedHashSet<Integer>();
        validSquares.add(root);
        validSquares.addAll(map.getRoomSquares(root));
        return new ArrayList<>(validSquares);
    }
}
