package stend.controller.viewController;

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
import stend.controller.Commands.*;
import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.exeptions.ConnectForStendExeption;
import stend.helper.exeptions.InterruptedTestException;
import stend.model.Methodic;

import java.io.IOException;
import java.util.List;

public class TestErrorTableFrameController {

    StendDLLCommands stendDLLCommands;

    //Исполняемая команда
    Commands command;

    //Завершает выполнение команды подачи напряжения
    boolean interrupt;

    private List<Meter> listMetersForTest;

    private Methodic methodic;

    private ObservableList<Commands> commandsAPPls;
    private ObservableList<Commands> commandsAPMns;
    private ObservableList<Commands> commandsRPPls;
    private ObservableList<Commands> commandsRPMns;

    private double Imax;

    private double Ib;

    private double Un;

    private double Fn;

    //Тип измерительного элемента счётчика шунт/трансформатор
    private boolean typeOfMeasuringElementShunt;

    //Тип сети трёхфазная или однофазная
    private boolean typeCircuitThreePhase;

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
    private TableView<Meter.CommandResult> tabViewErrorsAPPls;

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
    private TableView<Meter.CommandResult> tabViewErrorsAPMns;

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
    private TableView<Meter.CommandResult> tabViewErrorsRPPls;

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
    private TableView<Meter.CommandResult> tabViewErrorsRPMns;

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

                int constant;

                    //Если выбрана панель AP+
                    if (tglBtnAPPls.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 1;
                        } else phase = 0;

                        constant = Integer.parseInt(listMetersForTest.get(0).getConstantMeterAP());

                        startTestOnSelectPane(tabViewTestPointsAPPls, tabViewErrorsAPPls, commandsAPPls, constant, phase);

