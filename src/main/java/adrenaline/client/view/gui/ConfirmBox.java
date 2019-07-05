package adrenaline.client.view.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * To confirm box on view level
 *
 */
public class ConfirmBox {

    static boolean answer;

    /**
     *
     *
     * A static method to display a stage
     *
     * @param title The title string
     * @param message The message string
     * @return the boolean value of answer
     */
    public static boolean display(String title, String message){
        Stage stage = new Stage();

        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNIFIED);
        stage.setResizable(false);
        stage.setMinWidth(210);
        stage.setMinHeight(140);

        Label label = new Label(message);
        label.setLayoutX(10);
        label.setLayoutY(22);

        Button yesButton = new Button("YES");
        yesButton.setLayoutX(35);
        yesButton.setLayoutY(60);
        yesButton.setMinSize(50, 20);
        Button noButton = new Button("NO");
        noButton.setLayoutX(125);
        noButton.setLayoutY(60);
        noButton.setMinSize(50, 20);

        yesButton.setOnAction(e -> {
            answer = true;
            stage.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            stage.close();
        });

        Pane layout = new Pane();
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setStyle("-fx-background-color: #261212; -fx-border-color: white");
        label.setStyle("-fx-text-fill: white");
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("InitialView.css");
        stage.setScene(scene);
        stage.showAndWait();

        return answer;
    }
}