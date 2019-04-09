package Server.Model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Map {

    //playerPosition
    private char[][] mapRooms;
    private ArrayList<int[]> mapWalls;
    private int rows;
    private int columns;
    //private WeaponCard[] redWeapons;
    //private WeaponCard[] blueWeapons;
    //private WeaponCard[] yellowWeapons;

    //TODO add weapon
    public Map(int num, int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        String path = "FILE/Map" + num + ".txt";
        buildMap(path);
    }

    private void buildMap(String path){
        try{
            File file = new File(path);

            Scanner scanner = new Scanner(file);

            mapRooms = new char[rows][columns];
            mapWalls = new ArrayList<>();

            for(int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                            mapRooms[i][j] = scanner.next().charAt(0);
                    }
            }

            while(scanner.hasNextLine()){
                int[] w1 = {scanner.nextInt(), scanner.nextInt()};
                mapWalls.add(w1);
            }
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void printMap(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                System.out.print(mapRooms[i][j]);
            }
            System.out.println();
        }
    }

    public char getSquare(int i, int j){
        return mapRooms[i][j];
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public ArrayList<Integer> getValidSquares(Player p, int num){
        ArrayList<Integer> validSquares = new ArrayList<>();

        validSquares.add(p.getPosition());
        int nodeX, nodeY;
        int size;
        int oldSize = 0;
        for(int i = 0; i < num; i++){
            size = validSquares.size();
            for(int j = oldSize; j < size; j++){
                nodeX = validSquares.get(j) % columns;
                nodeY = validSquares.get(j)/columns;
                if((nodeX+1 < columns)&&(nodeX+1 >= 0)&&(mapRooms[nodeY][nodeX+1] != 'x')&&(!isWall(nodeX,nodeY,nodeX+1,nodeY))) validSquares.add((nodeY*4) + (nodeX+1));
                if((nodeX-1 < columns)&&(nodeX-1 >= 0)&&(mapRooms[nodeY][nodeX-1] != 'x')&&(!isWall(nodeX,nodeY,nodeX-1,nodeY))) validSquares.add(nodeY*4 + nodeX-1);
                if((nodeY+1 < rows)&&(nodeY+1 >= 0)&&(mapRooms[nodeY+1][nodeX] != 'x')&&(!isWall(nodeX,nodeY,nodeX,nodeY+1))) validSquares.add((nodeY+1)*4 + nodeX);
                if((nodeY-1 < rows)&&(nodeY-1 >= 0)&&(mapRooms[nodeY-1][nodeX] != 'x')&&(!isWall(nodeX,nodeY,nodeX,nodeY-1))) validSquares.add((nodeY-1)*4 + nodeX);
            }
            validSquares = (ArrayList<Integer>) validSquares.stream().distinct().collect(Collectors.toList());
            oldSize = size;
        }
        return validSquares;
    }

    public boolean isWall(int x1, int y1, int x2, int y2){
        int pos1 = y1*4 + x1;
        int pos2 = y2*4 + x2;
        int []arrayPos = {pos1,pos2};
        Arrays.sort(arrayPos);
        for(int[] i : mapWalls) {
            if (Arrays.equals(i,arrayPos)) return true;
        }
        return false;
    }

}