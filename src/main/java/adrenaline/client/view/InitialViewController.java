package adrenaline.client.view;


import adrenaline.client.controller.GameController;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.util.ArrayList;


public class InitialViewController implements ViewInterface {

    public Pane initPane;
    public Button rmi, socket, play, close;
    public TextField host, port, name;
    public Label label,error;
    private GameController gameController = null;

    public void initialize(){
        initPane.getStyleClass().add("pane");
        close.getStyleClass().add("close");
        error.getStyleClass().add("outline");
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"),16);
        close.setFont(font);
        rmi.setFont(font);
        socket.setFont(font);
        host.setFont(font);
        port.setFont(font);
        name.setFont(font);
        play.setFont(font);
        label.setFont(font);
        error.setFont(font);
    }

    public void setGameController(GameController gameController){
        this.gameController = gameController;
    }

    public void notifyView() { }

    public void RMISelected(){
        if(!host.getText().equals("") && (!port.getText().equals(""))){
            try{
                if(gameController.connectRMI(host.getText(), Integer.parseInt(port.getText()))) changeScene();
            }catch (NumberFormatException e){
                error.setText("Wrong host/port");
            }
        }
        else error.setText("Type host and port");
    }

    public void SocketSelected(){
        if(!host.getText().equals("") && (!port.getText().equals(""))) {
            try {
                if(gameController.connectSocket(host.getText(), Integer.parseInt(port.getText()))) changeScene();
            }catch (NumberFormatException e){
                error.setText("Wrong host/port");
            }
        }
        else error.setText("Type host and port");
    }

    public void changeScene(){
        error.setText("");
        rmi.setVisible(false);
        socket.setVisible(false);
        host.setVisible(false);
        port.setVisible(false);
        name.setVisible(true);
        play.setVisible(true);
        error.setLayoutY(362);
    }

    public void sendNickname(){
        if(!name.getText().equals("")) {
            gameController.setNickname(name.getText());
        }
        else error.setText("Type a nickname");
    }

    @Override
    public void showError(String errorMsg) {
        Platform.runLater(() -> {
            if(errorMsg.equals("/OK")){
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, event -> {
                            String statusText = label.getText();
                            label.setText(
                                    ("Searching for a game . . .".equals(statusText))
                                            ? "Searching for a game ."
                                            : statusText + " ."
                            );
                        }),
                        new KeyFrame(Duration.millis(1000))
                );
                timeline.setCycleCount(Timeline.INDEFINITE);
                label.setVisible(true);
                play.setDisable(true);
                name.setDisable(true);
                error.setText("");
                timeline.play();
            }
            else error.setText(errorMsg);
        });
    }

    public void changeStage(){
        Platform.runLater(() ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SelectionView.fxml"));
                Parent nextView = loader.load();
                Scene scene = new Scene(nextView);
                ViewInterface viewController = loader.getController();
                viewController.setGameController(gameController);
                gameController.setViewController(viewController);
                Stage stage = (Stage) initPane.getScene().getWindow();
                stage.setWidth(1280);
                stage.setHeight(768);
                stage.centerOnScreen();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close(){
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            gameController.cleanExit();
            Stage stage = (Stage) initPane.getScene().getWindow();
            stage.close();
        }
    }

}
