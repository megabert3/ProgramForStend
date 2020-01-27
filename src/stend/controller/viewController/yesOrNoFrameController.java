package stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import stend.model.Methodic;

public class yesOrNoFrameController {

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
        if (deliteMethodic) {
            questionTxt.setText("Вы действительно жедаете удалить методику:\n" +
                    methodic.getMethodicName() + "?");
            if (event.getSource() == yesBtn) {

            } else if (event){
                Stage stage = (Stage) questionTxt.getScene().getWindow();
                stage.close();
            }
        }

    }

    public boolean isDeliteMethodic() {
        return deliteMethodic;
    }

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
    }
}
