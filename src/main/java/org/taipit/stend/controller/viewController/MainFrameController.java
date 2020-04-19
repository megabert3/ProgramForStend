package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;

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
            loadStage("/viewFXML/properties.fxml", "Настройки");
        }

        if(event.getSource() == mainFrameMethodicsBtn) {
            //loadStage("/viewFXML/methodics/methodicsAddEditDeleteFrame.fxml", "Методики");
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/methodicsAddEditDeleteFrame.fxml"));
                fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Методики");
                stage.setScene(new Scene(fxmlLoader.getRoot()));

                MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController = fxmlLoader.getController();

                stage.show();

                methodicsAddEditDeleteFrameController.addListenerForResizeFrame();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (event.getSource() == mainFrameParamTestBtn) {
            loadStage("/viewFXML/testParametersFrame.fxml", "Параметры теста");
        }

        if (event.getSource() == mainFrameResultsBtn) {
            loadStage("/viewFXML/resultsMetersFrame.fxml", "Результаты");
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
