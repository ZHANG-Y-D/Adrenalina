package client.controller;

import client.view.ClientGui;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.HashMap;

public class SelectionViewController {
    public ImageView avatar1, avatar2, avatar3, avatar4, avatar5;
    public ImageView map1,map2,map3,map4;
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
        map1.setVisible(true);
        map2.setVisible(true);
        map3.setVisible(true);
        map4.setVisible(true);
        title.setText("VOTE A MAP");
    }
}
