import java.util.ArrayList;

public class Map {

    //playerPosition
    private char[][] mapRooms;
    private ArrayList<int[]> mapWalls = new ArrayList<>();
    //private WeaponCard[] redWeapons;
    //private WeaponCard[] blueWeapons;
    //private WeaponCard[] yellowWeapons;
    private MapBuilder mapBuilder;

    //TODO add weapon
    public Map(MapBuilder m){
        mapRooms = new char[3][4];
        mapBuilder = m;
    }

    public void buildMap(){
        mapRooms = mapBuilder.buildRooms();
        mapWalls = mapBuilder.buildWalls();
    }

    public void printMap(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(getSquare(i,j));
            }
            System.out.println();
        }

    }

    public char getSquare(int i, int j){
        return mapRooms[i][j];
    }
}