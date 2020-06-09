package org.taipit.stend.controller.viewController;

import java.io.IOException;
import java.util.*;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.InfoExсeption;
import org.taipit.stend.model.ResultsTest;

public class SaveResultsTestFrame {

    private TestErrorTableFrameController testErrorTableFrameController;

    private Properties properties = ConsoleHelper.properties;

    private String[] resultMass = properties.getProperty("restMeterResults").split(", ");

    private String[] meterModel = properties.getProperty("meterModels").split(", ");

    private String controller = properties.getProperty("lastController");
    private String operator = properties.getProperty("lastOperator");
    private String witnes = properties.getProperty("lastWitness");

    private String[] controllers = properties.getProperty("Controllers").split(", ");
    private String[] operators = properties.getProperty("Operators").split(", ");
    private String[] witneses = properties.getProperty("Witneses").split(", ");

    private String temperature = properties.getProperty("lastTemperature");
    private String humidity = properties.getProperty("lastHumidity");

    private String batchNumb = properties.getProperty("lastBatchNumb");

    private List<Meter> meterList;

    @FXML
    private ComboBox<String> chosBxOperator;

    @FXML
    private ComboBox<String> chosBxController;

    @FXML
    private ComboBox<String> chosBxWitness;

    @FXML
    private TextField txtFldBatchNumb;

    @FXML
    private TextField txtFldТMusterDate;

    @FXML
    private TextField txtFldTemperature;

    @FXML
    private TextField txtFldHumidity;

    @FXML
    private TextField txtFldOperator;

    @FXML
    private TextField txtFldController;

    @FXML
    private TextField txtFldWitness;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnBack;

    @FXML
    private TableView<Meter> tabViewResults;

    @FXML
    private TableColumn<Meter, Boolean> tabColChBxSelectOrNot;

    @FXML
    private TableColumn<Meter, Integer> tabColPosition;

    @FXML
    private TableColumn<Meter, String> tabColSerNo;

    @FXML
    private TableColumn<Meter, String> tabColMeterModel;

    @FXML
    private TableColumn<Meter, String> tabColResultVerification;

    @FXML
    private TableColumn<Meter, String> tabColCRPResult;

    @FXML
    private TableColumn<Meter, String> tabColStartResult;

    @FXML
    private TableColumn<Meter, String> tabColRTCResult;

    @FXML
    private TableColumn<Meter, String> tabColInsulationResult;

    @FXML
    private TableColumn<Meter, String> tabColApperianceResult;

    @FXML
    private TableColumn<Meter, String> tabColConstantResult;

    @FXML
    private TableColumn<Meter, String> tabColStartAPPls;
    @FXML
    private TableColumn<Meter, String> tabColStartAPMns;
    @FXML
    private TableColumn<Meter, String> tabColStartRPPls;
    @FXML
    private TableColumn<Meter, String> tabColStartRPMns;

    @FXML
    private TableColumn<Meter, String> tabColConstantAPPls;
    @FXML
    private TableColumn<Meter, String> tabColConstantAPMns;
    @FXML
    private TableColumn<Meter, String> tabColConstantRPPls;
    @FXML
    private TableColumn<Meter, String> tabColConstantRPMns;

    @FXML
    private TextField txtFldManufacturer;

    @FXML
    void initialize() {

    }

