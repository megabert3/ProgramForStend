package stend.controller.viewController.methodicsFrameController.addEditFraneController;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stend.controller.Commands.*;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.controller.viewController.methodicsFrameController.MethodicNameController;
import stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;
import stend.helper.ConsoleHelper;
import stend.model.Methodic;
import stend.model.MethodicsForTest;

public class AddEditFrameController {

    private MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private InfluenceFrame influenceFrame;

    private MethodicNameController methodicNameController;

    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    private Methodic methodic;

    private StendDLLCommands stendDLLCommands;

    //Это окно вызвано кнопкой редактировать?
    private boolean edit;

    //Значения коэффициента мощности
    private List<String> powerFactor = methodicsForTest.getPowerFactor();

    //Значения выставленного тока
    private List<String> current = methodicsForTest.getCurrent();

    //Список GridPane для выставления точек поверки
    private List<GridPane> gridPanesEnergyAndPhase;

    //Это трёхфазный стенд?
    private boolean isThrePhaseStend;

    //Листы с checkBox'ами.
    //AP+
    private ArrayList<CheckBox> checkBoxesAllPhaseAPPlus = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseAAPPlus = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseBAPPlus = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseCAPPlus = new ArrayList<>();

    //AP-
    private ArrayList<CheckBox> checkBoxesAllPhaseAPMns = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseAAPMns = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseBAPMns = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseCAPMns = new ArrayList<>();

    //RP+
    private ArrayList<CheckBox> checkBoxesAllPhaseRPPlus = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseARPPlus = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseBRPPlus = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseCRPPlus = new ArrayList<>();

    //RP-
    private ArrayList<CheckBox> checkBoxesAllPhaseRPMns = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseARPMns = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseBRPMns = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesPhaseCRPMns = new ArrayList<>();


    //Сохранённые листы с точками
    private ArrayList<Commands> saveListForCollumAPPls = new ArrayList<>();
    private ArrayList<Commands> saveListForCollumAPMns = new ArrayList<>();
    private ArrayList<Commands> saveListForCollumRPPls = new ArrayList<>();
    private ArrayList<Commands> saveListForCollumRPMns = new ArrayList<>();

    //Лист с точками общая методика
    private ObservableList<Commands> testListForCollumAPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumAPMns = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumRPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumRPMns = FXCollections.observableArrayList(new ArrayList<>());

    //------------------------------------------------------------------------------------------------------------
    //Данные полученные с окна "влияния".
    //Значения в % от номинального напрряжения U, частоты F и т.д.
    private double[] saveInfluenceUprocAPPls = new double[0];
    private double[] saveInfluenceFprocAPPls = new double[0];
    private String[] saveInfluenceInbUAPPls = new String[0];

    //AP-
    private double[] saveInfluenceUprocAPMns = new double[0];
    private double[] saveInfluenceFprocAPMns = new double[0];
    private String[] saveInfluenceInbUAPMns = new String[0];

    //RP+
    private double[] saveInfluenceUprocRPPls = new double[0];
    private double[] saveInfluenceFprocRPPls = new double[0];
    private String[] saveInfluenceInbURPPls = new String[0];

    //RP-
    private double[] saveInfluenceUprocRPMns = new double[0];
    private double[] saveInfluenceFprocRPMns = new double[0];
    private String[] saveInfluenceInbURPMns = new String[0];

    //Лист с точками влияния
    private List<Commands> saveInflListForCollumAPPls = new ArrayList<>();
    private List<Commands> saveInflListForCollumAPMns = new ArrayList<>();
    private List<Commands> saveInflListForCollumRPPls = new ArrayList<>();
    private List<Commands> saveInflListForCollumRPMns = new ArrayList<>();


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
    //Активная энергия в прямом направлении, Все фазы и отдельно А В С
    @FXML
    private GridPane gridPaneAllPhaseAPPlus;

    @FXML
    private GridPane gridPanePhaseAAPPlus;

    @FXML
    private GridPane gridPanePhaseBAPPlus;

    @FXML
    private GridPane gridPanePhaseCAPPlus;

    //Активная энергия в обратном направлении, Все фазы и отдельно А В С
    @FXML
    private GridPane gridPaneAllPhaseAPMinus;

    @FXML
    private GridPane gridPanePhaseAAPMinus;

    @FXML
    private GridPane gridPanePhaseBAPMinus;

    @FXML
    private GridPane gridPanePhaseCAPMinus;

    //Реактивная энергия в прямом направлении, Все фазы и отдельно А В С
    @FXML
    private GridPane gridPaneAllPhaseRPPlus;

    @FXML
    private GridPane gridPanePhaseARPPlus;

    @FXML
    private GridPane gridPanePhaseBRPPlus;

    @FXML
    private GridPane gridPanePhaseCRPPlus;

    //Реактивная энергия в обратном направлении, Все фазы и отдельно А В С
    @FXML
    private GridPane gridPaneAllPhaseRPMinus;

    @FXML
    private GridPane gridPanePhaseARPMinus;

    @FXML
    private GridPane gridPanePhaseBRPMinus;

    @FXML
    private GridPane gridPanePhaseCRPMinus;

    //-------------------------------------------------------

    @FXML
    private ToggleButton allPhaseBtn;

    @FXML
    private ToggleButton APhaseBtn;

    @FXML
    private ToggleButton BPhaseBtn;

    @FXML
    private ToggleButton CPhaseBtn;

    @FXML
    private ToggleButton APPlus;

    @FXML
    private ToggleButton APMinus;

    @FXML
    private ToggleButton RPPlus;

    @FXML
    private ToggleButton RPMinus;

    @FXML
    private Button CancelBtn;

    @FXML
    private Button SaveBtn;

    //Этот блок кода отвечает за установку параметров тестов Самахода, ТХЧ, Константы и Чувствительности
    //---------------------------------------------------------------------
    //Активная энергия в прямом напралении
    @FXML
    private ToggleButton APPlusCRPSTA;

    @FXML
    private Pane APPlsPane;

    @FXML
    private ToggleButton CRPTogBtnAPPls;

    @FXML
    private ToggleButton STATogBtnAPPls;

    @FXML
    private ToggleButton RTCTogBtnAPPls;

    @FXML
    private ToggleButton ConstTogBtnAPPls;

    @FXML
    private Pane paneConstAPPls;

    @FXML
    private TextField txtFieldConsErAPPls;

    @FXML
    private TextField txtFieldEngConstAPPls;

    @FXML
    private TextField txtFieldConstAPPls;

    @FXML
    private ToggleButton addTglBtnConstAPPls;

    @FXML
    private Pane paneRTCAPPls;

    @FXML
    private TextField txtFieldRngEAPPls;

    @FXML
    private TextField txtFldRTCAmtMshAPPls;

    @FXML
    private ToggleButton addTglBtnRTCAPPls;

    @FXML
    private ChoiceBox<String> ChcBxRTCErrAPPls;

    @FXML
    private TextField txtFldRTCTimeMshAPPls;

    @FXML
    private Pane paneCRPAPPls;

    @FXML
    private TextField txtFieldCRPUProcAPPls;

    @FXML
    private TextField txtFieldTimeCRPAPPls;

    @FXML
    private TextField txtFieldCRPAmtImpAPPls;

    @FXML
    private ToggleButton addTglBtnCRPAPPls;

    @FXML
    private ToggleButton addTglBtnCRPAPPlsGOST;

    @FXML
    private Pane paneSTAAPPls;

    @FXML
    private TextField txtFieldSTAIProcAPPls;

    @FXML
    private TextField txtFieldTimeSRAAPPls;

    @FXML
    private TextField txtFieldSTAAmtImpAPPls;

    @FXML
    private ToggleButton addTglBtnSTAAPPls;

    @FXML
    private ToggleButton addTglBtnSTAAPPlsGOST;

    //Активная энергия в обратном напралении
    //--------------------------------------------------------
    @FXML
    private ToggleButton APMinusCRPSTA;

    @FXML
    private Pane APMnsPane;

    @FXML
    private ToggleButton CRPTogBtnAPMns;

    @FXML
    private ToggleButton STATogBtnAPMns;

    @FXML
    private ToggleButton RTCTogBtnAPMns;

    @FXML
    private ToggleButton ConstTogBtnAPMns;

    @FXML
    private Pane paneCRPAPMns;

    @FXML
    private TextField txtFieldCRPUProcAPMns;

    @FXML
    private TextField txtFieldTimeCRPAPMns;

    @FXML
    private TextField txtFieldCRPAmtImpAPMns;

    @FXML
    private ToggleButton addTglBtnCRPAPMns;

    @FXML
    private ToggleButton addTglBtnCRPAPMnsGOST;

    @FXML
    private Pane paneSTAAPMns;

    @FXML
    private TextField txtFieldSTAIProcAPMns;

    @FXML
    private TextField txtFieldTimeSRAAPMns;

    @FXML
    private TextField txtFieldSTAAmtImpAPMns;

    @FXML
    private ToggleButton addTglBtnSTAAPMns;

    @FXML
    private ToggleButton addTglBtnSTAAPMnsGOST;

    @FXML
    private Pane paneRTCAPMns;

    @FXML
    private TextField txtFieldRngEAPMns;

    @FXML
    private TextField txtFldRTCAmtMshAPMns;

    @FXML
    private ToggleButton addTglBtnRTCAPMns;

    @FXML
    private ChoiceBox<String> ChcBxRTCErrAPMns;

    @FXML
    private TextField txtFldRTCTimeMshAPMns;

    @FXML
    private Pane paneConstAPMns;

    @FXML
    private TextField txtFieldConsErAPMns;

    @FXML
    private TextField txtFieldEngConstAPMns;

    @FXML
    private TextField txtFieldConstAPMns;

    @FXML
    private ToggleButton addTglBtnConstAPMns;

    //Реактивная энергия в прямом направлении
    //--------------------------------------------------------
    @FXML
    private ToggleButton RPPlusCRPSTA;

    @FXML
    private Pane RPPlsPane;

    @FXML
    private ToggleButton CRPTogBtnRPPls;

    @FXML
    private ToggleButton STATogBtnRPPls;

    @FXML
    private ToggleButton RTCTogBtnRPPls;

    @FXML
    private ToggleButton ConstTogBtnRPPls;

    @FXML
    private Pane paneCRPRPPls;

