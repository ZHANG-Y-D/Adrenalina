package adrenaline.client.view;

import adrenaline.client.controller.GameController;
import adrenaline.client.model.Map;
import adrenaline.client.model.Player;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;


public class GameViewController implements ViewInterface, PropertyChangeListener {

    @FXML
    private Pane pane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane map;
    @FXML
    private VBox redWeapons, yellowWeapons, chat;
    @FXML
    private TextField txtMsg;
    public ImageView player_label;
    private Map modelMap;
    private HashMap<adrenaline.Color, VBox> weaponBoxs = new HashMap<>();
    private GameController gameController;

    public void initialize(){
        scrollPane.vvalueProperty().bind(chat.heightProperty());
        weaponBoxs.put(adrenaline.Color.RED, redWeapons);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        gameController.addPropertyChangeListener(this);
        initializeMap();
    }

    public void initializeMap(){
        modelMap = gameController.getMap();
        String path = "url(/MAPS/MAP"+modelMap.getMapID()+".png)";
        map.setStyle("-fx-background-image: "+path);
    }

    public void sendMessage(){
        Label label = new Label(txtMsg.getText());
        label.setTextFill(Color.WHITE);
        txtMsg.setText("");
        chat.getChildren().add(label);
    }

    public void setDestination(){

    }

    public void showError(String error) {

    }

    public void changeStage() {

    }

    public void notifyTimer(Integer duration) {

    }

    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("ENTRATO");
        switch (evt.getPropertyName()){
            case "map":
                System.out.println("ENTRATO IN MAP");
                break;
            case "player":
                System.out.println("ENTRATO IN PLAYER");
                break;
            case "scoreboard":
                System.out.println("ENTRATO IN SCOREBOARD");
                break;

        }
    }
}
