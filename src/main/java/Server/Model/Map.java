package Server.Model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Map {

    //playerPosition
    private char[][] mapRooms;
    private ArrayList<int[]> mapWalls;
    //private Server.Model.WeaponCard[] redWeapons;
    //private Server.Model.WeaponCard[] blueWeapons;
    //private Server.Model.WeaponCard[] yellowWeapons;

    //TODO add weapon
    public Map(int num, int rows, int column){
        String path = "FILE/Server.Model.Map" + num + ".txt";
        buildMap(path, rows, column);
    }

    private void buildMap(String path, int rows, int column){
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
}