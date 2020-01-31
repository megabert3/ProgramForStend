package stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class MainFrameController {

    @FXML
    private Button mainFramePropertiesBtn;

    @FXML
    private Button mainFrameMethodicsBtn;

    @FXML
    private Button mainFrameParamTestBtn;

    @FXML
    private Button mainFrameResultsBtn;

    @FXML
    void mainFrameHandleClicks(ActionEvent event) {
        if (event.getSource() == mainFramePropertiesBtn) {
            loadStage("/stend/view/properties.fxml", "Настройки");
        }

        if(event.getSource() == mainFrameMethodicsBtn) {
            loadStage("/stend/view/method/methodicsAddEditDeleteFrame.fxml", "Методики");
        }

        if (event.getSource() == mainFrameParamTestBtn) {
            loadStage("/stend/view/testParametersFrame.fxml", "Параметры теста");
        }
    }

    private void loadStage(String fxml, String stageName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(stageName);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
