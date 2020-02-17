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
import javafx.stage.Stage;
import javafx.util.Callback;
import stend.controller.Commands.Commands;
import stend.controller.Commands.ErrorCommand;
import stend.controller.Commands.StartCommand;
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
    private TableView<Meter.Error> tabViewErrosAPPls;

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
    private TableView<Meter.Error> tabViewErrosAPMns;

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
    private TableView<Meter.Error> tabViewErrosRPPls;

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
    private TableView<Meter.Error> tabViewErrosRPMns;

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

        //Логика работы автоматического режима работы
        if (event.getSource() == tglBtnAuto) {
            tglBtnAuto.setSelected(true);
            tglBtnManualMode.setSelected(false);
            tglBtnUnom.setSelected(false);

            //Если выбрана панель AP+
            if (tglBtnAPPls.isSelected()) {
                int i = tabViewTestPointsAPPls.getSelectionModel().getFocusedIndex();

                while (i < commandsAPPls.size()) {
                    command = commandsAPPls.get(i);

                    //Если тестовая точка активна
                    if (command.isActive()) {


                        if (command instanceof ErrorCommand) {
                            ((ErrorCommand) command).setStendDLLCommands(stendDLLCommands);
                            ((ErrorCommand) command).setRatedVolt(Un);
                            ((ErrorCommand) command).setIb(Ib);
                            ((ErrorCommand) command).setImax(Imax);
                            ((ErrorCommand) command).setRatedFreq(Fn);
                            ((ErrorCommand) command).setIndex(i);
                            ((ErrorCommand) command).setMeterForTestList(listMetersForTest);

                            command.execute();
                        }

                        if (command instanceof StartCommand) {

                        }
                    }

                    i++;
                    //Устанавливаю строчку на следующей команде
                    /**
                     * Возможно нужно будет добавить и для окна ошибок авто фокусировку
                     */

                    tabViewErrosAPPls.getFocusModel().focus(i);
                    tabViewTestPointsAPPls.getFocusModel().focus(i);
                }
            }

            //Если нажата кнопка остановки
            if (event.getSource() == btnStop) {
                command.setInterrupt(true);
            }

        }

        //Логика работы ручного режима работы
        if (event.getSource() == tglBtnManualMode) {
            tglBtnAuto.setSelected(false);
            tglBtnManualMode.setSelected(true);
            tglBtnUnom.setSelected(false);

        }

        //Логика работы подачи напряжения
        if (event.getSource() == tglBtnUnom) {
            tglBtnAuto.setSelected(false);
            tglBtnManualMode.setSelected(false);
            tglBtnUnom.setSelected(true);

        }

        //Логика работы остановки теста
        if (event.getSource() == btnStop) {
            tglBtnAuto.setSelected(false);
            tglBtnManualMode.setSelected(false);
            tglBtnUnom.setSelected(false);
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
            exceptionFrameController.getExceptionLabel().setText("Потеряна связь с установной,\n проверьте подключение");

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Ошибка");
            stage.setScene(new Scene(root));
            stage.show();

            tglBtnAuto.setSelected(false);
            tglBtnManualMode.setSelected(false);
            tglBtnUnom.setSelected(false);
        }
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
            TableColumn<Meter.Error, String> tableColumnAPPls = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnAPPls.setStyle( "-fx-alignment: CENTER;");
            tableColumnAPPls.setCellValueFactory(new PropertyValueFactory<>("lastError"));
            tableColumnAPPls.setSortable(false);
            ObservableList<Meter.Error> observableListAPPls = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPPls());
            tabViewErrosAPPls.setItems(observableListAPPls);
            tabViewErrosAPPls.getColumns().add(tableColumnAPPls);

            //Создаю колонки счётчиков для splitPane AP-
            TableColumn<Meter.Error, String> tableColumnAPMns = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnAPMns.setStyle( "-fx-alignment: CENTER;");
            tableColumnAPMns.setCellValueFactory(new PropertyValueFactory<>("lastError"));
            tableColumnAPMns.setSortable(false);
            ObservableList<Meter.Error> observableListAPMns = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPMns());
            tabViewErrosAPMns.setItems(observableListAPMns);
            tabViewErrosAPMns.getColumns().add(tableColumnAPMns);

            //Создаю колонки счётчиков для splitPane RP+
            TableColumn<Meter.Error, String> tableColumnRPPls = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnRPPls.setStyle( "-fx-alignment: CENTER;");
            tableColumnRPPls.setCellValueFactory(new PropertyValueFactory<>("lastError"));
            tableColumnRPPls.setSortable(false);
            ObservableList<Meter.Error> observableListRPPls = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPPls());
            tabViewErrosRPPls.setItems(observableListRPPls);
            tabViewErrosRPPls.getColumns().add(tableColumnRPPls);

            //Создаю колонки счётчиков для splitPane RP-
            TableColumn<Meter.Error, String> tableColumnRPMns = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnRPMns.setStyle( "-fx-alignment: CENTER;");
            tableColumnRPMns.setCellValueFactory(new PropertyValueFactory<>("lastError"));
            tableColumnRPMns.setSortable(false);
            ObservableList<Meter.Error> observableListRPMns = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPMns());
            tabViewErrosRPMns.setItems(observableListRPMns);
            tabViewErrosRPMns.getColumns().add(tableColumnRPMns);
        }

        //--------------------------------------------------------------------
        //Устанавливаю фокусировку на окне ошибок такуюже как и в окне точек
        //AP+
        tabViewTestPointsAPPls.getSelectionModel().select(0);
        tabViewErrosAPPls.getSelectionModel().select(0);

        ObservableList<TablePosition> tablePositionsAPPls = tabViewTestPointsAPPls.getSelectionModel().getSelectedCells();

        tablePositionsAPPls.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsAPPls.getSelectionModel().getFocusedIndex();

                tabViewErrosAPPls.getSelectionModel().select(i);
                tabViewErrosAPPls.getFocusModel().focus(i);
            }
        });

        //AP-
        tabViewTestPointsAPMns.getSelectionModel().select(0);
        tabViewErrosAPMns.getSelectionModel().select(0);

        ObservableList<TablePosition> tablePositionsAPMns = tabViewTestPointsAPMns.getSelectionModel().getSelectedCells();

        tablePositionsAPMns.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsAPMns.getSelectionModel().getFocusedIndex();

                tabViewErrosAPMns.getSelectionModel().select(i);
                tabViewErrosAPMns.getFocusModel().focus(i);
            }
        });

        //RP+
        tabViewTestPointsRPPls.getSelectionModel().select(0);
        tabViewErrosRPPls.getSelectionModel().select(0);

        ObservableList<TablePosition> tablePositionsRPPls = tabViewTestPointsRPPls.getSelectionModel().getSelectedCells();

        tablePositionsRPPls.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsRPPls.getSelectionModel().getFocusedIndex();

                tabViewErrosRPPls.getSelectionModel().select(i);
                tabViewErrosRPPls.getFocusModel().focus(i);
            }
        });

        //AP-
        tabViewTestPointsRPMns.getSelectionModel().select(0);
        tabViewErrosRPMns.getSelectionModel().select(0);

        ObservableList<TablePosition> tablePositionsRPMns = tabViewTestPointsRPMns.getSelectionModel().getSelectedCells();

        tablePositionsRPMns.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change<? extends TablePosition> c) {
                int i = tabViewTestPointsRPMns.getSelectionModel().getFocusedIndex();

                tabViewErrosRPMns.getSelectionModel().select(i);
                tabViewErrosRPMns.getFocusModel().focus(i);
            }
        });
    }

    //Добавляет объект error к каждому счётчику необходимому для теста
    private void initErrorsForMeters() {
        for (Meter meter : listMetersForTest) {
            for (int i = 0; i < 4; i++) {
                for (Commands commandName : methodic.getCommandsMap().get(i)) {
                    meter.createError(i, commandName.getName());
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
        verticalBarErrors = (ScrollBar) tabViewErrosAPPls.lookup(".scroll-bar:vertical");

        bindScrolls(verticalBarCommands, verticalBarErrors);

        //AP-
        verticalBarCommands = (ScrollBar) tabViewTestPointsAPMns.lookup(".scroll-bar:vertical");
        verticalBarErrors = (ScrollBar) tabViewErrosAPMns.lookup(".scroll-bar:vertical");

        bindScrolls(verticalBarCommands, verticalBarErrors);

        //RP+
        verticalBarCommands = (ScrollBar) tabViewTestPointsRPPls.lookup(".scroll-bar:vertical");
        verticalBarErrors = (ScrollBar) tabViewErrosRPPls.lookup(".scroll-bar:vertical");

        bindScrolls(verticalBarCommands, verticalBarErrors);

        //RP-
        verticalBarCommands = (ScrollBar) tabViewTestPointsRPMns.lookup(".scroll-bar:vertical");
        verticalBarErrors = (ScrollBar) tabViewErrosRPMns.lookup(".scroll-bar:vertical");

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