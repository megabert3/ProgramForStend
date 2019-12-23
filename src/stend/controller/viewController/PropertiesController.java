package stend.controller.viewController;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.helper.ConsoleHelper;


import java.io.*;
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
    private ChoiceBox<String> standPaneCOMList;

    @FXML
    private ChoiceBox<String> stendPaneStendTypeList;

    @FXML
    private ChoiceBox<String> stendPaneStendModel;

    @FXML
    private TextField standPaneAmoutPlase;

    @FXML
    private ChoiceBox<String> stendPaneRefMetModel;

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

//----------------------------------------------------------- passwordPane
    @FXML
    private AnchorPane passwordPane;

//----------------------------------------------------------- demoPane
    @FXML
    private AnchorPane demoPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StendPaneRefSerNo.setText(ConsoleHelper.properties.getProperty("refMeterSerNo"));
        standPaneStandSerNo.setText(ConsoleHelper.properties.getProperty("stendSerNo"));
        stendPaneRefAcrCl.setText(ConsoleHelper.properties.getProperty("refMeterAccuracyClass"));
        stendPaneStandAcrCl.setText(ConsoleHelper.properties.getProperty("stendAccuracyClass"));

        //Тип установки (одно/трех фазная)
        if (ConsoleHelper.properties.getProperty("stendType").equals("OnePhaseStend")) {
            stendPaneStendTypeList.getItems().addAll("Однофазная", "Трехфазная");
            stendPaneStendTypeList.getSelectionModel().select(0);

            stendPaneStendModel.getItems().addAll(OnePhaseStend.stendModelList);
            stendPaneStendModel.getSelectionModel().select(ConsoleHelper.properties.getProperty("stendModel"));

            stendPaneRefMetModel.getItems().addAll(OnePhaseStend.refMetModelList);
            stendPaneRefMetModel.getSelectionModel().select(ConsoleHelper.properties.getProperty("refMeterModel"));
        }

        //Список ком портов
        standPaneCOMList.getItems().addAll(FXCollections.observableArrayList(StendDLLCommands.massPort()));

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
    }


    public void handleClicks(javafx.event.ActionEvent actionEvent) {
        //Переключение на вкладку Пароль
        if (actionEvent.getSource() == passwordBtn) {
            passwordPane.toFront();
        }

        //Переключение на вкладку Установка
        if (actionEvent.getSource() == stendBtn) {
            stendPane.toFront();
        }

        //Переключение на вкладку Демо
        if (actionEvent.getSource() == demoBtn) {
            demoPane.toFront();
        }
        if (actionEvent.getSource() == stendPaneSave) {
//            Properties properties = getProperties();
//
//            properties.setProperty("stendType", stendPaneStendTypeList.getValue());
//            properties.setProperty("stendModel", stendPaneStendModel.getValue());
//            properties.setProperty("stendAmountPlaces", standPaneAmoutPlase.getText());
//            properties.setProperty("stendSerNo", stendPaneStendSerNo.getText());
//            properties.setProperty("stendAccuracyClass", stendPaneStandAcrCl.getText());
//            properties.setProperty("refMeterModel", stendPaneRefMetModel.getValue());
//            properties.setProperty("refMeterSerNo", stendPaneRefSerNo.getText());
//            properties.setProperty("refMeterAccuracyClass", stendPaneRefAcrCl.getText());
//            properties.setProperty("stendCOMPort", standPaneCOMList.getValue());
        }
    }

    private void loadProperties() {
//        Properties load = getProperties();
//        stendPaneStendTypeList.setValue(load.getProperty("stendType"));
//        stendPaneStendModel.setValue(load.getProperty("stendModel"));
//        standPaneAmoutPlase.setText(load.getProperty("stendAmountPlaces"));
//        stendPaneStendSerNo.setText(load.getProperty("stendSerNo"));
////        stendPaneStandAcrCl.setText(load.getProperty("stendAccuracyClass"));
//        stendPaneRefMetModel.setValue(load.getProperty("refMeterModel"));
////        stendPaneRefSerNo.setText(load.getProperty("refMeterSerNo"));
//        stendPaneRefAcrCl.setText(load.getProperty("refMeterAccuracyClass"));
//        standPaneCOMList.setValue(load.getProperty("stendCOMPort"));
    }

}