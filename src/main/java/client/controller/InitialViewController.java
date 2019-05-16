package client.controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class InitialViewController {

    public Button rmi, socket;
    public TextField host, port;

    public void initialize(){
        rmi.getStyleClass().add("button");
        socket.getStyleClass().add("button");
        host.getStyleClass().add("host");
        port.getStyleClass().add("port");
    }
}
