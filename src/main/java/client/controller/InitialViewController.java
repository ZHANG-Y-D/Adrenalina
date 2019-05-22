package client.controller;

import client.RMIClient;
import client.SocketClient;
import client.view.ConfirmBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class InitialViewController {

    public Pane pane;
    public Button rmi, socket, search, close;
    public TextField host, port, name;
    private int choice;

    public void initialize(){
        pane.getStyleClass().add("pane");
        close.getStyleClass().add("close");
    }

    public void RMISelected(){
        choice = 0;
        changeScene();
    }

    public void SocketSelected(){
        choice = 1;
        changeScene();
    }

    public void changeScene(){
        rmi.setVisible(false);
        socket.setVisible(false);
        host.setVisible(false);
        port.setVisible(false);
        name.setVisible(true);
        search.setVisible(true);
    }

    public void createClient(){
        try{
            if(choice == 0) new RMIClient(host.getText(), Integer.parseInt(port.getText()), name.getText());
            if(choice == 1) new SocketClient(host.getText(), Integer.parseInt(port.getText()), name.getText());
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close(){
        boolean answer = ConfirmBox.display("QUIT", "Are you sure you want to exit?");
        if (answer) {
            Stage stage = (Stage)pane.getScene().getWindow();
            stage.close();
        }
    }
}
