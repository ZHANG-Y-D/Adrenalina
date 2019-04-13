package Server.Model.Constraints;

import Server.Model.Map;
import Server.Model.RangeConstraint;

import java.util.ArrayList;

public class InRadiusConstraint implements RangeConstraint {
    private int radius;

    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<Integer>(map.getValidSquares(shooterPosition,radius));
        return validSquares;
    }
}