    @FXML
    void backSaveCancelActions(ActionEvent event) {
        if (event.getSource() == btnBack) {
            Stage stageTestErrorTable = (Stage) testErrorTableFrameController.getTxtLabDate().getScene().getWindow();
            stageTestErrorTable.show();

            Stage stageSaveResultTest = (Stage) txtFldWitness.getScene().getWindow();
            stageSaveResultTest.close();
        }

        if (event.getSource() == btnCancel) {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/yesOrNoFrame.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            YesOrNoFrameController yesOrNoFrameController = fxmlLoader.getController();
            yesOrNoFrameController.setExitSaveResultFrameWithoutSaving(true);
            yesOrNoFrameController.setStageSaveResultTest((Stage) txtFldWitness.getScene().getWindow());
            yesOrNoFrameController.getQuestionTxt().setText("Вы уверены, что хотите выйти \nбез сохранения результатов теста?");
            yesOrNoFrameController.getQuestionTxt().setLayoutX(165);
            yesOrNoFrameController.getQuestionTxt().setLayoutY(30);

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Сохранение результата");
            stage.setScene(new Scene(root));
            stage.show();

        }

        if (event.getSource() == btnSave) {
            ResultsTest resultsTest = ResultsTest.getResultsTestInstance();

            for (Meter meter : meterList) {
                if (meter.isSaveResults()) {
                    meter.setOperator(txtFldOperator.getText());
                    meter.setController(txtFldController.getText());
                    meter.setWitness(txtFldWitness.getText());
                    meter.setTemperature(txtFldTemperature.getText());
                    meter.setHumidity(txtFldHumidity.getText());
                    meter.setFactoryManufacturer(txtFldManufacturer.getText());
                    meter.setBatchNo(txtFldBatchNumb.getText());
                    meter.setVerificationDate(txtFldТMusterDate.getText());
                    meter.setLastModifiedDate(new Date().toString());
                }
            }

            try {
                resultsTest.addMeterRusults(meterList);
            } catch (InfoExсeption infoExсeption) {
                infoExсeption.printStackTrace();
            }

            resultsTest.serializationResults();

            Stage stageTestErrorTable = (Stage) testErrorTableFrameController.getTxtLabDate().getScene().getWindow();
            stageTestErrorTable.close();

            Stage stageSaveResultTest = (Stage) txtFldWitness.getScene().getWindow();
            stageSaveResultTest.close();
        }
    }

