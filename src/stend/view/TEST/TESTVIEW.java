package stend.view.TEST;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import stend.controller.Commands.*;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.helper.ConsoleHelper;
import stend.model.Methodic;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TESTVIEW extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/stend/view/TEST/TESTVIEW.fxml"));
        primaryStage.setTitle("Тест");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private Map<String, CheckBox> checkBoxMap = new HashMap<>();

    private Methodic methodic = new Methodic();

    private StendDLLCommands stendDLLCommands;

    //Имя методики поверки
    private String metodicName = "default";

    //Значения коэффициента мощности
    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");

    //Значения выставленного тока
    private List<String> current = Arrays.asList("1.0 Imax", "0.5 Imax", "0.2 Imax", "0.5 Ib", "1.0 Ib","0.2 Ib", "0.1 Ib", "0.05 Ib", "0.02 Ib", "0.02 Ib", "0.01 Ib");

    //Список GridPane для выставления точек поверки
    private List<GridPane> gridPanesEnergyAndPhase;

    //Это трёхфазный стенд?
    private boolean isThrePhaseStend;

    //Лист с точками
    private ObservableList<Commands> testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
    private ObservableList<Commands> testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
    private ObservableList<Commands> testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
    private ObservableList<Commands> testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));

    @FXML
    private ResourceBundle resources = ResourceBundle.getBundle("stendProperties");

    @FXML
    private URL location;

    //Отвечают за окно отображения выбранных точек тестирования
    //-------------------------------------------------------
    //Активная энергия в прямом направлении тока
    @FXML
    private TableView<Commands> viewPointTableAPPls = new TableView<>();

    @FXML
    private TableColumn<Commands, String> loadCurrTabColAPPls;

    @FXML
    private TableColumn<Commands, String> eMaxTabColAPPls;

    @FXML
    private TableColumn<Commands, String> eMinTabColAPPls;

    @FXML
    private TableColumn<Commands, String> amountImplTabColAPPls;

    //-------------------------------------------------------
    //Активная энергия в обратном направлении тока
    @FXML
    private TableView<Commands> viewPointTableAPMns;

    @FXML
    private TableColumn<Commands, String> loadCurrTabColAPMns;

    @FXML
    private TableColumn<Commands, String> eMaxTabColAPMns;

    @FXML
    private TableColumn<Commands, String> eMinTabColAPMns;

    @FXML
    private TableColumn<Commands, String> amountImplTabColAPMns;

    //--------------------------------------------------------
    //Реактивная энергия в прямом напралении тока
    @FXML
    private TableView<Commands> viewPointTableRPPls;

    @FXML
    private TableColumn<Commands, String> loadCurrTabColRPPls;

    @FXML
    private TableColumn<Commands, String> eMaxTabColRPPls;

    @FXML
    private TableColumn<Commands, String> eMinTabColRPPls;

    @FXML
    private TableColumn<Commands, String> amountImplTabColRPPls;

    //--------------------------------------------------------
    //Реактивная энергия в обратном напралении тока
    @FXML
    private TableView<Commands> viewPointTableRPMns;

    @FXML
    private TableColumn<Commands, String> loadCurrTabColRPMns;

    @FXML
    private TableColumn<Commands, String> eMaxTabColRPMns;

    @FXML
    private TableColumn<Commands, String> eMinTabColRPMns;

    @FXML
    private TableColumn<Commands, String> amountImplTabColRPMns;
    //-------------------------------------------------------
    //Данный блок отвечает за сетку выбора точки.
    //Влияние напряжения

    @FXML
    private GridPane gridPaneUAPPlus;

    @FXML
    private GridPane gridPaneUAPMns;

    @FXML
    private GridPane gridPaneURPPls;

    @FXML
    private GridPane gridPaneURPMns;

    //Влияние частоты
    @FXML
    private GridPane gridPaneFAPPlus;

    @FXML
    private GridPane gridPaneFAPMns;

    @FXML
    private GridPane gridPaneFRPPls;

    @FXML
    private GridPane gridPaneFRPMns;

    //-------------------------------------------------------
    @FXML
    private ToggleButton allPhaseBtn;

    @FXML
    private ToggleButton APPlus;

    @FXML
    private ToggleButton APMinus;

    @FXML
    private ToggleButton RPPlus;

    @FXML
    private ToggleButton RPMinus;

    //-------------------------------------------------------
    @FXML
    private Button CancelBtn;

    @FXML
    private Button SaveBtn;

    //Этот блок кода отвечает за установку параметров тестов Влияния
    //---------------------------------------------------------------------
    //Активная энергия в прямом напралении
    @FXML
    private ToggleButton APPlusCRPSTA;

    @FXML
    private Pane APPlsPane;

    @FXML
    private ToggleButton InfluenceTglBtnUAPPls;

    @FXML
    private ToggleButton InfluenceTglBtnFAPPls;

    @FXML
    private ToggleButton InfluenceTglBtnUnbAPPls;

    @FXML
    private Pane InflUpaneAPPls;

    @FXML
    private ToggleButton addTglBtnInfUAPPls;

    @FXML
    private TextField txtFieldInfUAPPls;

    @FXML
    private Pane InflUnblnsPaneAPPls;

    @FXML
    private ToggleButton addTglBtnInfFAPPls;

    @FXML
    private TextField txtFieldInfFAPPls;

    @FXML
    private Pane InflFpaneAPPls;

    @FXML
    private ToggleButton addTglBtnInfUnblAPPls;

    @FXML
    private TextField txtFieldInfUnblAPPls;

    //Активная энергия в обратном напралении
    //--------------------------------------------------------
    @FXML
    private ToggleButton APMinusCRPSTA;

    @FXML
    private Pane APMnsPane;

    @FXML
    private ToggleButton InfluenceTglBtnUAPMns;

    @FXML
    private ToggleButton InfluenceTglBtnFAPMns;

    @FXML
    private ToggleButton InfluenceTglBtnUnbAPMns;

    @FXML
    private Pane InflUpaneAPMns;

    @FXML
    private ToggleButton addTglBtnInfUAPMns;

    @FXML
    private TextField txtFieldInfUAPMns;

    @FXML
    private Pane InflUnblnsPaneAPMns;

    @FXML
    private ToggleButton addTglBtnInfFAPMns;

    @FXML
    private TextField txtFieldInfFAPMns;

    @FXML
    private Pane InflFpaneAPMns;

    @FXML
    private ToggleButton addTglBtnInfUnblAPMns;

    @FXML
    private TextField txtFieldInfUnblAPMns;

    //--------------------------------------------------------
    //Реактивная энергия в обратном направлении
    @FXML
    private ToggleButton RPMinusCRPSTA;

    @FXML
    private Pane RPMnsPane;

    @FXML
    private ToggleButton InfluenceTglBtnURPMns;

    @FXML
    private ToggleButton InfluenceTglBtnFRPMns;

    @FXML
    private ToggleButton InfluenceTglBtnUnbRPMns;

    @FXML
    private Pane InflUpaneRPMns;

    @FXML
    private ToggleButton addTglBtnInfURPMns;

    @FXML
    private TextField txtFieldInfURPMns;

    @FXML
    private Pane InflUnblnsPaneRPMns;

    @FXML
    private ToggleButton addTglBtnInfFRPMns;

    @FXML
    private TextField txtFieldInfFRPMns;

    @FXML
    private Pane InflFpaneRPMns;

    @FXML
    private ToggleButton addTglBtnInfUnblRPMns;

    @FXML
    private TextField txtFieldInfUnblRPMns;
    //---------------------------------------------------------------------

    @FXML
    private ToggleButton RPPlusCRPSTA;

    @FXML
    private Pane RPPlsPane;

    @FXML
    void initialize() {
        if (ConsoleHelper.properties.getProperty("stendType").equals("ThreePhaseStend")) {
            isThrePhaseStend = true;
        }

        initGridPane();
        APPlsPane.toFront();


        viewPointTableAPPls.toFront();

        initTableView();
    }

    //Отвечает за отображение нужного окна настроек влияния
    @FXML
    void addInfluenceTests(ActionEvent event) {
        if (event.getSource() == InfluenceTglBtnUAPPls) {
            InfluenceTglBtnUAPPls.setSelected(addTglBtnInfUAPPls.isSelected());
            gridPaneUAPPlus.toFront();
            InflUpaneAPPls.toFront();
        }

        if (event.getSource() == InfluenceTglBtnFAPPls) {
            InfluenceTglBtnFAPPls.setSelected(addTglBtnInfFAPPls.isSelected());
            gridPaneFAPPlus.toFront();
            InflFpaneAPPls.toFront();
        }

        if (event.getSource() == InfluenceTglBtnUnbAPPls) {
            InfluenceTglBtnUnbAPPls.setSelected(addTglBtnInfUnblAPPls.isSelected());
            addTglBtnInfUnblAPPls.toFront();
        }

    }

    @FXML
    void saveOrCancelAction(ActionEvent event) {
    }

    //Устанавливает значение TgBtn в соответствующие значения
    @FXML
    void setPointFrameAction(ActionEvent event) {
        //Кнопки выбора необходимой сетки
        if (event.getSource() == APPlus || event.getSource() == APPlusCRPSTA) {
            setAPPlsTglBtn();
        }

        if (event.getSource() == APMinus || event.getSource() == APMinusCRPSTA) {
            setAPMnsTglBtn();
        }

        if (event.getSource() == RPPlus || event.getSource() == RPPlusCRPSTA) {
            setRPPlsTglBtn();
        }

        if (event.getSource() == RPMinus || event.getSource() == RPMinusCRPSTA) {
            setRPMnsTglBtn();
        }

    }

    //Устанавливает значение TglBtn если выбрана вкладка AP+
    private void setAPPlsTglBtn() {
        gridPaneUAPPlus.toFront();
        viewPointTableAPPls.toFront();
        APPlsPane.toFront();
        APPlus.setSelected(true);
        APMinus.setSelected(false);
        RPPlus.setSelected(false);
        RPMinus.setSelected(false);

        APPlusCRPSTA.setSelected(true);
        APMinusCRPSTA.setSelected(false);
        RPPlusCRPSTA.setSelected(false);
        RPMinusCRPSTA.setSelected(false);
    }

    //Устанавливает значение TglBtn если выбрана вкладка AP-
    private void setAPMnsTglBtn() {
        gridPaneUAPMns.toFront();
        viewPointTableAPMns.toFront();
        APMnsPane.toFront();
        APPlus.setSelected(false);
        APMinus.setSelected(true);
        RPPlus.setSelected(false);
        RPMinus.setSelected(false);

        APPlusCRPSTA.setSelected(false);
        APMinusCRPSTA.setSelected(true);
        RPPlusCRPSTA.setSelected(false);
        RPMinusCRPSTA.setSelected(false);
    }

    //Устанавливает значение TglBtn если выбрана вкладка RP+
    private void setRPPlsTglBtn() {
        gridPaneURPPls.toFront();
        viewPointTableRPPls.toFront();
        RPPlsPane.toFront();
        APPlus.setSelected(false);
        APMinus.setSelected(false);
        RPPlus.setSelected(true);
        RPMinus.setSelected(false);

        APPlusCRPSTA.setSelected(false);
        APMinusCRPSTA.setSelected(false);
        RPPlusCRPSTA.setSelected(true);
        RPMinusCRPSTA.setSelected(false);
    }

    //Устанавливает значение TglBtn если выбрана вкладка RP-
    private void setRPMnsTglBtn() {
        gridPaneURPMns.toFront();
        viewPointTableRPMns.toFront();
        RPMnsPane.toFront();
        APPlus.setSelected(false);
        APMinus.setSelected(false);
        RPPlus.setSelected(false);
        RPMinus.setSelected(true);

        APPlusCRPSTA.setSelected(false);
        APMinusCRPSTA.setSelected(false);
        RPPlusCRPSTA.setSelected(false);
        RPMinusCRPSTA.setSelected(true);
    }

    private void initGridPane() {
        initGridPanesEnergyAndPhase();

        // Phase - Режим:
// 		0 - Однофазный
//		1 - Трех-фазный четырех-проводной
//		2 - Трех-фазный трех-проводной
// 		3 - Трех-фазный трех-проводной реактив 90 градусов
// 		4 - Трех-фазный трех-проводной реактив 60 градусов
//		5 - Трех-фазный четырех-проводной (реактив)
//		6 - Трех-фазный трех-проводной (реактив)
//		7 - Однофазный реактив
//Режим;
        if (isThrePhaseStend) {
            gridPaneUAPPlus.setId("U;1;H;A;P");
            gridPaneUAPMns.setId("U;1;H;A;N");
            gridPaneURPPls.setId("U;5;H;R;P");
            gridPaneURPMns.setId("U;5;H;R;N");
            //Влияние частоты
            gridPaneFAPPlus.setId("F;1;H;A;P");
            gridPaneFAPMns.setId("F;1;H;A;N");
            gridPaneFRPPls.setId("F;5;H;R;P");
            gridPaneFRPMns.setId("F;5;H;R;N");
        } else {
            gridPaneUAPPlus.setId("U;0;H;A;P");
            gridPaneUAPMns.setId("U;0;H;A;N");
            gridPaneURPPls.setId("U;7;H;R;P");
            gridPaneURPMns.setId("U;7;H;R;N");
            //Влияние частоты
            gridPaneFAPPlus.setId("F;0;H;A;P");
            gridPaneFAPMns.setId("F;0;H;A;N");
            gridPaneFRPPls.setId("F;7;H;R;P");
            gridPaneFRPMns.setId("F;7;H;R;N");
        }

        for (GridPane pane : gridPanesEnergyAndPhase) {
            setBoxAndLabelGridPane(pane);
        }
    }


    private void initGridPanesEnergyAndPhase() {
        gridPanesEnergyAndPhase = Arrays.asList(
                gridPaneUAPPlus,
                gridPaneUAPMns,
                gridPaneURPPls,
                gridPaneURPMns,
                gridPaneFAPPlus,
                gridPaneFAPMns,
                gridPaneFRPPls,
                gridPaneFRPMns);
    }

    //Заполняет поля нужными значениями GridPane
    private void setBoxAndLabelGridPane(GridPane pane) {
        creadteGridPanel(pane);
        pane.setGridLinesVisible(true);
        Label label;
        CheckBox checkBox;

        for (int x = 0; x < current.size(); x++) {
            for (int y = 0; y < powerFactor.size(); y++) {

                //Устанавливаю значения строки тока
                if (y == 0) {
                    label = new Label(current.get(x));
                    GridPane.setColumnIndex(label, x + 1);
                    GridPane.setRowIndex(label, y);
                    GridPane.setHalignment(label, HPos.CENTER);
                    GridPane.setValignment(label, VPos.CENTER);
                    pane.getChildren().add(label);
                }

                //Устанавливаю значения столба PowerFactor
                if (x == 0) {
                    label = new Label(powerFactor.get(y));
                    GridPane.setRowIndex(label, y + 1);
                    GridPane.setColumnIndex(label, x);
                    GridPane.setHalignment(label, HPos.CENTER);
                    GridPane.setValignment(label, VPos.CENTER);
                    pane.getChildren().add(label);
                }

                //Устанавливаю CheckBox
                checkBox = new CheckBox();
                checkBox.setId(pane.getId() + ";" + current.get(x) + ";" + powerFactor.get(y));
                CheckBox finalCheckBox = checkBox;
                String[] idCheckBox = finalCheckBox.getId().split(";");
                String energy = idCheckBox[2];
                String direction = idCheckBox[3];

                checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                    if (newVal) {
                        addTestPointInMethodic(finalCheckBox.getId());
                        if (energy.equals("A") && direction.equals("P")) {
                            testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                            viewPointTableAPPls.setItems(testListForCollumAPPls);
                        }

                        if (energy.equals("A") && direction.equals("N")) {
                            testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                            viewPointTableAPMns.setItems(testListForCollumAPPls);
                        }

                        if (energy.equals("R") && direction.equals("P")) {
                            testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                            viewPointTableRPPls.setItems(testListForCollumAPPls);
                        }

                        if (energy.equals("R") && direction.equals("N")) {
                            testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                            viewPointTableRPMns.setItems(testListForCollumAPPls);
                        }
                    } else {
                        deleteTestPointInMethodic(idCheckBox);
                    }
                });

                GridPane.setColumnIndex(checkBox, x + 1);
                GridPane.setRowIndex(checkBox, y + 1);
                GridPane.setHalignment(checkBox, HPos.CENTER);
                GridPane.setValignment(checkBox, VPos.CENTER);

                pane.getChildren().add(checkBox);
            }
        }
    }

    //Создаёт поле нужной величины
    private void creadteGridPanel(GridPane pane) {
        for (int i = 0; i < current.size() + 1; i++) {
            pane.getColumnConstraints().add(new ColumnConstraints(50));
        }
        for (int j = 0; j < powerFactor.size() + 1; j++) {
            pane.getRowConstraints().add(new RowConstraints(23));
        }
    }

    //Имитация ToggleGroup
    private void setGropToggleButton(ActionEvent event) {

    }


    private void gridPaneToFront(GridPane pane) {
        pane.toFront();
    }

    //Инициализирует таблицу для отображения выбранных точек
    private void initTableView() {
        List<TableColumn<Commands, String>> collumnListAPPls = Arrays.asList(
                loadCurrTabColAPPls,
                eMaxTabColAPPls,
                eMinTabColAPPls,
                amountImplTabColAPPls
        );

        List<TableColumn<Commands, String>> collumnListAPMns = Arrays.asList(
                loadCurrTabColAPMns,
                eMaxTabColAPMns,
                eMinTabColAPMns,
                amountImplTabColAPMns
        );

        List<TableColumn<Commands, String>> collumnListRPPls = Arrays.asList(
                loadCurrTabColRPPls,
                eMaxTabColRPPls,
                eMinTabColRPPls,
                amountImplTabColRPPls
        );

        List<TableColumn<Commands, String>> collumnListRPMns = Arrays.asList(
                loadCurrTabColRPMns,
                eMaxTabColRPMns,
                eMinTabColRPMns,
                amountImplTabColRPMns
        );

        Map<Integer, List<TableColumn<Commands, String>>> mapTableColumn = new HashMap<>();
        mapTableColumn.put(0, collumnListAPPls);
        mapTableColumn.put(1, collumnListAPMns);
        mapTableColumn.put(2, collumnListRPPls);
        mapTableColumn.put(3, collumnListRPMns);


        for (int i = 0; i < mapTableColumn.size(); i++) {
            //Устанавливаем данные для колонок (AP+, AP-, RP+, RP-)
            mapTableColumn.get(i).get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
            mapTableColumn.get(i).get(1).setCellValueFactory(new PropertyValueFactory<>("emax"));
            mapTableColumn.get(i).get(2).setCellValueFactory(new PropertyValueFactory<>("emin"));
            mapTableColumn.get(i).get(3).setCellValueFactory(new PropertyValueFactory<>("pulse"));

            //Выставляем отображение информации в колонке "по центру"
            mapTableColumn.get(i).get(1).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(2).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(3).setStyle( "-fx-alignment: CENTER;");

            //Устанавливаем возможность редактирования информации в колонке
            mapTableColumn.get(i).get(1).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(2).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(3).setCellFactory(TextFieldTableCell.forTableColumn());

            //Действие при изменении информации в колонке
            mapTableColumn.get(i).get(1).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                ((ErrorCommand) command).setEmax(newImpulseValue);

            });

            mapTableColumn.get(i).get(2).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                ((ErrorCommand) command).setEmax(newImpulseValue);

            });

            mapTableColumn.get(i).get(3).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                ((ErrorCommand) command).setEmax(newImpulseValue);

            });
        }

        viewPointTableAPPls.setEditable(true);
        viewPointTableAPMns.setEditable(true);
        viewPointTableRPPls.setEditable(true);
        viewPointTableRPMns.setEditable(true);

        viewPointTableAPPls.setItems(testListForCollumAPPls);
        viewPointTableAPMns.setItems(testListForCollumAPMns);
        viewPointTableRPPls.setItems(testListForCollumRPPls);
        viewPointTableRPMns.setItems(testListForCollumRPMns);
    }

    //Добавляет тестовую точку в методику
    private void addTestPointInMethodic(String testPoint) {
        if (isThrePhaseStend) {
            stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
        } else {
            stendDLLCommands = OnePhaseStend.getOnePhaseStendInstance();
        }
        /** 1;H;A;P;0.2 Ib;0.5C
         *  режим;
         *  Фазы по которым необходимо пустить ток (H);
         *  Тип энергии актив/реактив;
         *  Направление тока прямое/обратное
         *  Ток 0.2 Ib
         *  Коэф мощности 0.8L
         *  */
        String[] dirCurFactor = testPoint.split(";");

        //Phase - Режим
        int phase = Integer.parseInt(dirCurFactor[0]);

        //фазы, по которым пустить ток
        String iABC = dirCurFactor[1];

        //Тип энергии
        String energyType = dirCurFactor[2];

        //Направление тока
        String currentDirection = dirCurFactor[3];

        //Целое значеник процент + Максимальный или минимальный
        String[] curAndPer = dirCurFactor[4].split(" ");
        //Процент от тока
        String percent = curAndPer[0];
        //Максимальный или минимальный ток.
        String current = curAndPer[1];

        //Коэф мощности
        String powerFactor = dirCurFactor[5];


        if (energyType.equals("A") && currentDirection.equals("P")) {
            methodic.addCommandToList(0, new ErrorCommand(stendDLLCommands, testPoint, phase, current, 0, percent, iABC, powerFactor, 0));
        }

        if (energyType.equals("A") && currentDirection.equals("N")) {
            methodic.addCommandToList(1, new ErrorCommand(stendDLLCommands, testPoint, phase, current, 1, percent, iABC, powerFactor, 1));
        }

        if (energyType.equals("R") && currentDirection.equals("P")) {
            methodic.addCommandToList(2, new ErrorCommand(stendDLLCommands, testPoint, phase, current, 0, percent, iABC, powerFactor, 2));
        }

        if (energyType.equals("R") && currentDirection.equals("N")) {
            methodic.addCommandToList(3, new ErrorCommand(stendDLLCommands, testPoint, phase, current, 1, percent, iABC, powerFactor, 3));
        }
    }

    private void deleteTestPointInMethodic(String [] point) {
        ErrorCommand errorCommand;
        String str;

        if (point[2].equals("A") && point[3].equals("P")) {

            if (point[1].equals("H")) {
                str = point[5] + "; " + point[4];
            }else str = point[1] + ": " + point[5] + "; " + point[4];

            for (Commands current : Methodic.commandsMap.get(0)) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getName().equals(str)) {
                        Methodic.commandsMap.get(0).remove(current);
                        break;
                    }
                }
            }
            testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
            viewPointTableAPPls.setItems(testListForCollumAPPls);
        }

        if (point[2].equals("A") && point[3].equals("N")) {

            if (point[1].equals("H")) {
                str = point[5] + "; " + point[4];
            }else str = point[1] + ": " + point[5] + "; " + point[4];

            for (Commands current : Methodic.commandsMap.get(1)) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getName().equals(str)) {
                        Methodic.commandsMap.get(1).remove(current);
                        break;
                    }
                }
            }
            testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
            viewPointTableAPMns.setItems(testListForCollumAPMns);
        }

        if (point[2].equals("R") && point[3].equals("P")) {

            if (point[1].equals("H")) {
                str = point[5] + "; " + point[4];
            }else str = point[1] + ": " + point[5] + "; " + point[4];

            for (Commands current : Methodic.commandsMap.get(2)) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getName().equals(str)) {
                        Methodic.commandsMap.get(2).remove(current);
                        break;
                    }
                }
            }
            testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
            viewPointTableRPPls.setItems(testListForCollumRPPls);
        }

        if (point[2].equals("R") && point[3].equals("N")) {

            if (point[1].equals("H")) {
                str = point[5] + "; " + point[4];
            }else str = point[1] + ": " + point[5] + "; " + point[4];

            for (Commands current : Methodic.commandsMap.get(3)) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getName().equals(str)) {
                        Methodic.commandsMap.get(3).remove(current);
                        break;
                    }
                }
            }
            testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
            viewPointTableRPMns.setItems(testListForCollumRPMns);
        }
    }

    private void loadStage(String fxml, String stageName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(stageName);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}