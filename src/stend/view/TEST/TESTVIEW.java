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
import stend.model.MethodicsForTest;

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

    private MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private StendDLLCommands stendDLLCommands;

    //Имя методики поверки
    private String metodicName = "default";

    //Значения коэффициента мощности
    private List<String> powerFactor = methodicsForTest.getPowerFactor();

    //Значения выставленного тока
    private List<String> current = methodicsForTest.getCurrent();

    //Список GridPane для выставления точек поверки
    private List<GridPane> gridPanesEnergyAndPhase;

    //Это трёхфазный стенд?
    private boolean isThrePhaseStend;

    //Лист с точками тестирования
    private ObservableList<Commands> testListForCollumAPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumAPMns = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumRPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumRPMns = FXCollections.observableArrayList(new ArrayList<>());

    //Листы с checkBox'ами.
    private ArrayList<CheckBox> checkBoxesListUAPPls = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesListUAPMns = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesListURPPls = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesListURPMns = new ArrayList<>();

    private ArrayList<CheckBox> checkBoxesListFAPPls = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesListFAPMns = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesListFRPPls = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesListFRPMns = new ArrayList<>();

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
    private Pane InflUInbPaneAPPls;

    @FXML
    private Pane InflUInbPaneAPMns;

    @FXML
    private Pane InflUInbPaneRPPls;

    @FXML
    private ToggleButton InfluenceTglBtnURPPls;

    @FXML
    private ToggleButton InfluenceTglBtnFRPPls;

    @FXML
    private ToggleButton addTglBtnInfURPPls;

    @FXML
    private ToggleButton addTglBtnInfFRPPls;

    @FXML
    private Pane InflFpaneRPPls;

    @FXML
    private ToggleButton InfluenceTglBtnUnbRPPls;

    @FXML
    private ToggleButton addTglBtnInfUnblRPPls;

    @FXML
    private Pane InflUpaneRPPls;

    @FXML
    private TextField txtFieldInfURPPls;

    @FXML
    private TextField txtFieldInfFRPPls;

    @FXML
    private TextField txtFieldInfUnblRPPls;


    //Поля необходимые для добавления точки испытания на влияние
    //AP+
    private double[] influenceUprocAPPls;
    private double[] influenceFprocAPPls;
    private String[] influenceInbUAPPls;

    //AP-
    private double[] influenceUprocAPMns;
    private double[] influenceFprocAPMns;
    private String[] influenceInbUAPMns;

    //RP+
    private double[] influenceUprocRPPls;
    private double[] influenceFprocRPPls;
    private String[] influenceInbURPPls;

    //RP-
    private double[] influenceUprocRPMns;
    private double[] influenceFprocRPMns;
    private String[] influenceInbURPMns;



    @FXML
    void initialize() {
        if (ConsoleHelper.properties.getProperty("stendType").equals("ThreePhaseStend")) {
            isThrePhaseStend = true;
        }
        initGridPane();

        APPlsPane.toFront();
        viewPointTableAPPls.toFront();
        gridPaneUAPPlus.toFront();
        InflUpaneAPPls.toFront();

        APPlusCRPSTA.setSelected(true);
        APPlus.setSelected(true);
        allPhaseBtn.setDisable(true);

        for (GridPane pane : gridPanesEnergyAndPhase) {
            pane.setDisable(true);
        }

        initTableView();
    }

    //Отвечает за отображение нужного окна настроек влияния
    @FXML
    void addInfluenceTests(ActionEvent event) {
        //AP+
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
            InflUInbPaneAPPls.toFront();
        }

        //AP-
        if (event.getSource() == InfluenceTglBtnUAPMns) {
            InfluenceTglBtnUAPMns.setSelected(addTglBtnInfUAPMns.isSelected());
            gridPaneUAPMns.toFront();
            InflUpaneAPMns.toFront();
        }

        if (event.getSource() == InfluenceTglBtnFAPMns) {
            InfluenceTglBtnFAPMns.setSelected(addTglBtnInfFAPMns.isSelected());
            gridPaneFAPMns.toFront();
            InflFpaneAPMns.toFront();
        }

        if (event.getSource() == InfluenceTglBtnUnbAPMns) {
            InfluenceTglBtnUnbAPMns.setSelected(addTglBtnInfUnblAPMns.isSelected());
            InflUInbPaneAPMns.toFront();
        }

        //RP+
        if (event.getSource() == InfluenceTglBtnURPPls) {
            InfluenceTglBtnURPPls.setSelected(addTglBtnInfURPPls.isSelected());
            gridPaneURPPls.toFront();
            InflUpaneRPPls.toFront();
        }

        if (event.getSource() == InfluenceTglBtnFRPPls) {
            InfluenceTglBtnFRPPls.setSelected(addTglBtnInfFRPPls.isSelected());
            gridPaneFRPPls.toFront();
            InflFpaneRPPls.toFront();
        }

        if (event.getSource() == InfluenceTglBtnUnbRPPls) {
            InfluenceTglBtnUnbRPPls.setSelected(addTglBtnInfUnblRPPls.isSelected());
            InflUInbPaneRPPls.toFront();
        }

        //RP-
        if (event.getSource() == InfluenceTglBtnURPMns) {
            InfluenceTglBtnURPMns.setSelected(addTglBtnInfURPMns.isSelected());
            gridPaneURPMns.toFront();
            InflUpaneRPMns.toFront();
        }

        if (event.getSource() == InfluenceTglBtnFRPMns) {
            InfluenceTglBtnFRPMns.setSelected(addTglBtnInfFRPMns.isSelected());
            gridPaneFRPMns.toFront();
            InflFpaneRPMns.toFront();
        }

        if (event.getSource() == InfluenceTglBtnUnbRPMns) {
            InfluenceTglBtnUnbRPMns.setSelected(addTglBtnInfUnblRPMns.isSelected());
            InflUnblnsPaneRPMns.toFront();
        }

        //Добавление параметров для добавления точек испытания
        String[] procentArr;

        //AP+ Uproc
        if (event.getSource() == addTglBtnInfUAPPls) {
            if (addTglBtnInfUAPPls.isSelected()) {
                procentArr = txtFieldInfUAPPls.getText().split(",");
                influenceUprocAPPls = new double[procentArr.length];

                for (int i = 0; i < procentArr.length; i++) {
                    influenceUprocAPPls[i] = Double.parseDouble(procentArr[i].trim());
                }

                InfluenceTglBtnUAPPls.setSelected(true);
                txtFieldInfUAPPls.setDisable(true);

                gridPaneUAPPlus.setDisable(false);
            } else {
                txtFieldInfUAPPls.setDisable(false);
                gridPaneUAPPlus.setDisable(true);
                InfluenceTglBtnUAPPls.setSelected(false);

                for (CheckBox checkBox :checkBoxesListUAPPls) {
                    checkBox.setSelected(false);
                }
            }
        }

        //AP+ Fproc
        if (event.getSource() == addTglBtnInfFAPPls) {
            if (addTglBtnInfFAPPls.isSelected()) {
                procentArr = txtFieldInfFAPPls.getText().split(",");
                influenceFprocAPPls = new double[procentArr.length];

                for (int i = 0; i < procentArr.length; i++) {
                    influenceFprocAPPls[i] = Double.parseDouble(procentArr[i].trim());
                }

                InfluenceTglBtnFAPPls.setSelected(true);
                txtFieldInfFAPPls.setDisable(true);

                gridPaneFAPPlus.setDisable(false);
            } else {
                txtFieldInfFAPPls.setDisable(false);
                gridPaneFAPPlus.setDisable(true);
                InfluenceTglBtnFAPPls.setSelected(false);

                for (CheckBox checkBox :checkBoxesListFAPPls) {
                    checkBox.setSelected(false);
                }
            }
        }

        //AP+ Uinbl
        if (event.getSource() == addTglBtnInfUnblAPPls) {
            if (addTglBtnInfUnblAPPls.isSelected()) {
                influenceInbUAPPls = txtFieldInfUnblAPPls.getText().split(",");

                InfluenceTglBtnUnbAPPls.setSelected(true);
                txtFieldInfUnblAPPls.setDisable(true);
            } else {
                InfluenceTglBtnUnbAPPls.setSelected(false);
                txtFieldInfUnblAPPls.setDisable(false);
            }
        }


        //AP- Uproc
        if (event.getSource() == addTglBtnInfUAPMns) {
            if (addTglBtnInfUAPMns.isSelected()) {
                procentArr = txtFieldInfUAPMns.getText().split(",");
                influenceUprocAPMns = new double[procentArr.length];

                for (int i = 0; i < procentArr.length; i++) {
                    influenceUprocAPMns[i] = Double.parseDouble(procentArr[i].trim());
                }

                InfluenceTglBtnUAPMns.setSelected(true);
                txtFieldInfUAPMns.setDisable(true);

                gridPaneUAPMns.setDisable(false);
            } else {
                txtFieldInfUAPMns.setDisable(false);
                gridPaneUAPMns.setDisable(true);
                InfluenceTglBtnUAPMns.setSelected(false);

                for (CheckBox checkBox : checkBoxesListUAPMns) {
                    checkBox.setSelected(false);
                }
            }
        }

        //AP- Fproc
        if (event.getSource() == addTglBtnInfFAPMns) {
            if (addTglBtnInfFAPMns.isSelected()) {
                procentArr = txtFieldInfFAPMns.getText().split(",");
                influenceFprocAPMns = new double[procentArr.length];

                for (int i = 0; i < procentArr.length; i++) {
                    influenceFprocAPMns[i] = Double.parseDouble(procentArr[i].trim());
                }

                InfluenceTglBtnFAPMns.setSelected(true);
                txtFieldInfFAPMns.setDisable(true);

                gridPaneFAPMns.setDisable(false);
            } else {
                txtFieldInfFAPMns.setDisable(false);
                gridPaneFAPMns.setDisable(true);
                InfluenceTglBtnFAPMns.setSelected(false);

                for (CheckBox checkBox : checkBoxesListFAPMns) {
                    checkBox.setSelected(false);
                }
            }
        }

        //AP- Uinbl
        if (event.getSource() == addTglBtnInfUnblAPMns) {
            if (addTglBtnInfUnblAPMns.isSelected()) {
                influenceInbUAPMns = txtFieldInfUnblAPMns.getText().split(",");

                InfluenceTglBtnUnbAPMns.setSelected(true);
                txtFieldInfUnblAPMns.setDisable(true);
            } else {
                InfluenceTglBtnUnbAPMns.setSelected(false);
                txtFieldInfUnblAPMns.setDisable(false);
            }
        }


        //RP+ Uproc
        if (event.getSource() == addTglBtnInfURPPls) {
            if (addTglBtnInfURPPls.isSelected()) {
                procentArr = txtFieldInfURPPls.getText().split(",");
                influenceUprocRPPls = new double[procentArr.length];

                for (int i = 0; i < procentArr.length; i++) {
                    influenceUprocRPPls[i] = Double.parseDouble(procentArr[i].trim());
                }

                InfluenceTglBtnURPPls.setSelected(true);
                txtFieldInfURPPls.setDisable(true);

                gridPaneURPPls.setDisable(false);
            } else {
                txtFieldInfURPPls.setDisable(false);
                gridPaneURPPls.setDisable(true);
                InfluenceTglBtnURPPls.setSelected(false);

                for (CheckBox checkBox : checkBoxesListURPPls) {
                    checkBox.setSelected(false);
                }
            }
        }

        //RP+ Fproc
        if (event.getSource() == addTglBtnInfFRPPls) {
            if (addTglBtnInfFRPPls.isSelected()) {
                procentArr = txtFieldInfFRPPls.getText().split(",");
                influenceFprocRPPls = new double[procentArr.length];

                for (int i = 0; i < procentArr.length; i++) {
                    influenceFprocRPPls[i] = Double.parseDouble(procentArr[i].trim());
                }

                InfluenceTglBtnFRPPls.setSelected(true);
                txtFieldInfFRPPls.setDisable(true);

                gridPaneFRPPls.setDisable(false);
            } else {
                txtFieldInfFRPPls.setDisable(false);
                gridPaneFRPPls.setDisable(true);
                InfluenceTglBtnFRPPls.setSelected(false);

                for (CheckBox checkBox : checkBoxesListFRPPls) {
                    checkBox.setSelected(false);
                }
            }
        }

        //RP+ Uinbl
        if (event.getSource() == addTglBtnInfUnblRPPls) {
            if (addTglBtnInfUnblRPPls.isSelected()) {
                influenceInbURPPls= txtFieldInfUnblRPPls.getText().split(",");

                InfluenceTglBtnUnbRPPls.setSelected(true);
                txtFieldInfUnblRPPls.setDisable(true);
            } else {
                InfluenceTglBtnUnbRPPls.setSelected(false);
                txtFieldInfUnblRPPls.setDisable(false);
            }
        }


        //RP- Uproc
        if (event.getSource() == addTglBtnInfURPMns) {
            if (addTglBtnInfURPMns.isSelected()) {
                procentArr = txtFieldInfURPMns.getText().split(",");
                influenceUprocRPMns = new double[procentArr.length];

                for (int i = 0; i < procentArr.length; i++) {
                    influenceUprocRPMns[i] = Double.parseDouble(procentArr[i].trim());
                }

                InfluenceTglBtnURPMns.setSelected(true);
                txtFieldInfURPMns.setDisable(true);

                gridPaneURPMns.setDisable(false);
            } else {
                txtFieldInfURPMns.setDisable(false);
                gridPaneURPMns.setDisable(true);
                InfluenceTglBtnURPMns.setSelected(false);

                for (CheckBox checkBox : checkBoxesListURPMns) {
                    checkBox.setSelected(false);
                }
            }
        }

        //RP- Fproc
        if (event.getSource() == addTglBtnInfFRPMns) {
            if (addTglBtnInfFRPMns.isSelected()) {
                procentArr = txtFieldInfFRPMns.getText().split(",");
                influenceFprocRPMns = new double[procentArr.length];

                for (int i = 0; i < procentArr.length; i++) {
                    influenceFprocRPMns[i] = Double.parseDouble(procentArr[i].trim());
                }

                InfluenceTglBtnFRPMns.setSelected(true);
                txtFieldInfFRPMns.setDisable(true);

                gridPaneFRPMns.setDisable(false);
            } else {
                txtFieldInfFRPMns.setDisable(false);
                gridPaneFRPMns.setDisable(true);
                InfluenceTglBtnFRPMns.setSelected(false);

                for (CheckBox checkBox : checkBoxesListFRPMns) {
                    checkBox.setSelected(false);
                }
            }
        }

        //RP- Uinbl
        if (event.getSource() == addTglBtnInfUnblRPMns) {
            if (addTglBtnInfUnblRPMns.isSelected()) {
                influenceInbURPMns = txtFieldInfUnblRPMns.getText().split(",");

                InfluenceTglBtnUnbRPMns.setSelected(true);
                txtFieldInfUnblRPMns.setDisable(true);
            } else {
                InfluenceTglBtnUnbRPMns.setSelected(false);
                txtFieldInfUnblRPMns.setDisable(false);
            }
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
        InflUpaneAPPls.toFront();
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
        InflUpaneAPMns.toFront();
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
        InflUpaneRPPls.toFront();
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
        InflUpaneRPMns.toFront();
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

                if (idCheckBox[0].equals("U") && idCheckBox[3].equals("A")) {
                    if (idCheckBox[4].equals("P")) {
                        checkBoxesListUAPPls.add(checkBox);
                    }

                    if (idCheckBox[4].equals("N")) {
                        checkBoxesListUAPMns.add(checkBox);
                    }
                }

                if (idCheckBox[0].equals("U") && idCheckBox[3].equals("R")) {
                    if (idCheckBox[4].equals("P")) {
                        checkBoxesListURPPls.add(checkBox);
                    }

                    if (idCheckBox[4].equals("N")) {
                        checkBoxesListURPMns.add(checkBox);
                    }
                }

                if (idCheckBox[0].equals("F") && idCheckBox[3].equals("A")) {
                    if (idCheckBox[4].equals("P")) {
                        checkBoxesListFAPPls.add(checkBox);
                    }

                    if (idCheckBox[4].equals("N")) {
                        checkBoxesListFAPMns.add(checkBox);
                    }
                }

                if (idCheckBox[0].equals("F") && idCheckBox[3].equals("R")) {
                    if (idCheckBox[4].equals("P")) {
                        checkBoxesListFRPPls.add(checkBox);
                    }

                    if (idCheckBox[4].equals("N")) {
                        checkBoxesListFRPMns.add(checkBox);
                    }
                }

                //Действие при нажатии на чек бокс
                checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                    if (newVal) {
                        addTestPointInMethodic(finalCheckBox.getId());

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
        /** U;1;H;A;P;0.2 Ib;0.5C
         *  По напряжению или частоте
         *  режим;
         *  Фазы по которым необходимо пустить ток (H);
         *  Тип энергии актив/реактив;
         *  Направление тока прямое/обратное
         *  Ток 0.2 Ib
         *  Коэф мощности 0.8L
         *  */
        String[] dirCurFactor = testPoint.split(";");

        //Влияние напряжения или частоты
        String influenceUorF = dirCurFactor[0];

        //Phase - Режим
        int phase = Integer.parseInt(dirCurFactor[1]);

        //фазы, по которым пустить ток
        String iABC = dirCurFactor[2];

        //Тип энергии
        String energyType = dirCurFactor[3];

        //Направление тока
        String currentDirection = dirCurFactor[4];

        //Целое значеник процент + Максимальный или минимальный
        String[] curAndPer = dirCurFactor[5].split(" ");
        //Процент от тока
        String percent = curAndPer[0];
        //Максимальный или минимальный ток.
        String current = curAndPer[1];

        //Коэф мощности
        String powerFactor = dirCurFactor[6];

        //Добавление точек по влиянию AP+
        if (energyType.equals("A") && currentDirection.equals("P")) {

            if (influenceUorF.equals("U")) {
                for (double influenceUprocAPPl : influenceUprocAPPls) {
                    testListForCollumAPPls.add(new ErrorCommand(influenceUorF, stendDLLCommands, testPoint, phase, current, influenceUprocAPPl,
                            0, percent, iABC, powerFactor, 0));
                }
            }

            if (influenceUorF.equals("F")) {

                for (double influenceFprocAPPl : influenceFprocAPPls) {
                    testListForCollumAPPls.add(new ErrorCommand(influenceUorF, stendDLLCommands, testPoint, phase, current, influenceFprocAPPl,
                            0, percent, iABC, powerFactor, 0));
                }
            }
        }

        //Добавление точек по влиянию AP-
        if (energyType.equals("A") && currentDirection.equals("N")) {

            if (influenceUorF.equals("U")) {

                for (double influenceUprocAPMns : influenceUprocAPMns) {
                    testListForCollumAPMns.add(new ErrorCommand(influenceUorF, stendDLLCommands, testPoint, phase, current, influenceUprocAPMns,
                            1, percent, iABC, powerFactor, 1));
                }
            }

            if (influenceUorF.equals("F")) {

                for (double influenceFprocAPMns : influenceFprocAPMns) {
                    testListForCollumAPMns.add(new ErrorCommand(influenceUorF, stendDLLCommands, testPoint, phase, current, influenceFprocAPMns,
                            1, percent, iABC, powerFactor, 1));
                }
            }
        }


        //Добавление точек по влиянию RP+
        if (energyType.equals("R") && currentDirection.equals("P")) {
            if (influenceUorF.equals("U")) {

                for (double influenceUprocRPPls : influenceUprocRPPls) {
                    testListForCollumRPPls.add(new ErrorCommand(influenceUorF, stendDLLCommands, testPoint, phase, current, influenceUprocRPPls,
                            0, percent, iABC, powerFactor, 2));
                }
            }

            if (influenceUorF.equals("F")) {

                for (double influenceFprocRPPls : influenceFprocRPPls) {
                    testListForCollumRPPls.add(new ErrorCommand(influenceUorF, stendDLLCommands, testPoint, phase, current, influenceFprocRPPls,
                            0, percent, iABC, powerFactor, 2));
                }
            }
        }


        //Добавление точек по влиянию RP-
        if (energyType.equals("R") && currentDirection.equals("N")) {

            if (influenceUorF.equals("U")) {
                for (double influenceUprocRPMns : influenceUprocRPMns) {
                    testListForCollumRPMns.add(new ErrorCommand(influenceUorF, stendDLLCommands, testPoint, phase, current, influenceUprocRPMns,
                            1, percent, iABC, powerFactor, 3));
                }

            }

            if (influenceUorF.equals("F")) {
                for (double influenceFprocRPPls : influenceFprocRPMns) {
                    testListForCollumRPMns.add(new ErrorCommand(influenceUorF, stendDLLCommands, testPoint, phase, current, influenceFprocRPPls,
                            1, percent, iABC, powerFactor, 3));
                }
            }
        }
    }

    //Удаляет точку по айди чекбокса
    private void deleteTestPointInMethodic(String [] point) {
        ErrorCommand errorCommand;
        String str;

        if (point[3].equals("A") && point[4].equals("P")) {

            if (point[0].equals("U")) {

                for (double influenceUprocAPPl : influenceUprocAPPls) {
                    str = influenceUprocAPPl + "%" + point[0] + "n: " + point[6] + "; " + point[5];

                    for (int j = 0; j < testListForCollumAPPls.size(); j++) {
                        errorCommand = (ErrorCommand) testListForCollumAPPls.get(j);
                        if (errorCommand.getName().equals(str)) {
                            testListForCollumAPPls.remove(testListForCollumAPPls.get(j));
                            j--;
                        }
                    }
                }
            }

            if (point[0].equals("F")) {
                for (double influenceFprocAPPl : influenceFprocAPPls) {
                    str = influenceFprocAPPl + "%" + point[0] + "n: " + point[6] + "; " + point[5];

                    for (int j = 0; j < testListForCollumAPPls.size(); j++) {
                        errorCommand = (ErrorCommand) testListForCollumAPPls.get(j);
                        if (errorCommand.getName().equals(str)) {
                            testListForCollumAPPls.remove(testListForCollumAPPls.get(j));
                            j--;
                        }
                    }
                }
            }
        }

        if (point[3].equals("A") && point[4].equals("N")) {
            if (point[0].equals("U")) {

                for (double proc : influenceUprocAPMns) {
                    str = proc + "%" + point[0] + "n: " + point[6] + "; " + point[5];

                    for (int j = 0; j < testListForCollumAPMns.size(); j++) {
                        errorCommand = (ErrorCommand) testListForCollumAPMns.get(j);
                        if (errorCommand.getName().equals(str)) {
                            testListForCollumAPMns.remove(testListForCollumAPMns.get(j));
                            j--;
                        }
                    }
                }
            }

            if (point[0].equals("F")) {
                for (double proc : influenceFprocAPMns) {
                    str = proc + "%" + point[0] + "n: " + point[6] + "; " + point[5];

                    for (int j = 0; j < testListForCollumAPMns.size(); j++) {
                        errorCommand = (ErrorCommand) testListForCollumAPMns.get(j);
                        if (errorCommand.getName().equals(str)) {
                            testListForCollumAPMns.remove(testListForCollumAPMns.get(j));
                            j--;
                        }
                    }
                }
            }
        }

        if (point[3].equals("R") && point[4].equals("P")) {

            if (point[0].equals("U")) {

                for (double proc : influenceUprocRPPls) {
                    str = proc + "%" + point[0] + "n: " + point[6] + "; " + point[5];

                    for (int j = 0; j < testListForCollumRPPls.size(); j++) {
                        errorCommand = (ErrorCommand) testListForCollumRPPls.get(j);
                        if (errorCommand.getName().equals(str)) {
                            testListForCollumRPPls.remove(testListForCollumRPPls.get(j));
                            j--;
                        }
                    }
                }
            }

            if (point[0].equals("F")) {
                for (double proc : influenceFprocRPPls) {
                    str = proc + "%" + point[0] + "n: " + point[6] + "; " + point[5];

                    for (int j = 0; j < testListForCollumRPPls.size(); j++) {
                        errorCommand = (ErrorCommand) testListForCollumRPPls.get(j);
                        if (errorCommand.getName().equals(str)) {
                            testListForCollumRPPls.remove(testListForCollumRPPls.get(j));
                            j--;
                        }
                    }
                }
            }
        }

        if (point[3].equals("R") && point[4].equals("N")) {

            if (point[0].equals("U")) {

                for (double proc : influenceUprocRPMns) {
                    str = proc + "%" + point[0] + "n: " + point[6] + "; " + point[5];

                    for (int j = 0; j < testListForCollumRPMns.size(); j++) {
                        errorCommand = (ErrorCommand) testListForCollumRPMns.get(j);
                        if (errorCommand.getName().equals(str)) {
                            testListForCollumRPMns.remove(testListForCollumRPMns.get(j));
                            j--;
                        }
                    }
                }
            }

            if (point[0].equals("F")) {
                for (double proc : influenceFprocRPMns) {
                    str = proc + "%" + point[0] + "n: " + point[6] + "; " + point[5];

                    for (int j = 0; j < testListForCollumRPMns.size(); j++) {
                        errorCommand = (ErrorCommand) testListForCollumRPMns.get(j);
                        if (errorCommand.getName().equals(str)) {
                            testListForCollumRPMns.remove(testListForCollumRPMns.get(j));
                            j--;
                        }
                    }
                }
            }
        }
    }
}