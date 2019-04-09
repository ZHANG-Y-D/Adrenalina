package Server.Model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;

public class Map {

    //playerPosition
    private char[][] mapRooms;
    private ArrayList<int[]> mapWalls;
    private int rows;
    private int column;
    //private WeaponCard[] redWeapons;
    //private WeaponCard[] blueWeapons;
    //private WeaponCard[] yellowWeapons;

    //TODO add weapon
    public Map(int num, int rows, int column){
        this.rows = rows;
        this.column = column;
        String path = "FILE/Map" + num + ".txt";
        buildMap(path);
    }

    private void buildMap(String path){
        try{
            File file = new File(path);

            Scanner scanner = new Scanner(file);

            mapRooms = new char[rows][column];
            mapWalls = new ArrayList<>();

            for(int i = 0; i < rows; i++) {
                    for (int j = 0; j < column; j++) {
                            mapRooms[i][j] = scanner.next().charAt(0);
                    }
            }

            while(scanner.hasNextLine()){
                int[] w1 = {scanner.nextInt(), scanner.nextInt()};
                int[] w2 = {scanner.nextInt(), scanner.nextInt()};
                mapWalls.add(w1);
                mapWalls.add(w1);
            }
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void printMap(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(mapRooms[i][j]);
            }
            System.out.println();
        }

    }

    public char getSquare(int i, int j){
        return mapRooms[i][j];
    }

    public int getColumn() {
        return column;
    }

    public int getRows() {
        return rows;
    }

    public ArrayList<Integer> getValidSquares(Player p, int num){
        ArrayList<Integer> validSquares = new ArrayList<>();
        int playerX = p.getPosition()%column;
        int playerY = (p.getPosition() - playerX)/column;




        return validSquares;
    }

    public boolean isThereWall(int x1, int y1, int x2, int y2){
        int pos1 = y1*4 + x1;
        int pos2 = y2*4 + x2;
        int []arrayPos = new int[2];
        Arrays.sort(arrayPos);
        for(int[] i : mapWalls) {
            if (arrayPos == i)  return true;
        }
        return false;
    }

}