    public void initAllColums() {
        //Получаю счётчики с окна тестирования
        meterList = testErrorTableFrameController.getListMetersForTest();

        //Устанавливаю значения общего результата теста
        for (Meter meter : meterList) {
            meter.setFinalAllTestResult(meter.meterPassOrNotAlltests());
        }

        //Установка чек боксов и добавление к ним слушателя
        tabColChBxSelectOrNot.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Meter, Boolean> param) {
                Meter meter = param.getValue();
                SimpleBooleanProperty simpleBooleanProperty = new SimpleBooleanProperty(meter.isSaveResults());

                simpleBooleanProperty.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        meter.setSaveResults(newValue);
                    }
                });

                return simpleBooleanProperty;
            }
        });

        tabColChBxSelectOrNot.setCellFactory(new Callback<TableColumn<Meter, Boolean>,
                TableCell<Meter, Boolean>>() {
            @Override
            public TableCell<Meter, Boolean> call(TableColumn<Meter, Boolean> p) {
                CheckBoxTableCell<Meter, Boolean> cell = new CheckBoxTableCell<Meter, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        tabColPosition.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabColPosition.setStyle( "-fx-alignment: CENTER;");

        //Отображение серийного номера счётчика
        tabColSerNo.setCellValueFactory(new PropertyValueFactory<>("serNoMeter"));
        tabColSerNo.setStyle( "-fx-alignment: CENTER;");
        tabColSerNo.setCellFactory(TextFieldTableCell.<Meter>forTableColumn());
        tabColSerNo.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

            TablePosition<Meter, String> pos = event.getTablePosition();

            String newSerNo = event.getNewValue();

            int row = pos.getRow();

            Meter meter = event.getTableView().getItems().get(row);

            meter.setSerNoMeter(newSerNo);
        });

        //Выбор модели счётчика из списка
        tabColMeterModel.setStyle( "-fx-alignment: CENTER;");
        ObservableList<String> meterModelList = FXCollections.observableArrayList(meterModel);
        tabColMeterModel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                String modelMeter = meter.getModelMeter();
                return new SimpleObjectProperty<>(modelMeter);
            }

        });

        tabColMeterModel.setCellFactory(ComboBoxTableCell.forTableColumn(meterModelList));

        tabColMeterModel.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newModel = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setModelMeter(newModel);
        });

        //============================= Установка заключающего результата ==========================================================
        tabColResultVerification.setStyle( "-fx-alignment: CENTER;");

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

        //============================ Установка результатов теста Самоход ============================================

        tabColCRPResult.setStyle( "-fx-alignment: CENTER;");

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

        //========================== Установка результатов теста Чувствительность ==============================================
        //Результаты теста на самоход активной энергии в прямом напралении
        tabColStartAPPls.setStyle( "-fx-alignment: CENTER;");
        tabColStartAPPls.setEditable(true);
        tabColStartAPPls.setSortable(false);
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


        //Если есть результаты теста на самоход активной энергии в обратном напралении
        tabColStartAPMns.setStyle( "-fx-alignment: CENTER;");
        tabColStartAPMns.setEditable(true);
        tabColStartAPMns.setSortable(false);

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

        //Если есть результаты теста на самоход реактивной энергии в прямом напралении
        tabColStartRPPls.setStyle( "-fx-alignment: CENTER;");
        tabColStartRPPls.setEditable(true);
        tabColStartRPPls.setSortable(false);

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

        tabColStartRPMns.setStyle( "-fx-alignment: CENTER;");
        tabColStartRPMns.setEditable(true);
        tabColStartRPMns.setSortable(false);

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

        //================================ Точность хода часов ===============================
        tabColRTCResult.setStyle( "-fx-alignment: CENTER;");

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

        //================================ Счётный механизм ===============================
        //Если есть результаты теста на самоход активной энергии в прямом напралении

        tabColConstantAPPls.setStyle( "-fx-alignment: CENTER;");
        tabColConstantAPPls.setEditable(true);
        tabColConstantAPPls.setSortable(false);

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

        //Если есть результаты теста на самоход активной энергии в обратном напралении

        tabColConstantAPMns.setStyle( "-fx-alignment: CENTER;");
        tabColConstantAPMns.setEditable(true);
        tabColConstantAPMns.setSortable(false);

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

        //Если есть результаты теста на самоход реактивной энергии в прямом напралении
        tabColConstantRPPls.setStyle( "-fx-alignment: CENTER;");
        tabColConstantRPPls.setEditable(true);
        tabColConstantRPPls.setSortable(false);

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

        //Если есть результаты теста на самоход реактивной энергии в прямом напралении
        tabColConstantRPMns.setStyle( "-fx-alignment: CENTER;");
        tabColConstantRPMns.setEditable(true);
        tabColConstantRPMns.setSortable(false);

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

        //======================== Проверка изоляции ===========================
        tabColInsulationResult.setStyle( "-fx-alignment: CENTER;");

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

        //======================== Внешний вид ===========================
        tabColApperianceResult.setStyle( "-fx-alignment: CENTER;");

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

        ObservableList<String> appearenseResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

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

        //tabViewResults.setPrefWidth(paneForTabView.getPrefWidth());
        tabViewResults.setItems(FXCollections.observableArrayList(meterList));

        chosBxOperator.getItems().addAll(operators);
        chosBxController.getItems().addAll(controllers);
        chosBxWitness.getItems().addAll(witneses);

        txtFldOperator.setText(operator);
        txtFldController.setText(controller);
        txtFldWitness.setText(witnes);

        txtFldTemperature.setText(temperature);
        txtFldHumidity.setText(humidity);
        txtFldBatchNumb.setText(batchNumb);

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

    public void setMeterList(List<Meter> meterList) {
        this.meterList = meterList;
    }

    public void setTestErrorTableFrameController(TestErrorTableFrameController testErrorTableFrameController) {
        this.testErrorTableFrameController = testErrorTableFrameController;
    }
}