    @FXML
    private TextField txtFieldCRPUProcRPPls;

    @FXML
    private TextField txtFieldTimeCRPRPPls;

    @FXML
    private TextField txtFieldCRPAmtImpRPPls;

    @FXML
    private ToggleButton addTglBtnCRPRPPls;

    @FXML
    private ToggleButton addTglBtnCRPRPPlsGOST;

    @FXML
    private Pane paneSTARPPls;

    @FXML
    private TextField txtFieldSTAIProcRPPls;

    @FXML
    private TextField txtFieldTimeSRARPPls;

    @FXML
    private TextField txtFieldSTAAmtImpRPPls;

    @FXML
    private ToggleButton addTglBtnSTARPPls;

    @FXML
    private ToggleButton addTglBtnSTARPPlsGOST;

    @FXML
    private Pane paneRTCRPPls;

    @FXML
    private TextField txtFieldRngERPPls;

    @FXML
    private TextField txtFldRTCAmtMshRPPls;

    @FXML
    private ToggleButton addTglBtnRTCRPPls;

    @FXML
    private ChoiceBox<String> ChcBxRTCErrRPPls;

    @FXML
    private TextField txtFldRTCTimeMshRPPls;

    @FXML
    private Pane paneConstRPPls;

    @FXML
    private TextField txtFieldConsErRPPls;

    @FXML
    private TextField txtFieldEngConstRPPls;

    @FXML
    private TextField txtFieldConstRPPls;

    @FXML
    private ToggleButton addTglBtnConstRPPls;

    //--------------------------------------------------------
    //Реактивная энергия в обратном направлении
    @FXML
    private ToggleButton RPMinusCRPSTA;

    @FXML
    private Pane RPMnsPane;

    @FXML
    private ToggleButton CRPTogBtnRPMns;

    @FXML
    private ToggleButton STATogBtnRPMns;

    @FXML
    private ToggleButton RTCTogBtnRPMns;

    @FXML
    private ToggleButton ConstTogBtnRPMns;

    @FXML
    private Pane paneCRPRPMns;

    @FXML
    private TextField txtFieldCRPUProcRPMns;

    @FXML
    private TextField txtFieldTimeCRPRPMns;

    @FXML
    private TextField txtFieldCRPAmtImpRPMns;

    @FXML
    private ToggleButton addTglBtnCRPRPMns;

    @FXML
    private ToggleButton addTglBtnCRPRPMnsGOST;

    @FXML
    private Pane paneSTARPMns;

    @FXML
    private TextField txtFieldSTAIProcRPMns;

    @FXML
    private TextField txtFieldTimeSRARPMns;

    @FXML
    private TextField txtFieldSTAAmtImpRPMns;

    @FXML
    private ToggleButton addTglBtnSTARPMns;

    @FXML
    private ToggleButton addTglBtnSTARPMnsGOST;

    @FXML
    private Pane paneRTCRPMns;

    @FXML
    private TextField txtFieldRngERPMns;

    @FXML
    private TextField txtFldRTCAmtMshRPMns;

    @FXML
    private TextField txtFldRTCTimeMshRPMns;

    @FXML
    private ToggleButton addTglBtnRTCRPMns;

    @FXML
    private Pane paneConstRPMns;

    @FXML
    private TextField txtFieldConsErRPMns;

    @FXML
    private TextField txtFieldEngConstRPMns;

    @FXML
    private TextField txtFieldConstRPMns;

    @FXML
    private ChoiceBox<String> ChcBxRTCErrRPMns;

    @FXML
    private ToggleButton addTglBtnConstRPMns;
    //---------------------------------------------------------------------

    @FXML
    private TextField metodicNameTxtFld;

    @FXML
    private Button influenceBtn;

    public void setMethodicNameController(MethodicNameController methodicNameController) {
        this.methodicNameController = methodicNameController;
    }

    void setSaveInfluenceUprocAPPls(double[] saveInfluenceUprocAPPls) {
        this.saveInfluenceUprocAPPls = saveInfluenceUprocAPPls;
    }

    void setSaveInfluenceFprocAPPls(double[] saveInfluenceFprocAPPls) {
        this.saveInfluenceFprocAPPls = saveInfluenceFprocAPPls;
    }

    void setSaveInfluenceInbUAPPls(String[] saveInfluenceInbUAPPls) {
        this.saveInfluenceInbUAPPls = saveInfluenceInbUAPPls;
    }

    void setSaveInfluenceUprocAPMns(double[] saveInfluenceUprocAPMns) {
        this.saveInfluenceUprocAPMns = saveInfluenceUprocAPMns;
    }

    void setSaveInfluenceFprocAPMns(double[] saveInfluenceFprocAPMns) {
        this.saveInfluenceFprocAPMns = saveInfluenceFprocAPMns;
    }

    void setSaveInfluenceInbUAPMns(String[] saveInfluenceInbUAPMns) {
        this.saveInfluenceInbUAPMns = saveInfluenceInbUAPMns;
    }

    void setSaveInfluenceUprocRPPls(double[] saveInfluenceUprocRPPls) {
        this.saveInfluenceUprocRPPls = saveInfluenceUprocRPPls;
    }

    void setSaveInfluenceFprocRPPls(double[] saveInfluenceFprocRPPls) {
        this.saveInfluenceFprocRPPls = saveInfluenceFprocRPPls;
    }

    void setSaveInfluenceInbURPPls(String[] saveInfluenceInbURPPls) {
        this.saveInfluenceInbURPPls = saveInfluenceInbURPPls;
    }

    void setSaveInfluenceUprocRPMns(double[] saveInfluenceUprocRPMns) {
        this.saveInfluenceUprocRPMns = saveInfluenceUprocRPMns;
    }

    void setSaveInfluenceFprocRPMns(double[] saveInfluenceFprocRPMns) {
        this.saveInfluenceFprocRPMns = saveInfluenceFprocRPMns;
    }

    void setSaveInfluenceInbURPMns(String[] saveInfluenceInbURPMns) {
        this.saveInfluenceInbURPMns = saveInfluenceInbURPMns;
    }

    void setSaveInflListForCollumAPPls(ArrayList<Commands> saveInflListForCollumAPPls) {
        this.saveInflListForCollumAPPls = saveInflListForCollumAPPls;
    }

    void setSaveInflListForCollumAPMns(ArrayList<Commands> saveInflListForCollumAPMns) {
        this.saveInflListForCollumAPMns = saveInflListForCollumAPMns;
    }

    void setSaveInflListForCollumRPPls(ArrayList<Commands> saveInflListForCollumRPPls) {
        this.saveInflListForCollumRPPls = saveInflListForCollumRPPls;
    }

    void setSaveInflListForCollumRPMns(ArrayList<Commands> saveInflListForCollumRPMns) {
        this.saveInflListForCollumRPMns = saveInflListForCollumRPMns;
    }

    public void setMethodicsAddEditDeleteFrameController(MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController) {
        this.methodicsAddEditDeleteFrameController = methodicsAddEditDeleteFrameController;
    }

    ObservableList<Commands> getTestListForCollumAPPls() {
        return testListForCollumAPPls;
    }

    ObservableList<Commands> getTestListForCollumAPMns() {
        return testListForCollumAPMns;
    }

    ObservableList<Commands> getTestListForCollumRPPls() {
        return testListForCollumRPPls;
    }

    ObservableList<Commands> getTestListForCollumRPMns() {
        return testListForCollumRPMns;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    //Устанавливает имя методики полученное с другого окна
    public void setTextFielMethodicName() {
        if (methodic == null) {
            metodicNameTxtFld.setText(methodicNameController.getName());
        } else metodicNameTxtFld.setText(methodic.getMethodicName());
        metodicNameTxtFld.setEditable(false);
    }

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
    }

    @FXML
    void initialize() {
        if (ConsoleHelper.properties.getProperty("stendType").equals("ThreePhaseStend")) {
            isThrePhaseStend = true;
        }

        initGridPane();
        APPlus.setSelected(true);
        allPhaseBtn.setSelected(true);
        APPlusCRPSTA.setSelected(true);
        APPlsPane.toFront();
        paneCRPAPPls.toFront();

        initCoiseBoxParamForRTC();
        gridPaneAllPhaseAPPlus.toFront();
        viewPointTableAPPls.toFront();

        initTableView();
    }

    @FXML
    void influenceAction(ActionEvent event) {
        if (event.getSource() == influenceBtn) {
            loadStage("/stend/view/influenceFrame.fxml", "Настройки теста влияния");
        }
    }

    @FXML
    void saveOrCancelAction(ActionEvent event) {
        if (event.getSource() == SaveBtn) {
            ArrayList<Commands> APPls = new ArrayList<>(testListForCollumAPPls);
            ArrayList<Commands> APMns = new ArrayList<>(testListForCollumAPMns);
            ArrayList<Commands> RPPls = new ArrayList<>(testListForCollumRPPls);
            ArrayList<Commands> RPMns = new ArrayList<>(testListForCollumRPMns);

            methodic.addCommandToList(0, APPls);
            methodic.addCommandToList(1, APMns);
            methodic.addCommandToList(2, RPPls);
            methodic.addCommandToList(3, RPMns);

            methodic.setSaveInflListForCollumAPPls(saveInflListForCollumAPPls);
            methodic.setSaveInflListForCollumAPMns(saveInflListForCollumAPMns);
            methodic.setSaveInflListForCollumRPPls(saveInflListForCollumRPPls);
            methodic.setSaveInflListForCollumRPMns(saveInflListForCollumRPMns);

            methodic.setSaveInfluenceUprocAPPls(saveInfluenceUprocAPPls);
            methodic.setSaveInfluenceUprocAPMns(saveInfluenceUprocAPMns);
            methodic.setSaveInfluenceUprocRPPls(saveInfluenceUprocRPPls);
            methodic.setSaveInfluenceUprocRPMns(saveInfluenceUprocRPMns);

            methodic.setSaveInfluenceFprocAPPls(saveInfluenceFprocAPPls);
            methodic.setSaveInfluenceFprocAPMns(saveInfluenceFprocAPMns);
            methodic.setSaveInfluenceFprocRPPls(saveInfluenceFprocRPPls);
            methodic.setSaveInfluenceFprocRPMns(saveInfluenceFprocRPMns);

            methodic.setSaveInfluenceInbUAPPls(saveInfluenceInbUAPPls);
            methodic.setSaveInfluenceInbUAPMns(saveInfluenceInbUAPMns);
            methodic.setSaveInfluenceInbURPPls(saveInfluenceInbURPPls);
            methodic.setSaveInfluenceInbURPMns(saveInfluenceInbURPMns);

            if (edit) {
                methodicsAddEditDeleteFrameController.setListsView(methodic);
            }else {
                methodicsAddEditDeleteFrameController.refreshMethodicList();
            }

            methodicsForTest.serializationMetodics();
        }

        if (event.getSource() == CancelBtn) {
            Stage thisScene = (Stage) CancelBtn.getScene().getWindow();
            thisScene.close();
        }
    }

