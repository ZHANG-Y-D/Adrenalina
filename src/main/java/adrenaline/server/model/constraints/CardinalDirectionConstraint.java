package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;

/**
 * {@inheritDoc}
 * This constraint checks whether the squares are aligned to the player executing the action, in one of the four cardinal directions.
 */
public class CardinalDirectionConstraint implements RangeConstraint {

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Integer> checkConst(int position, Map map) {
        ArrayList<Integer> validSquares = new ArrayList<>();
        for(int i=0; i<=map.getMaxSquare(); i++){
            if(map.areAligned(position,i)) validSquares.add(i);
        }
        return validSquares;
    }
}
