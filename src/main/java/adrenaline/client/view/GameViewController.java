package adrenaline.client.view;

import adrenaline.client.controller.GameController;
import adrenaline.client.model.Map;
import adrenaline.client.model.Player;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static javafx.scene.effect.BlurType.GAUSSIAN;


public class GameViewController implements ViewInterface, PropertyChangeListener {

    @FXML
    private Pane pane, ownPlayer,pane0;
    @FXML
    private Button  run, shoot, grab, reload, back;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane map;
    @FXML
    private VBox chat, enemyPlayers;
    @FXML
    private TextField txtMsg;
    @FXML
    private ImageView ownPlayerLabel,red_ammo1,red_ammo2,red_ammo3,blue_ammo1,blue_ammo2,blue_ammo3,yellow_ammo1,yellow_ammo2,yellow_ammo3,
                     weapon_red1,weapon_red2, weapon_red3, weapon_blue1, weapon_blue2, weapon_blue3, weapon_yellow1, weapon_yellow2, weapon_yellow3,
                     myWeapon;
    @FXML
    private Polygon powerupTriangle,weaponTriangle;
    private Map modelMap;
    private HashMap<adrenaline.Color, ArrayList<ImageView>> weaponLists = new HashMap<>();
    private ArrayList<ImageView> redWeaponsList, blueWeaponsList, yellowWeaponsList;
    private GameController gameController;
    private HashMap<adrenaline.Color, Pane> playersColorMap = new HashMap<>();
    private HashMap<Integer, Pane> mapPanes = new HashMap<>();
    private final int columns = 4;
    private final int rows = 3;

    public void initialize(){
        powerupTriangle.getStyleClass().add("triangle");
        weaponTriangle.getStyleClass().add("triangle");
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
        redWeaponsList = new ArrayList<>(Arrays.asList(weapon_red1, weapon_red2, weapon_red3));
        blueWeaponsList = new ArrayList<>(Arrays.asList(weapon_blue1, weapon_blue2, weapon_blue3));
        yellowWeaponsList = new ArrayList<>(Arrays.asList(weapon_yellow1, weapon_yellow2, weapon_yellow3));
        weaponLists.put(adrenaline.Color.RED,redWeaponsList);
        weaponLists.put(adrenaline.Color.BLUE,blueWeaponsList);
        weaponLists.put(adrenaline.Color.YELLOW,yellowWeaponsList);
        weaponLists.forEach((x,y) -> {
            for (ImageView img : y) img.getStyleClass().add("weapon");
        });
        myWeapon.getStyleClass().add("weapon");

        for(int i = 0; i <= 11; i++){
            mapPanes.put(i,(Pane) map.lookup("#pane"+i));
        }
        //gameController = new GameController();
        //gameController.addPropertyChangeListener(this);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        gameController.addPropertyChangeListener(this);
        initializeHUD();
    }

