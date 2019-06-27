package adrenaline.client.view;

import adrenaline.client.controller.GameController;
import adrenaline.client.model.Map;
import adrenaline.client.model.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
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
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.*;

import static javafx.scene.effect.BlurType.GAUSSIAN;


public class GameViewController implements ViewInterface, PropertyChangeListener {

    @FXML
    private Pane pane, ownPlayer, ownCard, firemodeSelection, firemodeSet0, firemodeSet1, firemodeSet2;
    @FXML
    private Button  run, shoot, grab, reload, back;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane map;
    @FXML
    private VBox chat, enemyPlayers;
    @FXML
    private HBox ownDamage, ownMarks;
    @FXML
    private TextField txtMsg;
    @FXML
    private ImageView ownPlayerLabel, redAmmo1, redAmmo2, redAmmo3, blueAmmo1, blueAmmo2, blueAmmo3, yellowAmmo1, yellowAmmo2, yellowAmmo3,
            weaponRed1, weaponRed2, weaponRed3, weaponBlue1, weaponBlue2, weaponBlue3, weaponYellow1, weaponYellow2, weaponYellow3,
                     myWeapon,myPowerup, bgWeapon1, bgWeapon2, bgPowerup1, bgPowerup2, firemodeBackground, fire0, fire1, fire2;
    @FXML
    private Label message, timerLabel, timerComment;
    @FXML
    private Polygon powerupTriangle,weaponTriangle;
    private Timer clock = null;
    private HashMap<Integer, ArrayList<ImageView>> ammoBoxs = new HashMap<>();
    private HashMap<adrenaline.Color, ArrayList<ImageView>> weaponLists = new HashMap<>();
    private GameController gameController;
    private HashMap<adrenaline.Color, Pane> playersColorMap = new HashMap<>(); //forse non serve
    private HashMap<adrenaline.Color, ImageView> tokensMap = new HashMap<>();
    private HashMap<Pane, ArrayList<Position>> positionMap = new HashMap<>(); //get pane used positions
    private HashMap<ImageView, Position> tokenPosition = new HashMap<>(); //get token current position
    private HashMap<Integer,Integer> firemodeMap;
    private HashMap<ImageView, adrenaline.Color> tokenColor = new HashMap<>();
    private HashMap<adrenaline.Color, HBox> enemyWeaponsMap = new HashMap<>();
    private final int columns = 4;
    private boolean shootState = false;
    private ArrayList<adrenaline.Color> targets = new ArrayList<>();
    private int mode0 = 0, mode1 = 0, mode2 = 0;

