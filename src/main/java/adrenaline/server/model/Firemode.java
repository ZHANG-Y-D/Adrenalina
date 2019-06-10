package adrenaline.server.model;

import adrenaline.server.controller.states.FiremodeSubState;
import adrenaline.server.controller.states.GameState;
import adrenaline.server.controller.states.MoveSelfState;
import adrenaline.server.exceptions.InvalidTargetsException;
import adrenaline.server.model.constraints.RangeConstraint;
import adrenaline.server.model.constraints.TargetsConstraint;

import java.util.*;

public class Firemode {
    private String name;
    private int[] extraCost;
    private ArrayList<RangeConstraint> rngConstraints;
    private ArrayList<TargetsConstraint> trgConstraints;
    private Queue<FiremodeSubState> firemodeSteps = new LinkedList<>();
    private MoveSelfState moveSelf = null;

    public Firemode(String name, int[] extraCost, ArrayList<RangeConstraint> rngConst, ArrayList<TargetsConstraint> trgConst){
        this.name = name;
        this.extraCost = extraCost;
        this.rngConstraints = rngConst;
        this.trgConstraints = trgConst;
    }

    public int[] getExtraCost(){
        return extraCost;
    }

    public FiremodeSubState getNextStep(){ return firemodeSteps.poll(); }

    public GameState getMoveSelfStep() { return moveSelf;}

    public ArrayList<Integer> getRange(int shooterPosition, Map map){
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        for(int i = 0; i<= map.getMaxSquare(); i++){
            if(!map.isEmptySquare(i)) validSquares.add(i);
        }
        for(RangeConstraint rc : rngConstraints){
            validSquares.retainAll(rc.checkConst(shooterPosition, map));
        }
        return validSquares;
    }

    public boolean checkTargets(Player shooter, ArrayList<Player> targets, Map map) {
        ArrayList<Integer> validSquares = getRange(shooter.getPosition(), map);
        for(Player trg : targets) {
            if (!validSquares.contains(trg.getPosition())){
                if(targets.indexOf(trg)==0 || trgConstraints.stream().noneMatch(TargetsConstraint::isSpecialRange)) return false;
            }
        }
        for(TargetsConstraint trgconst : trgConstraints){
            if(!trgconst.checkConst(shooter, targets, map)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String string = "Firemode{" +
                "name='" + name + '\'' +
                ", extraCost=" + Arrays.toString(extraCost);
        string += "\n\t\tRange Constraints: ";
        for(RangeConstraint rngConst : rngConstraints){
            string += "\t\t"+ rngConst.getClass().getName() + " ";
        }
        string += "\n\t\tTargets Constraints: ";
        for(TargetsConstraint trgConst : trgConstraints){
            string += "\t\t" + trgConst.getClass().getName() + " ";
        }
        string += "\n\t}";
        return string;
    }
}