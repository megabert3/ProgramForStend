package stend.controller.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class exceptionFrameController {

    private String exceptionMessage;

    @FXML
    private Label exceptionLabel;

    public exceptionFrameController(String exceptoinMessage) {
        this.exceptionMessage = exceptoinMessage;
    }

    @FXML
    void initialize() {
        exceptionLabel.setText(exceptionMessage);
    }
}
