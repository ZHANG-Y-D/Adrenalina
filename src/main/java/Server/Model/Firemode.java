package Server.Model;

import Server.InvalidTargetsException;

import java.util.ArrayList;

public class Firemode {
    private int extraCost;
    private int targetLimit;
    private ArrayList<MovementEffect> mvEffects;
    private ArrayList<RangeConstraint> rngConstraints;
    private ArrayList<TargetsConstraint> trgConstraints;
    private ArrayList<Integer[]> dmgmrkToEachTarget;

    public int getExtraCost(){
        return extraCost;
    }

    public ArrayList<MovementEffect> getMovementEffects() { return mvEffects; }

    public ArrayList<Integer> getRange(int shooterPosition, Map map){
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        // 3 must be replaced by map.getRows and 4 by map.getColumns
        int maxSquare = map.getRows()*map.getColumns() -1;
        for(int i = 0; i<= maxSquare; i++){
            validSquares.add(i);
        }
        for(RangeConstraint rc : rngConstraints){
            validSquares.retainAll(rc.checkConst(shooterPosition, map));
        }
        return validSquares;
    }


    public ArrayList<Integer[]> fire(ArrayList<Player> targets, ArrayList<Integer> invalidSquares) throws InvalidTargetsException {
        for(Player trg : targets) {
            if (invalidSquares.contains(trg.getPosition())){
                if(trgConstraints.stream().noneMatch(TargetsConstraint::isSpecialRange)) throw new InvalidTargetsException();
            }
        }
        for(TargetsConstraint trgconst : trgConstraints){
            if(!trgconst.checkConst(targets)) throw new InvalidTargetsException();
        }
        //TARGETS VALID
        ArrayList<Integer[]> returnToEachTarget = new ArrayList<>();
        for(Player trg : targets) {
            returnToEachTarget.add(dmgmrkToEachTarget.get(targets.indexOf(trg) < dmgmrkToEachTarget.size() ? targets.indexOf(trg) : dmgmrkToEachTarget.size()-1));
        }
        return returnToEachTarget;
    }

}