package adrenaline.client.view.GuiView;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.view.ViewInterface;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SelectionViewController implements ViewInterface, PropertyChangeListener {

    @FXML
    private Pane selectionPane;
    @FXML
    private ImageView avatar1, avatar2, avatar3, avatar4, avatar5;
    @FXML
    private ImageView skull1, skull2, skull3;
    @FXML
    private ImageView map1, map2, map3, map4;
    @FXML
    private StackPane stack1, stack2, stack3, stack4;
    @FXML
    private Button next, select, close, send;
    @FXML
    private Label title, message, playersList, timerLabel;
    @FXML
    private VBox nicknamesBox;
    @FXML
    private GridPane skullbar;

    private HashMap<Integer, ImageView> imageMap;
    private HashMap<String, Color> colorMap;
    private GameController gameController;
    private ArrayList<ImageView> skullList = new ArrayList<>();
    private ArrayList<ImageView> mapIDs = new ArrayList<>();
    private Timer timer;
    private int playerCount = 0;
    private int selectedSkull = -1;
    private int selectedMap = -1;
    private int nextstageTimer = 0;
    private String nextstageComment = "";

    public void initialize() {
        imageMap = new HashMap<>();
        imageMap.put(1, avatar1);
        imageMap.put(2, avatar2);
        imageMap.put(3, avatar3);
        imageMap.put(4, avatar4);
        imageMap.put(5, avatar5);
        colorMap = new HashMap<>();
        colorMap.put(avatar1.getImage().getUrl(), Color.YELLOW);
        colorMap.put(avatar2.getImage().getUrl(), Color.GRAY);
        colorMap.put(avatar3.getImage().getUrl(), Color.PURPLE);
        colorMap.put(avatar4.getImage().getUrl(), Color.GREEN);
        colorMap.put(avatar5.getImage().getUrl(), Color.BLUE);
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 60);
        title.setFont(font);
        font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 40);
        next.setFont(font);
        font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 20);
        select.setFont(font);
        close.setFont(font);
        send.setFont(font);
        playersList.setFont(font);
        map1.getStyleClass().add("map");
        map2.getStyleClass().add("map");
        map3.getStyleClass().add("map");
        map4.getStyleClass().add("map");
        stack1.getStyleClass().add("stack");
        stack2.getStyleClass().add("stack");
        stack3.getStyleClass().add("stack");
        stack4.getStyleClass().add("stack");
        Tooltip.install(map1, new Tooltip("This map is good for 3 or 4 players"));
        Tooltip.install(map2, new Tooltip("This map is good for any number of players."));
        Tooltip.install(map3, new Tooltip("This map is good for 4 or 5 players."));
        Tooltip.install(map4, new Tooltip("This map is good for any number of players."));
        skullList.add(skull3);
        skullList.add(skull2);
        skullList.add(skull1);
        mapIDs.add(map1);
        mapIDs.add(map2);
        mapIDs.add(map3);
        mapIDs.add(map4);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        if(gameController.getOwnColor()!=null && gameController.getOwnColor()!=Color.WHITE) changeStage();
        gameController.addPropertyChangeListener(this);
        initializeNicknames();
    }

    public void initializeNicknames() {
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"), 16);
        Platform.runLater(()->{
            timerLabel.setFont(font);
            timerLabel.getStyleClass().add("WHITE");
        });
        gameController.getPlayersNicknames().forEach((x, y) -> {
            Platform.runLater(() -> {
                Label newLabel = new Label(x);
                newLabel.setFont(font);
                newLabel.getStyleClass().add("WHITE");
                nicknamesBox.getChildren().add(newLabel);
            });
            playerCount++;
        });
    }

    public void notifyTimer(Integer duration, String comment) {
        if (timer != null){
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        if (playerCount == 0) {
            Platform.runLater(this::changeScene);
        } else if(playerCount<0){
            nextstageTimer=duration;
            nextstageComment = comment;
        }else timerLabel.setLayoutY(timerLabel.getLayoutY() + 26);
        playerCount--;
        timer.scheduleAtFixedRate(new TimerTask() {
            int time = duration;

            @Override
            public void run() {
                if (time > 0) {
                    Platform.runLater(() -> timerLabel.setText(Integer.toString(time)));
                    time--;
                }
            }
        }, 0, 1000);
    }

    /**
     *
     * Not supported at this stage
     *
     */
    public void newChatMessage(String nickname, Color senderColor, String message) {
        //operation not supported at this stage
    }

    /**
     *
     * Not supported at this stage
     *
     */
    public void showValidSquares(ArrayList<Integer> validSquares) {
        //operation not supported at this stage
    }

    public void nextImage() {
        Image firstImg = imageMap.get(1).getImage();
        for (int i = 1; i < 5; i++) {
            imageMap.get(i).setImage(imageMap.get(i + 1).getImage());
        }
        imageMap.get(5).setImage(firstImg);
        message.setText("");
    }

    public void selectAvatar() {
        gameController.selectAvatar(colorMap.get(avatar1.getImage().getUrl()));
    }

    public void changeScene(){
        avatar1.setVisible(false);
        avatar2.setVisible(false);
        avatar3.setVisible(false);
        avatar4.setVisible(false);
        avatar5.setVisible(false);
        next.setVisible(false);
        playersList.setVisible(false);
        nicknamesBox.setVisible(false);
        message.setText("");
        timerLabel.setLayoutY(10);
        select.setVisible(false);
        stack1.setVisible(true);
        stack2.setVisible(true);
        stack3.setVisible(true);
        stack4.setVisible(true);
        skullbar.setVisible(true);
        send.setVisible(true);
        title.setText("VOTE SETTINGS");

    }

    public void mapHover(Event event) {
        if (selectedMap == -1) {
            ImageView map = (ImageView) event.getSource();
            Glow glow = new Glow(0.2);
            map.setEffect(glow);
        }
    }

    public void mapDefault(Event event) {
        if (selectedMap == -1) {
            ImageView map = (ImageView) event.getSource();
            Glow glow = new Glow(0);
            map.setEffect(glow);
        }
    }

    public void selectMap(Event event) {
        if (selectedMap == -1) {
            ImageView map = (ImageView) event.getSource();
            selectedMap = mapIDs.indexOf(map) + 1;
            map.setEffect(new Glow(0.5));
        }
    }

    public void onSkull(Event event) {
        if (selectedSkull == -1) {
            Pane node = (Pane) event.getSource();
            ImageView skullHover = (ImageView) node.getChildren().get(0);
            for (ImageView skull : skullList) {
                skull.setVisible(true);
                if (skull.equals(skullHover)) break;
            }
        }
    }

    public void offSkull() {
        if (selectedSkull == -1) skullList.forEach(x -> x.setVisible(false));
    }

    public void selectSkull(Event event) {
        if (selectedSkull == -1) {
            Pane node = (Pane) event.getSource();
            ImageView skull = (ImageView) node.getChildren().get(0);
            selectedSkull = skullList.indexOf(skull) + 1;
            for (ImageView sk : skullList) {
                InnerShadow innerShadow = new InnerShadow();
                innerShadow.setColor(javafx.scene.paint.Color.LIGHTYELLOW);
                sk.setEffect(innerShadow);
                if (sk.equals(skull)) break;
            }
        }
    }

    public void showError(String errorMsg) {
        Platform.runLater(() -> {
            this.message.setText(errorMsg);
            });
        }

    @Override
    public void showMessage(String message) {
        Platform.runLater(() ->{
            this.message.setText(message);
            next.setDisable(true);
            select.setDisable(true);
        });
    }

    public void send() {
        selectedMap = (selectedMap == -1) ? 1 : selectedMap;
        selectedSkull = (selectedSkull == -1) ? 5 : 5 + selectedSkull;
        send.setDisable(true);
        gameController.sendSettings(selectedMap, selectedSkull);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("map")) changeStage();
        else {
            Platform.runLater(() -> {
                nicknamesBox.getChildren().forEach(x -> {
                    Label label = (Label) x;
                    label.getStyleClass().clear();
                    label.getStyleClass().add(gameController.getPlayersNicknames().get(label.getText()).toString());
                });
                gameController.getPlayersNicknames().values().forEach(x -> {
                    if (x != Color.WHITE) {
                        imageMap.values().forEach(y -> {
                            String imgUrl = y.getImage().getUrl();
                            String imgName = new File(imgUrl).getName();
                            if (colorMap.get(imgUrl) == x && !imgName.contains("TAKEN")) {
                                String newImgUrl = File.separator+"Graphic-assets"+File.separator+"Avatars"+ File.separator + imgName.substring(0, imgName.length() - 4) + "-TAKEN.png";
                                y.setImage(new Image(new File(getClass().getResource(newImgUrl).toExternalForm()).toString()));
                                colorMap.put(y.getImage().getUrl(), colorMap.remove(imgUrl));
                            }
                        });
                    }
                });
            });
        }
    }

    public void changeStage() {
        Platform.runLater(() ->{
            try {
                gameController.removePropertyChangeListener(this);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
                Parent nextView = loader.load();
                Scene scene = new Scene(nextView);
                ViewInterface viewController = loader.getController();
                viewController.setGameController(gameController);
                gameController.setViewController(viewController);
                Stage stage = (Stage) selectionPane.getScene().getWindow();
                stage.setWidth(1280);
                stage.setHeight(768);
                stage.centerOnScreen();
                stage.setScene(scene);
                viewController.notifyTimer(nextstageTimer, nextstageComment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            gameController.cleanExit();
            Stage stage = (Stage) selectionPane.getScene().getWindow();
            stage.close();
        }
    }

}