                    //Если выбрана панель AP-
                    } else if(tglBtnAPMns.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 1;
                        } else phase = 0;

                        constant = Integer.parseInt(listMetersForTest.get(0).getConstantMeterAP());

                        startTestOnSelectPane(tabViewTestPointsAPMns, tabViewErrorsAPMns, commandsAPMns, constant, phase);

                    } else if(tglBtnRPPls.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 5;
                        } else phase = 7;

                        constant = Integer.parseInt(listMetersForTest.get(0).getConstantMeterRP());

                        startTestOnSelectPane(tabViewTestPointsRPPls, tabViewErrorsRPPls, commandsRPPls, constant, phase);
                    } else if(tglBtnRPMns.isSelected()) {

                        if (typeCircuitThreePhase) {
                            phase = 5;
                        } else phase = 7;

                        constant = Integer.parseInt(listMetersForTest.get(0).getConstantMeterRP());

                        startTestOnSelectPane(tabViewTestPointsRPMns, tabViewErrorsRPMns, commandsRPMns, constant, phase);
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

                    int constant;

                    //Если выбрана панель AP+
                    if (tglBtnAPPls.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 1;
                        } else phase = 0;

                        constant = Integer.parseInt(listMetersForTest.get(0).getConstantMeterAP());

                        startContinuousTestOnSelectPane(tabViewTestPointsAPPls, commandsAPPls, constant, phase);

                        //Если выбрана панель AP-
                    } else if(tglBtnAPMns.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 1;
                        } else phase = 0;

                        constant = Integer.parseInt(listMetersForTest.get(0).getConstantMeterAP());

                        startContinuousTestOnSelectPane(tabViewTestPointsAPMns, commandsAPMns, constant, phase);

                    } else if(tglBtnRPPls.isSelected()) {
                        if (typeCircuitThreePhase) {
                            phase = 5;
                        } else phase = 7;

                        constant = Integer.parseInt(listMetersForTest.get(0).getConstantMeterRP());

                        startContinuousTestOnSelectPane(tabViewTestPointsRPPls, commandsRPPls, constant, phase);
                    } else if(tglBtnRPMns.isSelected()) {

                        if (typeCircuitThreePhase) {
                            phase = 5;
                        } else phase = 7;

                        constant = Integer.parseInt(listMetersForTest.get(0).getConstantMeterRP());

                        startContinuousTestOnSelectPane(tabViewTestPointsRPMns, commandsRPMns, constant, phase);
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

                    if (!stendDLLCommands.getUI(phase, Un, 0, Fn, 0, 0, 0, 0, "H", "1.0")) throw new ConnectForStendExeption();

                    while (!interrupt) {
                        Thread.sleep(200);
                    }

                    throw new InterruptedException();
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
                }

            //Если остановка потока или пользователь нажал кнопку стоп или сменил режим работы
            }catch (InterruptedTestException | InterruptedException e) {
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            }
        //Если разорвана связь со стендом
        }catch (ConnectForStendExeption e){

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/stend/view/exceptionFrame.fxml/"));
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
    private void startTestOnSelectPane(TableView<Commands> tabViewTestPoints, TableView<Meter.CommandResult> tabViewErrors, ObservableList<Commands> commands,int constant, int phase)
            throws InterruptedTestException, ConnectForStendExeption, InterruptedException {

        int i = tabViewTestPoints.getSelectionModel().getFocusedIndex();
        //int i = tabViewTestPointsAPPls.getSelectionModel().getFocusedIndex();

        while (i < commands.size()) {
            //while (i < commandsAPPls.size()) {
            command = commands.get(i);
            //command = commandsAPPls.get(i);

            //Если тестовая точка активна
            if (command.isActive()) {

                if (command instanceof ErrorCommand) {


                    initAllParamForErrorCommand((ErrorCommand) command, i);

                    command.setInterrupt(false);
                    command.execute();
                }

                if (command instanceof CreepCommand) {
                    /**
                     * Подумать над отображением времени теста
                     */

                    initAllParamForStartCommand((CreepCommand) command, constant, phase, i);
                    //Integer.parseInt(listMetersForTest.get(0).getConstantMeterAP()

                    command.setInterrupt(false);
                    command.execute();
                }

                if (command instanceof StartCommand) {
                    /**
                     * Подумать над отображением времени теста
                     */
                    initAllParamForStartCommand((StartCommand) command, constant,phase, i);

                    command.setInterrupt(false);
                    command.execute();
                }

                if (command instanceof RTCCommand) {

                    initAllParamForRTCCommand((RTCCommand) command, phase, i);

                    command.setInterrupt(false);
                    command.execute();
                }
            }

            i++;
            //Устанавливаю строчку на следующей команде
            /**
             * Возможно нужно будет добавить и для окна ошибок авто фокусировку
             */

            tabViewErrors.getFocusModel().focus(i);
            tabViewTestPoints.getFocusModel().focus(i);
//            tabViewErrorsAPPls.getFocusModel().focus(i);
//            tabViewTestPointsAPPls.getFocusModel().focus(i);
        }
    }

    //Метод для того чтобы узнать выбран ли какой-то режим работы уже

    //Старт автоматического теста
    private void startContinuousTestOnSelectPane(TableView<Commands> tabViewTestPoints, ObservableList<Commands> commands,int constant, int phase)
            throws InterruptedTestException, ConnectForStendExeption, InterruptedException {

        int i = tabViewTestPoints.getSelectionModel().getFocusedIndex();

        command = commands.get(i);


        if (command instanceof ErrorCommand) {


            initAllParamForErrorCommand((ErrorCommand) command, i);

            command.setInterrupt(false);
            command.executeForContinuousTest();
        }

        if (command instanceof CreepCommand) {
            /**
             * Подумать над отображением времени теста
             */

            initAllParamForStartCommand((CreepCommand) command, constant, phase, i);

            command.setInterrupt(false);
            command.executeForContinuousTest();
        }

        if (command instanceof StartCommand) {
            /**
             * Подумать над отображением времени теста
             */
            initAllParamForStartCommand((StartCommand) command, constant,phase, i);

            command.setInterrupt(false);
            command.executeForContinuousTest();
        }

        if (command instanceof RTCCommand) {

            initAllParamForRTCCommand((RTCCommand) command, phase, i);

            command.setInterrupt(false);
            command.executeForContinuousTest();
        }
    }



    /**Костанту нужно определять для кажого метода одинаково
     */

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
    private void initAllParamForStartCommand(CreepCommand creepCommand, int meterConstant,  int phase, int index){
        creepCommand.setStendDLLCommands(stendDLLCommands);
        creepCommand.setPhase(phase);
        creepCommand.setRatedVolt(Un);
        creepCommand.setRatedFreq(Fn);
        creepCommand.setConstMeterForTest(meterConstant);
        creepCommand.setMaxCurrMeter(Imax);
        creepCommand.setThreePhaseMeter(typeCircuitThreePhase);
        creepCommand.setIndex(index);
        creepCommand.setMeterList(listMetersForTest);
    }

    //Инициализирует параметры необходимые для команды чувствительность
    private void initAllParamForStartCommand(StartCommand startCommand, int meterConstant, int phase, int index) {
        startCommand.setStendDLLCommands(stendDLLCommands);
        startCommand.setPhase(phase);
        startCommand.setRatedFreq(Fn);
        startCommand.setRatedVolt(Un);
        startCommand.setAccuracyClass(accuracyClassAP);
        startCommand.setThreePhaseMeter(typeCircuitThreePhase);
        startCommand.setBaseCurrMeter(Ib);
        startCommand.setConstMeterForTest(meterConstant);
        startCommand.setTransfDetectShunt(typeOfMeasuringElementShunt);
        startCommand.setIndex(index);
        startCommand.setMeterList(listMetersForTest);
    }

    //Инициализирует параметры необходимые для команды чувстви
    private void initAllParamForRTCCommand(RTCCommand rTCCommand, int phase, int index) {
        rTCCommand.setStendDLLCommands(stendDLLCommands);
        rTCCommand.setPhase(phase);
        rTCCommand.setRatedVolt(Un);
        rTCCommand.setIndex(index);
        rTCCommand.setMeterList(listMetersForTest);
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
        for (int i = 0; i < listMetersForTest.size(); i++) {

            //Создаю колонки счётчиков для splitPane AP+
            TableColumn<Meter.CommandResult, String> tableColumnAPPls = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnAPPls.setStyle( "-fx-alignment: CENTER;");
            tableColumnAPPls.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
            tableColumnAPPls.setSortable(false);
            ObservableList<Meter.CommandResult> observableListAPPls = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPPls());
            tabViewErrorsAPPls.setItems(observableListAPPls);
            tabViewErrorsAPPls.getColumns().add(tableColumnAPPls);

            //Создаю колонки счётчиков для splitPane AP-
            TableColumn<Meter.CommandResult, String> tableColumnAPMns = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnAPMns.setStyle( "-fx-alignment: CENTER;");
            tableColumnAPMns.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
            tableColumnAPMns.setSortable(false);
            ObservableList<Meter.CommandResult> observableListAPMns = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPMns());
            tabViewErrorsAPMns.setItems(observableListAPMns);
            tabViewErrorsAPMns.getColumns().add(tableColumnAPMns);

            //Создаю колонки счётчиков для splitPane RP+
            TableColumn<Meter.CommandResult, String> tableColumnRPPls = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnRPPls.setStyle( "-fx-alignment: CENTER;");
            tableColumnRPPls.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
            tableColumnRPPls.setSortable(false);
            ObservableList<Meter.CommandResult> observableListRPPls = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPPls());
            tabViewErrorsRPPls.setItems(observableListRPPls);
            tabViewErrorsRPPls.getColumns().add(tableColumnRPPls);

            //Создаю колонки счётчиков для splitPane RP-
            TableColumn<Meter.CommandResult, String> tableColumnRPMns = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnRPMns.setStyle( "-fx-alignment: CENTER;");
            tableColumnRPMns.setCellValueFactory(new PropertyValueFactory<>("lastResult"));
            tableColumnRPMns.setSortable(false);
            ObservableList<Meter.CommandResult> observableListRPMns = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPMns());
            tabViewErrorsRPMns.setItems(observableListRPMns);
            tabViewErrorsRPMns.getColumns().add(tableColumnRPMns);
        }

        //--------------------------------------------------------------------
        //Устанавливаю фокусировку на окне ошибок такуюже как и в окне точек
        //AP+
        tabViewTestPointsAPPls.getSelectionModel().select(0);
        tabViewErrorsAPPls.getSelectionModel().select(0);

        ObservableList<TablePosition> tablePositionsAPPls = tabViewTestPointsAPPls.getSelectionModel().getSelectedCells();

        tablePositionsAPPls.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsAPPls.getSelectionModel().getFocusedIndex();

                tabViewErrorsAPPls.getSelectionModel().select(i);
                tabViewErrorsAPPls.getFocusModel().focus(i);
            }
        });

        //AP-
        tabViewTestPointsAPMns.getSelectionModel().select(0);
        tabViewErrorsAPMns.getSelectionModel().select(0);

        ObservableList<TablePosition> tablePositionsAPMns = tabViewTestPointsAPMns.getSelectionModel().getSelectedCells();

        tablePositionsAPMns.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsAPMns.getSelectionModel().getFocusedIndex();

                tabViewErrorsAPMns.getSelectionModel().select(i);
                tabViewErrorsAPMns.getFocusModel().focus(i);
            }
        });

        //RP+
        tabViewTestPointsRPPls.getSelectionModel().select(0);
        tabViewErrorsRPPls.getSelectionModel().select(0);

        ObservableList<TablePosition> tablePositionsRPPls = tabViewTestPointsRPPls.getSelectionModel().getSelectedCells();

        tablePositionsRPPls.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsRPPls.getSelectionModel().getFocusedIndex();

                tabViewErrorsRPPls.getSelectionModel().select(i);
                tabViewErrorsRPPls.getFocusModel().focus(i);
            }
        });

        //AP-
        tabViewTestPointsRPMns.getSelectionModel().select(0);
        tabViewErrorsRPMns.getSelectionModel().select(0);

        ObservableList<TablePosition> tablePositionsRPMns = tabViewTestPointsRPMns.getSelectionModel().getSelectedCells();

        tablePositionsRPMns.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsRPMns.getSelectionModel().getFocusedIndex();

                tabViewErrorsRPMns.getSelectionModel().select(i);
                tabViewErrorsRPMns.getFocusModel().focus(i);
            }
        });
    }

    //Добавляет объект error к каждому счётчику необходимому для теста
    private void initErrorsForMeters() {
        for (Meter meter : listMetersForTest) {
            for (int i = 0; i < 4; i++) {
                for (Commands commandName : methodic.getCommandsMap().get(i)) {
                    meter.createError(commandName, i, commandName.getName());
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
        verticalBarErrors = (ScrollBar) tabViewErrorsAPPls.lookup(".scroll-bar:vertical");

        bindScrolls(verticalBarCommands, verticalBarErrors);

        //AP-
        verticalBarCommands = (ScrollBar) tabViewTestPointsAPMns.lookup(".scroll-bar:vertical");
        verticalBarErrors = (ScrollBar) tabViewErrorsAPMns.lookup(".scroll-bar:vertical");

        bindScrolls(verticalBarCommands, verticalBarErrors);

        //RP+
        verticalBarCommands = (ScrollBar) tabViewTestPointsRPPls.lookup(".scroll-bar:vertical");
        verticalBarErrors = (ScrollBar) tabViewErrorsRPPls.lookup(".scroll-bar:vertical");

        bindScrolls(verticalBarCommands, verticalBarErrors);

        //RP-
        verticalBarCommands = (ScrollBar) tabViewTestPointsRPMns.lookup(".scroll-bar:vertical");
        verticalBarErrors = (ScrollBar) tabViewErrorsRPMns.lookup(".scroll-bar:vertical");

        bindScrolls(verticalBarCommands, verticalBarErrors);
    }

    //Делает проверку и привязывает скроллы друг к другу
    private void bindScrolls(ScrollBar verticalBarCommands, ScrollBar verticalBarErrors) {
        if (verticalBarCommands != null && verticalBarErrors != null) {
            verticalBarCommands.valueProperty().bindBidirectional(verticalBarErrors.valueProperty());
        }
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

}
