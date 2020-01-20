package stend.controller.viewController.methodicsFrameController;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;


public class MethodicsFrameController {

    private Map<String, CheckBox> checkBoxMap = new HashMap<>();

    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");
    private List<String> current = Arrays.asList("Imax", "0.5Imax", "0.2Imax", "0.5Ib", "Ib","0.2Ib", "0.1Ib", "0.05Ib");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button copyMetBtn;

    @FXML
    private Button deleteMetBtn;

    @FXML
    private Button addMetBtn;

    @FXML
    private ToggleButton tglBtnAPPls;

    @FXML
    private ToggleButton tglBtnAPMns;

    @FXML
    private ToggleButton tglBtnRPPls;

    @FXML
    private ToggleButton tglBtnRPMns;

    @FXML
    private Button BtnGoToStartTest;

    @FXML
    private TableView<?> viewPointTable;

    @FXML
    private TableColumn<?, ?> tabClMethodics;

    @FXML
    private ListView<?> ListViewAPMns;

    @FXML
    private ListView<?> ListViewAPPls;

    @FXML
    private ListView<?> ListViewRPPls;

    @FXML
    private ListView<?> ListViewAPRPMns;

    @FXML
    private Button editMetBtn;

    @FXML
    void tglBtnsEnegyViewAction(ActionEvent event) {
    }

    @FXML
    void initialize() {
    }

    @FXML
    void actinonForMethodicsFrame(ActionEvent event) {
        if (event.getSource() == addMetBtn) {
            loadStage("/stend/view/method/addEditMet.fxml", "Добавление методики");
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