package org.taipit.stend.controller.viewController;

import java.util.*;

import com.sun.istack.internal.Nullable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.ConsoleHelper;

public class SaveResultsTestFrame {

    private TestErrorTableFrameController testErrorTableFrameController;

    private Properties properties = ConsoleHelper.properties;

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
    private Button btnBack;

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
    private TextField txtFldManufacturer;

    @FXML
    void initialize() {

    }

    public void initAllColums() {
        //Получаю счётчики с окна тестирования
        meterList = testErrorTableFrameController.getListMetersForTest();

        //Устанавливаю значения общего результата теста
        for (Meter meter : meterList) {
            meter.setFinalAllTestResult(meter.meterPassOrNotAlltests());
        }

        TableColumn<Meter, String> tabColStartAPPls = null;
        TableColumn<Meter, String> tabColStartAPMns = null;
        TableColumn<Meter, String> tabColStartRPPls = null;
        TableColumn<Meter, String> tabColStartRPMns = null;

        TableColumn<Meter, String> tabColConstantAPPls = null;
        TableColumn<Meter, String> tabColConstantAPMns = null;
        TableColumn<Meter, String> tabColConstantRPPls = null;
        TableColumn<Meter, String> tabColConstantRPMns = null;

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
        ObservableList<String> meterModelList = FXCollections.observableArrayList(properties.getProperty("meterModel").split(", "));
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
                    result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                } else if (meter.getFinalAllTestResult()) {
                    result = new SimpleStringProperty("ГОДЕН");
                } else if (!meter.getFinalAllTestResult()) {
                    result = new SimpleStringProperty("ПРОВАЛИЛ");
                }
                System.out.println(result);
                return result;

            }
        });

        ObservableList<String> finalResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

        tabColResultVerification.setCellFactory(ComboBoxTableCell.forTableColumn(finalResult));

        tabColResultVerification.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String result = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            switch (result) {
                case "НЕ ПРОВОДИЛОСЬ":
                    meter.setFinalAllTestResult(null);
                    break;
                case "ГОДЕН":
                    meter.setFinalAllTestResult(true);
                    break;
                case "ПРОВАЛИЛ":
                    meter.setFinalAllTestResult(false);
                    break;
            }
        });

        //============================ Установка результатов теста Самоход ============================================
        if (spendCreepTest()) {
            tabColCRPResult.setStyle( "-fx-alignment: CENTER;");

            tabColCRPResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                    SimpleStringProperty result = null;

                    if (meter.getCreepTest() == null) {
                        result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                    } else if (meter.getCreepTest()) {
                        result = new SimpleStringProperty("ГОДЕН");
                    } else if (!meter.getCreepTest()) {
                        result = new SimpleStringProperty("ПРОВАЛИЛ");
                    }

                    return result;
                }
            });

            ObservableList<String> creepResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

            tabColCRPResult.setCellFactory(ComboBoxTableCell.forTableColumn(creepResult));

            tabColCRPResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                TablePosition<Meter, String> pos = event.getTablePosition();

                String result = event.getNewValue();

                int row = pos.getRow();
                Meter meter = event.getTableView().getItems().get(row);

                switch (result) {
                    case "НЕ ПРОВОДИЛОСЬ":
                        meter.setCreepTest(null);
                        break;
                    case "ГОДЕН":
                        meter.setCreepTest(true);
                        break;
                    case "ПРОВАЛИЛ":
                        meter.setCreepTest(false);
                        break;
                }
            });
        } else {
            tabViewResults.getColumns().remove(tabColCRPResult);
        }

        //========================== Установка результатов теста Чувствительность ==============================================
        if (spendStartTest().containsValue(true)) {

            //Если есть результаты теста на самоход активной энергии в прямом напралении
            if (spendStartTest().get(0)) {
                tabColStartAPPls = new TableColumn<>("А.Э.+");

                tabColStartAPPls.setStyle( "-fx-alignment: CENTER;");
                tabColStartAPPls.setEditable(true);
                tabColStartAPPls.setSortable(false);
                tabColStartAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                        SimpleStringProperty result = null;

                        if (meter.getStartTestAPPls() == null) {
                            result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                        } else if (meter.getStartTestAPPls()) {
                            result = new SimpleStringProperty("ГОДЕН");
                        } else if (!meter.getStartTestAPPls()) {
                            result = new SimpleStringProperty("ПРОВАЛИЛ");
                        }
                        return result;
                    }
                });

                ObservableList<String> startResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

                tabColStartAPPls.setCellFactory(ComboBoxTableCell.forTableColumn(startResult));

                tabColStartAPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                    TablePosition<Meter, String> pos = event.getTablePosition();

                    String result = event.getNewValue();

                    int row = pos.getRow();
                    Meter meter = event.getTableView().getItems().get(row);

                    switch (result) {
                        case "НЕ ПРОВОДИЛОСЬ":
                            meter.setStartTestAPPls(null);
                            break;
                        case "ГОДЕН":
                            meter.setStartTestAPPls(true);
                            break;
                        case "ПРОВАЛИЛ":
                            meter.setStartTestAPPls(false);
                            break;
                    }
                });

                tabColStartResult.getColumns().add(tabColStartAPPls);
            }

            //Если есть результаты теста на самоход активной энергии в обратном напралении
            if (spendStartTest().get(1)) {
                tabColStartAPMns = new TableColumn<>("А.Э.-");

                tabColStartAPMns.setStyle( "-fx-alignment: CENTER;");
                tabColStartAPMns.setEditable(true);
                tabColStartAPMns.setSortable(false);

                tabColStartAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                        SimpleStringProperty result = null;

                        if (meter.getStartTestAPMns() == null) {
                            result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                        } else if (meter.getStartTestAPMns()) {
                            result = new SimpleStringProperty("ГОДЕН");
                        } else if (!meter.getStartTestAPMns()) {
                            result = new SimpleStringProperty("ПРОВАЛИЛ");
                        }
                        return result;
                    }
                });

                ObservableList<String> startResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

                tabColStartAPMns.setCellFactory(ComboBoxTableCell.forTableColumn(startResult));

                tabColStartAPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                    TablePosition<Meter, String> pos = event.getTablePosition();

                    String result = event.getNewValue();

                    int row = pos.getRow();
                    Meter meter = event.getTableView().getItems().get(row);

                    switch (result) {
                        case "НЕ ПРОВОДИЛОСЬ":
                            meter.setStartTestAPMns(null);
                            break;
                        case "ГОДЕН":
                            meter.setStartTestAPMns(true);
                            break;
                        case "ПРОВАЛИЛ":
                            meter.setStartTestAPMns(false);
                            break;
                    }
                });
                tabColStartResult.getColumns().add(tabColStartAPMns);
            }

            //Если есть результаты теста на самоход реактивной энергии в прямом напралении
            if (spendStartTest().get(2)) {
                tabColStartRPPls = new TableColumn<>("Р.Э.+");

                tabColStartRPPls.setStyle( "-fx-alignment: CENTER;");
                tabColStartRPPls.setEditable(true);
                tabColStartRPPls.setSortable(false);

                tabColStartRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                        SimpleStringProperty result = null;

                        if (meter.getStartTestRPPls() == null) {
                            result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                        } else if (meter.getStartTestRPPls()) {
                            result = new SimpleStringProperty("ГОДЕН");
                        } else if (!meter.getStartTestRPPls()) {
                            result = new SimpleStringProperty("ПРОВАЛИЛ");
                        }
                        return result;
                    }
                });

                ObservableList<String> startResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

                tabColStartRPPls.setCellFactory(ComboBoxTableCell.forTableColumn(startResult));

                tabColStartRPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                    TablePosition<Meter, String> pos = event.getTablePosition();

                    String result = event.getNewValue();

                    int row = pos.getRow();
                    Meter meter = event.getTableView().getItems().get(row);

                    switch (result) {
                        case "НЕ ПРОВОДИЛОСЬ":
                            meter.setStartTestRPPls(null);
                            break;
                        case "ГОДЕН":
                            meter.setStartTestRPPls(true);
                            break;
                        case "ПРОВАЛИЛ":
                            meter.setStartTestRPPls(false);
                            break;
                    }
                });
                tabColStartResult.getColumns().add(tabColStartRPPls);
            }

            //Если есть результаты теста на самоход реактивной энергии в прямом напралении
            if (spendStartTest().get(3)) {
                tabColStartRPMns = new TableColumn<>("Р.Э.-");

                tabColStartRPMns.setStyle( "-fx-alignment: CENTER;");
                tabColStartRPMns.setEditable(true);
                tabColStartRPMns.setSortable(false);

                tabColStartRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                        SimpleStringProperty result = null;

                        if (meter.getStartTestRPMns() == null) {
                            result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                        } else if (meter.getStartTestRPMns()) {
                            result = new SimpleStringProperty("ГОДЕН");
                        } else if (!meter.getStartTestRPMns()) {
                            result = new SimpleStringProperty("ПРОВАЛИЛ");
                        }
                        return result;
                    }
                });

                ObservableList<String> startResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

                tabColStartRPMns.setCellFactory(ComboBoxTableCell.forTableColumn(startResult));

                tabColStartRPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                    TablePosition<Meter, String> pos = event.getTablePosition();

                    String result = event.getNewValue();

                    int row = pos.getRow();
                    Meter meter = event.getTableView().getItems().get(row);

                    switch (result) {
                        case "НЕ ПРОВОДИЛОСЬ":
                            meter.setStartTestRPMns(null);
                            break;
                        case "ГОДЕН":
                            meter.setStartTestRPMns(true);
                            break;
                        case "ПРОВАЛИЛ":
                            meter.setStartTestRPMns(false);
                            break;
                    }
                });
                tabColStartResult.getColumns().add(tabColStartRPMns);
            }
        } else {
            tabViewResults.getColumns().remove(tabColStartResult);
        }

        //================================ Точность хода часов ===============================
        if (spendRTCTest()) {
            tabColRTCResult.setStyle( "-fx-alignment: CENTER;");

            tabColRTCResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                    Meter meter = param.getValue();

                    SimpleStringProperty result = null;

                    if (meter.getRTCTest() == null) {
                        result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                    } else if (meter.getRTCTest()) {
                        result = new SimpleStringProperty("ГОДЕН");
                    } else if (!meter.getRTCTest()) {
                        result = new SimpleStringProperty("ПРОВАЛИЛ");
                    }
                    return result;
                }
            });

            ObservableList<String> RTCResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

            tabColRTCResult.setCellFactory(ComboBoxTableCell.forTableColumn(RTCResult));

            tabColRTCResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                TablePosition<Meter, String> pos = event.getTablePosition();

                String result = event.getNewValue();

                int row = pos.getRow();
                Meter meter = event.getTableView().getItems().get(row);

                switch (result) {
                    case "НЕ ПРОВОДИЛОСЬ":
                        meter.setRTCTest(null);
                        break;
                    case "ГОДЕН":
                        meter.setRTCTest(true);
                        break;
                    case "ПРОВАЛИЛ":
                        meter.setRTCTest(false);
                        break;
                }
            });
        } else {
            tabViewResults.getColumns().remove(tabColRTCResult);
        }

        //================================ Счётный механизм ===============================
        if (spendConstantTest().containsValue(true)) {

            //Если есть результаты теста на самоход активной энергии в прямом напралении
            if (spendConstantTest().get(0)) {
                tabColConstantAPPls = new TableColumn<>("А.Э.+");

                tabColConstantAPPls.setStyle( "-fx-alignment: CENTER;");
                tabColConstantAPPls.setEditable(true);
                tabColConstantAPPls.setSortable(false);

                tabColConstantAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                        SimpleStringProperty result = null;

                        if (meter.getConstantTestAPPls() == null) {
                            result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                        } else if (meter.getConstantTestAPPls()) {
                            result = new SimpleStringProperty("ГОДЕН");
                        } else if (!meter.getConstantTestAPPls()) {
                            result = new SimpleStringProperty("ПРОВАЛИЛ");
                        }
                        return result;
                    }
                });

                ObservableList<String> constantResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

                tabColConstantAPPls.setCellFactory(ComboBoxTableCell.forTableColumn(constantResult));

                tabColConstantAPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                    TablePosition<Meter, String> pos = event.getTablePosition();

                    String result = event.getNewValue();

                    int row = pos.getRow();
                    Meter meter = event.getTableView().getItems().get(row);

                    switch (result) {
                        case "НЕ ПРОВОДИЛОСЬ":
                            meter.setConstantTestAPPls(null);
                            break;
                        case "ГОДЕН":
                            meter.setConstantTestAPPls(true);
                            break;
                        case "ПРОВАЛИЛ":
                            meter.setConstantTestAPPls(false);
                            break;
                    }
                });

                tabColConstantResult.getColumns().add(tabColConstantAPPls);
            }

            //Если есть результаты теста на самоход активной энергии в обратном напралении
            if (spendConstantTest().get(1)) {
                tabColConstantAPMns = new TableColumn<>("А.Э.-");

                tabColConstantAPMns.setStyle( "-fx-alignment: CENTER;");
                tabColConstantAPMns.setEditable(true);
                tabColConstantAPMns.setSortable(false);

                tabColConstantAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                        SimpleStringProperty result = null;

                        if (meter.getConstantTestAPMns() == null) {
                            result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                        } else if (meter.getConstantTestAPMns()) {
                            result = new SimpleStringProperty("ГОДЕН");
                        } else if (!meter.getConstantTestAPMns()) {
                            result = new SimpleStringProperty("ПРОВАЛИЛ");
                        }
                        return result;
                    }
                });

                ObservableList<String> constantResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

                tabColConstantAPMns.setCellFactory(ComboBoxTableCell.forTableColumn(constantResult));

                tabColConstantAPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                    TablePosition<Meter, String> pos = event.getTablePosition();

                    String result = event.getNewValue();

                    int row = pos.getRow();
                    Meter meter = event.getTableView().getItems().get(row);

                    switch (result) {
                        case "НЕ ПРОВОДИЛОСЬ":
                            meter.setConstantTestAPMns(null);
                            break;
                        case "ГОДЕН":
                            meter.setConstantTestAPMns(true);
                            break;
                        case "ПРОВАЛИЛ":
                            meter.setConstantTestAPMns(false);
                            break;
                    }
                });
                tabColConstantResult.getColumns().add(tabColConstantAPMns);
            }

            //Если есть результаты теста на самоход реактивной энергии в прямом напралении
            if (spendConstantTest().get(2)) {
                tabColConstantRPPls = new TableColumn<>("Р.Э.+");

                tabColConstantRPPls.setStyle( "-fx-alignment: CENTER;");
                tabColConstantRPPls.setEditable(true);
                tabColConstantRPPls.setSortable(false);

                tabColConstantRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                        SimpleStringProperty result = null;

                        if (meter.getConstantTestRPPls() == null) {
                            result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                        } else if (meter.getConstantTestRPPls()) {
                            result = new SimpleStringProperty("ГОДЕН");
                        } else if (!meter.getConstantTestRPPls()) {
                            result = new SimpleStringProperty("ПРОВАЛИЛ");
                        }
                        return result;
                    }
                });

                ObservableList<String> constantResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

                tabColConstantRPPls.setCellFactory(ComboBoxTableCell.forTableColumn(constantResult));

                tabColConstantRPPls.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                    TablePosition<Meter, String> pos = event.getTablePosition();

                    String result = event.getNewValue();

                    int row = pos.getRow();
                    Meter meter = event.getTableView().getItems().get(row);

                    switch (result) {
                        case "НЕ ПРОВОДИЛОСЬ":
                            meter.setConstantTestRPPls(null);
                            break;
                        case "ГОДЕН":
                            meter.setConstantTestRPPls(true);
                            break;
                        case "ПРОВАЛИЛ":
                            meter.setConstantTestRPPls(false);
                            break;
                    }
                });
                tabColConstantResult.getColumns().add(tabColConstantRPPls);
            }

            //Если есть результаты теста на самоход реактивной энергии в прямом напралении
            if (spendConstantTest().get(3)) {
                tabColConstantRPMns = new TableColumn<>("Р.Э.-");

                tabColConstantRPMns.setStyle( "-fx-alignment: CENTER;");
                tabColConstantRPMns.setEditable(true);
                tabColConstantRPMns.setSortable(false);

                tabColConstantRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                        Meter meter = param.getValue();

                        SimpleStringProperty result = null;

                        if (meter.getConstantTestRPMns() == null) {
                            result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                        } else if (meter.getConstantTestRPMns()) {
                            result = new SimpleStringProperty("ГОДЕН");
                        } else if (!meter.getConstantTestRPMns()) {
                            result = new SimpleStringProperty("ПРОВАЛИЛ");
                        }
                        return result;
                    }
                });

                ObservableList<String> constantResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

                tabColConstantRPMns.setCellFactory(ComboBoxTableCell.forTableColumn(constantResult));

                tabColConstantRPMns.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                    TablePosition<Meter, String> pos = event.getTablePosition();

                    String result = event.getNewValue();

                    int row = pos.getRow();
                    Meter meter = event.getTableView().getItems().get(row);

                    switch (result) {
                        case "НЕ ПРОВОДИЛОСЬ":
                            meter.setConstantTestRPMns(null);
                            break;
                        case "ГОДЕН":
                            meter.setConstantTestRPMns(true);
                            break;
                        case "ПРОВАЛИЛ":
                            meter.setConstantTestRPMns(false);
                            break;
                    }
                });
                tabColConstantResult.getColumns().add(tabColConstantRPMns);
            }
        } else {
            tabViewResults.getColumns().remove(tabColConstantResult);
        }

        //======================== Проверка изоляции ===========================
        if (spendInsulationTest()) {
            tabColInsulationResult.setStyle( "-fx-alignment: CENTER;");

            tabColInsulationResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                    Meter meter = param.getValue();

                    SimpleStringProperty result = null;

                    if (meter.getInsulationTest() == null) {
                        result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                    } else if (meter.getInsulationTest()) {
                        result = new SimpleStringProperty("ГОДЕН");
                    } else if (!meter.getInsulationTest()) {
                        result = new SimpleStringProperty("ПРОВАЛИЛ");
                    }
                    return result;
                }
            });

            ObservableList<String> insulationResult = FXCollections.observableArrayList(properties.getProperty("restMeterResults").split(", "));

            tabColInsulationResult.setCellFactory(ComboBoxTableCell.forTableColumn(insulationResult));

            tabColInsulationResult.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
                TablePosition<Meter, String> pos = event.getTablePosition();

                String result = event.getNewValue();

                int row = pos.getRow();
                Meter meter = event.getTableView().getItems().get(row);

                switch (result) {
                    case "НЕ ПРОВОДИЛОСЬ":
                        meter.setInsulationTest(null);
                        break;
                    case "ГОДЕН":
                        meter.setInsulationTest(true);
                        break;
                    case "ПРОВАЛИЛ":
                        meter.setInsulationTest(false);
                        break;
                }
            });
        } else {
            tabViewResults.getColumns().remove(tabColInsulationResult);
        }

        //======================== Внешний вид ===========================
        if (spendAppearensTest()) {
            tabColApperianceResult.setStyle( "-fx-alignment: CENTER;");

            tabColApperianceResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                    Meter meter = param.getValue();

                    SimpleStringProperty result = null;

                    if (meter.getAppearensTest() == null) {
                        result = new SimpleStringProperty("НЕ ПРОВОДИЛОСЬ");
                    } else if (meter.getAppearensTest()) {
                        result = new SimpleStringProperty("ГОДЕН");
                    } else if (!meter.getAppearensTest()) {
                        result = new SimpleStringProperty("ПРОВАЛИЛ");
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

                switch (result) {
                    case "НЕ ПРОВОДИЛОСЬ":
                        meter.setAppearensTest(null);
                        break;
                    case "ГОДЕН":
                        meter.setAppearensTest(true);
                        break;
                    case "ПРОВАЛИЛ":
                        meter.setAppearensTest(false);
                        break;
                }
            });
        } else {
            tabViewResults.getColumns().remove(tabColApperianceResult);
        }

        //tabColResultVerification.setCellValueFactory(new PropertyValueFactory<>("finalAllTestResult"));

