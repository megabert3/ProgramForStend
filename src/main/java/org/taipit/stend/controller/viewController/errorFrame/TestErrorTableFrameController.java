package org.taipit.stend.controller.viewController.errorFrame;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.viewController.errorFrame.refMeter.OnePhaseStendRefParamController;
import org.taipit.stend.controller.viewController.errorFrame.refMeter.StendRefParametersForFrame;
import org.taipit.stend.controller.viewController.errorFrame.refMeter.ThreePhaseStendRefParamController;
import org.taipit.stend.helper.frameManager.FrameManager;
import org.taipit.stend.model.result.NotSavedResults;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.controller.viewController.SaveResultsTestFrame;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.StendConnectionException;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;
import org.taipit.stend.model.metodics.MethodicForThreePhaseStend;
import org.taipit.stend.model.metodics.Metodic;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * @autor Albert Khalimov
 *
 * Данный класс является контроллером окна "testErrorTableFrame.fxml".
 * Отвечает за обработку действий пользователя, а так же за отображение изменениЙ результата прохождения теста и самого результата теста
 */
public class TestErrorTableFrameController {

    //Статическая ссылка на кнопку СТОП, если внутри выполнения команды пойдёт что-то не так, то вызывается действие этой кнопки
    private static Button btnStopStatic;

    //Статическая ссылка на окно параметров получаемых из эталонного счётчика
    private StendRefParametersForFrame stendRefParametersForFrame;

    //Какую фазу необходимо использовать у трехфазного стенда, если на него навешан однофазный счётчик
    public static String phaseOnePhaseMode = ConsoleHelper.properties.getProperty("phaseOnOnePhaseMode");

    //Объект поверочной установки (стенда)
    private StendDLLCommands stendDLLCommands;

    //Окно с параметрами получаемыми из эталонного счётчика
    private Stage refMeterStage;

    //У этого стенда две токовые цепи?
    private boolean twoCircut;

    //Исполняемая на данный момент команда
    private Commands command;

    //Счётчики, которые необходимо протестировать
    private List<Meter> listMetersForTest;

    //Методика по которой необходимо протестировать счётчики
    private Metodic methodicForStend;

    //Флаг есть ли какие-то результаты, если да, то предлагаю сохранить при выходе
    public static boolean saveResults = false;

    //Флаг если появились новые результаты испытания
    public volatile static boolean newResults = false;

    //Директория сохранения несохранённых результатов
    private String dirWithNotSaveResults = ".\\src\\main\\resources\\mwrans";

    //Список команд из методики поверки для активной, реактивной энергии и прямого, обратного направления тока
    private ObservableList<Commands> commandsAPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> commandsAPMns = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> commandsRPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> commandsRPMns = FXCollections.observableArrayList(new ArrayList<>());

    //Список TableView c погрешностями для каждого счётчика
    private List<TableView<Meter.CommandResult>> tabViewErrorsList = new ArrayList<>();

    //======================================================== Параметры
    //Максимальный ток навешанных на стенд счётчиков
    private double Imax;

    //Базовый ток навешанных на стенд счётчиков
    private double Ib;

    //Номинальное напряжение навешанных на стенд счётчиков
    private double Un;

    //Номинальная частота навешанных на стенд счётчиков
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

    //Постоянная навешенных счётчиков для активной энергии (imp*kW*h)
    private int constantMeterAP;

    //Постоянная навешенных счётчиков для реактивной энергии (imp*kvar*h)
    private int constantMeterRP;

    //Класс точности измерения активной энергии счётчиков
    private double accuracyClassAP;

    //Класс точности измерения реактивной энергии счётчиков
    private double accuracyClassRP;

    //Поток для запуска автоматического теста (нажата кнопка автоматический режим)
    private Thread automaticTestThread = new Thread();

    //Поток для запуска ручного теста (нажата кнопка ручного режима)
    private Thread manualTestThread = new Thread();

    //Поток если необходимо просто подать напряжение на счётчики
    private Thread UnomThread = new Thread();

    //Поток получения информации от эталонного счётчика в ходе испытаний
    private Thread refMeterThread;

    //Поток блокировки кнопок
    private Thread blockButtonsThread;

    //Поток сериализации новых несохранённых результатов
    private Thread serializeNewNotSavedResults;

    //Флаг указывающий нажата ли кнопка подачи напряжения на счётчики
    private boolean startUnTest = false;

    //Для блокировки кнопок управления режимами работы установки. Чтобы пользователь не тыкал безумно на кнопки и давал установке стабилизировать параметры
    public static SimpleBooleanProperty blockBtns = new SimpleBooleanProperty(false);

    //Для блокировки кнопок изменения типа энергии или направления тока (нельзя менять уже в процессе запущенного теста)
    public static SimpleBooleanProperty blockTypeEnergyAndDirectionBtns = new SimpleBooleanProperty(false);


    //Лист с командами. Необходим для установки слушателя на список команд
    //для обработки нажатия ЛКМ на листе с командами.
    //В зависимости от режима работы разное поведение
    private ObservableList<Commands> selectedCommand;

