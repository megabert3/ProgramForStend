package org.taipit.stend.controller.viewController;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;
import org.taipit.stend.model.Methodic;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestErrorTableFrameController {

    StendDLLCommands stendDLLCommands;

    //Исполняемая команда
    Commands command;

    //Завершает выполнение команды подачи напряжения
    private static boolean interrupt;

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

    //Список TableColumn для испытываемых счётчиков в разных направлениях
    private List<TableColumn<Meter.CommandResult, String>> tabColumnListAPPls = new ArrayList<>();
    private List<TableColumn<Meter.CommandResult, String>> tabColumnListAPMns = new ArrayList<>();
    private List<TableColumn<Meter.CommandResult, String>> tabColumnListRPPls = new ArrayList<>();
    private List<TableColumn<Meter.CommandResult, String>> tabColumnListRPMns = new ArrayList<>();

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

    @FXML
    void actionEventSaveExit(ActionEvent event) {

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
    void actionEventTestControl(ActionEvent event) {
        try {
            try {
                int phase;

                //------------------------------------------------------------------------------------------------
                //Логика работы автоматического режима работы
                if (event.getSource() == tglBtnAuto) {
                interrupt = true;

                try {
                    command.setInterrupt(true);
                }catch (NullPointerException ignored){
                }

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

                        startTestOnSelectPane(tabViewTestPointsAPPls, tabColumnListAPPls, commandsAPPls, constantMeterAP, phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

                    //Если выбрана панель AP-
                    } else if(tglBtnAPMns.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 1;
                        } else phase = 0;

                        startTestOnSelectPane(tabViewTestPointsAPMns, tabColumnListAPMns, commandsAPMns, constantMeterAP, phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

                    //Если выбрана панель RP+
                    } else if(tglBtnRPPls.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 5;
                        } else phase = 7;

                        startTestOnSelectPane(tabViewTestPointsRPPls, tabColumnListRPPls, commandsRPPls, constantMeterRP, phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);

                    //Если выбрана панель RP-
                    } else if(tglBtnRPMns.isSelected()) {

                        if (typeCircuitThreePhase) {
                            phase = 5;
                        } else phase = 7;

                        startTestOnSelectPane(tabViewTestPointsRPMns, tabColumnListRPMns, commandsRPMns, constantMeterRP, phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);
                    }
                }

                //------------------------------------------------------------------------------------------------
                //Логика работы ручного режима работы
                if (event.getSource() == tglBtnManualMode) {
                    interrupt = true;
                    try {
                        command.setInterrupt(true);
                    }catch (NullPointerException ignored){
                    }

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

                        startContinuousTestOnSelectPane(tabViewTestPointsAPPls, tabColumnListAPPls,commandsAPPls, constantMeterAP, phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

                        //Если выбрана панель AP-
                    } else if(tglBtnAPMns.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 1;
                        } else phase = 0;

                        startContinuousTestOnSelectPane(tabViewTestPointsAPMns, tabColumnListAPMns, commandsAPMns, constantMeterAP, phase, timeToCreepTestGOSTAP, timeToStartTestGOSTAP);

                        //Если выбрана панель RP+
                    } else if(tglBtnRPPls.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 5;
                        } else phase = 7;

                        startContinuousTestOnSelectPane(tabViewTestPointsRPPls, tabColumnListRPPls, commandsRPPls, constantMeterRP, phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);

                        //Если выбрана панель RP-
                    } else if(tglBtnRPMns.isSelected()) {

                        if (typeCircuitThreePhase) {
                            phase = 5;
                        } else phase = 7;

                        startContinuousTestOnSelectPane(tabViewTestPointsRPMns,  tabColumnListRPMns, commandsRPMns, constantMeterRP, phase, timeToCreepTestGOSTRP, timeToStartTestGOSTRP);
                    }
                }

                //------------------------------------------------------------------------------------------------
                //Логика работы подачи напряжения
                if (event.getSource() == tglBtnUnom) {
                    interrupt = true;
                    try {
                        command.setInterrupt(true);
                    }catch (NullPointerException ignored){
                    }

                    tglBtnAuto.setDisable(false);
                    tglBtnAuto.setSelected(false);

                    tglBtnManualMode.setDisable(false);
                    tglBtnManualMode.setSelected(false);

                    tglBtnUnom.setSelected(true);
                    tglBtnUnom.setDisable(true);

                    interrupt = false;

                    if (typeCircuitThreePhase) {
                        phase = 1;
                    } else {
                        phase = 0;
                    }

                    if (!stendDLLCommands.getUI(phase, Un, 0, Fn, 0, 0, 100.0, 0, "H", "1.0")) throw new ConnectForStendExeption();

                }

                //------------------------------------------------------------------------------------------------
                //Логика работы остановки теста
                if (event.getSource() == btnStop) {

                    tglBtnAuto.setDisable(false);
                    tglBtnAuto.setSelected(false);
                    tglBtnManualMode.setDisable(false);
                    tglBtnManualMode.setSelected(false);
                    tglBtnUnom.setDisable(false);
                    tglBtnUnom.setSelected(false);

                    interrupt = true;
                    try {
                        command.setInterrupt(true);
                    }catch (NullPointerException ignored){
                    }

                    throw new InterruptedTestException();
                }

            //Если остановка потока или пользователь нажал кнопку стоп или сменил режим работы
            }catch (InterruptedTestException | InterruptedException e) {
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            }
        //Если разорвана связь со стендом
        }catch (ConnectForStendExeption e){

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/exceptionFrame.fxml/"));
            try {
                fxmlLoader.load();
            } catch (IOException er) {
                e.printStackTrace();
            }

            ExceptionFrameController exceptionFrameController = fxmlLoader.getController();
            exceptionFrameController.getExceptionLabel().setText("Потеряна связь с установной,\nпроверьте подключение");

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Ошибка");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            tglBtnAuto.setSelected(false);
            tglBtnManualMode.setSelected(false);
            tglBtnUnom.setSelected(false);
        }
    }

    //Старт автоматического теста
    private void startTestOnSelectPane(TableView<Commands> tabViewTestPoints, List<TableColumn<Meter.CommandResult, String>> errorColumnList,
                                       ObservableList<Commands> commands,int constant, int phase, long timeCRPForGOST, long timeSTAForGOST)
            throws InterruptedTestException, ConnectForStendExeption, InterruptedException {

        int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();

        while (i < commands.size()) {
            command = commands.get(i);

            //Если тестовая точка активна
            if (command.isActive()) {

                if (command instanceof ErrorCommand) {


                    initAllParamForErrorCommand((ErrorCommand) command, errorColumnList, i);

                    command.setInterrupt(false);
                    command.execute();
                }

                if (command instanceof CreepCommand) {
                    /**
                     * Подумать над отображением времени теста
                     */

                    initAllParamForCreepCommand((CreepCommand) command, errorColumnList, constant, phase, i, timeCRPForGOST);

                    command.setInterrupt(false);
                    command.execute();
                }

                if (command instanceof StartCommand) {
                    /**
                     * Подумать над отображением времени теста
                     */
                    initAllParamForStartCommand((StartCommand) command, errorColumnList, constant,phase, i, timeSTAForGOST);

                    command.setInterrupt(false);
                    command.execute();
                }

                if (command instanceof RTCCommand) {

                    initAllParamForRTCCommand((RTCCommand) command, errorColumnList, phase, i);

                    command.setInterrupt(false);
                    command.execute();
                }
            }

            i++;
            //Устанавливаю строчку на следующей команде
            /**
             * Возможно нужно будет добавить и для окна ошибок авто фокусировку
             */

            tabViewTestPoints.getSelectionModel().select(i);
        }
    }

    //Метод для того чтобы узнать выбран ли какой-то режим работы уже

    //Старт автоматического теста
    private void startContinuousTestOnSelectPane(TableView<Commands> tabViewTestPoints, List<TableColumn<Meter.CommandResult, String>> tableColumErrorList,
                                                 ObservableList<Commands> commands, int constant, int phase, long timeCRPForGOST, long timeSTAForGOST)
            throws InterruptedTestException, ConnectForStendExeption, InterruptedException {

        int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();

        command = commands.get(i);


        if (command instanceof ErrorCommand) {


            initAllParamForErrorCommand((ErrorCommand) command, tableColumErrorList, i);

            command.setInterrupt(false);
            command.executeForContinuousTest();
        }

        if (command instanceof CreepCommand) {

            initAllParamForCreepCommand((CreepCommand) command, tableColumErrorList, constant, phase, i, timeCRPForGOST);

            command.setInterrupt(false);
            command.executeForContinuousTest();
        }

        if (command instanceof StartCommand) {
            /**
             * Подумать над отображением времени теста
             */
            initAllParamForStartCommand((StartCommand) command, tableColumErrorList,constant,phase, i, timeSTAForGOST);

            command.setInterrupt(false);
            command.executeForContinuousTest();
        }

        if (command instanceof RTCCommand) {

            initAllParamForRTCCommand((RTCCommand) command, tableColumErrorList, phase, i);

            command.setInterrupt(false);
            command.executeForContinuousTest();
        }
    }

    /**Костанту нужно определять для кажого метода одинаково
     */

    //Инициализирует параметры необходимые для снятия погрешности в точке
    private void initAllParamForErrorCommand(ErrorCommand errorCommand, List<TableColumn<Meter.CommandResult, String>> errorColumnList, int index){
        errorCommand.setStendDLLCommands(stendDLLCommands);
        errorCommand.setRatedVolt(Un);
        errorCommand.setIb(Ib);
        errorCommand.setImax(Imax);
        errorCommand.setRatedFreq(Fn);
        errorCommand.setIndex(index);
        errorCommand.setMeterForTestList(listMetersForTest);
        errorCommand.setTableColumnError(errorColumnList);
    }

    //Инициализирует параметры необходимые для команды самохода
    private void initAllParamForCreepCommand(CreepCommand creepCommand, List<TableColumn<Meter.CommandResult, String>> errorColumnList, int meterConstant, int phase, int index, long timeForGOSTtest){
        creepCommand.setStendDLLCommands(stendDLLCommands);
        creepCommand.setPhase(phase);
        creepCommand.setRatedVolt(Un);
        creepCommand.setRatedFreq(Fn);
//        creepCommand.setConstMeterForTest(meterConstant);
//        creepCommand.setMaxCurrMeter(Imax);
//        creepCommand.setThreePhaseMeter(typeCircuitThreePhase);
        creepCommand.setIndex(index);
        creepCommand.setMeterList(listMetersForTest);
        creepCommand.setTableColumnError(errorColumnList);

        if (creepCommand.isGostTest()) {
            creepCommand.setTimeForTest(timeForGOSTtest);
        }
    }

    //Инициализирует параметры необходимые для команды чувствительность
    private void initAllParamForStartCommand(StartCommand startCommand, List<TableColumn<Meter.CommandResult, String>> errorColumnList, int meterConstant, int phase, int index, long timeForGOSTtest) {
        startCommand.setStendDLLCommands(stendDLLCommands);
        startCommand.setPhase(phase);
        startCommand.setRatedFreq(Fn);
        startCommand.setRatedVolt(Un);
//        startCommand.setAccuracyClass(accuracyClassAP); //Почему класс точности AP?
//        startCommand.setThreePhaseMeter(typeCircuitThreePhase);
//        startCommand.setBaseCurrMeter(Ib);
//        startCommand.setConstMeterForTest(meterConstant);
//        startCommand.setTransfDetectShunt(typeOfMeasuringElementShunt);
        startCommand.setIndex(index);
        startCommand.setMeterList(listMetersForTest);
        startCommand.setTableColumnError(errorColumnList);

        if (startCommand.isGostTest()) {
            startCommand.setTimeForTest(timeForGOSTtest);
        }
    }

    //Инициализирует параметры необходимые для команды чувстви
    private void initAllParamForRTCCommand(RTCCommand rTCCommand, List<TableColumn<Meter.CommandResult, String>> errorColumnList, int phase, int index) {
        rTCCommand.setStendDLLCommands(stendDLLCommands);
        rTCCommand.setPhase(phase);
        rTCCommand.setRatedVolt(Un);
        rTCCommand.setIndex(index);
        rTCCommand.setMeterList(listMetersForTest);
        rTCCommand.setTableColumnError(errorColumnList);
    }

    /**
     * Для кнопки начать тест сделать цикл из листа точек
     * начиная с выбранного индекса пользователем  до конца с проверкой на дисайбл
     */
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

        tabColTestPointsDisAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commands, Boolean>, ObservableValue<Boolean>>() {
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
        });

        tabColTestPointsDisAPPls.setCellFactory(new Callback<TableColumn<Commands, Boolean>, //
                TableCell<Commands, Boolean>>() {
            @Override
            public TableCell<Commands, Boolean> call(TableColumn<Commands, Boolean> p) {
                CheckBoxTableCell<Commands, Boolean> cell = new CheckBoxTableCell<Commands, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        tabColTestPointsDisAPPls.setSortable(false);

        //AP-
        tabViewTestPointsAPMns.setEditable(true);

        tabColTestPointsDisAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commands, Boolean>, ObservableValue<Boolean>>() {
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
        });

        tabColTestPointsDisAPMns.setCellFactory(new Callback<TableColumn<Commands, Boolean>, //
                TableCell<Commands, Boolean>>() {
            @Override
            public TableCell<Commands, Boolean> call(TableColumn<Commands, Boolean> p) {
                CheckBoxTableCell<Commands, Boolean> cell = new CheckBoxTableCell<Commands, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        tabColTestPointsDisAPMns.setSortable(false);

        //RP+

        tabViewTestPointsRPPls.setEditable(true);

        tabColTestPointsDisRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commands, Boolean>, ObservableValue<Boolean>>() {
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
        });

        tabColTestPointsDisRPPls.setCellFactory(new Callback<TableColumn<Commands, Boolean>, //
                TableCell<Commands, Boolean>>() {
            @Override
            public TableCell<Commands, Boolean> call(TableColumn<Commands, Boolean> p) {
                CheckBoxTableCell<Commands, Boolean> cell = new CheckBoxTableCell<Commands, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        tabColTestPointsDisRPPls.setSortable(false);

        //RP-
        tabViewTestPointsRPMns.setEditable(true);

        tabColTestPointsDisRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commands, Boolean>, ObservableValue<Boolean>>() {
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
        });

        tabColTestPointsDisRPMns.setCellFactory(new Callback<TableColumn<Commands, Boolean>, //
                TableCell<Commands, Boolean>>() {
            @Override
            public TableCell<Commands, Boolean> call(TableColumn<Commands, Boolean> p) {
                CheckBoxTableCell<Commands, Boolean> cell = new CheckBoxTableCell<Commands, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        tabColTestPointsDisRPMns.setSortable(false);

        //-----------------------------------------------------------
        //В зависимости от количества счётчиков инициализирую поля для отображения погрешности
        TableView<Meter.CommandResult> tableView;
        TableColumn<Meter.CommandResult, String> column;


        for (int i = 0; i < listMetersForTest.size(); i++) {

            //Создаю колонки счётчиков для splitPane AP+
            tableView = new TableView<>();
            column = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            column.setStyle( "-fx-alignment: CENTER;");
            column.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
            column.setSortable(false);
            column.setPrefWidth(paneErrorsAPPls.getPrefWidth() / listMetersForTest.size());
            tableView.setPrefSize(paneErrorsAPPls.getPrefWidth() / listMetersForTest.size(), paneErrorsAPPls.getPrefHeight());
            tableView.setLayoutX(i * column.getPrefWidth());
            tableView.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("styleCSS/hideScrollBars.css")));
            tableView.setItems(FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPPls()));
            tableView.getColumns().add(column);
            tabViewListAPPls.add(tableView);
            tabColumnListAPPls.add(column);
            paneErrorsAPPls.getChildren().add(tableView);

            //Создаю колонки счётчиков для splitPane AP-
            tableView = new TableView<>();
            column = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            column.setStyle( "-fx-alignment: CENTER;");
            column.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
            column.setSortable(false);
            column.setPrefWidth(paneErrorsAPMns.getPrefWidth() / listMetersForTest.size());
            tableView.setPrefSize(paneErrorsAPMns.getPrefWidth() / listMetersForTest.size(), paneErrorsAPMns.getPrefHeight());
            tableView.setLayoutX(i * column.getPrefWidth());
            tableView.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("styleCSS/hideScrollBars.css")));
            tableView.setItems(FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPMns()));
            tableView.getColumns().add(column);
            tabViewListAPMns.add(tableView);
            tabColumnListAPMns.add(column);
            paneErrorsAPMns.getChildren().add(tableView);

            //Создаю колонки счётчиков для splitPane RP+
            tableView = new TableView<>();
            column = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            column.setStyle( "-fx-alignment: CENTER;");
            column.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
            column.setSortable(false);
            column.setPrefWidth(paneErrorsRPPls.getPrefWidth() / listMetersForTest.size());
            tableView.setPrefSize(paneErrorsRPPls.getPrefWidth() / listMetersForTest.size(), paneErrorsRPPls.getPrefHeight());
            tableView.setLayoutX(i * column.getPrefWidth());
            tableView.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("styleCSS/hideScrollBars.css")));
            tableView.setItems(FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPPls()));
            tableView.getColumns().add(column);
            tabViewListRPPls.add(tableView);
            tabColumnListRPPls.add(column);
            paneErrorsRPPls.getChildren().add(tableView);

            //Создаю колонки счётчиков для splitPane RP-
            tableView = new TableView<>();
            column = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            column.setStyle( "-fx-alignment: CENTER;");
            column.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
            column.setSortable(false);
            column.setPrefWidth(paneErrorsRPMns.getPrefWidth() / listMetersForTest.size());
            tableView.setPrefSize(paneErrorsRPMns.getPrefWidth() / listMetersForTest.size(), paneErrorsRPMns.getPrefHeight());
            tableView.setLayoutX(i * column.getPrefWidth());
            tableView.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("styleCSS/hideScrollBars.css")));
            tableView.setItems(FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPMns()));
            tableView.getColumns().add(column);
            tabViewListRPMns.add(tableView);
            tabColumnListRPMns.add(column);
            paneErrorsRPMns.getChildren().add(tableView);
        }

        //--------------------------------------------------------------------
        //Устанавливаю фокусировку на окне ошибок такуюже как и в окне точек
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
                    }
                }
            }
        });


        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewListAPPls.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsAPPls = tabViewListAPPls.get(i).getSelectionModel().getSelectedCells();

            int finalI = i;
            tableErrorPositionsAPPls.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {
                    TableView<Meter.CommandResult> tableView1 = tabViewListAPPls.get(finalI);
                    int index = tableView1.getSelectionModel().getFocusedIndex();

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
                    }
                }
            }
        });

        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewListAPMns.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsAPMns = tabViewListAPMns.get(i).getSelectionModel().getSelectedCells();

            int finalI = i;
            tableErrorPositionsAPMns.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {
                    TableView<Meter.CommandResult> tableView1 = tabViewListAPMns.get(finalI);
                    int index = tableView1.getSelectionModel().getFocusedIndex();

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
                    }
                }
            }
        });

        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewListRPPls.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsRPPls = tabViewListRPPls.get(i).getSelectionModel().getSelectedCells();

            int finalI = i;
            tableErrorPositionsRPPls.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {
                    TableView<Meter.CommandResult> tableView1 = tabViewListRPPls.get(finalI);
                    int index = tableView1.getSelectionModel().getFocusedIndex();

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
                    }
                }
            }
        });

        //Устанавливаю общий селект для таблиц с погрешностью
        for (int i = 0; i < tabViewListRPMns.size(); i++) {

            ObservableList<TablePosition> tableErrorPositionsRPMns = tabViewListRPMns.get(i).getSelectionModel().getSelectedCells();

            int finalI = i;
            tableErrorPositionsRPMns.addListener(new ListChangeListener<TablePosition>() {
                @Override
                public void onChanged(Change<? extends TablePosition> c) {
                    TableView<Meter.CommandResult> tableView1 = tabViewListRPMns.get(finalI);
                    int index = tableView1.getSelectionModel().getFocusedIndex();

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

    //Находит все скрол бары
    void initScrolBars() {
        ScrollBar verticalBarCommands;
        ScrollBar verticalBarErrors;

        //Получаю скрол бары определённого окна
        //AP+
        verticalBarCommands = (ScrollBar) tabViewTestPointsAPPls.lookup(".scroll-bar:vertical");

        for (TableView<Meter.CommandResult> tableViewError : tabViewListAPPls) {
            verticalBarErrors = (ScrollBar) tableViewError.lookup(".scroll-bar:vertical");

            bindScrolls(verticalBarCommands, verticalBarErrors);
        }

        //AP-
        verticalBarCommands = (ScrollBar) tabViewTestPointsAPMns.lookup(".scroll-bar:vertical");

        for (TableView<Meter.CommandResult> tableViewError : tabViewListAPMns) {
            verticalBarErrors = (ScrollBar) tableViewError.lookup(".scroll-bar:vertical");

            bindScrolls(verticalBarCommands, verticalBarErrors);
        }


        //RP+
        verticalBarCommands = (ScrollBar) tabViewTestPointsRPPls.lookup(".scroll-bar:vertical");

        for (TableView<Meter.CommandResult> tableViewError : tabViewListRPPls) {
            verticalBarErrors = (ScrollBar) tableViewError.lookup(".scrol-bar:vertical");

            bindScrolls(verticalBarCommands, verticalBarErrors);
        }

        //RP-
        verticalBarCommands = (ScrollBar) tabViewTestPointsRPMns.lookup(".scroll-bar:vertical");

        for (TableView<Meter.CommandResult> tableViewError : tabViewListRPMns) {
            verticalBarErrors = (ScrollBar) tableViewError.lookup(".scroll-bar:vertical");

            bindScrolls(verticalBarCommands, verticalBarErrors);
        }
    }

    //Делает проверку и привязывает скроллы друг к другу
    private void bindScrolls(ScrollBar verticalBarCommands, ScrollBar verticalBarErrors) {
        if (verticalBarCommands != null && verticalBarErrors != null) {
            verticalBarCommands.valueProperty().bindBidirectional(verticalBarErrors.valueProperty());
        }
    }

    //Формула для расчёта времени теста на самоход по ГОСТУ
    public long initTimeForCreepTestGOST(int constMeterForTest) {
        int amountMeasElem;

        if (typeCircuitThreePhase) {
            amountMeasElem = 3;
        } else amountMeasElem = 1;

//        System.out.println("typeCircuitThreePhase = " + typeCircuitThreePhase);
//        System.out.println("amountMeasElem = " + amountMeasElem);
//        System.out.println("Imax = " + Imax);
//        System.out.println("Un = " + Un);
//        System.out.println("constMeterForTest = " + constMeterForTest);

        double formulaResult = 600000000 / (constMeterForTest * amountMeasElem * Un * Imax);

        //Округляю результат до 3х знаков
        BigDecimal bigDecimalResult = new BigDecimal(String.valueOf(formulaResult)).setScale(3, BigDecimal.ROUND_CEILING);

//        System.out.println("bigDecimalResult = " + bigDecimalResult);

        String[] timeArr = String.valueOf(bigDecimalResult).split("\\.");

        //Округляю значение после запятой до целых секунд
        BigDecimal bigDecimal = new BigDecimal(Integer.parseInt(timeArr[1]) * 0.06).setScale(0, BigDecimal.ROUND_CEILING);

        return  ((Integer.parseInt(timeArr[0]) * 60) + bigDecimal.intValue()) * 1000;
    }

    //Расчётная формула времени теста на чувствительность по ГОСТ
    public long initTimeForStartGOSTTest(double accuracyClass, int constMeterForTest) {
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

//        System.out.println("amountMeasElem = " + amountMeasElem);
//        System.out.println("Стартовый ток = " + ratedCurr);
//        System.out.println("Класс точности = " + accuracyClass);

        double formulaResult = 2.3 * (60000 / (constMeterForTest * amountMeasElem * Un * ratedCurr));

//        System.out.println("formulaResult = " + formulaResult);

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
}
