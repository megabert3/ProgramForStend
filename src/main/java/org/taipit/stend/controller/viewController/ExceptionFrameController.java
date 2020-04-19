package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        okBtn.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    Stage stage = (Stage) okBtn.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }

    public Label getLabel() {
        return label;
    }
}
