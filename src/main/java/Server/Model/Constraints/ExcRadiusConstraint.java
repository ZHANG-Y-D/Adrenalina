package Server.Model.Constraints;

import Server.Model.Map;
import Server.Model.RangeConstraint;

import java.util.ArrayList;

public class ExcRadiusConstraint implements RangeConstraint {
    private int radius;

    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> invalidSquares = new ArrayList<Integer>(map.getValidSquares(shooterPosition,radius));
        ArrayList<Integer> validSquares = new ArrayList<>();
        for(int i=0; i<=map.getMaxSquare(); i++){
            validSquares.add(i);
        }
        validSquares.removeAll(invalidSquares);
        return validSquares;
    }
}