package server.model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the map during a Game.
 */

public class Map {

    //playerPosition
    private final char[][] mapSquares;
    private ArrayList<int[]> mapWalls;
    private final int rows;
    private final int columns;
    //private WeaponCard[] redWeapons;
    //private WeaponCard[] blueWeapons;
    //private WeaponCard[] yellowWeapons;

    /**
     * Constructs a map with the given rows and columns
     * and creates the file path using the given map number.
     *
     * @param num       the number of the selected map
     * @param rows      the number of rows of the map
     * @param columns   the number of columns of the map
     */

    //TODO add weapon
    public Map(int num, int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        mapSquares = new char[rows][columns];
        mapWalls = new ArrayList<>();
        String path = "FILE/Map" + num + ".txt";
        buildMap(path);
    }

    /**
     * Builds the map taking the features from
     * the file with the given path.
     *
     * @param path   the path of the file
     * @throws FileNotFoundException if the path is wrong and the file is not found
     */

    private void buildMap(String path){
        try{
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            for(int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                            mapSquares[i][j] = scanner.next().charAt(0);
                    }
            }
            while(scanner.hasNextLine()){
                int[] w1 = {scanner.nextInt(), scanner.nextInt()};
                mapWalls.add(w1);
            }
            scanner.close();
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void printMap(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                System.out.print(mapSquares[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Gets the number of columns of the map.
     *
     * @return  the number of columns
     */

    public int getColumns() {
        return columns;
    }

    /**
     * Gets the number of rows of the map.
     *
     * @return  the number of rows
     */

    public int getRows() {
        return rows;
    }

    /**
     * Gets all the valid squares in which
     * the player can move from the given position.
     *
     * @param pos   is the position of the player that wants to move
     * @param num   is the maximum number of moves
     * @return      the list of valid squares
     */

    public ArrayList<Integer> getValidSquares(int pos, int num){
        ArrayList<Integer> validSquares = new ArrayList<>();

        validSquares.add(pos);
        int nodeX, nodeY;
        int size;
        int oldSize = 0;
        for(int i = 0; i < num; i++){
            size = validSquares.size();
            for(int j = oldSize; j < size; j++){
                nodeX = validSquares.get(j) % columns;
                nodeY = validSquares.get(j)/columns;
                if((nodeX+1 < columns)&&(nodeX+1 >= 0)&&(mapSquares[nodeY][nodeX+1] != 'x')&&(!isWall(nodeX,nodeY,nodeX+1,nodeY))) validSquares.add((nodeY*4) + (nodeX+1));
                if((nodeX-1 < columns)&&(nodeX-1 >= 0)&&(mapSquares[nodeY][nodeX-1] != 'x')&&(!isWall(nodeX,nodeY,nodeX-1,nodeY))) validSquares.add(nodeY*4 + nodeX-1);
                if((nodeY+1 < rows)&&(nodeY+1 >= 0)&&(mapSquares[nodeY+1][nodeX] != 'x')&&(!isWall(nodeX,nodeY,nodeX,nodeY+1))) validSquares.add((nodeY+1)*4 + nodeX);
                if((nodeY-1 < rows)&&(nodeY-1 >= 0)&&(mapSquares[nodeY-1][nodeX] != 'x')&&(!isWall(nodeX,nodeY,nodeX,nodeY-1))) validSquares.add((nodeY-1)*4 + nodeX);
            }
            validSquares = (ArrayList<Integer>) validSquares.stream().distinct().collect(Collectors.toList());
            oldSize = size;
        }
        return validSquares;
    }

    /**
     * Returns <code>true</code> if between the
     * two given squares there is wall.
     *
     * @param x1    is the x-coordinate of the first square
     * @param y1    is the y-coordinate of the first square
     * @param x2    is the x-coordinate of the second square
     * @param y2    is the y-coordinate of the second square
     * @return      <code>true</code> if there is a wall;
     *              <code>false</code> otherwise
     */

    private boolean isWall(int x1, int y1, int x2, int y2){
        int pos1 = y1*4 + x1;
        int pos2 = y2*4 + x2;
        int []arrayPos = {pos1,pos2};
        Arrays.sort(arrayPos);
        for(int[] i : mapWalls) {
            if (Arrays.equals(i,arrayPos)) return true;
        }
        return false;
    }

    public ArrayList<Integer> getRoomSquares(int pos){
        ArrayList<Integer> roomSquares = new ArrayList<>();
        char roomColor = Character.toLowerCase(mapSquares[pos/columns][pos%columns]);
        for (int i = 0; i < rows ; i++){
            for(int j = 0; j < columns; j++){
                if(roomColor == Character.toLowerCase(mapSquares[i][j]))
                    roomSquares.add(i*columns + j);
            }
        }
        return roomSquares;
    }

    public boolean areAligned(int pos1, int pos2){
        if((pos1%columns == pos2%columns)||(pos1/columns == pos2/columns)) return true;
        else return false;
    }

    public int getMaxSquare(){
        return rows*columns -1;
    }

    public boolean isEmptySquare(int pos) {
        return mapSquares[pos/columns][pos%columns] == 'x';
    }
}