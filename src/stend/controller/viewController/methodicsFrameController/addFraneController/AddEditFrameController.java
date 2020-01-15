package stend.controller.viewController.methodicsFrameController.addFraneController;

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
import javafx.stage.Stage;
import stend.controller.Commands.*;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.helper.ConsoleHelper;
import stend.model.Methodic;

public class AddEditFrameController {

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
                creepCommand = new CreepCommand(stendDLLCommands, false, 0);

                creepCommand.setPulseValue(Integer.parseInt(txtFieldCRPAmtImpAPPls.getText()));
                creepCommand.setUserTimeTest(txtFieldTimeCRPAPPls.getText());
                creepCommand.setVoltPer(Double.parseDouble(txtFieldCRPUProcAPPls.getText()));
                creepCommand.setName("Самоход AP+");

                txtFieldCRPAmtImpAPPls.setEditable(false);
                txtFieldTimeCRPAPPls.setEditable(false);
                txtFieldCRPUProcAPPls.setEditable(false);

                CRPTogBtnAPPls.setSelected(true);
                Methodic.commandsMap.get(0).add(creepCommand);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(0)) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход AP+")) {
                            Methodic.commandsMap.get(0).remove(command);
                            break;
                        }
                    }
                }
                txtFieldCRPAmtImpAPPls.setEditable(true);
                txtFieldTimeCRPAPPls.setEditable(true);
                txtFieldCRPUProcAPPls.setEditable(true);

                if (addTglBtnCRPAPPlsGOST.isSelected()) {
                    CRPTogBtnAPPls.setSelected(true);
                } else CRPTogBtnAPPls.setSelected(false);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            }
        }

        //Добаление самохода с параметрами по ГОСТу AP+
        if (event.getSource() == addTglBtnCRPAPPlsGOST) {
            if (addTglBtnCRPAPPlsGOST.isSelected()) {
                creepCommand = new CreepCommand(stendDLLCommands, true, 0);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);
                creepCommand.setName("Самоход AP+ ГОСТ");

                CRPTogBtnAPPls.setSelected(true);

                Methodic.commandsMap.get(0).add(creepCommand);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(0)) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход AP+ ГОСТ")) {
                            Methodic.commandsMap.get(0).remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnCRPAPPls.isSelected()) {
                    CRPTogBtnAPPls.setSelected(true);
                } else CRPTogBtnAPPls.setSelected(false);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            }
        }

        //Добаление самохода с параметрами пользователя AP-
        if (event.getSource() == addTglBtnCRPAPMns) {
            if (addTglBtnCRPAPMns.isSelected()) {
                creepCommand = new CreepCommand(stendDLLCommands, false, 1);

                creepCommand.setPulseValue(Integer.parseInt(txtFieldCRPAmtImpAPMns.getText()));
                creepCommand.setUserTimeTest(txtFieldTimeCRPAPMns.getText());
                creepCommand.setVoltPer(Double.parseDouble(txtFieldCRPUProcAPMns.getText()));
                creepCommand.setName("Самоход AP-");

                txtFieldCRPAmtImpAPMns.setEditable(false);
                txtFieldTimeCRPAPMns.setEditable(false);
                txtFieldCRPUProcAPMns.setEditable(false);

                CRPTogBtnAPMns.setSelected(true);
                Methodic.commandsMap.get(1).add(creepCommand);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(1)) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход AP-")) {
                            Methodic.commandsMap.get(1).remove(command);
                            break;
                        }
                    }
                }
                txtFieldCRPAmtImpAPMns.setEditable(true);
                txtFieldTimeCRPAPMns.setEditable(true);
                txtFieldCRPUProcAPMns.setEditable(true);

                if (addTglBtnCRPAPMnsGOST.isSelected()) {
                    CRPTogBtnAPMns.setSelected(true);
                } else CRPTogBtnAPMns.setSelected(false);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            }
        }

        //Добаление самохода с параметрами по ГОСТу AP-
        if (event.getSource() == addTglBtnCRPAPMnsGOST) {
            if (addTglBtnCRPAPMnsGOST.isSelected()) {
                creepCommand = new CreepCommand(stendDLLCommands, true, 1);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);
                creepCommand.setName("Самоход AP- ГОСТ");

                CRPTogBtnAPMns.setSelected(true);

                Methodic.commandsMap.get(1).add(creepCommand);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(1)) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход AP- ГОСТ")) {
                            Methodic.commandsMap.get(1).remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnCRPAPMns.isSelected()) {
                    CRPTogBtnAPMns.setSelected(true);
                } else CRPTogBtnAPMns.setSelected(false);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            }
        }

        //Добаление самохода с параметрами пользователя RP+
        if (event.getSource() == addTglBtnCRPRPPls) {
            if (addTglBtnCRPRPPls.isSelected()) {
                creepCommand = new CreepCommand(stendDLLCommands, false, 2);

                creepCommand.setPulseValue(Integer.parseInt(txtFieldCRPAmtImpRPPls.getText()));
                creepCommand.setUserTimeTest(txtFieldTimeCRPRPPls.getText());
                creepCommand.setVoltPer(Double.parseDouble(txtFieldCRPUProcRPPls.getText()));
                creepCommand.setName("Самоход RP+");

                txtFieldCRPAmtImpRPPls.setEditable(false);
                txtFieldTimeCRPRPPls.setEditable(false);
                txtFieldCRPUProcRPPls.setEditable(false);

                CRPTogBtnRPPls.setSelected(true);
                Methodic.commandsMap.get(2).add(creepCommand);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(2)) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход RP+")) {
                            Methodic.commandsMap.get(2).remove(command);
                            break;
                        }
                    }
                }
                txtFieldCRPAmtImpRPPls.setEditable(true);
                txtFieldTimeCRPRPPls.setEditable(true);
                txtFieldCRPUProcRPPls.setEditable(true);

                if (addTglBtnCRPRPPlsGOST.isSelected()) {
                    CRPTogBtnRPPls.setSelected(true);
                } else CRPTogBtnRPPls.setSelected(false);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            }
        }

        //Добаление самохода с параметрами по ГОСТу RP+
        if (event.getSource() == addTglBtnCRPRPPlsGOST) {
            if (addTglBtnCRPRPPlsGOST.isSelected()) {
                creepCommand = new CreepCommand(stendDLLCommands, true, 2);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);
                creepCommand.setName("Самоход RP+ ГОСТ");

                CRPTogBtnRPPls.setSelected(true);

                Methodic.commandsMap.get(2).add(creepCommand);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(2)) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход RP+ ГОСТ")) {
                            Methodic.commandsMap.get(2).remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnCRPRPPls.isSelected()) {
                    CRPTogBtnRPPls.setSelected(true);
                } else CRPTogBtnRPPls.setSelected(false);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            }
        }

        //Добаление самохода с параметрами пользователя RP-
        if (event.getSource() == addTglBtnCRPRPMns) {
            if (addTglBtnCRPRPMns.isSelected()) {
                creepCommand = new CreepCommand(stendDLLCommands, false, 3);

                creepCommand.setPulseValue(Integer.parseInt(txtFieldCRPAmtImpRPMns.getText()));
                creepCommand.setUserTimeTest(txtFieldTimeCRPRPMns.getText());
                creepCommand.setVoltPer(Double.parseDouble(txtFieldCRPUProcRPMns.getText()));
                creepCommand.setName("Самоход RP-");

                txtFieldCRPAmtImpRPMns.setEditable(false);
                txtFieldTimeCRPRPMns.setEditable(false);
                txtFieldCRPUProcRPMns.setEditable(false);

                CRPTogBtnRPMns.setSelected(true);
                Methodic.commandsMap.get(3).add(creepCommand);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(3)) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход RP-")) {
                            Methodic.commandsMap.get(3).remove(command);
                            break;
                        }
                    }
                }
                txtFieldCRPAmtImpRPMns.setEditable(true);
                txtFieldTimeCRPRPMns.setEditable(true);
                txtFieldCRPUProcRPMns.setEditable(true);

                if (addTglBtnCRPRPMnsGOST.isSelected()) {
                    CRPTogBtnRPMns.setSelected(true);
                } else CRPTogBtnRPMns.setSelected(false);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            }
        }

        //Добаление самохода с параметрами по ГОСТу RP-
        if (event.getSource() == addTglBtnCRPRPMnsGOST) {
            if (addTglBtnCRPRPMnsGOST.isSelected()) {
                creepCommand = new CreepCommand(stendDLLCommands, true, 3);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);
                creepCommand.setName("Самоход RP- ГОСТ");

                CRPTogBtnRPMns.setSelected(true);

                Methodic.commandsMap.get(3).add(creepCommand);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(3)) {
                    if (command instanceof CreepCommand) {
                        if (((CreepCommand) command).getName().equals("Самоход RP- ГОСТ")) {
                            Methodic.commandsMap.get(3).remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnCRPRPMns.isSelected()) {
                    CRPTogBtnRPMns.setSelected(true);
                } else CRPTogBtnRPMns.setSelected(false);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
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

                txtFieldSTAAmtImpAPPls.setEditable(false);
                txtFieldTimeSRAAPPls.setEditable(false);
                txtFieldSTAIProcAPPls.setEditable(false);

                STATogBtnAPPls.setSelected(true);
                Methodic.commandsMap.get(0).add(startCommand);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(0)) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность AP+")) {
                            Methodic.commandsMap.get(0).remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpAPPls.setEditable(true);
                txtFieldTimeSRAAPPls.setEditable(true);
                txtFieldSTAIProcAPPls.setEditable(true);

                if (addTglBtnSTAAPPlsGOST.isSelected()) {
                    STATogBtnAPPls.setSelected(true);
                } else STATogBtnAPPls.setSelected(false);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу AP+
        if (event.getSource() == addTglBtnSTAAPPlsGOST) {
            if (addTglBtnSTAAPPlsGOST.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 0, 0, true);

                startCommand.setName("Чувствительность ГОСТ AP+");

                STATogBtnAPPls.setSelected(true);
                Methodic.commandsMap.get(0).add(startCommand);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(0)) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность ГОСТ AP+")) {
                            Methodic.commandsMap.get(0).remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnSTAAPPls.isSelected()) {
                    STATogBtnAPPls.setSelected(true);
                } else STATogBtnAPPls.setSelected(false);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
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

                txtFieldSTAAmtImpAPMns.setEditable(false);
                txtFieldTimeSRAAPMns.setEditable(false);
                txtFieldSTAIProcAPMns.setEditable(false);

                STATogBtnAPMns.setSelected(true);
                Methodic.commandsMap.get(1).add(startCommand);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(1)) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность AP-")) {
                            Methodic.commandsMap.get(1).remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpAPMns.setEditable(true);
                txtFieldTimeSRAAPMns.setEditable(true);
                txtFieldSTAIProcAPMns.setEditable(true);

                if (addTglBtnSTAAPMnsGOST.isSelected()) {
                    STATogBtnAPMns.setSelected(true);
                } else STATogBtnAPMns.setSelected(false);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу AP-
        if (event.getSource() == addTglBtnSTAAPMnsGOST) {
            if (addTglBtnSTAAPMnsGOST.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 1, 1, true);

                startCommand.setName("Чувствительность ГОСТ AP-");

                STATogBtnAPMns.setSelected(true);
                Methodic.commandsMap.get(1).add(startCommand);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(1)) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность ГОСТ AP-")) {
                            Methodic.commandsMap.get(1).remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnSTAAPMns.isSelected()) {
                    STATogBtnAPMns.setSelected(true);
                } else STATogBtnAPMns.setSelected(false);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
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

                txtFieldSTAAmtImpRPPls.setEditable(false);
                txtFieldTimeSRARPPls.setEditable(false);
                txtFieldSTAIProcRPPls.setEditable(false);

                STATogBtnRPPls.setSelected(true);
                Methodic.commandsMap.get(2).add(startCommand);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(2)) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность RP+")) {
                            Methodic.commandsMap.get(2).remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpRPPls.setEditable(true);
                txtFieldTimeSRARPPls.setEditable(true);
                txtFieldSTAIProcRPPls.setEditable(true);

                if (addTglBtnSTARPPlsGOST.isSelected()) {
                    STATogBtnRPPls.setSelected(true);
                } else STATogBtnRPPls.setSelected(false);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу RP+
        if (event.getSource() == addTglBtnSTARPPlsGOST) {
            if (addTglBtnSTARPPlsGOST.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 0, 1, true);

                startCommand.setName("Чувствительность ГОСТ RP+");

                STATogBtnRPPls.setSelected(true);
                Methodic.commandsMap.get(2).add(startCommand);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(2)) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность ГОСТ RP+")) {
                            Methodic.commandsMap.get(2).remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnSTARPPls.isSelected()) {
                    STATogBtnRPPls.setSelected(true);
                } else STATogBtnRPPls.setSelected(false);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
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

                txtFieldSTAAmtImpRPMns.setEditable(false);
                txtFieldTimeSRARPMns.setEditable(false);
                txtFieldSTAIProcRPMns.setEditable(false);

                STATogBtnRPMns.setSelected(true);
                Methodic.commandsMap.get(3).add(startCommand);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(3)) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность RP-")) {
                            Methodic.commandsMap.get(3).remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpRPMns.setEditable(true);
                txtFieldTimeSRARPMns.setEditable(true);
                txtFieldSTAIProcRPMns.setEditable(true);

                if (addTglBtnSTARPMnsGOST.isSelected()) {
                    STATogBtnRPMns.setSelected(true);
                } else STATogBtnRPMns.setSelected(false);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу RP-
        if (event.getSource() == addTglBtnSTARPMnsGOST) {
            if (addTglBtnSTARPMnsGOST.isSelected()) {
                startCommand = new StartCommand(stendDLLCommands, 0, 3, true);

                startCommand.setName("Чувствительность ГОСТ RP-");

                STATogBtnRPMns.setSelected(true);
                Methodic.commandsMap.get(3).add(startCommand);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(3)) {
                    if (command instanceof StartCommand) {
                        if (((StartCommand) command).getName().equals("Чувствительность ГОСТ RP-")) {
                            Methodic.commandsMap.get(3).remove(command);
                            break;
                        }
                    }
                }

                if (addTglBtnSTARPMns.isSelected()) {
                    STATogBtnRPMns.setSelected(true);
                } else STATogBtnRPMns.setSelected(false);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            }
        }

        RTCCommand rtcCommand;
        //Добаление теста "точность хода часов" AP+
        String cbValue;
        if(event.getSource() == addTglBtnRTCAPPls) {
            if (addTglBtnRTCAPPls.isSelected()) {
                if (ChcBxRTCErrAPPls.getValue().equals("В ед. частоты")) {
                    rtcCommand = new RTCCommand(stendDLLCommands, Integer.parseInt(txtFldRTCTimeMshAPPls.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshAPPls.getText()), 0, Double.parseDouble(txtFieldRngEAPPls.getText()));
                    cbValue = "В ед. частоты";
                } else {
                    rtcCommand = new RTCCommand(stendDLLCommands, Integer.parseInt(txtFldRTCTimeMshAPPls.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshAPPls.getText()), 1, Double.parseDouble(txtFieldRngEAPPls.getText()));
                    cbValue = "Сутч. погрешность";
                }

                rtcCommand.setName("ТХЧ AP+");

                Methodic.commandsMap.get(0).add(rtcCommand);

                RTCTogBtnAPPls.setSelected(true);
                ChcBxRTCErrAPPls.getItems().clear();
                ChcBxRTCErrAPPls.getItems().addAll(cbValue);
                ChcBxRTCErrAPPls.getSelectionModel().select(0);
                txtFldRTCTimeMshAPPls.setEditable(false);
                txtFldRTCAmtMshAPPls.setEditable(false);
                txtFieldRngEAPPls.setEditable(false);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(0)) {
                    if (command instanceof RTCCommand) {
                        if (((RTCCommand) command).getName().equals("ТХЧ AP+")) {
                            Methodic.commandsMap.get(0).remove(command);
                            break;
                        }
                    }
                }

                RTCTogBtnAPPls.setSelected(false);
                ChcBxRTCErrAPPls.getItems().clear();
                ChcBxRTCErrAPPls.getItems().addAll("В ед. частоты", "Сутч. погрешность");
                ChcBxRTCErrAPPls.getSelectionModel().select(0);
                txtFldRTCTimeMshAPPls.setEditable(true);
                txtFldRTCAmtMshAPPls.setEditable(true);
                txtFieldRngEAPPls.setEditable(true);

                testListForCollumAPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(0));
                viewPointTableAPPls.setItems(testListForCollumAPPls);
            }
        }

        //Добаление теста "точность хода часов" AP-
        if(event.getSource() == addTglBtnRTCAPMns) {
            if (addTglBtnRTCAPMns.isSelected()) {
                if (ChcBxRTCErrAPMns.getValue().equals("В ед. частоты")) {
                    rtcCommand = new RTCCommand(stendDLLCommands, Integer.parseInt(txtFldRTCTimeMshAPMns.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshAPMns.getText()), 0, Double.parseDouble(txtFieldRngEAPMns.getText()));
                    cbValue = "В ед. частоты";
                } else {
                    rtcCommand = new RTCCommand(stendDLLCommands, Integer.parseInt(txtFldRTCTimeMshAPMns.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshAPMns.getText()), 1, Double.parseDouble(txtFieldRngEAPMns.getText()));
                    cbValue = "Сутч. погрешность";
                }

                rtcCommand.setName("ТХЧ AP-");

                RTCTogBtnAPMns.setSelected(true);
                ChcBxRTCErrAPMns.getItems().clear();
                ChcBxRTCErrAPMns.getItems().addAll(cbValue);
                ChcBxRTCErrAPMns.getSelectionModel().select(0);
                txtFldRTCTimeMshAPMns.setEditable(false);
                txtFldRTCAmtMshAPMns.setEditable(false);
                txtFieldRngEAPMns.setEditable(false);

                Methodic.commandsMap.get(1).add(rtcCommand);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(1)) {
                    if (command instanceof RTCCommand) {
                        if (((RTCCommand) command).getName().equals("ТХЧ AP-")) {
                            Methodic.commandsMap.get(1).remove(command);
                            break;
                        }
                    }
                }

                RTCTogBtnAPMns.setSelected(false);
                ChcBxRTCErrAPMns.getItems().clear();
                ChcBxRTCErrAPMns.getItems().addAll("В ед. частоты", "Сутч. погрешность");
                ChcBxRTCErrAPMns.getSelectionModel().select(0);
                txtFldRTCTimeMshAPMns.setEditable(true);
                txtFldRTCAmtMshAPMns.setEditable(true);
                txtFieldRngEAPMns.setEditable(true);

                testListForCollumAPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(1));
                viewPointTableAPMns.setItems(testListForCollumAPMns);
            }
        }

        //Добаление теста "точность хода часов" RP+
        if(event.getSource() == addTglBtnRTCRPPls) {
            if (addTglBtnRTCRPPls.isSelected()) {
                if (ChcBxRTCErrRPPls.getValue().equals("В ед. частоты")) {
                    rtcCommand = new RTCCommand(stendDLLCommands, Integer.parseInt(txtFldRTCTimeMshRPPls.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshRPPls.getText()), 0, Double.parseDouble(txtFieldRngERPPls.getText()));
                    cbValue = "В ед. частоты";
                } else {
                    rtcCommand = new RTCCommand(stendDLLCommands, Integer.parseInt(txtFldRTCTimeMshRPPls.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshRPPls.getText()), 1, Double.parseDouble(txtFieldRngERPPls.getText()));
                    cbValue = "Сутч. погрешность";
                }

                rtcCommand.setName("ТХЧ RP+");

                RTCTogBtnRPPls.setSelected(true);
                ChcBxRTCErrRPPls.getItems().clear();
                ChcBxRTCErrRPPls.getItems().addAll(cbValue);
                ChcBxRTCErrRPPls.getSelectionModel().select(0);
                txtFldRTCTimeMshRPPls.setEditable(false);
                txtFldRTCAmtMshRPPls.setEditable(false);
                txtFieldRngERPPls.setEditable(false);

                Methodic.commandsMap.get(2).add(rtcCommand);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            } else {
                for (Commands command : Methodic.commandsMap.get(2)) {
                    if (command instanceof RTCCommand) {
                        if (((RTCCommand) command).getName().equals("ТХЧ RP+")) {
                            Methodic.commandsMap.get(2).remove(command);
                            break;
                        }
                    }
                }

                RTCTogBtnRPPls.setSelected(false);
                ChcBxRTCErrRPPls.getItems().clear();
                ChcBxRTCErrRPPls.getItems().addAll("В ед. частоты", "Сутч. погрешность");
                ChcBxRTCErrRPPls.getSelectionModel().select(0);
                txtFldRTCTimeMshRPPls.setEditable(true);
                txtFldRTCAmtMshRPPls.setEditable(true);
                txtFieldRngERPPls.setEditable(true);

                testListForCollumRPPls = FXCollections.observableArrayList(Methodic.commandsMap.get(2));
                viewPointTableRPPls.setItems(testListForCollumRPPls);
            }
        }

        //Добаление теста "точность хода часов" RP-
        if(event.getSource() == addTglBtnRTCRPMns) {
            if (addTglBtnRTCRPMns.isSelected()) {
                if (ChcBxRTCErrRPMns.getValue().equals("В ед. частоты")) {
                    rtcCommand = new RTCCommand(stendDLLCommands, Integer.parseInt(txtFldRTCTimeMshRPMns.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshRPMns.getText()), 0, Double.parseDouble(txtFieldRngERPMns.getText()));
                    cbValue = "В ед. частоты";
                } else {
                    rtcCommand = new RTCCommand(stendDLLCommands, Integer.parseInt(txtFldRTCTimeMshRPMns.getText()), 1.000000,
                            Integer.parseInt(txtFldRTCAmtMshRPMns.getText()), 1, Double.parseDouble(txtFieldRngERPMns.getText()));
                    cbValue = "Сутч. погрешность";
                }

                rtcCommand.setName("ТХЧ RP-");

                RTCTogBtnRPMns.setSelected(true);
                ChcBxRTCErrRPMns.getItems().clear();
                ChcBxRTCErrRPMns.getItems().addAll(cbValue);
                ChcBxRTCErrRPMns.getSelectionModel().select(0);
                txtFldRTCTimeMshRPMns.setEditable(false);
                txtFldRTCAmtMshRPMns.setEditable(false);
                txtFieldRngERPMns.setEditable(false);

                Methodic.commandsMap.get(3).add(rtcCommand);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            } else {
                for (Commands command : Methodic.commandsMap.get(3)) {
                    if (command instanceof RTCCommand) {
                        if (((RTCCommand) command).getName().equals("ТХЧ RP-")) {
                            Methodic.commandsMap.get(3).remove(command);
                            break;
                        }
                    }
                }

                RTCTogBtnRPMns.setSelected(false);
                ChcBxRTCErrRPMns.getItems().clear();
                ChcBxRTCErrRPMns.getItems().addAll("В ед. частоты", "Сутч. погрешность");
                ChcBxRTCErrRPMns.getSelectionModel().select(0);
                txtFldRTCTimeMshRPMns.setEditable(true);
                txtFldRTCAmtMshRPMns.setEditable(true);
                txtFieldRngERPMns.setEditable(true);

                testListForCollumRPMns = FXCollections.observableArrayList(Methodic.commandsMap.get(3));
                viewPointTableRPMns.setItems(testListForCollumRPMns);
            }
        }
    }

    public void initCoiseBoxParamForRTC() {
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
                        System.out.println("Кнопка зажата " + finalCheckBox.getId() + "\n" + "Количество точек: " + Methodic.commandsMap.get(0).size());

                    } else {
                        deleteTestPointInMethodic(idCheckBox);
                        System.out.println("Кнопка разжата " + finalCheckBox.getId());
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
