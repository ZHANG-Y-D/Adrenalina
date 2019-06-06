package adrenaline.client.view;

import adrenaline.client.controller.GameController;
import adrenaline.client.model.Map;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;


public class GameViewController implements ViewInterface, PropertyChangeListener {

    @FXML
    private Pane pane, move, shoot, grab, reload, back;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane map;
    @FXML
    private VBox redWeapons, yellowWeapons, chat;
    @FXML
    private TextField txtMsg;
    public ImageView player_label,red_ammo1,red_ammo2,red_ammo3,blue_ammo1,blue_ammo2,blue_ammo3,yellow_ammo1,yellow_ammo2,yellow_ammo3,
                    weapon_red1, weapon_red2, weapon_red3, weapon_blue1, weapon_blue2, weapon_blue3, weapon_yellow1, weapon_yello2, weapon_yellow3;
    private Map modelMap;
    private HashMap<adrenaline.Color, VBox> weaponBoxs = new HashMap<>();
    private GameController gameController;

    public void initialize(){
        red_ammo1.getStyleClass().add("ammo");
        red_ammo2.getStyleClass().add("ammo");
        red_ammo3.getStyleClass().add("ammo");
        blue_ammo1.getStyleClass().add("ammo");
        blue_ammo2.getStyleClass().add("ammo");
        blue_ammo3.getStyleClass().add("ammo");
        yellow_ammo1.getStyleClass().add("ammo");
        yellow_ammo2.getStyleClass().add("ammo");
        yellow_ammo3.getStyleClass().add("ammo");
        scrollPane.vvalueProperty().bind(chat.heightProperty());
        weaponBoxs.put(adrenaline.Color.RED, redWeapons);
        gameController = new GameController();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        gameController.addPropertyChangeListener(this);
        initializeMap();
    }

    public void initializeMap(){
        modelMap = gameController.getMap();
        String path = "url(/Maps/MAP"+modelMap.getMapID()+".png)";
        map.setStyle("-fx-background-image: "+path);
    }

    public void sendMessage(){
        String message = txtMsg.getText();
        if(message.length()>0){
            gameController.sendChatMessage(message);
        }
        txtMsg.setText("");
    }

    public void setDestination(){

    }

    public void showError(String error) {

    }

    public void changeStage() {

    }

    public void notifyTimer(Integer duration) {

    }

    public void newChatMessage(String nickname, adrenaline.Color senderColor, String message) {
        Platform.runLater(()->{
            HBox fullMessage = new HBox();
            fullMessage.setLayoutX(2);
            Label senderName = new Label(nickname.toUpperCase()+": ");
            senderName.getStyleClass().add(senderColor.toString());
            Label sentMessage = new Label(message);
            sentMessage.setTextFill(Color.WHITE);
            fullMessage.getChildren().add(senderName);
            fullMessage.getChildren().add(sentMessage);
            chat.getChildren().add(fullMessage);
        });
    }

    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("ENTRATO");
        switch (evt.getPropertyName()){
            case "map":
                updateMap(evt.getNewValue());
                break;
            case "player":
                System.out.println("ENTRATO IN PLAYER");
                break;
            case "scoreboard":
                System.out.println("ENTRATO IN SCOREBOARD");
                break;

        }
    }

    public void test(){
        gameController.updateMap(new Map());
    }

    public void selectAction(Event evt){
        Pane pane = (Pane) evt.getSource();
        System.out.println(pane.getId());
        test();
    }

    private void updateMap(Object newMap){

    }
}
