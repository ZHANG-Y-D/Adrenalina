package Server.Model.Constraints;

import Server.Model.Map;
import Server.Model.RangeConstraint;

import java.util.ArrayList;

public class CardinalDirectionConstraint implements RangeConstraint {
    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<>();
        for(int i=0; i<=map.getMaxSquare(); i++){
            if(map.areAligned(shooterPosition,i)) validSquares.add(i);
        }
        return validSquares;
    }
}
