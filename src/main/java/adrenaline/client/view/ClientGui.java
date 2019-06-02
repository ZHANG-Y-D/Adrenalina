package adrenaline.client.view;

import adrenaline.client.controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ClientGui extends Application{

    private Stage window;

    public void start(Stage primaryStage)throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InitialView.fxml"));
        Parent root = loader.load();
        GameController gameController = new GameController();
        ViewInterface viewController = loader.getController();
        viewController.setGameController(gameController);
        gameController.setViewController(viewController);
        window = primaryStage;
        window.setTitle("Adrenalina");
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(new Scene(root, 750, 500));
        window.show();
    }

}
