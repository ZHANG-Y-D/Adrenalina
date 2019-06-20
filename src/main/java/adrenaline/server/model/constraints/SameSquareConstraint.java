package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;
import adrenaline.server.model.Player;

import java.util.ArrayList;

public class SameSquareConstraint implements TargetsConstraint, TargetsGenerator{
    @Override
    public boolean isSpecialRange() {
        return false;
    }

    @Override
    public boolean checkConst(Player shooter, ArrayList<Player> targets, Map map) {
        Integer square = targets.get(0).getPosition();
        for(Player trg : targets){
            if(square!=trg.getPosition()) return false;
        }
        return true;
    }

    @Override
    public ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<>();
        validSquares.add(root);
        return validSquares;
    }
}
