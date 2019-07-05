package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 * This constraint checks whether the squares are in the sight of the player executing the action.
 */
public class InSightConstraint implements RangeConstraint {

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> checkConst(int position, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<Integer>(map.getValidSquares(position,1));
        ArrayList<Integer> adjacentRooms = new ArrayList<>();
        for(int i : validSquares){
            adjacentRooms.addAll(map.getRoomSquares(i));
        }
        validSquares.addAll(adjacentRooms);
        validSquares = (ArrayList<Integer>) validSquares.stream().distinct().collect(Collectors.toList());
        return validSquares;
    }
}
