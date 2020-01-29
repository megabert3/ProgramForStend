package stend.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.helper.ConsoleHelper;
import stend.model.MethodicsForTest;

public class TestParametersFrameController {

    MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    StendDLLCommands stendDLLCommands;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<?> chosBx;

    @FXML
    private Button btnNumbersMe;

    @FXML
    private TextField txtFldOperator;

    @FXML
    private TextField txtFldController;

    @FXML
    private TextField txtFldWitness;

    @FXML
    private Button btnStartTest;

    @FXML
    private ComboBox<?> chosBxUnom;

    @FXML
    private TextField txtFldUnom;

    @FXML
    private ComboBox<?> chosBxAccuracyAP;

    @FXML
    private TextField txtFldAccuracyAP;

    @FXML
    private ComboBox<?> chosBxAccuracyRP;

    @FXML
    private ComboBox<?> chosBxCurrent;

    @FXML
    private ComboBox<?> chosBxTypeMeter;

    @FXML
    private ComboBox<?> chosBxPowerType;

    @FXML
    private ComboBox<?> chosBxFrg;

    @FXML
    private TextField txtFldAccuracyAR;

    @FXML
    private TextField txtFldTypeMeter;

    @FXML
    private TextField txtFldCurrent;

    @FXML
    private TextField txtFldPowerType;

    @FXML
    private TextField txtFldFrg;

    @FXML
    void buttonActionTestFrame(ActionEvent event) {

    }

    @FXML
    void initialize() {
        if (ConsoleHelper.properties.getProperty("stendType").equals("ThreePhaseStend")) {
            stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
        } else stendDLLCommands = OnePhaseStend.getOnePhaseStendInstance();

        
    }
}
