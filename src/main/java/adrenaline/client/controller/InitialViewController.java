package adrenaline.client.controller;

import adrenaline.client.RMIClient;
import adrenaline.client.SocketClient;
import adrenaline.client.view.ClientGui;
import adrenaline.client.view.ConfirmBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class InitialViewController {

    public Pane pane;
    public Button rmi, socket, search, close;
    public TextField host, port, name;
    public Label label;
    public Text errorText;
    private int choice;

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
        search.setFont(font);
        label.setFont(font);
        errorText.setFont(font);
    }

    public void RMISelected(){
        if(!host.getText().equals("") && (!port.getText().equals(""))){
            choice = 0;
            createClient();
        }
        else errorText.setText("Type host and port");
    }

    public void SocketSelected(){
        if(!host.getText().equals("") && (!port.getText().equals(""))) {
            choice = 1;
            createClient();
        }
        else errorText.setText("Type host and port");
    }

    public void changeScene(){
        errorText.setText("");
        errorText.setLayoutY(383);
        errorText.setLayoutX(300);
        rmi.setVisible(false);
        socket.setVisible(false);
        host.setVisible(false);
        port.setVisible(false);
        name.setVisible(true);
        search.setVisible(true);
    }

    public void createClient(){
        int flag = 0;
        try{
            if(choice == 0) new RMIClient(host.getText(), Integer.parseInt(port.getText()));
            if(choice == 1) new SocketClient(host.getText(), Integer.parseInt(port.getText()));
            } catch (RemoteException e) {
                errorText.setText("Wrong host/port");
                flag = 1;
            } catch (NotBoundException e) {
                errorText.setText("An RMI error has occurred");
                flag = 1;
            } catch (IOException e) {
                errorText.setText("Wrong host/port");
                flag = 1;
        }
        if(flag == 0) changeScene();
    }

    public void sendNickname(){
        if(!name.getText().equals("")) {
            label.setVisible(true);
            search.setDisable(true);
            name.setDisable(true);
        }
        else errorText.setText("Type a nickname");
    }

    public void close(){
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            Stage stage = (Stage)pane.getScene().getWindow();
            stage.close();
        }
    }
}