    //Описание поведения нажатия ЛКМ на списке команд при автоматическом режиме работы теста
    private ListChangeListener<Commands> automaticListChangeListener = new ListChangeListener<Commands>() {
        @Override
        public void onChanged(Change<? extends Commands> c) {
            //Завершаю работу треда предыдущей точки испытания
            automaticTestThread.interrupt();

            blockTypeEnergyAndDirectionBtns.setValue(true);

            //Блокирую кнопки чтобы пользователь не насоздавал потоков
            blockControlBtns(8000);

            //Запускаю новый поток с другой точкой испытания, которую выбрал пользователь
            automaticTestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            stendDLLCommands.setRefClock(0);
                            stendDLLCommands.errorClear();

                            startAutomaticTest();
                        } catch (InterruptedException ignore) { }

                    } catch (StendConnectionException e) {
                        cathConnectionException(e);
                    }
                }
            });

            automaticTestThread.start();
        }
    };

    //Описание поведения нажатия ЛКМ на списке команд при ручном режиме работы теста
    private ListChangeListener<Commands> manualListChangeListener = new ListChangeListener<Commands>() {
        @Override
        public void onChanged(Change<? extends Commands> c) {

            //Завершаю работу треда предыдущей точки испытания
            manualTestThread.interrupt();

            blockTypeEnergyAndDirectionBtns.setValue(true);

            //Блокирую кнопки чтобы пользователь не насоздавал потоков
            blockControlBtns(8000);

            //Запускаю новый поток с другой точкой испытания, которую выбрал пользователь
            manualTestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            stendDLLCommands.setRefClock(0);
                            stendDLLCommands.errorClear();

                            startManualTest();
                        } catch (InterruptedException ignore) { }

                    } catch (StendConnectionException e) {
                        cathConnectionException(e);
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
    private Pane buttonPane;

    @FXML
    private Pane energyAndDirectionBtnsPane;

    @FXML
    private AnchorPane tabViewCommandsPane;

    //Инициализация окна
    @FXML
    void initialize() {

        ToggleGroup toggleGroup = new ToggleGroup();

        //Может быть только один режим работы теста
        tglBtnUnom.setToggleGroup(toggleGroup);
        tglBtnManualMode.setToggleGroup(toggleGroup);
        tglBtnAuto.setToggleGroup(toggleGroup);

        btnStopStatic = btnStop;

        //Получаю ссылку на выбранную команду
        selectedCommand = tabViewTestPoints.getSelectionModel().getSelectedItems();

        //Слушатель для блокировки кнопок выбора режима работы
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

        //Слушатель для юлокировки кнопок изменения энергии и направления
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

        //Принцип работы кнопки стоп
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                blockControlBtns(1500);

                //Завершаю рабочие потоки
                if (manualTestThread.isAlive()) {
                    manualTestThread.interrupt();
                }

                if (automaticTestThread.isAlive()) {
                    automaticTestThread.interrupt();
                }

                if (UnomThread.isAlive() || startUnTest) {
                    UnomThread.interrupt();
                }

                startUnTest = false;

                //Удаляю слушателей
                selectedCommand.removeListener(manualListChangeListener);
                selectedCommand.removeListener(automaticListChangeListener);

                try {
                    //Отчищаю табло погрешности на установке и отключаю мощность
                    stendDLLCommands.errorClear();
                    stendDLLCommands.powerOf();

                    //Выставляю кнопки в изначальное положение
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            tglBtnManualMode.setSelected(false);
                            tglBtnAuto.setSelected(false);
                            tglBtnUnom.setSelected(false);
                            blockTypeEnergyAndDirectionBtns.setValue(false);
                        }
                    });

                }catch (StendConnectionException e) {
                    cathConnectionException(e);
                }
            }
        });

        checBoxePane.toFront();
    }

    /**
     * Обработчик событий сохранения теста или выхода из теста
     * @param event
     */
    @FXML
    void actionEventSaveExit(ActionEvent event) {
        //Если нажата кнопка сохранить
        if (event.getSource() == btnSave) {

            //Если тест запущен необходимо его выключить
            if (blockTypeEnergyAndDirectionBtns.getValue() || startUnTest) {
                ConsoleHelper.infoException("Нельзя сохранить результаты во время теста");
                return;
            }

            //Загружаю окно сохранения теста
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/saveResultsTest.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                ConsoleHelper.infoException("Ошибка при загрузке окна сохранения теста");
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
                    saveResultsTestFrame.getBtnCancel().fire();
                }
            });

            //Скрываю это окно от пользователя, если он передумает и захочет вернуться к тесту
            btnSave.getScene().getWindow().hide();
            refMeterStage.hide();
        }

        //Если пользователь нажал кнопку выйти из теста
        if (event.getSource() == btnExit) {

            //Если в этот момент тест ещё идёт
            if (blockTypeEnergyAndDirectionBtns.getValue() || startUnTest) {
                ConsoleHelper.infoException("Необходимо остановить тест");

                //Если тест не идёт
            } else {

                //Если есть какие-то результаты теста
                if (saveResults) {
                    //Пердлагаю сохранить результаты теста
                    Boolean answer = ConsoleHelper.yesOrNoFrame("Сохранение результатов", "Сохранить результаты теста?");

                    if (answer != null) {

                        //Если пользователь нажал сохранить
                        if (answer) {
                            btnSave.fire();

                            //Если пользователь нажал не сохранять
                        } else {
                            refMeterThread.interrupt();
                            serializeNewNotSavedResults.interrupt();

                            Stage testErrorTableFrameControllerStage = (Stage) btnExit.getScene().getWindow();
                            refMeterStage.close();
                            testErrorTableFrameControllerStage.close();
                            FrameManager.frameManagerInstance().setTestErrorTableFrameController(null);
                        }
                    }

                } else {
                    refMeterThread.interrupt();
                    serializeNewNotSavedResults.interrupt();

                    Stage testErrorTableFrameControllerStage = (Stage) btnExit.getScene().getWindow();
                    refMeterStage.close();
                    testErrorTableFrameControllerStage.close();
                    FrameManager.frameManagerInstance().setTestErrorTableFrameController(null);
                }
            }
        }
    }

    /**
     * Если пользователь нажал показать окно эталонного счётчика.
     * Какие сейчас параметры выставленны
     * @param event
     */
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

    /**
     * Если пользователь нажал кнопку начать автоматический тест
     * @param event
     */
    @FXML
    void actionEventTestControl(ActionEvent event) {
        //------------------------------------------------------------------------------------------------
        //Логика работы автоматического режима работы

        if (event.getSource() == tglBtnAuto) {

            //Если автоматический тест уже идёт
            if (automaticTestThread.isAlive()) {
                tglBtnAuto.setSelected(true);
                return;

                //Если не идёт
            } else {
                blockTypeEnergyAndDirectionBtns.setValue(true);

                //Блокирую кнопки
                blockControlBtns(8000);

                startUnTest = false;

                //Если установка работает в других режимах, то завершаю их
                if (manualTestThread.isAlive()) {
                    manualTestThread.interrupt();
                    selectedCommand.removeListener(manualListChangeListener);
                }

                if (UnomThread.isAlive()) {
                    UnomThread.interrupt();
                }

                //Запускаю автоматический режим работы
                selectedCommand.addListener(automaticListChangeListener);

                automaticTestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stendDLLCommands.setRefClock(0);
                            stendDLLCommands.errorClear();

                            try {
                                startAutomaticTest();

                            } catch (InterruptedException ignore) { }

                        } catch (StendConnectionException e) {
                            cathConnectionException(e);
                        }
                    }
                });

                automaticTestThread.start();
            }
        }


        /**
         * Если пользователь нажал кнопку ручного режима работы
         * @param event
         */
        if (event.getSource() == tglBtnManualMode) {

            //Если ручной тест уже идёт
            if (manualTestThread.isAlive()) {
                tglBtnManualMode.setSelected(true);
                return;

                //Если нет
            } else {

                blockTypeEnergyAndDirectionBtns.setValue(true);

                //Блокирую кнопк
                blockControlBtns(8000);

                startUnTest = false;

                //Останавливаю другие режимы
                if (automaticTestThread.isAlive()) {
                    automaticTestThread.interrupt();
                    selectedCommand.removeListener(automaticListChangeListener);
                }

                if (UnomThread.isAlive()) {
                    UnomThread.interrupt();
                }

                //Запускаю ручной режим работы
                selectedCommand.addListener(manualListChangeListener);

                manualTestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stendDLLCommands.setRefClock(0);
                            stendDLLCommands.errorClear();

                            try {
                                startManualTest();

                            } catch (InterruptedException ignore) { }

                        } catch (StendConnectionException e) {
                            cathConnectionException(e);
                        }
                    }
                });
                manualTestThread.start();
            }
        }


        /**
         * Если пользователь нажал кнопку подачи напряжения на счётчики
         * @param event
         */
        if (event.getSource() == tglBtnUnom) {
            //Если тест уже идёт
            if (startUnTest) {
                tglBtnUnom.setSelected(true);

                //Если не идёт
            } else {

                //Блокирую кнопки
                blockControlBtns(5000);
                startUnTest = true;

                //Завершаю работу других режимов работы
                if (automaticTestThread.isAlive()) {
                    automaticTestThread.interrupt();
                    selectedCommand.removeListener(automaticListChangeListener);
                }

                if (manualTestThread.isAlive()) {
                    manualTestThread.interrupt();
                    selectedCommand.removeListener(manualListChangeListener);
                }

                //Запускаю подачу напряжения
                UnomThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            try {
                                stendDLLCommands.errorClear();

                                startUn();

                            } catch (InterruptedException e) {
                                startUnTest = false;
                            }
                        } catch (StendConnectionException e) {
                            startUnTest = false;
                            cathConnectionException(e);
                        }
                    }
                });
                UnomThread.start();
            }
        }
    }

    /**
     * Реализация работы автоматического режима работы установки.
     * Режим, при котором все команды из методики поверки выполняются друг за другом
     * @throws StendConnectionException
     * @throws InterruptedException
     */
    private void startAutomaticTest() throws StendConnectionException, InterruptedException {

        //Получаю индекс команды, которую выбрал пользователь
        int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();
        //Получаю команду
        command = tabViewTestPoints.getSelectionModel().getSelectedItem();

        //Если пользователь выбрал не выполнять команду (check Box = false)
        if (!command.isActive()) {

            //Перехожу к следующей команде
            for (int index = i + 1; index < tabViewTestPoints.getItems().size(); index++) {

                //Проверяю её на необходмость выполнения
                if (tabViewTestPoints.getItems().get(index).isActive()) {

                    //Выделяю данную команду в GUI
                    tabViewTestPoints.getSelectionModel().select(index);

                    //Выделяю результаты этой команды в таблице результатов
                    for (TableView<Meter.CommandResult > errorsView : tabViewErrorsList) {
                        errorsView.getSelectionModel().select(index);
                    }
                    break;
                }

                //Если конец исполняемых команд, жму на кнопку стоп
                if (index == tabViewTestPoints.getItems().size() - 1 && !tabViewTestPoints.getItems().get(index).isActive()) {
                    btnStop.fire();
                }
            }

            //Если выбранная команда предназначена для исполнения (check box = true)
        } else {
            //Проверяю к какому типу команд она относится

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

            }else if (command instanceof RelayCommand) {
                initAllParamForStartCommand((RelayCommand) command, i);
                command.execute();
            }

            //Проверяю не была ли команда последней
            if (i == tabViewTestPoints.getItems().size() - 1) {
                btnStop.fire();

                //Если не последняя
            } else {

                //Иду дальше по командам
                for (int index = i + 1; index < tabViewTestPoints.getItems().size(); index++) {
                    //Если команда активная
                    if (tabViewTestPoints.getItems().get(index).isActive()) {

                        //Выделяю данную команду в GUI
                        tabViewTestPoints.getSelectionModel().select(index);

                        //Выделяю результаты этой команды в таблице результатов
                        for (TableView<Meter.CommandResult > errorsView : tabViewErrorsList) {
                            errorsView.getSelectionModel().select(index);
                        }
                        break;
                    }

                    //Если конец исполняемых команд, жму на кнопку стоп
                    if (index == tabViewTestPoints.getItems().size() - 1 && !tabViewTestPoints.getItems().get(index).isActive()) {
                        btnStop.fire();
                    }
                }
            }
        }
    }

    /**
     * Реализация работы ручного режима работы установки.
     * Режим, при котором одна команда выполняется постоянно пока пользователь не нажмёт кнопку стоп
     * @throws StendConnectionException
     * @throws InterruptedException
     */
    private void startManualTest() throws StendConnectionException, InterruptedException {

        int i = tabViewTestPoints.getSelectionModel().getSelectedIndex();
        //Получаю команду выполненую пользователем
        command = tabViewTestPoints.getSelectionModel().getSelectedItem();

        //Инициализирую командами параметрами и выполняю
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

        } else if (command instanceof RelayCommand) {
            initAllParamForStartCommand((RelayCommand) command, i);
            command.executeForContinuousTest();
        }
    }

    /**
     * Реализация работы подачи напряжения на установкой на счётчики.
     * @throws StendConnectionException
     * @throws InterruptedException
     */
    private void startUn () throws StendConnectionException, InterruptedException {

        //Если установка трехфазная
        if (stendDLLCommands instanceof ThreePhaseStend) {

            //И при этом на стенд навешен трехфазный счётчик
            if (methodicForStend instanceof MethodicForThreePhaseStend) {
                stendDLLCommands.getUI(0, Un, 0.0, Fn, 0, 0, 100.0, 0, "H", "1.0");

                //Если навешен однофазный счётчик
            } else {
                double voltPerA = 0.0;
                double voltPerB = 0.0;
                double voltPerC = 0.0;

                //Подаю напряжение лишь на ту фазу, которую выбрал пользователь
                //для однофазного режима в трехфазной установке
                switch (TestErrorTableFrameController.phaseOnePhaseMode) {
                    case "A": voltPerA = 100.0; break;
                    case "B": voltPerB = 100.0; break;
                    case "C": voltPerC = 100.0; break;
                }

                stendDLLCommands.getUIWithPhase(1, Un, 0, Fn, 0, 0, voltPerA, voltPerB, voltPerC, 0, "H", "1.0");
            }

            //Если стенд однофазный
        } else {
            stendDLLCommands.getUI(1, Un, 0, Fn, 0, 0, 100.0, 0, "H", "1.0");
        }
    }

    //Инициализирует параметры необходимые для снятия погрешности в точке

    /**
     * Передаёт параметры тока, напряжения и д.р. выбранные пользователем, которые необходимы для исполнения команды
     * "Погрешность счётчика"
     * @param errorCommand
     * @param index
     */
    private void initAllParamForErrorCommand(ErrorCommand errorCommand, int index) {
        errorCommand.setStendDLLCommands(stendDLLCommands);
        errorCommand.setRatedVolt(Un);
        errorCommand.setIb(Ib);
        errorCommand.setImax(Imax);
        errorCommand.setRatedFreq(Fn);
        errorCommand.setIndex(index);
        errorCommand.setMeterForTestList(listMetersForTest);
    }

    /**
     * Передаёт параметры тока, напряжения и д.р. выбранные пользователем, которые необходимы для исполнения команды
     * "Самоход"
     * @param creepCommand
     * @param index
     */
    private void initAllParamForCreepCommand(CreepCommand creepCommand, int index){
        creepCommand.setStendDLLCommands(stendDLLCommands);
        creepCommand.setRatedVolt(Un);
        creepCommand.setRatedFreq(Fn);
        creepCommand.setIndex(index);
        creepCommand.setMeterList(listMetersForTest);

        //Если время испытания выбрано по госту, то автоматически рассчитываю время теста
        if (creepCommand.isGostTest()) {
            if (creepCommand.getChannelFlag() < 2) {
                creepCommand.setUserTimeTest(timeToCreepTestGOSTAP);
            } else {
                creepCommand.setUserTimeTest(timeToCreepTestGOSTRP);
            }
        }
    }

    /**
     * Передаёт параметры тока, напряжения и д.р. выбранные пользователем, которые необходимы для исполнения команды
     * "Чувствительность"
     * @param startCommand
     * @param index
     */
    private void initAllParamForStartCommand(StartCommand startCommand, int index) {
        startCommand.setStendDLLCommands(stendDLLCommands);
        startCommand.setRatedFreq(Fn);
        startCommand.setRatedVolt(Un);
        startCommand.setIndex(index);
        startCommand.setMeterList(listMetersForTest);

        //Если время испытания выбрано по госту, то автоматически рассчитываю время теста
        if (startCommand.isGostTest()) {
            if (startCommand.getChannelFlag() < 2) {
                startCommand.setUserTimeTest(timeToStartTestGOSTAP);
            } else {
                startCommand.setUserTimeTest(timeToStartTestGOSTRP);
            }
        }
    }

    /**
     * Передаёт параметры выбранные пользователем, которые необходимы для исполнения команды
     * "Точность хода часов"
     * @param rTCCommand
     * @param index
     */
    private void initAllParamForRTCCommand(RTCCommand rTCCommand, int index) {
        rTCCommand.setStendDLLCommands(stendDLLCommands);
        rTCCommand.setRatedVolt(Un);
        rTCCommand.setIndex(index);
        rTCCommand.setMeterList(listMetersForTest);
    }

    /**
     * Передаёт параметры выбранные пользователем, которые необходимы для исполнения команды
     * "Проверка счётного механизма"
     * @param constantCommand
     * @param index
     */
    private void initAllParamForConstantCommand(ConstantCommand constantCommand, int index) {
        constantCommand.setStendDLLCommands(stendDLLCommands);
        constantCommand.setRatedVolt(Un);
        constantCommand.setRatedCurr(Ib);
        constantCommand.setIndex(index);
        constantCommand.setMeterForTestList(listMetersForTest);
    }

    /**
     * Передаёт параметры выбранные пользователем, которые необходимы для исполнения команды
     * "Имбаланс напряжений"
     * @param imbalansUCommand
     * @param index
     */
    private void initAllParamForImbCommand(ImbalansUCommand imbalansUCommand, int index){
        imbalansUCommand.setStendDLLCommands(stendDLLCommands);
        imbalansUCommand.setRatedVolt(Un);
        imbalansUCommand.setIb(Ib);
        imbalansUCommand.setImax(Imax);
        imbalansUCommand.setRatedFreq(Fn);
        imbalansUCommand.setIndex(index);
        imbalansUCommand.setMeterForTestList(listMetersForTest);
    }

    /**
     * Передаёт параметры выбранные пользователем, которые необходимы для исполнения команды
     * "Проверка работоспособности реле"
     * @param relayCommand
     * @param index
     */
    private void initAllParamForStartCommand(RelayCommand relayCommand, int index) {
        relayCommand.setStendDLLCommands(stendDLLCommands);
        relayCommand.setRatedFreq(Fn);
        relayCommand.setRatedVolt(Un);
        relayCommand.setIndex(index);
        relayCommand.setMeterList(listMetersForTest);
    }

    /**
     * Включает или выключает выполнение всех команд
     * @param event
     */
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

    /**
     * Действие при смене пользователем направления тока и энергии.
     * Инициализация таблиц с командами для испытаний в данных режимах
     * @param event
     */
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

    /**
     * Установка в таблицу команд испытаний для необходимого направления тока и типа энергии
     */
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

    /**
     * Устанавливает селект в таблице на первой команде
     */
    private void setlectFirsCommand() {
        if (tabViewTestPoints.getItems().size() != 0) {
            tabViewTestPoints.getSelectionModel().select(0);

            for (TableView<Meter.CommandResult> tableViewError : tabViewErrorsList) {
                tableViewError.getSelectionModel().select(0);
            }
        }
    }

    /**
     * Основной метод инициализации таблиц в окне testErrorTableFrame.fxml
     */
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
        //Копирую все команды из методики поверки и выставляю параметры согдасно выбору пользователя

        /**
         * См. интерфейс StendDLL команду Adjust_UI там описано что означает "H, A,B,C", а так же режимы работы установки
         * (Phase)
         */
        char[] idChar;
        Commands cloneCommand;
        try {
            //Если стенд трёхфазный
            if (stendDLLCommands instanceof ThreePhaseStend) {
                String typeCircut = ConsoleHelper.properties.getProperty("threePhaseStand.lastTypeCircuit");

                //При этом методика выбрана для однофазного стедна (навешан однофазный(ые) счётчик(и))
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

                    //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                    //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                    //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                    //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(7);
                        commandsRPMns.add(cloneCommand);
                    }

                    //Если методика выбрана для трехфазного стенда (чем он и является)
                } else {
                    //Если выбран трехфазный четырёхпроводный режим
                    if (typeCircut.equals("3P4W")) {

                        //AP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPPls.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumAPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPPls.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPPls.add(cloneCommand);
                        }

                        //AP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPMns.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumAPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPMns.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(1);
                            commandsAPMns.add(cloneCommand);
                        }

                        //RP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPPls.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPPls.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPPls.add(cloneCommand);
                        }

                        //RP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(5);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Если выбран трехфазный трехпроводный режим
                    } else if (typeCircut.equals("3P3W")) {

                        //AP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPPls.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumAPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPPls.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(0)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPPls.add(cloneCommand);
                        }

                        //AP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPMns.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumAPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPMns.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(1)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(2);
                            commandsAPMns.add(cloneCommand);
                        }

                        //RP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPPls.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPPls.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPPls.add(cloneCommand);
                        }

                        //RP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(6);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Если выбран трехфазный трехпроводный режим sin 90
                    } else if (typeCircut.equals("3P3W 90 R.P.")) {

                        //RP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPPls.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPPls.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPPls.add(cloneCommand);
                        }

                        //RP-
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(3);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Если выбран трехфазный трехпроводный режим sin 60
                    } else if (typeCircut.equals("3P3W 60 R.P.")) {

                        //RP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPPls.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPPls()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPPls.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(2)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPPls.add(cloneCommand);
                        }

                        //RP+
                        //Добавляю точки ErrorCommand
                        for (Commands command : methodicForStend.getCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Добавляю точки ErrorCommand с окна влияния
                        for (Commands command : methodicForStend.getSaveInflListForCollumRPMns()) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(4);
                            commandsRPMns.add(cloneCommand);
                        }
                    }
                }

                //Если стенд является однофазным
            } else {

                //Но при этом выбрана методика для трехфазного стенда
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

                    //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                    //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                    //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                    //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                    for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                        cloneCommand = command.clone();
                        cloneCommand.setPhase(7);
                        commandsRPMns.add(cloneCommand);
                    }

                    //Если методика выбрана для однофазного стенда (чем он и является)
                } else {

                    //Если данный стенд имеет две токовые цепи
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

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPMns.add(cloneCommand);
                        }

                        //Если данный стенд имеет одну токовую цепи
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

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
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

                        //Добавляю точки испытания "Самоход, чувствительность, Точность хода часов, проверка счётного механизма"
                        for (Commands command : methodicForStend.getCreepStartRTCConstCommandsMap().get(3)) {
                            cloneCommand = command.clone();
                            cloneCommand.setPhase(7);
                            commandsRPMns.add(cloneCommand);
                        }
                    }
                }
            }
        }catch (CloneNotSupportedException e) {
            ConsoleHelper.infoException(e.getMessage());
        }

        //Создаю обекты результата испытания в каждой точке (Command)
        initErrorsForMeters();

        serializeNewNotSavedResults = new Thread(()-> {

            while (!Thread.currentThread().isInterrupted()) {
                try {

                    if (newResults) {
                        System.out.println("Сработало сохранение");
                        NotSavedResults.serializationNotSaveResults(listMetersForTest, dirWithNotSaveResults);

                        newResults = false;
                    }

                    Thread.sleep(5000);
                }catch (InterruptedException e) {
                    break;
                }
            }
        });

        serializeNewNotSavedResults.start();

        //В зависимости от количества счётчиков инициализирую таблицы для отображения погрешности
        //Если счётчиков меньше 12 размещаю таблицы на одном листе
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

            //Действие при изменении размеров окна
            paneErrors.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    for (TableView<Meter.CommandResult> tableViewError : tabViewErrorsList) {
                        tableViewError.setPrefHeight((Double) newValue);
                    }
                }
            });

            //Если счётчиков меньше 24 делю окно и отображаю колонки один под одним
        } else if (listMetersForTest.size() <= 24) {

            //Если счётчиков чётное количество, то отобращаю пополам
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

                //Действие при изменении размеров окна
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

                //Если счётчиков нечётное количество, то отобращаю в верхней части больше счётчиков на 1
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

                //Действие при изменении размеров окна
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

                        widthColumn = ((Double) newValue) / (meters / 2 + 1);

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
        } else if (listMetersForTest.size() <= 36) {

            //Создаю колонки
            for (int i = 0; i < listMetersForTest.size(); i++) {
                addTableViewAndCollumnInErrorPane(i);
            }

            if (listMetersForTest.size() % 3 == 0) {

                //Устанавливаю размер колонок под окно погрешности
                paneErrors.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        double widthColumn = ((Double) newValue) / (tabViewErrorsList.size() / 3);

                        for (int i = 0; i < listMetersForTest.size(); i++) {
                            tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                            tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);

                            //Первый ряд
                            if (i < listMetersForTest.size() / 3) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * i);

                                //Середина
                            } else if (i >= listMetersForTest.size() / 3 && i < listMetersForTest.size() - listMetersForTest.size() / 3) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - listMetersForTest.size() / 3));

                                //Самый низ
                            } else if (i >= listMetersForTest.size() - listMetersForTest.size() / 3) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - (listMetersForTest.size() - listMetersForTest.size() / 3)));

                            }
                        }
                    }
                });

                //Действие при изменении размеров окна
                paneErrors.heightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                        double height = ((Double) newValue) / 3;

                        for (int i = 0; i < tabViewErrorsList.size(); i++) {
                            tabViewErrorsList.get(i).setPrefHeight(height);

                            //Середина
                            if (i >= listMetersForTest.size() / 3 && i < listMetersForTest.size() - listMetersForTest.size() / 3) {

                                tabViewErrorsList.get(i).setLayoutY(height);

                                //Самый низ
                            } else if (i >= listMetersForTest.size() - listMetersForTest.size() / 3) {

                                tabViewErrorsList.get(i).setLayoutY(height * 2);
                            }
                        }
                    }
                });

                //Если не кратно трём
            } else {

                //Устанавливаю размер колонок под окно погрешности
                paneErrors.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                        double widthColumn = ((Double) newValue) / (tabViewErrorsList.size() / 3 + 1);

                        int firsLine = listMetersForTest.size() / 3 + 1; // 12
                        int secondLineEnd;

                        if (listMetersForTest.size() % 2 == 0) {
                            secondLineEnd = listMetersForTest.size() - (listMetersForTest.size() / 3); //23
                        } else {
                            secondLineEnd = listMetersForTest.size() - ((listMetersForTest.size() / 3) + 1); //23
                        }

                        for (int i = 0; i < listMetersForTest.size(); i++) {
                            tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                            tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);

                            //Первый ряд
                            if (i < firsLine) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * i);

                                //Середина
                            } else if (i >= firsLine && i <= secondLineEnd) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - firsLine));

                                //Самый низ
                            } else if (i >= secondLineEnd) {

                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - (secondLineEnd + 1)));
                            }
                        }
                    }
                });

                //Действие при изменении размеров окна
                paneErrors.heightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                        double height = ((Double) newValue) / 3;

                        int firstLine = listMetersForTest.size() / 3 + 1;
                        int secondLineEnd;

                        if (listMetersForTest.size() % 2 == 0) {
                            secondLineEnd = listMetersForTest.size() - (listMetersForTest.size() / 3); //23
                        } else {
                            secondLineEnd = listMetersForTest.size() - ((listMetersForTest.size() / 3) + 1); //23
                        }

                        for (int i = 0; i < tabViewErrorsList.size(); i++) {

                            tabViewErrorsList.get(i).setPrefHeight(height);

                            //Середина
                            if (i >= firstLine && i <= secondLineEnd) {

                                tabViewErrorsList.get(i).setLayoutY(height);

                                //Самый низ
                            } else if (i >= secondLineEnd) {

                                tabViewErrorsList.get(i).setLayoutY(height * 2);
                            }
                        }
                    }
                });
            }

        } else if (listMetersForTest.size() <= 48) {

            //Создаю колонки
            for (int i = 0; i < listMetersForTest.size(); i++) {
                addTableViewAndCollumnInErrorPane(i);
            }

            if (listMetersForTest.size() % 4 == 0) {

                int secondLine = listMetersForTest.size() / 4; //12
                int thirdLine = listMetersForTest.size() / 2; //24
                int fourthLine = listMetersForTest.size() - listMetersForTest.size() / 4; //36

                //Устанавливаю размер колонок под окно погрешности
                paneErrors.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        double widthColumn = ((Double) newValue) / (tabViewErrorsList.size() / 4);

                        for (int i = 0; i < listMetersForTest.size(); i++) {
                            tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                            tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);

                            //Первый ряд
                            if (i < secondLine) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * i);

                                //Второй ряд
                            } else if (i < thirdLine) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - secondLine));

                                //Третий ряд
                            } else if (i < fourthLine) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - thirdLine));

                            }else {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - fourthLine));
                            }
                        }
                    }
                });

                //Действие при изменении размеров окна
                paneErrors.heightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                        double height = ((Double) newValue) / 4;

                        for (int i = 0; i < tabViewErrorsList.size(); i++) {
                            tabViewErrorsList.get(i).setPrefHeight(height);

                            //Первый ряд
                            if (i < secondLine) {
                                tabViewErrorsList.get(i).setLayoutY(0);

                                //Второй ряд
                            } else if (i < thirdLine) {
                                tabViewErrorsList.get(i).setLayoutY(height * 1);

                                //Третий ряд
                            } else if (i <fourthLine) {
                                tabViewErrorsList.get(i).setLayoutY(height * 2);

                                //Четвертый ряд
                            } else {
                                tabViewErrorsList.get(i).setLayoutY(height * 3);
                            }

                        }
                    }
                });

            } else {

                int secondLine = listMetersForTest.size() / 4 + 1;
                int thirdLine;

                if (listMetersForTest.size() % 2 == 0) {
                    thirdLine = listMetersForTest.size() / 2 + 1;
                } else {
                    thirdLine = secondLine * 2;
                }

                int fourthLine = thirdLine + secondLine;

                //Устанавливаю размер колонок под окно погрешности
                paneErrors.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                        double widthColumn = ((Double) newValue) / (secondLine);

                        for (int i = 0; i < listMetersForTest.size(); i++) {
                            tabViewErrorsList.get(i).setPrefWidth(widthColumn);
                            tabViewErrorsList.get(i).getColumns().get(0).setPrefWidth(widthColumn);

                            //Первый ряд
                            if (i < secondLine) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * i);

                                //Второй ряд
                            } else if (i < thirdLine) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - secondLine));

                                //Третий ряд
                            } else if (i < fourthLine) {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - thirdLine ));

                                //Четвёртый
                            } else {
                                tabViewErrorsList.get(i).setLayoutX(widthColumn * (i - fourthLine));
                            }
                        }
                    }
                });

                //Действие при изменении размеров окна
                paneErrors.heightProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                        double height = ((Double) newValue) / 4;

                        for (int i = 0; i < tabViewErrorsList.size(); i++) {

                            tabViewErrorsList.get(i).setPrefHeight(height);

                            //Первый ряд
                            if (i < secondLine) {
                                tabViewErrorsList.get(i).setLayoutY(0);

                                //Второй ряд
                            } else if (i < thirdLine) {
                                tabViewErrorsList.get(i).setLayoutY(height);

                                //Третий ряд
                            } else if (i < fourthLine) {
                                tabViewErrorsList.get(i).setLayoutY(height * 2);

                                //Четвёртый
                            } else {
                                tabViewErrorsList.get(i).setLayoutY(height * 3);
                            }
                        }
                    }
                });
            }

        } else {
            ConsoleHelper.infoException("В настройках указано больше посадочных мест\n чем может отоброзить программа");
            return;
        }


        //Если выбираю точку испытания, то должна выставляться фокусировка и на панели с погрешностью
        //Если все колонки с результатами находятся на одном листе
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

            //Если все колонки с результатами делят рабочее пространство
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
                            tableViewError.scrollTo(i - 3);
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

        //Отображение подсказок с параметрами выбранной команды
        tabViewTestPoints.setRowFactory(tv -> new TableRow<Commands>() {
            private Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(Commands item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {

                    if (item instanceof ErrorCommand) {
                        tooltip.setText("Emax: " + ((ErrorCommand) item).getEmax() +
                                "\nEmin: " + ((ErrorCommand) item).getEmin() +
                                "\nКоличество импульсов: " + ((ErrorCommand) item).getPulse() +
                                "\nКоличество измерений: " + ((ErrorCommand) item).getCountResult() +
                                "\nВремя стабилизации: " + ((ErrorCommand) item).getPauseForStabilization());

                    } else if (item instanceof CreepCommand) {
                        tooltip.setText("Время теста: " + ((CreepCommand) item).getUserTimeTestHHmmss() +
                                "\nКоличество импульсов: " + ((CreepCommand) item).getPulseValue() +
                                "\nПроцент от Un: " + item.getVoltPer());

                    } else if (item instanceof StartCommand) {
                        tooltip.setText("Время теста: " + ((StartCommand) item).getUserTimeTestHHmmss() +
                                "\nКоличество импульсов: " + ((StartCommand) item).getPulseValue() +
                                "\nТок: " + item.getRatedCurr());

                    } else if (item instanceof RTCCommand) {
                        if (((RTCCommand) item).getErrorType() == 0) {
                            tooltip.setText("Emax: " + String.format(Locale.ROOT, "%.7f", ((RTCCommand) item).getErrorForFalseTest()) +
                                    "\nEmin: " + String.format(Locale.ROOT, "%.7f", -((RTCCommand) item).getErrorForFalseTest()) +
                                    "\nЧастота: " + String.format(Locale.ROOT, "%.7f", ((RTCCommand) item).getFreg()) +
                                    "\nКоличество импульсов: " + ((RTCCommand) item).getPulseForRTC() +
                                    "\nКоличество измерений: " + ((RTCCommand) item).getCountResultTest());
                        } else {
                            tooltip.setText("Emax: " + ((RTCCommand) item).getErrorForFalseTest() +
                                    "\nEmin: " +  -((RTCCommand) item).getErrorForFalseTest() +
                                    "\nЧастота: " + String.format(Locale.ROOT, "%.7f", ((RTCCommand) item).getFreg()) +
                                    "\nКоличество импульсов: " + ((RTCCommand) item).getPulseForRTC() +
                                    "\nКоличество измерений: " + ((RTCCommand) item).getCountResultTest());
                        }

                    } else if (item instanceof ConstantCommand) {
                        if (((ConstantCommand) item).isRunTestToTime()) {
                            tooltip.setText("Emax: " + ((ConstantCommand) item).getEmaxProc() +
                                    "\nEmin: " + ((ConstantCommand) item).getEminProc() +
                                    "\nВремя теста: " + ((ConstantCommand) item).getTimeTheTestHHmmss());
                        } else {
                            tooltip.setText("Emax: " + ((ConstantCommand) item).getEmaxProc() +
                                    "\nEmin: " + ((ConstantCommand) item).getEminProc() +
                                    "\nКоличество энергии: " + ((ConstantCommand) item).getkWToTest());
                        }

                    }  else if (item instanceof ImbalansUCommand) {
                        tooltip.setText("Emax: " + ((ImbalansUCommand) item).getEmax() +
                                "\nEmin: " + ((ImbalansUCommand) item).getEmin() +
                                "\nКоличество импульсов: " + ((ImbalansUCommand) item).getPulse() +
                                "\nКоличество измерений: " + ((ImbalansUCommand) item).getCountResult());

                    } else if (item instanceof RelayCommand) {
                        tooltip.setText("Время теста: " + ((RelayCommand) item).getUserTimeTestHHmmss() +
                                "\nКоличество импульсов: " + ((RelayCommand) item).getPulseValue() +
                                "\nТок: " + item.getRatedCurr());
                    }

                    tooltip.setFont(Font.font(12));
                    tooltip.setStyle("-fx-background-radius: 0; -fx-background-color: gray; -fx-opacity: 0.9;");
                    setTooltip(tooltip);
                }
            }
        });

        //Если точки для активной энергии в прямом направлении есть, то отображаю их спытания есть,
        //если нет, то иду дальше пока не найду таблицу, где они есть
        if (commandsAPPls.size() != 0) {
            tglBtnAPPls.fire();
        } else if (commandsAPMns.size() != 0) {
            tglBtnAPMns.fire();
        } else if (commandsRPPls.size() != 0) {
            tglBtnRPPls.fire();
        } else if (commandsRPMns.size() != 0) {
            tglBtnRPMns.fire();

            //Если точек для испытания нет (пустая методика поверки)
        } else {
            tglBtnAPPls.fire();
        }

        //Ставлю слушатель на действие при закрытии окна пользователем
        Stage testErrorTableFrameControllerStage = (Stage) btnExit.getScene().getWindow();

        testErrorTableFrameControllerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                btnExit.fire();
            }
        });

        //Загрузка окна с параметрами этелонного счётчика
        //Если стенд трехфазный, то загружаю окно с параметрами этолонного счётчика для трехфазного стенда
        if (stendDLLCommands instanceof ThreePhaseStend) {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/refParamFrames/threePhaseStendrefParamFrame.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
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

            //Если стенд однофазный, то загружаю окно с параметрами этолонного счётчика для однофазного стенда
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/refParamFrames/onePhaseStendrefParamFrame.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
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

        //Запускаю считывание параметров с эталонного счётчика
        refMeterThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!Thread.currentThread().isInterrupted()) {

                    if (stendRefParametersForFrame != null) {
                        stendRefParametersForFrame.readParametersWithoutCheckingParan();
                    }

                    try {
                        Thread.sleep(3000);
                    }catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });

        refMeterThread.start();

        //Внутренние параметры стенда
        try {
            if (ConsoleHelper.properties.getProperty("cutNeitral").equals("T")) {
                stendDLLCommands.cutNeutral(0);
            } else {
                stendDLLCommands.cutNeutral(1);
            }

            stendDLLCommands.setReviseMode(Integer.parseInt(ConsoleHelper.properties.getProperty("reviseMode")));
        }catch (StendConnectionException ignored) {}

        try {
            if (ConsoleHelper.properties.getProperty("reviseOff").equals("F")) {
                stendDLLCommands.setNoRevise(false);
            } else {
                stendDLLCommands.setNoRevise(true);
            }

            stendDLLCommands.setReviseTime(Double.parseDouble(ConsoleHelper.properties.getProperty("reviseTime")));

        }catch (StendConnectionException ignored) {}
    }

    /**
     * Выполняет добавление результата теста в каждой точке для каждого счётчика
     */
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

    /**
     * Проверяет наличие несохранённых результатов в программе предлагает их восстановить
     */
    public void checkAndLoadNotSavedResults() {
        NotSavedResults notSavedResults = NotSavedResults.getNotSavedResultsInstance(dirWithNotSaveResults);

        //Если есть несохранённые результаты
        if (notSavedResults != null) {

            Boolean answer = ConsoleHelper.yesOrNoFrame("Восстановление результатов" ,"Найдены последние не сохранённые результаты теста,\n" +
                    "восстановить их?");

            if (answer != null) {

                if (answer) {

                    //Ссылки на конкретные результаты счётчиков, для удобного прохождения и замены
                    Meter.ErrorResult meterInTestErrorResult;
                    Meter.ErrorResult meterErrorResultNotSave;

                    //Прохожусь по не сохранённым результатам
                    for (Meter notSaveResultsMeter : notSavedResults.getMetersWhoseResultsAreNotSaved()) {

                        //Сравниваю id с посадочным местом установки
                        for (Meter meterInTest : listMetersForTest) {

                            //Если счётчик на посадочном месте есть
                            if (notSaveResultsMeter.getId() == meterInTest.getId()) {

                                //Мапы чтобы не дублировать код
                                Map<Integer, List<Meter.CommandResult>> mapNotSavedResultsMeter = new HashMap<>();
                                Map<Integer, List<Meter.CommandResult>> mapResultsMeterInTest = new HashMap<>();

                                mapNotSavedResultsMeter.put(0, notSaveResultsMeter.getErrorListAPPls());
                                mapNotSavedResultsMeter.put(1, notSaveResultsMeter.getErrorListAPMns());
                                mapNotSavedResultsMeter.put(2, notSaveResultsMeter.getErrorListRPPls());
                                mapNotSavedResultsMeter.put(3, notSaveResultsMeter.getErrorListRPMns());

                                mapResultsMeterInTest.put(0, meterInTest.getErrorListAPPls());
                                mapResultsMeterInTest.put(1, meterInTest.getErrorListAPMns());
                                mapResultsMeterInTest.put(2, meterInTest.getErrorListRPPls());
                                mapResultsMeterInTest.put(3, meterInTest.getErrorListRPMns());

                                for (int i = 0; i < 4; i++) {
                                    //Если точки испытаний по данной методике есть, переношу из несохранённых к текущему
                                    if (!mapResultsMeterInTest.get(i).isEmpty() && !mapNotSavedResultsMeter.get(i).isEmpty()) {

                                        for (Meter.CommandResult notSaveResult : mapNotSavedResultsMeter.get(i)) {

                                            for (Meter.CommandResult resultInTest : mapResultsMeterInTest.get(i)) {

                                                if (notSaveResult.getId().equals(resultInTest.getId())) {

                                                    if (notSaveResult instanceof Meter.ErrorResult) {

                                                        meterInTestErrorResult = (Meter.ErrorResult) resultInTest;
                                                        meterErrorResultNotSave = (Meter.ErrorResult) notSaveResult;

                                                        meterInTestErrorResult.setLastResult(meterErrorResultNotSave.getLastResult());
                                                        meterInTestErrorResult.setResults(meterErrorResultNotSave.getResults());
                                                        meterInTestErrorResult.refreshTipsInfo();

                                                        if (!meterInTestErrorResult.getLastResult().isEmpty()) {
                                                            double result = Double.parseDouble(meterInTestErrorResult.getLastResult());

                                                            if (Double.parseDouble(meterInTestErrorResult.getMaxError()) < result ||
                                                                    Double.parseDouble(meterInTestErrorResult.getMinError()) > result ) {
                                                                meterInTestErrorResult.setLastResultForTabView("F" + meterInTestErrorResult.getLastResult());
                                                                meterInTestErrorResult.setPassTest(false);
                                                            } else {
                                                                meterInTestErrorResult.setLastResultForTabView("P" + meterInTestErrorResult.getLastResult());
                                                                meterInTestErrorResult.setPassTest(true);
                                                            }
                                                        }

                                                    } else if (notSaveResult instanceof Meter.CreepResult) {

                                                        Meter.CreepResult meterInTestResult = (Meter.CreepResult) resultInTest;
                                                        Meter.CreepResult meterResultNotSave = (Meter.CreepResult) notSaveResult;

                                                        //Если есть результат
                                                        if (!meterResultNotSave.getLastResult().isEmpty()) {

                                                            //Если условия точки испытания такие же как и у не сохранённого результата
                                                            if (meterInTestResult.getTimeTheTest().equals(meterResultNotSave.getTimeTheTest()) &&
                                                                    meterInTestResult.getMaxPulse().equals(meterResultNotSave.getMaxPulse())) {

                                                                meterInTestResult.setLastResult(meterResultNotSave.getLastResult());
                                                                meterInTestResult.setResults(meterResultNotSave.getResults());
                                                                meterInTestResult.setPassTest(meterResultNotSave.isPassTest());
                                                                meterInTestResult.refreshTipsInfo();

                                                                if (meterInTestResult.isPassTest()) {
                                                                    meterInTestResult.setLastResultForTabView("P" + meterResultNotSave.getTimeTheTest() + " P");
                                                                } else {
                                                                    meterInTestResult.setTimeTheFailTest(meterResultNotSave.getTimeTheFailTest());
                                                                    meterInTestResult.setLastResultForTabView("F" + meterResultNotSave.getTimeTheFailTest() + " F");
                                                                }
                                                            }

                                                            //Устанавливаю результат счётчику
                                                            meterInTest.setCreepTest(meterInTestResult);
                                                        }

                                                    } else if (notSaveResult instanceof Meter.StartResult) {

                                                        Meter.StartResult meterInTestResult = (Meter.StartResult) resultInTest;
                                                        Meter.StartResult meterResultNotSave = (Meter.StartResult) notSaveResult;

                                                        //Если есть результат
                                                        if (!meterResultNotSave.getLastResult().isEmpty()) {

                                                            //Если условия точки испытания такие же как и у не сохранённого результата
                                                            if (meterInTestResult.getTimeTheTest().equals(meterResultNotSave.getTimeTheTest()) &&
                                                                    meterInTestResult.getMaxPulse().equals(meterResultNotSave.getMaxPulse())) {

                                                                meterInTestResult.setLastResult(meterResultNotSave.getLastResult());
                                                                meterInTestResult.setResults(meterResultNotSave.getResults());
                                                                meterInTestResult.setPassTest(meterResultNotSave.isPassTest());
                                                                meterInTestResult.refreshTipsInfo();

                                                                if (meterInTestResult.isPassTest()) {
                                                                    meterInTestResult.setTimeThePassTest(meterResultNotSave.getTimeThePassTest());
                                                                    meterInTestResult.setLastResultForTabView("P" + meterResultNotSave.getTimeThePassTest() + " P");
                                                                } else {
                                                                    meterInTestResult.setLastResultForTabView("F" + meterResultNotSave.getTimeTheTest() + " F");
                                                                }
                                                            }

                                                            //Установка результата счётчику
                                                            switch (i) {
                                                                case 0: {
                                                                    meterInTest.setStartTestAPPls(meterInTestResult);
                                                                }break;
                                                                case 1: {
                                                                    meterInTest.setStartTestAPMns(meterInTestResult);
                                                                }break;
                                                                case 2: {
                                                                    meterInTest.setStartTestRPPls(meterInTestResult);
                                                                }break;
                                                                case 3: {
                                                                    meterInTest.setStartTestRPMns(meterInTestResult);
                                                                }
                                                            }
                                                        }

                                                    } else if (notSaveResult instanceof  Meter.RTCResult) {

                                                        Meter.RTCResult meterInTestResult = (Meter.RTCResult) resultInTest;
                                                        Meter.RTCResult meterResultNotSave = (Meter.RTCResult) notSaveResult;

                                                        if (!meterResultNotSave.getLastResult().isEmpty()) {

                                                            //Если условия точки испытания такие же как и у не сохранённого результата
                                                            if (meterInTestResult.getTimeMeash().equals(meterResultNotSave.getTimeMeash()) &&
                                                                    meterInTestResult.getAmoutMeash().equals(meterResultNotSave.getAmoutMeash()) &&
                                                                    meterInTestResult.getFreg().equals(meterResultNotSave.getFreg())) {

                                                                meterInTestResult.setLastResult(meterResultNotSave.getLastResult());
                                                                meterInTestResult.setPassTest(meterResultNotSave.getPassTest());
                                                                meterInTestResult.setResults(meterResultNotSave.getResults());
                                                                meterInTestResult.refreshTipsInfo();

                                                                if (meterInTestResult.isPassTest()) {
                                                                    meterInTestResult.setLastResultForTabView("P" + meterInTestResult.getLastResult() + " P");
                                                                } else {
                                                                    meterInTestResult.setLastResultForTabView("F" + meterInTestResult.getLastResult() + " F");
                                                                }
                                                            }

                                                            //Устанавливаю результат счётчику
                                                            meterInTest.setRTCTest(meterInTestResult);
                                                        }

                                                    }  else if (notSaveResult instanceof Meter.ConstantResult) {

                                                        Meter.ConstantResult meterInTestResult = (Meter.ConstantResult) resultInTest;
                                                        Meter.ConstantResult meterResultNotSave = (Meter.ConstantResult) notSaveResult;

                                                        if (!meterResultNotSave.getLastResult().isEmpty()) {

                                                            meterInTestResult.setLastResult(meterResultNotSave.getLastResult());
                                                            meterInTestResult.setPassTest(meterResultNotSave.getPassTest());
                                                            meterInTestResult.setResults(meterResultNotSave.getResults());
                                                            meterInTestResult.refreshTipsInfo();

                                                            if (meterInTestResult.isPassTest()) {
                                                                meterInTestResult.setLastResultForTabView("P" + meterInTestResult.getLastResult() + " P");
                                                            } else {
                                                                meterInTestResult.setLastResultForTabView("F" + meterInTestResult.getLastResult() + " F");
                                                            }

                                                            //Установка результата счётчику
                                                            switch (i) {
                                                                case 0: {
                                                                    meterInTest.setConstantTestAPPls(meterInTestResult);
                                                                }break;
                                                                case 1: {
                                                                    meterInTest.setConstantTestAPMns(meterInTestResult);
                                                                }break;
                                                                case 2: {
                                                                    meterInTest.setConstantTestRPPls(meterInTestResult);
                                                                }break;
                                                                case 3: {
                                                                    meterInTest.setConstantTestRPMns(meterInTestResult);
                                                                }
                                                            }
                                                        }

                                                    } else if (notSaveResult instanceof  Meter.ImbUResult) {

                                                        Meter.ImbUResult meterInTestResult = (Meter.ImbUResult) resultInTest;
                                                        Meter.ImbUResult meterResultNotSave = (Meter.ImbUResult) notSaveResult;

                                                        if (!meterInTestResult.getLastResult().isEmpty()) {

                                                            meterInTestResult.setLastResult(meterResultNotSave.getLastResult());
                                                            meterInTestResult.setResults(meterResultNotSave.getResults());
                                                            meterInTestResult.refreshTipsInfo();

                                                            double result = Double.parseDouble(meterInTestResult.getLastResult());

                                                            if (Double.parseDouble(meterInTestResult.getMaxError()) < result ||
                                                                    Double.parseDouble(meterInTestResult.getMinError()) > result ) {

                                                                meterInTestResult.setLastResultForTabView("F" + meterInTestResult.getLastResult());
                                                                meterInTestResult.setPassTest(false);
                                                            } else {
                                                                meterInTestResult.setLastResultForTabView("P" + meterInTestResult.getLastResult());
                                                                meterInTestResult.setPassTest(true);
                                                            }
                                                        }

                                                    } else if (notSaveResult instanceof  Meter.RelayResult) {

                                                        Meter.RelayResult meterInTestResult = (Meter.RelayResult) resultInTest;
                                                        Meter.RelayResult meterResultNotSave = (Meter.RelayResult) notSaveResult;

                                                        if (!meterResultNotSave.getLastResult().isEmpty()) {

                                                            if (meterInTestResult.getTimeTheTest().equals(meterResultNotSave.getTimeTheTest()) &&
                                                                    meterInTestResult.getMaxPulse().equals(meterResultNotSave.getMaxPulse())) {
                                                                meterInTestResult.setPassTest(meterResultNotSave.getPassTest());
                                                                meterInTestResult.setLastResult(meterResultNotSave.getLastResult());
                                                                meterInTestResult.setResults(meterResultNotSave.getResults());
                                                                meterInTestResult.refreshTipsInfo();

                                                                if (meterInTestResult.getPassTest()) {
                                                                    meterInTestResult.setLastResultForTabView("P" + meterResultNotSave.getTimeTheTest() + " P");
                                                                } else {
                                                                    meterInTestResult.setLastResultForTabView("F" + meterResultNotSave.getTimeTheFailTest() + " F");
                                                                }
                                                            }

                                                            //Устанавливаю результат счётчику
                                                            meterInTest.setRelayTest(meterInTestResult);
                                                        }
                                                    } else {

                                                        if (notSaveResult instanceof Meter.AppearensResult) {
                                                            meterInTest.setAppearensTest((Meter.AppearensResult) notSaveResult);
                                                        } else if (notSaveResult instanceof Meter.InsulationResult) {
                                                            meterInTest.setInsulationTest((Meter.InsulationResult) notSaveResult);
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    saveResults = true;
                }
            }
        }
    }

    //В зафисимости от того вылезает ли результат счётчика за диапазон заданной погрешности раскрашиваю значение
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

                                    //Нейтральное значение
                                    if (firstSymbol == 'N') {
                                        setText(item.substring(1));
                                        setTextFill(Color.BLACK);

                                        //погрешность влезла в диапазон
                                    } else if (firstSymbol == 'P') {
                                        setText(item.substring(1));
                                        setTextFill(Color.rgb(0, 105, 201));

                                        //погрешность вылезла за диапазон
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

        //Задаю путь к стилю таблицы
        String styleErrorTableView = getClass().getClassLoader().getResource("styleCSS/testErrorTableFrame/tableViewErrors.css").toString();

        if (styleErrorTableView != null) {
            tableView.getStylesheets().add(styleErrorTableView);
        }

        //Задаю номер посадочного места
        TableColumn<Meter.CommandResult, String> column = new TableColumn<>("Место " + listMetersForTest.get(index).getId());
        column.setStyle("-fx-alignment: CENTER;");
        column.setCellValueFactory(new PropertyValueFactory<>("lastResultForTabView"));
        column.setSortable(false);
        column.setCellFactory(cellFactoryEndTest);
        tableView.getColumns().add(column);
        tableView.setPlaceholder(new Label("Нет точек"));
        paneErrors.getChildren().add(tableView);
        tabViewErrorsList.add(tableView);

        //Добавляю всплывающие окна с результатами прошлых измерений
        tableView.setRowFactory(tv -> new TableRow<Meter.CommandResult>() {
            private Tooltip tooltip = new Tooltip();

            @Override
            protected void updateItem(Meter.CommandResult item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {

                    SimpleStringProperty simpleStringProperty = item.errorsForTipsProperty();

                    if (item.errorsForTipsProperty() == null) {
                        item.setSimpPropErrorsForTips();

                        item.refreshTipsInfo();
                        simpleStringProperty = item.errorsForTipsProperty();
                    }

                    tooltip.textProperty().bind(Bindings.convert(simpleStringProperty));

                    tooltip.setFont(Font.font(12));
                    tooltip.setStyle("-fx-background-radius: 0; -fx-background-color: gray; -fx-opacity: 0.9;");
                    setTooltip(tooltip);
                }
            }
        });
    }

    //Находит все скрол бары и устанавливает значения других как у основного (двигаю основной, двигаются другие)
    public void initScrolBars() {
        ScrollBar verticalBarCommands;
        ScrollBar verticalBarErrorsFirst;
        ScrollBar verticalBarErrorsSecond;

        //Получаю скрол бары определённого окна
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

    //Получаю время тестов по ГОСТУ
    private void intiTimeCRPSTATests() {

        timeToCreepTestGOSTAP = initTimeForCreepTestGOST(constantMeterAP);

        timeToCreepTestGOSTRP = initTimeForCreepTestGOST(constantMeterRP);

        timeToStartTestGOSTAP = initTimeForStartGOSTTest(accuracyClassAP, constantMeterAP);

        timeToStartTestGOSTRP = initTimeForStartGOSTTest(accuracyClassRP, constantMeterRP);
    }

    //Блокирует кнопки управления от пользователя на заданное время
    private void blockControlBtns(int mls) {

        blockButtonsThread = new Thread(new Task() {
            @Override
            protected Object call() {

                Platform.runLater(() -> {
                    tabViewCommandsPane.setCursor(Cursor.WAIT);
                    tabViewTestPoints.setMouseTransparent(true);
                    buttonPane.setCursor(Cursor.WAIT);
                    buttonPane.setDisable(true);

                });

                try {
                    Thread.sleep(mls);
                } catch (InterruptedException ignore) {}

                Platform.runLater(() -> {
                    tabViewCommandsPane.setCursor(Cursor.DEFAULT);
                    tabViewTestPoints.setMouseTransparent(false);
                    buttonPane.setCursor(Cursor.DEFAULT);
                    buttonPane.setDisable(false);
                });

                return null;
            }
        });

        blockButtonsThread.start();
    }

    //Действие при возникновении дисконекта от устаноки
    private void cathConnectionException(Throwable e) {

        blockButtonsThread.interrupt();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                blockBtns.setValue(false);
                tglBtnAuto.setSelected(false);
                tglBtnManualMode.setSelected(false);
                tglBtnUnom.setSelected(false);

                blockTypeEnergyAndDirectionBtns.setValue(false);
            }
        });

        ConsoleHelper.infoException("Потеряна связь с установкой.\nПроверьте выставлен ли режим \"Онлайн\" и подключение интерфейса связи.\n" + e.getMessage());

        selectedCommand.removeListener(automaticListChangeListener);
        selectedCommand.removeListener(manualListChangeListener);
    }

    /**
     * TEST!
     */
    public void createRandomResults() {

        new Thread(() -> {

                for (Meter meter : listMetersForTest) {

                    Map<Integer, List<Meter.CommandResult>> map = new HashMap<>();
                    map.put(0, meter.getErrorListAPPls());
                    map.put(1, meter.getErrorListAPMns());
                    map.put(2, meter.getErrorListRPPls());
                    map.put(3, meter.getErrorListRPMns());

                    for (Map.Entry<Integer, List<Meter.CommandResult>> errorList : map.entrySet()) {

                        if (!errorList.getValue().isEmpty()) {

                            for (Meter.CommandResult result : errorList.getValue()) {

                                if (result instanceof Meter.ErrorResult) {

                                    double randRes = -5 + (Math.random() * 5);
                                    double randMin = -7 + (Math.random() * 7);
                                    double randMax = -7 + (Math.random() * 7);

                                    result.setMinError(String.format(Locale.ROOT, "%.3f", randMin));
                                    result.setMaxError(String.format(Locale.ROOT, "%.3f", randMax));
                                    result.setLastResult(String.format(Locale.ROOT, "%.3f", randRes));

                                    for (int j = 0; j < result.getResults().length; j++) {
                                        double newRand = -5 + (Math.random() * 5);
                                        result.getResults()[j] = String.format(Locale.ROOT, "%.3f", newRand);
                                    }

                                    if (randMin > randRes || randMax < randRes) {
                                        result.setLastResultForTabView("F" + String.format(Locale.ROOT,"%.3f", randRes));
                                        result.setPassTest(false);
                                    } else {
                                        result.setLastResultForTabView("P" + String.format(Locale.ROOT,"%.3f", randRes));
                                        result.setPassTest(true);
                                    }
                                }
                            }
                        }
                    }
                }

                newResults = true;

        }).start();
    }

    /** =======================================================================================
     * GET'S and SET'S
     *
     */
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

    public boolean isTypeOfMeasuringElementShunt() {
        return typeOfMeasuringElementShunt;
    }

    public double getIb() {
        return Ib;
    }

    public double getAccuracyClassAP() {
        return accuracyClassAP;
    }

    public double getAccuracyClassRP() {
        return accuracyClassRP;
    }

    public Stage getRefMeterStage() {
        return refMeterStage;
    }

    public Metodic getMethodicForStend() {
        return methodicForStend;
    }

    public Stage getTestErrorTableFrameStage() {
        return (Stage) btnExit.getScene().getWindow();
    }

    public Thread getRefMeterThread() {
        return refMeterThread;
    }

    public Thread getSerializeNewNotSavedResults() {
        return serializeNewNotSavedResults;
    }
}