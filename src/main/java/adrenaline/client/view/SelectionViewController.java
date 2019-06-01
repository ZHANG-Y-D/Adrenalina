package adrenaline.client.view;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;

public class SelectionViewController implements ViewInterface {
    public Pane selectionPane;
    public ImageView avatar1, avatar2, avatar3, avatar4, avatar5;
    public ImageView map1,map2,map3,map4;
    public StackPane stack1,stack2,stack3,stack4;
    public Button next,select,close;
    public Label title,error,playersList;
    public VBox nicknamesBox;
    private HashMap<Integer, ImageView> imageMap;
    private HashMap<String, Color> colorMap;
    private GameController gameController;

    public void initialize(){
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
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"),60);
        title.setFont(font);
        font= Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"),40);
        next.setFont(font);
        font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"),20);
        select.setFont(font);
        close.setFont(font);
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
    }

    public void setGameController(GameController gameController){
        this.gameController = gameController;
        notifyView();
    }

    @Override
    public void notifyView() {
        Platform.runLater(() -> {
            if (nicknamesBox.getChildren().isEmpty()){
                gameController.getPlayersNicknames().forEach((x,y) -> {
                    Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"),16);
                    Label newLabel = new Label(x);
                    newLabel.setFont(font);
                    newLabel.getStyleClass().add("WHITE");
                    nicknamesBox.getChildren().add(newLabel);
                });
            }
            else {
                nicknamesBox.getChildren().forEach(x -> {
                    Label label = (Label) x;
                    label.getStyleClass().clear();
                    label.getStyleClass().add(gameController.getPlayersNicknames().get(label.getText()).toString());
                });
                gameController.getPlayersNicknames().values().forEach(x -> {
                    if (x != Color.WHITE) {
                        imageMap.values().forEach(y -> {
                            String imgUrl = y.getImage().getUrl();
                            if(colorMap.get(imgUrl)==x && !imgUrl.substring(0,5).equals("TAKEN")){
                                String newImgUrl = "TAKEN-"+imgUrl;
                                colorMap.put(newImgUrl, colorMap.remove(imgUrl));
                                y.setImage(new Image(new File(newImgUrl).toURI().toString()));
                            }
                        });
                    }
                });
            }
        });
    }

    public void nextImage(){
        Image firstImg = imageMap.get(1).getImage();
        for(int i=1;i<5;i++){
            imageMap.get(i).setImage(imageMap.get(i+1).getImage());
        }
        imageMap.get(5).setImage(firstImg);
        error.setText("");
    }

    public void selectAvatar(){
        gameController.selectAvatar(colorMap.get(avatar1.getImage().getUrl()));
    }

    public void changeScene(){
        avatar1.setVisible(false);
        avatar2.setVisible(false);
        avatar3.setVisible(false);
        avatar4.setVisible(false);
        avatar5.setVisible(false);
        next.setVisible(false);
        select.setVisible(false);
        stack1.setVisible(true);
        stack2.setVisible(true);
        stack3.setVisible(true);
        stack4.setVisible(true);
        title.setText("VOTE A MAP");
    }

    public void mapHover(Event event){
        ImageView map = (ImageView) event.getSource();
        Glow glow = new Glow(0.2);
        map.setEffect(glow);
    }

    public void mapDefault(Event event){
        ImageView map = (ImageView) event.getSource();
        Glow glow = new Glow(0);
        map.setEffect(glow);
    }

    public void showError(String errorMsg){
        Platform.runLater(() -> {
            if(errorMsg.equals("/OK")) {
                this.error.setText("Avatar selected");
                next.setDisable(true);
                select.setDisable(true);
            }
            else this.error.setText(errorMsg);
        });
    }

    public void changeStage(){

    }


    public void close(){
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            gameController.cleanExit();
            Stage stage = (Stage)selectionPane.getScene().getWindow();
            stage.close();
        }
    }


}
