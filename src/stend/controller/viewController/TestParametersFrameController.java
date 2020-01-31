package stend.controller.viewController;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.helper.ConsoleHelper;
import stend.model.Methodic;
import stend.model.MethodicsForTest;

public class TestParametersFrameController {

    private MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private StendDLLCommands stendDLLCommands;

    private Properties properties = ConsoleHelper.properties;

    @FXML
    private ComboBox<String> chosBxMetodics;

    @FXML
    private Button btnNumbersMe;

    @FXML
    private TextField txtFldOperator;

    @FXML
    private TextField txtFldController;

    @FXML
    private TextField txtFldWitness;

    @FXML
    private TextField txtFldUnom;

    @FXML
    private TextField txtFldAccuracyAP;

    @FXML
    private TextField txtFldCurrent;

    @FXML
    private TextField txtFldFrg;

    @FXML
    private TextField txtFldAccuracyRP;

    @FXML
    private Button btnStartTest;

    @FXML
    private ComboBox<String> chosBxAccuracyAP;

    @FXML
    private ComboBox<String> chosBxUnom;

    @FXML
    private ComboBox<String> chosBxAccuracyRP;

    @FXML
    private ComboBox<String> chosBxCurrent;

    @FXML
    private ComboBox<String> chosBxTypeMeter;

    @FXML
    private ComboBox<String> chosBxPowerType;

    @FXML
    private ComboBox<String> chosBxFrg;

    @FXML
    private ComboBox<String> chosBxOperator;

    @FXML
    private ComboBox<String> chosBxController;

    @FXML
    private ComboBox<String> chosBxWitness;


    @FXML
    void buttonActionTestFrame(ActionEvent event) {

    }

    @FXML
    void initialize() {

        /**
         * Продолжи инициировать таблицу настроек для испытания счётчиков
         * Количество посадочных мест
         * И новые объекты счётчиков
         */

        if (properties.getProperty("stendType").equals("ThreePhaseStend")) {
            stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
        } else stendDLLCommands = OnePhaseStend.getOnePhaseStendInstance();

        initComboBoxesAndAddListerners();
    }

    //Инициирует комбо боксы и добавляет им слушателей
    private void initComboBoxesAndAddListerners() {
        //Переносим данные из проперти файла
        String[] UnomComBox = properties.getProperty("Unom").split(", ");
        String[] FnomComBox = properties.getProperty("Fnom").split(", ");
        String[] InomAndImaxComBox = properties.getProperty("InomAndImax").split(", ");
        String[] accuracyClassMeterAPComBox = properties.getProperty("accuracyClassMeterAP").split(", ");
        String[] accuracyClassMeterRPComBox = properties.getProperty("accuracyClassMeterRP").split(", ");
        String[] typeCircuitComBox = properties.getProperty("typeCircuit").split(", ");
        String[] typeOfMeasuringElementComBox = properties.getProperty("typeOfMeasuringElement").split(", ");
        String[] operatorsComBox = properties.getProperty("Operators").split(", ");
        String[] WitnessComBox = properties.getProperty("Witness").split(", ");
        String[] controllerComBox = properties.getProperty("Controller").split(", ");

        //Кладём элементы в КомбоБокс
        chosBxUnom.getItems().addAll(UnomComBox);
        chosBxFrg.getItems().addAll(FnomComBox);
        chosBxCurrent.getItems().addAll(InomAndImaxComBox);
        chosBxAccuracyAP.getItems().addAll(accuracyClassMeterAPComBox);
        chosBxAccuracyRP.getItems().addAll(accuracyClassMeterRPComBox);
        chosBxPowerType.getItems().addAll(typeCircuitComBox);
        chosBxTypeMeter.getItems().addAll(typeOfMeasuringElementComBox);
        chosBxOperator.getItems().addAll(operatorsComBox);
        chosBxWitness.getItems().addAll(WitnessComBox);
        chosBxController.getItems().addAll(controllerComBox);

        for (Methodic methodic : methodicsForTest.getMethodics()) {
            chosBxMetodics.getItems().addAll(methodic.getMethodicName());
        }

        //Задаём текстовым полям последнее сохранённое значение
        txtFldUnom.setText(properties.getProperty("lastUnom"));
        txtFldFrg.setText(properties.getProperty("lastFnom"));
        txtFldCurrent.setText(properties.getProperty("lastInomAndImax"));
        txtFldAccuracyAP.setText(properties.getProperty("lastAccuracyClassMeterAP"));
        txtFldAccuracyRP.setText(properties.getProperty("lastAccuracyClassMeterRP"));
        chosBxPowerType.setValue(properties.getProperty("lastTypeCircuit"));
        chosBxTypeMeter.setValue(properties.getProperty("lastTypeOfMeasuringElement"));
        txtFldOperator.setText(properties.getProperty("lastOperator"));
        txtFldController.setText(properties.getProperty("lastController"));
        txtFldWitness.setText(properties.getProperty("lastWitness"));

        //Устанавливаем слушателей
        chosBxUnom.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
           txtFldUnom.setText(newValue);
        });
        chosBxFrg.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldFrg.setText(newValue);
        });
        chosBxCurrent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldCurrent.setText(newValue);
        });
        chosBxAccuracyAP.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldAccuracyAP.setText(newValue);
        });
        chosBxAccuracyRP.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldAccuracyRP.setText(newValue);
        });
        chosBxOperator.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldOperator.setText(newValue);
        });
        chosBxController.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldController.setText(newValue);
        });
        chosBxWitness.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldWitness.setText(newValue);
        });
    }
}
