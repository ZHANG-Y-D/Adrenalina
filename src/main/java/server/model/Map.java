package server.model;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import server.controller.PlayerShell;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the map during a Game.
 */

public class Map {

    //playerPosition
    private Square[][] mapSquares;
    private ArrayList<int[]> mapWalls;
    private int rows;
    private int columns;
    //private WeaponCard[] redWeapons;
    //private WeaponCard[] blueWeapons;
    //private WeaponCard[] yellowWeapons;
    

    public void printMap(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                System.out.print(mapSquares[i][j].getColor());
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
                if((nodeX+1 < columns)&&(nodeX+1 >= 0)&&(mapSquares[nodeY][nodeX+1].getColor() != Color.BLACK)&&(!isWall(nodeX,nodeY,nodeX+1,nodeY))) validSquares.add((nodeY*4) + (nodeX+1));
                if((nodeX-1 < columns)&&(nodeX-1 >= 0)&&(mapSquares[nodeY][nodeX-1].getColor() != Color.BLACK)&&(!isWall(nodeX,nodeY,nodeX-1,nodeY))) validSquares.add(nodeY*4 + nodeX-1);
                if((nodeY+1 < rows)&&(nodeY+1 >= 0)&&(mapSquares[nodeY+1][nodeX].getColor() != Color.BLACK)&&(!isWall(nodeX,nodeY,nodeX,nodeY+1))) validSquares.add((nodeY+1)*4 + nodeX);
                if((nodeY-1 < rows)&&(nodeY-1 >= 0)&&(mapSquares[nodeY-1][nodeX].getColor() != Color.BLACK)&&(!isWall(nodeX,nodeY,nodeX,nodeY-1))) validSquares.add((nodeY-1)*4 + nodeX);
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

    /**
     * Get all the squares that are in the same room
     * of the given square.
     *
     * @param pos   is the position of the first square
     * @return      the list of the squares in the room
     */

    public ArrayList<Integer> getRoomSquares(int pos){
        ArrayList<Integer> roomSquares = new ArrayList<>();
        Color roomColor = mapSquares[pos/columns][pos%columns].getColor();
        for (int i = 0; i < rows ; i++){
            for(int j = 0; j < columns; j++){
                if(roomColor == mapSquares[i][j].getColor())
                    roomSquares.add(i*columns + j);
            }
        }
        return roomSquares;
    }

    /**
     * Returns <code>true</code> if the two given
     * squares are in the same row or in the same column.
     *
     * @param pos1  is the position of the first square
     * @param pos2  is the position of the second square
     * @return      <code>true</code> if the squares are in the same row or column;
     *              <code>false</code> otherwise
     */

    public boolean areAligned(int pos1, int pos2){
        if((pos1%columns == pos2%columns)||(pos1/columns == pos2/columns)) return true;
        else return false;
    }

    /**
     * Returns the number of squares of the map.
     *
     * @return  the number of squares
     */

    public int getMaxSquare(){
        return rows*columns -1;
    }

    /**
     * Returns <code>true</code> if the given square
     * is empty.
     *
     * @param pos   the position of the square
     * @return      <code>true</code> if the square is empty;
     *              <code>false</code> otherwise
     */

    public boolean isEmptySquare(int pos) {
        return mapSquares[pos/columns][pos%columns].getColor() == Color.BLACK;
    }
}