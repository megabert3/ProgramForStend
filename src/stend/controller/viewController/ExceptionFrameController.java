package stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ExceptionFrameController {

    @FXML
    private Label exceptionLabel;

    @FXML
    private Button exitBtn;

    @FXML
    void exitAction(ActionEvent event) {
        if (event.getSource() == exitBtn) {
            Stage stage = (Stage) exceptionLabel.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void initialize() {

    }

    public Label getExceptionLabel() {
        return exceptionLabel;
    }
}
