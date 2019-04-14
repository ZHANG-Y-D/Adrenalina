package server.model.constraints;

import server.model.Map;
import server.model.RangeConstraint;

import java.util.ArrayList;

public class ExcRadiusConstraint implements RangeConstraint {
    private int radius;

    public ExcRadiusConstraint(int radius){
        this.radius = radius;
    }
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
