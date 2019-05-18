module proj {
    requires javafx.fxml;
    requires javafx.controls;
    requires gson;
    requires java.rmi;

    exports client;
    exports client.controller;
    exports client.view;
    exports server;
}