//        if (tabColStartAPPls != null) {
//            tabColStartAPPls.setCellValueFactory(new PropertyValueFactory<>("startTestAPPls"));
//        }
//
//        if (tabColStartAPMns != null) {
//            tabColStartAPMns.setCellValueFactory(new PropertyValueFactory<>("startTestAPMns"));
//        }
//
//        if (tabColStartRPPls != null) {
//            tabColStartRPPls.setCellValueFactory(new PropertyValueFactory<>("startTestRPPls"));
//        }
//
//        if (tabColStartRPMns != null) {
//            tabColStartRPMns.setCellValueFactory(new PropertyValueFactory<>("startTestRPMns"));
//        }

//        tabColRTCResult.setCellValueFactory(new PropertyValueFactory<>("RTCTest"));
//        tabColInsulationResult.setCellValueFactory(new PropertyValueFactory<>("insulationTest"));
//        tabColApperianceResult.setCellValueFactory(new PropertyValueFactory<>("appearensTest"));

//        if (tabColConstantAPPls != null) {
//            tabColConstantAPPls.setCellValueFactory(new PropertyValueFactory<>("constantTestAPPls"));
//        }
//
//        if (tabColConstantAPMns != null) {
//            tabColConstantAPMns.setCellValueFactory(new PropertyValueFactory<>("constantTestAPMns"));
//        }
//
//        if (tabColConstantRPPls != null) {
//            tabColConstantRPPls.setCellValueFactory(new PropertyValueFactory<>("constantTestRPPls"));
//        }
//
//        if (tabColConstantRPMns != null) {
//            tabColConstantRPMns.setCellValueFactory(new PropertyValueFactory<>("constantTestRPMns"));
//        }

        tabViewResults.setItems(FXCollections.observableArrayList(meterList));
    }

    //Проводился ли тест на самоход?
    private boolean spendCreepTest() {
        for (Meter meter : meterList) {
            if (meter.getCreepTest() != null) {
                return true;
            }
        }
        //return false;
        return true;
    }

    //Проводился ли тест на Чувствительность
    private Map<Integer, Boolean> spendStartTest() {
        Map<Integer, Boolean> mapResultStart = new HashMap<>(4);
//        mapResultStart.put(0, false);
//        mapResultStart.put(1, false);
//        mapResultStart.put(2, false);
//        mapResultStart.put(3, false);
        mapResultStart.put(0, true);
        mapResultStart.put(1, true);
        mapResultStart.put(2, true);
        mapResultStart.put(3, true);

        for (Meter meter : meterList) {
            if (meter.getStartTestAPPls() != null) {
                mapResultStart.put(0, true);
                break;
            }
        }

        for (Meter meter : meterList) {
            if (meter.getStartTestAPMns() != null) {
                mapResultStart.put(1, true);
                break;
            }
        }

        for (Meter meter : meterList) {
            if (meter.getStartTestRPPls() != null) {
                mapResultStart.put(2, true);
                break;
            }
        }

        for (Meter meter : meterList) {
            if (meter.getStartTestRPMns() != null) {
                mapResultStart.put(3, true);
                break;
            }
        }

        return mapResultStart;
    }

    //Проводился ли тест Точность хода часов
    private boolean spendRTCTest() {
        for (Meter meter : meterList) {
            if (meter.getRTCTest() != null) {
                return true;
            }
        }
        //return false;
        return true;
    }

    //Проводился ли тест Проверка счётного механизма
    private Map<Integer, Boolean> spendConstantTest() {
        Map<Integer, Boolean> mapResultStart = new HashMap<>(4);
//        mapResultStart.put(0, false);
//        mapResultStart.put(1, false);
//        mapResultStart.put(2, false);
//        mapResultStart.put(3, false);

        mapResultStart.put(0, true);
        mapResultStart.put(1, true);
        mapResultStart.put(2, true);
        mapResultStart.put(3, true);

        for (Meter meter : meterList) {
            if (meter.getConstantTestAPPls() != null) {
                mapResultStart.put(0, true);
                break;
            }
        }

        for (Meter meter : meterList) {
            if (meter.getConstantTestAPMns() != null) {
                mapResultStart.put(1, true);
                break;
            }
        }

        for (Meter meter : meterList) {
            if (meter.getConstantTestRPPls() != null) {
                mapResultStart.put(2, true);
                break;
            }
        }

        for (Meter meter : meterList) {
            if (meter.getConstantTestRPMns() != null) {
                mapResultStart.put(3, true);
                break;
            }
        }

        return mapResultStart;
    }

    //Проводился ли тест проверка изоляции
    private boolean spendInsulationTest() {
        for (Meter meter : meterList) {
            if (meter.getInsulationTest() != null) {
                return true;
            }
        }
//        return false;
        return true;
    }

    //Проводился ли тест проверка внешнего вида
    private boolean spendAppearensTest() {
        for (Meter meter : meterList) {
            if (meter.getAppearensTest() != null) {
                return true;
            }
        }
        //return false;
        return true;
    }


    public void setMeterList(List<Meter> meterList) {
        this.meterList = meterList;
    }

    public void setTestErrorTableFrameController(TestErrorTableFrameController testErrorTableFrameController) {
        this.testErrorTableFrameController = testErrorTableFrameController;
    }
}
