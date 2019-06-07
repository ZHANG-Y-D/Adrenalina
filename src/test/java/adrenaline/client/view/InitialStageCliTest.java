package adrenaline.client.view;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.fusesource.jansi.Ansi.ansi;

public class InitialStageCliTest {

    //to delete because test must be automatic
    /*@Test
    void InitialTest() {

        InitialStageCli initialCli = new InitialStageCli();
        initialCli.initialStageCli();
    }*/

    @Test
    void ColorPrintTest() {

        for (int i=0; i<=300;i++ ) {
            System.out.println("\u001B[" + i + "m This is for Test \u001B[0m i = " + i);

        }

        String printString = "\u001B[41m \"name\": \":D-STRUCT-OR\" \u001B[0m";

        System.out.println(printString);
    }

    @Test
    void printSrcFileTest() {

        String srcFileName = "Avatar.txt";

        try(FileReader fileReader = new FileReader("src/main/resources/ForCli/" + srcFileName)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String string = bufferedReader.readLine();
            while (string != null){
                System.out.println(ansi().eraseScreen().render(string) );
                string=bufferedReader.readLine();
            }
        }catch (FileNotFoundException e){
            System.out.println("\nsrc/main/resources/ForCli/CliBegin.txt  File Not Found ");
        }catch (IOException e){
            System.out.println("\n IOException ");
        }

    }

    @Test
    void jansiTest() {

        AnsiConsole.systemInstall();
        System.out.println(ansi().eraseScreen().fg(Ansi.Color.RED).a("Hello").fg(Ansi.Color.GREEN).a(" World").reset());
        AnsiConsole.systemUninstall();

        String string="ASDFGH";
        System.out.println(ansi().eraseScreen().fg(Ansi.Color.WHITE).a(string));
    }


}
