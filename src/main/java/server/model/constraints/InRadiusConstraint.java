package server.model.constraints;

import server.model.Map;
import server.model.RangeConstraint;

import java.util.ArrayList;

public class InRadiusConstraint implements RangeConstraint {
    private int radius;

    public InRadiusConstraint(int radius){
        this.radius = radius;
    }

    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<Integer>(map.getValidSquares(shooterPosition,radius));
        return validSquares;
    }
}
