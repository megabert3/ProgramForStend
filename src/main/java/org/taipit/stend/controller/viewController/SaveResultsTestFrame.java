package org.taipit.stend.controller.viewController;

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
import javafx.geometry.Pos;
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
import org.taipit.stend.helper.frameManager.FrameManager;
import org.taipit.stend.model.ExcelReport;
import org.taipit.stend.model.ResultsTest;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна сохранения результатов теста "saveResultsTest.fxml".
 */
public class SaveResultsTestFrame {

    //Окно самого теста
    private TestErrorTableFrameController testErrorTableFrameController;

    //Настройки
    private Properties properties = ConsoleHelper.properties;

    //Пароль для редактирования отчётов
    private boolean password;

    //Установка значений из настроек полям окна
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
    private Button saveAndPrint;

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
    private TableColumn<Meter, String> tabColRelayResult;

    @FXML
    void initialize() {

        if (properties.getProperty("config").isEmpty()) {
            password = false;
        } else {
            password = true;
        }
    }

    /**
     * Отвечает за действия кнопок отмены, сохранения или возврата к тесту
     * @param event
     */
    @FXML
    void backSaveCancelActions(ActionEvent event) {

        //Если пользорватель нажал вернуться к тесту
        if (event.getSource() == btnBack) {
            Stage stageTestErrorTable = (Stage) testErrorTableFrameController.getTxtLabDate().getScene().getWindow();
            stageTestErrorTable.show();

            Stage stageSaveResultTest = (Stage) txtFldWitness.getScene().getWindow();
            stageSaveResultTest.close();
        }

        //Если пользорватель нажал отменить сохранения результатов
        if (event.getSource() == btnCancel) {

            //Спрашиваю уверен ли пользователь, что хочет выйти без сохранения
            Boolean b = ConsoleHelper.yesOrNoFrame("Сохранение", "Вы уверены, что хотите выйти \nбез сохранения результатов теста?");

            if (b != null) {
                if (b) {
                    //Закрываю это окно
                    Stage saveResults = (Stage) btnCancel.getScene().getWindow();
                    saveResults.close();

                    //Закрываю окно теста
                    Stage stageTestErrorTableFraneControler = (Stage) testErrorTableFrameController.getTxtLabInom().getScene().getWindow();
                    stageTestErrorTableFraneControler.close();
                    testErrorTableFrameController.getRefMeterStage().close();
                    FrameManager.frameManagerInstance().setTestErrorTableFrameController(null);
                }
            }
        }

        //Если пользователь хочет сохранить результат теста
        if (event.getSource() == btnSave) {

            ResultsTest resultsTest = ResultsTest.getResultsTestInstance();

            List<Meter> helpList = new ArrayList<>();

            txtFldTemperature.setStyle("");
            txtFldHumidity.setStyle("");
            txtFldBatchNumb.setStyle("");

            for (Meter meter : meterList) {

                if (meter.isSaveResults()) {

                    meter.setOperator(txtFldOperator.getText());
                    meter.setController(txtFldController.getText());
                    meter.setWitness(txtFldWitness.getText());

                    try {
                        meter.setTemperature(Float.parseFloat(txtFldTemperature.getText()));
                    }catch (NumberFormatException e) {
                        txtFldTemperature.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        ConsoleHelper.infoException("Значение должно быть численным");
                        return;
                    }

                    try {
                        meter.setHumidity(Float.parseFloat(txtFldHumidity.getText()));
                    }catch (NumberFormatException e) {
                        txtFldHumidity.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        ConsoleHelper.infoException("Значение должно быть численным");
                        return;
                    }

                    try {
                        Float.parseFloat(txtFldBatchNumb.getText());
                    }catch (NumberFormatException e) {
                        txtFldBatchNumb.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        ConsoleHelper.infoException("Значение должно быть численным");
                        return;
                    }

                    meter.setBatchNo(txtFldBatchNumb.getText());
                    meter.setVerificationDate(txtFldТMusterDate.getText());
                    meter.setLastModifiedDate(new Date().toString());
                    helpList.add(meter);
                }
            }

            //Добавляю результаты в хранилище результатов
            resultsTest.addMeterRusults(helpList);

            //Сохраняю на компьютере
            resultsTest.serializationResults();

            Stage stageTestErrorTable = (Stage) testErrorTableFrameController.getTxtLabDate().getScene().getWindow();
            stageTestErrorTable.close();

            Stage stageSaveResultTest = (Stage) txtFldWitness.getScene().getWindow();
            stageSaveResultTest.close();
        }

        //Если пользователь выбрал сохранить результат и вывести отчёт
        if (event.getSource() == saveAndPrint) {

            txtFldTemperature.setStyle("");
            txtFldHumidity.setStyle("");
            txtFldBatchNumb.setStyle("");

            ResultsTest resultsTest = ResultsTest.getResultsTestInstance();
            List<Meter> helpList = new ArrayList<>();

            for (Meter meter : meterList) {
                if (meter.isSaveResults()) {
                    meter.setOperator(txtFldOperator.getText());
                    meter.setController(txtFldController.getText());
                    meter.setWitness(txtFldWitness.getText());
                    try {
                        meter.setTemperature(Float.parseFloat(txtFldTemperature.getText()));
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        txtFldTemperature.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        ConsoleHelper.infoException("Неверные данные");
                        return;
                    }

                    try {
                        meter.setHumidity(Float.parseFloat(txtFldHumidity.getText()));
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        txtFldHumidity.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        ConsoleHelper.infoException("Неверные данные");
                        return;
                    }

                    try {
                        Float.parseFloat(txtFldBatchNumb.getText());
                    }catch (NumberFormatException e) {
                        txtFldBatchNumb.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        ConsoleHelper.infoException("Значение должно быть численным");
                        return;
                    }

                    meter.setBatchNo(txtFldBatchNumb.getText());
                    meter.setVerificationDate(txtFldТMusterDate.getText());
                    meter.setLastModifiedDate(new Date().toString());

                    helpList.add(meter);
                }
            }

            ExcelReport excelReport = new ExcelReport();

            if (excelReport.createExcelReport(helpList, saveAndPrint.getScene().getWindow())) {
                excelReport.openExcelReport();
            } else return;

            resultsTest.addMeterRusults(helpList);

            resultsTest.serializationResults();

            Stage stageTestErrorTable = (Stage) testErrorTableFrameController.getTxtLabDate().getScene().getWindow();
            stageTestErrorTable.close();

            Stage stageSaveResultTest = (Stage) txtFldWitness.getScene().getWindow();
            stageSaveResultTest.close();
        }
    }

