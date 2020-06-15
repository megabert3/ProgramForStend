package org.taipit.stend.controller.viewController;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.taipit.stend.model.stend.OnePhaseStend;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.Frame;
import org.taipit.stend.model.MeterParamepersForProperty;
import org.taipit.stend.model.MeterParameter;

import java.net.URL;
import java.util.*;

public class PropertiesController implements Initializable, Frame {

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
    private TextField txtStendModel;

    @FXML
    private TextField txtFidRefMetModel;

    @FXML
    private TextField txtFldCOMList;

    @FXML
    private TextField txtFldCertificate;

    @FXML
    private TextField txtFldDateLastVerification;

    @FXML
    private TextField txtFldDateNextVerification;

//----------------------------------------------------------- passwordPane
    @FXML
    private AnchorPane passwordPane;

//----------------------------------------------------------- ParametersPane
    @FXML
    private AnchorPane parametersPane;

    @FXML
    private TableView<MeterParameter> tabViewParameters;

    @FXML
    private TableColumn<MeterParameter, String> tabColParameters;

    @FXML
    private ListView<String> listViewParameters;

    @FXML
    private TextField txtFldParameters;

    @FXML
    private Button btnDeleteParameter;

    @FXML
    private Button btnAddParameter;

    private Properties properties = ConsoleHelper.properties;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setParamWithPropFile();
        initMeterParameterPane();
    }

    @FXML
    private void stendPaneActionEvent(ActionEvent event) {
        if (event.getSource() == stendPaneSave) {
            if (stendPaneStendTypeList.getValue().equals("Однофазная")) {
                properties.setProperty("stendType", "OnePhaseStend");
            } else properties.setProperty("stendType", "ThreePhaseStend");

            properties.setProperty("stendModel", txtStendModel.getText().trim());
            properties.setProperty("refMeterModel", txtFidRefMetModel.getText().trim());
            properties.setProperty("stendAmountPlaces", standPaneAmoutPlase.getText().trim());
            properties.setProperty("stendSerNo",standPaneStandSerNo.getText().trim());
            properties.setProperty("refMeterSerNo", StendPaneRefSerNo.getText().trim());
            properties.setProperty("stendAccuracyClass", stendPaneStandAcrCl.getText().trim());
            properties.setProperty("refMeterAccuracyClass", stendPaneRefAcrCl.getText().trim());
            properties.setProperty("stendCOMPort", txtFldCOMList.getText().trim());
            properties.setProperty("param.certificate", txtFldCertificate.getText().trim());
            properties.setProperty("param.dateLastVerification", txtFldDateLastVerification.getText());
            properties.setProperty("param.dateNextVerification", txtFldDateNextVerification.getText());
            ConsoleHelper.saveProperties();
        }
    }
    @FXML
    private void parameterPaneActionEvent(ActionEvent event) {
        if (event.getSource() == btnAddParameter) {

            if (txtFldParameters.getText().isEmpty()) {
                System.out.println("Введите название параметра, который хотите добавить.");
            } else {
                MeterParameter meterParameter = tabViewParameters.getSelectionModel().getSelectedItem();
                String parameter = ConsoleHelper.properties.getProperty(meterParameter.getId());
                ConsoleHelper.properties.setProperty(meterParameter.getId(), parameter + ", " + txtFldParameters.getText());
                meterParameter.setParameterValues(new ArrayList<>(Arrays.asList(ConsoleHelper.properties.getProperty(meterParameter.getId()).split(", "))));
                listViewParameters.setItems(FXCollections.observableArrayList(meterParameter.getParameterValues()));
                txtFldParameters.clear();
                ConsoleHelper.saveProperties();
            }
        }

        if (event.getSource() == btnDeleteParameter) {
            MeterParameter meterParameter = tabViewParameters.getSelectionModel().getSelectedItem();
            String deleteItem = listViewParameters.getSelectionModel().getSelectedItems().get(0);
            List<String> listParam = new ArrayList<>(meterParameter.getParameterValues());
            listParam.remove(deleteItem);
            String newItems = listParam.toString();
            ConsoleHelper.properties.setProperty(meterParameter.getId(), newItems.substring(1, newItems.length() - 1));
            meterParameter.setParameterValues(listParam);
            listViewParameters.setItems(FXCollections.observableArrayList(meterParameter.getParameterValues()));
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
            parametersPane.toFront();
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
        txtFldCertificate.setText(properties.getProperty("param.certificate"));
        txtFldDateLastVerification.setText(properties.getProperty("param.dateLastVerification"));
        txtFldDateNextVerification.setText(properties.getProperty("param.dateNextVerification"));

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

    private void initMeterParameterPane() {
        MeterParamepersForProperty meterParamepersForProperty = MeterParamepersForProperty.getInstance();

        tabColParameters.setCellValueFactory(new PropertyValueFactory<>("nameParam"));

        tabViewParameters.setItems(FXCollections.observableArrayList(meterParamepersForProperty.getMeterParameterList()));

        tabViewParameters.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<MeterParameter>() {
            @Override
            public void onChanged(Change<? extends MeterParameter> c) {
                listViewParameters.setItems(FXCollections.observableArrayList(c.getList().get(0).getParameterValues()));
            }
        });
    }

    @Override
    public Stage getStage() {
        return (Stage) stendBtn.getScene().getWindow();
    }
}