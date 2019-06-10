package adrenaline.server.model.constraints;

import adrenaline.server.model.Map;

import java.util.ArrayList;

public interface TargetsGenerator {
    ArrayList<Integer> generateRange(Integer shooterPos, Integer root, Map map );
}
