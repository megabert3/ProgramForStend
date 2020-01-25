package stend.controller.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExceptionFrameController {

    private String exceptionMessage;

    @FXML
    private Label exceptionLabel;

    public ExceptionFrameController(String exceptoinMessage) {
        this.exceptionMessage = exceptoinMessage;
    }

    @FXML
    void initialize() {
        exceptionLabel.setText(exceptionMessage);
    }
}
