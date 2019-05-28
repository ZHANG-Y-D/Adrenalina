package adrenaline.client.view;

import adrenaline.client.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ClientGui extends Application{

    private Stage window;
    private ViewInterface currentController;

    public void start(Stage primaryStage)throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InitialView.fxml"));
        Parent root = loader.load();
        Controller controller = new Controller();
        currentController = loader.getController();
        currentController.setController(controller);
        controller.setViewController(currentController);
        window = primaryStage;
        window.setTitle("Adrenalina");
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(new Scene(root, 750, 500));
        window.show();
    }

}
