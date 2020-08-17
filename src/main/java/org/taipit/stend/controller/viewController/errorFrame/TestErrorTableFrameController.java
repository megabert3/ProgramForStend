package org.taipit.stend.controller.viewController.errorFrame;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.viewController.errorFrame.refMeter.OnePhaseStendRefParamController;
import org.taipit.stend.controller.viewController.errorFrame.refMeter.StendRefParametersForFrame;
import org.taipit.stend.controller.viewController.errorFrame.refMeter.ThreePhaseStendRefParamController;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.controller.viewController.SaveResultsTestFrame;
import org.taipit.stend.controller.viewController.YesOrNoFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;
import org.taipit.stend.model.metodics.MethodicForThreePhaseStend;
import org.taipit.stend.model.metodics.Metodic;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestErrorTableFrameController {

    private static Button btnStopStatic;

    private StendDLLCommands stendDLLCommands;

    private static StendRefParametersForFrame stendRefParametersForFrame;

    private Stage refMeterStage;

    private boolean twoCircut;

    //Исполняемая команда
    private Commands command;

    private List<Meter> listMetersForTest;

    private Metodic methodicForStend;

    private String mode;

    //Список команд
    private ObservableList<Commands> commandsAPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> commandsAPMns = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> commandsRPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> commandsRPMns = FXCollections.observableArrayList(new ArrayList<>());

    //Список TableView c погрешностями
    private List<TableView<Meter.CommandResult>> tabViewErrorsList = new ArrayList<>();

    private double Imax;

    private double Ib;

    private double Un;

    private double Fn;

    //Время для теста Самоход ГОСТ активная энергия
    private long timeToCreepTestGOSTAP;

    //Время для теста Самоход ГОСТ реактивная энергия
    private long timeToCreepTestGOSTRP;

    //Время для теста Чувствительность ГОСТ активная энергия
    private long timeToStartTestGOSTAP;

    //Время для теста Чувствительность ГОСТ реактивная энергия
    private long timeToStartTestGOSTRP;

    //Тип измерительного элемента счётчика шунт/трансформатор
    private boolean typeOfMeasuringElementShunt;

    private int constantMeterAP;

    private int constantMeterRP;

    private double accuracyClassAP;

    private double accuracyClassRP;

    private Thread automaticTestThread = new Thread();

    private Thread manualTestThread = new Thread();

    private Thread UnomThread = new Thread();

    private boolean startUnTest = false;

    //Для блокировки кнопок управления
    public static SimpleBooleanProperty blockBtns = new SimpleBooleanProperty(false);

    //Для блокировки кнопок управления
    public static SimpleBooleanProperty blockTypeEnergyAndDirectionBtns = new SimpleBooleanProperty(false);

    //Лист с командами для установки слушателя
    private ObservableList<Commands> selectedCommand;

    //Слушатель для таблицы с командами для автоматического теста
    private ListChangeListener<Commands> automaticListChangeListener = new ListChangeListener<Commands>() {
        @Override
        public void onChanged(Change<? extends Commands> c) {
            automaticTestThread.interrupt();

            automaticTestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            refreshRefMeterParameters();
                            blockBtns.setValue(true);
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            startAutomaticTest();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } catch (ConnectForStendExeption e) {
                        e.printStackTrace();
                        ConsoleHelper.infoException("Потеряна связь с установкой");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                blockBtns.setValue(false);
                                tglBtnAuto.setSelected(false);
                                blockTypeEnergyAndDirectionBtns.setValue(false);
                                selectedCommand.removeListener(automaticListChangeListener);
                            }
                        });
                    }
                }
            });
            automaticTestThread.start();
        }
    };

    //Слушатель для таблицы с командами для ручного теста
    private ListChangeListener<Commands> manualListChangeListener = new ListChangeListener<Commands>() {
        @Override
        public void onChanged(Change<? extends Commands> c) {
            manualTestThread.interrupt();

            manualTestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            refreshRefMeterParameters();
                            blockBtns.setValue(true);
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            startManualTest();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (ConnectForStendExeption e) {
                        e.printStackTrace();
                        ConsoleHelper.infoException("Потеряна связь с установкой");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                blockBtns.setValue(false);
                                tglBtnManualMode.setSelected(false);
                                blockTypeEnergyAndDirectionBtns.setValue(false);
                                selectedCommand.removeListener(manualListChangeListener);
                            }
                        });
                    }
                }
            });
            manualTestThread.start();
        }
    };

    @FXML
    ToggleButton refMeterParam;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Commands> tabViewTestPoints = new TableView<>();

    @FXML
    private Pane checBoxePane;

    @FXML
    private CheckBox checkBoxDisableAll;

    @FXML
    private TableColumn<Commands, String> tabColTestPoints;

    @FXML
    private TableColumn<Commands, Boolean> tabColTestPointsDis;

    @FXML
    private Pane paneErrors;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnStop;

    @FXML
    private ToggleButton tglBtnAuto;

    @FXML
    private ToggleButton tglBtnManualMode;

    @FXML
    private ToggleButton tglBtnUnom;

    @FXML
    private ToggleButton tglBtnAPPls;

    @FXML
    private ToggleButton tglBtnAPMns;

    @FXML
    private ToggleButton tglBtnRPPls;

    @FXML
    private ToggleButton tglBtnRPMns;

    @FXML
    private Label txtLabInom;

    @FXML
    private Label txtLabFn;

    @FXML
    private Label txtLabImax;

    @FXML
    private Label txtLabUn;

    @FXML
    private Label txtLabAccuracyСlass;

    @FXML
    private Label txtLabTypeCircuit;

    @FXML
    private Label txtLabDate;

    @FXML
    private SplitPane splPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Pane buttonPane;

    @FXML
    private Pane energyAndDirectionBtnsPane;

    @FXML
    private AnchorPane tabViewCommandsPane;

    @FXML
    void initialize() {

        btnStopStatic = btnStop;

        selectedCommand = tabViewTestPoints.getSelectionModel().getSelectedItems();

        blockBtns.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            buttonPane.setCursor(Cursor.WAIT);
                            tglBtnUnom.setDisable(true);
                            tglBtnManualMode.setDisable(true);
                            tglBtnAuto.setDisable(true);
                            btnStop.setDisable(true);
                            tabViewCommandsPane.setCursor(Cursor.WAIT);
                            tabViewTestPoints.setMouseTransparent(true);

                        }
                    });
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            buttonPane.setCursor(Cursor.DEFAULT);
                            tglBtnUnom.setDisable(false);
                            tglBtnManualMode.setDisable(false);
                            tglBtnAuto.setDisable(false);
                            btnStop.setDisable(false);
                            tabViewCommandsPane.setCursor(Cursor.DEFAULT);
                            tabViewTestPoints.setMouseTransparent(false);

                        }
                    });
                }
            }
        });

        blockTypeEnergyAndDirectionBtns.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            energyAndDirectionBtnsPane.setDisable(true);
                        }
                    });
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            energyAndDirectionBtnsPane.setDisable(false);
                        }
                    });
                }
            }
        });

        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                new Thread(new Task() {
                    @Override
                    protected Object call() throws Exception {
                        blockBtns.setValue(true);

                        if (manualTestThread.isAlive()) {
                            manualTestThread.interrupt();
                        }

                        if (automaticTestThread.isAlive()) {
                            automaticTestThread.interrupt();
                        }

                        selectedCommand.removeListener(manualListChangeListener);
                        selectedCommand.removeListener(automaticListChangeListener);

                        if (UnomThread.isAlive() || startUnTest) {
                            UnomThread.interrupt();
                            startUnTest = false;
                        }

                        refreshRefMeterParameters();

                        try {
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

                            TestErrorTableFrameController.refreshRefMeterParameters();

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    tglBtnManualMode.setSelected(false);
                                    tglBtnAuto.setSelected(false);
                                    tglBtnUnom.setSelected(false);
                                }
                            });

                            blockBtns.setValue(false);
                            blockTypeEnergyAndDirectionBtns.setValue(false);

                        }catch (ConnectForStendExeption e) {
                            e.printStackTrace();
                            ConsoleHelper.infoException("Потеряна связь с утановкой");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    tglBtnManualMode.setSelected(false);
                                    tglBtnAuto.setSelected(false);
                                    tglBtnUnom.setSelected(false);
                                    blockBtns.setValue(false);
                                    blockTypeEnergyAndDirectionBtns.setValue(false);
                                }
                            });
                        }
                        return null;
                    }
                }).start();
            }
        });

        checBoxePane.toFront();
    }

    @FXML
    void actionEventSaveExit(ActionEvent event) {

        if (event.getSource() == btnSave) {

            if (blockTypeEnergyAndDirectionBtns.getValue() || startUnTest) {
                ConsoleHelper.infoException("Нельзя сохранить результаты во время теста");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/saveResultsTest.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            SaveResultsTestFrame saveResultsTestFrame = fxmlLoader.getController();
            saveResultsTestFrame.setTestErrorTableFrameController(this);
            saveResultsTestFrame.initAllColums();

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Сохранение результата");
            stage.setScene(new Scene(root));
            stage.show();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/yesOrNoFrame.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    YesOrNoFrameController yesOrNoFrameController = fxmlLoader.getController();
                    yesOrNoFrameController.setExitSaveResultFrameWithoutSaving(true);
                    yesOrNoFrameController.setStageSaveResultTest(stage);
                    yesOrNoFrameController.getQuestionTxt().setText("Вы уверены, что хотите выйти \nбез сохранения результатов теста?");
                    yesOrNoFrameController.getQuestionTxt().setLayoutX(165);
                    yesOrNoFrameController.getQuestionTxt().setLayoutY(30);

                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setTitle("Сохранение результата");
                    stage.setScene(new Scene(root));
                    stage.show();
                }
            });

            txtLabFn.getScene().getWindow().hide();
            refMeterStage.hide();
        }

        if (event.getSource() == btnExit) {
            if (blockTypeEnergyAndDirectionBtns.getValue() || startUnTest) {
                ConsoleHelper.infoException("Невозможно выйти во время теста");
            } else {

                Boolean answer = ConsoleHelper.yesOrNoFrame("Сохранение результатов", "Желаете сохранить результаты теста?");

                if (answer != null) {

                    if (answer) {
                        btnSave.fire();
                    } else {
                        Stage testErrorTableFrameControllerStage = (Stage) btnExit.getScene().getWindow();
                        refMeterStage.close();
                        testErrorTableFrameControllerStage.close();
                    }
                }
            }
        }
    }

    @FXML
    void refMeterParamAction(ActionEvent event) {
        if (event.getSource() == refMeterParam) {
            if (refMeterParam.isSelected()) {
                refMeterStage.show();
            } else {
                refMeterStage.hide();
            }
        }
    }

    @FXML
    void actionEventTestControl(ActionEvent event) {
        //------------------------------------------------------------------------------------------------
        //Логика работы автоматического режима работы
        if (event.getSource() == tglBtnAuto) {

            if (automaticTestThread.isAlive()) {
                tglBtnAuto.setSelected(true);
                return;
            } else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        startUnTest = false;
                        blockBtns.setValue(true);
                        blockTypeEnergyAndDirectionBtns.setValue(true);

                        if (manualTestThread.isAlive()) {
                            manualTestThread.interrupt();
                            selectedCommand.removeListener(manualListChangeListener);
                        }

                        if (UnomThread.isAlive()) {
                            UnomThread.interrupt();
                        }

                        tglBtnUnom.setSelected(false);
                        tglBtnManualMode.setSelected(false);
                        tglBtnAuto.setSelected(true);

                        selectedCommand.addListener(automaticListChangeListener);

                        automaticTestThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    try {
                                        startAutomaticTest();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } catch (ConnectForStendExeption e) {
                                    e.printStackTrace();
                                    ConsoleHelper.infoException("Потеряна связь с установкой");
                                    blockBtns.setValue(false);
                                    tglBtnAuto.setSelected(false);
                                    blockTypeEnergyAndDirectionBtns.setValue(false);
                                    selectedCommand.removeListener(automaticListChangeListener);
                                }
                            }
                        });
                        automaticTestThread.start();
                    }
                });
            }
        }

        //------------------------------------------------------------------------------------------------
        //Логика работы ручного режима работы
        if (event.getSource() == tglBtnManualMode) {

            if (manualTestThread.isAlive()) {
                tglBtnManualMode.setSelected(true);
                return;
            } else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        blockBtns.setValue(true);
                        blockTypeEnergyAndDirectionBtns.setValue(true);
                        startUnTest = false;

                        if (automaticTestThread.isAlive()) {
                            automaticTestThread.interrupt();
                            selectedCommand.removeListener(automaticListChangeListener);
                        }

                        if (UnomThread.isAlive()) {
                            UnomThread.interrupt();
                        }

                        tglBtnUnom.setSelected(false);
                        tglBtnAuto.setSelected(false);
                        tglBtnManualMode.setSelected(true);

                        selectedCommand.addListener(manualListChangeListener);

                        manualTestThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    try {
                                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                                        startManualTest();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } catch (ConnectForStendExeption e) {
                                    e.printStackTrace();
                                    ConsoleHelper.infoException("Потеряна связь с установкой");
                                    blockBtns.setValue(false);
                                    tglBtnManualMode.setSelected(false);
                                    selectedCommand.removeListener(manualListChangeListener);
                                    blockTypeEnergyAndDirectionBtns.setValue(false);
                                }
                            }
                        });
                        manualTestThread.start();
                    }
                });
            }
        }

        //------------------------------------------------------------------------------------------------
        //Логика работы подачи напряжения
        if (event.getSource() == tglBtnUnom) {

            if (startUnTest) {
                tglBtnUnom.setSelected(true);
            } else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        blockBtns.setValue(true);

                        startUnTest = true;

                        if (automaticTestThread.isAlive()) {
                            automaticTestThread.interrupt();
                            selectedCommand.removeListener(automaticListChangeListener);
                        }

                        if (manualTestThread.isAlive()) {
                            manualTestThread.interrupt();
                            selectedCommand.removeListener(manualListChangeListener);
                        }

                        tglBtnManualMode.setSelected(false);
                        tglBtnAuto.setSelected(false);
                        tglBtnUnom.setSelected(true);

                        UnomThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    try {
                                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

                                        startUn();

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        startUnTest = false;
                                    }
                                } catch (ConnectForStendExeption e) {
                                    e.printStackTrace();
                                    ConsoleHelper.infoException("Потеряна связь с установкой");
                                    blockBtns.setValue(false);
                                    startUnTest = false;
                                    tglBtnUnom.setSelected(false);
                                }
                            }
                        });
                        UnomThread.start();
                    }
                });
            }
        }
    }

    //Блок команд для старта автоматического теста
    private void startAutomaticTest() throws ConnectForStendExeption, InterruptedException {
        int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();

        command = tabViewTestPoints.getSelectionModel().getSelectedItem();

        if (!command.isActive()) {
            for (int index = i + 1; index < tabViewTestPoints.getItems().size(); index++) {
                if (tabViewTestPoints.getItems().get(index).isActive()) {
                    tabViewTestPoints.getSelectionModel().select(index);
                    for (TableView<Meter.CommandResult> errorsView : tabViewErrorsList) {
                        errorsView.getSelectionModel().select(index);
                    }
                    break;
                }

                if (index == tabViewTestPoints.getItems().size() - 1 && !tabViewTestPoints.getItems().get(index).isActive()) {
                    btnStop.fire();
                }
            }
        } else {
            if (command instanceof ErrorCommand) {

                initAllParamForErrorCommand((ErrorCommand) command, i);
                command.execute();
            }else if (command instanceof CreepCommand) {

                initAllParamForCreepCommand((CreepCommand) command, i);
                command.execute();
            }else if (command instanceof StartCommand) {

                initAllParamForStartCommand((StartCommand) command, i);
                command.execute();
            }else if (command instanceof RTCCommand) {

                initAllParamForRTCCommand((RTCCommand) command, i);
                command.execute();
            }else if (command instanceof ConstantCommand) {

                initAllParamForConstantCommand((ConstantCommand) command, i);
                command.execute();
            }else if (command instanceof ImbalansUCommand) {

                initAllParamForImbCommand((ImbalansUCommand) command, i);
                command.execute();
            }

            if (i == tabViewTestPoints.getItems().size() - 1) {
                btnStop.fire();
            } else {
                for (int index = i + 1; index < tabViewTestPoints.getItems().size(); index++) {
                    if (tabViewTestPoints.getItems().get(index).isActive()) {
                        tabViewTestPoints.getSelectionModel().select(index);
                        for (TableView<Meter.CommandResult > errorsView : tabViewErrorsList) {
                            errorsView.getSelectionModel().select(index);
                        }
                        break;
                    }

                    if (index == tabViewTestPoints.getItems().size() - 1 && !tabViewTestPoints.getItems().get(index).isActive()) {
                        btnStop.fire();
                    }
                }
            }
        }
    }

    //Общая команда для старта ручного теста
    private void startManualTest() throws ConnectForStendExeption, InterruptedException {

        int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();

        command = tabViewTestPoints.getSelectionModel().getSelectedItem();

        if (command instanceof ErrorCommand) {

            initAllParamForErrorCommand((ErrorCommand) command, i);

            command.executeForContinuousTest();
        }else if (command instanceof CreepCommand) {

            initAllParamForCreepCommand((CreepCommand) command, i);

            command.executeForContinuousTest();
        }else if (command instanceof StartCommand) {

            initAllParamForStartCommand((StartCommand) command, i);

            command.executeForContinuousTest();
        }else if (command instanceof RTCCommand) {

            initAllParamForRTCCommand((RTCCommand) command, i);

            command.executeForContinuousTest();

        }else if (command instanceof ConstantCommand) {

            initAllParamForConstantCommand((ConstantCommand) command, i);
            command.executeForContinuousTest();
        }else if (command instanceof ImbalansUCommand) {

            initAllParamForImbCommand((ImbalansUCommand) command, i);
            command.executeForContinuousTest();
        }
    }

    //Общая команда для старта напряжения
    private void startUn () throws ConnectForStendExeption, InterruptedException {

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (methodicForStend instanceof MethodicForThreePhaseStend) {
                if (!stendDLLCommands.getUI(0, Un, 0, Fn, 0, 0, 100.0, 0, "H", "1.0")) throw new ConnectForStendExeption();
            } else {
                if (!stendDLLCommands.getUIWithPhase(1, Un, 0, Fn, 0, 0, 0, 0, 100, 0, "H", "1.0")) throw new ConnectForStendExeption();
            }

        } else {
            if (!stendDLLCommands.getUI(1, Un, 0, Fn, 0, 0, 100.0, 0, "H", "1.0")) throw new ConnectForStendExeption();
        }

        blockBtns.setValue(false);
    }

    //Инициализирует параметры необходимые для снятия погрешности в точке
    private void initAllParamForErrorCommand(ErrorCommand errorCommand, int index){
        errorCommand.setStendDLLCommands(stendDLLCommands);
        errorCommand.setRatedVolt(Un);
        errorCommand.setIb(Ib);
        errorCommand.setImax(Imax);
        errorCommand.setRatedFreq(Fn);
        errorCommand.setIndex(index);
        errorCommand.setMeterForTestList(listMetersForTest);
    }

    //Инициализирует параметры необходимые для команды самохода
    private void initAllParamForCreepCommand(CreepCommand creepCommand, int index){
        creepCommand.setStendDLLCommands(stendDLLCommands);
        creepCommand.setRatedVolt(Un);
        creepCommand.setRatedFreq(Fn);
        creepCommand.setIndex(index);
        creepCommand.setMeterList(listMetersForTest);

        if (creepCommand.isGostTest()) {
            if (creepCommand.getChannelFlag() < 2) {
                creepCommand.setUserTimeTest(timeToCreepTestGOSTAP);
            } else {
                creepCommand.setUserTimeTest(timeToCreepTestGOSTRP);
            }
        }
    }

    //Инициализирует параметры необходимые для команды чувствительность
    private void initAllParamForStartCommand(StartCommand startCommand, int index) {
        startCommand.setStendDLLCommands(stendDLLCommands);
        startCommand.setRatedFreq(Fn);
        startCommand.setRatedVolt(Un);
        startCommand.setIndex(index);
        startCommand.setRatedCurr(Ib);
        startCommand.setMeterList(listMetersForTest);

        if (startCommand.isGostTest()) {
            if (startCommand.getChannelFlag() < 2) {
                startCommand.setUserTimeTest(timeToStartTestGOSTAP);
            } else {
                startCommand.setUserTimeTest(timeToStartTestGOSTRP);
            }
        }
    }

    //Инициализирует параметры необходимые для команды чувстви
    private void initAllParamForRTCCommand(RTCCommand rTCCommand, int index) {
        rTCCommand.setStendDLLCommands(stendDLLCommands);
        rTCCommand.setRatedVolt(Un);
        rTCCommand.setIndex(index);
        rTCCommand.setMeterList(listMetersForTest);
    }

    //Инициализирует параметры необходимые для команды чувстви
    private void initAllParamForConstantCommand(ConstantCommand constantCommand, int index) {
        constantCommand.setStendDLLCommands(stendDLLCommands);
        constantCommand.setRatedVolt(Un);
        constantCommand.setRatedCurr(Ib);
        constantCommand.setIndex(index);
        constantCommand.setMeterForTestList(listMetersForTest);
    }

    private void initAllParamForImbCommand(ImbalansUCommand imbalansUCommand, int index){
        imbalansUCommand.setStendDLLCommands(stendDLLCommands);
        imbalansUCommand.setRatedVolt(Un);
        imbalansUCommand.setIb(Ib);
        imbalansUCommand.setImax(Imax);
        imbalansUCommand.setRatedFreq(Fn);
        imbalansUCommand.setIndex(index);
        imbalansUCommand.setMeterForTestList(listMetersForTest);
    }

    @FXML
    void checBoxAllDisAction(ActionEvent event) {
        //Устанавливает значение для всех чек боксов сразу
        //AP+
        if (event.getSource() == checkBoxDisableAll) {
            if (checkBoxDisableAll.isSelected()) {
                for (Commands commsnd : tabViewTestPoints.getItems()) {
                    commsnd.setActive(true);

                }
                tabViewTestPoints.refresh();
            } else {
                for (Commands commsnd : tabViewTestPoints.getItems()) {
                    commsnd.setActive(false);
                }
                tabViewTestPoints.refresh();
            }
        }
    }

    @FXML
    void actionEventSwithEnergyPane(ActionEvent event) {
        if (event.getSource() == tglBtnAPPls) {
            tglBtnAPPls.setSelected(true);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);

            initErrorPaneForAPPls();

        } else if (event.getSource() == tglBtnAPMns) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(true);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);

            initErrorPaneForAPMns();

        } else if (event.getSource() == tglBtnRPPls) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(true);
            tglBtnRPMns.setSelected(false);

            initErrorPaneForRPPls();

        } else if (event.getSource() == tglBtnRPMns) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(true);

            initErrorPaneForRPMns();
        }
    }

    //Установка значений таблицы погрешности AP+
    private void initErrorPaneForAPPls() {
        setTabColTestPoints(commandsAPPls);
        setErrorTablesAPPls();
        setlectFirsCommand();
        checkBoxDisableAll.setSelected(true);
    }

    //Установка значений таблицы погрешности AP-
    private void initErrorPaneForAPMns() {
        setTabColTestPoints(commandsAPMns);
        setErrorTablesAPMns();
        setlectFirsCommand();
        checkBoxDisableAll.setSelected(true);
    }

    //Установка значений таблицы погрешности RP-
    private void initErrorPaneForRPPls() {
        setTabColTestPoints(commandsRPPls);
        setErrorTablesRPPls();
        setlectFirsCommand();
        checkBoxDisableAll.setSelected(true);
    }

    //Установка значений таблицы погрешности RP+
    private void initErrorPaneForRPMns() {
        setTabColTestPoints(commandsRPMns);
        setErrorTablesRPMns();
        setlectFirsCommand();
        checkBoxDisableAll.setSelected(true);
    }

    //Инициализирую таблицу с командами
    private void setTabColTestPoints(List<Commands> commandsList) {
        tabColTestPoints.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPoints.setSortable(false);
        tabViewTestPoints.setItems(FXCollections.observableArrayList(commandsList));
        tabViewTestPoints.refresh();
    }

    private void setErrorTablesAPPls() {
        for (int i = 0; i < tabViewErrorsList.size(); i++) {
            tabViewErrorsList.get(i).setItems(FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPPls()));
        }
    }

    private void setErrorTablesAPMns() {
        for (int i = 0; i < tabViewErrorsList.size(); i++) {
            tabViewErrorsList.get(i).setItems(FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPMns()));
        }
    }

    private void setErrorTablesRPPls() {
        for (int i = 0; i < tabViewErrorsList.size(); i++) {
            tabViewErrorsList.get(i).setItems(FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPPls()));
        }
    }

    private void setErrorTablesRPMns() {
        for (int i = 0; i < tabViewErrorsList.size(); i++) {
            tabViewErrorsList.get(i).setItems(FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPMns()));
        }
    }

    private void setlectFirsCommand() {
        if (tabViewTestPoints.getItems().size() != 0) {
            tabViewTestPoints.getSelectionModel().select(0);

            for (TableView<Meter.CommandResult> tableViewError : tabViewErrorsList) {
                tableViewError.getSelectionModel().select(0);
            }
        }
    }

    public void myInitTestErrorTableFrame() {
        //Реакция на отключение или включение точки испытания
        Callback<TableColumn.CellDataFeatures<Commands, Boolean>, ObservableValue<Boolean>> tabColCellData =
                new Callback<TableColumn.CellDataFeatures<Commands, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Commands, Boolean> param) {
                Commands command = param.getValue();
                SimpleBooleanProperty simpleBooleanProperty = new SimpleBooleanProperty(command.isActive());

                simpleBooleanProperty.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        command.setActive(newValue);
                    }
                });

                return simpleBooleanProperty;
            }
        };

        Callback<TableColumn<Commands, Boolean>, TableCell<Commands, Boolean>> tabColCell =
                new Callback<TableColumn<Commands, Boolean>, TableCell<Commands, Boolean>>() {
            @Override
            public TableCell<Commands, Boolean> call(TableColumn<Commands, Boolean> p) {
                CheckBoxTableCell<Commands, Boolean> cell = new CheckBoxTableCell<Commands, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };

        //Установка чек боксов для отключения или включения точки
        tabViewTestPoints.setEditable(true);
        tabViewTestPoints.setPlaceholder(new Label("Нет точек"));
        tabColTestPointsDis.setCellValueFactory(tabColCellData);
        tabColTestPointsDis.setCellFactory(tabColCell);
        tabColTestPointsDis.setSortable(false);

        //Установка команд для таблицы с выбором команды для испытания
        char[] idChar;
        Commands cloneCommand;
        try {
            if (stendDLLCommands instanceof ThreePhaseStend) {
                String typeCircut = ConsoleHelper.properties.getProperty("threePhaseStand.lastTypeCircuit");

                if (methodicForStend instanceof MethodicForOnePhaseStend) {
                    //AP+
                    //Добавляю точки ErrorCommand
                    for (Commands command : methodicForStend.getCommandsMap().get(0)) {
                        idChar = command.getId().toCharArray();

                        if (idChar[2] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPPls.add(cloneCommand);
                        }
                    }

                    //Добавляю точки ErrorCommand с окна влияния
                    for (Commands command : methodicForStend.getSaveInflListForCollumAPPls()) {
                        idChar = command.getId().toCharArray();

                        if (idChar[4] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPPls.add(cloneCommand);
                        }
                    }

                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(0)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(0);
                        commandsAPPls.add(cloneCommand);
                    }


                    //AP-
                    //Добавляю точки ErrorCommand
                    for (Commands command : methodicForStend.getCommandsMap().get(1)) {
                        idChar = command.getId().toCharArray();

                        if (idChar[2] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPMns.add(cloneCommand);
                        }
                    }

                    //Добавляю точки ErrorCommand с окна влияния
                    for (Commands command : methodicForStend.getSaveInflListForCollumAPMns()) {
                        idChar = command.getId().toCharArray();

                        if (idChar[4] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPMns.add(cloneCommand);
                        }
                    }

                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(1)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(0);
                        commandsAPMns.add(cloneCommand);
                    }

                    //RP+
                    //Добавляю точки ErrorCommand
                    for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                        idChar = command.getId().toCharArray();

                        if (idChar[2] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPPls.add(cloneCommand);
                        }
                    }

                    //Добавляю точки ErrorCommand с окна влияния
                    for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                        idChar = command.getId().toCharArray();

                        if (idChar[4] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPPls.add(cloneCommand);
                        }
                    }

                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(7);
                        commandsRPPls.add(cloneCommand);
                    }

                    //RP-
                    //Добавляю точки ErrorCommand
                    for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                        idChar = command.getId().toCharArray();

                        if (idChar[2] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPMns.add(cloneCommand);
                        }
                    }

                    //Добавляю точки ErrorCommand с окна влияния
                    for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                        idChar = command.getId().toCharArray();

                        if (idChar[4] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPMns.add(cloneCommand);
                        }
                    }

                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(7);
                        commandsRPMns.add(cloneCommand);
                    }

                } else {
                    //3P4W, 3P3W, 3P3W 90 R.P., 3P3W 60 R.P.
                    if (typeCircut.equals("3P4W")) {

                        for (Commands command : methodicForStend.getCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumAPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPPls.add(cloneCommand);
                        }


                        for (Commands command : methodicForStend.getCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumAPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPMns.add(cloneCommand);
                        }


                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPPls.add(cloneCommand);
                        }


                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPMns.add(cloneCommand);
                        }

                    } else if (typeCircut.equals("3P3W")) {

                        for (Commands command : methodicForStend.getCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumAPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPPls.add(cloneCommand);
                        }


                        for (Commands command : methodicForStend.getCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumAPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPMns.add(cloneCommand);
                        }


                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPPls.add(cloneCommand);
                        }


                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPMns.add(cloneCommand);
                        }

                    } else if (typeCircut.equals("3P3W 90 R.P.")) {
                        //RP+
                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPPls.add(cloneCommand);
                        }

                        //RP-
                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPMns.add(cloneCommand);
                        }

                    } else if (typeCircut.equals("3P3W 60 R.P.")) {
                        //RP+
                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPPls.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPPls.add(cloneCommand);
                        }

                        //RP-
                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPMns.add(cloneCommand);
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPMns.add(cloneCommand);
                        }
                    }
                }

                //Однофазный стенд
            } else {

                if (methodicForStend instanceof ThreePhaseStend) {

                    //AP+
                    //Добавляю точки ErrorCommand
                    for (Commands command : methodicForStend.getCommandsMap().get(0)) {
                        idChar = command.getId().toCharArray();

                        if (idChar[2] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPPls.add(cloneCommand);
                        }
                    }

                    //Добавляю точки ErrorCommand с окна влияния
                    for (Commands command : methodicForStend.getSaveInflListForCollumAPPls()) {
                        idChar = command.getId().toCharArray();

                        if (idChar[4] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPPls.add(cloneCommand);
                        }
                    }

                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(0)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(0);
                        commandsAPPls.add(cloneCommand);
                    }

                    //AP-
                    //Добавляю точки ErrorCommand
                    for (Commands command : methodicForStend.getCommandsMap().get(1)) {
                        idChar = command.getId().toCharArray();

                        if (idChar[2] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPMns.add(cloneCommand);
                        }
                    }

                    //Добавляю точки ErrorCommand с окна влияния
                    for (Commands command : methodicForStend.getSaveInflListForCollumAPMns()) {
                        idChar = command.getId().toCharArray();

                        if (idChar[4] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPMns.add(cloneCommand);
                        }
                    }

                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(1)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(0);
                        commandsAPMns.add(cloneCommand);
                    }

                    //RP+
                    //Добавляю точки ErrorCommand
                    for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                        idChar = command.getId().toCharArray();

                        if (idChar[2] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPPls.add(cloneCommand);
                        }
                    }

                    //Добавляю точки ErrorCommand с окна влияния
                    for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                        idChar = command.getId().toCharArray();

                        if (idChar[4] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPPls.add(cloneCommand);
                        }
                    }

                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(7);
                        commandsRPPls.add(cloneCommand);
                    }

                    //RP-
                    //Добавляю точки ErrorCommand
                    for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                        idChar = command.getId().toCharArray();

                        if (idChar[2] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPMns.add(cloneCommand);
                        }
                    }

                    //Добавляю точки ErrorCommand с окна влияния
                    for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                        idChar = command.getId().toCharArray();

                        if (idChar[4] == 'H') {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPMns.add(cloneCommand);
                        }
                    }

                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(7);
                        commandsRPMns.add(cloneCommand);
                    }

                } else {

                    if (twoCircut) {

                        //AP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(0)) {
                            idChar = command.getId().toCharArray();

                            if (idChar[2] != 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(0);
                                commandsAPPls.add(cloneCommand);
                            }
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumAPPls()) {
                            idChar = command.getId().toCharArray();

                            if (idChar[4] != 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(0);
                                commandsAPPls.add(cloneCommand);
                            }
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPPls.add(cloneCommand);
                        }

                        //AP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(1)) {
                            idChar = command.getId().toCharArray();

                            if (idChar[2] != 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(0);
                                commandsAPMns.add(cloneCommand);
                            }
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumAPMns()) {
                            idChar = command.getId().toCharArray();

                            if (idChar[4] != 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(0);
                                commandsAPMns.add(cloneCommand);
                            }
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPMns.add(cloneCommand);
                        }

                        //RP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            idChar = command.getId().toCharArray();

                            if (idChar[2] != 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(7);
                                commandsRPPls.add(cloneCommand);
                            }
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            idChar = command.getId().toCharArray();

                            if (idChar[4] != 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(7);
                                commandsRPPls.add(cloneCommand);
                            }
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPPls.add(cloneCommand);
                        }

                        //RP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            idChar = command.getId().toCharArray();

                            if (idChar[2] != 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(7);
                                commandsRPMns.add(cloneCommand);
                            }
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            idChar = command.getId().toCharArray();

                            if (idChar[4] != 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(7);
                                commandsRPMns.add(cloneCommand);
                            }
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPMns.add(cloneCommand);
                        }

                    } else {

                        //AP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(0)) {
                            idChar = command.getId().toCharArray();

                            if (idChar[2] == 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(0);
                                commandsAPPls.add(cloneCommand);
                            }
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumAPPls()) {
                            idChar = command.getId().toCharArray();

                            if (idChar[4] == 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(0);
                                commandsAPPls.add(cloneCommand);
                            }
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPPls.add(cloneCommand);
                        }

                        //AP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(1)) {
                            idChar = command.getId().toCharArray();

                            if (idChar[2] == 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(0);
                                commandsAPMns.add(cloneCommand);
                            }
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumAPMns()) {
                            idChar = command.getId().toCharArray();

                            if (idChar[4] == 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(0);
                                commandsAPMns.add(cloneCommand);
                            }
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(0);
                            commandsAPMns.add(cloneCommand);
                        }

                        //RP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            idChar = command.getId().toCharArray();

                            if (idChar[2] == 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(7);
                                commandsRPPls.add(cloneCommand);
                            }
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            idChar = command.getId().toCharArray();

                            if (idChar[4] == 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(7);
                                commandsRPPls.add(cloneCommand);
                            }
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPPls.add(cloneCommand);
                        }

                        //RP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            idChar = command.getId().toCharArray();

                            if (idChar[2] == 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(7);
                                commandsRPMns.add(cloneCommand);
                            }
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            idChar = command.getId().toCharArray();

                            if (idChar[4] == 'H') {
                                cloneCommand = command.clone();
                                cloneCommand.setPhase(7);
                                commandsRPMns.add(cloneCommand);
                            }
                        }

                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPMns.add(cloneCommand);
                        }
                    }
                }
            }
        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        initErrorsForMeters();

        //В зависимости от количества счётчиков инициализирую поля для отображения погрешности
        if (listMetersForTest.size() <= 12) {

            for (int i = 0; i < listMetersForTest.size(); i++) {
                //Создаю колонки
                addTableViewAndCollumnInErrorPane(i);
            }

            //Устанавливаю размер колонок под окно погрешности
            paneErrors.widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    double widthColumn = ((Double) newValue) / tabViewErrorsList.size();

                    for (int i = 0; i < listMetersForTest.size(); i++) {
                        tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                        tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);
                        tabViewErrorsList.get(i).setLayoutX(widthColumn * i);
                    }
                }
            });

            paneErrors.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    for (TableView<Meter.CommandResult> tableViewError : tabViewErrorsList) {
                        tableViewError.setPrefHeight((Double) newValue);
                    }
                }
            });

        } else if (listMetersForTest.size() <= 24) {

            if (listMetersForTest.size() % 2 == 0) {

                for (int j = 0; j < 2; j++) {
                    if (j == 0) {

                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i);
                        }
                    } else {

                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2);
                        }
                    }
                }

                paneErrors.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        double widthColumn = ((Double) newValue) / (listMetersForTest.size() / 2);

                        for (int i = 0; i < tabViewErrorsList.size() / 2; i++) {
                            tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                            tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);

                            tabViewErrorsList.get(i).setLayoutX(widthColumn * i);
                        }

                        for (int i = tabViewErrorsList.size() / 2; i < tabViewErrorsList.size(); i++) {
                            tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                            tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);

                            tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - (tabViewErrorsList.size() / 2)));
                        }
                    }
                });

                paneErrors.heightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        double height = ((Double) newValue) / 2;

                        for (int i = 0; i < tabViewErrorsList.size() / 2; i++) {
                            tabViewErrorsList.get(i).setPrefHeight(height);
                        }

                        for (int i = tabViewErrorsList.size() / 2; i < tabViewErrorsList.size(); i++) {
                            tabViewErrorsList.get(i).setPrefHeight(height);

                            tabViewErrorsList.get(i).setLayoutY(height);
                        }
                    }
                });

            } else {

                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        for (int i = 0; i < (listMetersForTest.size() / 2) + 1; i++) {
                            addTableViewAndCollumnInErrorPane(i);
                        }

                    } else {
                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2 + 1);

                        }
                    }
                }

                paneErrors.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        int meters = listMetersForTest.size();

                        double widthColumn = ((Double) newValue) / (meters / 2 + 1);

                        for (int i = 0; i < meters / 2 + 1; i++) {
                            tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                            tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);

                            tabViewErrorsList.get(i).setLayoutX(widthColumn * i);
                        }

                        widthColumn = ((Double) newValue) / (meters / 2);

                        for (int i = (meters / 2) + 1; i < tabViewErrorsList.size(); i++) {
                            tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                            tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);

                            tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - ((meters / 2) + 1)));
                        }

                    }
                });

                paneErrors.heightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        double height = ((Double) newValue) / 2;

                        for (int i = 0; i < tabViewErrorsList.size(); i++) {
                            tabViewErrorsList.get(i).setPrefHeight(height);
                        }

                        for (int i = tabViewErrorsList.size() / 2  + 1; i < tabViewErrorsList.size(); i++) {
                            tabViewErrorsList.get(i).setLayoutY(height);
                        }
                    }
                });
            }
        }

        //Если выбираю точку испытания, то должна выставляться фокусировка и на панели с погрешностью
        if (listMetersForTest.size() <= 12) {
            ObservableList<TablePosition> tablePositions = tabViewTestPoints.getSelectionModel().getSelectedCells();

            tablePositions.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {
                    int i = tabViewTestPoints.getSelectionModel().getFocusedIndex();

                    for (TableView<Meter.CommandResult> tableViewError : tabViewErrorsList) {

                        if (!tableViewError.getSelectionModel().isSelected(i)) {
                            tableViewError.getFocusModel().focus(i);
                            tableViewError.getSelectionModel().select(i);
                        }
                    }
                }
            });

        } else {

            ObservableList<TablePosition> tablePositions = tabViewTestPoints.getSelectionModel().getSelectedCells();

            tablePositions.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {
                    int i = tabViewTestPoints.getSelectionModel().getFocusedIndex();

                    for (TableView<Meter.CommandResult> tableViewError : tabViewErrorsList) {

                        if (!tableViewError.getSelectionModel().isSelected(i)) {
                            tableViewError.getFocusModel().focus(i);
                            tableViewError.getSelectionModel().select(i);
                            tableViewError.scrollTo(i - 5);
                        }
                    }
                }
            });
        }

        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewErrorsList.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsAPPls = tabViewErrorsList.get(i).getSelectionModel().getSelectedCells();

            tableErrorPositionsAPPls.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {

                    int index = tabViewTestPoints.getSelectionModel().getSelectedIndex();

                    for (TableView<Meter.CommandResult> tableViewError : tabViewErrorsList) {
                        if (!tableViewError.getSelectionModel().isSelected(index)) {
                            tableViewError.getFocusModel().focus(index);
                            tableViewError.getSelectionModel().select(index);
                        }
                    }
                }
            });
        }

        if (commandsAPPls.size() != 0) {
            tglBtnAPPls.fire();
        } else if (commandsAPMns.size() != 0) {
            tglBtnAPMns.fire();
        } else if (commandsRPPls.size() != 0) {
            tglBtnRPPls.fire();
        } else if (commandsRPMns.size() != 0) {
            tglBtnRPMns.fire();
        } else {
            tglBtnAPPls.fire();
        }

        Stage testErrorTableFrameControllerStage = (Stage) btnExit.getScene().getWindow();

        testErrorTableFrameControllerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();

                if (blockTypeEnergyAndDirectionBtns.getValue() || startUnTest) {
                    ConsoleHelper.infoException("Нельзя выйти во время теста");
                    return;
                }

                Boolean answer = ConsoleHelper.yesOrNoFrame("Сохранение результатов", "Желаете сохранить результаты теста?");

                if (answer != null) {

                    if (answer) {
                        btnSave.fire();
                    } else {
                        Stage testErrorTableFrameControllerStage = (Stage) btnExit.getScene().getWindow();
                        refMeterStage.close();
                        testErrorTableFrameControllerStage.close();
                    }
                }
            }
        });

        //Загрузка окна с параметрами этелонного счётчика
        if (stendDLLCommands instanceof ThreePhaseStend) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/refParamFrames/threePhaseStendrefParamFrame.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Ошибка при загрузке окна.");
                    }

                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.getRoot());
                    stage.setScene(scene);

                    stendRefParametersForFrame = (ThreePhaseStendRefParamController) fxmlLoader.getController();
                    stendRefParametersForFrame.initRefType(stendDLLCommands);
                    stendRefParametersForFrame.addMovingActions();

                    stage.initStyle(StageStyle.TRANSPARENT);

                    refMeterStage = stage;

                    stage.hide();
                }
            });

        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/refParamFrames/onePhaseStendrefParamFrame.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Ошибка при загрузке окна.");
                    }

                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.getRoot());
                    stage.setTitle("Методики");
                    stage.setScene(scene);

                    stendRefParametersForFrame = (OnePhaseStendRefParamController) fxmlLoader.getController();
                    stendRefParametersForFrame.initRefType(stendDLLCommands);
                    stendRefParametersForFrame.addMovingActions();

                    stage.initStyle(StageStyle.TRANSPARENT);

                    refMeterStage = stage;

                    stage.hide();
                }
            });
        }
    }

    public static void refreshRefMeterParameters() throws InterruptedException {
        stendRefParametersForFrame.readParameters();
    }

    public static void transferParam(Commands command) {
        stendRefParametersForFrame.transferParameters(command);
    }

    public static StendRefParametersForFrame getRefMeterController() {
        return stendRefParametersForFrame.getStendRefParametersForFrame();
    }

    //Добавляет объект resultError к каждому счётчику необходимому для теста
    private void initErrorsForMeters() {

        //Инициализицрую константы активной и реактивной энергии
        constantMeterAP = Integer.parseInt(listMetersForTest.get(0).getConstantMeterAP());
        constantMeterRP = Integer.parseInt(listMetersForTest.get(0).getConstantMeterRP());

        //Инициирую время теста для Самохода и чувствительности по ГОСТУ
        intiTimeCRPSTATests();

        for (Meter meter : listMetersForTest) {

            for (Commands command : commandsAPPls) {
                meter.createError(command, 0, command.getId(), this);
            }

            for (Commands command : commandsAPMns) {
                meter.createError(command, 1, command.getId(), this);
            }

            for (Commands command : commandsRPPls) {
                meter.createError(command, 2, command.getId(), this);
            }

            for (Commands command : commandsRPMns) {
                meter.createError(command, 3, command.getId(), this);
            }
        }
    }

    private void addTableViewAndCollumnInErrorPane(int index) {

        //Настройка для отдельного поля счётчика изменения цвета погрешности после окончания теста
        Callback<TableColumn<Meter.CommandResult, String>, TableCell<Meter.CommandResult, String>> cellFactoryEndTest =
                new Callback<TableColumn<Meter.CommandResult, String>, TableCell<Meter.CommandResult, String>>() {
                    public TableCell call(TableColumn p) {
                        return new TableCell<Meter.CommandResult, String>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                char firstSymbol;

                                if (item == null || empty) {
                                    setText("");
                                } else {
                                    firstSymbol = item.charAt(0);

                                    if (firstSymbol == 'N') {
                                        setText(item.substring(1));

                                    } else if (firstSymbol == 'P') {
                                        setText(item.substring(1));
                                        setTextFill(Color.rgb(0, 105, 201));

                                    } else if (firstSymbol == 'F') {
                                        setText(item.substring(1));
                                        setTextFill(Color.rgb(245, 0, 0));
                                    }
                                }
                            }
                        };
                    }
                };

        TableView<Meter.CommandResult> tableView = new TableView<>();

        String styleErrorTableView = getClass().getClassLoader().getResource("styleCSS/testErrorTableFrame/tableViewErrors.css").toString();
        if (styleErrorTableView != null) {
            tableView.getStylesheets().add(styleErrorTableView);
        }

        TableColumn<Meter.CommandResult, String> column = new TableColumn<>("Место " + listMetersForTest.get(index).getId());
        column.setStyle("-fx-alignment: CENTER;");
        column.setCellValueFactory(new PropertyValueFactory<>("lastResultForTabView"));
        column.setSortable(false);
        column.setCellFactory(cellFactoryEndTest);
        tableView.getColumns().add(column);
        tableView.setPlaceholder(new Label("Нет точек"));
        paneErrors.getChildren().add(tableView);
        tabViewErrorsList.add(tableView);
    }

    //Находит все скрол бары
    public void initScrolBars() {
        ScrollBar verticalBarCommands;
        ScrollBar verticalBarErrorsFirst;
        ScrollBar verticalBarErrorsSecond;

        //Получаю скрол бары определённого окна
        //AP+
        verticalBarCommands = (ScrollBar) tabViewTestPoints.lookup(".scroll-bar:vertical");

        for (int i = tabViewErrorsList.size() - 1; i > 0; i--) {
            verticalBarErrorsFirst = (ScrollBar) tabViewErrorsList.get(i).lookup(".scroll-bar:vertical");
            verticalBarErrorsSecond = (ScrollBar) tabViewErrorsList.get(i - 1).lookup(".scroll-bar:vertical");

            bindScrolls(verticalBarErrorsFirst, verticalBarErrorsSecond);

            ScrollBar finalVerticalBarErrors1 = verticalBarErrorsFirst;
            verticalBarCommands.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    finalVerticalBarErrors1.valueProperty().setValue(newValue);
                }
            });
        }
    }

    //Делает проверку и привязывает скроллы друг к другу
    private void bindScrolls(ScrollBar verticalBarErrorFirst, ScrollBar verticalBarErrorsSecond) {
        if (verticalBarErrorFirst != null && verticalBarErrorsSecond != null) {
            verticalBarErrorFirst.valueProperty().bindBidirectional(verticalBarErrorsSecond.valueProperty());
        }
    }

    //Формула для расчёта времени теста на самоход по ГОСТУ
    private long initTimeForCreepTestGOST(int constMeterForTest) {
        int amountMeasElem;

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (methodicForStend instanceof MethodicForThreePhaseStend) {
                amountMeasElem = 3;
            } else {
                amountMeasElem = 1;
            }
        } else amountMeasElem = 1;

        double formulaResult = 600000000 / (constMeterForTest * amountMeasElem * Un * Imax);

        //Округляю результат до 3х знаков
        BigDecimal bigDecimalResult = new BigDecimal(String.valueOf(formulaResult)).setScale(3, BigDecimal.ROUND_CEILING);

        String[] timeArr = String.valueOf(bigDecimalResult).split("\\.");

        //Округляю значение после запятой до целых секунд
        BigDecimal bigDecimal = new BigDecimal(Integer.parseInt(timeArr[1]) * 0.06).setScale(0, BigDecimal.ROUND_CEILING);

        return  ((Integer.parseInt(timeArr[0]) * 60) + bigDecimal.intValue()) * 1000;
    }

    //Расчётная формула времени теста на чувствительность по ГОСТ
    private long initTimeForStartGOSTTest(double accuracyClass, int constMeterForTest) {
        int amountMeasElem;
        double ratedCurr;

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (methodicForStend instanceof MethodicForThreePhaseStend) {
                amountMeasElem = 3;
            } else {
                amountMeasElem = 1;
            }
        } else amountMeasElem = 1;

        if (!typeOfMeasuringElementShunt) {
            if (accuracyClass > 0.5) {
                ratedCurr = Ib * 0.002;
            } else {
                ratedCurr = Ib * 0.001;
            }
        } else {
            ratedCurr = Ib * 0.004;
        }

        double formulaResult = 2.3 * (60000 / (constMeterForTest * amountMeasElem * Un * ratedCurr));

        //Округляю результат до 3х знаков
        BigDecimal bigDecimalResult = new BigDecimal(String.valueOf(formulaResult)).setScale(3, BigDecimal.ROUND_CEILING);

        String[] timeArr = String.valueOf(bigDecimalResult).split("\\.");

        //Округляю значение после запятой до целых секунд
        BigDecimal bigDecimal = new BigDecimal(Integer.parseInt(timeArr[1]) * 0.06).setScale(0, BigDecimal.ROUND_CEILING);

        return ((Integer.parseInt(timeArr[0]) * 60) + bigDecimal.intValue()) * 1000;
    }

    private void intiTimeCRPSTATests() {
        //Время для самохода гостовские тесты
        timeToCreepTestGOSTAP = initTimeForCreepTestGOST(constantMeterAP);

        timeToCreepTestGOSTRP = initTimeForCreepTestGOST(constantMeterRP);

        timeToStartTestGOSTAP = initTimeForStartGOSTTest(accuracyClassAP, constantMeterAP);

        timeToStartTestGOSTRP = initTimeForStartGOSTTest(accuracyClassRP, constantMeterRP);
    }

    //=====================================================================================
    //get sets

    public Label getTxtLabUn() {
        return txtLabUn;
    }

    public Label getTxtLabInom() {
        return txtLabInom;
    }

    public Label getTxtLabImax() {
        return txtLabImax;
    }

    public Label getTxtLabFn() {
        return txtLabFn;
    }

    public Label getTxtLabTypeCircuit() {
        return txtLabTypeCircuit;
    }

    public Label getTxtLabAccuracyСlass() {
        return txtLabAccuracyСlass;
    }

    public Label getTxtLabDate() {
        return txtLabDate;
    }

    public long getTimeToCreepTestGOSTAP() {
        return timeToCreepTestGOSTAP;
    }

    public long getTimeToCreepTestGOSTRP() {
        return timeToCreepTestGOSTRP;
    }

    public long getTimeToStartTestGOSTAP() {
        return timeToStartTestGOSTAP;
    }

    public long getTimeToStartTestGOSTRP() {
        return timeToStartTestGOSTRP;
    }

    public void setListMetersForTest(List<Meter> listMetersForTest) {
        this.listMetersForTest = listMetersForTest;
    }

    public void setImax(double imax) {
        Imax = imax;
    }

    public void setIb(double ib) {
        Ib = ib;
    }

    public void setUn(double un) {
        Un = un;
    }

    public void setFn(double fn) {
        Fn = fn;
    }

    public void setMethodicForStend(Metodic methodicForStend) {
        this.methodicForStend = methodicForStend;
    }

    public void setTypeOfMeasuringElementShunt(boolean typeOfMeasuringElementShunt) {
        this.typeOfMeasuringElementShunt = typeOfMeasuringElementShunt;
    }

    public void setAccuracyClassAP(double accuracyClassAP) {
        this.accuracyClassAP = accuracyClassAP;
    }

    public void setAccuracyClassRP(double accuracyClassRP) {
        this.accuracyClassRP = accuracyClassRP;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public List<Meter> getListMetersForTest() {
        return listMetersForTest;
    }

    public void setTwoCircut(boolean twoCircut) {
        this.twoCircut = twoCircut;
    }

    public static Button getStaticBtnStop() {
        return btnStopStatic;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
