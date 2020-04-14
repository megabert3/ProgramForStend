package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ExceptionFrameController {

    @FXML
    private Label label;

    @FXML
    private Button okBtn;

    @FXML
    void okAction(ActionEvent event) {
        if (event.getSource() == okBtn) {
            Stage stage = (Stage) okBtn.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void initialize() {

    }

    public Label getLabel() {
        return label;
    }
}
