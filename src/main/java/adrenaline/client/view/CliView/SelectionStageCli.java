package adrenaline.client.view.CliView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;
import org.fusesource.jansi.Ansi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.fusesource.jansi.Ansi.ansi;


public class SelectionStageCli extends ControllerCli implements ViewInterface, PropertyChangeListener {



    private volatile boolean selectAvatarFinished = false;


    public SelectionStageCli(GameController gameController) {

        this.gameController = gameController;
        gameController.setViewController(this);
        gameController.addPropertyChangeListener(this);
        returnValueIsOk.set(0);
        initialStageCli();

    }

    @Override
    protected void initialStageCli() {

        printPlayerInfo();
        printSrcFile("Avatar.txt");


        do {
            if (selectAvatarFinished)
                break;
            selectAvatar();
        }while (listenerReturnValueIsOK());

        printPlayerInfo();

        selectMapAndSkulls();

        System.out.println("Wait for Game Start...");

    }

    private void selectMapAndSkulls() {

        int mapNum;
        int skulls;

        printSrcFile("Map.txt");
        System.out.println("Which map do you want to play?");

        mapNum = readANumber();

        System.out.println("How many skulls you want to play? From 5 to 8");

        skulls = readANumber();

        if (mapNum < 1 || mapNum > 4 || skulls < 5 || skulls > 8){

            System.err.println("Wrong range！！！ Map from 1 to 4, skull from 5 to 8!");
            System.err.println("Please retry.\n");
            selectMapAndSkulls();

        }

        gameController.sendSettings(mapNum,skulls);

    }


    private void selectAvatar() {

        int num;
        Color color = Color.BLACK;

        System.out.println("Which you like? From 1 to 5: ");
        num = readANumber();


        switch (num){
            case 1:
                color = Color.YELLOW;
                break;
            case 2:
                color = Color.BLUE;
                break;
            case 3:
                color = Color.GRAY;
                break;
            case 4:
                color = Color.PURPLE;
                break;
            case 5:
                color = Color.GREEN;
                break;
            default:
                System.err.println("Please answer with a number from 1 to 5.");
                selectAvatar();
        }

        returnValueIsOk.set(0);
        gameController.selectAvatar(color);

    }



    private void printPlayerInfo() {

        int num=1;

        System.out.println("\nThese players are in the lobby:");
        for (Map.Entry<String, Color> players : gameController.getPlayersNicknames().entrySet()) {

            Color color = players.getValue();


            switch (color) {
                case YELLOW:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.YELLOW).a(num+"."+players.getKey()));
                    break;
                case BLUE:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.BLUE).a(num+"."+players.getKey()));
                    break;
                case PURPLE:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.MAGENTA).a(num+"."+players.getKey()));
                    break;
                case GRAY:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.WHITE).a(num+"."+players.getKey()));
                    break;
                case GREEN:
                    System.out.println(ansi().eraseScreen().fg(Ansi.Color.GREEN).a(num+"."+players.getKey()));
                    break;
                default:
                    System.out.println(ansi().eraseScreen().fgDefault().a(num+"."+players.getKey()));
                    break;
            }
            num++;
        }
    }


    @Override
    public void showError(String error) {

        if (!error.equals("/OK")) {
            System.err.println(error);
            returnValueIsOk.set(2);
        }
        else{
            returnValueIsOk.set(1);
        }

    }


    @Override
    public void changeStage() {


        new GameStageCli(gameController);

    }


    @Override
    public void setGameController(GameController gameController) {

    }


    @Override
    public void notifyTimer(Integer duration) {

        System.out.println("You still have "+duration+" Secondi");

        //TODO for Timer
        try {
            sleep(duration);
        }catch (InterruptedException e){
            selectAvatarFinished = true;
        }
        selectAvatarFinished = true;

    }



    @Override
    public void newChatMessage(String nickname, Color senderColor, String message) {
        //called when server notifies client of a new message in chat
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if(evt.getPropertyName().equals("map"))
            changeStage();

    }
}
