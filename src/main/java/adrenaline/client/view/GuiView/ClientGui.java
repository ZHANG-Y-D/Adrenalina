package adrenaline.client.view.GuiView;

import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 *
 * For start the Gui
 *
 */
public class ClientGui extends Application{

    private Stage window;

    /**
     *
     * For start the gui view
     *
     * @param primaryStage the primary Stage
     */
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
