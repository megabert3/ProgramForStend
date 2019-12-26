package stend.controller.viewController.methodicsFrameController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import stend.helper.exeptions.InfoExeption;
import stend.model.Methodic;
import stend.model.MethodicsForTest;

public class methodicNameController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nameField;

    @FXML
    private Button acceptNameBtn;

    @FXML
    void initialize() {

    }

    @FXML
    void actinonForNameFrame(ActionEvent event) {
        if (event.getSource() == acceptNameBtn) {
            try {
                if (MethodicsForTest.getMethodicsForTestInstance().addMethodicListToMap(nameField.getText(), new Methodic(nameField.getText())))
                loadStage("/stend/view/method/metodicName.fxml", "Добавление методики");
            } catch (InfoExeption infoExeption) {
                loadStageExeptionStage("dfsdfs");
            }
        }
    }

    private void loadStageExeptionStage(String error) {
        loadStage("/stend/view/exceptionName.fxml", "Ошибка");
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