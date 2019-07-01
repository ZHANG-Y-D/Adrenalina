package adrenaline.server.model;

import adrenaline.server.controller.states.FiremodeSubState;
import adrenaline.server.controller.states.MoveSelfState;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsConstraint;

import java.util.*;

public class Firemode {

    private int[] extraCost;
    private ArrayList<RangeConstraint> rngConstraints;
    private ArrayList<TargetsConstraint> trgConstraints;
    private Queue<FiremodeSubState> firemodeSteps = new LinkedList<>();
    private int allowedMovement;
    
    public int[] getExtraCost(){
        return extraCost;
    }

    public FiremodeSubState getNextStep(){ return firemodeSteps.poll(); }

    public MoveSelfState getMoveSelfStep() {
        if(allowedMovement>0){
            int movement = allowedMovement;
            allowedMovement=0;
            return new MoveSelfState(movement);
        }else return null;
    }

    public ArrayList<Integer> getRange(int shooterPosition, Map map){
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        for(int i = 0; i<= map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        rngConstraints.forEach(x -> validSquares.retainAll(x.checkConst(shooterPosition, map)));
        return validSquares;
    }

    public boolean checkTargets(Player shooter, ArrayList<Player> targets, Map map) {
        ArrayList<Integer> validSquares = getRange(shooter.getPosition(), map);
        for(Player trg : targets) {
            if (!validSquares.contains(trg.getPosition()) && (targets.indexOf(trg)==0 || trgConstraints.stream().noneMatch(TargetsConstraint::isSpecialRange)))
                return false;
        }
        for(TargetsConstraint trgconst : trgConstraints){
            if(!trgconst.checkConst(shooter, targets, map)) return false;
        }
        return true;
    }
}