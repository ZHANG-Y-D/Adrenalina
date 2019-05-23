package client.controller;

import client.view.ClientGui;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.HashMap;

public class SelectionViewController {
    public ImageView avatar1, avatar2, avatar3, avatar4, avatar5;
    public ImageView map1,map2,map3,map4;
    public StackPane stack1,stack2,stack3, stack4;
    public Button next,ok;
    public Label title;
    private HashMap<Integer, ImageView> imageMap;

    public void initialize(){
        imageMap = new HashMap<>();
        imageMap.put(1, avatar1);
        imageMap.put(2, avatar2);
        imageMap.put(3, avatar3);
        imageMap.put(4, avatar4);
        imageMap.put(5, avatar5);
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"),60);
        title.setFont(font);
        next.setFont(font);
        map1.getStyleClass().add("map");
        map2.getStyleClass().add("map");
        map3.getStyleClass().add("map");
        map4.getStyleClass().add("map");
        stack1.getStyleClass().add("stack");
        stack2.getStyleClass().add("stack");
        stack3.getStyleClass().add("stack");
        stack4.getStyleClass().add("stack");
        Tooltip.install(map1, new Tooltip("This map is good for 3 or 4 players"));
        Tooltip.install(map2, new Tooltip("This map is good for any numbers of players."));
        Tooltip.install(map3, new Tooltip("This map is good for 4 or 5 players."));
        Tooltip.install(map4, new Tooltip("This map is good for any number of players."));
    }

    public void nextImage(){
        Image firstImg = imageMap.get(1).getImage();
        for(int i=1;i<5;i++){
            imageMap.get(i).setImage(imageMap.get(i+1).getImage());
        }
        imageMap.get(5).setImage(firstImg);
    }

    public void changeScene(){
        avatar1.setVisible(false);
        avatar2.setVisible(false);
        avatar3.setVisible(false);
        avatar4.setVisible(false);
        avatar5.setVisible(false);
        next.setVisible(false);
        ok.setVisible(false);
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
}