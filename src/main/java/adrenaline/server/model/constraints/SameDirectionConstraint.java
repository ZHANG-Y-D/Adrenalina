package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class SameDirectionConstraint extends TargetsConstraint implements TargetsGenerator {
    protected static boolean specialRange = false;

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

    @Override
    public ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map) {
        Set<Integer> validSquares = new LinkedHashSet<>();
        validSquares.add(root);
        for(int i=0; i<=map.getMaxSquare(); i++){
            if(map.areAligned(shooterPos,i) && map.areAligned(root, i) && ((root > shooterPos && i > shooterPos) || (root < shooterPos && i < shooterPos))
                && !map.isWall(root,i))
                validSquares.add(i);
        }
        return new ArrayList<>(validSquares);
    }
}
