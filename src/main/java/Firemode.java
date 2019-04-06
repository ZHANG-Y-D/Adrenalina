import java.util.ArrayList;

public class Firemode {
    private int extraCost;
    private int targetLimit;
    private ArrayList<RangeConstraint> rngConstraints;
    private ArrayList<TargetsContraint> trgConstraints;

    public int getExtraCost(){
        return extraCost;
    }

    public void fire(){
        ArrayList<Integer> validSquares = new ArrayList<Integer>();
        for(RangeConstraint rc : rngConstraints){
            validSquares.addAll(rc.checkConst());
        }

    }

}
