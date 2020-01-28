package stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;
import stend.model.Methodic;
import stend.model.MethodicsForTest;

public class YesOrNoFrameController {

    MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    private Methodic methodic;

    private boolean deliteMethodic;

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
        //если вызвано с окна удаления методики "Удалить методику"
        if (event.getSource() == yesBtn) {
            if (deliteMethodic) {

                methodicsForTest.deleteMethodic(methodic.getMethodicName());
                methodicsForTest.getMethodics().size();

                methodicsAddEditDeleteFrameController.refreshAfterDelete();

                Stage stage = (Stage) questionTxt.getScene().getWindow();
                stage.close();

                deliteMethodic = false;
            }
        }

        if (event.getSource() == noBtn) {
            Stage stage = (Stage) questionTxt.getScene().getWindow();
            stage.close();
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
}
