package org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.controller.Commands.ErrorCommand;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;

import java.util.*;

public class InfluencePointsOnePhaseStendFrame {

    private MethodicForOnePhaseStend methodicForOnePhaseStend;

    //Значения коэффициента мощности
    private List<String> powerFactor;

    //Значения выставленного тока
    private List<String> current;

    //Список GridPane для выставления точек поверки
    private List<GridPane> gridPanesEnergyAndPhase;

    private ScrollPane mainScrollPane = new ScrollPane();

    private StackPane stackPaneForGridPane = new StackPane();

    private ScrollPane scrollPaneForCurrent = new ScrollPane();
    private ScrollPane scrollPaneForPowerFactor = new ScrollPane();
    private GridPane gridPaneForCurrent = new GridPane();
    private GridPane gridPaneForPowerFactor = new GridPane();

    private Pane fillSquare = new Pane();

    //Листы с точками тестирования до сохранения
    private ObservableList<Commands> inflListForCollumAPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> inflListForCollumAPMns = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> inflListForCollumRPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> inflListForCollumRPMns = FXCollections.observableArrayList(new ArrayList<>());

    //листы с точками после сохранения
    private List<Commands> saveInflListForCollumAPPls = new ArrayList<>();
    private List<Commands> saveInflListForCollumAPMns = new ArrayList<>();
    private List<Commands> saveInflListForCollumRPPls = new ArrayList<>();
    private List<Commands> saveInflListForCollumRPMns = new ArrayList<>();

//=====================================================================
    //Поля необходимые для добавления точек испытания на влияние
    //До сохранения
    //AP+
    private float[] influenceUprocAllPhaseAPPls = new float[0];
    private float[] influenceUprocPhaseAAPPls = new float[0];
    private float[] influenceUprocPhaseBAPPls = new float[0];

    private float[] influenceFprocAllPhaseAPPls = new float[0];
    private float[] influenceFprocPhaseAAPPls = new float[0];
    private float[] influenceFprocPhaseBAPPls = new float[0];

    //AP-
    private float[] influenceUprocAllPhaseAPMns = new float[0];
    private float[] influenceUprocPhaseAAPMns = new float[0];
    private float[] influenceUprocPhaseBAPMns = new float[0];

    private float[] influenceFprocAllPhaseAPMns = new float[0];
    private float[] influenceFprocPhaseAAPMns = new float[0];
    private float[] influenceFprocPhaseBAPMns = new float[0];

    //RP+
    private float[] influenceUprocAllPhaseRPPls = new float[0];
    private float[] influenceUprocPhaseARPPls = new float[0];
    private float[] influenceUprocPhaseBRPPls = new float[0];

    private float[] influenceFprocAllPhaseRPPls = new float[0];
    private float[] influenceFprocPhaseARPPls = new float[0];
    private float[] influenceFprocPhaseBRPPls = new float[0];

    //RP-
    private float[] influenceUprocAllPhaseRPMns = new float[0];
    private float[] influenceUprocPhaseARPMns = new float[0];
    private float[] influenceUprocPhaseBRPMns = new float[0];

    private float[] influenceFprocAllPhaseRPMns = new float[0];
    private float[] influenceFprocPhaseARPMns = new float[0];
    private float[] influenceFprocPhaseBRPMns = new float[0];

    //======================================================================
    @FXML
    private TableView<Commands> viewPointTableAPPls;

    @FXML
    private TableColumn<Commands, String> loadCurrTabColAPPls;

    @FXML
    private TableColumn<Commands, String> eMaxTabColAPPls;

    @FXML
    private TableColumn<Commands, String> eMinTabColAPPls;

    @FXML
    private TableColumn<Commands, String> amountImplTabColAPPls;

    @FXML
    private TableColumn<Commands, String> amountMeasTabColAPPls;

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

    @FXML
    private TableColumn<Commands, String> amountMeasTabColAPMns;

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

    @FXML
    private TableColumn<Commands, String> amountMeasTabColRPPls;

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

    @FXML
    private TableColumn<Commands, String> amountMeasTabColRPMns;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Button saveBtn;

    @FXML
    private ToggleButton APPlusCRPSTA;

    @FXML
    private ToggleButton APMinusCRPSTA;

    @FXML
    private ToggleButton RPPlusCRPSTA;

    @FXML
    private ToggleButton RPMinusCRPSTA;

    @FXML
    private Pane mainAllPhasePane;

    @FXML
    private ToggleButton selectInfUAllPhaseTgl;

    @FXML
    private ToggleButton selectInfFAllPhaseTgl;

    @FXML
    private Pane InflUAllPhasePane;

    @FXML
    private Label directLabAllPhaseU;

    @FXML
    private ToggleButton addTglBtnInfUAllPhase;

    @FXML
    private TextField txtFieldInfUAllPhase;

    @FXML
    private Pane InflFAllPhasePane;

    @FXML
    private Label directLabAllPhaseF;

    @FXML
    private ToggleButton addTglBtnInfFAllPhase;

    @FXML
    private TextField txtFieldInfFAllPhase;

    @FXML
    private Pane mainPhaseAPane;

    @FXML
    private ToggleButton selectInfUPhaseATgl;

    @FXML
    private ToggleButton selectInfFPhaseATgl;

    @FXML
    private Pane InflUPhaseAPane;

    @FXML
    private Label directLabPhaseAU;

    @FXML
    private ToggleButton addTglBtnInfUPhaseA;

    @FXML
    private TextField txtFieldInfUPhaseA;

    @FXML
    private Pane InflFPhaseAPane;

    @FXML
    private Label directLabPhaseAF;

    @FXML
    private ToggleButton addTglBtnInfFPhaseA;

    @FXML
    private TextField txtFieldInfFPhaseA;

    @FXML
    private Pane mainPhaseBPane;

    @FXML
    private ToggleButton selectInfUPhaseBTgl;

    @FXML
    private ToggleButton selectInfFPhaseBTgl;

    @FXML
    private Pane InflUPhaseBPane;

    @FXML
    private Label directLabPhaseBU;

    @FXML
    private ToggleButton addTglBtnInfUPhaseB;

    @FXML
    private TextField txtFieldInfUPhaseB;

    @FXML
    private Pane InflFPhaseBPane;

    @FXML
    private Label directLabPhaseBF;

    @FXML
    private ToggleButton addTglBtnInfFPhaseB;

    @FXML
    private TextField txtFieldInfFPhaseB;

    @FXML
    private Pane mainPhaseCPane;

    @FXML
    private ToggleButton selectInfUPhaseCTgl;

    @FXML
    private ToggleButton selectInfFPhaseCTgl;

    @FXML
    private Pane InflUPhaseCPane;

    @FXML
    private Label directLabPhaseCU;

    @FXML
    private ToggleButton addTglBtnInfUPhaseC;

    @FXML
    private TextField txtFieldInfUPhaseC;

    @FXML
    private Pane InflFPhaseCPane;

    @FXML
    private Label directLabPhaseCF;

    @FXML
    private ToggleButton addTglBtnInfFPhaseC;

    @FXML
    private TextField txtFieldInfFPhaseC;

    @FXML
    private VBox vBoxForPhaseTgBtn;

    @FXML
    private ToggleButton allPhaseBtn;

    @FXML
    private ToggleButton phaseABtn;

    @FXML
    private ToggleButton phaseBBtn;

    @FXML
    private HBox hBoxForDirectionTgBtn;

    @FXML
    private ToggleButton APPlus;

    @FXML
    private ToggleButton APMinus;

    @FXML
    private ToggleButton RPPlus;

    @FXML
    private ToggleButton RPMinus;

    //Данный блок отвечает за сетку выбора точки.
    //Влияние напряжения
    private GridPane gridPaneUAllPhase = new GridPane();
    private GridPane gridPaneUPhaseA = new GridPane();
    private GridPane gridPaneUPhaseB = new GridPane();

    //Влияние частоты
    private GridPane gridPaneFAllPhase = new GridPane();
    private GridPane gridPaneFPhaseA = new GridPane();
    private GridPane gridPaneFPhaseB = new GridPane();

    @FXML
    void initialize() {
        current = Arrays.asList(ConsoleHelper.properties.getProperty("currentForMethodicPane").split(", "));
        powerFactor = Arrays.asList(ConsoleHelper.properties.getProperty("powerFactorForMetodicPane").split(", "));

        initMainScrollPaneAndScrollPaneCurrentScrollPanePowerFactor();
    }

    public void myInitInflFrame(MethodicForOnePhaseStend methodicForOnePhaseStend) {
        this.methodicForOnePhaseStend = methodicForOnePhaseStend;

        intitInflFields(this.methodicForOnePhaseStend);

        APPlus.fire();

        initTableView();
    }