    /**
     * Инициализирует таблицу результатами тестов счётчика(ов)
     */
    public void initAllColums() {
        //Получаю счётчики с окна тестирования
        meterList = testErrorTableFrameController.getListMetersForTest();

        //Устанавливаю значения общего результата теста
        for (Meter meter : meterList) {
            Meter.TotalResult totalResult = meter.getTotalResultObject();
            meter.setTotalResult(totalResult.calculateTotalResult());
        }

        //Установка чек боксов для сохранения результатов счётчика и добавление к ним слушателя
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

        //Номер места на установке
        tabColPosition.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabColPosition.setStyle( "-fx-alignment: CENTER;");

        //Отображение серийного номера счётчика
        tabColSerNo.setCellValueFactory(new PropertyValueFactory<>("serNoMeter"));
        tabColSerNo.setStyle( "-fx-alignment: CENTER;");
        tabColSerNo.setCellFactory(TextFieldTableCell.<Meter>forTableColumn());
        tabColSerNo.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        //============================ Установка результатов теста Самоход ============================================

        tabColCRPResult.setStyle( "-fx-alignment: CENTER;");

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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


        //Если есть результаты теста на самоход активной энергии в обратном напралении
        tabColStartAPMns.setStyle( "-fx-alignment: CENTER;");
        tabColStartAPMns.setEditable(true);
        tabColStartAPMns.setSortable(false);

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        //Если есть результаты теста на самоход реактивной энергии в прямом напралении
        tabColStartRPPls.setStyle( "-fx-alignment: CENTER;");
        tabColStartRPPls.setEditable(true);
        tabColStartRPPls.setSortable(false);

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        tabColStartRPMns.setStyle( "-fx-alignment: CENTER;");
        tabColStartRPMns.setEditable(true);
        tabColStartRPMns.setSortable(false);

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        //================================ Точность хода часов ===============================
        tabColRTCResult.setStyle( "-fx-alignment: CENTER;");

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        //Если есть результаты теста на самоход активной энергии в обратном напралении
        tabColConstantAPMns.setStyle( "-fx-alignment: CENTER;");
        tabColConstantAPMns.setEditable(true);
        tabColConstantAPMns.setSortable(false);

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        //Если есть результаты теста на самоход реактивной энергии в прямом напралении
        tabColConstantRPPls.setStyle( "-fx-alignment: CENTER;");
        tabColConstantRPPls.setEditable(true);
        tabColConstantRPPls.setSortable(false);

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        //Если есть результаты теста на самоход реактивной энергии в прямом напралении
        tabColConstantRPMns.setStyle( "-fx-alignment: CENTER;");
        tabColConstantRPMns.setEditable(true);
        tabColConstantRPMns.setSortable(false);

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        //================================ Реле ===================================
        tabColRelayResult.setStyle("-fx-alignment: CENTER;");
        tabColRelayResult.setEditable(true);
        tabColRelayResult.setSortable(false);

        tabColRelayResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                Meter.RelayResult relayResult = meter.getRelayTest();

                if (relayResult.getPassTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (relayResult.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!relayResult.getPassTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });


        ObservableList<String> relayResult = FXCollections.observableArrayList(resultMass);

        tabColRelayResult.setCellFactory(ComboBoxTableCell.forTableColumn(relayResult));

        tabColRelayResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();

            Meter meter = event.getTableView().getItems().get(row);

            Meter.RelayResult relayResultEdit = meter.getRelayTest();

            if (result.equals(resultMass[0])) {
                relayResultEdit.setPassTest(null);
            } else if (result.equals(resultMass[1])) {
                relayResultEdit.setPassTest(true);
            } else if (result.equals(resultMass[2])) {
                relayResultEdit.setPassTest(false);
            }
        });

        //======================== Проверка изоляции ===========================
        tabColInsulationResult.setStyle( "-fx-alignment: CENTER;");

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
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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

        //======================== Внешний вид ===========================
        tabColApperianceResult.setStyle( "-fx-alignment: CENTER;");

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

        ObservableList<String> appearenseResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

        tabColApperianceResult.setCellFactory(ComboBoxTableCell.forTableColumn(appearenseResult));

        tabColApperianceResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            if (requestPassword()) {
                tabViewResults.refresh();
                return;
            }

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


    public void setTestErrorTableFrameController(TestErrorTableFrameController testErrorTableFrameController) {
        this.testErrorTableFrameController = testErrorTableFrameController;
    }

    //Если установлен пароль, то запрашиваю его
    private boolean requestPassword() {
        if (password) {
            if (ConsoleHelper.passwordFrame()) {
                password = false;
            }
        }
        return password;
    }

    public Button getBtnCancel() {
        return btnCancel;
    }
}
