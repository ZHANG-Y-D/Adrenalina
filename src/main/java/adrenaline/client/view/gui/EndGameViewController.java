package adrenaline.client.view.gui;

import adrenaline.Color;
import adrenaline.client.controller.GameController;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    private Label winnerText;
    @FXML
    private Button close;
    @FXML
    private HBox losersBox, winnersBox;
    private HashMap<Color, String> nicknamesMap  = new HashMap<>();
    private GameController gameController;

    public void initialize(){
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 80);
        winnerText.setFont(font);
        winnerText.getStyleClass().add("WHITE");
        font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 20);
        close.setFont(font);
    }

    public void setController(GameController gameController){
        this.gameController = gameController;
        setPlayers();
    }

    private void setPlayers(){
        Map<Color, Integer> playersMap = gameController.getScoreBoard().getFinalPlayersPosition();
        Map<Color, Integer> scoreMap = gameController.getScoreBoard().getScoreMap();
        gameController.getPlayersNicknames().forEach((x,y) -> {
            nicknamesMap.put(y,x);
        });
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 50);
        Font font2 = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 30);
        Font font3 = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 16);
        playersMap.forEach((x,y) -> {
            if(y == 1){
                VBox playerBox = new VBox();
                Label winner = new Label();
                winner.setMaxWidth(300);
                winner.setMinWidth(300);
                winner.getStyleClass().add(x.toString());
                winner.setFont(font);
                winner.setText(nicknamesMap.get(x));
                winner.setAlignment(Pos.CENTER);
                playerBox.getChildren().add(winner);
                Label score = new Label();
                score.setMinWidth(300);
                score.getStyleClass().add("WHITE");
                score.setFont(font3);
                score.setText("SCORE: "+scoreMap.get(x));
                score.setAlignment(Pos.CENTER);
                playerBox.getChildren().add(score);
                winnersBox.getChildren().add(playerBox);
                winnersBox.setLayoutX(winnersBox.getLayoutX() - 165);
            }
            else {
                VBox playerBox = new VBox();
                Label pos = new Label();
                pos.setMinWidth(300);
                pos.setAlignment(Pos.CENTER);
                pos.setText("POS: "+y);
                pos.setFont(font);
                pos.getStyleClass().add("WHITE");
                playerBox.getChildren().add(pos);
                Label name = new Label();
                name.setMaxWidth(300);
                name.setMinWidth(300);
                name.setAlignment(Pos.CENTER);
                name.setText(nicknamesMap.get(x));
                name.setFont(font2);
                name.getStyleClass().add(x.toString());
                playerBox.getChildren().add(name);
                Label score = new Label();
                score.setMinWidth(300);
                score.getStyleClass().add("WHITE");
                score.setFont(font3);
                score.setText("SCORE: "+scoreMap.get(x));
                score.setAlignment(Pos.CENTER);
                playerBox.getChildren().add(score);
                losersBox.getChildren().add(playerBox);
                losersBox.setLayoutX(losersBox.getLayoutX() - 165);
            }
        });
        animation();
    }

    private void animation(){
        ArrayList<Node> playersList = new ArrayList<>(losersBox.getChildren());
        Collections.reverse(playersList);
        playersList.add(winnersBox);
        SequentialTransition sequence = new SequentialTransition();
        playersList.forEach(x -> {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(4), x);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            sequence.getChildren().add(fadeIn);
        });
        sequence.play();
    }

    public void close(){
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            gameController.cleanExit();
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.close();
        }
    }
}
