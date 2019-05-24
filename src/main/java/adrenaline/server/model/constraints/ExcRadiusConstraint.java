package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

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
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        validSquares.removeAll(invalidSquares);
        return validSquares;
    }
}
