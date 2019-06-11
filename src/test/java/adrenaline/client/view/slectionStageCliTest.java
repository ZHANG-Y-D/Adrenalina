package adrenaline.client.view;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.fusesource.jansi.Ansi.ansi;

public class slectionStageCliTest {



    @Test
    void mapFilePrintTest(){

        printSrcFile("GameStareTitle.txt");

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
}