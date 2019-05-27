package adrenaline.client.view;

import adrenaline.client.controller.Controller;

public interface ViewInterface {
    void showError(String error);

    void setController(Controller controller);
}
