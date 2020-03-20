package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;
import org.taipit.stend.model.Methodic;
import org.taipit.stend.model.MethodicsForTest;

public class YesOrNoFrameController {

    MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    private Stage stageSaveResultTest;

    private Methodic methodic;

    private boolean deliteMethodic;

    private boolean exitSaveResultFrameWithoutSaving;

    @FXML
    private Button noBtn;

    @FXML
    private Label questionTxt;

    @FXML
    private Button yesBtn;

    @FXML
    void initialize() {
    }

    @FXML
    void yesOrNoAction(ActionEvent event) {

        Stage stageYesOrNo = (Stage) questionTxt.getScene().getWindow();
        //если вызвано с окна удаления методики "Удалить методику"
        if (event.getSource() == yesBtn) {
            if (deliteMethodic) {

                methodicsForTest.deleteMethodic(methodic.getMethodicName());

                methodicsAddEditDeleteFrameController.refreshAfterDelete();

                stageYesOrNo.close();

                deliteMethodic = false;
            }

            if (exitSaveResultFrameWithoutSaving) {
                stageYesOrNo.close();
                stageSaveResultTest.close();
                exitSaveResultFrameWithoutSaving = false;
            }
        }

        if (event.getSource() == noBtn) {
            stageYesOrNo.close();
        }
    }

    public void setDeliteMethodic(boolean deliteMethodic) {
        this.deliteMethodic = deliteMethodic;
    }

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
    }

    public Label getQuestionTxt() {
        return questionTxt;
    }

    public void setMethodicsAddEditDeleteFrameController(MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController) {
        this.methodicsAddEditDeleteFrameController = methodicsAddEditDeleteFrameController;
    }

    public void setExitSaveResultFrameWithoutSaving(boolean exitSaveResultFrameWithoutSaving) {
        this.exitSaveResultFrameWithoutSaving = exitSaveResultFrameWithoutSaving;
    }

    public void setStageSaveResultTest(Stage stageSaveResultTest) {
        this.stageSaveResultTest = stageSaveResultTest;
    }
}
