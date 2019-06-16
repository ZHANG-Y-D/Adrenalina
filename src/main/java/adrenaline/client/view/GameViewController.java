package adrenaline.client.view;

import adrenaline.client.controller.GameController;
import adrenaline.client.model.Map;
import adrenaline.client.model.Player;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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
    private Pane pane, ownPlayer;
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
    private ImageView ownPlayerLabel, redAmmo1, redAmmo2, redAmmo3, blueAmmo1, blueAmmo2, blueAmmo3, yellowAmmo1, yellowAmmo2, yellowAmmo3,
            weaponRed1, weaponRed2, weaponRed3, weaponBlue1, weaponBlue2, weaponBlue3, weaponYellow1, weaponYellow2, weaponYellow3,
                     myWeapon,myPowerup;
    @FXML
    private Label message;
    @FXML
    private Polygon powerupTriangle,weaponTriangle;
    private HashMap<Integer, ArrayList<ImageView>> ammoBoxs = new HashMap<>();
    private HashMap<adrenaline.Color, ArrayList<ImageView>> weaponLists = new HashMap<>();
    private GameController gameController;
    private HashMap<adrenaline.Color, Pane> playersColorMap = new HashMap<>(); //forse non serve
    private HashMap<adrenaline.Color, ImageView> tokensMap = new HashMap<>();
    private HashMap<Integer, Pane> mapPanes = new HashMap<>(); //get pane from position
    private HashMap<Pane, ArrayList<Position>> positionMap = new HashMap<>(); //get pane used positions
    private HashMap<ImageView, Position> tokenPosition = new HashMap<>(); //get token current position
    private final int columns = 4;
    private final int rows = 3;

    public void initialize(){
        message.setStyle("-fx-font-family: Helvetica ; -fx-font-weight: bold");
        powerupTriangle.getStyleClass().add("triangle");
        weaponTriangle.getStyleClass().add("triangle");
        redAmmo1.getStyleClass().add("ammo");
        redAmmo2.getStyleClass().add("ammo");
        redAmmo3.getStyleClass().add("ammo");
        blueAmmo1.getStyleClass().add("ammo");
        blueAmmo2.getStyleClass().add("ammo");
        blueAmmo3.getStyleClass().add("ammo");
        yellowAmmo1.getStyleClass().add("ammo");
        yellowAmmo2.getStyleClass().add("ammo");
        yellowAmmo3.getStyleClass().add("ammo");
        scrollPane.vvalueProperty().bind(chat.heightProperty());
        ArrayList<ImageView> redAmmo = new ArrayList<>(Arrays.asList(redAmmo1,redAmmo2,redAmmo3));
        ArrayList<ImageView> blueAmmo = new ArrayList<>(Arrays.asList(blueAmmo1,blueAmmo2, blueAmmo3));
        ArrayList<ImageView> yellowAmmo = new ArrayList<>(Arrays.asList(yellowAmmo1,yellowAmmo2,yellowAmmo3));
        ammoBoxs.put(0,redAmmo);
        ammoBoxs.put(1,blueAmmo);
        ammoBoxs.put(2,yellowAmmo);
        ArrayList<ImageView> redWeaponsList = new ArrayList<>(Arrays.asList(weaponRed1, weaponRed2, weaponRed3));
        ArrayList<ImageView> blueWeaponsList = new ArrayList<>(Arrays.asList(weaponBlue1, weaponBlue2, weaponBlue3));
        ArrayList<ImageView> yellowWeaponsList = new ArrayList<>(Arrays.asList(weaponYellow1, weaponYellow2, weaponYellow3));
        weaponLists.put(adrenaline.Color.RED, redWeaponsList);
        weaponLists.put(adrenaline.Color.BLUE, blueWeaponsList);
        weaponLists.put(adrenaline.Color.YELLOW, yellowWeaponsList);
        weaponLists.forEach((x,y) -> {
            for (ImageView img : y) img.getStyleClass().add("weapon");
        });
        myWeapon.getStyleClass().add("weapon");
        myPowerup.getStyleClass().add("weapon");

        for(int i = 0; i <= 11; i++){
            Pane pane = (Pane) map.lookup("#pane"+i);
            pane.getChildren().get(0).setEffect(new Glow(0.5));
            mapPanes.put(i,pane);
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
        Map modelMap = gameController.getMap();
        String path = "url(/Maps/MAP"+ modelMap.getMapID()+".png)";
        map.setStyle("-fx-background-image: "+path);
        updateMap(modelMap);
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

    public void selectSquare(Event event){
        Pane pane = (Pane) event.getSource();
        gameController.selectSquare(Integer.parseInt(pane.getId().substring(4)));
    }

    public void showError(String error) {
        Platform.runLater(() -> {
            message.getStyleClass().clear();
            message.getStyleClass().add("RED");
            this.message.setText(error);
        });
    }

    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> {
            for(int i = 0; i <= 11; i++) ((Pane) map.lookup("#pane"+i)).getChildren().get(0).setVisible(false);
            this.message.getStyleClass().clear();
            this.message.getStyleClass().add("GREEN");
            this.message.setText(message);
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
        validSquares.forEach(i -> {
            ((Pane) map.lookup("#pane"+i)).getChildren().get(0).setVisible(true);
        });
    }

    public void propertyChange(PropertyChangeEvent evt) {
        for(int i = 0; i <= 11; i++) ((Pane) map.lookup("#pane"+i)).getChildren().get(0).setVisible(false);
        switch (evt.getPropertyName()){
            case "map":
                updateMap((Map)evt.getNewValue());
                break;
            case "player":
                updatePlayer((HashMap<adrenaline.Color, Player>)evt.getNewValue());
                break;
            case "scoreboard":
                break;
            default: break;
        }
    }

    public void test(){
        gameController.updateMap(new Map());
        gameController.updatePlayer(new Player());
    }

    public void selectAction(Event evt){
        message.setText("");
        Button button = (Button) evt.getSource();
        switch (button.getId()){
            case "reload": gameController.endTurn(); break;
            case "run": gameController.run(); break;
            case "grab": gameController.grab(); break;
            case "shoot": gameController.shoot(); break;
            case "back": gameController.back(); break;
            default: break;
        }
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

    private void updateMap(Map newMap){
        Platform.runLater(() -> {
            HashMap<Integer,Integer> ammoMap = newMap.getAmmoMap();
            ammoMap.forEach((x,y) -> {
                ImageView ammoImage = (ImageView) mapPanes.get(x).getChildren().get(1);
                if(y == 0) ammoImage.setImage(null);
                else {
                    String imgUrl = y.toString() + ".png";
                    ammoImage.setImage(new Image(getClass().getResourceAsStream("/Ammo/ammo-" + imgUrl)));
                }
            });
            newMap.getWeaponMap().forEach((x,y) -> {
                ArrayList<ImageView> list = weaponLists.get(x);
                for(int i = 0; i < y.size(); i++) {
                    String newImgUrl = "/Weapons/weapon_" + y.get(i) + "-TOP.png";
                    try {
                        list.get(i).setImage(new Image(new File(getClass().getResource(newImgUrl).toURI()).toURI().toString()));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
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

            //set ammo
            int[] ammoBox = ownPlayer.getAmmoBox();
            if(ammoBox != null) {
                for (int i = 0; i < 3; i++) {
                    int ammoNum = ammoBox[i];
                    ArrayList<ImageView> ammoList = ammoBoxs.get(i);
                    ammoList.forEach(x -> {
                        if (ammoList.indexOf(x) < ammoNum) x.setVisible(true);
                        else x.setVisible(false);
                    });
                }
            }

            //set player position
            updatePosition(newPlayersMap);
        });

    }

    private void updatePosition(HashMap<adrenaline.Color,Player> newPlayersMap){
        newPlayersMap.forEach((x,y) -> {
            if((y.getPosition() != -1)){
                ImageView token = tokensMap.get(x);
                Position newPosition;
                ArrayList<Position> list = new ArrayList<>();
                Pane newPane =  mapPanes.get(y.getPosition());
                Pane oldPane = (Pane) token.getParent();
                if(oldPane == null) { //if is the first turn
                    String tokenUrl = "/"+x.toString()+"-TOKEN.png";
                    token.setImage(new Image(getClass().getResourceAsStream(tokenUrl)));
                    token.setFitHeight(45);
                    token.setFitWidth(45);
                    newPosition = getFreePosition(newPane);
                    token.setLayoutX(newPosition.getX());
                    token.setLayoutY(newPosition.getY());
                    list.add(newPosition);
                    positionMap.put(newPane, list);
                    tokenPosition.put(token, newPosition);
                    newPane.getChildren().add(token);
                }
                else if(newPane != oldPane){
                    System.out.println(oldPane.getId()+" "+newPane.getId());
                    newPosition = getFreePosition(newPane);
                    if(positionMap.get(newPane) == null) list.add(newPosition);
                    else {
                        list = positionMap.get(newPane);
                        list.add(newPosition);
                    }
                    TranslateTransition transition = new TranslateTransition();
                    transition.setNode(token);
                    transition.setDuration(Duration.millis(300));
                    transition.setToX(((y.getPosition()%columns) - (y.getOldPosition()%columns))*145 + newPosition.getX() - tokenPosition.get(token).getX());
                    transition.setToY(((y.getPosition()/columns) - (y.getOldPosition()/columns))*148 + newPosition.getY() - tokenPosition.get(token).getY());
                    transition.setOnFinished(e -> {
                        oldPane.getChildren().remove(token);
                        token.setTranslateX(0);
                        token.setTranslateY(0);
                        token.setLayoutX(newPosition.getX());
                        token.setLayoutY(newPosition.getY());
                        newPane.getChildren().add(token);
                    });
                    transition.play();
                    positionMap.get(oldPane).remove(tokenPosition.get(token));
                    tokenPosition.put(token, newPosition);
                    if(positionMap.get(oldPane).isEmpty()) positionMap.remove(oldPane);
                    positionMap.put(newPane, list);
                }
            }
        });
    }

    private Position getFreePosition(Pane pane){
        ArrayList<Position> positionList = positionMap.get(pane);
        if(positionList == null) return Position.CENTER;
        else {
            ArrayList<Position> allPosition = new ArrayList<>(Arrays.asList(Position.TOP, Position.CENTER, Position.RIGHT, Position.LEFT, Position.DOWN));
            allPosition.removeIf(positionList::contains);
            Random rand = new Random();
            return allPosition.get(rand.nextInt(allPosition.size()));
        }
    }

    public void nextPowerUp(){
        ArrayList<Integer> powerUpList = gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards();
        String powerup = myPowerup.getImage().getUrl();
        powerup = new File(powerup).getName();
        powerup = powerup.substring(powerup.indexOf('-') + 1, powerup.indexOf('.'));
        int newIndex;
        if(powerUpList.indexOf(Integer.parseInt(powerup)) == (powerUpList.size() -1)) newIndex = 0;
        else newIndex = powerUpList.indexOf(Integer.parseInt(powerup)) + 1;
        powerup = "/Powerup/powerup-"+powerUpList.get(newIndex)+".png";
        try {
            myPowerup.setImage(new Image(new File(getClass().getResource(powerup).toURI()).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void selectPowerUp(){
        message.setText("");
        String powerupID = myPowerup.getImage().getUrl();
        powerupID = new File(powerupID).getName();
        powerupID = powerupID.substring(powerupID.indexOf('-') + 1, powerupID.indexOf('.'));
        gameController.selectPowerUp(Integer.parseInt(powerupID));
    }
}
