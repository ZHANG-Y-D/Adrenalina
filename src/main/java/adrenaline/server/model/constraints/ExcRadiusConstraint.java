package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;

/**
 * {@inheritDoc}
 * This constraint checks whether the squares are NOT within a radius of the player executing the action
 */
public class ExcRadiusConstraint implements RangeConstraint {
    private int radius;

    /**
     * {@inheritDoc}
     */
    public ExcRadiusConstraint(int radius){
        this.radius = radius;
    }
    @Override
    public ArrayList<Integer> checkConst(int position, Map map) {
        ArrayList<Integer> invalidSquares = new ArrayList<Integer>(map.getValidSquares(position,radius));
        ArrayList<Integer> validSquares = new ArrayList<>();
        for(int i=0; i<=map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        validSquares.removeAll(invalidSquares);
        return validSquares;
    }
}
