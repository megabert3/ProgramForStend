package stend.controller.viewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import stend.controller.Commands.Commands;
import stend.controller.Meter;
import stend.model.Methodic;

import java.util.List;

public class TestErrorTableFrameController {

    private List<Meter> listMetersForTest;

    private double Imax;

    private double Ib;

    private double Un;

    private double Fn;

    private Methodic methodic;

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
    private TableColumn<Commands, String> tabColTestPointsAPPls;

    @FXML
    private TableView<Meter.Error> tabViewErrosAPPls;

    @FXML
    private SplitPane splPaneAPMns;

    @FXML
    private TableView<Commands> tabViewTestPointsAPMns;

    @FXML
    private TableColumn<Commands, String> tabColTestPointsAPMns;

    @FXML
    private TableView<Meter.Error> tabViewErrosAPMns;

    @FXML
    private SplitPane splPaneRPPls;

    @FXML
    private TableView<Commands> tabViewTestPointsRPPls;

    @FXML
    private TableColumn<Commands, String> tabColTestPointsRPPls;

    @FXML
    private TableView<Meter.Error> tabViewErrosRPPls;

    @FXML
    private SplitPane splPaneRPMns;

    @FXML
    private TableView<Commands> tabViewTestPointsRPMns;

    @FXML
    private TableColumn<Commands, String> tabColTestPointsRPMns;

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

    @FXML
    void actionEventSaveExit(ActionEvent event) {

    }

    @FXML
    void actionEventSwithEnergyPane(ActionEvent event) {
        if (event.getSource() == tglBtnAPPls) {
            splPaneAPPls.toFront();
            tglBtnAPPls.setSelected(true);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnAPMns) {
            splPaneAPMns.toFront();
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(true);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnRPPls) {
            splPaneRPPls.toFront();
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(true);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnRPMns) {
            splPaneRPMns.toFront();
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(true);
        }
    }

    @FXML
    void actionEventTestControl(ActionEvent event) {

    }

    @FXML
    void initialize() {
        tglBtnAPPls.setSelected(true);
        splPaneAPPls.toFront();
    }

    public void myInitTestErrorTableFrame() {
        initErrorsForMeters();

        //Инициирую колонку с точками для испытаний AP+
        tabColTestPointsAPPls.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPointsAPPls.setSortable(false);
        ObservableList<Commands> commandsAPPls = FXCollections.observableArrayList(methodic.getCommandsMap().get(0));
        tabViewTestPointsAPPls.setItems(commandsAPPls);

        //Инициирую колонку с точками для испытаний AP-
        tabColTestPointsAPMns.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPointsAPMns.setSortable(false);
        ObservableList<Commands> commandsAPMns = FXCollections.observableArrayList(methodic.getCommandsMap().get(1));
        tabViewTestPointsAPMns.setItems(commandsAPMns);

        //Инициирую колонку с точками для испытаний RP+
        tabColTestPointsRPPls.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPointsRPPls.setSortable(false);
        ObservableList<Commands> commandsRPPls = FXCollections.observableArrayList(methodic.getCommandsMap().get(2));
        tabViewTestPointsRPPls.setItems(commandsRPPls);

        //Инициирую колонку с точками для испытаний RP+
        tabColTestPointsRPMns.setCellValueFactory(new PropertyValueFactory<>("name"));
        tabColTestPointsRPMns.setSortable(false);
        ObservableList<Commands> commandsRPMns = FXCollections.observableArrayList(methodic.getCommandsMap().get(3));
        tabViewTestPointsRPMns.setItems(commandsRPMns);

        //-----------------------------------------------------------
        //В зависимости от количества счётчиков инициализирую поля для отображения погрешности
        for (int i = 0; i < listMetersForTest.size(); i++) {

            //Создаю колонки счётчиков для splitPane AP+
            TableColumn<Meter.Error, String> tableColumnAPPls = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnAPPls.setCellValueFactory(new PropertyValueFactory<>("lastError"));
            tableColumnAPPls.setSortable(false);
            ObservableList<Meter.Error> observableListAPPls = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPPls());
            tabViewErrosAPPls.setItems(observableListAPPls);
            tabViewErrosAPPls.getColumns().add(tableColumnAPPls);

            //Создаю колонки счётчиков для splitPane AP-
            TableColumn<Meter.Error, String> tableColumnAPMns = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnAPMns.setCellValueFactory(new PropertyValueFactory<>("lastError"));
            tableColumnAPMns.setSortable(false);
            ObservableList<Meter.Error> observableListAPMns = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListAPMns());
            tabViewErrosAPMns.setItems(observableListAPMns);
            tabViewErrosAPMns.getColumns().add(tableColumnAPMns);

            //Создаю колонки счётчиков для splitPane RP+
            TableColumn<Meter.Error, String> tableColumnRPPls = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnRPPls.setCellValueFactory(new PropertyValueFactory<>("lastError"));
            tableColumnRPPls.setSortable(false);
            ObservableList<Meter.Error> observableListRPPls = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPPls());
            tabViewErrosRPPls.setItems(observableListRPPls);
            tabViewErrosRPPls.getColumns().add(tableColumnRPPls);

            //Создаю колонки счётчиков для splitPane RP-
            TableColumn<Meter.Error, String> tableColumnRPMns = new TableColumn<>("Место " + listMetersForTest.get(i).getId());
            tableColumnRPMns.setCellValueFactory(new PropertyValueFactory<>("lastError"));
            tableColumnRPMns.setSortable(false);
            ObservableList<Meter.Error> observableListRPMns = FXCollections.observableArrayList(listMetersForTest.get(i).getErrorListRPMns());
            tabViewErrosRPMns.setItems(observableListRPMns);
            tabViewErrosRPMns.getColumns().add(tableColumnRPMns);
        }
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
}