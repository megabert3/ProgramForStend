package stend.controller.viewController;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.helper.ConsoleHelper;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {

//----------------------------------------------------------- Menu
    @FXML
    private Button passwordBtn;

    @FXML
    private Button stendBtn;

    @FXML
    private Button demoBtn;

//----------------------------------------------------------- stendPane
    @FXML
    private AnchorPane stendPane;

    @FXML
    private ComboBox<String> standPaneCOMList;

    @FXML
    private ComboBox<String> stendPaneStendTypeList;

    @FXML
    private ComboBox<String> stendPaneStendModel;

    @FXML
    private ComboBox<String> stendPaneRefMetModel;

    @FXML
    private TextField standPaneAmoutPlase;

    @FXML
    private TextField StendPaneRefSerNo;

    @FXML
    private TextField standPaneStandSerNo;

    @FXML
    private TextField stendPaneRefAcrCl;

    @FXML
    private TextField stendPaneStandAcrCl;

    @FXML
    private Button stendPaneSave;

    @FXML
    private TextField txtFieldStendTypeList;

    @FXML
    private TextField txtStendModel;

    @FXML
    private TextField txtFidRefMetModel;

    @FXML
    private TextField txtFldCOMList;

//----------------------------------------------------------- passwordPane
    @FXML
    private AnchorPane passwordPane;

//----------------------------------------------------------- demoPane
    @FXML
    private AnchorPane demoPane;

    private Properties properties = ConsoleHelper.properties;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setParamWithPropFile();

    }

    @FXML
    private void stendPaneActionEvent(ActionEvent event) {
        if (event.getSource() == stendPaneSave) {
            if (stendPaneStendTypeList.getValue().equals("Однофазная")) {
                properties.setProperty("stendType", "OnePhaseStend");
            } else properties.setProperty("stendType", "ThreePhaseStend");

            properties.setProperty("stendModel", txtStendModel.getText());
            properties.setProperty("refMeterModel", txtFidRefMetModel.getText());
            properties.setProperty("stendAmountPlaces", standPaneAmoutPlase.getText());
            properties.setProperty("stendSerNo",standPaneStandSerNo.getText());
            properties.setProperty("refMeterSerNo", StendPaneRefSerNo.getText());
            properties.setProperty("stendAccuracyClass", stendPaneStandAcrCl.getText());
            properties.setProperty("refMeterAccuracyClass", stendPaneRefAcrCl.getText());
            properties.setProperty("stendCOMPort", txtFldCOMList.getText());
            ConsoleHelper.saveProperties();
        }
    }

    @FXML
    void handleClicks(ActionEvent event) {

        //Переключение на вкладку Пароль
        if (event.getSource() == passwordBtn) {
            passwordPane.toFront();
        }

        //Переключение на вкладку Установка
        if (event.getSource() == stendBtn) {
            stendPane.toFront();
        }

        //Переключение на вкладку Демо
        if (event.getSource() == demoBtn) {
            demoPane.toFront();
        }
    }

    private void setParamWithPropFile() {
        //Заполнение полей напрямую из файла
        StendPaneRefSerNo.setText(properties.getProperty("refMeterSerNo"));
        standPaneStandSerNo.setText(properties.getProperty("stendSerNo"));
        stendPaneRefAcrCl.setText(properties.getProperty("refMeterAccuracyClass"));
        stendPaneStandAcrCl.setText(properties.getProperty("stendAccuracyClass"));
        txtFldCOMList.setText(properties.getProperty("stendCOMPort"));
        standPaneAmoutPlase.setText(properties.getProperty("stendAmountPlaces"));

        stendPaneStendTypeList.getItems().addAll("Однофазная", "Трехфазная");

        //Иициализация комбо боксов
        if (properties.getProperty("stendType").equals("OnePhaseStend")) {

            stendPaneStendTypeList.getSelectionModel().select(0);

            stendPaneStendModel.getItems().addAll(OnePhaseStend.stendModelList);
            stendPaneStendModel.getSelectionModel().select(properties.getProperty("stendModel"));


            stendPaneRefMetModel.getItems().addAll(OnePhaseStend.refMetModelList);
            stendPaneRefMetModel.getSelectionModel().select(properties.getProperty("refMeterModel"));

        } else if (properties.getProperty("stendType").equals("ThreePhaseStend")){

            stendPaneStendTypeList.getSelectionModel().select(1);

            stendPaneStendModel.getItems().addAll(ThreePhaseStend.stendModelList);
            stendPaneStendModel.getSelectionModel().select(properties.getProperty("stendModel"));

            stendPaneRefMetModel.getItems().addAll(ThreePhaseStend.refMetModelList);
            stendPaneRefMetModel.getSelectionModel().select(properties.getProperty("refMeterModel"));
        }

        txtStendModel.setText(stendPaneStendModel.getValue());
        txtFidRefMetModel.setText(stendPaneRefMetModel.getValue());

        //Список ком портов
        standPaneCOMList.getItems().addAll(FXCollections.observableArrayList(StendDLLCommands.massPort()));

        //Установка слушателей
        standPaneCOMList.getSelectionModel().selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    txtFldCOMList.setText(newValue);
                });

        //В зависимости от типа стенда предлагает возможные варианты эталонного счётчика и установки
        stendPaneStendTypeList.getSelectionModel().selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.equals("Однофазная")) {
                        stendPaneStendModel.getItems().clear();
                        stendPaneRefMetModel.getItems().clear();
                        stendPaneStendModel.getItems().addAll(OnePhaseStend.stendModelList);
                        stendPaneStendModel.getSelectionModel().select(0);
                        stendPaneRefMetModel.getItems().addAll(OnePhaseStend.refMetModelList);
                        stendPaneRefMetModel.getSelectionModel().select(0);
                    }

                    if (newValue.equals("Трехфазная")) {
                        stendPaneStendModel.getItems().clear();
                        stendPaneRefMetModel.getItems().clear();
                        stendPaneStendModel.getItems().addAll(ThreePhaseStend.stendModelList);
                        stendPaneStendModel.getSelectionModel().select(0);
                        stendPaneRefMetModel.getItems().addAll(ThreePhaseStend.refMetModelList);
                        stendPaneRefMetModel.getSelectionModel().select(0);
                    }
                } );

        //Добавление слушателя для комбо бокса "модель установки"
        stendPaneStendModel.getSelectionModel().selectedItemProperty()
                .addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    txtStendModel.setText(newValue);
                });

        //Добавление слушателя для комбо бокса "модель образцового счётчика"
        stendPaneRefMetModel.getSelectionModel().selectedItemProperty()
                .addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    txtFidRefMetModel.setText(newValue);

                });
    }
}