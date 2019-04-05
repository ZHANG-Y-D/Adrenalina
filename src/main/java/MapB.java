import java.util.ArrayList;

public class Map_B implements MapBuilder{


    @Override
    public char[][] buildRooms() {
        char[][] map = new char[3][4];

        map[0][0] = 'b';
        map[0][1] = 'b';
        map[0][2] = 'B';
        map[0][3] = 'g';
        map[1][0] = 'R';
        map[1][1] = 'r';
        map[1][2] = 'y';
        map[1][3] = 'y';
        map[2][0] = 'x';
        map[2][1] = 'w';
        map[2][2] = 'y';
        map[2][3] = 'Y';
        return map;
    }

    @Override
    public ArrayList<int[]> buildWalls() {
        ArrayList<int[]> walls = new ArrayList<>();
        int[] w1 = {1,5};
        int[] w2 = {5,6};

        walls.add(w1);
        walls.add(w2);

        return walls;
    }
}
