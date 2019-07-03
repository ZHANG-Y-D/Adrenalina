package adrenaline.client.view.GuiView;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class EndGameViewController {

    @FXML
    private Pane pane;
    @FXML
    private Label winner, winnerText;
    @FXML
    private Button close;
    @FXML
    private HBox losers;
    private HashMap<Color, String> nicknamesMap  = new HashMap<>();
    private GameController gameController;

    public void initialize(){
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 80);
        winnerText.setFont(font);
        winnerText.getStyleClass().add("WHITE");
        font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 20);
        close.setFont(font);
        //test();
    }

    public void setController(GameController gameController){
        this.gameController = gameController;
        setPlayers();
    }

    private void setPlayers(){
        Map<Color, Integer> playersMap = gameController.getScoreBoard().getFinalPlayersPosition();
        gameController.getPlayersNicknames().forEach((x,y) -> {
            nicknamesMap.put(y,x);
        });
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 50);
        Font font2 = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 30);
        playersMap.forEach((x,y) -> {
            if(y == 1){
                winner.getStyleClass().add(x.toString());
                winner.setFont(font);
                winner.setText(nicknamesMap.get(x));
            }
            else {
                VBox vBox = new VBox();
                Label pos = new Label();
                pos.setText("POS: "+y);
                pos.setFont(font);
                pos.getStyleClass().add("WHITE");
                vBox.getChildren().add(pos);
                Label name = new Label();
                name.setText(nicknamesMap.get(x));
                name.setFont(font2);
                name.getStyleClass().add(x.toString());
                vBox.getChildren().add(name);
                losers.getChildren().add(vBox);
                losers.setLayoutX(losers.getLayoutX() - 145);
                losers.setSpacing(losers.getSpacing() - 10);
            }
        });
        animation();
    }

    private void animation(){
        ArrayList<Node> playersList = new ArrayList<>(losers.getChildren());
        Collections.reverse(playersList);
        playersList.add(winner);
        SequentialTransition sequence = new SequentialTransition();
        playersList.forEach(x -> {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(4), x);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            sequence.getChildren().add(fadeIn);
        });
        sequence.play();
    }

    /*public void test(){
        Map<Color, Integer> playersMap = new LinkedHashMap<>();
        playersMap.put(Color.YELLOW, 1);
        playersMap.put(Color.BLUE,2);
        playersMap.put(Color.GREEN,3);
        playersMap.put(Color.PURPLE,4);
        playersMap.put(Color.GRAY,5);
        nicknamesMap.put(Color.YELLOW,"Scianna");
        nicknamesMap.put(Color.BLUE,"Rick");
        nicknamesMap.put(Color.GREEN,"Charlie");
        nicknamesMap.put(Color.PURPLE,"Guido");
        nicknamesMap.put(Color.GRAY,"Luca");
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 50);
        Font font2 = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 30);
        playersMap.forEach((x,y) -> {
            if(y == 1){
                winner.getStyleClass().add(x.toString());
                winner.setFont(font);
                winner.setText(nicknamesMap.get(x));
            }
            else {
                VBox vBox = new VBox();
                Label pos = new Label();
                pos.setText("POS: "+y);
                pos.setFont(font);
                pos.getStyleClass().add("WHITE");
                vBox.getChildren().add(pos);
                Label name = new Label();
                name.setText(nicknamesMap.get(x));
                name.setFont(font2);
                name.getStyleClass().add(x.toString());
                vBox.getChildren().add(name);
                losers.getChildren().add(vBox);
                losers.setLayoutX(losers.getLayoutX() - 145);
                losers.setSpacing(losers.getSpacing() - 10);
            }
        });
        animation();
    }*/

    public void close(){
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            gameController.cleanExit();
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.close();
        }
    }
}