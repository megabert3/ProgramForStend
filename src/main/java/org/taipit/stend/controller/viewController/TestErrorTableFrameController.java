package org.taipit.stend.controller.viewController;

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.model.Methodic;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestErrorTableFrameController {

    StendDLLCommands stendDLLCommands;

    public static boolean interrupt;

    //Исполняемая команда
    Commands command;
    private List<Meter> listMetersForTest;

    private Methodic methodic;

    //Список команд
    private ObservableList<Commands> commandsAPPls;
    private ObservableList<Commands> commandsAPMns;
    private ObservableList<Commands> commandsRPPls;
    private ObservableList<Commands> commandsRPMns;

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

    //Тип сети трёхфазная или однофазная
    private boolean typeCircuitThreePhase;

    private int constantMeterAP;

    private int constantMeterRP;

    private double accuracyClassAP;

    private double accuracyClassRP;

    private String operator;

    private String controller;

    private String witness;

    //private ExecutorService executorService = newSingleThreadExecutor();

    private Thread thread = new Thread();

    private Thread automaticTestThread = new Thread();

    private Thread manualTestThread = new Thread();

    private Thread UnomThread = new Thread();

    @FXML
    private Button btnSave;

    @FXML
    private SplitPane splPane;

    @FXML
    private TableView<Commands> tabViewTestPoints;

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
    void actionEventSaveExit(ActionEvent event) {

        if (event.getSource() == btnSave) {
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
        }
    }

    @FXML
    void actionEventTestControl(ActionEvent event) {
        //------------------------------------------------------------------------------------------------
        //Логика работы автоматического режима работы
        if (event.getSource() == tglBtnAuto) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (manualTestThread.isAlive()) {
                        manualTestThread.interrupt();
                        tglBtnManualMode.setSelected(false);
                    }

                    if (UnomThread.isAlive()) {
                        UnomThread.interrupt();
                        tglBtnUnom.setSelected(false);
                    }

                    tglBtnAuto.setSelected(true);
                    tglBtnAuto.setDisable(true);

                    automaticTestThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                try {
                                    if (stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

                                    startAutomaticTest();

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            tglBtnAuto.setDisable(false);
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            tglBtnAuto.setDisable(false);
                                        }
                                    });
                                }
                            } catch (ConnectForStendExeption e) {
                                e.printStackTrace();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        ConsoleHelper.infoException("Потеряна связь с установкой");
                                        tglBtnAuto.setDisable(false);
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        //------------------------------------------------------------------------------------------------
        //Логика работы ручного режима работы
        if (event.getSource() == tglBtnManualMode) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (automaticTestThread.isAlive()) {
                        automaticTestThread.interrupt();
                        tglBtnAuto.setSelected(false);
                    }

                    if (UnomThread.isAlive()) {
                        UnomThread.interrupt();
                        tglBtnUnom.setSelected(false);
                    }

                    tglBtnManualMode.setSelected(true);
                    tglBtnManualMode.setDisable(true);

                    automaticTestThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                try {
                                    if (stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

                                    startManualTest();

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            tglBtnManualMode.setDisable(false);
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            tglBtnManualMode.setDisable(false);
                                        }
                                    });
                                }
                            } catch (ConnectForStendExeption e) {
                                e.printStackTrace();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        ConsoleHelper.infoException("Потеряна связь с установкой");
                                        tglBtnManualMode.setDisable(false);
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        //------------------------------------------------------------------------------------------------
        //Логика работы подачи напряжения
        if (event.getSource() == tglBtnUnom) {

            if (tglBtnUnom.isSelected()) {
                tglBtnUnom.setSelected(true);
                return;
            }

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (automaticTestThread.isAlive()) {
                        automaticTestThread.interrupt();
                        tglBtnAuto.setSelected(false);
                    }

                    if (manualTestThread.isAlive()) {
                        manualTestThread.interrupt();
                        tglBtnManualMode.setSelected(false);
                    }

                    tglBtnUnom.setSelected(true);
                    tglBtnUnom.setDisable(true);

                    UnomThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                try {
                                    if (stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

                                    startUn();

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            tglBtnUnom.setDisable(false);
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            tglBtnUnom.setDisable(false);
                                        }
                                    });
                                }
                            } catch (ConnectForStendExeption e) {
                                e.printStackTrace();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        ConsoleHelper.infoException("Потеряна связь с установкой");
                                        tglBtnUnom.setDisable(false);
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        //------------------------------------------------------------------------------------------------
        //Логика работы остановки теста
        if (event.getSource() == btnStop) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (manualTestThread.isAlive()) {
                        manualTestThread.interrupt();
                    }

                    if (automaticTestThread.isAlive()) {
                        automaticTestThread.interrupt();
                    }

                    if (UnomThread.isAlive()) {
                        UnomThread.interrupt();
                    }

                    tglBtnManualMode.setSelected(false);
                    tglBtnAuto.setSelected(false);
                    tglBtnUnom.setSelected(false);

                    stendDLLCommands.errorClear();
                    stendDLLCommands.powerOf();
                }
            });
        }
    }

    //Блок команд для старта автоматического теста
    private void startAutomaticTest() throws ConnectForStendExeption, InterruptedException {
        int phase;

        //Если выбрана панель AP+
        if (tglBtnAPPls.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 1;
            } else phase = 0;

            startTestOnSelectPane(phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

            //Если выбрана панель AP-
        } else if(tglBtnAPMns.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 1;
            } else phase = 0;

            startTestOnSelectPane(phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

            //Если выбрана панель RP+
        } else if(tglBtnRPPls.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 5;
            } else phase = 7;

           startTestOnSelectPane(phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);

            //Если выбрана панель RP-
        } else if(tglBtnRPMns.isSelected()) {

            if (typeCircuitThreePhase) {
                phase = 5;
            } else phase = 7;

           startTestOnSelectPane(phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);
        }
    }

    //Общая команда для старта ручного теста
    private void startManualTest() throws ConnectForStendExeption, InterruptedException {
        int phase;

        //Если выбрана панель AP+
        if (tglBtnAPPls.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 1;
            } else phase = 0;

           startContinuousTestOnSelectPane(phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

            //Если выбрана панель AP-
        } else if(tglBtnAPMns.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 1;
            } else phase = 0;

           startContinuousTestOnSelectPane(phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

            //Если выбрана панель RP+
        } else if(tglBtnRPPls.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 5;
            } else phase = 7;

            startContinuousTestOnSelectPane(phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);

            //Если выбрана панель RP-
        } else if(tglBtnRPMns.isSelected()) {

            if (typeCircuitThreePhase) {
                phase = 5;
            } else phase = 7;

            startContinuousTestOnSelectPane(phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);
        }
    }

    //Общая команда для старта напряжения
    private void startUn () throws ConnectForStendExeption, InterruptedException {

        int phase;

        if (typeCircuitThreePhase) {
            phase = 1;
        } else {
            phase = 0;
        }

        if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

        if (!stendDLLCommands.getUI(phase, Un, 0, Fn, 0, 0, 100.0, 0, "H", "1.0")) throw new ConnectForStendExeption();
    }

    //Старт автоматического теста в зависимости от выбранной панели (направления и типа энергии)
    private void startTestOnSelectPane(int phase, long timeCRPForGOST, long timeSTAForGOST)
            throws ConnectForStendExeption, InterruptedException {

//        ObservableList<Commands> comandsList = tabViewTestPoints.getSelectionModel().getSelectedItems();
//
//        comandsList.addListener(new ListChangeListener<Commands>() {
//            @Override
//            public void onChanged(Change<? extends Commands> c) {

//                int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();
//
//                while (i < commands.size()) {
//                    try {
//                        command = commands.get(i);
//
//                        //Если тестовая точка активна
//                        if (command.isActive()) {
//
//                            if (command instanceof ErrorCommand) {
//
//                                initAllParamForErrorCommand((ErrorCommand) command, i);
//
//                                if (i != commands.size() - 1) {
//                                    if (commands.get(i + 1) instanceof ErrorCommand) {
//                                        ((ErrorCommand) command).setNextCommand(true);
//                                    }
//                                }
//                                command.execute();
//                            }
//
//                            if (command instanceof CreepCommand) {
//
//                                initAllParamForCreepCommand((CreepCommand) command, phase, i, timeCRPForGOST);
//
//                                command.execute();
//                            }
//
//                            if (command instanceof StartCommand) {
//
//                                initAllParamForStartCommand((StartCommand) command, phase, i, timeSTAForGOST);
//
//                                command.execute();
//                            }
//
//                            if (command instanceof RTCCommand) {
//
//                                initAllParamForRTCCommand((RTCCommand) command, phase, i);
//
//                                command.execute();
//                            }
//                        }
//                    }catch (ConnectForStendExeption e){
//                        e.printStackTrace();
//                        connectionException();
//                    }
//
//                    i++;
//                    tabViewTestPoints.getSelectionModel().select(i);
//
//                    for (TableView<Meter.CommandResult> errorsView : tableViewCommandResults) {
//                        errorsView.getSelectionModel().select(i);
//                    }
//                }
//                tglBtnAuto.setSelected(false);
//            }
//        });

        int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();

        while (i < tabViewTestPoints.getItems().size()) {

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Сработала команда interrupt в методе startTestOnSelectPane" +
                        "Вышел из него");
                return;
            }

            command = tabViewTestPoints.getSelectionModel().getSelectedItem();

            //Если тестовая точка активна
            if (command.isActive()) {

                if (i != tabViewTestPoints.getItems().size() - 1) {
                    command.setNextCommand(true);
                } else {
                    command.setNextCommand(false);
                }

                if (command instanceof ErrorCommand) {

                    initAllParamForErrorCommand((ErrorCommand) command, i);
                    command.execute();
                }else if (command instanceof CreepCommand) {

                    initAllParamForCreepCommand((CreepCommand) command, phase, i, timeCRPForGOST);
                    command.execute();
                }else if (command instanceof StartCommand) {

                    initAllParamForStartCommand((StartCommand) command, phase, i, timeSTAForGOST);
                    command.execute();
                }else if (command instanceof RTCCommand) {

                    initAllParamForRTCCommand((RTCCommand) command, phase, i);
                    command.execute();
                }
            }

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Сработала команда interrupt в методе startTestOnSelectPane " +
                        "Вышел из него");
                return;
            }

            i++;

            tabViewTestPoints.getSelectionModel().select(i);

            for (TableView<Meter.CommandResult> errorsView : tabViewErrorsList) {
                errorsView.getSelectionModel().select(i);
            }
        }
    }

    //Старт ручного теста в зависимости от выбранной панели (направления и типа энергии)
    private void startContinuousTestOnSelectPane(int phase, long timeCRPForGOST, long timeSTAForGOST)
            throws ConnectForStendExeption, InterruptedException {

//        ObservableList<Commands> comandsList = tabViewTestPoints.getSelectionModel().getSelectedItems();
//
//        comandsList.addListener(new ListChangeListener<Commands>() {
//            @Override
//            public void onChanged(Change<? extends Commands> c) {
//                try {
//                    stendDLLCommands.errorClear();
//
//                    int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();
//
//                    command = commands.get(i);
//
//
//                    if (command instanceof ErrorCommand) {
//
//
//                        initAllParamForErrorCommand((ErrorCommand) command, i);
//
//                        command.executeForContinuousTest();
//                    }
//
//                    if (command instanceof CreepCommand) {
//
//                        initAllParamForCreepCommand((CreepCommand) command, phase, i, timeCRPForGOST);
//
//                        command.executeForContinuousTest();
//                    }
//
//                    if (command instanceof StartCommand) {
//
//                        initAllParamForStartCommand((StartCommand) command, phase, i, timeSTAForGOST);
//
//                        command.executeForContinuousTest();
//                    }
//
//                    if (command instanceof RTCCommand) {
//
//                        initAllParamForRTCCommand((RTCCommand) command, phase, i);
//
//                        command.executeForContinuousTest();
//                    }
//                } catch (ConnectForStendExeption e) {
//                    e.printStackTrace();
//                    connectionException();
//                }
//            }
//        });

        if (Thread.currentThread().isInterrupted()) {
            System.out.println("Сработала команда interrupt в методе startTestOnSelectPane " +
                    "Вышел из него");
            return;
        }

        int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();

        command = tabViewTestPoints.getSelectionModel().getSelectedItem();

        if (command instanceof ErrorCommand) {

            initAllParamForErrorCommand((ErrorCommand) command, i);

            command.executeForContinuousTest();
        }else if (command instanceof CreepCommand) {

            initAllParamForCreepCommand((CreepCommand) command, phase, i, timeCRPForGOST);

            command.executeForContinuousTest();
        }else if (command instanceof StartCommand) {

            initAllParamForStartCommand((StartCommand) command,phase, i, timeSTAForGOST);

            command.executeForContinuousTest();
        }else if (command instanceof RTCCommand) {

            initAllParamForRTCCommand((RTCCommand) command, phase, i);

            command.executeForContinuousTest();
        }
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
    private void initAllParamForCreepCommand(CreepCommand creepCommand, int phase, int index, long timeForGOSTtest){
        creepCommand.setStendDLLCommands(stendDLLCommands);
        creepCommand.setPhase(phase);
        creepCommand.setRatedVolt(Un);
        creepCommand.setRatedFreq(Fn);
        creepCommand.setIndex(index);
        creepCommand.setMeterList(listMetersForTest);

        if (creepCommand.isGostTest()) {
            creepCommand.setTimeForTest(timeForGOSTtest);
        }
    }

    //Инициализирует параметры необходимые для команды чувствительность
    private void initAllParamForStartCommand(StartCommand startCommand, int phase, int index, long timeForGOSTtest) {
        startCommand.setStendDLLCommands(stendDLLCommands);
        startCommand.setPhase(phase);
        startCommand.setRatedFreq(Fn);
        startCommand.setRatedVolt(Un);
        startCommand.setIndex(index);
        startCommand.setMeterList(listMetersForTest);

        if (startCommand.isGostTest()) {
            startCommand.setTimeForTest(timeForGOSTtest);
        }
    }

    //Инициализирует параметры необходимые для команды чувстви
    private void initAllParamForRTCCommand(RTCCommand rTCCommand, int phase, int index) {
        rTCCommand.setStendDLLCommands(stendDLLCommands);
        rTCCommand.setPhase(phase);
        rTCCommand.setRatedVolt(Un);
        rTCCommand.setIndex(index);
        rTCCommand.setMeterList(listMetersForTest);
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

    //Установка тзначений таблицы погрешности AP+
    private void initErrorPaneForAPPls() {
        setTabColTestPoints(methodic.getCommandsMap().get(0));
        setErrorTablesAPPls();
        checkBoxDisableAll.setSelected(true);
    }

    private void initErrorPaneForAPMns() {
        setTabColTestPoints(methodic.getCommandsMap().get(1));
        setErrorTablesAPMns();
        checkBoxDisableAll.setSelected(true);
    }

    private void initErrorPaneForRPPls() {
        setTabColTestPoints(methodic.getCommandsMap().get(2));
        setErrorTablesRPPls();
        checkBoxDisableAll.setSelected(true);
    }

    private void initErrorPaneForRPMns() {
        setTabColTestPoints(methodic.getCommandsMap().get(3));
        setErrorTablesRPMns();
        checkBoxDisableAll.setSelected(true);
    }

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

    @FXML
    void initialize() {
        tglBtnAPPls.setSelected(true);

        checBoxePane.toFront();

        checkBoxDisableAll.setSelected(true);
    }


    public void myInitTestErrorTableFrame() {
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

        initErrorsForMeters();

        //----------------------------------------------------------------------------------
        //Установка чек боксов для отключения или включения точки
        //AP+
        tabViewTestPoints.setEditable(true);
        tabColTestPointsDis.setCellValueFactory(tabColCellData);
        tabColTestPointsDis.setCellFactory(tabColCell);
        tabColTestPointsDis.setSortable(false);


        //В зависимости от количества счётчиков инициализирую поля для отображения погрешности
        if (listMetersForTest.size() <= 12) {

            for (int i = 0; i < listMetersForTest.size(); i++) {
                //Создаю колонки
                addTableViewAndCollumnInErrorPane(i, paneErrors.getPrefWidth() / listMetersForTest.size(), paneErrors.getPrefHeight(),
                        i * (paneErrors.getPrefWidth() / listMetersForTest.size()), 0);

            }

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
                double widthColumn = paneErrors.getPrefWidth() / (listMetersForTest.size() / 2);
                double heightColumn = paneErrors.getPrefHeight() / 2;

                for (int j = 0; j < 2; j++) {
                    if (j == 0) {

                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0);
                        }
                    } else {

                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2, widthColumn, heightColumn,
                                    i * widthColumn, paneErrors.getPrefHeight() / 2);
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
                double widthColumn;
                double heightColumn = paneErrors.getPrefHeight() / 2;

                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        widthColumn = paneErrors.getPrefWidth() / (listMetersForTest.size() / 2 + 1);

                        for (int i = 0; i < (listMetersForTest.size() / 2) + 1; i++) {
                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0);

                        }
                    } else {
                        widthColumn = paneErrors.getPrefWidth() / (listMetersForTest.size() / 2);

                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2 + 1, widthColumn, heightColumn,
                                    i * widthColumn, paneErrors.getPrefHeight() / 2);

                        }
                    }
                }

                paneErrors.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        int meters = listMetersForTest.size();

                        double widthColumn = ((Double) newValue) / (meters / 2) + 1;

                        for (int i = 0; i < (meters / 2) + 1; i++) {
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

        tglBtnAPPls.fire();

        //--------------------------------------------------------------------
        //Устанавливаю фокусировку на окне ошибок такую же как и в окне точек
        //AP+
        //Значение при инициализации
        tabViewTestPoints.getSelectionModel().select(0);

        for (TableView<Meter.CommandResult> tableViewError : tabViewErrorsList) {
            tableViewError.getSelectionModel().select(0);
        }

        //Если выбираю точку испытания, то должна выставляться фокусировка и на панели с погрешностью
        ObservableList<TablePosition> tablePositionsAPPls = tabViewTestPoints.getSelectionModel().getSelectedCells();

        tablePositionsAPPls.addListener(new ListChangeListener<TablePosition>() {
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
    }

    //Добавляет объект error к каждому счётчику необходимому для теста
    private void initErrorsForMeters() {

        //Инициализицрую константы активной и реактивной энергии
        constantMeterAP = Integer.parseInt(listMetersForTest.get(0).getConstantMeterAP());
        constantMeterRP = Integer.parseInt(listMetersForTest.get(0).getConstantMeterRP());

        //Инициирую время теста для Самохода и чувствительности по ГОСТУ
        intiTimeCRPSTATests();

        for (Meter meter : listMetersForTest) {
            for (int i = 0; i < 4; i++) {
                for (Commands commandName : methodic.getCommandsMap().get(i)) {
                    meter.createError(commandName, i, commandName.getName(), this);
                }
            }
        }
    }

    private void addTableViewAndCollumnInErrorPane(int index, double prefWidth, double prefHeight, double layoutX, double layoutY) {

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
                                        setTextFill(Color.BLUE);

                                    } else if (firstSymbol == 'F') {
                                        setText(item.substring(1));
                                        setTextFill(Color.RED);
                                    }
                                }
                            }
                        };
                    }
                };

        TableView<Meter.CommandResult> tableView = new TableView<>();
        TableColumn<Meter.CommandResult, String> column = new TableColumn<>("Место " + listMetersForTest.get(index).getId());
        column.setStyle("-fx-alignment: CENTER;");
        column.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
        column.setSortable(false);
        column.setCellFactory(cellFactoryEndTest);
        tableView.setPrefSize(prefWidth, prefHeight);
        column.setPrefWidth(tableView.getPrefWidth());
        tableView.setLayoutX(layoutX);
        tableView.setLayoutY(layoutY);
        tableView.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("styleCSS/hideScrollBars.css")));
        tableView.getColumns().add(column);
        paneErrors.getChildren().add(tableView);
        tabViewErrorsList.add(tableView);
    }

    //Находит все скрол бары
    void initScrolBars() {
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

        if (typeCircuitThreePhase) {
            amountMeasElem = 3;
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

        if (typeCircuitThreePhase) {
            amountMeasElem = 3;
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

    //Если возникает ошибка связи с установкой
    void connectionException() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/viewFXML/exceptionFrame.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException er) {
            er.printStackTrace();
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ConsoleHelper.infoException("Потеряна связь с установной,\nпроверьте подключение");
            }
        });

        tglBtnAuto.setDisable(false);
        tglBtnManualMode.setDisable(false);
        tglBtnUnom.setDisable(false);
    }
    
    //=====================================================================================
    //get sets

    Label getTxtLabUn() {
        return txtLabUn;
    }

    Label getTxtLabInom() {
        return txtLabInom;
    }

    Label getTxtLabImax() {
        return txtLabImax;
    }

    Label getTxtLabFn() {
        return txtLabFn;
    }

    Label getTxtLabTypeCircuit() {
        return txtLabTypeCircuit;
    }

    Label getTxtLabAccuracyСlass() {
        return txtLabAccuracyСlass;
    }

    Label getTxtLabDate() {
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

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
    }

    public void setTypeOfMeasuringElementShunt(boolean typeOfMeasuringElementShunt) {
        this.typeOfMeasuringElementShunt = typeOfMeasuringElementShunt;
    }

    public void setTypeCircuit(boolean typeCircuitThreePhase) {
        this.typeCircuitThreePhase = typeCircuitThreePhase;
    }

    public void setAccuracyClassAP(double accuracyClassAP) {
        this.accuracyClassAP = accuracyClassAP;
    }

    public void setAccuracyClassRP(double accuracyClassRP) {
        this.accuracyClassRP = accuracyClassRP;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public void setWitness(String witness) {
        this.witness = witness;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public List<Meter> getListMetersForTest() {
        return listMetersForTest;
    }
}
