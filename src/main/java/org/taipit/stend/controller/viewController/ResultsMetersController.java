package org.taipit.stend.controller.viewController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.controller.EditResultsMetersController;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.viewController.YesOrNoFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.Frame;
import org.taipit.stend.helper.frameManager.FrameManager;
import org.taipit.stend.model.ExcelReport;
import org.taipit.stend.model.result.ResultsTest;

/**
 * @autor Albert Khalimov
 *
 * Данный класс является контроллером окна сохранённых результатов теста "resultsMetersFrame.fxml"
 *
 * Отвечает за отображение всех сохранённых результатов проведённых когда либо тестов.
 */
public class ResultsMetersController implements Frame {

    //Сериализованные результаты
    private ResultsTest resultsTest = ResultsTest.getResultsTestInstance();

    private Properties properties = ConsoleHelper.properties;

    //Варианты результатов
    private String[] resultMass = properties.getProperty("restMeterResults").split(", ");

    //для инициализации таблицы результатами
    private ObservableList<Meter> observableList = FXCollections.observableArrayList(resultsTest.getListAllResults());

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnPrintResult;

    @FXML
    private Button btnDrirectoryChooser;

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
    private TableColumn<Meter, String> tabColRelayResult;

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

    /**
     * Действие при удалении или редактировании результатов теста
     * @param event
     */
    @FXML
    void editDeleteAction(ActionEvent event) {
        //Если нажата кнопка редактировать
        if (event.getSource() == btnEdit) {
            //Если есть установлен пароль, то запрашиваю
            if (!ConsoleHelper.properties.getProperty("config").isEmpty()) {
                if (!ConsoleHelper.passwordFrame()) {
                    return;
                }
            }

            //Выбранные для редактировния счётчики
            List<Meter> listSelectedMeters = new ArrayList<>();

            ResultsTest results = ResultsTest.getResultsTestInstance();

            for (Integer index : tabViewResults.getSelectionModel().getSelectedIndices()) {
                listSelectedMeters.add(results.getListAllResults().get(index));
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/editResultsMetersFrame.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            EditResultsMetersController editResultsMetersController = fxmlLoader.getController();
            editResultsMetersController.setResultsMetersController(this);
            editResultsMetersController.setSelectedMetersForEdit(listSelectedMeters);
            editResultsMetersController.setIndexcesList(tabViewResults.getSelectionModel().getSelectedIndices());
            editResultsMetersController.initColumsEditFrame();

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Редактирование результатов");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }

        //Если нажата кнопка удалить
        if (event.getSource() == btnDelete) {
            if (!ConsoleHelper.properties.getProperty("config").isEmpty()) {
                if (!ConsoleHelper.passwordFrame()) {
                    return;
                }
            }

            if (ResultsTest.getResultsTestInstance().getListAllResults().isEmpty()) {
                ConsoleHelper.infoException("Нет результатов");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/yesOrNoFrame.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            YesOrNoFrameController yesOrNoFrameController = fxmlLoader.getController();
            yesOrNoFrameController.setResultsMeters(true);
            yesOrNoFrameController.setListIndexces(tabViewResults.getSelectionModel().getSelectedIndices());
            yesOrNoFrameController.getQuestionTxt().setText("Вы уверены, что хотите удалить\nвыбранные записи?");
            yesOrNoFrameController.getQuestionTxt().setLayoutX(165);
            yesOrNoFrameController.getQuestionTxt().setLayoutY(30);

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Удаление результатов");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            tabViewResults.getItems().setAll(FXCollections.observableArrayList(resultsTest.getListAllResults()));
        }

        //Если нажата кнопка создать отчёт
        if (event.getSource() == btnPrintResult) {
            List<Meter> listSelectedMeters = new ArrayList<>();

            ResultsTest results = ResultsTest.getResultsTestInstance();

            for (Integer index : tabViewResults.getSelectionModel().getSelectedIndices()) {
                listSelectedMeters.add(results.getListAllResults().get(index));
            }

            FrameManager.frameManagerInstance().printMeterResults(listSelectedMeters, btnPrintResult.getScene().getWindow());
        }

        //Если нажата кнопка выбора пути сохранения отчёта
        if (event.getSource() == btnDrirectoryChooser) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Путь сохранения отчёта");

            File newFile = directoryChooser.showDialog(btnDrirectoryChooser.getScene().getWindow());

            if (newFile != null) {
                ConsoleHelper.properties.setProperty("printReportPath", newFile.getAbsolutePath());

                ConsoleHelper.saveProperties();
            }
        }
    }

    @FXML
    void initialize() {
        tabColPosition.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabColPosition.setStyle( "-fx-alignment: CENTER;");

        tabColSerNo.setCellValueFactory(new PropertyValueFactory<>("serNoMeter"));
        tabColSerNo.setStyle( "-fx-alignment: CENTER;");

        tabColMeterModel.setCellValueFactory(new PropertyValueFactory<>("modelMeter"));
        tabColMeterModel.setStyle( "-fx-alignment: CENTER;");

        //Общий результат теста для счётчика
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
        tabColResultVerification.setStyle( "-fx-alignment: CENTER;");

        tabColDate.setCellValueFactory(new PropertyValueFactory<>("verificationDate"));
        tabColDate.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Самоход
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
        tabColCRPResult.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Чувствительность AP+
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

        //Результат теста Чувствительность AP-
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
        tabColStartAPMns.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Чувствительность RP+
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
        tabColStartRPPls.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Чувствительность RP-
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
        tabColStartRPMns.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Точность хода часов
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
        tabColRTCResult.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Проверка счётного механизма AP+
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
        tabColConstantAPPls.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Проверка счётного механизма AP-
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
        tabColConstantAPMns.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Проверка счётного механизма RP+
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
        tabColConstantRPPls.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Проверка счётного механизма RP-
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
        tabColConstantRPMns.setStyle("-fx-alignment: CENTER;");

        //Результат теста Проверка работоспособности реле
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
        tabColRelayResult.setStyle("-fx-alignment: CENTER;");

        //Результат теста Проверка изоляции
        tabColInsulationResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
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
        tabColInsulationResult.setStyle( "-fx-alignment: CENTER;");

        //Результат теста Внешний вид счётчика
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
        tabColApperianceResult.setStyle( "-fx-alignment: CENTER;");

        //Отображение температуры
        tabColTemperature.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        tabColTemperature.setStyle( "-fx-alignment: CENTER;");

        //Отображение влажности
        tabColHumidity.setCellValueFactory(new PropertyValueFactory<>("humidity"));
        tabColHumidity.setStyle( "-fx-alignment: CENTER;");

        //Оператор
        tabColOperator.setCellValueFactory(new PropertyValueFactory<>("operator"));
        tabColOperator.setStyle( "-fx-alignment: CENTER;");

        //Контроллер
        tabColController.setCellValueFactory(new PropertyValueFactory<>("controller"));
        tabColController.setStyle( "-fx-alignment: CENTER;");

        //Поверитель
        tabColWitnes.setCellValueFactory(new PropertyValueFactory<>("witness"));
        tabColWitnes.setStyle( "-fx-alignment: CENTER;");

        //Напряжение
        tabColUn.setCellValueFactory(new PropertyValueFactory<>("Un"));
        tabColUn.setStyle( "-fx-alignment: CENTER;");

        //Ток
        tabColInomImax.setCellValueFactory(new PropertyValueFactory<>("InomImax"));
        tabColInomImax.setStyle( "-fx-alignment: CENTER;");

        //Частота
        tabColFn.setCellValueFactory(new PropertyValueFactory<>("Fn"));
        tabColFn.setStyle( "-fx-alignment: CENTER;");

        //Постоянная счётчика Активная энергия
        tabColConstantMeterAPPls.setCellValueFactory(new PropertyValueFactory<>("constantMeterAP"));
        tabColConstantMeterAPPls.setStyle( "-fx-alignment: CENTER;");

        //Постоянная счётчика реактивная энергия
        tabColConstantMeterRPMns.setCellValueFactory(new PropertyValueFactory<>("constantMeterRP"));
        tabColConstantMeterRPMns.setStyle( "-fx-alignment: CENTER;");

        //Тип счётчика
        tabColTypeMeter.setCellValueFactory(new PropertyValueFactory<>("typeMeter"));
        tabColTypeMeter.setStyle( "-fx-alignment: CENTER;");

        //Тип измерительного элемента счётчика
        tabColTypeCurrentDetector.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result;

                if (meter.isTypeOfMeasuringElementShunt()) {
                    result = new SimpleStringProperty("Шунт");
                } else {
                    result = new SimpleStringProperty("Трансформатор");
                }
                return result;
            }
        });
        tabColTypeCurrentDetector.setStyle( "-fx-alignment: CENTER;");

        //Завод изготовитель
        tabColfactoryManufacturer.setCellValueFactory(new PropertyValueFactory<>("factoryManufacturer"));
        tabColfactoryManufacturer.setStyle( "-fx-alignment: CENTER;");

        //Номер партии
        tabColBatсhNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        tabColBatсhNo.setStyle( "-fx-alignment: CENTER;");

        tabViewResults.setItems(FXCollections.observableArrayList(observableList));

        tabViewResults.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public TableView<Meter> getTabViewResults() {
        return tabViewResults;
    }

    @Override
    public Stage getStage() {
        return (Stage) btnDelete.getScene().getWindow();
    }
}