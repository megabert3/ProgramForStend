package org.taipit.stend.controller.viewController;

import java.util.*;

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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.ConsoleHelper;

public class SaveResultsTestFrame {

    private TestErrorTableFrameController testErrorTableFrameController;

    private Properties properties = ConsoleHelper.properties;

    private String[] resultMass = properties.getProperty("restMeterResults").split(", ");

    private String[] meterModel = properties.getProperty("meterModel").split(", ");

    private String controller = properties.getProperty("lastController");
    private String operator = properties.getProperty("lastOperator");
    private String witnes = properties.getProperty("lastWitness");

    private String[] controllers = properties.getProperty("Controller").split(", ");
    private String[] operators = properties.getProperty("Operators").split(", ");
    private String[] witneses = properties.getProperty("Witness").split(", ");

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
    private Pane paneForTabView;

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
        Callback<TableColumn<Meter, String>, TableCell<Meter, String>> cellFactoryEndTest =
                new Callback<TableColumn<Meter, String>, TableCell<Meter, String>>() {
                    public TableCell call(TableColumn p) {
                        return new TableCell<Meter, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                char firstSymbol;

                                if (item == null || empty) {
                                    setText("");
                                } else {
                                    firstSymbol = item.charAt(0);

                                    if (firstSymbol == 'Н') {
                                        setText(item);

                                    } else if (firstSymbol == 'Г') {
                                        setText(item);
                                        setTextFill(Color.BLUE);

                                    } else if (firstSymbol == 'П') {
                                        setText(item);
                                        setTextFill(Color.RED);
                                    }
                                }
                            }
                        };
                    }
                };

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

        //tabColResultVerification.setCellFactory(cellFactoryEndTest);

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
        if (spendCreepTest()) {
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
        } else {
            tabViewResults.getColumns().remove(tabColCRPResult);
        }

        //========================== Установка результатов теста Чувствительность ==============================================
        if (spendStartTest().containsValue(true)) {

            //Если есть результаты теста на самоход активной энергии в прямом напралении
            if (spendStartTest().get(0)) {
                tabColStartAPPls = new TableColumn<>("А.Э.+");
                tabColStartAPPls.setPrefWidth(120);

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

                ObservableList<String> startResult = FXCollections.observableArrayList(resultMass);

                tabColStartAPPls.setCellFactory(ComboBoxTableCell.forTableColumn(startResult));

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

                tabColStartResult.getColumns().add(tabColStartAPPls);
            }

            //Если есть результаты теста на самоход активной энергии в обратном напралении
            if (spendStartTest().get(1)) {
                tabColStartAPMns = new TableColumn<>("А.Э.-");
                tabColStartAPMns.setPrefWidth(120);

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

                ObservableList<String> startResult = FXCollections.observableArrayList(resultMass);

                tabColStartAPMns.setCellFactory(ComboBoxTableCell.forTableColumn(startResult));

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
                tabColStartResult.getColumns().add(tabColStartAPMns);
            }

            //Если есть результаты теста на самоход реактивной энергии в прямом напралении
            if (spendStartTest().get(2)) {
                tabColStartRPPls = new TableColumn<>("Р.Э.+");
                tabColStartRPPls.setPrefWidth(120);

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

                ObservableList<String> startResult = FXCollections.observableArrayList(resultMass);

                tabColStartRPPls.setCellFactory(ComboBoxTableCell.forTableColumn(startResult));

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
                tabColStartResult.getColumns().add(tabColStartRPPls);
            }

            //Если есть результаты теста на самоход реактивной энергии в прямом напралении
            if (spendStartTest().get(3)) {
                tabColStartRPMns = new TableColumn<>("Р.Э.-");
                tabColStartRPMns.setPrefWidth(120);

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

                ObservableList<String> startResult = FXCollections.observableArrayList(resultMass);

                tabColStartRPMns.setCellFactory(ComboBoxTableCell.forTableColumn(startResult));

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
        } else {
            tabViewResults.getColumns().remove(tabColRTCResult);
        }

        //================================ Счётный механизм ===============================
        if (spendConstantTest().containsValue(true)) {

            //Если есть результаты теста на самоход активной энергии в прямом напралении
            if (spendConstantTest().get(0)) {
                tabColConstantAPPls = new TableColumn<>("А.Э.+");
                tabColConstantAPPls.setPrefWidth(120);

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

                ObservableList<String> constantResult = FXCollections.observableArrayList(resultMass);

                tabColConstantAPPls.setCellFactory(ComboBoxTableCell.forTableColumn(constantResult));

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

                tabColConstantResult.getColumns().add(tabColConstantAPPls);
            }

            //Если есть результаты теста на самоход активной энергии в обратном напралении
            if (spendConstantTest().get(1)) {
                tabColConstantAPMns = new TableColumn<>("А.Э.-");
                tabColConstantAPMns.setPrefWidth(120);

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

                ObservableList<String> constantResult = FXCollections.observableArrayList(resultMass);

                tabColConstantAPMns.setCellFactory(ComboBoxTableCell.forTableColumn(constantResult));

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
                tabColConstantResult.getColumns().add(tabColConstantAPMns);
            }

            //Если есть результаты теста на самоход реактивной энергии в прямом напралении
            if (spendConstantTest().get(2)) {
                tabColConstantRPPls = new TableColumn<>("Р.Э.+");
                tabColConstantRPPls.setPrefWidth(120);

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

                ObservableList<String> constantResult = FXCollections.observableArrayList(resultMass);

                tabColConstantRPPls.setCellFactory(ComboBoxTableCell.forTableColumn(constantResult));

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
                tabColConstantResult.getColumns().add(tabColConstantRPPls);
            }

            //Если есть результаты теста на самоход реактивной энергии в прямом напралении
            if (spendConstantTest().get(3)) {
                tabColConstantRPMns = new TableColumn<>("Р.Э.-");
                tabColConstantRPMns.setPrefWidth(120);

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

                ObservableList<String> constantResult = FXCollections.observableArrayList(resultMass);

                tabColConstantRPMns.setCellFactory(ComboBoxTableCell.forTableColumn(constantResult));

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
        } else {
            tabViewResults.getColumns().remove(tabColApperianceResult);
        }

        tabViewResults.setPrefWidth(paneForTabView.getPrefWidth());
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
        //return false;
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
