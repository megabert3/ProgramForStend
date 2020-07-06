package org.taipit.stend.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.ResultsTest;

import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class EditResultsMetersController {

    private ResultsTest resultsTest = ResultsTest.getResultsTestInstance();

    private Properties properties = ConsoleHelper.properties;

    private String[] resultMass = properties.getProperty("restMeterResults").split(", ");

    private String[] meterModel = properties.getProperty("meterModels").split(", ");

    private String[] controllers = properties.getProperty("Controllers").split(", ");
    private String[] operators = properties.getProperty("Operators").split(", ");
    private String[] witneses = properties.getProperty("Witneses").split(", ");
    private String[] UnMass = properties.getProperty("Unom").split(", ");
    private String[] InomAndImax = properties.getProperty("InomAndImax").split(", ");
    private String[] FnMass = properties.getProperty("Fnom").split(", ");
    private String[] meterManufacturers = properties.getProperty("meterManufacturer").split(", ");
    private String[] typeMeter = properties.getProperty("meterTypeOnePhaseMultiTarif").split(", ");
    private String[] typeOfMeasuringElement = properties.getProperty("typeOfMeasuringElement").split(", ");

    private ResultsMetersController resultsMetersController;

    private ObservableList<Meter> resultList = FXCollections.observableArrayList(resultsTest.getListAllResults());

    private List<Meter> selectedMetersForEdit;

    private ObservableList<Integer> indexcesList;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private TableView<Meter> tabViewResults;

    @FXML
    private TableColumn<Meter, Integer> tabColPosition;

    @FXML
    private TableColumn<Meter, String> tabColSerNo;

    @FXML
    private TableColumn<Meter, String> tabColMeterModel;

    @FXML
    private TableColumn<Meter, String> tabColResultVerification;

    @FXML
    private TableColumn<Meter, String> tabColDate;

    @FXML
    private TableColumn<Meter, String> tabColCRPResult;

    @FXML
    private TableColumn<Meter, String> tabColStartResult;

    @FXML
    private TableColumn<Meter, String> tabColStartAPPls;

    @FXML
    private TableColumn<Meter, String> tabColStartAPMns;

    @FXML
    private TableColumn<Meter, String> tabColStartRPPls;

    @FXML
    private TableColumn<Meter, String> tabColStartRPMns;

    @FXML
    private TableColumn<Meter, String> tabColRTCResult;

    @FXML
    private TableColumn<Meter, String> tabColConstantResult;

    @FXML
    private TableColumn<Meter, String> tabColConstantAPPls;

    @FXML
    private TableColumn<Meter, String> tabColConstantAPMns;

    @FXML
    private TableColumn<Meter, String> tabColConstantRPPls;

    @FXML
    private TableColumn<Meter, String> tabColConstantRPMns;

    @FXML
    private TableColumn<Meter, String> tabColInsulationResult;

    @FXML
    private TableColumn<Meter, String> tabColApperianceResult;

    @FXML
    private TableColumn<Meter, String> tabColTemperature;

    @FXML
    private TableColumn<Meter, String> tabColHumidity;

    @FXML
    private TableColumn<Meter, String> tabColOperator;

    @FXML
    private TableColumn<Meter, String> tabColController;

    @FXML
    private TableColumn<Meter, String> tabColWitnes;

    @FXML
    private TableColumn<Meter, String> tabColUn;

    @FXML
    private TableColumn<Meter, String> tabColInomImax;

    @FXML
    private TableColumn<Meter, String> tabColFn;

    @FXML
    private TableColumn<Meter, String> tabColConstants;

    @FXML
    private TableColumn<Meter, String> tabColConstantMeterAPPls;

    @FXML
    private TableColumn<Meter, String> tabColConstantMeterRPMns;

    @FXML
    private TableColumn<Meter, String> tabColTypeMeter;

    @FXML
    private TableColumn<Meter, String> tabColTypeCurrentDetector;

    @FXML
    private TableColumn<Meter, String> tabColfactoryManufacturer;

    @FXML
    private TableColumn<Meter, String> tabColBatсhNo;

    @FXML
    void saveCancelAction(ActionEvent event) {
        if (event.getSource() == btnSave) {

            for (int i = 0; i < indexcesList.size(); i++) {
                resultList.set(indexcesList.get(i), selectedMetersForEdit.get(i));
            }

            resultsMetersController.getTabViewResults().getItems().setAll(FXCollections.observableArrayList(resultList));

            ResultsTest.getResultsTestInstance().serializationResults();

            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }

        if (event.getSource() == btnCancel) {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    void initialize() {

    }

    public void initColumsEditFrame() {
        tabColPosition.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabColPosition.setStyle( "-fx-alignment: CENTER;");

        //Серийный номер
        tabColSerNo.setCellValueFactory(new PropertyValueFactory<>("serNoMeter"));
        tabColSerNo.setCellFactory(TextFieldTableCell.forTableColumn());
        tabColSerNo.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newImpulseValue = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setSerNoMeter(newImpulseValue);

        });
        tabColSerNo.setStyle( "-fx-alignment: CENTER;");

        //Модель счётчика
        tabColMeterModel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                String modelMeter = meter.getModelMeter();
                return new SimpleObjectProperty<>(modelMeter);
            }

        });
        ObservableList<String> meterModelList = FXCollections.observableArrayList(meterModel);
        tabColMeterModel.setCellFactory(ComboBoxTableCell.forTableColumn(meterModelList));
        tabColMeterModel.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newModel = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setModelMeter(newModel);
        });
        tabColMeterModel.setStyle( "-fx-alignment: CENTER;");

        //Заключающий результат
        tabColResultVerification.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getTotalResult() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getTotalResult()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getTotalResult()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;

            }
        });

        ObservableList<String> finalResult = FXCollections.observableArrayList(resultMass);

        tabColResultVerification.setCellFactory(ComboBoxTableCell.forTableColumn(finalResult));

        tabColResultVerification.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            if (result.equals(resultMass[0])) {
                meter.setTotalResult(null);
            } else if (result.equals(resultMass[1])) {
                meter.setTotalResult(true);
            } else if (result.equals(resultMass[2])) {
                meter.setTotalResult(false);
            }
        });
        tabColResultVerification.setStyle( "-fx-alignment: CENTER;");

        //Дата поверки
        tabColDate.setCellValueFactory(new PropertyValueFactory<>("verificationDate"));
        tabColDate.setCellFactory(TextFieldTableCell.forTableColumn());
        tabColDate.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newModel = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setVerificationDate(newModel);
        });
        tabColDate.setStyle( "-fx-alignment: CENTER;");

        //Самоход
        tabColCRPResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.CreepResult creepResult = meter.getCreepTest();

                if (creepResult.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (creepResult.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!creepResult.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> creepResult = FXCollections.observableArrayList(resultMass);

        tabColCRPResult.setCellFactory(ComboBoxTableCell.forTableColumn(creepResult));

        tabColCRPResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.CreepResult creepResultSet = meter.getCreepTest();

            if (result.equals(resultMass[0])) {
                creepResultSet.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                creepResultSet.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                creepResultSet.setPassTest(false);
            }
        });
        tabColCRPResult.setStyle( "-fx-alignment: CENTER;");

        //Чувствительность AP+
        tabColStartAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.StartResult startResultAPPls = meter.getStartTestAPPls();

                if (startResultAPPls.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (startResultAPPls.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!startResultAPPls.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> startResultAPPls = FXCollections.observableArrayList(resultMass);

        tabColStartAPPls.setCellFactory(ComboBoxTableCell.forTableColumn(startResultAPPls));

        tabColStartAPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.StartResult staAPPls = meter.getStartTestAPPls();

            if (result.equals(resultMass[0])) {
                staAPPls.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                staAPPls.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                staAPPls.setPassTest(false);
            }
        });

        //Чувствительность AP-
        tabColStartAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.StartResult startResultAPMns = meter.getStartTestAPMns();

                if (startResultAPMns.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (startResultAPMns.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!startResultAPMns.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> startResultAPMns = FXCollections.observableArrayList(resultMass);

        tabColStartAPMns.setCellFactory(ComboBoxTableCell.forTableColumn(startResultAPMns));

        tabColStartAPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.StartResult staAPMns = meter.getStartTestAPMns();

            if (result.equals(resultMass[0])) {
                staAPMns.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                staAPMns.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                staAPMns.setPassTest(false);
            }
        });
        tabColStartAPMns.setStyle( "-fx-alignment: CENTER;");

        //Чувствительность RP+
        tabColStartRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.StartResult startResultRPPls = meter.getStartTestRPPls();

                if (startResultRPPls.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (startResultRPPls.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!startResultRPPls.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> startResultRPPls = FXCollections.observableArrayList(resultMass);
        tabColStartRPPls.setCellFactory(ComboBoxTableCell.forTableColumn(startResultRPPls));
        tabColStartRPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.StartResult staRPPls = meter.getStartTestRPPls();

            if (result.equals(resultMass[0])) {
                staRPPls.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                staRPPls.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                staRPPls.setPassTest(false);
            }
        });
        tabColStartRPPls.setStyle( "-fx-alignment: CENTER;");

        //Чувствительно RP-
        tabColStartRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.StartResult startResultRPMns = meter.getStartTestRPMns();

                if (startResultRPMns.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (startResultRPMns.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!startResultRPMns.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> startResultRPMns = FXCollections.observableArrayList(resultMass);

        tabColStartRPMns.setCellFactory(ComboBoxTableCell.forTableColumn(startResultRPMns));

        tabColStartRPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.StartResult staRPMns = meter.getStartTestRPMns();

            if (result.equals(resultMass[0])) {
                staRPMns.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                staRPMns.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                staRPMns.setPassTest(false);
            }
        });
        tabColStartRPMns.setStyle( "-fx-alignment: CENTER;");

        //Точность хода часов
        tabColRTCResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.RTCResult RTC = meter.getRTCTest();

                if (RTC.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (RTC.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!RTC.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> RTCResult = FXCollections.observableArrayList(resultMass);

        tabColRTCResult.setCellFactory(ComboBoxTableCell.forTableColumn(RTCResult));

        tabColRTCResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.RTCResult rtcResult = meter.getRTCTest();

            if (result.equals(resultMass[0])) {
                rtcResult.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                rtcResult.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                rtcResult.setPassTest(false);
            }
        });
        tabColRTCResult.setStyle( "-fx-alignment: CENTER;");

        //Проверка константы AP+
        tabColConstantAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.ConstantResult consAPPls = meter.getConstantTestAPPls();

                if (consAPPls.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (consAPPls.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!consAPPls.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> constantResultAPPls = FXCollections.observableArrayList(resultMass);

        tabColConstantAPPls.setCellFactory(ComboBoxTableCell.forTableColumn(constantResultAPPls));

        tabColConstantAPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.ConstantResult constAPPls = meter.getConstantTestAPPls();

            if (result.equals(resultMass[0])) {
                constAPPls.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                constAPPls.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                constAPPls.setPassTest(false);
            }
        });
        tabColConstantAPPls.setStyle( "-fx-alignment: CENTER;");

        //Проверка константы AP-
        tabColConstantAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.ConstantResult constAPMns = meter.getConstantTestAPMns();

                if (constAPMns.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (constAPMns.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!constAPMns.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> constantResultAPMns = FXCollections.observableArrayList(resultMass);

        tabColConstantAPMns.setCellFactory(ComboBoxTableCell.forTableColumn(constantResultAPMns));

        tabColConstantAPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.ConstantResult constAPMns = meter.getConstantTestAPMns();

            if (result.equals(resultMass[0])) {
                constAPMns.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                constAPMns.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                constAPMns.setPassTest(false);
            }
        });
        tabColConstantAPMns.setStyle( "-fx-alignment: CENTER;");

        //Проверка константы RP+
        tabColConstantRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.ConstantResult constRPPls = meter.getConstantTestRPPls();

                if (constRPPls.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (constRPPls.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!constRPPls.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> constantResultRPPls = FXCollections.observableArrayList(resultMass);

        tabColConstantRPPls.setCellFactory(ComboBoxTableCell.forTableColumn(constantResultRPPls));

        tabColConstantRPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.ConstantResult constRPPls = meter.getConstantTestRPPls();

            if (result.equals(resultMass[0])) {
                constRPPls.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                constRPPls.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                constRPPls.setPassTest(false);
            }
        });
        tabColConstantRPPls.setStyle( "-fx-alignment: CENTER;");

        //Проверка константы RP-
        tabColConstantRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.ConstantResult constRPMns = meter.getConstantTestRPMns();

                if (constRPMns.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (constRPMns.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!constRPMns.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> constantResultRPMns = FXCollections.observableArrayList(resultMass);

        tabColConstantRPMns.setCellFactory(ComboBoxTableCell.forTableColumn(constantResultRPMns));

        tabColConstantRPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.ConstantResult constRPMns = meter.getConstantTestRPMns();

            if (result.equals(resultMass[0])) {
                constRPMns.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                constRPMns.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                constRPMns.setPassTest(false);
            }
        });
        tabColConstantRPMns.setStyle( "-fx-alignment: CENTER;");

        //Изоляция
        tabColInsulationResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.InsulationResult insulationResult = meter.getInsulationTest();

                if (insulationResult.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (insulationResult.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!insulationResult.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> insulationResult = FXCollections.observableArrayList(resultMass);

        tabColInsulationResult.setCellFactory(ComboBoxTableCell.forTableColumn(insulationResult));

        tabColInsulationResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.InsulationResult insResult = meter.getInsulationTest();

            if (result.equals(resultMass[0])) {
                insResult.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                insResult.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                insResult.setPassTest(false);
            }
        });
        tabColInsulationResult.setStyle( "-fx-alignment: CENTER;");

        //Внешний вид
        tabColApperianceResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.AppearensResult appearensResult = meter.getAppearensTest();

                if (appearensResult.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (appearensResult.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!appearensResult.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        ObservableList<String> appearenseResult = FXCollections.observableArrayList(resultMass);

        tabColApperianceResult.setCellFactory(ComboBoxTableCell.forTableColumn(appearenseResult));

        tabColApperianceResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            Meter.AppearensResult appearensResult = meter.getAppearensTest();

            if (result.equals(resultMass[0])) {
                appearensResult.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                appearensResult.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                appearensResult.setPassTest(false);
            }
        });
        tabColApperianceResult.setStyle( "-fx-alignment: CENTER;");

        //Температура
        tabColTemperature.setCellValueFactory(new PropertyValueFactory<>("temperature"));

        tabColTemperature.setCellFactory(TextFieldTableCell.forTableColumn());

        tabColTemperature.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);
            try {
                meter.setTemperature(Float.parseFloat(result));
            }catch (NumberFormatException e) {
                e.printStackTrace();
                ConsoleHelper.infoException("Неверные данные");
            }
        });
        tabColTemperature.setStyle( "-fx-alignment: CENTER;");

        //Влажность
        tabColHumidity.setCellValueFactory(new PropertyValueFactory<>("humidity"));

        tabColHumidity.setCellFactory(TextFieldTableCell.forTableColumn());

        tabColHumidity.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            try {
                meter.setHumidity(Float.parseFloat(result));
            }catch (NumberFormatException e) {
                e.printStackTrace();
                ConsoleHelper.infoException("Неверные данные");
            }

        });
        tabColHumidity.setStyle( "-fx-alignment: CENTER;");

        //Оператор
        tabColOperator.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();
                return new SimpleObjectProperty<>(meter.getOperator());
            }
        });

        ObservableList<String> meterOperatorsList = FXCollections.observableArrayList(operators);

        tabColOperator.setCellFactory(ComboBoxTableCell.forTableColumn(meterOperatorsList));

        tabColOperator.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newOperator = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setOperator(newOperator);
        });
        tabColOperator.setStyle( "-fx-alignment: CENTER;");

        //Контроллёр
        tabColController.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                return new SimpleObjectProperty<>(param.getValue().getController());
            }
        });

        ObservableList<String> meterControllersList = FXCollections.observableArrayList(controllers);

        tabColController.setCellFactory(ComboBoxTableCell.forTableColumn(meterControllersList));
        tabColController.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newController = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setController(newController);
        });
        tabColController.setStyle( "-fx-alignment: CENTER;");

        tabColWitnes.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                String modelWitness = meter.getWitness();
                return new SimpleObjectProperty<>(modelWitness);
            }
        });

        ObservableList<String> meterWitnessList = FXCollections.observableArrayList(witneses);

        tabColWitnes.setCellFactory(ComboBoxTableCell.forTableColumn(meterWitnessList));

        tabColWitnes.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newWitness = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setWitness(newWitness);
        });
        tabColWitnes.setStyle( "-fx-alignment: CENTER;");

        //Напряжение
        tabColUn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                String Unom = String.valueOf(meter.getUn());
                return new SimpleObjectProperty<>(Unom);
            }
        });

        ObservableList<String> meterUnList = FXCollections.observableArrayList(UnMass);

        tabColUn.setCellFactory(ComboBoxTableCell.forTableColumn(meterUnList));

        tabColUn.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newUnom = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setUn(Float.parseFloat(newUnom));
        });
        tabColUn.setStyle( "-fx-alignment: CENTER;");

        //Ток
        tabColInomImax.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                String I = meter.getInomImax();
                return new SimpleObjectProperty<>(I);
            }
        });

        ObservableList<String> meterImaxInomList = FXCollections.observableArrayList(InomAndImax);

        tabColInomImax.setCellFactory(ComboBoxTableCell.forTableColumn(meterImaxInomList));

        tabColInomImax.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newUnom = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setInomImax(newUnom);
        });
        tabColInomImax.setStyle( "-fx-alignment: CENTER;");

        //Частота
        tabColFn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                String I = String.valueOf(meter.getFn());
                return new SimpleObjectProperty<>(I);
            }
        });

        ObservableList<String> FnomList = FXCollections.observableArrayList(FnMass);

        tabColFn.setCellFactory(ComboBoxTableCell.forTableColumn(FnomList));

        tabColFn.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newFnom = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setFn(Float.parseFloat(newFnom));
        });
        tabColFn.setStyle( "-fx-alignment: CENTER;");

        //Константа AP
        tabColConstantMeterAPPls.setCellValueFactory(new PropertyValueFactory<>("constantMeterAP"));

        tabColConstantMeterAPPls.setCellFactory(TextFieldTableCell.forTableColumn());

        tabColConstantMeterAPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newImpulseValue = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setConstantMeterAP(newImpulseValue);

        });
        tabColConstantMeterAPPls.setStyle( "-fx-alignment: CENTER;");

        //Константа RP
        tabColConstantMeterRPMns.setCellValueFactory(new PropertyValueFactory<>("constantMeterRP"));

        tabColConstantMeterRPMns.setCellFactory(TextFieldTableCell.forTableColumn());

        tabColConstantMeterRPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newImpulseValue = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setConstantMeterRP(newImpulseValue);

        });
        tabColConstantMeterRPMns.setStyle( "-fx-alignment: CENTER;");

        tabColTypeMeter.setCellValueFactory(new PropertyValueFactory<>("typeMeter"));

        tabColTypeMeter.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(typeMeter)));

        tabColTypeMeter.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newImpulseValue = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setTypeMeter(newImpulseValue);

        });
        tabColTypeMeter.setStyle( "-fx-alignment: CENTER;");

        //Датчик тока
        tabColTypeCurrentDetector.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result;

                if (meter.isTypeOfMeasuringElementShunt()) {
                    result = new SimpleStringProperty(typeOfMeasuringElement[1]);
                } else {
                    result = new SimpleStringProperty(typeOfMeasuringElement[0]);
                }
                return result;
            }
        });

        tabColTypeCurrentDetector.setCellFactory(ComboBoxTableCell.forTableColumn(typeOfMeasuringElement));

        tabColTypeCurrentDetector.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            if (result.equals(typeOfMeasuringElement[1])) {
                meter.setTypeOfMeasuringElementShunt(true);
            } else {
                meter.setTypeOfMeasuringElementShunt(false);
            }
        });
        tabColTypeCurrentDetector.setStyle( "-fx-alignment: CENTER;");

        //Производитель
        tabColfactoryManufacturer.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                return new SimpleStringProperty(meter.getFactoryManufacturer());
            }
        });

        tabColfactoryManufacturer.setCellFactory(ComboBoxTableCell.forTableColumn(meterManufacturers));

        tabColfactoryManufacturer.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setFactoryManufacturer(result);
        });
        tabColfactoryManufacturer.setStyle( "-fx-alignment: CENTER;");

        //Номер партии
        tabColBatсhNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));

        tabColBatсhNo.setCellFactory(TextFieldTableCell.forTableColumn());

        tabColBatсhNo.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);
            meter.setBatchNo(result);
        });
        tabColBatсhNo.setStyle( "-fx-alignment: CENTER;");

        tabViewResults.setItems(FXCollections.observableArrayList(selectedMetersForEdit));
    }

    public void setSelectedMetersForEdit(List<Meter> selectedMetersForEdit) {
        this.selectedMetersForEdit = selectedMetersForEdit;
    }

    public void setIndexcesList(ObservableList<Integer> indexcesList) {
        this.indexcesList = indexcesList;
    }

    public void setResultsMetersController(ResultsMetersController resultsMetersController) {
        this.resultsMetersController = resultsMetersController;
    }
}