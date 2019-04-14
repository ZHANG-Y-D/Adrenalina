package Server.Model.Constraints;

import Server.Model.Map;
import Server.Model.RangeConstraint;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SightConstraint implements RangeConstraint {
    @Override
    public ArrayList<Integer> checkConst(int shooterPosition, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<Integer>(map.getValidSquares(shooterPosition,1));
        ArrayList<Integer> adjacentRooms = new ArrayList<>();
        for(int i : validSquares){
            adjacentRooms.addAll(map.getRoomSquares(i));
        }
        validSquares.addAll(adjacentRooms);
        validSquares = (ArrayList<Integer>) validSquares.stream().distinct().collect(Collectors.toList());
        return validSquares;
    }
}
