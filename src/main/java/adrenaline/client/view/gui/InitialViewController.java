package adrenaline.client.view.gui;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;


public class InitialViewController implements ViewInterface {

    @FXML
    private Pane initPane;
    @FXML
    private Button rmi, socket, play, close;
    @FXML
    private TextField host, port, name;
    @FXML
    private Label label, message;
    private GameController gameController = null;
    private int time;

    /**
     *
     * To init this stage
     *
     */
    public void initialize(){
        initPane.getStyleClass().add("pane");
        close.getStyleClass().add("close");
        message.getStyleClass().add("outline");
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"),16);
        close.setFont(font);
        rmi.setFont(font);
        socket.setFont(font);
        host.setFont(font);
        port.setFont(font);
        name.setFont(font);
        play.setFont(font);
        label.setFont(font);
        message.setFont(font);
    }

    /**
     *
     * To set the current controller
     *
     * @param gameController The gameController reference
     */
    public void setGameController(GameController gameController){
        this.gameController = gameController;
    }

    public void notifyTimer(Integer duration, String comment) {
        time = duration;
    }


    /**
     *
     * Not supported at this stage
     *
     */
    public void newChatMessage(String nickname, Color senderColor, String message) {
        //operation not supported at this stage
    }

    /**
     *
     * Not supported at this stage
     *
     */
    public void showValidSquares(ArrayList<Integer> validSquares) {
        //operation not supported at this stage
    }

    /**
     *
     * For RMI bottom and connect
     */
    public void RMISelected(){
        if(!host.getText().equals("") && (!port.getText().equals(""))){
            try{
                if(gameController.connectRMI(host.getText(), Integer.parseInt(port.getText()))) changeScene();
            }catch (NumberFormatException e){
                message.setText("Wrong host/port");
            }
        }
        else message.setText("Type host and port");
    }

    /**
     * For Socket bottom and connect
     */
    public void SocketSelected(){
        if(!host.getText().equals("") && (!port.getText().equals(""))) {
            try {
                if(gameController.connectSocket(host.getText(), Integer.parseInt(port.getText()))) changeScene();
            }catch (NumberFormatException e){
                message.setText("Wrong host/port");
            }
        }
        else message.setText("Type host and port");
    }

    /**
     *
     * To change scene
     */
    public void changeScene(){
        message.setText("");
        rmi.setVisible(false);
        socket.setVisible(false);
        host.setVisible(false);
        port.setVisible(false);
        name.setVisible(true);
        play.setVisible(true);
        message.setLayoutY(362);
    }

    /**
     *
     * Send nickname to server
     */
    public void sendNickname(){
        if(!name.getText().equals("")) {
            gameController.setNickname(name.getText());
        }
        else message.setText("Type a nickname");
    }

    /**
     *
     * For show the error message from server
     *
     * @param errorMsg the error message
     */
    @Override
    public void showError(String errorMsg) {
        Platform.runLater(() -> {
            message.setText(errorMsg);
        });
    }

    /**
     *
     * For show the OK message from server
     *
     * @param message the ok message
     */
    @Override
    public void showMessage(String message) {
        Platform.runLater(() ->{
            if(message.contains("NICKNAME;")) name.setText(message.substring(9));
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
            this.message.setText("");
            timeline.play();
        });
    }

    /**
     *
     * For get the change stage signal from game controller
     *
     */
    public void changeStage(){
        Platform.runLater(() ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SelectionView.fxml"));
                Parent nextView = loader.load();
                Scene scene = new Scene(nextView);
                ViewInterface viewController = loader.getController();
                gameController.setViewController(viewController);
                viewController.setGameController(gameController);
                viewController.notifyTimer(time, "");
                Stage stage = (Stage) initPane.getScene().getWindow();
                stage.setWidth(1280);
                stage.setHeight(768);
                stage.centerOnScreen();
                stage.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *
     * To close the stage and connect
     *
     */
    public void close(){
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            gameController.cleanExit();
            Stage stage = (Stage) initPane.getScene().getWindow();
            stage.close();
        }
    }


}
