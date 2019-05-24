package adrenaline.client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ClientGui extends Application {

    private Stage window;

    public void start(Stage primaryStage)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/SelectionView.fxml"));
        window = primaryStage;
        window.setTitle("Adrenalina");
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(new Scene(root, 1280, 768));
        window.show();
    }

}
