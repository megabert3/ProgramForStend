package org.taipit.stend.controller.viewController;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.taipit.stend.model.stend.OnePhaseStend;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.Frame;
import org.taipit.stend.model.MeterParamepersForProperty;
import org.taipit.stend.model.MeterParameter;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class PropertiesController implements Initializable, Frame {

    @FXML
    private StackPane mainStackPane;

    @FXML
    private VBox vBoxBtn;

    private ToggleGroup toggleGroup = new ToggleGroup();

//----------------------------------------------------------- Menu
    @FXML
    private ToggleButton passwordBtn;

    @FXML
    private ToggleButton stendBtn;

    @FXML
    private ToggleButton meterParanBtn;

    @FXML
    private ToggleButton linksBtn;

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

    @FXML
    private Button passPaneSave;

    @FXML
    private TextField passFldOldPass;

    @FXML
    private TextField passFldNewPass;

    @FXML
    private TextField passFldRepeatNewPass;

//----------------------------------------------------------- ParametersPane
    @FXML
    private AnchorPane parametersPane;

    @FXML
    private TableView<MeterParameter> tabViewParameters;

    @FXML
    private TableColumn<MeterParameter, String> tabColParameters = new TableColumn<>("Параметры");

    @FXML
    private ListView<String> listViewParameters;

    @FXML
    private TextField txtFldParameters;

    @FXML
    private Button btnDeleteParameter;

    @FXML
    private Button btnAddParameter;

//----------------------------------------------------------- linksPane
    @FXML
    private AnchorPane linksPane;

    @FXML
    private TextField txtFldPathSerNoMeter;

    @FXML
    private TextField txtFldPathReport;

    @FXML
    private Button btnPathSerNoMeter;

    @FXML
    private Button btnPathReport;

//----------------------------------------------------------- reportPane
    @FXML
    private ToggleButton reportBtn;

    @FXML
    private AnchorPane reportPane;

    @FXML
    private ComboBox<String> cmbBxCreep;

    @FXML
    private ComboBox<String> cmbBxRTC;

    @FXML
    private ComboBox<String> cmbBxSTA_APPls;

    @FXML
    private ComboBox<String> cmbBxSTA_APMns;

    @FXML
    private ComboBox<String> cmbBxSTA_RPPls;

    @FXML
    private ComboBox<String> cmbBxSTA_RPMns;

    @FXML
    private ComboBox<String> cmbBxConst_APPls;

    @FXML
    private ComboBox<String> cmbBxConst_APMns;

    @FXML
    private ComboBox<String> cmbBxConst_RPPls;

    @FXML
    private ComboBox<String> cmbBxConst_RPMns;

    @FXML
    private ComboBox<String> cmbBxRelay;

    @FXML
    private ComboBox<String> cmbBxAppearance;

    @FXML
    private ComboBox<String> cmbBxInsulation;

    @FXML
    private RadioButton radBtnFormat97XLS;

    @FXML
    private RadioButton radBtnFormat2007XLSX;

    @FXML
    private Button reportSave;


    private Properties properties = ConsoleHelper.properties;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stendBtn.setToggleGroup(toggleGroup);
        meterParanBtn.setToggleGroup(toggleGroup);
        passwordBtn.setToggleGroup(toggleGroup);
        linksBtn.setToggleGroup(toggleGroup);
        reportBtn.setToggleGroup(toggleGroup);

        setParamWithPropFile();
        initMeterParameterPane();
        initLinksPane();
        initReporPane();
        stendBtn.fire();
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
            ConsoleHelper.infoException("Информация", "Параметры успешно сохранены");
        }
    }

    @FXML
    private void passPaneActionEvent(ActionEvent event) {

        if (event.getSource() == passPaneSave) {
            passFldNewPass.setStyle("");
            passFldRepeatNewPass.setStyle("");
            passFldOldPass.setStyle("");

            if (properties.getProperty("config").isEmpty()) {

                if (passFldNewPass.getText().equals(passFldRepeatNewPass.getText())) {
                    try {
                        MessageDigest digester = MessageDigest.getInstance("SHA-512");

                        //Генерирую хэш из пароля
                        String digest = DatatypeConverter.printHexBinary(digester.digest(passFldNewPass.getText().getBytes()));

                        //Сохраняю его в пропертиес
                        properties.setProperty("config", digest);
                        ConsoleHelper.saveProperties();
                        ConsoleHelper.infoException("Пароль успешно установлен");
                    } catch (NoSuchAlgorithmException ignore) {
                    }
                } else {
                    ConsoleHelper.infoException("Пароли не совпадают");
                    passFldNewPass.setStyle("-fx-border-color: red; -fx-focus-color: red;");
                    passFldRepeatNewPass.setStyle("-fx-border-color: red; -fx-focus-color: red;");
                }
            } else {
                try {
                    MessageDigest digester = MessageDigest.getInstance("SHA-512");

                    //Генерирую хэш из пароля
                    String digest = DatatypeConverter.printHexBinary(digester.digest(passFldOldPass.getText().getBytes()));

                    if (digest.equals(properties.getProperty("config"))) {
                        if (passFldNewPass.getText().equals(passFldRepeatNewPass.getText())) {

                            //Генерирую хэш из пароля
                            String digest1 = DatatypeConverter.printHexBinary(digester.digest(passFldNewPass.getText().getBytes()));

                            //Сохраняю его в пропертиес
                            properties.setProperty("config", digest1);
                            ConsoleHelper.saveProperties();
                            ConsoleHelper.infoException("Пароль успешно установлен");

                        } else {
                            ConsoleHelper.infoException("Пароли не совпадают");
                            passFldNewPass.setStyle("-fx-border-color: red; -fx-focus-color: red ;");
                            passFldRepeatNewPass.setStyle("-fx-border-color: red; -fx-focus-color: red ;");
                        }
                    } else {
                        ConsoleHelper.infoException("Неверный пароль");
                        passFldOldPass.setStyle("-fx-border-color: red; -fx-focus-color: red ;");
                    }
                } catch (NoSuchAlgorithmException ignore) {}
            }
        }
    }

    @FXML
    private void reportPaneActionEvent(ActionEvent event) {
        if (event.getSource() == reportSave) {
            saveAcrionInReportPane();
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
        //Переключение на вкладку Установка
        if (event.getSource() == stendBtn) {
            if (stendBtn.isSelected()) {
                stendPane.toFront();
            }
        }

        //Переключение на вкладку Параметры счётчиков
        if (event.getSource() == meterParanBtn) {
            if (meterParanBtn.isSelected()) {
                parametersPane.toFront();
            }
        }

        //Переключение на вкладку Пароль
        if (event.getSource() == passwordBtn) {
            if (passwordBtn.isSelected()) {
                passwordPane.toFront();
            }
        }

        //Переключение на вкладку Ссылки
        if (event.getSource() == linksBtn) {
            if (linksBtn.isSelected()) {
                linksPane.toFront();
            }
        }

        if (event.getSource() == reportBtn) {
            if (reportBtn.isSelected()) {
                reportPane.toFront();
            }
        }
    }

    @FXML
    private void linksPaneActionEvent(ActionEvent event) {
        if (event.getSource() == btnPathReport) {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Путь сохранения отчёта");

            File newFile = directoryChooser.showDialog(btnPathReport.getScene().getWindow());

            if (newFile != null) {
                properties.setProperty("printReportPath", newFile.getAbsolutePath());
                ConsoleHelper.saveProperties();
                txtFldPathReport.setText(newFile.getAbsolutePath());
            } else {
                txtFldPathReport.setText(properties.getProperty("printReportPath"));
            }
        }

        if (event.getSource() == btnPathSerNoMeter) {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Выбор файла");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Все файлы", "*.*"),
                    new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt", "*.doc")
            );

            File file = fileChooser.showOpenDialog(btnPathSerNoMeter.getScene().getWindow());

            if (file != null) {
                properties.setProperty("testParamFrame.fileForSerNo", file.getAbsolutePath());
                ConsoleHelper.saveProperties();

                txtFldPathSerNoMeter.setText(file.getAbsolutePath());
            } else {
                txtFldPathSerNoMeter.setText(properties.getProperty("testParamFrame.fileForSerNo"));
            }
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

        tabColParameters.setPrefWidth(tabViewParameters.getPrefWidth() - 2);

        tabViewParameters.getColumns().add(tabColParameters);
    }

    private void initLinksPane() {
        txtFldPathSerNoMeter.setText(properties.getProperty("testParamFrame.fileForSerNo"));
        txtFldPathReport.setText(properties.getProperty("printReportPath"));
    }

    private void initReporPane() {
        String[] info = {"НЕ ПРОВОДИЛОСЬ", "ГОДЕН", "ПРОВАЛИЛ"};
        String result;

        cmbBxRTC.getItems().addAll(info);
        cmbBxSTA_APPls.getItems().addAll(info);
        cmbBxSTA_APMns.getItems().addAll(info);
        cmbBxSTA_RPPls.getItems().addAll(info);
        cmbBxSTA_RPMns.getItems().addAll(info);
        cmbBxConst_APPls.getItems().addAll(info);
        cmbBxConst_APMns.getItems().addAll(info);
        cmbBxConst_RPPls.getItems().addAll(info);
        cmbBxConst_RPMns.getItems().addAll(info);
        cmbBxRelay.getItems().addAll(info);
        cmbBxAppearance.getItems().addAll(info);
        cmbBxInsulation.getItems().addAll(info);
        cmbBxCreep.getItems().addAll(info);

        ToggleGroup tg = new ToggleGroup();

        radBtnFormat97XLS.setToggleGroup(tg);
        radBtnFormat2007XLSX.setToggleGroup(tg);

        result = properties.getProperty("propertiesController.reportPane.cmbBxCreep");
        if (result.equals("N")) {
            cmbBxCreep.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxCreep.setValue(info[1]);
        } else {
            cmbBxCreep.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxRTC");
        if (result.equals("N")) {
            cmbBxRTC.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxRTC.setValue(info[1]);
        } else {
            cmbBxRTC.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxSTA_APPls");
        if (result.equals("N")) {
            cmbBxSTA_APPls.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxSTA_APPls.setValue(info[1]);
        } else {
            cmbBxSTA_APPls.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxSTA_APMns");
        if (result.equals("N")) {
            cmbBxSTA_APMns.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxSTA_APMns.setValue(info[1]);
        } else {
            cmbBxSTA_APMns.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxSTA_RPPls");
        if (result.equals("N")) {
            cmbBxSTA_RPPls.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxSTA_RPPls.setValue(info[1]);
        } else {
            cmbBxSTA_RPPls.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxSTA_RPMns");
        if (result.equals("N")) {
            cmbBxSTA_RPMns.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxSTA_RPMns.setValue(info[1]);
        } else {
            cmbBxSTA_RPMns.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxConst_APPls");
        if (result.equals("N")) {
            cmbBxConst_APPls.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxConst_APPls.setValue(info[1]);
        } else {
            cmbBxConst_APPls.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxConst_APMns");
        if (result.equals("N")) {
            cmbBxConst_APMns.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxConst_APMns.setValue(info[1]);
        } else {
            cmbBxConst_APMns.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxConst_RPPls");
        if (result.equals("N")) {
            cmbBxConst_RPPls.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxConst_RPPls.setValue(info[1]);
        } else {
            cmbBxConst_RPPls.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxConst_RPMns");
        if (result.equals("N")) {
            cmbBxConst_RPMns.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxConst_RPMns.setValue(info[1]);
        } else {
            cmbBxConst_RPMns.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxRelay");
        if (result.equals("N")) {
            cmbBxRelay.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxRelay.setValue(info[1]);
        } else {
            cmbBxRelay.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxAppearance");
        if (result.equals("N")) {
            cmbBxAppearance.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxAppearance.setValue(info[1]);
        } else {
            cmbBxAppearance.setValue(info[2]);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxInsulation");
        if (result.equals("N")) {
            cmbBxInsulation.setValue(info[0]);
        } else if (result.equals("T")) {
            cmbBxInsulation.setValue(info[1]);
        } else {
            cmbBxInsulation.setValue(info[2]);
        }

        result = properties.getProperty("reportType");
        if (result.equals("HSSF")) {
            radBtnFormat97XLS.setSelected(true);
        } else {
            radBtnFormat2007XLSX.setSelected(true);
        }
    }

    private void saveAcrionInReportPane() {
        String[] info = {"НЕ ПРОВОДИЛОСЬ", "ГОДЕН", "ПРОВАЛИЛ"};

        if (cmbBxCreep.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxCreep", "N");
        } else if (cmbBxCreep.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxCreep", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxCreep", "F");
        }

        if (cmbBxRTC.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxRTC", "N");
        } else if (cmbBxCreep.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxRTC", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxRTC", "F");
        }

        if (cmbBxSTA_APPls.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_APPls", "N");
        } else if (cmbBxSTA_APPls.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_APPls", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_APPls", "F");
        }

        if (cmbBxSTA_APMns.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_APMns", "N");
        } else if (cmbBxSTA_APMns.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_APMns", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_APMns", "F");
        }

        if (cmbBxSTA_RPPls.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_RPPls", "N");
        } else if (cmbBxSTA_RPPls.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_RPPls", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_RPPls", "F");
        }

        if (cmbBxSTA_RPMns.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_RPMns", "N");
        } else if (cmbBxSTA_RPMns.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_RPMns", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxSTA_RPMns", "F");
        }

        if (cmbBxConst_APPls.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_APPls", "N");
        } else if (cmbBxConst_APPls.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_APPls", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_APPls", "F");
        }

        if (cmbBxConst_APMns.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_APMns", "N");
        } else if (cmbBxConst_APMns.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_APMns", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_APMns", "F");
        }

        if (cmbBxConst_RPPls.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_RPPls", "N");
        } else if (cmbBxConst_RPPls.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_RPPls", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_RPPls", "F");
        }

        if (cmbBxConst_RPMns.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_RPMns", "N");
        } else if (cmbBxConst_RPMns.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_RPMns", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxConst_RPMns", "F");
        }

        if (cmbBxRelay.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxRelay", "N");
        } else if (cmbBxRelay.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxRelay", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxRelay", "F");
        }

        if (cmbBxAppearance.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxAppearance", "N");
        } else if (cmbBxAppearance.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxAppearance", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxAppearance", "F");
        }

        if (cmbBxInsulation.getValue().equals(info[0])) {
            properties.setProperty("propertiesController.reportPane.cmbBxInsulation", "N");
        } else if (cmbBxInsulation.getValue().equals(info[1])) {
            properties.setProperty("propertiesController.reportPane.cmbBxInsulation", "T");
        } else {
            properties.setProperty("propertiesController.reportPane.cmbBxInsulation", "F");
        }

        if (radBtnFormat97XLS.isSelected()) {
            properties.setProperty("reportType", "HSSF");
        } else {
            properties.setProperty("reportType", "XSSF");
        }

        ConsoleHelper.saveProperties();

        ConsoleHelper.infoException("Информация", "Параметры успешно сохранены");
    }

    @Override
    public Stage getStage() {
        return (Stage) stendBtn.getScene().getWindow();
    }
}