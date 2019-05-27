package adrenaline.client.view;

import adrenaline.client.ConnectionHandler;
import adrenaline.client.RMIHandler;
import adrenaline.client.SocketHandler;
import adrenaline.client.controller.Controller;
import adrenaline.client.view.ClientGui;
import adrenaline.client.view.ConfirmBox;
import adrenaline.client.view.ViewInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;

public class InitialViewController implements ViewInterface {

    public Pane pane;
    public Button rmi, socket, play, close;
    public TextField host, port, name;
    public Label label,error;
    private Controller controller = null;

    public void initialize(){
        pane.getStyleClass().add("pane");
        close.getStyleClass().add("close");
        Font font = Font.loadFont(ClientGui.class.getResourceAsStream("/airstrike.ttf"),16);
        close.setFont(font);
        rmi.setFont(font);
        socket.setFont(font);
        host.setFont(font);
        port.setFont(font);
        name.setFont(font);
        play.setFont(font);
        label.setFont(font);
        error.setFont(font);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void RMISelected(){
        if(!host.getText().equals("") && (!port.getText().equals(""))){
            try{
                if(controller.connectRMI(host.getText(), Integer.parseInt(port.getText()))) changeScene();
            }catch (NumberFormatException e){
                error.setText("Wrong host/port");
            }
        }
        else error.setText("Type host and port");
    }

    public void SocketSelected(){
        if(!host.getText().equals("") && (!port.getText().equals(""))) {
            try {
                if(controller.connectSocket(host.getText(), Integer.parseInt(port.getText()))) changeScene();
            }catch (NumberFormatException e){
                error.setText("Wrong host/port");
            }
        }
        else error.setText("Type host and port");
    }

    public void changeScene(){
        error.setText("");
        rmi.setVisible(false);
        socket.setVisible(false);
        host.setVisible(false);
        port.setVisible(false);
        name.setVisible(true);
        play.setVisible(true);
    }

    public void sendNickname(){
        if(!name.getText().equals("")) {
            label.setVisible(true);
            play.setDisable(true);
            name.setDisable(true);
            error.setText("");
            controller.setNickname(name.getText());
        }
        else error.setText("Type a nickname");
    }

    public void close(){
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            controller.cleanExit();
            Stage stage = (Stage)pane.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void showError(String error) {
        Platform.runLater(() -> this.error.setText(error));
    }
}
