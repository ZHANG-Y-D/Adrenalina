package adrenaline.client.view;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.fusesource.jansi.Ansi.ansi;

public class selectionStageCliTest {



    @Test
    void mapFilePrintTest(){

        printSrcFile("MapSelection.txt");
        printSrcFile("Map1.txt");
        printSrcFile("Map2.txt");
        printSrcFile("Map3.txt");
        printSrcFile("Map4.txt");






    }

    @Test
    void powerupFilePrintTest() {
        for (int i=1;i<=24;i++)
            printSrcFile("Powerup"+i+".txt");
    }

    @Test
    void weaponFilePrintTest() {

        for (int i=1;i<=21;i++)
            printSrcFile("Weapon"+i+".txt");

    }

    protected static void printSrcFile(String srcFileName){

        try(FileReader fileReader = new FileReader("src/main/resources/ForCli/" + srcFileName)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String string = bufferedReader.readLine();
            while (string != null){
                System.out.println(ansi().eraseScreen().render(string) );
                string=bufferedReader.readLine();
            }
        }catch (FileNotFoundException e){
            System.out.println("\nsrc/main/resources/ForCli/"+srcFileName+"  File Not Found ");
        }catch (IOException e){
            System.out.println("\n IOException ");
        }
    }

    @Test
    synchronized void notifyTimerTest(){





    }


}