    //Устанавливает значения Tgl Btn grid и добавления тестов ТХЧ и т.д.
    // в соответствующие значения
    @FXML
    void setPointFrameAction(ActionEvent event) {
        setGropToggleButton(event);
        gridPaneToFront(Objects.requireNonNull(getGridPane()));
    }

    @FXML
    void addSTAcRPrTCcOnst(ActionEvent event) {
        //Действие для добавления теста Самоход
        CreepCommand creepCommand;
        //---------------------------------------------------------------------------------------
        //Добаление самохода с параметрами пользователя AP+
        if (event.getSource() == addTglBtnCRPAPPls) {
            if (addTglBtnCRPAPPls.isSelected()) {
                creepCommand = new CreepCommand(false, 0);

                creepCommand.setPulseValue(Integer.parseInt(txtFieldCRPAmtImpAPPls.getText()));
                creepCommand.setUserTimeTest(txtFieldTimeCRPAPPls.getText());
                creepCommand.setVoltPer(Double.parseDouble(txtFieldCRPUProcAPPls.getText()));
                creepCommand.setName("Самоход AP+");

                txtFieldCRPAmtImpAPPls.setDisable(true);
                txtFieldTimeCRPAPPls.setDisable(true);
                txtFieldCRPUProcAPPls.setDisable(true);

                testListForCollumAPPls.add(creepCommand);
                CRPTogBtnAPPls.setSelected(true);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход AP+")) {
                            testListForCollumAPPls.remove(command);
                            break;
                        }
                    }
                }
                txtFieldCRPAmtImpAPPls.setDisable(false);
                txtFieldTimeCRPAPPls.setDisable(false);
                txtFieldCRPUProcAPPls.setDisable(false);

