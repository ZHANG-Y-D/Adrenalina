import java.util.ArrayList;

public class MapD implements MapBuilder {

    @Override
    public char[][] buildRooms(){
        char[][] map = new char[3][4];

        map[0][0] = 'r';
        map[0][1] = 'b';
        map[0][2] = 'B';
        map[0][3] = 'x';
        map[1][0] = 'R';
        map[1][1] = 'p';
        map[1][2] = 'p';
        map[1][3] = 'y';
        map[2][0] = 'w';
        map[2][1] = 'w';
        map[2][2] = 'w';
        map[2][3] = 'Y';

        return map;
    }

    @Override
    public ArrayList<int[]> buildWalls() {
        ArrayList<int[]> walls = new ArrayList<>();
        int[] w1 = {4,5};
        int[] w2 = {6,10};

        walls.add(w1);
        walls.add(w2);

        return walls;
    }
}