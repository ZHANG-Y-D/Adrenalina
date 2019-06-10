package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;
import java.util.Set;

public class InRadiusConstraint implements RangeConstraint, TargetsGenerator{
    private int radius;

    public InRadiusConstraint(int radius){
        this.radius = radius;
    }

    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        return new ArrayList<>(map.getValidSquares(shooterPosition,radius));
    }

    @Override
    public ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map) {
        return new ArrayList<>(map.getValidSquares(shooterPos,radius));
    }
}