                if (addTglBtnCRPAPPlsGOST.isSelected()) {
                    CRPTogBtnAPPls.setSelected(true);
                } else CRPTogBtnAPPls.setSelected(false);
            }
        }

        //Добаление самохода с параметрами по ГОСТу AP+
        if (event.getSource() == addTglBtnCRPAPPlsGOST) {
            if (addTglBtnCRPAPPlsGOST.isSelected()) {
                creepCommand = new CreepCommand(true, 0);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);
                creepCommand.setName("Самоход AP+ ГОСТ");

                CRPTogBtnAPPls.setSelected(true);

                testListForCollumAPPls.add(creepCommand);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход AP+ ГОСТ")) {
                            testListForCollumAPPls.remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnCRPAPPls.isSelected()) {
                    CRPTogBtnAPPls.setSelected(true);
                } else CRPTogBtnAPPls.setSelected(false);
            }
        }

        //Добаление самохода с параметрами пользователя AP-
        if (event.getSource() == addTglBtnCRPAPMns) {
            if (addTglBtnCRPAPMns.isSelected()) {
                creepCommand = new CreepCommand(false, 1);

                creepCommand.setPulseValue(Integer.parseInt(txtFieldCRPAmtImpAPMns.getText()));
                creepCommand.setUserTimeTest(txtFieldTimeCRPAPMns.getText());
                creepCommand.setVoltPer(Double.parseDouble(txtFieldCRPUProcAPMns.getText()));
                creepCommand.setName("Самоход AP-");

                txtFieldCRPAmtImpAPMns.setDisable(true);
                txtFieldTimeCRPAPMns.setDisable(true);
                txtFieldCRPUProcAPMns.setDisable(true);

                CRPTogBtnAPMns.setSelected(true);
                testListForCollumAPMns.add(creepCommand);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход AP-")) {
                            testListForCollumAPMns.remove(command);
                            break;
                        }
                    }
                }
                txtFieldCRPAmtImpAPMns.setDisable(false);
                txtFieldTimeCRPAPMns.setDisable(false);
                txtFieldCRPUProcAPMns.setDisable(false);

                if (addTglBtnCRPAPMnsGOST.isSelected()) {
                    CRPTogBtnAPMns.setSelected(true);
                } else CRPTogBtnAPMns.setSelected(false);
            }
        }

        //Добаление самохода с параметрами по ГОСТу AP-
        if (event.getSource() == addTglBtnCRPAPMnsGOST) {
            if (addTglBtnCRPAPMnsGOST.isSelected()) {
                creepCommand = new CreepCommand(true, 1);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);
                creepCommand.setName("Самоход AP- ГОСТ");

                CRPTogBtnAPMns.setSelected(true);

                testListForCollumAPMns.add(creepCommand);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход AP- ГОСТ")) {
                            testListForCollumAPMns.remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnCRPAPMns.isSelected()) {
                    CRPTogBtnAPMns.setSelected(true);
                } else CRPTogBtnAPMns.setSelected(false);
            }
        }

        //Добаление самохода с параметрами пользователя RP+
        if (event.getSource() == addTglBtnCRPRPPls) {
            if (addTglBtnCRPRPPls.isSelected()) {
                creepCommand = new CreepCommand(false, 2);

                creepCommand.setPulseValue(Integer.parseInt(txtFieldCRPAmtImpRPPls.getText()));
                creepCommand.setUserTimeTest(txtFieldTimeCRPRPPls.getText());
                creepCommand.setVoltPer(Double.parseDouble(txtFieldCRPUProcRPPls.getText()));
                creepCommand.setName("Самоход RP+");

                txtFieldCRPAmtImpRPPls.setDisable(true);
                txtFieldTimeCRPRPPls.setDisable(true);
                txtFieldCRPUProcRPPls.setDisable(true);

                CRPTogBtnRPPls.setSelected(true);
                testListForCollumRPPls.add(creepCommand);
            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход RP+")) {
                            testListForCollumRPPls.remove(command);
                            break;
                        }
                    }
                }
                txtFieldCRPAmtImpRPPls.setDisable(false);
                txtFieldTimeCRPRPPls.setDisable(false);
                txtFieldCRPUProcRPPls.setDisable(false);

                if (addTglBtnCRPRPPlsGOST.isSelected()) {
                    CRPTogBtnRPPls.setSelected(true);
                } else CRPTogBtnRPPls.setSelected(false);
            }
        }

        //Добаление самохода с параметрами по ГОСТу RP+
        if (event.getSource() == addTglBtnCRPRPPlsGOST) {
            if (addTglBtnCRPRPPlsGOST.isSelected()) {
                creepCommand = new CreepCommand(true, 2);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);
                creepCommand.setName("Самоход RP+ ГОСТ");

                CRPTogBtnRPPls.setSelected(true);

                testListForCollumRPPls.add(creepCommand);
            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход RP+ ГОСТ")) {
                            testListForCollumRPPls.remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnCRPRPPls.isSelected()) {
                    CRPTogBtnRPPls.setSelected(true);
                } else CRPTogBtnRPPls.setSelected(false);
            }
        }

        //Добаление самохода с параметрами пользователя RP-
        if (event.getSource() == addTglBtnCRPRPMns) {
            if (addTglBtnCRPRPMns.isSelected()) {
                creepCommand = new CreepCommand(false, 3);

                creepCommand.setPulseValue(Integer.parseInt(txtFieldCRPAmtImpRPMns.getText()));
                creepCommand.setUserTimeTest(txtFieldTimeCRPRPMns.getText());
                creepCommand.setVoltPer(Double.parseDouble(txtFieldCRPUProcRPMns.getText()));
                creepCommand.setName("Самоход RP-");

                txtFieldCRPAmtImpRPMns.setDisable(true);
                txtFieldTimeCRPRPMns.setDisable(true);
                txtFieldCRPUProcRPMns.setDisable(true);

                CRPTogBtnRPMns.setSelected(true);
                testListForCollumRPMns.add(creepCommand);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход RP-")) {
                            testListForCollumRPMns.remove(command);
                            break;
                        }
                    }
                }
                txtFieldCRPAmtImpRPMns.setDisable(false);
                txtFieldTimeCRPRPMns.setDisable(false);
                txtFieldCRPUProcRPMns.setDisable(false);

                if (addTglBtnCRPRPMnsGOST.isSelected()) {
                    CRPTogBtnRPMns.setSelected(true);
                } else CRPTogBtnRPMns.setSelected(false);
            }
        }

        //Добаление самохода с параметрами по ГОСТу RP-
        if (event.getSource() == addTglBtnCRPRPMnsGOST) {
            if (addTglBtnCRPRPMnsGOST.isSelected()) {
                creepCommand = new CreepCommand(true, 3);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);
                creepCommand.setName("Самоход RP- ГОСТ");

                CRPTogBtnRPMns.setSelected(true);

                testListForCollumRPMns.add(creepCommand);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход RP- ГОСТ")) {
                            testListForCollumRPMns.remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnCRPRPMns.isSelected()) {
                    CRPTogBtnRPMns.setSelected(true);
                } else CRPTogBtnRPMns.setSelected(false);
            }
        }
        //------------------------------------------------------------------------------
        //Добаление теста на чувствительность с параметрами пользователя AP+
        StartCommand startCommand;
        if (event.getSource() == addTglBtnSTAAPPls) {
            if (addTglBtnSTAAPPls.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 0, 0, false);

                startCommand.setPulseValue(Integer.parseInt(txtFieldSTAAmtImpAPPls.getText()));
                startCommand.setUserTimeTest(txtFieldTimeSRAAPPls.getText());
                startCommand.setRatedCurr(Double.parseDouble(txtFieldSTAIProcAPPls.getText()));
                startCommand.setName("Чувствительность AP+");

                txtFieldSTAAmtImpAPPls.setDisable(true);
                txtFieldTimeSRAAPPls.setDisable(true);
                txtFieldSTAIProcAPPls.setDisable(true);

                STATogBtnAPPls.setSelected(true);
                testListForCollumAPPls.add(startCommand);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность AP+")) {
                            testListForCollumAPPls.remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpAPPls.setDisable(false);
                txtFieldTimeSRAAPPls.setDisable(false);
                txtFieldSTAIProcAPPls.setDisable(false);

                if (addTglBtnSTAAPPlsGOST.isSelected()) {
                    STATogBtnAPPls.setSelected(true);
                } else STATogBtnAPPls.setSelected(false);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу AP+
        if (event.getSource() == addTglBtnSTAAPPlsGOST) {
            if (addTglBtnSTAAPPlsGOST.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 0, 0, true);

                startCommand.setName("Чувствительность ГОСТ AP+");

                STATogBtnAPPls.setSelected(true);
                testListForCollumAPPls.add(startCommand);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность ГОСТ AP+")) {
                            testListForCollumAPPls.remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnSTAAPPls.isSelected()) {
                    STATogBtnAPPls.setSelected(true);
                } else STATogBtnAPPls.setSelected(false);
            }
        }

        //Добаление теста на чувствительность с параметрами пользователя AP-
        if (event.getSource() == addTglBtnSTAAPMns) {
            if (addTglBtnSTAAPMns.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 1, 1, false);

                startCommand.setPulseValue(Integer.parseInt(txtFieldSTAAmtImpAPMns.getText()));
                startCommand.setUserTimeTest(txtFieldTimeSRAAPMns.getText());
                startCommand.setRatedCurr(Double.parseDouble(txtFieldSTAIProcAPMns.getText()));
                startCommand.setName("Чувствительность AP-");

                txtFieldSTAAmtImpAPMns.setDisable(true);
                txtFieldTimeSRAAPMns.setDisable(true);
                txtFieldSTAIProcAPMns.setDisable(true);

                STATogBtnAPMns.setSelected(true);
                testListForCollumAPMns.add(startCommand);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность AP-")) {
                            testListForCollumAPMns.remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpAPMns.setDisable(false);
                txtFieldTimeSRAAPMns.setDisable(false);
                txtFieldSTAIProcAPMns.setDisable(false);

                if (addTglBtnSTAAPMnsGOST.isSelected()) {
                    STATogBtnAPMns.setSelected(true);
                } else STATogBtnAPMns.setSelected(false);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу AP-
        if (event.getSource() == addTglBtnSTAAPMnsGOST) {
            if (addTglBtnSTAAPMnsGOST.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 1, 1, true);

                startCommand.setName("Чувствительность ГОСТ AP-");

                STATogBtnAPMns.setSelected(true);
                testListForCollumAPMns.add(startCommand);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность ГОСТ AP-")) {
                            testListForCollumAPMns.remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnSTAAPMns.isSelected()) {
                    STATogBtnAPMns.setSelected(true);
                } else STATogBtnAPMns.setSelected(false);
            }
        }

        //Добаление теста на чувствительность с параметрами пользователя RP+
        if (event.getSource() == addTglBtnSTARPPls) {
            if (addTglBtnSTARPPls.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 0, 2, false);

                startCommand.setPulseValue(Integer.parseInt(txtFieldSTAAmtImpRPPls.getText()));
                startCommand.setUserTimeTest(txtFieldTimeSRARPPls.getText());
                startCommand.setRatedCurr(Double.parseDouble(txtFieldSTAIProcRPPls.getText()));
                startCommand.setName("Чувствительность RP+");

                txtFieldSTAAmtImpRPPls.setDisable(false);
                txtFieldTimeSRARPPls.setDisable(false);
                txtFieldSTAIProcRPPls.setDisable(false);

                STATogBtnRPPls.setSelected(true);
                testListForCollumRPPls.add(startCommand);

            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность RP+")) {
                            testListForCollumRPPls.remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpRPPls.setDisable(true);
                txtFieldTimeSRARPPls.setDisable(true);
                txtFieldSTAIProcRPPls.setDisable(true);

                if (addTglBtnSTARPPlsGOST.isSelected()) {
                    STATogBtnRPPls.setSelected(true);
                } else STATogBtnRPPls.setSelected(false);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу RP+
        if (event.getSource() == addTglBtnSTARPPlsGOST) {
            if (addTglBtnSTARPPlsGOST.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 0, 1, true);

                startCommand.setName("Чувствительность ГОСТ RP+");

                STATogBtnRPPls.setSelected(true);
                testListForCollumRPPls.add(startCommand);
            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность ГОСТ RP+")) {
                            testListForCollumRPPls.remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnSTARPPls.isSelected()) {
                    STATogBtnRPPls.setSelected(true);
                } else STATogBtnRPPls.setSelected(false);
            }
        }

        //Добаление теста на чувствительность с параметрами пользователя RP-
        if (event.getSource() == addTglBtnSTARPMns) {
            if (addTglBtnSTARPMns.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 1, 3, false);

                startCommand.setPulseValue(Integer.parseInt(txtFieldSTAAmtImpRPMns.getText()));
                startCommand.setUserTimeTest(txtFieldTimeSRARPMns.getText());
                startCommand.setRatedCurr(Double.parseDouble(txtFieldSTAIProcRPMns.getText()));
                startCommand.setName("Чувствительность RP-");

                txtFieldSTAAmtImpRPMns.setDisable(false);
                txtFieldTimeSRARPMns.setDisable(false);
                txtFieldSTAIProcRPMns.setDisable(false);

                STATogBtnRPMns.setSelected(true);
                testListForCollumRPMns.add(startCommand);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность RP-")) {
                            testListForCollumRPMns.remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpRPMns.setDisable(true);
                txtFieldTimeSRARPMns.setDisable(true);
                txtFieldSTAIProcRPMns.setDisable(true);

                if (addTglBtnSTARPMnsGOST.isSelected()) {
                    STATogBtnRPMns.setSelected(true);
                } else STATogBtnRPMns.setSelected(false);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу RP-
        if (event.getSource() == addTglBtnSTARPMnsGOST) {
            if (addTglBtnSTARPMnsGOST.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 0, 3, true);

                startCommand.setName("Чувствительность ГОСТ RP-");

                STATogBtnRPMns.setSelected(true);
                testListForCollumRPMns.add(startCommand);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность ГОСТ RP-")) {
                            testListForCollumRPMns.remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnSTARPMns.isSelected()) {
                    STATogBtnRPMns.setSelected(true);
                } else STATogBtnRPMns.setSelected(false);
            }
        }

        RTCCommand rtcCommand;
        //Добаление теста "точность хода часов" AP+
        String cbValue;
        if(event.getSource() == addTglBtnRTCAPPls) {
            if (addTglBtnRTCAPPls.isSelected()) {
                if (ChcBxRTCErrAPPls.getValue().equals("В ед. частоты")) {

                    rtcCommand = new RTCCommand(Integer.parseInt(txtFldRTCTimeMshAPPls.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshAPPls.getText()), 0, Double.parseDouble(txtFieldRngEAPPls.getText()));
                    cbValue = "В ед. частоты";
                } else {
                    rtcCommand = new RTCCommand(Integer.parseInt(txtFldRTCTimeMshAPPls.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshAPPls.getText()), 1, Double.parseDouble(txtFieldRngEAPPls.getText()));
                    cbValue = "Сутч. погрешность";
                }

                rtcCommand.setName("ТХЧ AP+");

                testListForCollumAPPls.add(rtcCommand);

                RTCTogBtnAPPls.setSelected(true);
                ChcBxRTCErrAPPls.getItems().clear();
                ChcBxRTCErrAPPls.getItems().addAll(cbValue);
                ChcBxRTCErrAPPls.getSelectionModel().select(0);

                txtFldRTCTimeMshAPPls.setDisable(true);
                txtFldRTCAmtMshAPPls.setDisable(true);
                txtFieldRngEAPPls.setDisable(true);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof RTCCommand) {
                        if (((RTCCommand) command).getName().equals("ТХЧ AP+")) {
                            testListForCollumAPPls.remove(command);
                            break;
                        }
                    }
                }

                RTCTogBtnAPPls.setSelected(false);
                ChcBxRTCErrAPPls.getItems().clear();
                ChcBxRTCErrAPPls.getItems().addAll("В ед. частоты", "Сутч. погрешность");
                ChcBxRTCErrAPPls.getSelectionModel().select(0);

                txtFldRTCTimeMshAPPls.setDisable(false);
                txtFldRTCAmtMshAPPls.setDisable(false);
                txtFieldRngEAPPls.setDisable(false);
            }
        }

        //Добаление теста "точность хода часов" AP-
        if(event.getSource() == addTglBtnRTCAPMns) {
            if (addTglBtnRTCAPMns.isSelected()) {
                if (ChcBxRTCErrAPMns.getValue().equals("В ед. частоты")) {
                    rtcCommand = new RTCCommand(Integer.parseInt(txtFldRTCTimeMshAPMns.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshAPMns.getText()), 0, Double.parseDouble(txtFieldRngEAPMns.getText()));
                    cbValue = "В ед. частоты";
                } else {
                    rtcCommand = new RTCCommand(Integer.parseInt(txtFldRTCTimeMshAPMns.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshAPMns.getText()), 1, Double.parseDouble(txtFieldRngEAPMns.getText()));
                    cbValue = "Сутч. погрешность";
                }

                rtcCommand.setName("ТХЧ AP-");

                RTCTogBtnAPMns.setSelected(true);
                ChcBxRTCErrAPMns.getItems().clear();
                ChcBxRTCErrAPMns.getItems().addAll(cbValue);
                ChcBxRTCErrAPMns.getSelectionModel().select(0);

                txtFldRTCTimeMshAPMns.setDisable(true);
                txtFldRTCAmtMshAPMns.setDisable(true);
                txtFieldRngEAPMns.setDisable(true);

                testListForCollumAPMns.add(rtcCommand);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof RTCCommand) {
                        if (((RTCCommand) command).getName().equals("ТХЧ AP-")) {
                            testListForCollumAPMns.remove(command);
                            break;
                        }
                    }
                }

                RTCTogBtnAPMns.setSelected(false);
                ChcBxRTCErrAPMns.getItems().clear();
                ChcBxRTCErrAPMns.getItems().addAll("В ед. частоты", "Сутч. погрешность");
                ChcBxRTCErrAPMns.getSelectionModel().select(0);

                txtFldRTCTimeMshAPMns.setDisable(false);
                txtFldRTCAmtMshAPMns.setDisable(false);
                txtFieldRngEAPMns.setDisable(false);
            }
        }

        //Добаление теста "точность хода часов" RP+
        if(event.getSource() == addTglBtnRTCRPPls) {
            if (addTglBtnRTCRPPls.isSelected()) {
                if (ChcBxRTCErrRPPls.getValue().equals("В ед. частоты")) {
                    rtcCommand = new RTCCommand(Integer.parseInt(txtFldRTCTimeMshRPPls.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshRPPls.getText()), 0, Double.parseDouble(txtFieldRngERPPls.getText()));
                    cbValue = "В ед. частоты";
                } else {
                    rtcCommand = new RTCCommand(Integer.parseInt(txtFldRTCTimeMshRPPls.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshRPPls.getText()), 1, Double.parseDouble(txtFieldRngERPPls.getText()));
                    cbValue = "Сутч. погрешность";
                }

                rtcCommand.setName("ТХЧ RP+");

                RTCTogBtnRPPls.setSelected(true);
                ChcBxRTCErrRPPls.getItems().clear();
                ChcBxRTCErrRPPls.getItems().addAll(cbValue);
                ChcBxRTCErrRPPls.getSelectionModel().select(0);

                txtFldRTCTimeMshRPPls.setDisable(true);
                txtFldRTCAmtMshRPPls.setDisable(true);
                txtFieldRngERPPls.setDisable(true);

                testListForCollumRPPls.add(rtcCommand);

            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof RTCCommand) {
                        if (((RTCCommand) command).getName().equals("ТХЧ RP+")) {
                            testListForCollumRPPls.remove(command);
                            break;
                        }
                    }
                }

                RTCTogBtnRPPls.setSelected(false);
                ChcBxRTCErrRPPls.getItems().clear();
                ChcBxRTCErrRPPls.getItems().addAll("В ед. частоты", "Сутч. погрешность");
                ChcBxRTCErrRPPls.getSelectionModel().select(0);

                txtFldRTCTimeMshRPPls.setDisable(false);
                txtFldRTCAmtMshRPPls.setDisable(false);
                txtFieldRngERPPls.setDisable(false);
            }
        }

        //Добаление теста "точность хода часов" RP-
        if(event.getSource() == addTglBtnRTCRPMns) {
            if (addTglBtnRTCRPMns.isSelected()) {
                if (ChcBxRTCErrRPMns.getValue().equals("В ед. частоты")) {
                    rtcCommand = new RTCCommand(Integer.parseInt(txtFldRTCTimeMshRPMns.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshRPMns.getText()), 0, Double.parseDouble(txtFieldRngERPMns.getText()));
                    cbValue = "В ед. частоты";
                } else {
                    rtcCommand = new RTCCommand(Integer.parseInt(txtFldRTCTimeMshRPMns.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshRPMns.getText()), 1, Double.parseDouble(txtFieldRngERPMns.getText()));
                    cbValue = "Сутч. погрешность";
                }

                rtcCommand.setName("ТХЧ RP-");

                RTCTogBtnRPMns.setSelected(true);
                ChcBxRTCErrRPMns.getItems().clear();
                ChcBxRTCErrRPMns.getItems().addAll(cbValue);
                ChcBxRTCErrRPMns.getSelectionModel().select(0);

                txtFldRTCTimeMshRPMns.setDisable(true);
                txtFldRTCAmtMshRPMns.setDisable(true);
                txtFieldRngERPMns.setDisable(true);

                testListForCollumRPMns.add(rtcCommand);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof RTCCommand) {
                        if (((RTCCommand) command).getName().equals("ТХЧ RP-")) {
                            testListForCollumRPMns.remove(command);
                            break;
                        }
                    }
                }

                RTCTogBtnRPMns.setSelected(false);
                ChcBxRTCErrRPMns.getItems().clear();
                ChcBxRTCErrRPMns.getItems().addAll("В ед. частоты", "Сутч. погрешность");
                ChcBxRTCErrRPMns.getSelectionModel().select(0);

                txtFldRTCTimeMshRPMns.setDisable(false);
                txtFldRTCAmtMshRPMns.setDisable(false);
                txtFieldRngERPMns.setDisable(false);
            }
        }
    }

    private void initCoiseBoxParamForRTC() {
        ChcBxRTCErrAPPls.getItems().addAll("В ед. частоты", "Сутч. погрешность");
        ChcBxRTCErrAPPls.getSelectionModel().select(0);

        ChcBxRTCErrAPMns.getItems().addAll("В ед. частоты", "Сутч. погрешность");
        ChcBxRTCErrAPMns.getSelectionModel().select(0);

        ChcBxRTCErrRPPls.getItems().addAll("В ед. частоты", "Сутч. погрешность");
        ChcBxRTCErrRPPls.getSelectionModel().select(0);

        ChcBxRTCErrRPMns.getItems().addAll("В ед. частоты", "Сутч. погрешность");
        ChcBxRTCErrRPMns.getSelectionModel().select(0);
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
            gridPaneAllPhaseAPPlus.setId("1;H;A;P");
            gridPanePhaseAAPPlus.setId("1;A;A;P");
            gridPanePhaseBAPPlus.setId("1;B;A;P");
            gridPanePhaseCAPPlus.setId("1;C;A;P");
            gridPaneAllPhaseAPMinus.setId("1;H;A;N");
            gridPanePhaseAAPMinus.setId("1;A;A;N");
            gridPanePhaseBAPMinus.setId("1;B;A;N");
            gridPanePhaseCAPMinus.setId("1;C;A;N");
            gridPaneAllPhaseRPPlus.setId("5;H;R;P");
            gridPanePhaseARPPlus.setId("5;A;R;P");
            gridPanePhaseBRPPlus.setId("5;B;R;P");
            gridPanePhaseCRPPlus.setId("5;C;R;P");
            gridPaneAllPhaseRPMinus.setId("5;H;R;N");
            gridPanePhaseARPMinus.setId("5;A;R;N");
            gridPanePhaseBRPMinus.setId("5;B;R;N");
            gridPanePhaseCRPMinus.setId("5;C;R;N");
        } else {
            gridPaneAllPhaseAPPlus.setId("0;H;A;P");
            gridPanePhaseAAPPlus.setId("0;A;A;P");
            gridPanePhaseBAPPlus.setId("0;B;A;P");
            gridPanePhaseCAPPlus.setId("0;C;A;P");
            gridPaneAllPhaseAPMinus.setId("0;H;A;N");
            gridPanePhaseAAPMinus.setId("0;A;A;N");
            gridPanePhaseBAPMinus.setId("0;B;A;N");
            gridPanePhaseCAPMinus.setId("0;C;A;N");
            gridPaneAllPhaseRPPlus.setId("7;H;R;P");
            gridPanePhaseARPPlus.setId("7;A;R;P");
            gridPanePhaseBRPPlus.setId("7;B;R;P");
            gridPanePhaseCRPPlus.setId("7;C;R;P");
            gridPaneAllPhaseRPMinus.setId("7;H;R;N");
            gridPanePhaseARPMinus.setId("7;A;R;N");
            gridPanePhaseBRPMinus.setId("7;B;R;N");
            gridPanePhaseCRPMinus.setId("7;C;R;N");
        }
        for (GridPane pane : gridPanesEnergyAndPhase) {
            setBoxAndLabelGridPane(pane);
        }
    }


    private void initGridPanesEnergyAndPhase() {
        gridPanesEnergyAndPhase = Arrays.asList(gridPaneAllPhaseAPPlus,
                gridPanePhaseAAPPlus,
                gridPanePhaseBAPPlus,
                gridPanePhaseCAPPlus,
                gridPaneAllPhaseAPMinus,
                gridPanePhaseAAPMinus,
                gridPanePhaseBAPMinus,
                gridPanePhaseCAPMinus,
                gridPaneAllPhaseRPPlus,
                gridPanePhaseARPPlus,
                gridPanePhaseBRPPlus,
                gridPanePhaseCRPPlus,
                gridPaneAllPhaseRPMinus,
                gridPanePhaseARPMinus,
                gridPanePhaseBRPMinus,
                gridPanePhaseCRPMinus);
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

                addCheckBoxInList(idCheckBox, checkBox);

                checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                    if (newVal) {
                        addTestPointInMethodic(idCheckBox, finalCheckBox.getId());
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

    //Добавляет CheckBox в нужний лист CB
    private void addCheckBoxInList(String[] idCheck, CheckBox checkBox) {

        //AP+
        if (idCheck[2].equals("A") && idCheck[3].equals("P")) {
            if (idCheck[1].equals("H")) {
                checkBoxesAllPhaseAPPlus.add(checkBox);
            }

            if (idCheck[1].equals("A")) {
                checkBoxesPhaseAAPPlus.add(checkBox);
            }

            if (idCheck[1].equals("B")) {
                checkBoxesPhaseBAPPlus.add(checkBox);
            }

            if (idCheck[1].equals("C")) {
                checkBoxesPhaseCAPPlus.add(checkBox);
            }
        }

        //AP-
        if (idCheck[2].equals("A") && idCheck[3].equals("N")) {

            if (idCheck[1].equals("H")) {
                checkBoxesAllPhaseAPMns.add(checkBox);
            }

            if (idCheck[1].equals("A")) {
                checkBoxesPhaseAAPMns.add(checkBox);
            }

            if (idCheck[1].equals("B")) {
                checkBoxesPhaseBAPMns.add(checkBox);
            }

            if (idCheck[1].equals("C")) {
                checkBoxesPhaseCAPMns.add(checkBox);
            }
        }

        //RP+
        if (idCheck[2].equals("R") && idCheck[3].equals("P")) {

            if (idCheck[1].equals("H")) {
                checkBoxesAllPhaseRPPlus.add(checkBox);
            }

            if (idCheck[1].equals("A")) {
                checkBoxesPhaseARPPlus.add(checkBox);
            }

            if (idCheck[1].equals("B")) {
                checkBoxesPhaseBRPPlus.add(checkBox);
            }

            if (idCheck[1].equals("C")) {
                checkBoxesPhaseCRPPlus.add(checkBox);
            }
        }

        //RP-
        if (idCheck[2].equals("R") && idCheck[3].equals("N")) {

            if (idCheck[1].equals("H")) {
                checkBoxesAllPhaseRPMns.add(checkBox);
            }

            if (idCheck[1].equals("A")) {
                checkBoxesPhaseARPMns.add(checkBox);
            }

            if (idCheck[1].equals("B")) {
                checkBoxesPhaseBRPMns.add(checkBox);
            }

            if (idCheck[1].equals("C")) {
                checkBoxesPhaseCRPMns.add(checkBox);
            }
        }
    }

    //Задаёт параметр true или false нужной точке
    public void addTestPointsOnGreedPane() {
        char[] testPointIdArr;

        if (!saveListForCollumAPPls.isEmpty()) {

            for (Commands command : saveListForCollumAPPls) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = ((ErrorCommand) command).getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);
                }

                if (command instanceof CreepCommand) {
                    if (!((CreepCommand) command).isGostTest()) {

                        txtFieldCRPUProcAPPls.setText(String.valueOf(((CreepCommand) command).getVoltPer()));
                        txtFieldTimeCRPAPPls.setText(((CreepCommand) command).getUserTimeTest());
                        txtFieldCRPAmtImpAPPls.setText(String.valueOf(((CreepCommand) command).getPulseValue()));

                        addTglBtnCRPAPPls.setSelected(true);

                        txtFieldCRPUProcAPPls.setDisable(true);
                        txtFieldTimeCRPAPPls.setDisable(true);
                        txtFieldCRPAmtImpAPPls.setDisable(true);
                    } else {
                        addTglBtnCRPAPPlsGOST.setSelected(true);
                    }
                    CRPTogBtnAPPls.setSelected(true);
                }

                if (command instanceof StartCommand) {
                    if (!((StartCommand) command).isGostTest()) {
                        txtFieldSTAIProcAPPls.setText(String.valueOf(((StartCommand) command).getRatedCurr()));
                        txtFieldTimeSRAAPPls.setText(((StartCommand) command).getUserTimeTest());
                        txtFieldSTAAmtImpAPPls.setText(String.valueOf(((StartCommand) command).getPulseValue()));

                        addTglBtnSTAAPPls.setSelected(true);

                        txtFieldSTAIProcAPPls.setDisable(true);
                        txtFieldTimeSRAAPPls.setDisable(true);
                        txtFieldSTAAmtImpAPPls.setDisable(true);
                    }else {
                        addTglBtnSTAAPPlsGOST.setSelected(true);
                    }
                    STATogBtnAPPls.setSelected(true);
                }

                if (command instanceof RTCCommand) {
                    if (((RTCCommand)command).getErrorType() == 0) {
                        ChcBxRTCErrAPPls.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrAPPls.setValue("Сутч. погрешность");
                    }

                    txtFieldRngEAPPls.setText(String.valueOf(((RTCCommand) command).getErrorForFalseTest()));
                    txtFldRTCAmtMshAPPls.setText(String.valueOf(((RTCCommand) command).getCountResult()));
                    txtFldRTCTimeMshAPPls.setText(String.valueOf(((RTCCommand)command).getPulseForRTC()));

                    ChcBxRTCErrAPPls.setDisable(true);

                    txtFieldRngEAPPls.setDisable(true);
                    txtFldRTCAmtMshAPPls.setDisable(true);
                    txtFldRTCTimeMshAPPls.setDisable(true);
                    addTglBtnRTCAPPls.setSelected(true);

                    RTCTogBtnAPPls.setSelected(true);
                }
            }
        }

        if (!saveListForCollumAPMns.isEmpty()) {

            for (Commands command : saveListForCollumAPMns) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = ((ErrorCommand) command).getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);
                }

                if (command instanceof CreepCommand) {
                    if (!((CreepCommand) command).isGostTest()) {

                        txtFieldCRPUProcAPMns.setText(String.valueOf(((CreepCommand) command).getVoltPer()));
                        txtFieldTimeCRPAPMns.setText(((CreepCommand) command).getUserTimeTest());
                        txtFieldCRPAmtImpAPMns.setText(String.valueOf(((CreepCommand) command).getPulseValue()));

                        addTglBtnCRPAPMns.setSelected(true);

                        txtFieldCRPUProcAPMns.setDisable(true);
                        txtFieldTimeCRPAPMns.setDisable(true);
                        txtFieldCRPAmtImpAPMns.setDisable(true);
                    } else {
                        addTglBtnCRPAPMnsGOST.setSelected(true);
                    }
                    CRPTogBtnAPMns.setSelected(true);
                }

                if (command instanceof StartCommand) {
                    if (!((StartCommand) command).isGostTest()) {
                        txtFieldSTAIProcAPMns.setText(String.valueOf(((StartCommand) command).getRatedCurr()));
                        txtFieldTimeSRAAPMns.setText(((StartCommand) command).getUserTimeTest());
                        txtFieldSTAAmtImpAPMns.setText(String.valueOf(((StartCommand) command).getPulseValue()));

                        addTglBtnSTAAPMns.setSelected(true);

                        txtFieldSTAIProcAPMns.setDisable(true);
                        txtFieldTimeSRAAPMns.setDisable(true);
                        txtFieldSTAAmtImpAPMns.setDisable(true);
                    }else {
                        addTglBtnSTAAPMnsGOST.setSelected(true);
                    }
                    STATogBtnAPMns.setSelected(true);
                }

                if (command instanceof RTCCommand) {
                    if (((RTCCommand)command).getErrorType() == 0) {
                        ChcBxRTCErrAPMns.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrAPMns.setValue("Сутч. погрешность");
                    }

                    txtFieldRngEAPMns.setText(String.valueOf(((RTCCommand) command).getErrorForFalseTest()));
                    txtFldRTCAmtMshAPMns.setText(String.valueOf(((RTCCommand) command).getCountResult()));
                    txtFldRTCTimeMshAPMns.setText(String.valueOf(((RTCCommand)command).getPulseForRTC()));

                    ChcBxRTCErrAPMns.setDisable(true);

                    txtFieldRngEAPMns.setDisable(true);
                    txtFldRTCAmtMshAPMns.setDisable(true);
                    txtFldRTCTimeMshAPMns.setDisable(true);
                    addTglBtnRTCAPMns.setSelected(true);

                    RTCTogBtnAPMns.setSelected(true);
                }
            }
        }

        if (!saveListForCollumRPPls.isEmpty()) {

            for (Commands command : saveListForCollumRPPls) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = ((ErrorCommand) command).getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);
                }

                if (command instanceof CreepCommand) {
                    if (!((CreepCommand) command).isGostTest()) {

                        txtFieldCRPUProcRPPls.setText(String.valueOf(((CreepCommand) command).getVoltPer()));
                        txtFieldTimeCRPRPPls.setText(((CreepCommand) command).getUserTimeTest());
                        txtFieldCRPAmtImpRPPls.setText(String.valueOf(((CreepCommand) command).getPulseValue()));

                        addTglBtnCRPRPPls.setSelected(true);

                        txtFieldCRPUProcRPPls.setDisable(true);
                        txtFieldTimeCRPRPPls.setDisable(true);
                        txtFieldCRPAmtImpRPPls.setDisable(true);
                    } else {
                        addTglBtnCRPRPPlsGOST.setSelected(true);
                    }
                    CRPTogBtnRPPls.setSelected(true);
                }

                if (command instanceof StartCommand) {
                    if (!((StartCommand) command).isGostTest()) {
                        txtFieldSTAIProcRPPls.setText(String.valueOf(((StartCommand) command).getRatedCurr()));
                        txtFieldTimeSRARPPls.setText(((StartCommand) command).getUserTimeTest());
                        txtFieldSTAAmtImpRPPls.setText(String.valueOf(((StartCommand) command).getPulseValue()));

                        addTglBtnSTARPPls.setSelected(true);

                        txtFieldSTAIProcRPPls.setDisable(true);
                        txtFieldTimeSRARPPls.setDisable(true);
                        txtFieldSTAAmtImpRPPls.setDisable(true);
                    }else {
                        addTglBtnSTARPPlsGOST.setSelected(true);
                    }
                    STATogBtnRPPls.setSelected(true);
                }

                if (command instanceof RTCCommand) {
                    if (((RTCCommand)command).getErrorType() == 0) {
                        ChcBxRTCErrRPPls.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrRPPls.setValue("Сутч. погрешность");
                    }

                    txtFieldRngERPPls.setText(String.valueOf(((RTCCommand) command).getErrorForFalseTest()));
                    txtFldRTCAmtMshRPPls.setText(String.valueOf(((RTCCommand) command).getCountResult()));
                    txtFldRTCTimeMshRPPls.setText(String.valueOf(((RTCCommand)command).getPulseForRTC()));

                    ChcBxRTCErrRPPls.setDisable(true);

                    txtFieldRngERPPls.setDisable(true);
                    txtFldRTCAmtMshRPPls.setDisable(true);
                    txtFldRTCTimeMshRPPls.setDisable(true);
                    addTglBtnRTCRPPls.setSelected(true);

                    RTCTogBtnRPPls.setSelected(true);
                }
            }
        }

        if (!saveListForCollumRPMns.isEmpty()) {

            for (Commands command : saveListForCollumRPMns) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = ((ErrorCommand) command).getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);
                }

                if (command instanceof CreepCommand) {
                    if (!((CreepCommand) command).isGostTest()) {

                        txtFieldCRPUProcRPMns.setText(String.valueOf(((CreepCommand) command).getVoltPer()));
                        txtFieldTimeCRPRPMns.setText(((CreepCommand) command).getUserTimeTest());
                        txtFieldCRPAmtImpRPMns.setText(String.valueOf(((CreepCommand) command).getPulseValue()));

                        addTglBtnCRPRPMns.setSelected(true);

                        txtFieldCRPUProcRPMns.setDisable(true);
                        txtFieldTimeCRPRPMns.setDisable(true);
                        txtFieldCRPAmtImpRPMns.setDisable(true);
                    } else {
                        addTglBtnCRPRPMnsGOST.setSelected(true);
                    }
                    CRPTogBtnRPMns.setSelected(true);
                }

                if (command instanceof StartCommand) {
                    if (!((StartCommand) command).isGostTest()) {
                        txtFieldSTAIProcRPMns.setText(String.valueOf(((StartCommand) command).getRatedCurr()));
                        txtFieldTimeSRARPMns.setText(((StartCommand) command).getUserTimeTest());
                        txtFieldSTAAmtImpRPMns.setText(String.valueOf(((StartCommand) command).getPulseValue()));

                        addTglBtnSTARPMns.setSelected(true);

                        txtFieldSTAIProcRPMns.setDisable(true);
                        txtFieldTimeSRARPMns.setDisable(true);
                        txtFieldSTAAmtImpRPMns.setDisable(true);
                    }else {
                        addTglBtnSTARPMnsGOST.setSelected(true);
                    }
                    STATogBtnRPMns.setSelected(true);
                }

                if (command instanceof RTCCommand) {
                    if (((RTCCommand)command).getErrorType() == 0) {
                        ChcBxRTCErrRPMns.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrRPMns.setValue("Сутч. погрешность");
                    }

                    txtFieldRngERPMns.setText(String.valueOf(((RTCCommand) command).getErrorForFalseTest()));
                    txtFldRTCAmtMshRPMns.setText(String.valueOf(((RTCCommand) command).getCountResult()));
                    txtFldRTCTimeMshRPMns.setText(String.valueOf(((RTCCommand)command).getPulseForRTC()));

                    ChcBxRTCErrRPMns.setDisable(true);

                    txtFieldRngERPMns.setDisable(true);
                    txtFldRTCAmtMshRPMns.setDisable(true);
                    txtFldRTCTimeMshRPMns.setDisable(true);
                    addTglBtnRTCRPMns.setSelected(true);

                    RTCTogBtnRPMns.setSelected(true);
                }
            }
        }
        testListForCollumAPPls.clear();
        testListForCollumAPMns.clear();
        testListForCollumRPPls.clear();
        testListForCollumRPMns.clear();

        testListForCollumAPPls.addAll(saveListForCollumAPPls);
        testListForCollumAPMns.addAll(saveListForCollumAPMns);
        testListForCollumRPPls.addAll(saveListForCollumRPPls);
        testListForCollumRPMns.addAll(saveListForCollumRPMns);

    }

    //Находит нужный CheckBox и задаёт значение
    private void setTrueOrFalseOnCheckBox(char[] testPointIdArr, Commands commands) {
        //AP+
        if (testPointIdArr[4] == 'A' && testPointIdArr[6] == 'P') {

            if (testPointIdArr[2] == 'H') {

                for (CheckBox checkBox : checkBoxesAllPhaseAPPlus) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'A') {

                for (CheckBox checkBox : checkBoxesPhaseAAPPlus) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'B') {

                for (CheckBox checkBox : checkBoxesPhaseBAPPlus) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'C') {

                for (CheckBox checkBox : checkBoxesPhaseCAPPlus) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }
        }

        //AP-
        if (testPointIdArr[4] == 'A' && testPointIdArr[6] == 'N') {

            if (testPointIdArr[2] == 'H') {

                for (CheckBox checkBox : checkBoxesAllPhaseAPMns) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'A') {

                for (CheckBox checkBox : checkBoxesPhaseAAPMns) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'B') {

                for (CheckBox checkBox : checkBoxesPhaseBAPMns) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'C') {

                for (CheckBox checkBox : checkBoxesPhaseCAPMns) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }
        }

        //RP+
        if (testPointIdArr[4] == 'R' && testPointIdArr[6] == 'P') {

            if (testPointIdArr[2] == 'H') {

                for (CheckBox checkBox : checkBoxesAllPhaseRPPlus) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'A') {

                for (CheckBox checkBox : checkBoxesPhaseARPPlus) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'B') {

                for (CheckBox checkBox : checkBoxesPhaseBRPPlus) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'C') {

                for (CheckBox checkBox : checkBoxesPhaseCRPPlus) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }
        }


        //RP-
        if (testPointIdArr[4] == 'R' && testPointIdArr[6] == 'N') {

            if (testPointIdArr[2] == 'H') {

                for (CheckBox checkBox : checkBoxesAllPhaseRPMns) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'A') {

                for (CheckBox checkBox : checkBoxesPhaseARPMns) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'B') {

                for (CheckBox checkBox : checkBoxesPhaseBRPMns) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
            }

            if (testPointIdArr[2] == 'C') {

                for (CheckBox checkBox : checkBoxesPhaseCRPMns) {
                    if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                        checkBox.setSelected(true);
                    }
                }
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
        if (event.getSource() == allPhaseBtn) {
            allPhaseBtn.setSelected(true);
            APhaseBtn.setSelected(false);
            BPhaseBtn.setSelected(false);
            CPhaseBtn.setSelected(false);
        }
        if (event.getSource() == APhaseBtn) {
            APhaseBtn.setSelected(true);
            allPhaseBtn.setSelected(false);
            BPhaseBtn.setSelected(false);
            CPhaseBtn.setSelected(false);
        }
        if (event.getSource() == BPhaseBtn) {
            BPhaseBtn.setSelected(true);
            allPhaseBtn.setSelected(false);
            APhaseBtn.setSelected(false);
            CPhaseBtn.setSelected(false);
        }
        if (event.getSource() == CPhaseBtn) {
            CPhaseBtn.setSelected(true);
            allPhaseBtn.setSelected(false);
            APhaseBtn.setSelected(false);
            BPhaseBtn.setSelected(false);
        }

        //Tg btns "энергия и направление" отвечающие за сетку точек и добавление тестов Сам., ТХЧ, Чувств., Конст.
        if (event.getSource() == APPlus || event.getSource() == APPlusCRPSTA) {
            setDefPosBtn();
            viewPointTableAPPls.toFront();
            APPlsPane.toFront();
            paneCRPAPPls.toFront();

            APPlus.setSelected(true);
            APMinus.setSelected(false);
            RPPlus.setSelected(false);
            RPMinus.setSelected(false);

            APPlusCRPSTA.setSelected(true);
            APMinusCRPSTA.setSelected(false);
            RPPlusCRPSTA.setSelected(false);
            RPMinusCRPSTA.setSelected(false);
        }

        if (event.getSource() == APMinus || event.getSource() == APMinusCRPSTA) {
            setDefPosBtn();
            viewPointTableAPMns.toFront();
            APMnsPane.toFront();
            paneCRPAPMns.toFront();

            APMinus.setSelected(true);
            APPlus.setSelected(false);
            RPPlus.setSelected(false);
            RPMinus.setSelected(false);

            APPlusCRPSTA.setSelected(false);
            APMinusCRPSTA.setSelected(true);
            RPPlusCRPSTA.setSelected(false);
            RPMinusCRPSTA.setSelected(false);
        }
        if (event.getSource() == RPPlus || event.getSource() == RPPlusCRPSTA) {
            setDefPosBtn();
            viewPointTableRPPls.toFront();
            RPPlsPane.toFront();
            paneCRPRPPls.toFront();

            RPPlus.setSelected(true);
            APPlus.setSelected(false);
            APMinus.setSelected(false);
            RPMinus.setSelected(false);

            APPlusCRPSTA.setSelected(false);
            APMinusCRPSTA.setSelected(false);
            RPPlusCRPSTA.setSelected(true);
            RPMinusCRPSTA.setSelected(false);
        }
        if (event.getSource() == RPMinus || event.getSource() == RPMinusCRPSTA) {
            setDefPosBtn();
            viewPointTableRPMns.toFront();
            RPMnsPane.toFront();
            paneCRPRPMns.toFront();

            RPMinus.setSelected(true);
            RPPlus.setSelected(false);
            APPlus.setSelected(false);
            APMinus.setSelected(false);

            APPlusCRPSTA.setSelected(false);
            APMinusCRPSTA.setSelected(false);
            RPPlusCRPSTA.setSelected(false);
            RPMinusCRPSTA.setSelected(true);
        }

        //Переключение окон внутри фрейма "направление" между вкладками Сам. ТХЧ и т.д.
        //AP+
        if (event.getSource() == CRPTogBtnAPPls) {
            paneCRPAPPls.toFront();
            if (addTglBtnCRPAPPls.isSelected() || addTglBtnCRPAPPlsGOST.isSelected()) {
                CRPTogBtnAPPls.setSelected(true);
            }else CRPTogBtnAPPls.setSelected(false);
        }

        if (event.getSource() == STATogBtnAPPls) {
            paneSTAAPPls.toFront();
            if (addTglBtnSTAAPPls.isSelected() || addTglBtnSTAAPPlsGOST.isSelected()) {
                STATogBtnAPPls.setSelected(true);
            }else STATogBtnAPPls.setSelected(false);
        }

        if (event.getSource() == RTCTogBtnAPPls) {
            paneRTCAPPls.toFront();
            RTCTogBtnAPPls.setSelected(addTglBtnRTCAPPls.isSelected());
        }

        if (event.getSource() == ConstTogBtnAPPls) {
            paneConstAPPls.toFront();
            ConstTogBtnAPPls.setSelected(addTglBtnConstAPPls.isSelected());
        }

        //AP-
        if (event.getSource() == CRPTogBtnAPMns) {
            paneCRPAPMns.toFront();
            if (addTglBtnCRPAPMns.isSelected() || addTglBtnCRPAPMnsGOST.isSelected()) {
                CRPTogBtnAPMns.setSelected(true);
            } else CRPTogBtnAPMns.setSelected(false);
        }

        if (event.getSource() == STATogBtnAPMns) {
            paneSTAAPMns.toFront();
            if (addTglBtnSTAAPMns.isSelected() || addTglBtnSTAAPMnsGOST.isSelected()) {
                STATogBtnAPMns.setSelected(true);
            } else STATogBtnAPMns.setSelected(false);
        }

        if (event.getSource() == RTCTogBtnAPMns) {
            RTCTogBtnAPMns.setSelected(addTglBtnRTCAPMns.isSelected());
            paneRTCAPMns.toFront();
        }

        if (event.getSource() == ConstTogBtnAPMns) {
            ConstTogBtnAPMns.setSelected(addTglBtnConstAPMns.isSelected());
            paneConstAPMns.toFront();
        }

        //RP+
        if (event.getSource() == CRPTogBtnRPPls) {
            CRPTogBtnRPPls.setSelected(addTglBtnCRPRPPls.isSelected() || addTglBtnCRPRPPlsGOST.isSelected());
            paneCRPRPPls.toFront();
        }

        if (event.getSource() == STATogBtnRPPls) {
            STATogBtnRPPls.setSelected(addTglBtnSTARPPls.isSelected() || addTglBtnSTARPPlsGOST.isSelected());
            paneSTARPPls.toFront();
        }

        if (event.getSource() == RTCTogBtnRPPls) {
            RTCTogBtnRPPls.setSelected(addTglBtnRTCRPPls.isSelected());
            paneRTCRPPls.toFront();
        }

        if (event.getSource() == ConstTogBtnRPPls) {
            ConstTogBtnRPPls.setSelected(addTglBtnConstRPPls.isSelected());
            paneConstRPPls.toFront();
        }

        //RP-
        if (event.getSource() == CRPTogBtnRPMns) {
            CRPTogBtnRPMns.setSelected(addTglBtnCRPRPMns.isSelected() || addTglBtnCRPRPMnsGOST.isSelected());
            paneCRPRPMns.toFront();
        }

        if (event.getSource() == STATogBtnRPMns) {
            STATogBtnRPMns.setSelected(addTglBtnSTARPMns.isSelected() || addTglBtnSTARPMnsGOST.isSelected());
            paneSTARPMns.toFront();
        }

        if (event.getSource() == RTCTogBtnRPMns) {
            RTCTogBtnRPMns.setSelected(addTglBtnRTCRPMns.isSelected());
            paneRTCRPMns.toFront();
        }

        if (event.getSource() == ConstTogBtnRPMns) {
            ConstTogBtnRPMns.setSelected(addTglBtnConstRPMns.isSelected());
            paneConstRPMns.toFront();
        }
    }

    //При переключении вкладки Мощности и Направления устанавливает положение в "Все фазы"
    private void setDefPosBtn() {
        allPhaseBtn.setSelected(true);
        APhaseBtn.setSelected(false);
        BPhaseBtn.setSelected(false);
        CPhaseBtn.setSelected(false);
    }

    private void gridPaneToFront(GridPane pane) {
        pane.toFront();
    }

    //Выдаёт нужный GridPane в зависимости от нажатой кнопки
    private GridPane getGridPane() {
        if (allPhaseBtn.isSelected()) {
            if (APPlus.isSelected()) return gridPaneAllPhaseAPPlus;
            if (APMinus.isSelected()) return gridPaneAllPhaseAPMinus;
            if (RPPlus.isSelected()) return gridPaneAllPhaseRPPlus;
            if (RPMinus.isSelected()) return gridPaneAllPhaseRPMinus;
        }

        if (APhaseBtn.isSelected()) {
            if (APPlus.isSelected()) return gridPanePhaseAAPPlus;
            if (APMinus.isSelected()) return gridPanePhaseAAPMinus;
            if (RPPlus.isSelected()) return gridPanePhaseARPPlus;
            if (RPMinus.isSelected()) return gridPanePhaseARPMinus;
        }

        if (BPhaseBtn.isSelected()) {
            if (APPlus.isSelected()) return gridPanePhaseBAPPlus;
            if (APMinus.isSelected()) return gridPanePhaseBAPMinus;
            if (RPPlus.isSelected()) return gridPanePhaseBRPPlus;
            if (RPMinus.isSelected()) return gridPanePhaseBRPMinus;
        }

        if (CPhaseBtn.isSelected()) {
            if (APPlus.isSelected()) return gridPanePhaseCAPPlus;
            if (APMinus.isSelected()) return gridPanePhaseCAPMinus;
            if (RPPlus.isSelected()) return gridPanePhaseCRPPlus;
            if (RPMinus.isSelected()) return gridPanePhaseCRPMinus;
        }
        return null;
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
    private void addTestPointInMethodic(String[] dirCurFactor, String testPoint) {
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
            testListForCollumAPPls.add(new ErrorCommand(testPoint, phase, current, 0, percent, iABC, powerFactor, 0));
        }

        if (energyType.equals("A") && currentDirection.equals("N")) {
            testListForCollumAPMns.add(new ErrorCommand(testPoint, phase, current, 1, percent, iABC, powerFactor, 1));
        }

        if (energyType.equals("R") && currentDirection.equals("P")) {
            testListForCollumRPPls.add(new ErrorCommand(testPoint, phase, current, 0, percent, iABC, powerFactor, 2));
        }

        if (energyType.equals("R") && currentDirection.equals("N")) {
            testListForCollumRPMns.add(new ErrorCommand(testPoint, phase, current, 1, percent, iABC, powerFactor, 3));
        }
    }

    private void deleteTestPointInMethodic(String [] point) {
        ErrorCommand errorCommand;
        String str;

        if (point[2].equals("A") && point[3].equals("P")) {

            if (point[1].equals("H")) {
                str = point[5] + "; " + point[4];
            }else str = point[1] + ": " + point[5] + "; " + point[4];

            for (Commands current : testListForCollumAPPls) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getName().equals(str)) {
                        testListForCollumAPPls.remove(current);
                        break;
                    }
                }
            }
        }

        if (point[2].equals("A") && point[3].equals("N")) {

            if (point[1].equals("H")) {
                str = point[5] + "; " + point[4];
            }else str = point[1] + ": " + point[5] + "; " + point[4];

            for (Commands current : testListForCollumAPMns) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getName().equals(str)) {
                        testListForCollumAPMns.remove(current);
                        break;
                    }
                }
            }
        }

        if (point[2].equals("R") && point[3].equals("P")) {

            if (point[1].equals("H")) {
                str = point[5] + "; " + point[4];
            }else str = point[1] + ": " + point[5] + "; " + point[4];

            for (Commands current : testListForCollumRPPls) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getName().equals(str)) {
                        testListForCollumRPPls.remove(current);
                        break;
                    }
                }
            }
        }

        if (point[2].equals("R") && point[3].equals("N")) {

            if (point[1].equals("H")) {
                str = point[5] + "; " + point[4];
            }else str = point[1] + ": " + point[5] + "; " + point[4];

            for (Commands current : testListForCollumRPMns) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getName().equals(str)) {
                        testListForCollumRPMns.remove(current);
                        break;
                    }
                }
            }
        }
    }

    private void initInfluenceFrame() {
        influenceFrame.setSaveInflListForCollumAPPls(saveInflListForCollumAPPls);
        influenceFrame.setSaveInflListForCollumAPMns(saveInflListForCollumAPMns);
        influenceFrame.setSaveInflListForCollumRPPls(saveInflListForCollumRPPls);
        influenceFrame.setSaveInflListForCollumRPMns(saveInflListForCollumRPMns);

        influenceFrame.setSaveInfluenceUprocAPPls(saveInfluenceUprocAPPls);
        influenceFrame.setSaveInfluenceFprocAPPls(saveInfluenceFprocAPPls);
        influenceFrame.setSaveInfluenceInbUAPPls(saveInfluenceInbUAPPls);

        influenceFrame.setSaveInfluenceUprocAPMns(saveInfluenceUprocAPMns);
        influenceFrame.setSaveInfluenceFprocAPMns(saveInfluenceFprocAPMns);
        influenceFrame.setSaveInfluenceInbUAPMns(saveInfluenceInbUAPMns);

        influenceFrame.setSaveInfluenceUprocRPPls(saveInfluenceUprocRPPls);
        influenceFrame.setSaveInfluenceFprocRPPls(saveInfluenceFprocRPPls);
        influenceFrame.setSaveInfluenceInbURPPls(saveInfluenceInbURPPls);

        influenceFrame.setSaveInfluenceUprocRPMns(saveInfluenceUprocRPMns);
        influenceFrame.setSaveInfluenceFprocRPMns(saveInfluenceFprocRPMns);
        influenceFrame.setSaveInfluenceInbURPMns(saveInfluenceInbURPMns);

        influenceFrame.initOfAdeedTestPoints();
    }


    //Проверияет нет ли данных с полученной методики и если у неё есть данные, то выгружает её в это окно
    //Необходимо для команды Редактирования методики
    public void initEditsMetodic() {
        saveListForCollumAPPls.addAll(methodic.getCommandsMap().get(0));
        saveListForCollumAPMns.addAll(methodic.getCommandsMap().get(1));
        saveListForCollumRPPls.addAll(methodic.getCommandsMap().get(2));
        saveListForCollumRPMns.addAll(methodic.getCommandsMap().get(3));

        saveInflListForCollumAPPls.addAll(methodic.getSaveInflListForCollumAPPls());
        saveInflListForCollumAPMns.addAll(methodic.getSaveInflListForCollumAPMns());
        saveInflListForCollumRPPls.addAll(methodic.getSaveInflListForCollumRPPls());
        saveInflListForCollumRPMns.addAll(methodic.getSaveInflListForCollumRPMns());

        saveInfluenceUprocAPPls = methodic.getSaveInfluenceUprocAPPls();
        saveInfluenceUprocAPMns = methodic.getSaveInfluenceUprocAPMns();
        saveInfluenceUprocRPPls = methodic.getSaveInfluenceUprocRPPls();
        saveInfluenceUprocRPMns = methodic.getSaveInfluenceUprocRPMns();

        saveInfluenceFprocAPPls = methodic.getSaveInfluenceFprocAPPls();
        saveInfluenceFprocAPMns = methodic.getSaveInfluenceFprocAPMns();
        saveInfluenceFprocRPPls = methodic.getSaveInfluenceFprocRPPls();
        saveInfluenceFprocRPMns = methodic.getSaveInfluenceFprocRPMns();

        saveInfluenceInbUAPPls = methodic.getSaveInfluenceInbUAPPls();
        saveInfluenceInbUAPMns = methodic.getSaveInfluenceInbUAPMns();
        saveInfluenceInbURPPls = methodic.getSaveInfluenceInbURPPls();
        saveInfluenceInbURPMns = methodic.getSaveInfluenceInbURPMns();
    }

    private void loadStage(String fxml, String stageName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxml));
            fxmlLoader.load();
            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle(stageName);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            influenceFrame = fxmlLoader.getController();
            influenceFrame.setAddEditFrameController(this);
            initInfluenceFrame();

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
