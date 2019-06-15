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
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
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
import java.util.Random;

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
                     myWeapon,myPowerup;
    @FXML
    private Label error;
    @FXML
    private Polygon powerupTriangle,weaponTriangle;
    private Map modelMap;
    private HashMap<adrenaline.Color, ArrayList<ImageView>> weaponLists = new HashMap<>();
    private ArrayList<ImageView> redWeaponsList, blueWeaponsList, yellowWeaponsList;
    private GameController gameController;
    private HashMap<adrenaline.Color, Pane> playersColorMap = new HashMap<>();
    private HashMap<adrenaline.Color, ImageView> tokensMap = new HashMap<>();
    private HashMap<Integer, Pane> mapPanes = new HashMap<>();
    private HashMap<Pane, ArrayList<Position>> positionMap = new HashMap<>();
    private final int columns = 4;
    private final int rows = 3;

    public void initialize(){
        error.getStyleClass().add("RED");
        error.setStyle("-fx-font-family: Helvetica ; -fx-font-weight: bold");
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
        myPowerup.getStyleClass().add("weapon");

        for(int i = 0; i <= 11; i++){
            mapPanes.put(i,(Pane) map.lookup("#pane"+i));
            ImageView highlight = new ImageView(new Image(getClass().getResourceAsStream("/highlight.png")));
            highlight.setEffect(new Glow(0.6));
            highlight.setVisible(false);
            ((Pane) map.lookup("#pane"+i)).getChildren().add(highlight);
        }

        //gameController = new GameController();
        //gameController.addPropertyChangeListener(this);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        gameController.addPropertyChangeListener(this);
        initializeHUD();
        updatePlayer(gameController.getPlayersMap());
    }

    public void initializeHUD(){
        modelMap = gameController.getMap();
        String path = "url(/Maps/MAP"+modelMap.getMapID()+".png)";
        map.setStyle("-fx-background-image: "+path);
        HashMap<Integer,Integer> ammoMap = modelMap.getAmmoMap();
        ammoMap.forEach((x,y) -> {
            ImageView ammoImage = (ImageView) mapPanes.get(x).getChildren().get(0);
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
        tokensMap.put(ownColor, new ImageView());
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
                    tokensMap.put(x, new ImageView());
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
        Platform.runLater(() -> {
            this.error.setText(error);
        });
    }

    public void changeStage() {

    }

    public void notifyTimer(Integer duration, String comment) {

    }

    public void newChatMessage(String nickname, adrenaline.Color senderColor, String message) {
        Platform.runLater(()->{
            HBox fullMessage = new HBox();
            fullMessage.setLayoutX(4);
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

    public void showValidSquares(ArrayList<Integer> validSquares) {
        validSquares.forEach(i -> ((Pane) map.lookup("#pane"+i)).getChildren().get(1).setVisible(true));
    }

    public void propertyChange(PropertyChangeEvent evt) {
        for(int i = 0; i <= 11; i++) ((Pane) map.lookup("#pane"+i)).getChildren().get(1).setVisible(false);
        switch (evt.getPropertyName()){
            case "map":
                updateMap((Map)evt.getNewValue());
                break;
            case "player":
                updatePlayer((HashMap<adrenaline.Color, Player>)evt.getNewValue());
                break;
            case "scoreboard":
                break;

        }
    }

    public void test(){
        gameController.updateMap(new Map());
        gameController.updatePlayer(new Player());
    }

    public void selectAction(Event evt){
        error.setText("");
        Button button = (Button) evt.getSource();
        switch (button.getId()){
            case "reload": gameController.endTurn(); break;
        }
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
        if(gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards()!=null) powerupTriangle.setVisible(true);
        if(gameController.getPlayersMap().get(gameController.getOwnColor()).getWeaponCards()!=null) weaponTriangle.setVisible(true);
    }

    public void hideTriangles(){
        powerupTriangle.setVisible(false);
        weaponTriangle.setVisible(false);
    }

    public void updatePlayer(HashMap<adrenaline.Color, Player> newPlayersMap){
        Platform.runLater(() ->{
            //set own powerup
            Player ownPlayer = newPlayersMap.get(gameController.getOwnColor());
            if(ownPlayer.getPowerupCards() != null) {
                String newImgUrl = "/Powerup/powerup-"+ownPlayer.getPowerupCards().get(0)+".png";
                try {
                    myPowerup.setImage(new Image(new File(getClass().getResource(newImgUrl).toURI()).toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            else myPowerup.setImage(null);

            //set player position
            newPlayersMap.forEach((x,y) -> {
                if(y.getPosition() != -1){
                    ImageView token = tokensMap.get(x);
                    if(token.getParent() == null) { //if is the first turn
                        String tokenUrl = "/"+x.toString()+"-TOKEN.png";
                        token.setImage(new Image(getClass().getResourceAsStream(tokenUrl)));
                        token.setFitHeight(45);
                        token.setFitWidth(45);
                        Pane pane =  mapPanes.get(y.getPosition());
                        ArrayList list = new ArrayList();
                        if(positionMap.get(pane) == null){
                            token.setLayoutX(Position.CENTER.getX());
                            token.setLayoutY(Position.CENTER.getY());
                            list.add(Position.CENTER);
                            positionMap.put(pane, list);
                        }
                        else{
                            Position newPosition = getFreePosition(pane);
                            token.setLayoutX(newPosition.getX());
                            token.setLayoutY(newPosition.getY());
                            list = positionMap.get(pane);
                            list.add(newPosition);
                            positionMap.put(pane, list);
                        }
                        mapPanes.get(pane.getChildren().add(token));
                    }
                    else{} //TODO transition
                }
            });
        });

    }

    private Position getFreePosition(Pane pane){
        ArrayList<Position> positionList = positionMap.get(pane);
        ArrayList<Position> allPosition = new ArrayList<>(Arrays.asList(Position.TOP,Position.CENTER,Position.RIGHT,Position.LEFT,Position.DOWN));
        allPosition.removeIf(positionList::contains);
        Random rand = new Random();
        return allPosition.get(rand.nextInt(allPosition.size()));
    }

    public void selectPowerUp(){
        String powerupID = myPowerup.getImage().getUrl();
        powerupID = new File(powerupID).getName();
        powerupID = powerupID.substring(powerupID.indexOf('-') + 1, powerupID.indexOf('.'));
        System.out.println(powerupID);
        gameController.selectPowerUp(Integer.parseInt(powerupID));
    }
}