    public void initializeHUD(){
        modelMap = gameController.getMap();
        String path = "url(/Maps/MAP"+modelMap.getMapID()+".png)";
        map.setStyle("-fx-background-image: "+path);
        HashMap<Integer,Integer> ammoMap = modelMap.getAmmoMap();
        ammoMap.forEach((x,y) -> {
            ImageView ammoImage = (ImageView) mapPanes.get(x).getChildren().get(0);
            ammoImage.setOpacity(0.70);
            String imgUrl = y.toString() + ".png";
            ammoImage.setImage(new Image(getClass().getResourceAsStream("/Ammo/ammo-"+imgUrl)));
        });
        Pane skullPane = new Pane();
        ImageView skulls = new ImageView(new Image(getClass().getResourceAsStream("/SKULLBAR.png")));
        skulls.setFitHeight(57);
        skulls.setFitWidth(320);
        skullPane.getChildren().add(skulls);
        enemyPlayers.getChildren().add(skullPane);
        HashMap<String, adrenaline.Color> nicknamesMap = gameController.getPlayersNicknames();
        adrenaline.Color ownColor = gameController.getPlayersNicknames().get(gameController.getOwnNickname());
        String newImgUrl = "/HUD/"+ownColor.toString()+"-HUD.png";
        ownPlayerLabel.setImage(new Image(getClass().getResourceAsStream(newImgUrl)));
        String runPath = "url(/HUD/"+ownColor.toString()+"-RUN.png)";
        run.setStyle("-fx-background-image: "+ runPath);
        String grabPath = "url(/HUD/"+ownColor.toString()+"-GRAB.png)";
        grab.setStyle("-fx-background-image: "+ grabPath);
        String shootPath = "url(/HUD/"+ownColor.toString()+"-SHOOT.png)";
        shoot.setStyle("-fx-background-image: "+ shootPath);
        String reloadPath = "url(/HUD/"+ownColor.toString()+"-RELOAD.png)";
        reload.setStyle("-fx-background-image: "+ reloadPath);
        String backPath = "url(/HUD/"+ownColor.toString()+"-GOBACK.png)";
        back.setStyle("-fx-background-image: "+ backPath);
        nicknamesMap.forEach((y,x) -> {
                if(x.equals(ownColor)) playersColorMap.put(x, ownPlayer);
                else{
                    Pane newPane = new Pane();
                    String newUrl = "/HUD/"+x.toString()+"-SCOREBOARD.png";
                    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(newUrl)));
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(320);
                    newPane.getChildren().add(imageView);
                    Label nickname = new Label(y);
                    Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 16);
                    nickname.setFont(font);
                    nickname.minWidth(260);
                    nickname.maxWidth(260);
                    nickname.setLayoutY(5);
                    nickname.getStyleClass().add(x.toString());
                    nickname.setAlignment(Pos.TOP_RIGHT);
                    newPane.getChildren().add(nickname);
                    enemyPlayers.getChildren().add(newPane);
                    playersColorMap.put(x, newPane);
                }
        });
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

    public void notifyTimer(Integer duration, String comment) {

    }

    public void newChatMessage(String nickname, adrenaline.Color senderColor, String message) {
        Platform.runLater(()->{
            HBox fullMessage = new HBox();
            fullMessage.setLayoutX(2);
            Label senderName = new Label(nickname.toUpperCase()+": ");
            senderName.getStyleClass().add(senderColor.toString());
            senderName.setStyle("-fx-font-family: Helvetica ; -fx-font-weight: bold");
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
                System.out.println("ENTRATO IN MAP");
                updateMap((Map)evt.getNewValue());
                break;
            case "player":
                System.out.println("ENTRATO IN PLAYER");
                updatePlayer((Player)evt.getNewValue());
                break;
            case "scoreboard":
                System.out.println("ENTRATO IN SCOREBOARD");
                break;

        }
    }

    public void test(){
        gameController.updateMap(new Map());
        gameController.updatePlayer(new Player());
    }

    public void selectAction(Event evt){
        Button button = (Button) evt.getSource();
        System.out.println(button.getId());
        test();
    }

    private void updateMap(Map newMap){
        //initializeHUD();
        newMap.getWeaponMap().forEach((x,y) -> {
            ArrayList<ImageView> weaponList = weaponLists.get(x);
            for(int i = 0; i < 3; i++) {
                String newImgUrl = "/Weapons/weapon_" + y.get(i) + "-TOP.png";
                try {
                    weaponList.get(i).setImage(new Image(new File(getClass().getResource(newImgUrl).toURI()).toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public synchronized void drawBottom(MouseEvent mouseEvent){
        ImageView bottom = new ImageView();
        ImageView image = (ImageView) mouseEvent.getSource();
        String imgUrl = image.getImage().getUrl();
        String imgName = new File(imgUrl).getName();
        String newImgUrl = "/Weapons/"+imgName.substring(0, imgName.length()-7) + "BOTTOM.png";
        try {
            bottom.setImage(new Image(new File(getClass().getResource(newImgUrl).toURI()).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        bottom.setFitWidth(120);
        bottom.setFitHeight(127);
        bottom.setLayoutX(image.getParent().getLayoutX());
        bottom.setLayoutY(image.getParent().getLayoutY());
        bottom.setX(mouseEvent.getX()+20 +image.getLayoutX());
        bottom.setY(mouseEvent.getY()+20 +image.getLayoutY());
        bottom.setEffect(new DropShadow(GAUSSIAN, Color.BLACK,20,0.2,0,0));
        image.setOnMouseMoved(x -> {
            bottom.setX(x.getX()+20+image.getLayoutX());
            if(x.getY()+image.getLayoutY()+147 < (768 - image.getParent().getLayoutY())){
                bottom.setY(x.getY()+20+image.getLayoutY());
            }else{
                bottom.setY(x.getY()+image.getLayoutY()- 147);
            }

        });
        image.setOnMouseExited(e -> {
            pane.getChildren().remove(bottom);
        });
        long timestart = System.currentTimeMillis();
        while((System.currentTimeMillis() - timestart < 400));
        pane.getChildren().add(bottom);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), bottom);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void showTriangles(){
        weaponTriangle.setVisible(true);
        powerupTriangle.setVisible(true);
    }

    public void hideTriangles(){
        powerupTriangle.setVisible(false);
        weaponTriangle.setVisible(false);
    }

    public void updatePlayer(Player newPlayer){
        ImageView player = new ImageView();
        String newUrl = "/"+newPlayer.getColor().toString() +"-TOKEN.png";
        player.setImage(new Image(getClass().getResourceAsStream(newUrl)));
        player.setFitWidth(45);
        player.setFitHeight(45);
        player.setLayoutX(10);
        pane0.getChildren().add(player);
        //provvisorio
        ImageView player2 = new ImageView();
        newUrl = "/"+adrenaline.Color.YELLOW.toString() +"-TOKEN.png";
        player2.setImage(new Image(getClass().getResourceAsStream(newUrl)));
        player2.setFitWidth(45);
        player2.setFitHeight(45);
        ImageView player3 = new ImageView();
        newUrl = "/"+adrenaline.Color.PURPLE.toString() +"-TOKEN.png";
        player3.setImage(new Image(getClass().getResourceAsStream(newUrl)));
        player3.setFitWidth(45);
        player3.setFitHeight(45);
        player2.setLayoutX(55);
        player3.setLayoutX(100);
        pane0.getChildren().add(player2);
        pane0.getChildren().add(player3);
    }
}
