module proj {
    requires javafx.fxml;
    requires javafx.controls;
    requires gson;
    requires java.rmi;

    exports client.view;
    exports server;
}