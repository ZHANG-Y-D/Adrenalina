package adrenaline.client.view;

import adrenaline.client.controller.GameController;
import adrenaline.client.view.CliView.SelectionStageCli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.fusesource.jansi.Ansi.ansi;

public class selectionStageCliTest {


    SelectionStageCli selectionStageCli;
    GameController gameController;


    @BeforeEach
    void setUp() {


        Runnable runnable = ()-> {
            gameController= new GameController();
            gameController.setOwnNickname("zhang");
            selectionStageCli = new SelectionStageCli(gameController);
        };

        Thread thread = new Thread(runnable);

        thread.start();


    }

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

    @Test
    synchronized void notifyTimerTest(){





    }


}