    @FXML
    void addInfluenceTests(ActionEvent event) {
        //Влияние напряжения все фазы
        if (event.getSource() == addTglBtnInfUAllPhase) {
            if (addTglBtnInfUAllPhase.isSelected()) {

                //Если выбрано AP+
                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUAllPhase.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocAllPhaseAPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocAllPhaseAPPls[i] = inflFloat.get(i);
                        }

                        gridPaneUAllPhase.setDisable(false);
                        txtFieldInfUAllPhase.setDisable(true);
                    } catch (NumberFormatException e) {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                ConsoleHelper.infoException("Неверные данные");
                            }
                        });

                        addTglBtnInfUAllPhase.setSelected(false);
                    }

                    //Если выбрано AP-
                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUAllPhase.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocAllPhaseAPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocAllPhaseAPMns[i] = inflFloat.get(i);
                        }

                        gridPaneUAllPhase.setDisable(false);
                        txtFieldInfUAllPhase.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUAllPhase.setSelected(false);
                    }

                    //Если выбрано RP+
                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUAllPhase.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocAllPhaseRPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocAllPhaseRPPls[i] = inflFloat.get(i);
                        }

                        gridPaneUAllPhase.setDisable(false);
                        txtFieldInfUAllPhase.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUAllPhase.setSelected(false);
                    }

                    //Если выбрано RP-
                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUAllPhase.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocAllPhaseRPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocAllPhaseRPMns[i] = inflFloat.get(i);
                        }

                        gridPaneUAllPhase.setDisable(false);
                        txtFieldInfUAllPhase.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUAllPhase.setSelected(false);
                    }
                }
            } else {
                gridPaneUAllPhase.setDisable(true);
                txtFieldInfUAllPhase.setDisable(false);

                CheckBox checkBox;
                for (Node node : gridPaneUAllPhase.getChildren()) {
                    try {
                        checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            checkBox.setSelected(false);
                        }
                    } catch (ClassCastException ignore) {}
                }

                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {

                    influenceUprocAllPhaseAPPls = new float[0];

                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {

                    influenceUprocAllPhaseAPMns = new float[0];

                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {

                    influenceUprocAllPhaseRPPls = new float[0];

                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {

                    influenceUprocAllPhaseRPMns = new float[0];
                }
            }
        }

            //Влияние напряжения Фаза А
        if (event.getSource() == addTglBtnInfUPhaseA) {
            if (addTglBtnInfUPhaseA.isSelected()) {

                //Если выбрано AP+
                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUPhaseA.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocPhaseAAPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocPhaseAAPPls[i] = inflFloat.get(i);
                        }

                        gridPaneUPhaseA.setDisable(false);
                        txtFieldInfUPhaseA.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUPhaseA.setSelected(false);
                    }

                    //Если выбрано AP-
                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUPhaseA.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocPhaseAAPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocPhaseAAPMns[i] = inflFloat.get(i);
                        }

                        gridPaneUPhaseA.setDisable(false);
                        txtFieldInfUPhaseA.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUPhaseA.setSelected(false);
                    }

                    //Если выбрано RP+
                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUPhaseA.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocPhaseARPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocPhaseARPPls[i] = inflFloat.get(i);
                        }

                        gridPaneUPhaseA.setDisable(false);
                        txtFieldInfUPhaseA.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUPhaseA.setSelected(false);
                    }

                    //Если выбрано RP-
                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUPhaseA.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocPhaseARPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocPhaseARPMns[i] = inflFloat.get(i);
                        }

                        gridPaneUPhaseA.setDisable(false);
                        txtFieldInfUPhaseA.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUPhaseA.setSelected(false);
                    }
                }
            } else {
                gridPaneUPhaseA.setDisable(true);
                txtFieldInfUPhaseA.setDisable(false);

                CheckBox checkBox;
                for (Node node : gridPaneUPhaseA.getChildren()) {
                    try {
                        checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            checkBox.setSelected(false);
                        }
                    } catch (ClassCastException ignore) {
                    }
                }

                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {

                    influenceUprocPhaseAAPPls = new float[0];

                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {

                    influenceUprocPhaseAAPMns = new float[0];

                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {

                    influenceUprocPhaseARPPls = new float[0];

                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {

                    influenceUprocPhaseARPMns = new float[0];
                }
            }
        }

        //Влияние напряжения Фаза B
        if (event.getSource() == addTglBtnInfUPhaseB) {
            if (addTglBtnInfUPhaseB.isSelected()) {

                //Если выбрано AP+
                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUPhaseB.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocPhaseBAPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocPhaseBAPPls[i] = inflFloat.get(i);
                        }

                        gridPaneUPhaseB.setDisable(false);
                        txtFieldInfUPhaseB.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUPhaseB.setSelected(false);
                    }

                    //Если выбрано AP-
                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUPhaseB.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocPhaseBAPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocPhaseBAPMns[i] = inflFloat.get(i);
                        }

                        gridPaneUPhaseB.setDisable(false);
                        txtFieldInfUPhaseB.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUPhaseB.setSelected(false);
                    }

                    //Если выбрано RP+
                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUPhaseB.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocPhaseBRPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocPhaseBRPPls[i] = inflFloat.get(i);
                        }

                        gridPaneUPhaseB.setDisable(false);
                        txtFieldInfUPhaseB.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUPhaseB.setSelected(false);
                    }

                    //Если выбрано RP-
                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfUPhaseB.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceUprocPhaseBRPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceUprocPhaseBRPMns[i] = inflFloat.get(i);
                        }

                        gridPaneUPhaseB.setDisable(false);
                        txtFieldInfUPhaseB.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfUPhaseB.setSelected(false);
                    }
                }
            } else {
                gridPaneUPhaseB.setDisable(true);
                txtFieldInfUPhaseB.setDisable(false);

                CheckBox checkBox;
                for (Node node : gridPaneUPhaseB.getChildren()) {
                    try {
                        checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            checkBox.setSelected(false);
                        }
                    } catch (ClassCastException ignore) {
                    }
                }

                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {

                    influenceUprocPhaseBAPPls = new float[0];

                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {

                    influenceUprocPhaseBAPMns = new float[0];

                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {

                    influenceUprocPhaseBRPPls = new float[0];

                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {

                    influenceUprocPhaseBRPMns = new float[0];
                }
            }
        }

        //Влияние частоты все фазы
        if (event.getSource() == addTglBtnInfFAllPhase) {
            if (addTglBtnInfFAllPhase.isSelected()) {

                //Если выбрано AP+
                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFAllPhase.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocAllPhaseAPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocAllPhaseAPPls[i] = inflFloat.get(i);
                        }

                        gridPaneFAllPhase.setDisable(false);
                        txtFieldInfFAllPhase.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFAllPhase.setSelected(false);
                    }

                    //Если выбрано AP-
                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFAllPhase.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocAllPhaseAPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocAllPhaseAPMns[i] = inflFloat.get(i);
                        }

                        gridPaneFAllPhase.setDisable(false);
                        txtFieldInfFAllPhase.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFAllPhase.setSelected(false);
                    }

                    //Если выбрано RP+
                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFAllPhase.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocAllPhaseRPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocAllPhaseRPPls[i] = inflFloat.get(i);
                        }

                        gridPaneFAllPhase.setDisable(false);
                        txtFieldInfFAllPhase.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFAllPhase.setSelected(false);
                    }

                    //Если выбрано RP-
                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFAllPhase.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocAllPhaseRPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocAllPhaseRPMns[i] = inflFloat.get(i);
                        }

                        gridPaneFAllPhase.setDisable(false);
                        txtFieldInfFAllPhase.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFAllPhase.setSelected(false);
                    }
                }
            } else {
                gridPaneFAllPhase.setDisable(true);
                txtFieldInfFAllPhase.setDisable(false);

                CheckBox checkBox;
                for (Node node : gridPaneFAllPhase.getChildren()) {
                    try {
                        checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            checkBox.setSelected(false);
                        }
                    } catch (ClassCastException ignore) {
                    }
                }

                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {

                    influenceFprocAllPhaseAPPls = new float[0];

                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {

                    influenceFprocAllPhaseAPMns = new float[0];

                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {

                    influenceFprocAllPhaseRPPls = new float[0];

                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {

                    influenceFprocAllPhaseRPMns = new float[0];
                }
            }
        }

        //Влияние напряжения Фаза А
        if (event.getSource() == addTglBtnInfFPhaseA) {
            if (addTglBtnInfFPhaseA.isSelected()) {

                //Если выбрано AP+
                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFPhaseA.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocPhaseAAPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocPhaseAAPPls[i] = inflFloat.get(i);
                        }

                        gridPaneFPhaseA.setDisable(false);
                        txtFieldInfFPhaseA.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFPhaseA.setSelected(false);
                    }

                    //Если выбрано AP-
                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFPhaseA.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocPhaseAAPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocPhaseAAPMns[i] = inflFloat.get(i);
                        }

                        gridPaneFPhaseA.setDisable(false);
                        txtFieldInfFPhaseA.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFPhaseA.setSelected(false);
                    }

                    //Если выбрано RP+
                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFPhaseA.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocPhaseARPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocPhaseARPPls[i] = inflFloat.get(i);
                        }

                        gridPaneFPhaseA.setDisable(false);
                        txtFieldInfFPhaseA.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFPhaseA.setSelected(false);
                    }

                    //Если выбрано RP-
                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFPhaseA.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocPhaseARPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocPhaseARPMns[i] = inflFloat.get(i);
                        }

                        gridPaneFPhaseA.setDisable(false);
                        txtFieldInfFPhaseA.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFPhaseA.setSelected(false);
                    }
                }
            } else {
                gridPaneFPhaseA.setDisable(true);
                txtFieldInfFPhaseA.setDisable(false);

                CheckBox checkBox;
                for (Node node : gridPaneFPhaseA.getChildren()) {
                    try {
                        checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            checkBox.setSelected(false);
                        }
                    } catch (ClassCastException ignore) {
                    }
                }

                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {

                    influenceFprocPhaseAAPPls = new float[0];

                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {

                    influenceFprocPhaseAAPMns = new float[0];

                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {

                    influenceFprocPhaseARPPls = new float[0];

                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {

                    influenceFprocPhaseARPMns = new float[0];
                }
            }
        }

        //Влияние напряжения Фаза B
        if (event.getSource() == addTglBtnInfFPhaseB) {
            if (addTglBtnInfFPhaseB.isSelected()) {

                //Если выбрано AP+
                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFPhaseB.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocPhaseBAPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocPhaseBAPPls[i] = inflFloat.get(i);
                        }

                        gridPaneFPhaseB.setDisable(false);
                        txtFieldInfFPhaseB.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFPhaseB.setSelected(false);
                    }

                    //Если выбрано AP-
                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFPhaseB.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocPhaseBAPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocPhaseBAPMns[i] = inflFloat.get(i);
                        }

                        gridPaneFPhaseB.setDisable(false);
                        txtFieldInfFPhaseB.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFPhaseB.setSelected(false);
                    }

                    //Если выбрано RP+
                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFPhaseB.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocPhaseBRPPls = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocPhaseBRPPls[i] = inflFloat.get(i);
                        }

                        gridPaneFPhaseB.setDisable(false);
                        txtFieldInfFPhaseB.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFPhaseB.setSelected(false);
                    }

                    //Если выбрано RP-
                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {
                    try {
                        String[] inf = txtFieldInfFPhaseB.getText().split(",");
                        List<Float> inflFloat = new ArrayList<>();
                        Float procFloat;

                        for (String proc : inf) {
                            procFloat = Float.parseFloat(proc.trim());
                            inflFloat.add(procFloat);
                        }

                        influenceFprocPhaseBRPMns = new float[inflFloat.size()];

                        for (int i = 0; i < inflFloat.size(); i++) {
                            influenceFprocPhaseBRPMns[i] = inflFloat.get(i);
                        }

                        gridPaneFPhaseB.setDisable(false);
                        txtFieldInfFPhaseB.setDisable(true);
                    } catch (NumberFormatException e) {
                        //InfoEx
                        addTglBtnInfFPhaseB.setSelected(false);
                    }
                }
            } else {
                gridPaneFPhaseB.setDisable(true);
                txtFieldInfFPhaseB.setDisable(false);

                CheckBox checkBox;
                for (Node node : gridPaneFPhaseB.getChildren()) {
                    try {
                        checkBox = (CheckBox) node;
                        if (checkBox.isSelected()) {
                            checkBox.setSelected(false);
                        }
                    } catch (ClassCastException ignore) {
                    }
                }

                if (APPlus.isSelected() || APPlusCRPSTA.isSelected()) {

                    influenceFprocPhaseBAPPls = new float[0];

                } else if (APMinus.isSelected() || APMinusCRPSTA.isSelected()) {

                    influenceFprocPhaseBAPMns = new float[0];

                } else if (RPPlus.isSelected() || RPPlusCRPSTA.isSelected()) {

                    influenceFprocPhaseBRPPls = new float[0];

                } else if (RPMinus.isSelected() || RPMinusCRPSTA.isSelected()) {

                    influenceFprocPhaseBRPMns = new float[0];
                }
            }
        }
    }

    @FXML
    void selectInfluenceTests(ActionEvent event) {
        if (event.getSource() == selectInfUAllPhaseTgl) {
            selectInfUAllPhaseTgl.setSelected(true);
            selectInfFAllPhaseTgl.setSelected(false);

            InflUAllPhasePane.toFront();
            gridPaneUAllPhase.toFront();

            setVisibleSelectedGridPane(gridPaneUAllPhase);
        }
        if (event.getSource() == selectInfFAllPhaseTgl) {
            selectInfUAllPhaseTgl.setSelected(false);
            selectInfFAllPhaseTgl.setSelected(true);

            InflFAllPhasePane.toFront();
            gridPaneFAllPhase.toFront();

            setVisibleSelectedGridPane(gridPaneFAllPhase);
        }

        if (event.getSource() == selectInfUPhaseATgl) {
            selectInfUPhaseATgl.setSelected(true);
            selectInfFPhaseATgl.setSelected(false);

            InflUPhaseAPane.toFront();
            gridPaneUPhaseA.toFront();

            setVisibleSelectedGridPane(gridPaneUPhaseA);
        }
        if (event.getSource() == selectInfFPhaseATgl) {
            selectInfUPhaseATgl.setSelected(false);
            selectInfFPhaseATgl.setSelected(true);

            InflFPhaseAPane.toFront();
            gridPaneFPhaseA.toFront();

            setVisibleSelectedGridPane(gridPaneFPhaseA);
        }

        if (event.getSource() == selectInfUPhaseBTgl) {
            selectInfUPhaseBTgl.setSelected(true);
            selectInfFPhaseBTgl.setSelected(false);

            InflUPhaseBPane.toFront();
            gridPaneUPhaseB.toFront();

            setVisibleSelectedGridPane(gridPaneUPhaseB);
        }
        if (event.getSource() == selectInfFPhaseBTgl) {
            selectInfUPhaseBTgl.setSelected(false);
            selectInfFPhaseBTgl.setSelected(true);

            InflFPhaseBPane.toFront();
            gridPaneFPhaseB.toFront();

            setVisibleSelectedGridPane(gridPaneFPhaseB);
        }

        //На всякий
        if (event.getSource() == selectInfUPhaseCTgl) {
            selectInfUPhaseCTgl.setSelected(true);
            selectInfFPhaseCTgl.setSelected(false);

            InflUPhaseCPane.toFront();
        }

        if (event.getSource() == selectInfFPhaseCTgl) {
            selectInfUPhaseCTgl.setSelected(false);
            selectInfFPhaseCTgl.setSelected(true);

            InflFPhaseCPane.toFront();
        }
    }

    @FXML
    void saveOrCancelAction(ActionEvent event) {
        if (event.getSource() == saveBtn) {
            saveInflInMetodic();
            Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void setPhaseGridPaneAction(ActionEvent event) {
        if (event.getSource() == allPhaseBtn) {
            allPhaseBtn.setSelected(true);
            phaseABtn.setSelected(false);
            phaseBBtn.setSelected(false);

            mainAllPhasePane.toFront();
            selectInfUAllPhaseTgl.fire();
        }

        if (event.getSource() == phaseABtn) {
            allPhaseBtn.setSelected(false);
            phaseABtn.setSelected(true);
            phaseBBtn.setSelected(false);

            mainPhaseAPane.toFront();
            selectInfUPhaseATgl.fire();
        }

        if (event.getSource() == phaseBBtn) {
            allPhaseBtn.setSelected(false);
            phaseABtn.setSelected(false);
            phaseBBtn.setSelected(true);

            mainPhaseBPane.toFront();
            selectInfUPhaseBTgl.fire();
        }
    }

    @FXML
    void setPointFrameAction(ActionEvent event) {
        if (event.getSource() == APPlus || event.getSource() == APPlusCRPSTA) {

            if (!APPlus.isSelected() || !APPlusCRPSTA.isSelected()) {
                APPlus.setSelected(true);
                APMinus.setSelected(false);
                RPPlus.setSelected(false);
                RPMinus.setSelected(false);

                APPlusCRPSTA.setSelected(true);
                APMinusCRPSTA.setSelected(false);
                RPPlusCRPSTA.setSelected(false);
                RPMinusCRPSTA.setSelected(false);

                viewPointTableAPPls.toFront();

                initGridPanesAndChoiceSelectionPanesForAPPls();
            }

        } else if (event.getSource() == APMinus || event.getSource() == APMinusCRPSTA) {

            if (!APMinus.isSelected() || !APMinusCRPSTA.isSelected()) {
                APPlus.setSelected(false);
                APMinus.setSelected(true);
                RPPlus.setSelected(false);
                RPMinus.setSelected(false);

                APPlusCRPSTA.setSelected(false);
                APMinusCRPSTA.setSelected(true);
                RPPlusCRPSTA.setSelected(false);
                RPMinusCRPSTA.setSelected(false);

                viewPointTableAPMns.toFront();

                initGridPanesAndChoiceSelectionPanesForAPMns();
            }

        } else if (event.getSource() == RPPlus || event.getSource() == RPPlusCRPSTA) {

            if (!RPPlus.isSelected() || !RPPlusCRPSTA.isSelected()) {
                APPlus.setSelected(false);
                APMinus.setSelected(false);
                RPPlus.setSelected(true);
                RPMinus.setSelected(false);

                APPlusCRPSTA.setSelected(false);
                APMinusCRPSTA.setSelected(false);
                RPPlusCRPSTA.setSelected(true);
                RPMinusCRPSTA.setSelected(false);

                viewPointTableRPPls.toFront();

                initGridPanesAndChoiceSelectionPanesForRPPls();
            }

        }  else if (event.getSource() == RPMinus || event.getSource() == RPMinusCRPSTA) {

            if (!RPMinus.isSelected() || !RPMinusCRPSTA.isSelected()) {
                APPlus.setSelected(false);
                APMinus.setSelected(false);
                RPPlus.setSelected(false);
                RPMinus.setSelected(true);

                APPlusCRPSTA.setSelected(false);
                APMinusCRPSTA.setSelected(false);
                RPPlusCRPSTA.setSelected(false);
                RPMinusCRPSTA.setSelected(true);

                viewPointTableRPMns.toFront();

                initGridPanesAndChoiceSelectionPanesForRPMns();
            }
        }
    }

    //Инициализация полей влияния
    private void intitInflFields(MethodicForOnePhaseStend methodicForOnePhaseStend) {
        inflListForCollumAPPls = FXCollections.observableArrayList(methodicForOnePhaseStend.getSaveInflListForCollumAPPls());
        inflListForCollumAPMns = FXCollections.observableArrayList(methodicForOnePhaseStend.getSaveInflListForCollumAPMns());
        inflListForCollumRPPls = FXCollections.observableArrayList(methodicForOnePhaseStend.getSaveInflListForCollumRPPls());
        inflListForCollumRPMns = FXCollections.observableArrayList(methodicForOnePhaseStend.getSaveInflListForCollumRPMns());

        influenceUprocAllPhaseAPPls = methodicForOnePhaseStend.getSaveInfluenceUprocAllPhaseAPPls();
        influenceUprocPhaseAAPPls = methodicForOnePhaseStend.getSaveInfluenceUprocPhaseAAPPls();
        influenceUprocPhaseBAPPls = methodicForOnePhaseStend.getSaveInfluenceUprocPhaseBAPPls();

        influenceFprocAllPhaseAPPls = methodicForOnePhaseStend.getSaveInfluenceFprocAllPhaseAPPls();
        influenceFprocPhaseAAPPls = methodicForOnePhaseStend.getSaveInfluenceFprocPhaseAAPPls();
        influenceFprocPhaseBAPPls = methodicForOnePhaseStend.getSaveInfluenceFprocPhaseBAPPls();

        influenceUprocAllPhaseAPMns = methodicForOnePhaseStend.getSaveInfluenceUprocAllPhaseAPMns();
        influenceUprocPhaseAAPMns = methodicForOnePhaseStend.getSaveInfluenceUprocPhaseAAPMns();
        influenceUprocPhaseBAPMns = methodicForOnePhaseStend.getSaveInfluenceUprocPhaseBAPMns();

        influenceFprocAllPhaseAPMns = methodicForOnePhaseStend.getSaveInfluenceFprocAllPhaseAPMns();
        influenceFprocPhaseAAPMns = methodicForOnePhaseStend.getSaveInfluenceFprocPhaseAAPMns();
        influenceFprocPhaseBAPMns = methodicForOnePhaseStend.getSaveInfluenceFprocPhaseBAPMns();

        influenceUprocAllPhaseRPPls = methodicForOnePhaseStend.getSaveInfluenceUprocAllPhaseRPPls();
        influenceUprocPhaseARPPls = methodicForOnePhaseStend.getSaveInfluenceUprocPhaseARPPls();
        influenceUprocPhaseBRPPls = methodicForOnePhaseStend.getSaveInfluenceUprocPhaseBRPPls();

        influenceFprocAllPhaseRPPls = methodicForOnePhaseStend.getSaveInfluenceFprocAllPhaseRPPls();
        influenceFprocPhaseARPPls = methodicForOnePhaseStend.getSaveInfluenceFprocPhaseARPPls();
        influenceFprocPhaseBRPPls = methodicForOnePhaseStend.getSaveInfluenceFprocPhaseBRPPls();

        influenceUprocAllPhaseRPMns = methodicForOnePhaseStend.getSaveInfluenceUprocAllPhaseRPMns();
        influenceUprocPhaseARPMns = methodicForOnePhaseStend.getSaveInfluenceUprocPhaseARPMns();
        influenceUprocPhaseBRPMns = methodicForOnePhaseStend.getSaveInfluenceUprocPhaseBRPMns();

        influenceFprocAllPhaseRPMns = methodicForOnePhaseStend.getSaveInfluenceFprocAllPhaseRPMns();
        influenceFprocPhaseARPMns = methodicForOnePhaseStend.getSaveInfluenceFprocPhaseARPMns();
        influenceFprocPhaseBRPMns = methodicForOnePhaseStend.getSaveInfluenceFprocPhaseBRPMns();
    }

    private void saveInflInMetodic() {
        methodicForOnePhaseStend.setSaveInflListForCollumAPPls(new ArrayList<>(inflListForCollumAPPls));
        methodicForOnePhaseStend.setSaveInflListForCollumAPMns(new ArrayList<>(inflListForCollumAPMns));
        methodicForOnePhaseStend.setSaveInflListForCollumRPPls(new ArrayList<>(inflListForCollumRPPls));
        methodicForOnePhaseStend.setSaveInflListForCollumRPMns(new ArrayList<>(inflListForCollumRPMns));

        methodicForOnePhaseStend.setSaveInfluenceUprocAllPhaseAPPls(influenceUprocAllPhaseAPPls);
        methodicForOnePhaseStend.setSaveInfluenceUprocPhaseAAPPls(influenceUprocPhaseAAPPls);
        methodicForOnePhaseStend.setSaveInfluenceUprocPhaseBAPPls(influenceUprocPhaseBAPPls);

        methodicForOnePhaseStend.setSaveInfluenceFprocAllPhaseAPPls(influenceFprocAllPhaseAPPls);
        methodicForOnePhaseStend.setSaveInfluenceFprocPhaseAAPPls(influenceFprocPhaseAAPPls);
        methodicForOnePhaseStend.setSaveInfluenceFprocPhaseBAPPls(influenceFprocPhaseBAPPls);

        methodicForOnePhaseStend.setSaveInfluenceUprocAllPhaseAPMns(influenceUprocAllPhaseAPMns);
        methodicForOnePhaseStend.setSaveInfluenceUprocPhaseAAPMns(influenceUprocPhaseAAPMns);
        methodicForOnePhaseStend.setSaveInfluenceUprocPhaseBAPMns(influenceUprocPhaseBAPMns);

        methodicForOnePhaseStend.setSaveInfluenceFprocAllPhaseAPMns(influenceFprocAllPhaseAPMns);
        methodicForOnePhaseStend.setSaveInfluenceFprocPhaseAAPMns(influenceFprocPhaseAAPMns);
        methodicForOnePhaseStend.setSaveInfluenceFprocPhaseBAPMns(influenceFprocPhaseBAPMns);

        methodicForOnePhaseStend.setSaveInfluenceUprocAllPhaseRPPls(influenceUprocAllPhaseRPPls);
        methodicForOnePhaseStend.setSaveInfluenceUprocPhaseARPPls(influenceUprocPhaseARPPls);
        methodicForOnePhaseStend.setSaveInfluenceUprocPhaseBRPPls(influenceUprocPhaseBRPPls);

        methodicForOnePhaseStend.setSaveInfluenceFprocAllPhaseRPPls(influenceFprocAllPhaseRPPls);
        methodicForOnePhaseStend.setSaveInfluenceFprocPhaseARPPls(influenceFprocPhaseARPPls);
        methodicForOnePhaseStend.setSaveInfluenceFprocPhaseBRPPls(influenceFprocPhaseBRPPls);

        methodicForOnePhaseStend.setSaveInfluenceUprocAllPhaseRPMns(influenceUprocAllPhaseRPMns);
        methodicForOnePhaseStend.setSaveInfluenceUprocPhaseARPMns(influenceUprocPhaseARPMns);
        methodicForOnePhaseStend.setSaveInfluenceUprocPhaseBRPMns(influenceUprocPhaseBRPMns);

        methodicForOnePhaseStend.setSaveInfluenceFprocAllPhaseRPMns(influenceFprocAllPhaseRPMns);
        methodicForOnePhaseStend.setSaveInfluenceFprocPhaseARPMns(influenceFprocPhaseARPMns);
        methodicForOnePhaseStend.setSaveInfluenceFprocPhaseBRPMns(influenceFprocPhaseBRPMns);
    }

    //=========================================================================================================//
    //========================= Инициализация основного и дополнительных scroll panes ==========================//
    //=========================================================================================================//
    private void initMainScrollPaneAndScrollPaneCurrentScrollPanePowerFactor() {
        initMainScrollPane();
        initScrolPaneForCurrentAndPowerFactor();
        createScrollPanesForGridPane();
    }

    private void initMainScrollPane() {
        mainScrollPane.setPrefHeight(230);
        mainScrollPane.setPrefWidth(643);
        mainScrollPane.setLayoutX(136);
        mainScrollPane.setLayoutY(116);
        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setStyle("-fx-background: #6A6A6A;");

        String cssAdress = getClass().getClassLoader().getResource("styleCSS/scrollPane.css").toString();
        if (cssAdress != null) {
            mainScrollPane.getStylesheets().add(cssAdress);
        }

        mainScrollPane.setContent(stackPaneForGridPane);

        mainAnchorPane.getChildren().add(mainScrollPane);

        //stackPaneForGridPane.getChildren().addAll(gridPanesEnergyAndPhase);

        mainScrollPane.setContent(stackPaneForGridPane);
    }

    private void initScrolPaneForCurrentAndPowerFactor() {
        gridPaneForCurrent.setPrefWidth(gridPaneUAllPhase.getWidth());
        scrollPaneForCurrent.setContent(gridPaneForCurrent);

        gridPaneForCurrent.setGridLinesVisible(true);
        gridPaneForCurrent.setStyle("#6A6A6A");
        gridPaneForCurrent.getRowConstraints().add(new RowConstraints(23));
        gridPaneForCurrent.getColumnConstraints().add(new ColumnConstraints(50));
        Label labelCurr;

        for (int i = 0; i < current.size(); i++) {
            gridPaneForCurrent.getColumnConstraints().add(new ColumnConstraints(50));
            labelCurr = new Label(current.get(i));
            labelCurr.setTextFill(Color.BLACK);
            GridPane.setColumnIndex(labelCurr, i + 1);
            GridPane.setHalignment(labelCurr, HPos.CENTER);
            GridPane.setValignment(labelCurr, VPos.CENTER);
            gridPaneForCurrent.getChildren().add(labelCurr);
        }

        gridPaneForPowerFactor.setGridLinesVisible(true);
        gridPaneForPowerFactor.setStyle("#6A6A6A");
        gridPaneForPowerFactor.getRowConstraints().add(new RowConstraints(23));
        gridPaneForPowerFactor.getColumnConstraints().add(new ColumnConstraints(50));
        Label labelPowerFactor;

        for (int i = 0; i < powerFactor.size(); i++) {
            gridPaneForPowerFactor.getRowConstraints().add(new RowConstraints(23));
            labelPowerFactor = new Label(powerFactor.get(i));
            labelPowerFactor.setTextFill(Color.BLACK);
            GridPane.setRowIndex(labelPowerFactor, i + 1);
            GridPane.setHalignment(labelPowerFactor, HPos.CENTER);
            GridPane.setValignment(labelPowerFactor, VPos.CENTER);
            gridPaneForPowerFactor.getChildren().add(labelPowerFactor);
        }
    }

    private void createScrollPanesForGridPane() {
        //Curr
        scrollPaneForCurrent.setMinHeight(0);
        scrollPaneForCurrent.setPrefHeight(24);
        scrollPaneForCurrent.setStyle("-fx-background: #FFC107;" +
                "-fx-background-insets: 0, 0 1 1 0;" +
                "-fx-background-color: #FFC107;");

        scrollPaneForCurrent.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForCurrent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForCurrent.setLayoutX(136);
        scrollPaneForCurrent.setLayoutY(116);

        scrollPaneForCurrent.setPrefWidth(mainScrollPane.getPrefWidth() - 13);
        mainAnchorPane.getChildren().add(scrollPaneForCurrent);

        //PF
        scrollPaneForPowerFactor.setMinWidth(0);
        scrollPaneForPowerFactor.setPrefWidth(50);
        scrollPaneForPowerFactor.setStyle("-fx-background: #FFC107;" +
                "-fx-background-insets: 0, 0 1 1 0;" +
                "-fx-background-color: #FFC107;");

        scrollPaneForPowerFactor.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForPowerFactor.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForPowerFactor.setLayoutX(136);
        scrollPaneForPowerFactor.setLayoutY(116);

        scrollPaneForPowerFactor.setPrefHeight(mainScrollPane.getPrefHeight() - 13);
        mainAnchorPane.getChildren().add(scrollPaneForPowerFactor);

        gridPaneForPowerFactor.setPrefHeight(gridPaneUAllPhase.getHeight());
        scrollPaneForPowerFactor.setContent(gridPaneForPowerFactor);

        //Закрывающий квадрат
        fillSquare.toFront();
        fillSquare.setStyle("-fx-background-color: #FFC107;");
        fillSquare.setPrefHeight(23);
        fillSquare.setPrefWidth(50);
        fillSquare.setLayoutX(136);
        fillSquare.setLayoutY(116);
        mainAnchorPane.getChildren().add(fillSquare);
    }

    public void bindScrollPanesCurrentAndPowerFactorToMainScrollPane() {
        ScrollBar currentHorizontalScroll = null;
        ScrollBar mainHorizontalScroll = null;

        ScrollBar powerFactorVerticalScroll = null;
        ScrollBar mainVerticalScroll = null;

        Set<Node> mainScrollBars = mainScrollPane.lookupAll(".scroll-bar");

        ScrollBar nodeScroll;

        for (Node node : mainScrollBars) {
            nodeScroll = (ScrollBar) node;

            if (nodeScroll.getOrientation() == Orientation.HORIZONTAL) {
                mainHorizontalScroll = nodeScroll;
            } else {
                mainVerticalScroll = nodeScroll;
            }
        }

        Set<Node> currentScrollBars = scrollPaneForCurrent.lookupAll(".scroll-bar");
        for (Node node : currentScrollBars) {
            currentHorizontalScroll = (ScrollBar) node;
            if (currentHorizontalScroll.getOrientation() == Orientation.HORIZONTAL) {
                break;
            }
        }

        Set<Node> powerFactorScrollBars = scrollPaneForPowerFactor.lookupAll(".scroll-bar");
        for (Node node : powerFactorScrollBars) {
            powerFactorVerticalScroll = (ScrollBar) node;
            if (powerFactorVerticalScroll.getOrientation() == Orientation.VERTICAL) {
                break;
            }
        }

        if (currentHorizontalScroll == null || mainHorizontalScroll == null || powerFactorVerticalScroll == null || mainVerticalScroll == null) {
            return;
        }

        currentHorizontalScroll.valueProperty().bindBidirectional(mainHorizontalScroll.valueProperty());
        powerFactorVerticalScroll.valueProperty().bindBidirectional(mainVerticalScroll.valueProperty());
    }

    //=========================================================================================================//
    //========================= Инициализация GridPanes и панели выбора натроек ===============================//
    //=========================================================================================================//
    //Инициализирует все поля, сетку и чекбоксы для AP+
    private void initGridPanesAndChoiceSelectionPanesForAPPls() {
        initAllGridPanesForAPPls();
        setSelectBtns();
        initSelectPanesAPPls();
    }

    //Инициализирует все поля, сетку и чекбоксы для AP-
    private void initGridPanesAndChoiceSelectionPanesForAPMns() {
        initAllGridPanesForAPMns();
        setSelectBtns();
        initSelectPanesAPMns();
    }

    //Инициализирует все поля, сетку и чекбоксы для RP+
    private void initGridPanesAndChoiceSelectionPanesForRPPls() {
        initAllGridPanesForRPPls();
        setSelectBtns();
        initSelectPanesRPPls();
    }

    //Инициализирует все поля, сетку и чекбоксы для RP-
    private void initGridPanesAndChoiceSelectionPanesForRPMns() {
        initAllGridPanesForRPMns();
        setSelectBtns();
        initSelectPanesRPMns();
    }

    //Устанавливает начальное положение пейнов при смене на AP+
    private void setSelectBtns() {
        allPhaseBtn.setSelected(true);
        phaseABtn.setSelected(false);
        phaseBBtn.setSelected(false);

        gridPaneUAllPhase.toFront();

        mainAllPhasePane.toFront();
        selectInfUAllPhaseTgl.setSelected(true);
        selectInfFAllPhaseTgl.setSelected(false);

        InflUAllPhasePane.toFront();
    }

    private void initAllGridPanesForAPPls() {
        removeOldGrPnAndSetIDGridPanesForAPPls();
        creadteGridPane();
        addCheckBoxesInGridPane();
        selectCheckBoxesInGridPane(inflListForCollumAPPls);
        setListenerForChecBoxInGridPane();
    }

    private void initAllGridPanesForAPMns() {
        removeOldGrPnAndSetIDGridPanesForAPMns();
        creadteGridPane();
        addCheckBoxesInGridPane();
        selectCheckBoxesInGridPane(inflListForCollumAPMns);
        setListenerForChecBoxInGridPane();
    }

    private void initAllGridPanesForRPPls() {
        removeOldGrPnAndSetIDGridPanesForRPPls();
        creadteGridPane();
        addCheckBoxesInGridPane();
        selectCheckBoxesInGridPane(inflListForCollumRPPls);
        setListenerForChecBoxInGridPane();
    }

    private void initAllGridPanesForRPMns() {
        removeOldGrPnAndSetIDGridPanesForRPMns();
        creadteGridPane();
        addCheckBoxesInGridPane();
        selectCheckBoxesInGridPane(inflListForCollumRPMns);
        setListenerForChecBoxInGridPane();
    }

    //Вбивает ранее использованные параметры влияния в необходимые поля
    private void initSelectPanesAPPls() {
        String infStr;
        directLabAllPhaseU.setText("Активная энергия в прямом направлении тока");
        directLabPhaseAU.setText("Активная энергия в прямом направлении тока");
        directLabPhaseBU.setText("Активная энергия в прямом направлении тока");
        directLabPhaseCU.setText("Активная энергия в прямом направлении тока");

        directLabAllPhaseF.setText("Активная энергия в прямом направлении тока");
        directLabPhaseAF.setText("Активная энергия в прямом направлении тока");
        directLabPhaseBF.setText("Активная энергия в прямом направлении тока");
        directLabPhaseCF.setText("Активная энергия в прямом направлении тока");

        if (influenceUprocAllPhaseAPPls.length != 0) {
            infStr = Arrays.toString(influenceUprocAllPhaseAPPls);

            txtFieldInfUAllPhase.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUAllPhase.setSelected(true);
            gridPaneUAllPhase.setDisable(false);
            txtFieldInfUAllPhase.setDisable(true);

        } else {
            txtFieldInfUAllPhase.setDisable(false);
            txtFieldInfUAllPhase.clear();
            addTglBtnInfUAllPhase.setSelected(false);
            gridPaneUAllPhase.setDisable(true);
            txtFieldInfUAllPhase.setDisable(false);
        }

        if (influenceUprocPhaseAAPPls.length != 0) {
            infStr = Arrays.toString(influenceUprocPhaseAAPPls);

            txtFieldInfUPhaseA.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseA.setSelected(true);
            gridPaneUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.setDisable(true);
        } else {
            txtFieldInfUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.clear();
            addTglBtnInfUPhaseA.setSelected(false);
            gridPaneUPhaseA.setDisable(true);
            txtFieldInfUPhaseA.setDisable(false);
        }

        if (influenceUprocPhaseBAPPls.length != 0) {
            infStr = Arrays.toString(influenceUprocPhaseBAPPls);

            txtFieldInfUPhaseB.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseB.setSelected(true);
            gridPaneUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.setDisable(true);
        } else {
            txtFieldInfUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.clear();
            addTglBtnInfUPhaseB.setSelected(false);
            gridPaneUPhaseB.setDisable(true);
            txtFieldInfUPhaseB.setDisable(false);
        }

        if (influenceFprocAllPhaseAPPls.length != 0) {
            infStr = Arrays.toString(influenceFprocAllPhaseAPPls);

            txtFieldInfFAllPhase.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfFAllPhase.setSelected(true);
            gridPaneFAllPhase.setDisable(false);
            txtFieldInfFAllPhase.setDisable(true);
        } else {
            txtFieldInfFAllPhase.setDisable(false);
            txtFieldInfFAllPhase.clear();
            addTglBtnInfFAllPhase.setSelected(false);
            gridPaneFAllPhase.setDisable(true);
            txtFieldInfFAllPhase.setDisable(false);
        }

        if (influenceFprocPhaseAAPPls.length != 0) {
            infStr = Arrays.toString(influenceFprocPhaseAAPPls);
            txtFieldInfFPhaseA.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfFPhaseA.setSelected(true);
            gridPaneFPhaseA.setDisable(false);
            txtFieldInfFPhaseA.setDisable(true);
        } else {
            txtFieldInfFPhaseA.setDisable(false);
            txtFieldInfFPhaseA.clear();
            addTglBtnInfFPhaseA.setSelected(false);
            gridPaneFPhaseA.setDisable(true);
            txtFieldInfFPhaseA.setDisable(false);
        }

        if (influenceFprocPhaseBAPPls.length != 0) {
            infStr = Arrays.toString(influenceFprocPhaseBAPPls);
            txtFieldInfFPhaseB.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfFPhaseB.setSelected(true);
            gridPaneFPhaseB.setDisable(false);
            txtFieldInfFPhaseB.setDisable(true);
        } else {
            txtFieldInfFPhaseB.setDisable(false);
            txtFieldInfFPhaseB.clear();
            addTglBtnInfFPhaseB.setSelected(false);
            gridPaneFPhaseB.setDisable(true);
            txtFieldInfFPhaseB.setDisable(false);
        }
    }

    private void initSelectPanesAPMns() {
        String infStr;

        directLabAllPhaseU.setText("Активная энергия в обратном направлении тока");
        directLabPhaseAU.setText("Активная энергия в обратном направлении тока");
        directLabPhaseBU.setText("Активная энергия в обратном направлении тока");
        directLabPhaseCU.setText("Активная энергия в обратном направлении тока");

        directLabAllPhaseF.setText("Активная энергия в обратном направлении тока");
        directLabPhaseAF.setText("Активная энергия в обратном направлении тока");
        directLabPhaseBF.setText("Активная энергия в обратном направлении тока");
        directLabPhaseCF.setText("Активная энергия в обратном направлении тока");

        if (influenceUprocAllPhaseAPMns.length != 0) {
            infStr = Arrays.toString(influenceUprocAllPhaseAPMns);
            txtFieldInfUAllPhase.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUAllPhase.setSelected(true);
            gridPaneUAllPhase.setDisable(false);
            txtFieldInfUAllPhase.setDisable(true);
        } else {
            txtFieldInfUAllPhase.setDisable(false);
            txtFieldInfUAllPhase.clear();
            addTglBtnInfUAllPhase.setSelected(false);
            gridPaneUAllPhase.setDisable(true);
            txtFieldInfUAllPhase.setDisable(false);
        }

        if (influenceUprocPhaseAAPMns.length != 0) {
            infStr = Arrays.toString(influenceUprocPhaseAAPMns);
            txtFieldInfUPhaseA.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseA.setSelected(true);
            gridPaneUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.setDisable(true);
        } else {
            txtFieldInfUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.clear();
            addTglBtnInfUPhaseA.setSelected(false);
            gridPaneUPhaseA.setDisable(true);
            txtFieldInfUPhaseA.setDisable(false);
        }

        if (influenceUprocPhaseBAPMns.length != 0) {
            infStr = Arrays.toString(influenceUprocPhaseBAPMns);
            txtFieldInfUPhaseB.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseB.setSelected(true);
            gridPaneUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.setDisable(true);
        } else {
            txtFieldInfUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.clear();
            addTglBtnInfUPhaseB.setSelected(false);
            gridPaneUPhaseB.setDisable(true);
            txtFieldInfUPhaseB.setDisable(false);
        }

        if (influenceFprocAllPhaseAPMns.length != 0) {
            infStr = Arrays.toString(influenceFprocAllPhaseAPMns);
            txtFieldInfFAllPhase.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfFAllPhase.setSelected(true);
            gridPaneFAllPhase.setDisable(false);
            txtFieldInfFAllPhase.setDisable(true);
        } else {
            txtFieldInfFAllPhase.setDisable(false);
            txtFieldInfFAllPhase.clear();
            addTglBtnInfFAllPhase.setSelected(false);
            gridPaneFAllPhase.setDisable(true);
            txtFieldInfFAllPhase.setDisable(false);
        }

        if (influenceFprocPhaseAAPMns.length != 0) {
            infStr = Arrays.toString(influenceFprocPhaseAAPMns);
            txtFieldInfFPhaseA.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfFPhaseA.setSelected(true);
            gridPaneFPhaseA.setDisable(false);
            txtFieldInfFPhaseA.setDisable(true);
        } else {
            txtFieldInfFPhaseA.setDisable(false);
            txtFieldInfFPhaseA.clear();
            addTglBtnInfFPhaseA.setSelected(false);
            gridPaneFPhaseA.setDisable(true);
            txtFieldInfFPhaseA.setDisable(false);
        }

        if (influenceFprocPhaseBAPMns.length != 0) {
            infStr = Arrays.toString(influenceFprocPhaseBAPMns);
            txtFieldInfFPhaseB.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfFPhaseB.setSelected(true);
            gridPaneFPhaseB.setDisable(false);
            txtFieldInfFPhaseB.setDisable(true);
        } else {
            txtFieldInfFPhaseB.setDisable(false);
            txtFieldInfFPhaseB.clear();
            addTglBtnInfFPhaseB.setSelected(false);
            gridPaneFPhaseB.setDisable(true);
            txtFieldInfFPhaseB.setDisable(false);
        }
    }

    private void initSelectPanesRPPls() {
        String infStr;

        directLabAllPhaseU.setText("Реактивная энергия в прямом направлении тока");
        directLabPhaseAU.setText("Реактивная энергия в прямом направлении тока");
        directLabPhaseBU.setText("Реактивная энергия в прямом направлении тока");
        directLabPhaseCU.setText("Реактивная энергия в прямом направлении тока");

        directLabAllPhaseF.setText("Реактивная энергия в прямом направлении тока");
        directLabPhaseAF.setText("Реактивная энергия в прямом направлении тока");
        directLabPhaseBF.setText("Реактивная энергия в прямом направлении тока");
        directLabPhaseCF.setText("Реактивная энергия в прямом направлении тока");

        if (influenceUprocAllPhaseRPPls.length != 0) {
            infStr = Arrays.toString(influenceUprocAllPhaseRPPls);
            txtFieldInfUAllPhase.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfUAllPhase.setSelected(true);
            gridPaneUAllPhase.setDisable(false);
            txtFieldInfUAllPhase.setDisable(true);
        } else {
            txtFieldInfUAllPhase.setDisable(false);
            txtFieldInfUAllPhase.clear();
            addTglBtnInfUAllPhase.setSelected(false);
            gridPaneUAllPhase.setDisable(true);
            txtFieldInfUAllPhase.setDisable(false);
        }

        if (influenceUprocPhaseARPPls.length != 0) {
            infStr = Arrays.toString(influenceUprocPhaseARPPls);
            txtFieldInfUPhaseA.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseA.setSelected(true);
            gridPaneUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.setDisable(true);
        } else {
            txtFieldInfUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.clear();
            addTglBtnInfUPhaseA.setSelected(false);
            gridPaneUPhaseA.setDisable(true);
            txtFieldInfUPhaseA.setDisable(false);
        }

        if (influenceUprocPhaseBRPPls.length != 0) {
            infStr = Arrays.toString(influenceUprocPhaseBRPPls);
            txtFieldInfUPhaseB.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseB.setSelected(true);
            gridPaneUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.setDisable(true);
        } else {
            txtFieldInfUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.clear();
            addTglBtnInfUPhaseB.setSelected(false);
            gridPaneUPhaseB.setDisable(true);
            txtFieldInfUPhaseB.setDisable(false);
        }

        if (influenceFprocAllPhaseRPPls.length != 0) {
            infStr = Arrays.toString(influenceFprocAllPhaseRPPls);
            txtFieldInfFAllPhase.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfFAllPhase.setSelected(true);
            gridPaneFAllPhase.setDisable(false);
            txtFieldInfFAllPhase.setDisable(true);
        } else {
            txtFieldInfFAllPhase.setDisable(false);
            txtFieldInfFAllPhase.clear();
            addTglBtnInfFAllPhase.setSelected(false);
            gridPaneFAllPhase.setDisable(true);
            txtFieldInfFAllPhase.setDisable(false);
        }

        if (influenceFprocPhaseARPPls.length != 0) {
            infStr = Arrays.toString(influenceFprocPhaseARPPls);
            txtFieldInfFPhaseA.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfFPhaseA.setSelected(true);
            gridPaneFPhaseA.setDisable(false);
            txtFieldInfFPhaseA.setDisable(true);
        } else {
            txtFieldInfFPhaseA.setDisable(false);
            txtFieldInfFPhaseA.clear();
            addTglBtnInfFPhaseA.setSelected(false);
            gridPaneFPhaseA.setDisable(true);
            txtFieldInfFPhaseA.setDisable(false);
        }

        if (influenceFprocPhaseBRPPls.length != 0) {
            infStr = Arrays.toString(influenceFprocPhaseBRPPls);
            txtFieldInfFPhaseB.setText(infStr .substring(1, infStr.length() - 1));

            addTglBtnInfFPhaseB.setSelected(true);
            gridPaneFPhaseB.setDisable(false);
            txtFieldInfFPhaseB.setDisable(true);
        } else {
            txtFieldInfFPhaseB.setDisable(false);
            txtFieldInfFPhaseB.clear();
            addTglBtnInfFPhaseB.setSelected(false);
            gridPaneFPhaseB.setDisable(true);
            txtFieldInfFPhaseB.setDisable(false);
        }
    }

    private void initSelectPanesRPMns() {
        String infStr;

        directLabAllPhaseU.setText("Реактивная энергия в обратном направлении тока");
        directLabPhaseAU.setText("Реактивная энергия в обратном направлении тока");
        directLabPhaseBU.setText("Реактивная энергия в обратном направлении тока");
        directLabPhaseCU.setText("Реактивная энергия в обратном направлении тока");

        directLabAllPhaseF.setText("Реактивная энергия в обратном направлении тока");
        directLabPhaseAF.setText("Реактивная энергия в обратном направлении тока");
        directLabPhaseBF.setText("Реактивная энергия в обратном направлении тока");
        directLabPhaseCF.setText("Реактивная энергия в обратном направлении тока");

        if (influenceUprocAllPhaseRPMns.length != 0) {
            infStr = Arrays.toString(influenceUprocAllPhaseRPMns);
            txtFieldInfUAllPhase.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUAllPhase.setSelected(true);
            gridPaneUAllPhase.setDisable(false);
            txtFieldInfUAllPhase.setDisable(true);
        } else {
            txtFieldInfUAllPhase.setDisable(false);
            txtFieldInfUAllPhase.clear();
            addTglBtnInfUAllPhase.setSelected(false);
            gridPaneUAllPhase.setDisable(true);
            txtFieldInfUAllPhase.setDisable(false);
        }

        if (influenceUprocPhaseARPMns.length != 0) {
            infStr = Arrays.toString(influenceUprocPhaseARPMns);
            txtFieldInfUPhaseA.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseA.setSelected(true);
            gridPaneUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.setDisable(true);
        } else {
            txtFieldInfUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.clear();
            addTglBtnInfUPhaseA.setSelected(false);
            gridPaneUPhaseA.setDisable(true);
            txtFieldInfUPhaseA.setDisable(false);
        }

        if (influenceUprocPhaseBRPMns.length != 0) {
            infStr = Arrays.toString(influenceUprocPhaseBRPMns);
            txtFieldInfUPhaseB.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseB.setSelected(true);
            gridPaneUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.setDisable(true);
        } else {
            txtFieldInfUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.clear();
            addTglBtnInfUPhaseB.setSelected(false);
            gridPaneUPhaseB.setDisable(true);
            txtFieldInfUPhaseB.setDisable(false);
        }

        if (influenceFprocAllPhaseRPMns.length != 0) {
            infStr = Arrays.toString(influenceFprocAllPhaseRPMns);
            txtFieldInfFAllPhase.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfFAllPhase.setSelected(true);
            gridPaneFAllPhase.setDisable(false);
            txtFieldInfFAllPhase.setDisable(true);
        } else {
            txtFieldInfFAllPhase.setDisable(false);
            txtFieldInfFAllPhase.clear();
            addTglBtnInfFAllPhase.setSelected(false);
            gridPaneFAllPhase.setDisable(true);
            txtFieldInfFAllPhase.setDisable(false);
        }

        if (influenceFprocPhaseARPMns.length != 0) {
            infStr = Arrays.toString(influenceFprocPhaseARPMns);
            txtFieldInfFPhaseA.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseA.setSelected(true);
            gridPaneUPhaseA.setDisable(false);
            txtFieldInfUPhaseA.setDisable(true);
        } else {
            txtFieldInfFPhaseA.setDisable(false);
            txtFieldInfFPhaseA.clear();
            addTglBtnInfFPhaseA.setSelected(false);
            gridPaneFPhaseA.setDisable(true);
            txtFieldInfFPhaseA.setDisable(false);
        }

        if (influenceFprocPhaseBRPMns.length != 0) {
            infStr = Arrays.toString(influenceFprocPhaseBRPMns);
            txtFieldInfFPhaseB.setText(infStr.substring(1, infStr.length() - 1));

            addTglBtnInfUPhaseB.setSelected(true);
            gridPaneUPhaseB.setDisable(false);
            txtFieldInfUPhaseB.setDisable(true);
        } else {
            txtFieldInfFPhaseB.setDisable(false);
            txtFieldInfFPhaseB.clear();
            addTglBtnInfFPhaseB.setSelected(false);
            gridPaneFPhaseB.setDisable(true);
            txtFieldInfFPhaseB.setDisable(false);
        }
    }

    private void setVisibleSelectedGridPane(GridPane gridPane) {
        for (GridPane gridPane1 : gridPanesEnergyAndPhase) {
            if (gridPane1.equals(gridPane)) {
                gridPane.setVisible(true);
            } else {
                gridPane1.setVisible(false);
            }
        }
    }

    //Удаляет старые GridPane и инициализирует новые после смены напрявления или типа энергии
    private void removeOldGrPnAndSetIDGridPanesForAPPls() {
        stackPaneForGridPane.getChildren().clear();

        gridPaneUAllPhase = new GridPane();
        gridPaneUPhaseA = new GridPane();
        gridPaneUPhaseB = new GridPane();

        gridPaneFAllPhase = new GridPane();
        gridPaneFPhaseA = new GridPane();
        gridPaneFPhaseB = new GridPane();

        gridPaneUAllPhase.setId("U;0;H;A;P");
        gridPaneUPhaseA.setId("U;0;A;A;P");
        gridPaneUPhaseB.setId("U;0;B;A;P");

        gridPaneFAllPhase.setId("F;0;H;A;P");
        gridPaneFPhaseA.setId("F;0;A;A;P");
        gridPaneFPhaseB.setId("F;0;B;A;P");

        gridPaneUAllPhase.setVisible(true);
        gridPaneUPhaseA.setVisible(false);
        gridPaneUPhaseB.setVisible(false);

        gridPaneFAllPhase.setVisible(false);
        gridPaneFPhaseA.setVisible(false);
        gridPaneFPhaseB.setVisible(false);

        gridPanesEnergyAndPhase = Arrays.asList(
                gridPaneUAllPhase,
                gridPaneUPhaseA,
                gridPaneUPhaseB,
                gridPaneFAllPhase,
                gridPaneFPhaseA,
                gridPaneFPhaseB
        );
        stackPaneForGridPane.getChildren().addAll(gridPanesEnergyAndPhase);
    }

    private void removeOldGrPnAndSetIDGridPanesForAPMns() {
        stackPaneForGridPane.getChildren().clear();

        gridPaneUAllPhase = new GridPane();
        gridPaneUPhaseA = new GridPane();
        gridPaneUPhaseB = new GridPane();

        gridPaneFAllPhase = new GridPane();
        gridPaneFPhaseA = new GridPane();
        gridPaneFPhaseB = new GridPane();

        gridPaneUAllPhase.setId("U;0;H;A;N");
        gridPaneUPhaseA.setId("U;0;A;A;N");
        gridPaneUPhaseB.setId("U;0;B;A;N");

        gridPaneFAllPhase.setId("F;0;H;A;N");
        gridPaneFPhaseA.setId("F;0;A;A;N");
        gridPaneFPhaseB.setId("F;0;B;A;N");

        gridPaneUAllPhase.setVisible(true);
        gridPaneUPhaseA.setVisible(false);
        gridPaneUPhaseB.setVisible(false);

        gridPaneFAllPhase.setVisible(false);
        gridPaneFPhaseA.setVisible(false);
        gridPaneFPhaseB.setVisible(false);

        gridPanesEnergyAndPhase = Arrays.asList(
                gridPaneUAllPhase,
                gridPaneUPhaseA,
                gridPaneUPhaseB,
                gridPaneFAllPhase,
                gridPaneFPhaseA,
                gridPaneFPhaseB
        );
        stackPaneForGridPane.getChildren().addAll(gridPanesEnergyAndPhase);
    }

    private void removeOldGrPnAndSetIDGridPanesForRPPls() {
        stackPaneForGridPane.getChildren().clear();

        gridPaneUAllPhase = new GridPane();
        gridPaneUPhaseA = new GridPane();
        gridPaneUPhaseB = new GridPane();

        gridPaneFAllPhase = new GridPane();
        gridPaneFPhaseA = new GridPane();
        gridPaneFPhaseB = new GridPane();

        gridPaneUAllPhase.setId("U;7;H;R;P");
        gridPaneUPhaseA.setId("U;7;A;R;P");
        gridPaneUPhaseB.setId("U;7;B;R;P");

        gridPaneFAllPhase.setId("F;7;H;R;P");
        gridPaneFPhaseA.setId("F;7;A;R;P");
        gridPaneFPhaseB.setId("F;7;B;R;P");

        gridPaneUAllPhase.setVisible(true);
        gridPaneUPhaseA.setVisible(false);
        gridPaneUPhaseB.setVisible(false);

        gridPaneFAllPhase.setVisible(false);
        gridPaneFPhaseA.setVisible(false);
        gridPaneFPhaseB.setVisible(false);

        gridPanesEnergyAndPhase = Arrays.asList(
                gridPaneUAllPhase,
                gridPaneUPhaseA,
                gridPaneUPhaseB,
                gridPaneFAllPhase,
                gridPaneFPhaseA,
                gridPaneFPhaseB
        );
        stackPaneForGridPane.getChildren().addAll(gridPanesEnergyAndPhase);
    }

    private void removeOldGrPnAndSetIDGridPanesForRPMns() {
        stackPaneForGridPane.getChildren().clear();

        gridPaneUAllPhase = new GridPane();
        gridPaneUPhaseA = new GridPane();
        gridPaneUPhaseB = new GridPane();

        gridPaneFAllPhase = new GridPane();
        gridPaneFPhaseA = new GridPane();
        gridPaneFPhaseB = new GridPane();

        gridPaneUAllPhase.setId("U;7;H;R;N");
        gridPaneUPhaseA.setId("U;7;A;R;N");
        gridPaneUPhaseB.setId("U;7;B;R;N");

        gridPaneFAllPhase.setId("F;7;H;R;N");
        gridPaneFPhaseA.setId("F;7;A;R;N");
        gridPaneFPhaseB.setId("F;7;B;R;N");

        gridPaneUAllPhase.setVisible(true);
        gridPaneUPhaseA.setVisible(false);
        gridPaneUPhaseB.setVisible(false);

        gridPaneFAllPhase.setVisible(false);
        gridPaneFPhaseA.setVisible(false);
        gridPaneFPhaseB.setVisible(false);

        gridPanesEnergyAndPhase = Arrays.asList(
                gridPaneUAllPhase,
                gridPaneUPhaseA,
                gridPaneUPhaseB,
                gridPaneFAllPhase,
                gridPaneFPhaseA,
                gridPaneFPhaseB
        );
        stackPaneForGridPane.getChildren().addAll(gridPanesEnergyAndPhase);
    }

    //Задаёт GridPane необходимую величину
    private void creadteGridPane() {
        for (GridPane gridPane : gridPanesEnergyAndPhase) {
            gridPane.setGridLinesVisible(true);

            for (int i = 0; i < current.size() + 1; i++) {
                gridPane.getColumnConstraints().add(new ColumnConstraints(50));
            }

            for (int j = 0; j < powerFactor.size() + 1; j++) {
                gridPane.getRowConstraints().add(new RowConstraints(23));
            }
        }
    }

    //Добавляет соответствующий checkBox в соответствующий узел
    private void addCheckBoxesInGridPane() {
        CheckBox checkBox;
        for (GridPane gridPane : gridPanesEnergyAndPhase) {

            for (int x = 0; x < current.size(); x++) {
                for (int y = 0; y < powerFactor.size(); y++) {

                    checkBox = new CheckBox();
                    checkBox.setId(gridPane.getId() + ";" + current.get(x) + ";" + powerFactor.get(y));

                    GridPane.setColumnIndex(checkBox, x + 1);
                    GridPane.setRowIndex(checkBox, y + 1);
                    GridPane.setHalignment(checkBox, HPos.CENTER);
                    GridPane.setValignment(checkBox, VPos.CENTER);

                    gridPane.getChildren().add(checkBox);
                }
            }
        }
    }

    //Устанавливает значение true/false checBox'у в зависимости от того есть ли эта команта в списке выбранных
    private void selectCheckBoxesInGridPane(List<Commands> inflListForCollum) {
        if (!inflListForCollum.isEmpty()) {
            char[] charIdTestPoint;

            for (Commands command : inflListForCollum) {
                if (command instanceof ErrorCommand) {
                    charIdTestPoint = ((ErrorCommand) command).getId().toCharArray();

                    if (charIdTestPoint[0] == 'U') {

                        if (charIdTestPoint[4] == 'H') {
                            for (Node node : gridPaneUAllPhase.getChildren()) {
                                if (node != null) {
                                    if (((ErrorCommand) command).getId().equals(node.getId())) {
                                        ((CheckBox) node).setSelected(true);
                                        break;
                                    }
                                }
                            }
                        }

                        if (charIdTestPoint[4] == 'A') {
                            for (Node node : gridPaneUPhaseA.getChildren()) {
                                if (node != null) {
                                    if (((ErrorCommand) command).getId().equals(node.getId())) {
                                        ((CheckBox) node).setSelected(true);
                                        break;
                                    }
                                }
                            }
                        }

                        if (charIdTestPoint[4] == 'B') {
                            for (Node node : gridPaneUPhaseB.getChildren()) {
                                if (node != null) {
                                    if (((ErrorCommand) command).getId().equals(node.getId())) {
                                        ((CheckBox) node).setSelected(true);
                                        break;
                                    }
                                }
                            }
                        }

                    } else if (charIdTestPoint[0] == 'F') {

                        if (charIdTestPoint[4] == 'H') {
                            for (Node node : gridPaneFAllPhase.getChildren()) {
                                if (node != null) {
                                    if (((ErrorCommand) command).getId().equals(node.getId())) {
                                        ((CheckBox) node).setSelected(true);
                                        break;
                                    }
                                }
                            }
                        }

                        if (charIdTestPoint[4] == 'A') {
                            for (Node node : gridPaneFPhaseA.getChildren()) {
                                if (node != null) {
                                    if (((ErrorCommand) command).getId().equals(node.getId())) {
                                        ((CheckBox) node).setSelected(true);
                                        break;
                                    }
                                }
                            }
                        }

                        if (charIdTestPoint[4] == 'B') {
                            for (Node node : gridPaneFPhaseB.getChildren()) {
                                if (node != null) {
                                    if (((ErrorCommand) command).getId().equals(node.getId())) {
                                        ((CheckBox) node).setSelected(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Устанавливает слушателя для CheckBox'ов в выбранной сетке
    private void setListenerForChecBoxInGridPane() {
        for (GridPane gridPane : gridPanesEnergyAndPhase) {
            for (Node node : gridPane.getChildren()) {
                try {
                    CheckBox checkBox = (CheckBox) node;
                    checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if (newValue) {
                                addTestPointInMethodic(checkBox.getId());

                            } else {
                                deleteTestPointOfMethodic(checkBox.getId());
                            }
                        }
                    });
                }catch (ClassCastException ignore) {}
            }
        }
    }

    //Добавляет тестовую точку в методику
    private void addTestPointInMethodic(String testPoint) {
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

                if (iABC.equals("H")) {
                    for (double influenceUproc : influenceUprocAllPhaseAPPls) {
                        inflListForCollumAPPls.add(new ErrorCommand(false,"", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 0));
                    }

                } else if (iABC.equals("A")) {
                    for (double influenceUproc : influenceUprocPhaseAAPPls) {
                        inflListForCollumAPPls.add(new ErrorCommand(false, "A; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 0));
                    }

                } else if (iABC.equals("B")) {
                    for (double influenceUproc : influenceUprocPhaseBAPPls) {
                        inflListForCollumAPPls.add(new ErrorCommand(false, "B; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 0));
                    }
                }

            } else if (influenceUorF.equals("F")) {

                if (iABC.equals("H")) {
                    for (double influenceUproc : influenceFprocAllPhaseAPPls) {
                        inflListForCollumAPPls.add(new ErrorCommand(false, "", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 0));
                    }

                } else if (iABC.equals("A")) {
                    for (double influenceUproc : influenceFprocPhaseAAPPls) {
                        inflListForCollumAPPls.add(new ErrorCommand(false, "A; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 0));
                    }

                } else if (iABC.equals("B")) {
                    for (double influenceUproc : influenceFprocPhaseBAPPls) {
                        inflListForCollumAPPls.add(new ErrorCommand(false, "B; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 0));
                    }
                }
            }

        //Добавление точек по влиянию AP-
        } else if (energyType.equals("A") && currentDirection.equals("N")) {

            if (influenceUorF.equals("U")) {

                if (iABC.equals("H")) {
                    for (double influenceUproc : influenceUprocAllPhaseAPMns) {
                        inflListForCollumAPMns.add(new ErrorCommand(false, "", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 1));
                    }

                } else if (iABC.equals("A")) {
                    for (double influenceUproc : influenceUprocPhaseAAPMns) {
                        inflListForCollumAPMns.add(new ErrorCommand(false, "A; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 1));
                    }

                } else if (iABC.equals("B")) {
                    for (double influenceUproc : influenceUprocPhaseBAPMns) {
                        inflListForCollumAPMns.add(new ErrorCommand(false, "B; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 1));
                    }
                }

            } else if (influenceUorF.equals("F")) {

                if (iABC.equals("H")) {
                    for (double influenceUproc : influenceFprocAllPhaseAPMns) {
                        inflListForCollumAPMns.add(new ErrorCommand(false, "", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 1));
                    }

                } else if (iABC.equals("A")) {
                    for (double influenceUproc : influenceFprocPhaseAAPMns) {
                        inflListForCollumAPMns.add(new ErrorCommand(false, "A; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 1));
                    }

                } else if (iABC.equals("B")) {
                    for (double influenceUproc : influenceFprocPhaseBAPMns) {
                        inflListForCollumAPMns.add(new ErrorCommand(false, "B; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 1));
                    }
                }
            }

        //Добавление точек по влиянию RP+
        } else if (energyType.equals("R") && currentDirection.equals("P")) {

            if (influenceUorF.equals("U")) {

                if (iABC.equals("H")) {
                    for (double influenceUproc : influenceUprocAllPhaseRPPls) {
                        inflListForCollumRPPls.add(new ErrorCommand(false, "", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 2));
                    }

                } else if (iABC.equals("A")) {
                    for (double influenceUproc : influenceUprocPhaseARPPls) {
                        inflListForCollumRPPls.add(new ErrorCommand(false, "A; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 2));
                    }

                } else if (iABC.equals("B")) {
                    for (double influenceUproc : influenceUprocPhaseBRPPls) {
                        inflListForCollumRPPls.add(new ErrorCommand(false, "B; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 2));
                    }
                }
            } else if (influenceUorF.equals("F")) {

                if (iABC.equals("H")) {
                    for (double influenceUproc : influenceFprocAllPhaseRPPls) {
                        inflListForCollumRPPls.add(new ErrorCommand(false, "", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 2));
                    }

                } else if (iABC.equals("A")) {
                    for (double influenceUproc : influenceFprocPhaseARPPls) {
                        inflListForCollumRPPls.add(new ErrorCommand(false, "A; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 2));
                    }

                } else if (iABC.equals("B")) {
                    for (double influenceUproc : influenceFprocPhaseBRPPls) {
                        inflListForCollumRPPls.add(new ErrorCommand(false, "B; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                0, percent, iABC, powerFactor, 2));
                    }
                }
            }


        //Добавление точек по влиянию RP-
        } else if (energyType.equals("R") && currentDirection.equals("N")) {

            if (influenceUorF.equals("U")) {

                if (iABC.equals("H")) {
                    for (double influenceUproc : influenceUprocAllPhaseRPMns) {
                        inflListForCollumRPMns.add(new ErrorCommand(false, "", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 3));
                    }

                } else if (iABC.equals("A")) {
                    for (double influenceUproc : influenceUprocPhaseARPMns) {
                        inflListForCollumRPMns.add(new ErrorCommand(false, "A; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 3));
                    }

                } else if (iABC.equals("B")) {
                    for (double influenceUproc : influenceUprocPhaseBRPMns) {
                        inflListForCollumRPMns.add(new ErrorCommand(false, "B; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 3));
                    }
                }
            } else if (influenceUorF.equals("F")) {

                if (iABC.equals("H")) {
                    for (double influenceUproc : influenceFprocAllPhaseRPMns) {
                        inflListForCollumRPMns.add(new ErrorCommand(false, "", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 3));
                    }

                } else if (iABC.equals("A")) {
                    for (double influenceUproc : influenceFprocPhaseARPMns) {
                        inflListForCollumRPMns.add(new ErrorCommand(false, "A; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 3));
                    }

                } else if (iABC.equals("B")) {
                    for (double influenceUproc : influenceFprocPhaseBRPMns) {
                        inflListForCollumRPMns.add(new ErrorCommand(false, "B; ", influenceUorF, testPoint, phase, current, influenceUproc,
                                1, percent, iABC, powerFactor, 3));
                    }
                }
            }
        }
    }

    //Удаляет точку по айди чекбокса
    private void deleteTestPointOfMethodic(String idCheckBox) {
        String[] point = idCheckBox.split(";");

        if (point[3].equals("A") && point[4].equals("P")) {

            for (int i = 0; i < inflListForCollumAPPls.size(); i++) {
                if (((ErrorCommand) inflListForCollumAPPls.get(i)).getId().equals(idCheckBox)) {
                    inflListForCollumAPPls.remove(i);
                    i--;
                }
            }

        } else if (point[3].equals("A") && point[4].equals("N")) {

            for (int i = 0; i < inflListForCollumAPMns.size(); i++) {
                if (((ErrorCommand) inflListForCollumAPMns.get(i)).getId().equals(idCheckBox)) {
                    inflListForCollumAPMns.remove(i);
                    i--;
                }
            }

        } else if (point[3].equals("R") && point[4].equals("P")) {

            for (int i = 0; i < inflListForCollumRPPls.size(); i++) {
                if (((ErrorCommand) inflListForCollumRPPls.get(i)).getId().equals(idCheckBox)) {
                    inflListForCollumRPPls.remove(i);
                    i--;
                }
            }

        } else if (point[3].equals("R") && point[4].equals("N")) {

            for (int i = 0; i < inflListForCollumRPMns.size(); i++) {
                if (((ErrorCommand) inflListForCollumRPMns.get(i)).getId().equals(idCheckBox)) {
                    inflListForCollumRPMns.remove(i);
                    i--;
                }
            }
        }
    }

    //=========================================================================================================//
    //==================================== Инициализация TableView  ===========================================//
    //=========================================================================================================//
    private void initTableView() {
        List<TableColumn<Commands, String>> collumnListAPPls = Arrays.asList(
                loadCurrTabColAPPls,
                eMaxTabColAPPls,
                eMinTabColAPPls,
                amountImplTabColAPPls,
                amountMeasTabColAPPls
        );

        List<TableColumn<Commands, String>> collumnListAPMns = Arrays.asList(
                loadCurrTabColAPMns,
                eMaxTabColAPMns,
                eMinTabColAPMns,
                amountImplTabColAPMns,
                amountMeasTabColAPMns
        );

        List<TableColumn<Commands, String>> collumnListRPPls = Arrays.asList(
                loadCurrTabColRPPls,
                eMaxTabColRPPls,
                eMinTabColRPPls,
                amountImplTabColRPPls,
                amountMeasTabColRPPls
        );

        List<TableColumn<Commands, String>> collumnListRPMns = Arrays.asList(
                loadCurrTabColRPMns,
                eMaxTabColRPMns,
                eMinTabColRPMns,
                amountImplTabColRPMns,
                amountMeasTabColRPMns
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
            mapTableColumn.get(i).get(4).setCellValueFactory(new PropertyValueFactory<>("countResult"));

            //Выставляем отображение информации в колонке "по центру"
            mapTableColumn.get(i).get(1).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(2).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(3).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(4).setStyle( "-fx-alignment: CENTER;");

            //Устанавливаем возможность редактирования информации в колонке
            mapTableColumn.get(i).get(1).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(2).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(3).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(4).setCellFactory(TextFieldTableCell.forTableColumn());

            //Действие при изменении информации в колонке
            mapTableColumn.get(i).get(1).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                try {
                    Float.parseFloat(newImpulseValue);
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные\nЗначение поля должно быть десятичным");
                    event.getTableView().refresh();
                    return;
                }

                command.setEmax(newImpulseValue);

            });

            mapTableColumn.get(i).get(2).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                try {
                    Float.parseFloat(newImpulseValue);
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные\nЗначение поля должно быть десятичным");
                    event.getTableView().refresh();
                    return;
                }

                command.setEmin(newImpulseValue);

            });

            mapTableColumn.get(i).get(3).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                try {
                    Float.parseFloat(newImpulseValue);
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные\nЗначение поля должно быть десятичным");
                    event.getTableView().refresh();
                    return;
                }

                command.setPulse(newImpulseValue);

            });

            mapTableColumn.get(i).get(4).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                try {
                    Float.parseFloat(newImpulseValue);
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные\nЗначение поля должно быть десятичным");
                    event.getTableView().refresh();
                    return;
                }

                ((ErrorCommand) command).setCountResult(newImpulseValue);

            });
        }

        Callback<TableView<Commands>, TableRow<Commands>> dragAndRow = new Callback<TableView<Commands>, TableRow<Commands>>() {
            @Override
            public TableRow<Commands> call(TableView<Commands> param) {
                TableRow<Commands> row = new TableRow<>();

                row.setOnDragDetected(event -> {
                    if (! row.isEmpty()) {
                        Integer index = row.getIndex();
                        Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                        db.setDragView(row.snapshot(null, null));
                        ClipboardContent cc = new ClipboardContent();
                        cc.put(DataFormat.PLAIN_TEXT, index);
                        db.setContent(cc);
                        event.consume();
                    }
                });

                row.setOnDragOver(event -> {
                    Dragboard db = event.getDragboard();
                    if (db.hasContent(DataFormat.PLAIN_TEXT)) {
                        if (row.getIndex() != (Integer) db.getContent(DataFormat.PLAIN_TEXT)) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                            event.consume();
                        }
                    }

                });

                row.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    if (db.hasContent(DataFormat.PLAIN_TEXT)) {
                        int draggedIndex = (Integer) db.getContent(DataFormat.PLAIN_TEXT);
                        Commands draggedPerson = param.getItems().remove(draggedIndex);

                        int dropIndex ;

                        if (row.isEmpty()) {
                            dropIndex = param.getItems().size() ;
                        } else {
                            dropIndex = row.getIndex();
                        }

                        param.getItems().add(dropIndex, draggedPerson);

                        event.setDropCompleted(true);
                        param.getSelectionModel().select(dropIndex);
                        event.consume();
                    }
                });
                return row;
            }
        };


        Comparator<String> comparatorForCommands = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                //115.0%Un; 0.8L; 1.0 Ib
                //A; 15.0%Un; 0.8L; 0.5 Ib
                String[] arrO1 = o1.split(";");
                String[] arrO2 = o2.split(";");

                if (arrO1.length == 3 && arrO2.length != 3) {
                    return -1;
                } else if (arrO1.length != 3 && arrO2.length == 3) {
                    return 1;
                } else if (arrO1.length == 4 && arrO2.length == 2) {
                    return -1;
                } else if (arrO1.length == 2 && arrO2.length == 4) {
                    return 1;
                } else if (arrO1.length == 3 && arrO2.length == 3) {
                    if (arrO1[0].contains("U") && arrO2[0].contains("F")) {
                        return -1;
                    } else if (arrO1[0].contains("F") && arrO2[0].contains("U")) {
                        return 1;
                    } else if (arrO1[0].contains("U") && arrO2[0].contains("U") ||
                            arrO1[0].contains("F") && arrO2[0].contains("F")) {

                        float proc1 = Float.parseFloat(arrO1[0].substring(0, arrO1[0].length() - 3));
                        float proc2 = Float.parseFloat(arrO2[0].substring(0, arrO2[0].length() - 3));

                        if (proc1 > proc2) {
                            return -1;
                        } else if (proc1 < proc2) {
                            return 1;
                        } else if (proc1 == proc2) {

                            String[] curArr1 = arrO1[2].trim().split(" ");
                            String[] curArr2 = arrO2[2].trim().split(" ");

                            if (curArr1[1].contains("Imax") && curArr2[1].contains("Ib")) {
                                return -1;
                            } else if (curArr1[1].contains("Ib") && curArr2[1].contains("Imax")) {
                                return 1;
                            } else if (curArr1[1].contains("Imax") && curArr2[1].contains("Imax") ||
                                    curArr1[1].contains("Ib") && curArr2[1].contains("Ib")) {

                                float current1 = Float.parseFloat(curArr1[0]);
                                float current2 = Float.parseFloat(curArr2[0]);

                                if (current1 > current2) {
                                    return -1;
                                } else if (current1 < current2) {
                                    return 1;
                                } else if (current1 == current2) {

                                    String pf1 = arrO1[1].trim();
                                    String pf2 = arrO2[1].trim();

                                    if ((!pf1.contains("L") && !pf1.contains("C")) && (pf2.contains("L") || pf2.contains("C"))) {
                                        return -1;
                                    } else if ((pf1.contains("L") || pf1.contains("C")) && (!pf2.contains("L") && !pf2.contains("C"))) {
                                        return 1;
                                    } else if ((!pf1.contains("L") && !pf1.contains("C")) && (!pf2.contains("L") && !pf2.contains("C"))) {

                                        float coef1 = Float.parseFloat(pf1);
                                        float coef2 = Float.parseFloat(pf2);

                                        if (coef1 > coef2) {
                                            return -1;
                                        } else if (coef1 < coef2) {
                                            return 1;
                                        } else return 0;

                                    } else if (pf1.contains("L") && pf2.contains("C")) {
                                        return -1;
                                    } else if (pf1.contains("C") && pf2.contains("L")) {
                                        return 1;
                                    } else if (pf1.contains("L") && pf2.contains("L") ||
                                            pf1.contains("C") && pf2.contains("C")) {

                                        float coef1 = Float.parseFloat(pf1.substring(0, pf1.length() - 1));
                                        float coef2 = Float.parseFloat(pf2.substring(0, pf2.length() - 1));

                                        if (coef1 > coef2) {
                                            return -1;
                                        } else if (coef1 < coef2) {
                                            return 1;
                                        } else return 0;
                                    }
                                }
                            }
                        }
                    }

                } else if (arrO1.length == 4 && arrO2.length == 4) {
                    if (arrO1[0].contains("A") && !arrO2[0].contains("A")) {
                        return -1;
                    } else if (!arrO1[0].contains("A") && arrO2[0].contains("A")) {
                        return 1;
                    } else if (arrO1[0].contains("B") && arrO2[0].contains("C")) {
                        return -1;
                    } else if (arrO1[0].contains("C") && arrO2[0].contains("B")) {
                        return 1;
                    } else if (arrO1[0].contains("A") && arrO2[0].contains("A") ||
                            arrO1[0].contains("B") && arrO2[0].contains("B") ||
                            arrO1[0].contains("C") && arrO2[0].contains("C")) {

                        String[] newArrO1 = o1.substring(3).split(";");
                        String[] newArrO2 = o2.substring(3).split(";");

                        if (newArrO1[0].contains("U") && newArrO2[0].contains("F")) {
                            return -1;
                        } else if (newArrO1[0].contains("F") && newArrO2[0].contains("U")) {
                            return 1;
                        } else if (newArrO1[0].contains("U") && newArrO2[0].contains("U") ||
                                newArrO1[0].contains("F") && newArrO2[0].contains("F")) {

                            float proc1 = Float.parseFloat(newArrO1[0].substring(0, newArrO1[0].length() - 3));
                            float proc2 = Float.parseFloat(newArrO2[0].substring(0, newArrO2[0].length() - 3));

                            if (proc1 > proc2) {
                                return -1;
                            } else if (proc1 < proc2) {
                                return 1;
                            } else if (proc1 == proc2) {

                                String[] curArr1 = newArrO1[2].trim().split(" ");
                                String[] curArr2 = newArrO2[2].trim().split(" ");

                                if (curArr1[1].contains("Imax") && curArr2[1].contains("Ib")) {
                                    return -1;
                                } else if (curArr1[1].contains("Ib") && curArr2[1].contains("Imax")) {
                                    return 1;
                                } else if (curArr1[1].contains("Imax") && curArr2[1].contains("Imax") ||
                                        curArr1[1].contains("Ib") && curArr2[1].contains("Ib")) {

                                    float current1 = Float.parseFloat(curArr1[0]);
                                    float current2 = Float.parseFloat(curArr2[0]);

                                    if (current1 > current2) {
                                        return -1;
                                    } else if (current1 < current2) {
                                        return 1;
                                    } else if (current1 == current2) {

                                        String pf1 = newArrO1[1].trim();
                                        String pf2 = newArrO2[1].trim();

                                        if ((!pf1.contains("L") && !pf1.contains("C")) && (pf2.contains("L") || pf2.contains("C"))) {
                                            return -1;
                                        } else if ((pf1.contains("L") || pf1.contains("C")) && (!pf2.contains("L") && !pf2.contains("C"))) {
                                            return 1;
                                        } else if ((!pf1.contains("L") && !pf1.contains("C")) && (!pf2.contains("L") && !pf2.contains("C"))) {

                                            float coef1 = Float.parseFloat(pf1);
                                            float coef2 = Float.parseFloat(pf2);

                                            if (coef1 > coef2) {
                                                return -1;
                                            } else if (coef1 < coef2) {
                                                return 1;
                                            } else return 0;

                                        } else if (pf1.contains("L") && pf2.contains("C")) {
                                            return -1;
                                        } else if (pf1.contains("C") && pf2.contains("L")) {
                                            return 1;
                                        } else if (pf1.contains("L") && pf2.contains("L") ||
                                                pf1.contains("C") && pf2.contains("C")) {

                                            float coef1 = Float.parseFloat(pf1.substring(0, pf1.length() - 1));
                                            float coef2 = Float.parseFloat(pf2.substring(0, pf2.length() - 1));

                                            if (coef1 > coef2) {
                                                return -1;
                                            } else if (coef1 < coef2) {
                                                return 1;
                                            } else return 0;
                                        }
                                    }
                                }
                            }
                        }
                    }

                } else if (arrO1.length == 2 && arrO2.length == 2) {
                    String imb1 = arrO1[0];
                    String imb2 = arrO2[0];

                    if (imb1.equals("A") && !imb2.equals("A")) {
                        return -1;
                    } else if (!imb1.equals("A") && imb2.equals("A")) {
                        return 1;
                    } else if (imb1.equals("B") && !imb2.equals("B")) {
                        return -1;
                    } else if (!imb1.equals("B") && imb2.equals("B")) {
                        return 1;
                    } else if (imb1.equals("C") && !imb2.equals("C")) {
                        return -1;
                    } else if (!imb1.equals("C") && imb2.equals("C")) {
                        return 1;
                    } else {
                        return imb1.compareTo(imb2);
                    }
                }

                return 0;
            }
        };

        //Устанавливаю компаратор
        loadCurrTabColAPPls.setComparator(comparatorForCommands);
        loadCurrTabColAPMns.setComparator(comparatorForCommands);
        loadCurrTabColRPPls.setComparator(comparatorForCommands);
        loadCurrTabColRPMns.setComparator(comparatorForCommands);

        eMaxTabColAPPls.setSortable(false);
        eMinTabColAPPls.setSortable(false);
        amountImplTabColAPPls.setSortable(false);
        amountMeasTabColAPPls.setSortable(false);

        eMaxTabColAPMns.setSortable(false);
        eMinTabColAPMns.setSortable(false);
        amountImplTabColAPMns.setSortable(false);
        amountMeasTabColAPMns.setSortable(false);

        eMaxTabColRPPls.setSortable(false);
        eMinTabColRPPls.setSortable(false);
        amountImplTabColRPPls.setSortable(false);
        amountMeasTabColRPPls.setSortable(false);

        eMaxTabColRPMns.setSortable(false);
        eMinTabColRPMns.setSortable(false);
        amountImplTabColRPMns.setSortable(false);
        amountMeasTabColRPMns.setSortable(false);

        viewPointTableAPPls.setEditable(true);
        viewPointTableAPMns.setEditable(true);
        viewPointTableRPPls.setEditable(true);
        viewPointTableRPMns.setEditable(true);

        viewPointTableAPPls.setRowFactory(dragAndRow);
        viewPointTableAPMns.setRowFactory(dragAndRow);
        viewPointTableRPPls.setRowFactory(dragAndRow);
        viewPointTableRPMns.setRowFactory(dragAndRow);

        viewPointTableAPPls.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableAPMns.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableRPPls.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableRPMns.setPlaceholder(new Label("Не выбрано ни одной точки"));

        viewPointTableAPPls.setItems(inflListForCollumAPPls);
        viewPointTableAPMns.setItems(inflListForCollumAPMns);
        viewPointTableRPPls.setItems(inflListForCollumRPPls);
        viewPointTableRPMns.setItems(inflListForCollumRPMns);

        String cssAdress = getClass().getClassLoader().getResource("styleCSS/tableViewStyle.css").toString();
        if (cssAdress != null) {
            viewPointTableAPPls.getStylesheets().add(cssAdress);
            viewPointTableAPMns.getStylesheets().add(cssAdress);
            viewPointTableRPPls.getStylesheets().add(cssAdress);
            viewPointTableRPMns.getStylesheets().add(cssAdress);
        }
    }
}
