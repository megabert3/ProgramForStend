package org.taipit.stend.controller.viewController;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
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

    private volatile boolean isFinish = true;

    //Исполняемая команда
    Commands command;
   private List<Meter> listMetersForTest;

    private Methodic methodic;

    //Список команд
    private ObservableList<Commands> commandsAPPls;
    private ObservableList<Commands> commandsAPMns;
    private ObservableList<Commands> commandsRPPls;
    private ObservableList<Commands> commandsRPMns;

    //Список TableView для испытываемых счётчиков в разных направлениях
    private List<TableView<Meter.CommandResult>> tabViewListAPPls = new ArrayList<>();
    private List<TableView<Meter.CommandResult>> tabViewListAPMns = new ArrayList<>();
    private List<TableView<Meter.CommandResult>> tabViewListRPPls = new ArrayList<>();
    private List<TableView<Meter.CommandResult>> tabViewListRPMns = new ArrayList<>();

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

    private Service<Void> threadAutomaticTest = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        try {
                            System.out.println("Поток threadAutomaticTest начал свою работу");
                            isFinish = false;
                            startAutomaticTest();
                            isFinish = true;
                            System.out.println("Поток threadAutomaticTest закончил свою работу");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Сработал перехват в классе TestErrorTableFrameController" +
                                    "в потоке threadAutomaticTest");
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                            isFinish = true;
                        }
                    }catch (ConnectForStendExeption e) {
                        System.out.println("Ошибка подключения");
                        isFinish = true;
                        e.printStackTrace();
                        connectionException();
                    }
                    return null;
                }
            };
        }
    };

    private Service<Void> threadManualTest = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        try {
                            System.out.println("Поток threadManualTest начал свою работу");
                            isFinish = false;
                            startManualTest();
                            isFinish = true;
                            System.out.println("Поток threadManualTest закончил свою работу");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Сработал перехват в классе TestErrorTableFrameController" +
                                    "в потоке threadManualTest");
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                            isFinish = true;
                        }
                    }catch (ConnectForStendExeption e) {
                        System.out.println("Ошибка подключения");
                        isFinish = true;
                        e.printStackTrace();
                        connectionException();
                    }
                    return null;
                }
            };
        }
    };

    private Service<Void> threadUn = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() {
                    try {
                        try {
                            System.out.println("Поток threadUn начал свою работу");
                            isFinish = false;
                            startUn();
                            isFinish = true;
                            System.out.println("Поток threadUn закончил свою работу");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Сработал перехват в классе TestErrorTableFrameController" +
                                    "в потоке threadUn");
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                            isFinish = true;
                        }
                    }catch (ConnectForStendExeption e) {
                        e.printStackTrace();
                        connectionException();
                        isFinish = true;
                    }
                    return null;
                }
            };
        }
    };

    @FXML
    private Button btnSave;

    @FXML
    private SplitPane splPaneAPPls;

    @FXML
    private TableView<Commands> tabViewTestPointsAPPls;

    @FXML
    private Pane checBoxePaneAPPls;

    @FXML
    private CheckBox checkBoxDisableAllAPPls;

    @FXML
    private TableColumn<Commands, String> tabColTestPointsAPPls;

    @FXML
    private TableColumn<Commands, Boolean> tabColTestPointsDisAPPls;

    @FXML
    private Pane paneErrorsAPPls;

    @FXML
    private Pane paneErrorsAPMns;

    @FXML
    private Pane paneErrorsRPPls;

    @FXML
    private Pane paneErrorsRPMns;

    @FXML
    private SplitPane splPaneAPMns;

    @FXML
    private TableView<Commands> tabViewTestPointsAPMns;

    @FXML
    private Pane checBoxePaneAPMns;

    @FXML
    private CheckBox checkBoxDisableAllAPMns;

    @FXML
    private TableColumn<Commands, String> tabColTestPointsAPMns;

    @FXML
    private TableColumn<Commands, Boolean> tabColTestPointsDisAPMns;

    @FXML
    private SplitPane splPaneRPPls;

    @FXML
    private TableView<Commands> tabViewTestPointsRPPls;

    @FXML
    private Pane checBoxePaneRPPls;

    @FXML
    private CheckBox checkBoxDisableAllRPPls;

    @FXML
    private TableColumn<Commands, String> tabColTestPointsRPPls;

    @FXML
    private TableColumn<Commands, Boolean> tabColTestPointsDisRPPls;

    @FXML
    private SplitPane splPaneRPMns;

    @FXML
    private TableView<Commands> tabViewTestPointsRPMns;

    @FXML
    private Pane checBoxePaneRPMns;

    @FXML
    private CheckBox checkBoxDisableAllRPMns;

    @FXML
    private TableColumn<Commands, String> tabColTestPointsRPMns;

    @FXML
    private TableColumn<Commands, Boolean> tabColTestPointsDisRPMns;

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

            if (threadManualTest.isRunning()) {
                System.out.println("Останавливаю threadManualTest");
                System.out.println("Статус threadManualTest до остановки " + threadManualTest.getState());
                threadManualTest.cancel();
                System.out.println("Статус threadManualTest после остановки остановки " + threadManualTest.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadManualTest");
                }
            }

            if (threadUn.isRunning()) {
                System.out.println("Останавливаю threadUn");
                System.out.println("Статус threadUn до остановки " + threadUn.getState());
                threadUn.cancel();
                System.out.println("Статус threadUn после остановки остановки " + threadUn.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadUn");
                }
            }
            System.out.println("Состояние threadAutomaticTest до ресета " + threadAutomaticTest.getState());
            threadAutomaticTest.reset();
            System.out.println("Состояние threadAutomaticTest после ресета " + threadAutomaticTest.getState());
            threadAutomaticTest.start();
        }

        //------------------------------------------------------------------------------------------------
        //Логика работы ручного режима работы
        if (event.getSource() == tglBtnManualMode) {

            if (threadAutomaticTest.isRunning()) {
                System.out.println("Останавливаю threadAutomaticTest");
                System.out.println("Статус threadAutomaticTest до остановки " + threadManualTest.getState());
                threadManualTest.cancel();
                System.out.println("Статус threadAutomaticTest после остановки остановки " + threadManualTest.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadAutomaticTest");
                }
            }

            if (threadUn.isRunning()) {
                System.out.println("Останавливаю threadUn");
                System.out.println("Статус threadUn до остановки " + threadUn.getState());
                threadUn.cancel();
                System.out.println("Статус threadUn после остановки остановки " + threadUn.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadUn");
                }
            }

            System.out.println("Состояние threadManualTest до ресета " + threadManualTest.getState());
            threadManualTest.reset();
            System.out.println("Состояние threadManualTest после ресета " + threadManualTest.getState());
            threadManualTest.start();
        }

        //------------------------------------------------------------------------------------------------
        //Логика работы подачи напряжения
        if (event.getSource() == tglBtnUnom) {

            if (threadAutomaticTest.isRunning()) {
                System.out.println("Останавливаю threadAutomaticTest");
                System.out.println("Статус threadAutomaticTest до остановки " + threadManualTest.getState());
                threadManualTest.cancel();
                System.out.println("Статус threadAutomaticTest после остановки остановки " + threadManualTest.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadAutomaticTest");
                }
            }

            if (threadManualTest.isRunning()) {
                System.out.println("Останавливаю threadManualTest");
                System.out.println("Статус threadManualTest до остановки " + threadManualTest.getState());
                threadManualTest.cancel();
                System.out.println("Статус threadManualTest после остановки остановки " + threadManualTest.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadManualTest");
                }
            }

            System.out.println("Состояние threadUn до ресета " + threadUn.getState());
            threadUn.reset();
            System.out.println("Состояние threadUn после ресета " + threadUn.getState());
            threadUn.start();
        }

        //------------------------------------------------------------------------------------------------
        //Логика работы остановки теста
        if (event.getSource() == btnStop) {
            if (threadAutomaticTest.isRunning()) {
                System.out.println("Останавливаю threadAutomaticTest");
                System.out.println("Статус threadAutomaticTest до остановки " + threadManualTest.getState());
                threadManualTest.cancel();
                System.out.println("Статус threadAutomaticTest после остановки остановки " + threadManualTest.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadAutomaticTest");
                }
            }

            if (threadManualTest.isRunning()) {
                System.out.println("Останавливаю threadManualTest");
                System.out.println("Статус threadManualTest до остановки " + threadManualTest.getState());
                threadManualTest.cancel();
                System.out.println("Статус threadManualTest после остановки остановки " + threadManualTest.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadManualTest");
                }
            }

            if (threadUn.isRunning()) {
                System.out.println("Останавливаю threadUn");
                System.out.println("Статус threadUn до остановки " + threadUn.getState());
                threadUn.cancel();
                System.out.println("Статус threadUn после остановки остановки " + threadUn.getState());

                while (!isFinish) {
                    System.out.println("Завершение threadUn");
                }
            }

            System.out.println("Обнуляю табло погрешности, выключаю напряжение и ток");

            tglBtnAuto.setDisable(false);
            tglBtnAuto.setSelected(false);
            tglBtnManualMode.setDisable(false);
            tglBtnManualMode.setSelected(false);
            tglBtnUnom.setDisable(false);
            tglBtnUnom.setSelected(false);
        }
    }

    //Блок команд для старта автоматического теста
    private void startAutomaticTest() throws ConnectForStendExeption {
        int phase;

        tglBtnAuto.setSelected(true);
        tglBtnAuto.setDisable(true);

        tglBtnManualMode.setDisable(false);
        tglBtnManualMode.setSelected(false);
        tglBtnUnom.setDisable(false);
        tglBtnUnom.setSelected(false);

        //Если выбрана панель AP+
        if (tglBtnAPPls.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 1;
            } else phase = 0;

            startTestOnSelectPane(tabViewTestPointsAPPls, tabViewListAPPls, commandsAPPls, phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

            //Если выбрана панель AP-
        } else if(tglBtnAPMns.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 1;
            } else phase = 0;

            startTestOnSelectPane(tabViewTestPointsAPMns, tabViewListAPMns, commandsAPMns, phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

            //Если выбрана панель RP+
        } else if(tglBtnRPPls.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 5;
            } else phase = 7;

            startTestOnSelectPane(tabViewTestPointsRPPls, tabViewListRPPls, commandsRPPls, phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);

            //Если выбрана панель RP-
        } else if(tglBtnRPMns.isSelected()) {

            if (typeCircuitThreePhase) {
                phase = 5;
            } else phase = 7;

            startTestOnSelectPane(tabViewTestPointsRPMns, tabViewListRPMns, commandsRPMns, phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);
        }
    }

    //Общая команда для старта ручного теста
    private void startManualTest() throws ConnectForStendExeption {
        int phase;

        tglBtnAuto.setDisable(false);
        tglBtnAuto.setSelected(false);

        tglBtnManualMode.setSelected(true);
        tglBtnManualMode.setDisable(true);

        tglBtnUnom.setDisable(false);
        tglBtnUnom.setSelected(false);


        //Если выбрана панель AP+
        if (tglBtnAPPls.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 1;
            } else phase = 0;

            startContinuousTestOnSelectPane(tabViewTestPointsAPPls, commandsAPPls, phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

            //Если выбрана панель AP-
        } else if(tglBtnAPMns.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 1;
            } else phase = 0;

            startContinuousTestOnSelectPane(tabViewTestPointsAPMns, commandsAPMns, phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

            //Если выбрана панель RP+
        } else if(tglBtnRPPls.isSelected()) {
            if (typeCircuitThreePhase) {
                phase = 5;
            } else phase = 7;

            startContinuousTestOnSelectPane(tabViewTestPointsRPPls, commandsRPPls, phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);

            //Если выбрана панель RP-
        } else if(tglBtnRPMns.isSelected()) {

            if (typeCircuitThreePhase) {
                phase = 5;
            } else phase = 7;

            startContinuousTestOnSelectPane(tabViewTestPointsRPMns, commandsRPMns, phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);
        }
    }

    //Общая команда для старта напряжения
    private void startUn () throws ConnectForStendExeption {

        int phase;

        tglBtnAuto.setDisable(false);
        tglBtnAuto.setSelected(false);

        tglBtnManualMode.setDisable(false);
        tglBtnManualMode.setSelected(false);

        tglBtnUnom.setSelected(true);
        tglBtnUnom.setDisable(true);

        if (typeCircuitThreePhase) {
            phase = 1;
        } else {
            phase = 0;
        }

        if (!stendDLLCommands.getUI(phase, Un, 0, Fn, 0, 0, 100.0, 0, "H", "1.0")) throw new ConnectForStendExeption();

        if (Thread.currentThread().isInterrupted()) {
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        }
    }

    //Старт автоматического теста в зависимости от выбранной панели (направления и типа энергии)
    private void startTestOnSelectPane(TableView<Commands> tabViewTestPoints, List<TableView<Meter.CommandResult>> tableViewCommandResults,
                                       ObservableList<Commands> commands, int phase, long timeCRPForGOST, long timeSTAForGOST)
            throws ConnectForStendExeption {

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

        while (i < commands.size()) {

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Сработала команда interrupt в методе startTestOnSelectPane" +
                        "Вышел из него");
                return;
            }

            command = commands.get(i);

            //Если тестовая точка активна
            if (command.isActive()) {

                if (i != commands.size() - 1) {
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

            for (TableView<Meter.CommandResult> errorsView : tableViewCommandResults) {
                errorsView.getSelectionModel().select(i);
            }
        }
        tglBtnAuto.setSelected(false);
    }

    //Старт ручного теста в зависимости от выбранной панели (направления и типа энергии)
    private void startContinuousTestOnSelectPane(TableView<Commands> tabViewTestPoints, ObservableList<Commands> commands,
                                                 int phase, long timeCRPForGOST, long timeSTAForGOST)
            throws ConnectForStendExeption {

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

        command = commands.get(i);

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
        if (event.getSource() == checkBoxDisableAllAPPls) {
            if (checkBoxDisableAllAPPls.isSelected()) {
                for (Commands commsnd : commandsAPPls) {
                    commsnd.setActive(true);
                }
                tabViewTestPointsAPPls.refresh();
            } else {
                for (Commands commsnd : commandsAPPls) {
                    commsnd.setActive(false);
                }
                tabViewTestPointsAPPls.refresh();
            }
        }

        //AP-
        if (event.getSource() == checkBoxDisableAllAPMns) {
            if (checkBoxDisableAllAPMns.isSelected()) {
                for (Commands commsnd : commandsAPMns) {
                    commsnd.setActive(true);
                }
                tabViewTestPointsAPMns.refresh();
            } else {
                for (Commands commsnd : commandsAPMns) {
                    commsnd.setActive(false);
                }
                tabViewTestPointsAPMns.refresh();
            }
        }

        //RP+
        if (event.getSource() == checkBoxDisableAllRPPls) {
            if (checkBoxDisableAllRPPls.isSelected()) {
                for (Commands commsnd : commandsRPPls) {
                    commsnd.setActive(true);
                }
                tabViewTestPointsRPPls.refresh();
            } else {
                for (Commands commsnd : commandsRPPls) {
                    commsnd.setActive(false);
                }
                tabViewTestPointsRPPls.refresh();
            }
        }

        //RP-
        if (event.getSource() == checkBoxDisableAllRPMns) {
            if (checkBoxDisableAllRPMns.isSelected()) {
                for (Commands commsnd : commandsRPMns) {
                    commsnd.setActive(true);
                }
                tabViewTestPointsRPMns.refresh();
            } else {
                for (Commands commsnd : commandsRPMns) {
                    commsnd.setActive(false);
                }
                tabViewTestPointsRPMns.refresh();
            }
        }
    }

    @FXML
    void actionEventSwithEnergyPane(ActionEvent event) {
        if (event.getSource() == tglBtnAPPls) {
            splPaneAPPls.toFront();
            checBoxePaneAPPls.toFront();
            tglBtnAPPls.setSelected(true);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnAPMns) {
            splPaneAPMns.toFront();
            checBoxePaneAPMns.toFront();
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(true);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnRPPls) {
            splPaneRPPls.toFront();
            checBoxePaneRPPls.toFront();
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(true);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnRPMns) {
            splPaneRPMns.toFront();
            checBoxePaneRPMns.toFront();
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(true);
        }
    }

    @FXML
    void initialize() {
        tglBtnAPPls.setSelected(true);
        splPaneAPPls.toFront();

        checBoxePaneAPPls.toFront();

        checkBoxDisableAllAPPls.setSelected(true);
        checkBoxDisableAllAPMns.setSelected(true);
        checkBoxDisableAllRPPls.setSelected(true);
        checkBoxDisableAllRPMns.setSelected(true);
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

        //Инициирую колонку с точками для испытаний AP+
        tabColTestPointsAPPls.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPointsAPPls.setSortable(false);
        commandsAPPls = FXCollections.observableArrayList(methodic.getCommandsMap().get(0));
        tabViewTestPointsAPPls.setItems(commandsAPPls);

        //Инициирую колонку с точками для испытаний AP-
        tabColTestPointsAPMns.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPointsAPMns.setSortable(false);
        commandsAPMns = FXCollections.observableArrayList(methodic.getCommandsMap().get(1));
        tabViewTestPointsAPMns.setItems(commandsAPMns);

        //Инициирую колонку с точками для испытаний RP+
        tabColTestPointsRPPls.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPointsRPPls.setSortable(false);
        commandsRPPls = FXCollections.observableArrayList(methodic.getCommandsMap().get(2));
        tabViewTestPointsRPPls.setItems(commandsRPPls);

        //Инициирую колонку с точками для испытаний RP+
        tabColTestPointsRPMns.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPointsRPMns.setSortable(false);
        commandsRPMns = FXCollections.observableArrayList(methodic.getCommandsMap().get(3));
        tabViewTestPointsRPMns.setItems(commandsRPMns);

        //----------------------------------------------------------------------------------
        //Установка чек боксов для отключения или включения точки
        //AP+
        tabViewTestPointsAPPls.setEditable(true);
        tabColTestPointsDisAPPls.setCellValueFactory(tabColCellData);
        tabColTestPointsDisAPPls.setCellFactory(tabColCell);
        tabColTestPointsDisAPPls.setSortable(false);

        //AP-
        tabViewTestPointsAPMns.setEditable(true);
        tabColTestPointsDisAPMns.setCellValueFactory(tabColCellData);
        tabColTestPointsDisAPMns.setCellFactory(tabColCell);
        tabColTestPointsDisAPMns.setSortable(false);

        //RP+
        tabViewTestPointsRPPls.setEditable(true);
        tabColTestPointsDisRPPls.setCellValueFactory(tabColCellData);
        tabColTestPointsDisRPPls.setCellFactory(tabColCell);
        tabColTestPointsDisRPPls.setSortable(false);

        //RP-
        tabViewTestPointsRPMns.setEditable(true);
        tabColTestPointsDisRPMns.setCellValueFactory(tabColCellData);
        tabColTestPointsDisRPMns.setCellFactory(tabColCell);
        tabColTestPointsDisRPMns.setSortable(false);

        //-----------------------------------------------------------
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

        //В зависимости от количества счётчиков инициализирую поля для отображения погрешности
        if (listMetersForTest.size() <= 12) {

            for (int i = 0; i < listMetersForTest.size(); i++) {
                //Создаю колонки AP+
                addTableViewAndCollumnInErrorPane(i, paneErrorsAPPls.getPrefWidth() / listMetersForTest.size(), paneErrorsAPPls.getPrefHeight(),
                        i * (paneErrorsAPPls.getPrefWidth() / listMetersForTest.size()), 0, listMetersForTest.get(i).getErrorListAPPls(), paneErrorsAPPls,
                        tabViewListAPPls, cellFactoryEndTest);

                //Создаю колонки AP-
                addTableViewAndCollumnInErrorPane(i, paneErrorsAPMns.getPrefWidth() / listMetersForTest.size(), paneErrorsAPMns.getPrefHeight(),
                        i * (paneErrorsAPMns.getPrefWidth() / listMetersForTest.size()), 0, listMetersForTest.get(i).getErrorListAPMns(), paneErrorsAPMns,
                        tabViewListAPMns, cellFactoryEndTest);

                //Создаю колонки RP+
                addTableViewAndCollumnInErrorPane(i, paneErrorsRPPls.getPrefWidth() / listMetersForTest.size(), paneErrorsRPPls.getPrefHeight(),
                        i * (paneErrorsRPPls.getPrefWidth() / listMetersForTest.size()), 0, listMetersForTest.get(i).getErrorListRPPls(), paneErrorsRPPls,
                        tabViewListRPPls, cellFactoryEndTest);

                //Создаю колонки RP-
                addTableViewAndCollumnInErrorPane(i, paneErrorsRPMns.getPrefWidth() / listMetersForTest.size(), paneErrorsRPMns.getPrefHeight(),
                        i * (paneErrorsRPMns.getPrefWidth() / listMetersForTest.size()), 0, listMetersForTest.get(i).getErrorListRPMns(), paneErrorsRPMns,
                        tabViewListRPMns, cellFactoryEndTest);
            }
        } else if (listMetersForTest.size() <= 24) {
            if (listMetersForTest.size() % 2 == 0) {
                /**
                 * Значения других панелей напрямую зависят от размеров первой.
                 */

                double widthColumn = paneErrorsAPPls.getPrefWidth() / (listMetersForTest.size() / 2);
                double heightColumn = paneErrorsAPPls.getPrefHeight() / 2;

                for (int j = 0; j < 2; j++) {
                    if (j == 0) {

                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0, listMetersForTest.get(i).getErrorListAPPls(), paneErrorsAPPls,
                                    tabViewListAPPls, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0, listMetersForTest.get(i).getErrorListAPMns(), paneErrorsAPMns,
                                    tabViewListAPMns, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0, listMetersForTest.get(i).getErrorListRPPls(), paneErrorsRPPls,
                                    tabViewListRPPls, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0, listMetersForTest.get(i).getErrorListRPMns(), paneErrorsRPMns,
                                    tabViewListRPMns, cellFactoryEndTest);
                        }
                    } else {

                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2, widthColumn, heightColumn,
                                    i * widthColumn, paneErrorsAPPls.getPrefHeight() / 2,
                                    listMetersForTest.get(i + listMetersForTest.size() / 2).getErrorListAPPls(), paneErrorsAPPls,
                                    tabViewListAPPls, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2, widthColumn, heightColumn,
                                    i * widthColumn, paneErrorsAPMns.getPrefHeight() / 2,
                                    listMetersForTest.get(i + listMetersForTest.size() / 2).getErrorListAPMns(), paneErrorsAPMns,
                                    tabViewListAPMns, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2, widthColumn, heightColumn,
                                    i * widthColumn, paneErrorsRPPls.getPrefHeight() / 2,
                                    listMetersForTest.get(i + listMetersForTest.size() / 2).getErrorListRPPls(), paneErrorsRPPls,
                                    tabViewListRPPls, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2, widthColumn, heightColumn,
                                    i * widthColumn, paneErrorsRPMns.getPrefHeight() / 2,
                                    listMetersForTest.get(i + listMetersForTest.size() / 2).getErrorListRPMns(), paneErrorsRPMns,
                                    tabViewListRPMns, cellFactoryEndTest);
                        }
                    }
                }

            } else {
                double widthColumn; // = paneErrorsAPPls.getPrefWidth() / (listMetersForTest.size() / 2);
                double heightColumn = paneErrorsAPPls.getPrefHeight() / 2;

                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        widthColumn = paneErrorsAPPls.getPrefWidth() / (listMetersForTest.size() / 2 + 1);

                        for (int i = 0; i < (listMetersForTest.size() / 2) + 1; i++) {
                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0, listMetersForTest.get(i).getErrorListAPPls(), paneErrorsAPPls,
                                    tabViewListAPPls, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0, listMetersForTest.get(i).getErrorListAPMns(), paneErrorsAPMns,
                                    tabViewListAPMns, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0, listMetersForTest.get(i).getErrorListRPPls(), paneErrorsRPPls,
                                    tabViewListRPPls, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i, widthColumn, heightColumn,
                                    i * widthColumn, 0, listMetersForTest.get(i).getErrorListRPMns(), paneErrorsRPMns,
                                    tabViewListRPMns, cellFactoryEndTest);
                        }
                    } else {
                        widthColumn = paneErrorsAPPls.getPrefWidth() / (listMetersForTest.size() / 2);

                        for (int i = 0; i < listMetersForTest.size() / 2; i++) {
                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2 + 1, widthColumn, heightColumn,
                                    i * widthColumn, paneErrorsAPPls.getPrefHeight() / 2,
                                    listMetersForTest.get(i + listMetersForTest.size() / 2).getErrorListAPPls(), paneErrorsAPPls,
                                    tabViewListAPPls, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2 + 1, widthColumn, heightColumn,
                                    i * widthColumn, paneErrorsAPMns.getPrefHeight() / 2,
                                    listMetersForTest.get(i + listMetersForTest.size() / 2).getErrorListAPMns(), paneErrorsAPMns,
                                    tabViewListAPMns, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2 + 1, widthColumn, heightColumn,
                                    i * widthColumn, paneErrorsRPPls.getPrefHeight() / 2,
                                    listMetersForTest.get(i + listMetersForTest.size() / 2).getErrorListRPPls(), paneErrorsRPPls,
                                    tabViewListRPPls, cellFactoryEndTest);

                            addTableViewAndCollumnInErrorPane(i + listMetersForTest.size() / 2 + 1, widthColumn, heightColumn,
                                    i * widthColumn, paneErrorsRPMns.getPrefHeight() / 2,
                                    listMetersForTest.get(i + listMetersForTest.size() / 2).getErrorListRPMns(), paneErrorsRPMns,
                                    tabViewListRPMns, cellFactoryEndTest);
                        }
                    }
                }
            }
        }

        //--------------------------------------------------------------------
        //Устанавливаю фокусировку на окне ошибок такую же как и в окне точек
        //AP+
        //Значение при инициализации
        tabViewTestPointsAPPls.getSelectionModel().select(0);

        for (TableView<Meter.CommandResult> tableViewError : tabViewListAPPls) {
            tableViewError.getSelectionModel().select(0);
        }

        //Если выбираю точку испытания, то должна выставляться фокусировка и на панели с погрешностью
        ObservableList<TablePosition> tablePositionsAPPls = tabViewTestPointsAPPls.getSelectionModel().getSelectedCells();

        tablePositionsAPPls.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsAPPls.getSelectionModel().getFocusedIndex();

                for (TableView<Meter.CommandResult> tableViewError : tabViewListAPPls) {

                    if (!tableViewError.getSelectionModel().isSelected(i)) {
                        tableViewError.getFocusModel().focus(i);
                        tableViewError.getSelectionModel().select(i);
                        tableViewError.scrollTo(i - 5);
                    }
                }
            }
        });

        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewListAPPls.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsAPPls = tabViewListAPPls.get(i).getSelectionModel().getSelectedCells();

            tableErrorPositionsAPPls.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {

                    int index = tabViewTestPointsAPPls.getSelectionModel().getSelectedIndex();

                    for (TableView<Meter.CommandResult> tableViewError : tabViewListAPPls) {
                        if (!tableViewError.getSelectionModel().isSelected(index)) {
                            tableViewError.getFocusModel().focus(index);
                            tableViewError.getSelectionModel().select(index);
                        }
                    }
                }
            });
        }

        //AP-
        tabViewTestPointsAPMns.getSelectionModel().select(0);
        for (TableView<Meter.CommandResult> tableViewError : tabViewListAPMns) {
            tableViewError.getSelectionModel().select(0);
        }

        ObservableList<TablePosition> tablePositionsAPMns = tabViewTestPointsAPMns.getSelectionModel().getSelectedCells();

        tablePositionsAPMns.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsAPMns.getSelectionModel().getFocusedIndex();

                for (TableView<Meter.CommandResult> tableViewError : tabViewListAPMns) {
                    if (!tableViewError.getSelectionModel().isSelected(i)) {
                        tableViewError.getFocusModel().focus(i);
                        tableViewError.getSelectionModel().select(i);
                        tableViewError.scrollTo(i - 5);
                    }
                }
            }
        });

        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewListAPMns.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsAPMns = tabViewListAPMns.get(i).getSelectionModel().getSelectedCells();

            tableErrorPositionsAPMns.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {
                    int index = tabViewTestPointsAPMns.getSelectionModel().getSelectedIndex();

                    for (TableView<Meter.CommandResult> tableViewError : tabViewListAPMns) {
                        if (!tableViewError.getSelectionModel().isSelected(index)) {
                            tableViewError.getFocusModel().focus(index);
                            tableViewError.getSelectionModel().select(index);
                        }
                    }
                }
            });
        }

        //RP+
        tabViewTestPointsRPPls.getSelectionModel().select(0);
        for (TableView<Meter.CommandResult> tableViewError : tabViewListRPPls) {
            tableViewError.getSelectionModel().select(0);
        }

        ObservableList<TablePosition> tablePositionsRPPls = tabViewTestPointsRPPls.getSelectionModel().getSelectedCells();

        tablePositionsRPPls.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsRPPls.getSelectionModel().getFocusedIndex();

                for (TableView<Meter.CommandResult> tableViewError : tabViewListRPPls) {
                    if (!tableViewError.getSelectionModel().isSelected(i)) {
                        tableViewError.getFocusModel().focus(i);
                        tableViewError.getSelectionModel().select(i);
                        tableViewError.scrollTo(i - 5);
                    }
                }
            }
        });

        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewListRPPls.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsRPPls = tabViewListRPPls.get(i).getSelectionModel().getSelectedCells();

            tableErrorPositionsRPPls.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {
                    int index = tabViewTestPointsRPPls.getSelectionModel().getFocusedIndex();

                    for (TableView<Meter.CommandResult> tableViewError : tabViewListRPPls) {
                        if (!tableViewError.getSelectionModel().isSelected(index)) {
                            tableViewError.getFocusModel().focus(index);
                            tableViewError.getSelectionModel().select(index);
                        }

                    }
                }
            });
        }

        //RP-
        tabViewTestPointsRPMns.getSelectionModel().select(0);
        for (TableView<Meter.CommandResult> tableViewError : tabViewListRPMns) {
            tableViewError.getSelectionModel().select(0);
        }

        ObservableList<TablePosition> tablePositionsRPMns = tabViewTestPointsRPMns.getSelectionModel().getSelectedCells();

        tablePositionsRPMns.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsRPMns.getSelectionModel().getFocusedIndex();

                for (TableView<Meter.CommandResult> tableViewError : tabViewListRPMns) {
                    if (!tableViewError.getSelectionModel().isSelected(i)) {
                        tableViewError.getFocusModel().focus(i);
                        tableViewError.getSelectionModel().select(i);
                        tableViewError.scrollTo(i - 5);
                    }
                }
            }
        });

        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewListRPMns.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsRPMns = tabViewListRPMns.get(i).getSelectionModel().getSelectedCells();

            tableErrorPositionsRPMns.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {

                    int index = tabViewTestPointsRPMns.getSelectionModel().getFocusedIndex();

                    for (TableView<Meter.CommandResult> tableViewError : tabViewListRPMns) {
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

    private void addTableViewAndCollumnInErrorPane(int index, double prefWidth, double prefHeight, double layoutX, double layoutY, List<Meter.CommandResult> errorList,
                                                   Pane errorPane, List<TableView<Meter.CommandResult>> tableViewList, Callback callback) {

        TableView<Meter.CommandResult> tableView = new TableView<>();
        TableColumn<Meter.CommandResult, String> column = new TableColumn<>("Место " + listMetersForTest.get(index).getId());
        column.setStyle("-fx-alignment: CENTER;");
        column.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
        column.setSortable(false);
        column.setCellFactory(callback);
        tableView.setPrefSize(prefWidth, prefHeight);
        column.setPrefWidth(tableView.getPrefWidth());
        tableView.setLayoutX(layoutX);
        tableView.setLayoutY(layoutY);
        tableView.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("styleCSS/hideScrollBars.css")));
        tableView.setItems(FXCollections.observableArrayList(errorList));
        tableView.getColumns().add(column);
        errorPane.getChildren().add(tableView);
        tableViewList.add(tableView);
    }

    //Находит все скрол бары
    void initScrolBars() {
        ScrollBar verticalBarCommands;
        ScrollBar verticalBarErrorsFirst;
        ScrollBar verticalBarErrorsSecond;

        //Получаю скрол бары определённого окна
        //AP+
        verticalBarCommands = (ScrollBar) tabViewTestPointsAPPls.lookup(".scroll-bar:vertical");

        for (int i = tabViewListAPPls.size() - 1; i > 0; i--) {
            verticalBarErrorsFirst = (ScrollBar) tabViewListAPPls.get(i).lookup(".scroll-bar:vertical");
            verticalBarErrorsSecond = (ScrollBar) tabViewListAPPls.get(i - 1).lookup(".scroll-bar:vertical");

            bindScrolls(verticalBarErrorsFirst, verticalBarErrorsSecond);

            ScrollBar finalVerticalBarErrors1 = verticalBarErrorsFirst;
            verticalBarCommands.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    finalVerticalBarErrors1.valueProperty().setValue(newValue);
                }
            });
        }

        //AP-
        verticalBarCommands = (ScrollBar) tabViewTestPointsAPMns.lookup(".scroll-bar:vertical");

        for (int i = tabViewListAPMns.size() - 1; i > 0; i--) {
            verticalBarErrorsFirst = (ScrollBar) tabViewListAPMns.get(i).lookup(".scroll-bar:vertical");
            verticalBarErrorsSecond = (ScrollBar) tabViewListAPMns.get(i - 1).lookup(".scroll-bar:vertical");

            bindScrolls(verticalBarErrorsFirst, verticalBarErrorsSecond);

            ScrollBar finalVerticalBarErrors1 = verticalBarErrorsFirst;
            verticalBarCommands.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    finalVerticalBarErrors1.valueProperty().setValue(newValue);
                }
            });
        }


        //RP+
        verticalBarCommands = (ScrollBar) tabViewTestPointsRPPls.lookup(".scroll-bar:vertical");

        for (int i = tabViewListRPPls.size() - 1; i > 0; i--) {
            verticalBarErrorsFirst = (ScrollBar) tabViewListRPPls.get(i).lookup(".scroll-bar:vertical");
            verticalBarErrorsSecond = (ScrollBar) tabViewListRPPls.get(i - 1).lookup(".scroll-bar:vertical");

            bindScrolls(verticalBarErrorsFirst, verticalBarErrorsSecond);

            ScrollBar finalVerticalBarErrors1 = verticalBarErrorsFirst;
            verticalBarCommands.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    finalVerticalBarErrors1.valueProperty().setValue(newValue);
                }
            });
        }

        //RP-
        verticalBarCommands = (ScrollBar) tabViewTestPointsRPMns.lookup(".scroll-bar:vertical");

        for (int i = tabViewListRPMns.size() - 1; i > 0; i--) {
            verticalBarErrorsFirst = (ScrollBar) tabViewListRPMns.get(i).lookup(".scroll-bar:vertical");
            verticalBarErrorsSecond = (ScrollBar) tabViewListRPMns.get(i - 1).lookup(".scroll-bar:vertical");

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
        tglBtnAuto.setSelected(false);
        tglBtnManualMode.setDisable(false);
        tglBtnManualMode.setSelected(false);
        tglBtnUnom.setDisable(false);
        tglBtnUnom.setSelected(false);
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
