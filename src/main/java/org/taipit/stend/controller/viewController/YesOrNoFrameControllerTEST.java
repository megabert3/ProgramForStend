package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.taipit.stend.helper.ConsoleHelper;

public class YesOrNoFrameControllerTEST {

    @FXML
    private Button yesBtn;

    @FXML
    private Button noBtn;

    @FXML
    private Label questionTxt;

    @FXML
    void yesOrNoAction(ActionEvent event) {
        if (event.getSource() == yesBtn) {
            ConsoleHelper.setYesOrNo(true);
            Stage yesOrNoFrameControllerStage = (Stage) yesBtn.getScene().getWindow();
            yesOrNoFrameControllerStage.close();
        }

        if (event.getSource() == noBtn) {
            ConsoleHelper.setYesOrNo(false);
            Stage yesOrNoFrameControllerStage = (Stage) yesBtn.getScene().getWindow();
            yesOrNoFrameControllerStage.close();
        }
    }

    public Label getQuestionTxt() {
        return questionTxt;
    }
}