    public void initialize(){
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 30);
        timerLabel.setFont(font);
        font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 16);
        timerComment.setFont(font);
        timerLabel.setTextFill(Color.WHITE);
        timerComment.setTextFill(Color.WHITE);
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
            for (ImageView img : y) img.getStyleClass().add("hand");
        });
        myWeapon.getStyleClass().add("hand");
        myPowerup.getStyleClass().add("hand");
        myPowerup.setEffect(new DropShadow());
        bgPowerup1.setEffect(new DropShadow());

        for(int i = 0; i <= 11; i++){
            Pane pane = (Pane) map.lookup("#pane"+i);
            pane.getChildren().get(0).setEffect(new Glow(0.5));
            pane.getChildren().get(0).getStyleClass().add("hand");
        }

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<Integer, Integer>>(){}.getType();
        firemodeMap = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/Jsonsrc/Firemode.json")), type);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        gameController.addPropertyChangeListener(this);
        initializeHUD();
        updatePlayer(gameController.getPlayersMap());
    }

    public void initializeHUD(){
        Map modelMap = gameController.getMap();
        String path = "url(/Graphic-assets/Maps/MAP"+ modelMap.getMapID()+".png)";
        map.setStyle("-fx-background-image: "+path);
        updateMap(modelMap);
        Pane skullPane = new Pane();
        ImageView skulls = new ImageView(new Image(getClass().getResourceAsStream("/Graphic-assets/SKULLBAR.png")));
        skulls.setFitHeight(57);
        skulls.setFitWidth(320);
        skullPane.getChildren().add(skulls);
        enemyPlayers.getChildren().add(skullPane);
        HashMap<String, adrenaline.Color> nicknamesMap = gameController.getPlayersNicknames();
        adrenaline.Color ownColor = gameController.getPlayersNicknames().get(gameController.getOwnNickname());
        ImageView token = new ImageView();
        tokensMap.put(ownColor, token);
        tokenColor.put(token, ownColor);
        String newImgUrl = "/Graphic-assets/HUD/" +ownColor.toString()+"-HUD.png";
        ownPlayerLabel.setImage(new Image(getClass().getResourceAsStream(newImgUrl)));
        String runPath = "url(/Graphic-assets/HUD/"+ownColor.toString()+"-RUN.png)";
        run.setStyle("-fx-background-image: "+ runPath);
        String grabPath = "url(/Graphic-assets/HUD/"+ownColor.toString()+"-GRAB.png)";
        grab.setStyle("-fx-background-image: "+ grabPath);
        String shootPath = "url(/Graphic-assets/HUD/"+ownColor.toString()+"-SHOOT.png)";
        shoot.setStyle("-fx-background-image: "+ shootPath);
        String reloadPath = "url(/Graphic-assets/HUD/"+ownColor.toString()+"-RELOAD.png)";
        reload.setStyle("-fx-background-image: "+ reloadPath);
        String backPath = "url(/Graphic-assets/HUD/"+ownColor.toString()+"-GOBACK.png)";
        back.setStyle("-fx-background-image: "+ backPath);
        nicknamesMap.forEach((y,x) -> {
                if(x.equals(ownColor)) playersColorMap.put(x, ownPlayer);
                else{
                    ImageView newtoken = new ImageView();
                    newtoken.getStyleClass().add("hand");
                    tokensMap.put(x, newtoken);
                    tokenColor.put(newtoken, x);
                    Pane newPane = new Pane();
                    String newUrl = "/Graphic-assets/HUD/" +x.toString()+"-SCOREBOARD.png";
                    ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(newUrl)));
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(320);
                    newPane.getChildren().add(imageView);
                    newPane.setId(x.toString());
                    Label nickname = new Label(y);
                    Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 16);
                    nickname.setFont(font);
                    nickname.minWidth(260);
                    nickname.maxWidth(260);
                    nickname.setLayoutY(5);
                    nickname.getStyleClass().add(x.toString());
                    nickname.setAlignment(Pos.TOP_RIGHT);
                    newPane.getChildren().add(nickname);
                    HBox damage = new HBox();
                    damage.setLayoutX(5);
                    damage.setLayoutY(65);
                    damage.setSpacing(9);
                    newPane.getChildren().add(damage);
                    HBox marks = new HBox();
                    marks.setLayoutX(167);
                    marks.setLayoutY(35);
                    marks.setSpacing(2);
                    newPane.getChildren().add(marks);
                    String cardsUrl = "/Graphic-assets/HUD/CARDS_BUTTON.png";
                    ImageView cards = new ImageView(new Image(getClass().getResourceAsStream(cardsUrl)));
                    cards.setLayoutY(20);
                    cards.setFitWidth(40);
                    cards.setFitHeight(40);
                    cards.getStyleClass().add("hand");
                    cards.setOnMouseClicked(this::showEnemyCards);
                    newPane.getChildren().add(cards);
                    Pane cardsPane = new Pane();
                    cardsPane.setLayoutY(20);
                    cardsPane.setVisible(false);
                    cardsPane.getStyleClass().add("blackTransparent");
                    cardsPane.setOnMouseClicked(this::hideEnemyCards);
                    HBox weapons = new HBox();
                    weapons.setLayoutX(46);
                    enemyWeaponsMap.put(x,weapons);
                    cardsPane.getChildren().add(weapons);
                    String pwupUrl = "/Graphic-assets/Powerups/powerup-BACK.png";
                    ImageView powerup = new ImageView(new Image(getClass().getResourceAsStream(pwupUrl)));
                    powerup.setFitHeight(70);
                    powerup.setFitWidth(45);
                    cardsPane.getChildren().add(powerup);
                    Label num = new Label();
                    num.setLayoutX(17);
                    num.setLayoutY(28);
                    num.setFont(font);
                    num.setText("0");
                    num.getStyleClass().add("RED");
                    cardsPane.getChildren().add(num);
                    newPane.getChildren().add(cardsPane);
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
        ImageView square = (ImageView) event.getSource();
        Platform.runLater(()-> {
            for(int i = 0; i <= 11; i++) ((Pane) map.lookup("#pane"+i)).getChildren().get(0).setVisible(false);
        });
        gameController.selectSquare(Integer.parseInt(square.getId().substring(4)));
    }

    public void showError(String error) {
        Platform.runLater(() -> {
            message.getStyleClass().clear();
            message.getStyleClass().add("RED");
            this.message.setText(error);
            //shootState = false;
        });
    }

    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> {
            this.message.getStyleClass().clear();
            this.message.getStyleClass().add("GREEN");
            this.message.setText(message);
            if(message.contains("HIT")) {
                clearShootInfo();
                shootState = false;
            }
        });
    }

    public void changeStage() {

    }

    public void notifyTimer(Integer duration, String comment) {
        Platform.runLater(() -> {
            shootState = false;
            clearShootInfo();
            timerComment.setText(comment);
            timerLabel.setTextFill(Color.WHITE);
        });
        if (clock != null){
            clock.cancel();
            clock.purge();
        }
        clock = new Timer();
        clock.scheduleAtFixedRate(new TimerTask() {
            int time = duration;
            @Override
            public void run() {
                if (time > 0) {
                    Platform.runLater(() -> {
                        if(time==10) timerLabel.setTextFill(Color.MEDIUMVIOLETRED);
                        timerLabel.setText(Integer.toString(time));
                    });
                    time--;
                }
            }
        }, 0, 1000);
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
        Platform.runLater(()-> validSquares.forEach(i -> ((Pane) map.lookup("#pane"+i)).getChildren().get(0).setVisible(true)));
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(()-> {
            for(int i = 0; i <= 11; i++) ((Pane) map.lookup("#pane"+i)).getChildren().get(0).setVisible(false);
        });
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
            case "shoot":
                shootState = true;
                gameController.shoot();
                break;
            case "back":
                gameController.back();
                shootState = false;
                clearShootInfo();
                for(int i = 0; i <= 11; i++) ((Pane) map.lookup("#pane"+i)).getChildren().get(0).setVisible(false);
                break;
            default: break;
        }
    }

    public synchronized void drawBottom(MouseEvent mouseEvent){
        ImageView bottom = new ImageView();
        ImageView image = (ImageView) mouseEvent.getSource();
        String imgUrl = image.getImage().getUrl();
        String imgName = new File(imgUrl).getName();
        String newImgUrl = "/Graphic-assets/Weapons/" +imgName.substring(0, imgName.length()-7) + "BOTTOM.png";
        try {
            bottom.setImage(new Image(new File(getClass().getResource(newImgUrl).toURI()).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        bottom.setFitWidth(127);
        bottom.setFitHeight(120);
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
        if(!gameController.getPlayersMap().get(gameController.getOwnColor()).getPowerupCards().isEmpty()) powerupTriangle.setVisible(true);
        if(!gameController.getPlayersMap().get(gameController.getOwnColor()).getWeaponCards().isEmpty()) weaponTriangle.setVisible(true);
    }

    public void hideTriangles(){
        powerupTriangle.setVisible(false);
        weaponTriangle.setVisible(false);
    }

    private void updateMap(Map newMap){
        Platform.runLater(() -> {
            HashMap<Integer,Integer> ammoMap = newMap.getAmmoMap();
            ammoMap.forEach((x,y) -> {
                Pane ammoPane = (Pane) map.lookup("#pane"+x);
                ImageView ammoImage = (ImageView) ammoPane.getChildren().get(1);
                if(y == 0) ammoImage.setImage(null);
                else {
                    String imgUrl = y.toString() + ".png";
                    ammoImage.setImage(new Image(getClass().getResourceAsStream("/Graphic-assets/Ammo/ammo-" + imgUrl)));
                }
            });
            newMap.getWeaponMap().forEach((x,y) -> {
                ArrayList<ImageView> list = weaponLists.get(x);
                for(int i = 0; i < 3; i++) {
                    if(i > y.size()-1) list.get(i).setImage(null);
                    else {
                        String newImgUrl = "/Graphic-assets/Weapons/weapon_" + y.get(i) + "-TOP.png";
                        try {
                            list.get(i).setImage(new Image(new File(getClass().getResource(newImgUrl).toURI()).toURI().toString()));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        });
    }


    public void updatePlayer(HashMap<adrenaline.Color, Player> newPlayersMap){
        //set own powerups
        Player ownPlayer = newPlayersMap.get(gameController.getOwnColor());
        ArrayList<Integer> puCards = ownPlayer.getPowerupCards();
        Platform.runLater(() -> {
            if (!puCards.isEmpty()) {
                String newImgUrl = "/Graphic-assets/Powerups/powerup-" + puCards.get(0) + ".png";
                try {
                    myPowerup.setImage(new Image(new File(getClass().getResource(newImgUrl).toURI()).toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                bgPowerup1.setVisible(puCards.size() > 1);
                bgPowerup2.setVisible(puCards.size() > 2);
            } else{
                myPowerup.setImage(null);
                bgPowerup1.setVisible(false);
                bgPowerup2.setVisible(false);
            }
        });

        //set damage
        Platform.runLater(() -> {
            newPlayersMap.forEach((x,y)->{
                ArrayList<adrenaline.Color> list = y.getDamage();
                if(x.equals(gameController.getOwnColor())) updateDamageMarks(list,ownDamage,30,20);
                else {
                    Pane playerPane = (Pane) enemyPlayers.lookup("#"+x.toString());
                    HBox damageTraker = (HBox) playerPane.getChildren().get(2);
                    updateDamageMarks(list,damageTraker,25,17);
                }
            });
        });

        //set marks
        Platform.runLater(() -> {
            newPlayersMap.forEach((x,y) -> {
                ArrayList<adrenaline.Color> list = y.getMarks();
                if(x.equals(gameController.getOwnColor())) {
                    ownMarks.getChildren().clear();
                    updateDamageMarks(list,ownMarks,28,18);
                }
                else {
                    Pane playerPane = (Pane) enemyPlayers.lookup("#"+x.toString());
                    HBox markTraker = (HBox) playerPane.getChildren().get(3);
                    markTraker.getChildren().clear();
                    updateDamageMarks(list,markTraker,20,13);
                }
            });
        });

        //set own weapons
        ArrayList<Integer> wpCards = ownPlayer.getWeaponCards();
        Platform.runLater(() -> {
            if(!wpCards.isEmpty()){
                String newImgUrl = "/Graphic-assets/Weapons/weapon_" + wpCards.get(0) + "-TOP.png";
                try {
                    myWeapon.setImage(new Image(new File(getClass().getResource(newImgUrl).toURI()).toURI().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                bgWeapon1.setVisible(wpCards.size()>1);
                bgWeapon2.setVisible(wpCards.size()>2);
            } else{
                myWeapon.setImage(null);
                bgWeapon1.setVisible(false);
                bgWeapon2.setVisible(false);
            }
        });

        //set others weapons
        Platform.runLater(()->{
            newPlayersMap.forEach((x,y)->{
                if(x!=gameController.getOwnColor()){
                    HBox weapons = enemyWeaponsMap.get(x);
                    ArrayList<Integer> cards = y.getWeaponCards();
                    weapons.getChildren().clear();
                    if(!cards.isEmpty()){
                        for (Integer card : cards) {
                            String weaponUrl = "/Graphic-assets/Weapons/weapon_" + card + "-TOP.png";
                            ImageView weapon = new ImageView();
                            weapon.setFitWidth(94);
                            weapon.setFitHeight(70);
                            weapon.setImage(new Image(getClass().getResourceAsStream(weaponUrl)));
                            weapons.getChildren().add(weapon);
                        }
                    }
                    Pane parent = (Pane) weapons.getParent();
                    Label num = (Label) parent.getChildren().get(2);
                    num.setText(Integer.toString(y.getPowerupCards().size()));
                }
            });
        });

        //set ammo
        int[] ammoBox = ownPlayer.getAmmoBox();
        if(ammoBox != null) {
            Platform.runLater(() -> {
                for (int i = 0; i < 3; i++) {
                    int ammoNum = ammoBox[i];
                    ArrayList<ImageView> ammoList = ammoBoxs.get(i);
                    ammoList.forEach(x -> {
                        if (ammoList.indexOf(x) < ammoNum) x.setVisible(true);
                        else x.setVisible(false);
                    });
                }
            });
        }

        //set player position
        updatePosition(newPlayersMap);
    }

    private void updateDamageMarks(ArrayList<adrenaline.Color> list, HBox damageTracker, int height, int width){
        if(list.isEmpty()) damageTracker.getChildren().clear();
        else {
            for (int i = 0; i < list.size(); i++) {
                if (i >= (damageTracker.getChildren().size())) {
                    String damegeUrl = "/Graphic-assets/HUD/" + list.get(i).toString() + "-DROP.png";
                    ImageView damage = new ImageView();
                    damage.setFitWidth(width);
                    damage.setFitHeight(height);
                    damage.setImage(new Image(getClass().getResourceAsStream(damegeUrl)));
                    damageTracker.getChildren().add(damage);
                }
            }
        }
    }

    private void updatePosition(HashMap<adrenaline.Color,Player> newPlayersMap){
        newPlayersMap.forEach((x,y) -> {
            Platform.runLater(() -> {
                if((y.getPosition() != -1)){
                    ImageView token = tokensMap.get(x);
                    Position newPosition;
                    ArrayList<Position> list = new ArrayList<>();
                    Pane newPane = (Pane) map.lookup("#pane"+y.getPosition());
                    Pane oldPane = (Pane) token.getParent();
                    if(oldPane == null) { //if is the first turn
                        token.setOnMouseClicked(this::selectTarget);
                        String tokenUrl = "/Graphic-assets/"+x.toString()+"-TOKEN.png";
                        token.setImage(new Image(getClass().getResourceAsStream(tokenUrl)));
                        token.setFitHeight(45);
                        token.setFitWidth(45);
                        newPosition = getFreePosition(newPane);
                        token.setLayoutX(newPosition.getX());
                        token.setLayoutY(newPosition.getY());
                        if(positionMap.get(newPane) != null) list = positionMap.get(newPane);
                        list.add(newPosition);
                        positionMap.put(newPane, list);
                        tokenPosition.put(token, newPosition);
                        newPane.getChildren().add(token);
                    }
                    else if(newPane != oldPane){
                        newPosition = getFreePosition(newPane);
                        if(positionMap.get(newPane) == null) list.add(newPosition);
                        else {
                            list = positionMap.get(newPane);
                            list.add(newPosition);
                        }
                        TranslateTransition transition = new TranslateTransition();
                        transition.setNode(token);
                        transition.setDuration(Duration.millis(700));
                        Position oldPosition = tokenPosition.get(token);
                        int oldX = (y.getOldPosition()%columns -y.getPosition()%columns)*145 + oldPosition.getX() -newPosition.getX();
                        int oldY = (y.getOldPosition()/columns -y.getPosition()/columns)*148 + oldPosition.getY() -newPosition.getY();
                        oldPane.getChildren().remove(token);
                        token.setLayoutX(newPosition.getX());
                        token.setLayoutY(newPosition.getY());
                        newPane.getChildren().add(token);
                        transition.setFromX(oldX);
                        transition.setFromY(oldY);
                        transition.setToX(0);
                        transition.setToY(0);
                        positionMap.put(newPane, list);
                        ArrayList<Position> positions = positionMap.get(oldPane);
                        Position pos = tokenPosition.get(token);
                        positions.remove(pos);
                        positionMap.put(oldPane,positions);
                        //positionMap.get(oldPane).remove(tokenPosition.get(token));
                        if(positionMap.get(oldPane).isEmpty()) positionMap.remove(oldPane);
                        tokenPosition.put(token, newPosition);
                        transition.setOnFinished(e -> {
                            token.setTranslateX(0);
                            token.setTranslateY(0);
                        });
                        transition.play();
                    }
                }
            });
        });
    }

    private void selectTarget(Event event) {
        ImageView token = (ImageView) event.getSource();
        if(shootState) {
            targets.add(tokenColor.get(token));
            token.setEffect(new Glow(0.5));
        }
        else {
            ArrayList<adrenaline.Color> player = new ArrayList<>(Collections.singletonList(tokenColor.get(token)));
            gameController.selectPlayers(player);
        }
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
        powerup = "/Graphic-assets/Powerups/powerup-" +powerUpList.get(newIndex)+".png";
        try {
            myPowerup.setImage(new Image(new File(getClass().getResource(powerup).toURI()).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void nextWeapon(){
        ArrayList<Integer> weaponList = gameController.getPlayersMap().get(gameController.getOwnColor()).getWeaponCards();
        String weapon = myWeapon.getImage().getUrl();
        weapon = new File(weapon).getName();
        weapon = weapon.substring(weapon.indexOf('_') + 1, weapon.indexOf('-'));
        int newIndex;
        if(weaponList.indexOf(Integer.parseInt(weapon)) == (weaponList.size() -1)) newIndex = 0;
        else newIndex = weaponList.indexOf(Integer.parseInt(weapon)) + 1;
        weapon = "/Graphic-assets/Weapons/weapon_" +weaponList.get(newIndex)+"-TOP.png";
        try {
            myWeapon.setImage(new Image(new File(getClass().getResource(weapon).toURI()).toURI().toString()));
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

    public void selectWeapon(Event event){
        message.setText("");
        //targets.forEach(x -> tokensMap.get(x).setEffect(null));
        ImageView weapon = (ImageView) event.getSource();
        String weaponID = weapon.getImage().getUrl();
        weaponID = new File(weaponID).getName();
        weaponID = weaponID.substring(weaponID.indexOf('_') + 1, weaponID.indexOf('-'));
        gameController.selectWeapon(Integer.parseInt(weaponID));
        if(weapon == myWeapon) weaponSelection(Integer.parseInt(weaponID));
    }

    private void weaponSelection(int weaponID){
        if(shootState) {
            ownCard.setVisible(false);
            firemodeSelection.setVisible(true);
            String path = "/Graphic-assets/Weapons/weapon_" + weaponID + "-BOTTOM.png";
            int firemode = firemodeMap.get(weaponID);
            Pane weapon = firemodeSet0;
            switch (firemode){
                case 0: gameController.selectFiremode(0);
                        fire0.setFitHeight(120);
                        fire0.setFitWidth(127);
                        fire0.setLayoutX(20);
                        fire0.setLayoutY(10);
                        fire0.setVisible(true);
                        break;
                case 1: weapon = firemodeSet1;
                        fire0.setFitHeight(60);
                        fire0.setFitWidth(127);
                        fire0.setLayoutX(20);
                        fire0.setLayoutY(10);
                        fire1.setFitHeight(60);
                        fire1.setFitWidth(127);
                        fire1.setLayoutX(20);
                        fire1.setLayoutY(70);
                        break;
                case 2: weapon = firemodeSet2;
                        fire0.setFitHeight(60);
                        fire0.setFitWidth(127);
                        fire0.setLayoutX(20);
                        fire0.setLayoutY(10);
                        fire1.setFitHeight(60);
                        fire1.setFitWidth(63);
                        fire1.setLayoutX(20);
                        fire1.setLayoutY(70);
                        fire2.setFitHeight(60);
                        fire2.setFitWidth(63);
                        fire2.setLayoutX(83);
                        fire2.setLayoutY(70);
                        break;
                default: break;
            }
            firemodeBackground.setImage(new Image(getClass().getResourceAsStream(path)));
            weapon.getStyleClass().add("setFiremode");
            weapon.setVisible(true);
        }
    }

    public void selectFiremode(Event event) {
        Pane clickedFiremode = (Pane) event.getSource();
        int firemodeID = Integer.parseInt(clickedFiremode.getId().substring(12));
        switch (firemodeID){
            case 0: fire0.setVisible(true); break;
            case 1: mode1 = 1;
                    fire1.setVisible(true);
                    break;
            case 2: mode2 = 2;
                    fire2.setVisible(true);
                    break;

            default: break;
        }
    }

    public void sendShoot() {
        if(targets.isEmpty()){
            gameController.selectFiremode(mode0+mode1+mode2);
        }
        else {
            gameController.selectPlayers(targets);
            targets.forEach(x -> tokensMap.get(x).setEffect(null));
            //for(int i = 0; i <= 11; i++) ((Pane) map.lookup("#pane"+i)).getChildren().get(0).setVisible(false);
            shootState = false;
        }
    }

    private void clearShootInfo(){
        mode1 = 0;
        mode2 = 0;
        firemodeSet0.setVisible(false);
        firemodeSet1.setVisible(false);
        firemodeSet2.setVisible(false);
        fire0.setVisible(false);
        fire1.setVisible(false);
        fire2.setVisible(false);
        targets.clear();
        firemodeBackground.setImage(null);
        firemodeSelection.setVisible(false);
        ownCard.setVisible(true);
    }

    private void showEnemyCards(Event event){
        Node source = (Node) event.getSource();
        Pane parent = (Pane) source.getParent();
        parent.getChildren().get(5).setVisible(true);
    }

    private void hideEnemyCards(Event event) {
        Pane source = (Pane) event.getSource();
        source.setVisible(false);
    }

    public void moveSub(){ gameController.moveSubAction(); }
}
