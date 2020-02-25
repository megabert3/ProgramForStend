package org.taipit.stend.controller.viewController.methodicsFrameController;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class MethodicsFrameController {

    private Map<String, CheckBox> checkBoxMap = new HashMap<>();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addMetBtn;

    @FXML
    private TableView<?> viewPointTable;

    @FXML
    private GridPane setPointGird;

    @FXML
    private Button saveMetBtn;

    @FXML
    private Button addMetBtn2;

    @FXML
    private Button deleteMetBtn;

    @FXML
    void initialize() {

    }

    @FXML
    void actinonForMethodicsFrame(ActionEvent event) {
        if (event.getSource() == addMetBtn) {
            loadStage("/org/taipit/stend/view/method/addEditMet.fxml", "Добавление методики");
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