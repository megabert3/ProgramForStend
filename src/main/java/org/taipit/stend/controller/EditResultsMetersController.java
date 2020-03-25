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
import javafx.scene.layout.Pane;
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

    private String[] meterModel = properties.getProperty("meterModel").split(", ");

    private String[] controllers = properties.getProperty("Controller").split(", ");
    private String[] operators = properties.getProperty("Operators").split(", ");
    private String[] witneses = properties.getProperty("Witness").split(", ");
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
    private Pane paneForTabView;

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

                if (meter.getFinalAllTestResult() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getFinalAllTestResult()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getFinalAllTestResult()) {
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
                meter.setFinalAllTestResult(null);
            } else if (result.equals(resultMass[1])) {
                meter.setFinalAllTestResult(true);
            } else if (result.equals(resultMass[2])) {
                meter.setFinalAllTestResult(false);
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

                if (meter.getCreepTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getCreepTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getCreepTest()) {
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

            if (result.equals(resultMass[0])) {
                meter.setCreepTest(null);
            } else if (result.equals(resultMass[1])) {
                meter.setCreepTest(true);
            } else if (result.equals(resultMass[2])) {
                meter.setCreepTest(false);
            }
        });
        tabColCRPResult.setStyle( "-fx-alignment: CENTER;");

        //Чувствительность AP+
        tabColStartAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getStartTestAPPls() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getStartTestAPPls()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getStartTestAPPls()) {
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

            if (result.equals(resultMass[0])) {
                meter.setStartTestAPPls(null);
            } else if (result.equals(resultMass[1])) {
                meter.setStartTestAPPls(true);
            } else if (result.equals(resultMass[2])) {
                meter.setStartTestAPPls(false);
            }
        });

        //Чувствительность AP-
        tabColStartAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getStartTestAPMns() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getStartTestAPMns()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getStartTestAPMns()) {
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

            if (result.equals(resultMass[0])) {
                meter.setStartTestAPMns(null);
            } else if (result.equals(resultMass[1])) {
                meter.setStartTestAPMns(true);
            } else if (result.equals(resultMass[2])) {
                meter.setStartTestAPMns(false);
            }
        });
        tabColStartAPMns.setStyle( "-fx-alignment: CENTER;");

        //Чувствительность RP+
        tabColStartRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getStartTestRPPls() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getStartTestRPPls()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getStartTestRPPls()) {
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

            if (result.equals(resultMass[0])) {
                meter.setStartTestRPPls(null);
            } else if (result.equals(resultMass[1])) {
                meter.setStartTestRPPls(true);
            } else if (result.equals(resultMass[2])) {
                meter.setStartTestRPPls(false);
            }
        });
        tabColStartRPPls.setStyle( "-fx-alignment: CENTER;");

        //Чувствительно RP-
        tabColStartRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getStartTestRPMns() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getStartTestRPMns()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getStartTestRPMns()) {
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

            if (result.equals(resultMass[0])) {
                meter.setStartTestRPMns(null);
            } else if (result.equals(resultMass[1])) {
                meter.setStartTestRPMns(true);
            } else if (result.equals(resultMass[2])) {
                meter.setStartTestRPMns(false);
            }
        });
        tabColStartRPMns.setStyle( "-fx-alignment: CENTER;");

        //Точность хода часов
        tabColRTCResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getRTCTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getRTCTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getRTCTest()) {
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

            if (result.equals(resultMass[0])) {
                meter.setRTCTest(null);
            } else if (result.equals(resultMass[1])) {
                meter.setRTCTest(true);
            } else if (result.equals(resultMass[2])) {
                meter.setRTCTest(false);
            }
        });
        tabColRTCResult.setStyle( "-fx-alignment: CENTER;");

        //Проверка константы AP+
        tabColConstantAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getConstantTestAPPls() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getConstantTestAPPls()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getConstantTestAPPls()) {
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

            if (result.equals(resultMass[0])) {
                meter.setConstantTestAPPls(null);
            } else if (result.equals(resultMass[1])) {
                meter.setConstantTestAPPls(true);
            } else if (result.equals(resultMass[2])) {
                meter.setConstantTestAPPls(false);
            }
        });
        tabColConstantAPPls.setStyle( "-fx-alignment: CENTER;");

        //Проверка константы AP-
        tabColConstantAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getConstantTestAPMns() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getConstantTestAPMns()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getConstantTestAPMns()) {
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

            if (result.equals(resultMass[0])) {
                meter.setConstantTestAPMns(null);
            } else if (result.equals(resultMass[1])) {
                meter.setConstantTestAPMns(true);
            } else if (result.equals(resultMass[2])) {
                meter.setConstantTestAPMns(false);
            }
        });
        tabColConstantAPMns.setStyle( "-fx-alignment: CENTER;");

        //Проверка константы RP+
        tabColConstantRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getConstantTestRPPls() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getConstantTestRPPls()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getConstantTestRPPls()) {
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

            if (result.equals(resultMass[0])) {
                meter.setConstantTestRPPls(null);
            } else if (result.equals(resultMass[1])) {
                meter.setConstantTestRPPls(true);
            } else if (result.equals(resultMass[2])) {
                meter.setConstantTestRPPls(false);
            }
        });
        tabColConstantRPPls.setStyle( "-fx-alignment: CENTER;");

        //Проверка константы RP-
        tabColConstantRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getConstantTestRPMns() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getConstantTestRPMns()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getConstantTestRPMns()) {
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

            if (result.equals(resultMass[0])) {
                meter.setConstantTestRPMns(null);
            } else if (result.equals(resultMass[1])) {
                meter.setConstantTestRPMns(true);
            } else if (result.equals(resultMass[2])) {
                meter.setConstantTestRPMns(false);
            }
        });
        tabColConstantRPMns.setStyle( "-fx-alignment: CENTER;");

        //Изоляция
        tabColInsulationResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getInsulationTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getInsulationTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getInsulationTest()) {
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

            if (result.equals(resultMass[0])) {
                meter.setInsulationTest(null);
            } else if (result.equals(resultMass[1])) {
                meter.setInsulationTest(true);
            } else if (result.equals(resultMass[2])) {
                meter.setInsulationTest(false);
            }
        });
        tabColInsulationResult.setStyle( "-fx-alignment: CENTER;");

        //Внешний вид
        tabColApperianceResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getAppearensTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getAppearensTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getAppearensTest()) {
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

            if (result.equals(resultMass[0])) {
                meter.setAppearensTest(null);
            } else if (result.equals(resultMass[1])) {
                meter.setAppearensTest(true);
            } else if (result.equals(resultMass[2])) {
                meter.setAppearensTest(false);
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

            meter.setTemperature(result);
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

            meter.setHumidity(result);
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

            meter.setUn(Double.parseDouble(newUnom));
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

            meter.setFn(Double.parseDouble(newFnom));
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

        tabViewResults.setPrefWidth(paneForTabView.getPrefWidth());
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