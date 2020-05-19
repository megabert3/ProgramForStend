package org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicNameController;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.Frame;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;
import org.taipit.stend.model.metodics.MetodicsForTest;

import java.io.IOException;
import java.util.*;

public class AddEditPointsOnePhaseStendFrameController implements  Frame {

    private AddEditPointsOnePhaseStendFrameController addEditPointsOnePhaseStendFrameController = this;

    private MetodicsForTest metodicsForTest = MetodicsForTest.getMetodicsForTestInstance();

    private InfluencePointsOnePhaseStendFrame influencePointsOnePhaseStendFrame;

    private MethodicNameController methodicNameController;

    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    private MethodicForOnePhaseStend methodicForOnePhaseStend;

    private boolean bindParameters;

    //Это окно вызвано кнопкой редактировать?
    private boolean edit;

    //Значения коэффициента мощности
    private List<String> powerFactor;

    //Значения выставленного тока
    private List<String> current;

    private List<GridPane> gridPanesEnergyAndPhase = new ArrayList<>();

    //Лист с точками общая методика
    private ObservableList<Commands> testListForCollumAPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumAPMns = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumRPPls = FXCollections.observableArrayList(new ArrayList<>());
    private ObservableList<Commands> testListForCollumRPMns = FXCollections.observableArrayList(new ArrayList<>());

    @FXML
    private AnchorPane mainAnchorPane;

    private ScrollPane mainScrollPane = new ScrollPane();

    private StackPane stackPaneForGridPane = new StackPane();

    private ScrollPane scrollPaneForCurrent = new ScrollPane();
    private ScrollPane scrollPaneForPowerFactor = new ScrollPane();
    private GridPane gridPaneForCurrent = new GridPane();
    private GridPane gridPaneForPowerFactor = new GridPane();

    private Pane fillSquare;
    private Button btnAddDeleteTestPoints = new Button();

    @FXML
    private ToggleButton parametersBtn;

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
    //-------------------------------------------------------
    //Данный блок отвечает за сетку выбора точки.
    //Активная энергия в прямом направлении, Все фазы и отдельно А В С
    private GridPane gridPaneOnePhaseAPPlus = new GridPane();
    private GridPane gridPanePhaseAAPPlus = new GridPane();
    private GridPane gridPanePhaseBAPPlus = new GridPane();

    //Активная энергия в обратном направлении, Все фазы и отдельно А В С
    private GridPane gridPaneOnePhaseAPMinus = new GridPane();
    private GridPane gridPanePhaseAAPMinus = new GridPane();
    private GridPane gridPanePhaseBAPMinus = new GridPane();

    //Реактивная энергия в прямом направлении, Все фазы и отдельно А В С
    private GridPane gridPaneOnePhaseRPPlus = new GridPane();
    private GridPane gridPanePhaseARPPlus = new GridPane();
    private GridPane gridPanePhaseBRPPlus = new GridPane();

    //Реактивная энергия в обратном направлении, Все фазы и отдельно А В С
    private GridPane gridPaneOnePhaseRPMinus = new GridPane();
    private GridPane gridPanePhaseARPMinus = new GridPane();
    private GridPane gridPanePhaseBRPMinus = new GridPane();

    //-------------------------------------------------------

    @FXML
    private ToggleButton onePhaseBtn;

    @FXML
    private ToggleButton APhaseBtn;

    @FXML
    private ToggleButton BPhaseBtn;

    @FXML
    private ToggleButton APPlus;

    @FXML
    private ToggleButton APMinus;

    @FXML
    private ToggleButton RPPlus;

    @FXML
    private ToggleButton RPMinus;

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
    private TextField  txtFieldEngConstAPPls;

    @FXML
    private TextField txtFieldConstTimeAPPls;

    @FXML
    private TextField txtFieldConsProcUAPPls;

    @FXML
    private TextField txtFieldConsProcIAPPls;

    private ToggleGroup radioBtnGroupAPPls = new ToggleGroup();

    @FXML
    private RadioButton radBtnConstTimeAPPls;

    @FXML
    private RadioButton radBtnConstEnergyAPPls;

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
    private ComboBox<String> ChcBxRTCErrAPPls;

    @FXML
    private TextField txtFldRTCTimeMshAPPls;

    @FXML
    private TextField txtFldRTCFrqAPPls;

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
    private ComboBox<String> ChcBxRTCErrAPMns;

    @FXML
    private TextField txtFldRTCTimeMshAPMns;

    @FXML
    private TextField txtFldRTCFrqAPMns;

    @FXML
    private Pane paneConstAPMns;

    @FXML
    private TextField txtFieldConsErAPMns;

    @FXML
    private TextField  txtFieldEngConstAPMns;

    @FXML
    private TextField txtFieldConstTimeAPMns;

    @FXML
    private TextField txtFieldConsProcUAPMns;

    @FXML
    private TextField txtFieldConsProcIAPMns;

    private ToggleGroup radioBtnGroupAPMns = new ToggleGroup();

    @FXML
    private RadioButton radBtnConstTimeAPMns;

    @FXML
    private RadioButton radBtnConstEnergyAPMns;

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
    private ComboBox<String> ChcBxRTCErrRPPls;

    @FXML
    private TextField txtFldRTCTimeMshRPPls;

    @FXML
    private TextField txtFldRTCFrqRPPls;

    @FXML
    private Pane paneConstRPPls;

    @FXML
    private TextField txtFieldConsErRPPls;

    @FXML
    private TextField  txtFieldEngConstRPPls;

    @FXML
    private TextField txtFieldConstTimeRPPls;

    @FXML
    private TextField txtFieldConsProcURPPls;

    @FXML
    private TextField txtFieldConsProcIRPPls;

    private ToggleGroup radioBtnGroupRPPls = new ToggleGroup();

    @FXML
    private RadioButton radBtnConstTimeRPPls;

    @FXML
    private RadioButton radBtnConstEnergyRPPls;

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
    private TextField txtFldRTCFrqRPMns;

    @FXML
    private ToggleButton addTglBtnRTCRPMns;

    @FXML
    private Pane paneConstRPMns;

    @FXML
    private TextField txtFieldConsErRPMns;

    @FXML
    private TextField  txtFieldEngConstRPMns;

    @FXML
    private TextField txtFieldConstTimeRPMns;

    @FXML
    private TextField txtFieldConsProcURPMns;

    @FXML
    private TextField txtFieldConsProcIRPMns;

    private ToggleGroup radioBtnGroupRPMns = new ToggleGroup();

    @FXML
    private RadioButton radBtnConstTimeRPMns;

    @FXML
    private RadioButton radBtnConstEnergyRPMns;

    @FXML
    private ComboBox<String> ChcBxRTCErrRPMns;

    @FXML
    private ToggleButton addTglBtnConstRPMns;
    //---------------------------------------------------------------------

    @FXML
    private TextField metodicNameTxtFld;

    @FXML
    private Button influenceBtn;

    //Устанавливает имя методики полученное с другого окна
    public void setTextFielMethodicName() {
        metodicNameTxtFld.setText(methodicForOnePhaseStend.getMetodicName());
    }

    @FXML
    void initialize() {

        current = Arrays.asList(ConsoleHelper.properties.getProperty("currentForMethodicPane").split(", "));
        powerFactor = Arrays.asList(ConsoleHelper.properties.getProperty("powerFactorForMetodicPane").split(", "));

        setIdGridPanes();

        createGridPaneAndsetCheckBoxes();

        initMainScrollPane();

        initScrolPaneForCurrentAndPowerFactor();

        createScrollPanesForGridPane();

        btnAddDeleteTestPoints.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/addDeleteTestPointInGridPaneFrame.fxml"));
                    fxmlLoader.load();

                    AddDeleteTestPointInGridPaneController addDeleteTestPointInGridPaneController = fxmlLoader.getController();
                    addDeleteTestPointInGridPaneController.setAddEditPointsOnePhaseStendFrameController(addEditPointsOnePhaseStendFrameController);

                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setTitle("Добавление точек испытаний");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        initTableView();

        radBtnConstEnergyAPPls.setToggleGroup(radioBtnGroupAPPls);
        radBtnConstTimeAPPls.setToggleGroup(radioBtnGroupAPPls);
        radBtnConstTimeAPPls.setSelected(true);

        radBtnConstEnergyAPMns.setToggleGroup(radioBtnGroupAPMns);
        radBtnConstTimeAPMns.setToggleGroup(radioBtnGroupAPMns);
        radBtnConstTimeAPMns.setSelected(true);

        radBtnConstEnergyRPPls.setToggleGroup(radioBtnGroupRPPls);
        radBtnConstTimeRPPls.setToggleGroup(radioBtnGroupRPPls);
        radBtnConstTimeRPPls.setSelected(true);

        radBtnConstEnergyRPMns.setToggleGroup(radioBtnGroupRPMns);
        radBtnConstTimeRPMns.setToggleGroup(radioBtnGroupRPMns);
        radBtnConstTimeRPMns.setSelected(true);

        APPlus.setSelected(true);
        onePhaseBtn.setSelected(true);
        APPlusCRPSTA.setSelected(true);
        APPlsPane.toFront();
        paneCRPAPPls.toFront();

        initCoiseBoxParamForRTC();
        gridPaneOnePhaseAPPlus.toFront();
        viewPointTableAPPls.toFront();
    }

//================Всё чтоё связанно с инициализацией грид пайн ======================
    private void initMainScrollPane() {
        mainScrollPane.setPrefHeight(230);
        mainScrollPane.setPrefWidth(643);
        mainScrollPane.setLayoutX(135);
        mainScrollPane.setLayoutY(175);

        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        mainScrollPane.setStyle("-fx-background: #6A6A6A;");

        mainScrollPane.setContent(stackPaneForGridPane);

        mainAnchorPane.getChildren().add(mainScrollPane);

        stackPaneForGridPane.getChildren().addAll(gridPanesEnergyAndPhase);
    }

    private void setIdGridPanes() {
            gridPaneOnePhaseAPPlus.setId("0;H;A;P");
            gridPanePhaseAAPPlus.setId("0;A;A;P");
            gridPanePhaseBAPPlus.setId("0;B;A;P");

            gridPaneOnePhaseAPMinus.setId("0;H;A;N");
            gridPanePhaseAAPMinus.setId("0;A;A;N");
            gridPanePhaseBAPMinus.setId("0;B;A;N");

            gridPaneOnePhaseRPPlus.setId("7;H;R;P");
            gridPanePhaseARPPlus.setId("7;A;R;P");
            gridPanePhaseBRPPlus.setId("7;B;R;P");

            gridPaneOnePhaseRPMinus.setId("7;H;R;N");
            gridPanePhaseARPMinus.setId("7;A;R;N");
            gridPanePhaseBRPMinus.setId("7;B;R;N");


        gridPanesEnergyAndPhase = Arrays.asList(
                gridPaneOnePhaseAPPlus,
                gridPanePhaseAAPPlus,
                gridPanePhaseBAPPlus,

                gridPaneOnePhaseAPMinus,
                gridPanePhaseAAPMinus,
                gridPanePhaseBAPMinus,

                gridPaneOnePhaseRPPlus,
                gridPanePhaseARPPlus,
                gridPanePhaseBRPPlus,

                gridPaneOnePhaseRPMinus,
                gridPanePhaseARPMinus,
                gridPanePhaseBRPMinus
        );
    }

    private void createGridPaneAndsetCheckBoxes() {
        createRowAndColumnForGridPane();
        setCheckBoxAndLabelInGridPane();
    }

    private void createRowAndColumnForGridPane() {
        for (GridPane gridPane : gridPanesEnergyAndPhase) {
            for (int i = 0; i < current.size() + 1; i++) {
                gridPane.getColumnConstraints().add(new ColumnConstraints(50));
            }

            for (int j = 0; j < powerFactor.size() + 1; j++) {
                gridPane.getRowConstraints().add(new RowConstraints(23));
            }
        }
    }

    private void setCheckBoxAndLabelInGridPane() {
        for (GridPane gridPane : gridPanesEnergyAndPhase) {

            gridPane.setGridLinesVisible(true);
            gridPane.setStyle("-fx-background: #6A6A6A;");

            CheckBox checkBox;

            for (int x = 0; x < current.size(); x++) {
                for (int y = 0; y < powerFactor.size(); y++) {
                    //Устанавливаю CheckBox в нужную и соответствующую ячейку
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

    public void addListenerToCheckBoxes() {
        for (GridPane gridPane : gridPanesEnergyAndPhase) {
            for (Node node : gridPane.getChildren()) {
                try {
                    CheckBox checkBox = (CheckBox) node;

                    checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                        if (newVal) {
                            addTestPointInMethodic(checkBox.getId());
                        } else {
                            deleteTestPointInMethodic(checkBox.getId());
                        }
                    });
                }catch (ClassCastException | NullPointerException ignore) {}
            }
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
        scrollPaneForCurrent.setLayoutX(135);
        scrollPaneForCurrent.setLayoutY(175);

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
        scrollPaneForPowerFactor.setLayoutX(135);
        scrollPaneForPowerFactor.setLayoutY(175);

        scrollPaneForPowerFactor.setPrefHeight(mainScrollPane.getPrefHeight() - 13);
        mainAnchorPane.getChildren().add(scrollPaneForPowerFactor);

        gridPaneForPowerFactor.setPrefHeight(gridPaneOnePhaseAPPlus.getHeight());
        scrollPaneForPowerFactor.setContent(gridPaneForPowerFactor);

        //Закрывающий квадрат
        fillSquare = new Pane();
        fillSquare.setStyle("-fx-background-color: #FFC107;");
        fillSquare.setPrefHeight(23);
        fillSquare.setPrefWidth(50);
        fillSquare.setLayoutX(135);
        fillSquare.setLayoutY(175);
        mainAnchorPane.getChildren().add(fillSquare);

        btnAddDeleteTestPoints.setText("Точки");
        btnAddDeleteTestPoints.setMinHeight(0);
        btnAddDeleteTestPoints.setPrefSize(fillSquare.getPrefWidth(), fillSquare.getPrefHeight());
        btnAddDeleteTestPoints.setStyle("-fx-background-color: #FFC107;" +
                "-fx-background-insets: 0, 0 0 0 0;");

        fillSquare.getChildren().add(btnAddDeleteTestPoints);
    }

    private void initScrolPaneForCurrentAndPowerFactor() {
        gridPaneForCurrent.setPrefWidth(gridPaneOnePhaseAPPlus.getWidth());
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

    private void createScrollPanesForGridPaneWithoutSquare() {
        //Curr
        scrollPaneForCurrent.setMinHeight(0);
        scrollPaneForCurrent.setPrefHeight(24);
        scrollPaneForCurrent.setStyle("-fx-background: #FFC107;" +
                "-fx-background-insets: 0, 0 1 1 0;" +
                "-fx-background-color: #FFC107;");

        scrollPaneForCurrent.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForCurrent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForCurrent.setLayoutX(135);
        scrollPaneForCurrent.setLayoutY(175);

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
        scrollPaneForPowerFactor.setLayoutX(135);
        scrollPaneForPowerFactor.setLayoutY(175);

        scrollPaneForPowerFactor.setPrefHeight(mainScrollPane.getPrefHeight() - 13);
        mainAnchorPane.getChildren().add(scrollPaneForPowerFactor);

        gridPaneForPowerFactor.setPrefHeight(gridPaneOnePhaseAPPlus.getHeight());
        scrollPaneForPowerFactor.setContent(gridPaneForPowerFactor);

        fillSquare.toFront();
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

    //======================= Всё что связано с отображением выбранных точек в TableView ================================
    //Инициализирует таблицу для отображения выбранных точек
    public void initTableView() {
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

                ((ErrorCommand) command).setEmax(newImpulseValue);
            });

            mapTableColumn.get(i).get(2).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                ((ErrorCommand) command).setEmin(newImpulseValue);
            });

            mapTableColumn.get(i).get(3).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                ((ErrorCommand) command).setPulse(newImpulseValue);
            });

            mapTableColumn.get(i).get(4).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

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

        //Компараторы для точек
        Comparator<String> comparatorForCommands = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                //Общая точка
                String[] arrO1 = o1.split(";");
                String[] arrO2 = o2.split(";");

                //Токи
                String[] curArr1;
                String[] curArr2;

                //Значение тока
                float current1;
                float current2;

                //Тип мощности
                String powerFactorType1;
                String powerFactorType2;

                //Значение угла
                float powerFactor1;
                float powerFactor2;

                if (arrO1.length == 2 && arrO2.length != 2) {
                    return 1;
                } else if (arrO1.length != 2 && arrO2.length == 2) {
                    return -1;
                } else if (arrO1.length == 3 && arrO2.length == 1) {
                    return 1;
                } else if (arrO1.length == 1 && arrO2.length == 3) {
                    return -1;
                } else if (arrO1.length == 2 && arrO2.length == 2) {
                    curArr1 = arrO1[1].trim().split(" ");
                    curArr2 = arrO2[1].trim().split(" ");

                    if (curArr1[1].equals("Imax") && curArr2[1].equals("Ib")) {
                        return 1;
                    } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Imax")) {
                        return -1;
                    } else if (curArr1[1].equals("Imax") && curArr2[1].equals("Imax")) {
                        current1 = Float.parseFloat(curArr1[0]);
                        current2 = Float.parseFloat(curArr2[0]);

                        if (current1 > current2) {
                            return 1;
                        } else if (current1 < current2) {
                            return -1;
                        } else if (current1 == current2) {
                            powerFactorType1 = arrO1[0];
                            powerFactorType2 = arrO2[0];

                            if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                return 1;
                            } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                return -1;
                            } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                powerFactor1 = Float.parseFloat(powerFactorType1);
                                powerFactor2 = Float.parseFloat(powerFactorType2);

                                if (powerFactor1 > powerFactor2) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                powerFactor1 = Float.parseFloat(powerFactorStr1);
                                powerFactor2 = Float.parseFloat(powerFactorStr2);

                                if (powerFactor1 > powerFactor2) {
                                    return 1;
                                } else if (powerFactor1 < powerFactor2) {
                                    return -1;
                                } else if (powerFactor1 == powerFactor2) {
                                    if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }

                            } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                powerFactor1 = Float.parseFloat(powerFactorStr1);
                                powerFactor2 = Float.parseFloat(powerFactorStr2);

                                if (powerFactor1 > powerFactor2) {
                                    return 1;
                                } else if (powerFactor1 < powerFactor2) {
                                    return -1;
                                } else if (powerFactor1 == powerFactor2) {
                                    if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }
                            }
                        }
                    } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Ib")) {
                        current1 = Float.parseFloat(curArr1[0]);
                        current2 = Float.parseFloat(curArr2[0]);

                        if (current1 > current2) {
                            return 1;
                        } else if (current1 < current2) {
                            return -1;
                        } else if (current1 == current2) {
                            powerFactorType1 = arrO1[0];
                            powerFactorType2 = arrO2[0];

                            if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                return 1;
                            } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                return -1;
                            } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                powerFactor1 = Float.parseFloat(powerFactorType1);
                                powerFactor2 = Float.parseFloat(powerFactorType2);

                                if (powerFactor1 > powerFactor2) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                powerFactor1 = Float.parseFloat(powerFactorStr1);
                                powerFactor2 = Float.parseFloat(powerFactorStr2);

                                if (powerFactor1 > powerFactor2) {
                                    return 1;
                                } else if (powerFactor1 < powerFactor2) {
                                    return -1;
                                } else if (powerFactor1 == powerFactor2) {
                                    if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }

                            } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                powerFactor1 = Float.parseFloat(powerFactorStr1);
                                powerFactor2 = Float.parseFloat(powerFactorStr2);

                                if (powerFactor1 > powerFactor2) {
                                    return 1;
                                } else if (powerFactor1 < powerFactor2) {
                                    return -1;
                                } else if (powerFactor1 == powerFactor2) {
                                    if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                        return 1;
                                    } else {
                                        return -1;
                                    }
                                }
                            }
                        }
                    }
                } else if (arrO1.length == 3 && arrO2.length == 3) {

                    if (arrO1.length < 2 && arrO2.length > 2) {
                        return -1;
                    } else if (arrO1.length > 2 && arrO2.length < 2) {
                        return 1;
                    } else if (arrO1.length > 1 && arrO2.length > 1) {
                        if (arrO1.length == 2 && arrO2.length == 3) {
                            return 1;
                        } else if (arrO1.length == 3 && arrO2.length == 2) {
                            return -1;
                        } else if (arrO1.length == 2 && arrO2.length == 2) {
                            curArr1 = arrO1[1].trim().split(" ");
                            curArr2 = arrO2[1].trim().split(" ");

                            if (curArr1[1].equals("Imax") && curArr2[1].equals("Ib")) {
                                return 1;
                            } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Imax")) {
                                return -1;
                            } else if (curArr1[1].equals("Imax") && curArr2[1].equals("Imax")) {
                                current1 = Float.parseFloat(curArr1[0]);
                                current2 = Float.parseFloat(curArr2[0]);

                                if (current1 > current2) {
                                    return 1;
                                } else if (current1 < current2) {
                                    return -1;
                                } else if (current1 == current2) {
                                    powerFactorType1 = arrO1[0];
                                    powerFactorType2 = arrO2[0];

                                    if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                        return 1;
                                    } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                        return -1;
                                    } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                        powerFactor1 = Float.parseFloat(powerFactorType1);
                                        powerFactor2 = Float.parseFloat(powerFactorType2);

                                        if (powerFactor1 > powerFactor2) {
                                            return 1;
                                        } else {
                                            return -1;
                                        }
                                    } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                        String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                        String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                        powerFactor1 = Float.parseFloat(powerFactorStr1);
                                        powerFactor2 = Float.parseFloat(powerFactorStr2);

                                        if (powerFactor1 > powerFactor2) {
                                            return 1;
                                        } else if (powerFactor1 < powerFactor2) {
                                            return -1;
                                        } else if (powerFactor1 == powerFactor2) {
                                            if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        }

                                    } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                        String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                        String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                        powerFactor1 = Float.parseFloat(powerFactorStr1);
                                        powerFactor2 = Float.parseFloat(powerFactorStr2);

                                        if (powerFactor1 > powerFactor2) {
                                            return 1;
                                        } else if (powerFactor1 < powerFactor2) {
                                            return -1;
                                        } else if (powerFactor1 == powerFactor2) {
                                            if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        }
                                    }
                                }
                            } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Ib")) {
                                current1 = Float.parseFloat(curArr1[0]);
                                current2 = Float.parseFloat(curArr2[0]);

                                if (current1 > current2) {
                                    return 1;
                                } else if (current1 < current2) {
                                    return -1;
                                } else if (current1 == current2) {
                                    powerFactorType1 = arrO1[0];
                                    powerFactorType2 = arrO2[0];

                                    if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                        return 1;
                                    } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                        return -1;
                                    } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                        powerFactor1 = Float.parseFloat(powerFactorType1);
                                        powerFactor2 = Float.parseFloat(powerFactorType2);

                                        if (powerFactor1 > powerFactor2) {
                                            return 1;
                                        } else {
                                            return -1;
                                        }
                                    } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                        String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                        String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                        powerFactor1 = Float.parseFloat(powerFactorStr1);
                                        powerFactor2 = Float.parseFloat(powerFactorStr2);

                                        if (powerFactor1 > powerFactor2) {
                                            return 1;
                                        } else if (powerFactor1 < powerFactor2) {
                                            return -1;
                                        } else if (powerFactor1 == powerFactor2) {
                                            if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        }

                                    } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                        String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                        String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                        powerFactor1 = Float.parseFloat(powerFactorStr1);
                                        powerFactor2 = Float.parseFloat(powerFactorStr2);

                                        if (powerFactor1 > powerFactor2) {
                                            return 1;
                                        } else if (powerFactor1 < powerFactor2) {
                                            return -1;
                                        } else if (powerFactor1 == powerFactor2) {
                                            if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        }
                                    }
                                }
                            }

                        } else if (arrO1.length == 3 && arrO2.length == 3) {
                            if (arrO1[0].equals("A") && (arrO2[0].equals("B") || arrO2[0].equals("C"))) {
                                return 1;
                            } else if (arrO1[0].equals("A") && arrO2[0].equals("A")) {
                                curArr1 = arrO1[2].trim().split(" ");
                                curArr2 = arrO2[2].trim().split(" ");

                                if (curArr1[1].equals("Imax") && curArr2[1].equals("Ib")) {
                                    return 1;
                                } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Imax")) {
                                    return -1;
                                } else if (curArr1[1].equals("Imax") && curArr2[1].equals("Imax")) {
                                    current1 = Float.parseFloat(curArr1[0]);
                                    current2 = Float.parseFloat(curArr2[0]);

                                    if (current1 > current2) {
                                        return 1;
                                    } else if (current1 < current2) {
                                        return -1;
                                    } else if (current1 == current2) {
                                        powerFactorType1 = arrO1[1].trim();
                                        powerFactorType2 = arrO2[1].trim();

                                        if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return 1;
                                        } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return -1;
                                        } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            powerFactor1 = Float.parseFloat(powerFactorType1);
                                            powerFactor2 = Float.parseFloat(powerFactorType2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }

                                        } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }
                                        }
                                    }
                                } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Ib")) {
                                    current1 = Float.parseFloat(curArr1[0]);
                                    current2 = Float.parseFloat(curArr2[0]);

                                    if (current1 > current2) {
                                        return 1;
                                    } else if (current1 < current2) {
                                        return -1;
                                    } else if (current1 == current2) {
                                        powerFactorType1 = arrO1[1].trim();
                                        powerFactorType2 = arrO2[1].trim();

                                        if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return 1;
                                        } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return -1;
                                        } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            powerFactor1 = Float.parseFloat(powerFactorType1);
                                            powerFactor2 = Float.parseFloat(powerFactorType2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }

                                        } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else if (arrO1[0].equals("B") && arrO2[0].equals("C")) {
                                return 1;
                            } else if (arrO1[0].equals("C") && arrO2[0].equals("B")) {
                                return -1;
                            } else if (arrO1[0].equals("B") && arrO2[0].equals("B")) {
                                curArr1 = arrO1[2].trim().split(" ");
                                curArr2 = arrO2[2].trim().split(" ");

                                if (curArr1[1].equals("Imax") && curArr2[1].equals("Ib")) {
                                    return 1;
                                } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Imax")) {
                                    return -1;
                                } else if (curArr1[1].equals("Imax") && curArr2[1].equals("Imax")) {
                                    current1 = Float.parseFloat(curArr1[0]);
                                    current2 = Float.parseFloat(curArr2[0]);

                                    if (current1 > current2) {
                                        return 1;
                                    } else if (current1 < current2) {
                                        return -1;
                                    } else if (current1 == current2) {
                                        powerFactorType1 = arrO1[1].trim();
                                        powerFactorType2 = arrO2[1].trim();

                                        if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return 1;
                                        } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return -1;
                                        } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            powerFactor1 = Float.parseFloat(powerFactorType1);
                                            powerFactor2 = Float.parseFloat(powerFactorType2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }

                                        } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }
                                        }
                                    }
                                } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Ib")) {
                                    current1 = Float.parseFloat(curArr1[0]);
                                    current2 = Float.parseFloat(curArr2[0]);

                                    if (current1 > current2) {
                                        return 1;
                                    } else if (current1 < current2) {
                                        return -1;
                                    } else if (current1 == current2) {
                                        powerFactorType1 = arrO1[1].trim();
                                        powerFactorType2 = arrO2[1].trim();

                                        if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return 1;
                                        } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return -1;
                                        } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            powerFactor1 = Float.parseFloat(powerFactorType1);
                                            powerFactor2 = Float.parseFloat(powerFactorType2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }

                                        } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (arrO1[0].equals("C") && arrO2[0].equals("C")) {
                                curArr1 = arrO1[2].trim().split(" ");
                                curArr2 = arrO2[2].trim().split(" ");

                                if (curArr1[1].equals("Imax") && curArr2[1].equals("Ib")) {
                                    return 1;
                                } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Imax")) {
                                    return -1;
                                } else if (curArr1[1].equals("Imax") && curArr2[1].equals("Imax")) {
                                    current1 = Float.parseFloat(curArr1[0]);
                                    current2 = Float.parseFloat(curArr2[0]);

                                    if (current1 > current2) {
                                        return 1;
                                    } else if (current1 < current2) {
                                        return -1;
                                    } else if (current1 == current2) {
                                        powerFactorType1 = arrO1[1].trim();
                                        powerFactorType2 = arrO2[1].trim();

                                        if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return 1;
                                        } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return -1;
                                        } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            powerFactor1 = Float.parseFloat(powerFactorType1);
                                            powerFactor2 = Float.parseFloat(powerFactorType2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }

                                        } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }
                                        }
                                    }
                                } else if (curArr1[1].equals("Ib") && curArr2[1].equals("Ib")) {
                                    current1 = Float.parseFloat(curArr1[0]);
                                    current2 = Float.parseFloat(curArr2[0]);

                                    if (current1 > current2) {
                                        return 1;
                                    } else if (current1 < current2) {
                                        return -1;
                                    } else if (current1 == current2) {
                                        powerFactorType1 = arrO1[1].trim();
                                        powerFactorType2 = arrO2[1].trim();

                                        if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && (powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return 1;
                                        } else if ((powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            return -1;
                                        } else if (!(powerFactorType1.contains("C") || powerFactorType1.contains("L")) && !(powerFactorType2.contains("C") || powerFactorType2.contains("L"))) {
                                            powerFactor1 = Float.parseFloat(powerFactorType1);
                                            powerFactor2 = Float.parseFloat(powerFactorType2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        } else if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }

                                        } else if (powerFactorType1.contains("C") && powerFactorType2.contains("L")) {
                                            String powerFactorStr1 = powerFactorType1.substring(0, powerFactorType1.length() - 1);
                                            String powerFactorStr2 = powerFactorType2.substring(0, powerFactorType2.length() - 1);

                                            powerFactor1 = Float.parseFloat(powerFactorStr1);
                                            powerFactor2 = Float.parseFloat(powerFactorStr2);

                                            if (powerFactor1 > powerFactor2) {
                                                return 1;
                                            } else if (powerFactor1 < powerFactor2) {
                                                return -1;
                                            } else if (powerFactor1 == powerFactor2) {
                                                if (powerFactorType1.contains("L") && powerFactorType2.contains("C")) {
                                                    return 1;
                                                } else {
                                                    return -1;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if ((arrO1[0].equals("B") || (arrO1[0].equals("C")) && arrO2[0].equals("A"))) {
                                return -1;
                            }
                        }
                    }
                }
                return 1;
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

        //Перенос строк
        viewPointTableAPPls.setRowFactory(dragAndRow);
        viewPointTableAPMns.setRowFactory(dragAndRow);
        viewPointTableRPPls.setRowFactory(dragAndRow);
        viewPointTableRPMns.setRowFactory(dragAndRow);

        viewPointTableAPPls.setEditable(true);
        viewPointTableAPMns.setEditable(true);
        viewPointTableRPPls.setEditable(true);
        viewPointTableRPMns.setEditable(true);

        viewPointTableAPPls.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableAPMns.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableRPPls.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableRPMns.setPlaceholder(new Label("Не выбрано ни одной точки"));

        viewPointTableAPPls.setItems(testListForCollumAPPls);
        viewPointTableAPMns.setItems(testListForCollumAPMns);
        viewPointTableRPPls.setItems(testListForCollumRPPls);
        viewPointTableRPMns.setItems(testListForCollumRPMns);
    }

    //========== Всё для инициализации уже созданной методики (нажата кнопка редактирование) ============

    //Проверияет нет ли данных с полученной методики и если у неё есть данные, то выгружает её в это окно
    //Необходимо для команды Редактирования методики
    public void initEditsMetodic() {
        testListForCollumAPPls.addAll(methodicForOnePhaseStend.getCommandsMap().get(0));
        testListForCollumAPMns.addAll(methodicForOnePhaseStend.getCommandsMap().get(1));
        testListForCollumRPPls.addAll(methodicForOnePhaseStend.getCommandsMap().get(2));
        testListForCollumRPMns.addAll(methodicForOnePhaseStend.getCommandsMap().get(3));

        testListForCollumAPPls.addAll(methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(0));
        testListForCollumAPMns.addAll(methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(1));
        testListForCollumRPPls.addAll(methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(2));
        testListForCollumRPMns.addAll(methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(3));
    }

    //Задаёт параметр true или false нужному checkBox'у
    public void addTestPointsOnGreedPane() {
        char[] testPointIdArr;

        if (!testListForCollumAPPls.isEmpty()) {

            for (Commands command : testListForCollumAPPls) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = ((ErrorCommand) command).getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);

                } else if (command instanceof CreepCommand) {
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

                } else if (command instanceof StartCommand) {
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

                } else if (command instanceof RTCCommand) {

                    if (((RTCCommand) command).getErrorType() == 0) {
                        ChcBxRTCErrAPPls.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrAPPls.setValue("Сутч. погрешность");
                    }

                    txtFieldRngEAPPls.setText(String.valueOf(((RTCCommand) command).getErrorForFalseTest()));
                    txtFldRTCAmtMshAPPls.setText(String.valueOf(((RTCCommand) command).getCountResultTest()));
                    txtFldRTCTimeMshAPPls.setText(String.valueOf(((RTCCommand)command).getPulseForRTC()));

                    ChcBxRTCErrAPPls.setDisable(true);

                    txtFieldRngEAPPls.setDisable(true);
                    txtFldRTCAmtMshAPPls.setDisable(true);
                    txtFldRTCTimeMshAPPls.setDisable(true);
                    addTglBtnRTCAPPls.setSelected(true);

                    RTCTogBtnAPPls.setSelected(true);

                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    if (((ConstantCommand) command).isRunTestToTime()) {
                        radBtnConstTimeAPPls.setSelected(true);
                        txtFieldConstTimeAPPls.setText(constantCommand.getStrTimeToTest());
                    } else {
                        radBtnConstEnergyAPPls.setSelected(true);
                        txtFieldEngConstAPPls.setText(String.valueOf(constantCommand.getkWToTest()));
                    }

                    txtFieldConsProcUAPPls.setText(String.valueOf(constantCommand.getVoltPer()));
                    txtFieldConsProcIAPPls.setText(String.valueOf(constantCommand.getCurrPer()));
                    txtFieldConsErAPPls.setText(String.valueOf(Math.abs(constantCommand.getEmaxProc())));

                    addTglBtnConstAPPls.setSelected(true);
                    ConstTogBtnAPPls.setSelected(true);

                    radBtnConstTimeAPPls.setDisable(true);
                    txtFieldConstTimeAPPls.setDisable(true);
                    radBtnConstEnergyAPPls.setDisable(true);
                    txtFieldEngConstAPPls.setDisable(true);
                    txtFieldConsProcUAPPls.setDisable(true);
                    txtFieldConsProcIAPPls.setDisable(true);
                    txtFieldConsErAPPls.setDisable(true);
                }
            }
        }

        if (!testListForCollumAPMns.isEmpty()) {

            for (Commands command : testListForCollumAPMns) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = ((ErrorCommand) command).getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);
                } else if (command instanceof CreepCommand) {

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

                } else if (command instanceof StartCommand) {
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

                } else if (command instanceof RTCCommand) {

                    if (((RTCCommand) command).getErrorType() == 0) {
                        ChcBxRTCErrAPMns.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrAPMns.setValue("Сутч. погрешность");
                    }

                    txtFieldRngEAPMns.setText(String.valueOf(((RTCCommand) command).getErrorForFalseTest()));
                    txtFldRTCAmtMshAPMns.setText(String.valueOf(((RTCCommand) command).getCountResultTest()));
                    txtFldRTCTimeMshAPMns.setText(String.valueOf(((RTCCommand)command).getPulseForRTC()));

                    ChcBxRTCErrAPMns.setDisable(true);

                    txtFieldRngEAPMns.setDisable(true);
                    txtFldRTCAmtMshAPMns.setDisable(true);
                    txtFldRTCTimeMshAPMns.setDisable(true);
                    addTglBtnRTCAPMns.setSelected(true);

                    RTCTogBtnAPMns.setSelected(true);

                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    if (((ConstantCommand) command).isRunTestToTime()) {
                        radBtnConstTimeAPMns.setSelected(true);
                        txtFieldConstTimeAPMns.setText(constantCommand.getStrTimeToTest());
                    } else {
                        radBtnConstEnergyAPMns.setSelected(true);
                        txtFieldEngConstAPMns.setText(String.valueOf(constantCommand.getkWToTest()));
                    }

                    txtFieldConsProcUAPMns.setText(String.valueOf(constantCommand.getVoltPer()));
                    txtFieldConsProcIAPMns.setText(String.valueOf(constantCommand.getCurrPer()));
                    txtFieldConsErAPMns.setText(String.valueOf(Math.abs(constantCommand.getEmaxProc())));

                    addTglBtnConstAPMns.setSelected(true);
                    ConstTogBtnAPMns.setSelected(true);

                    radBtnConstTimeAPMns.setDisable(true);
                    txtFieldConstTimeAPMns.setDisable(true);
                    radBtnConstEnergyAPMns.setDisable(true);
                    txtFieldEngConstAPMns.setDisable(true);
                    txtFieldConsProcUAPMns.setDisable(true);
                    txtFieldConsProcIAPMns.setDisable(true);
                    txtFieldConsErAPMns.setDisable(true);
                }
            }
        }

        if (!testListForCollumRPPls.isEmpty()) {

            for (Commands command : testListForCollumRPPls) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = ((ErrorCommand) command).getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);

                } else if (command instanceof CreepCommand) {
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

                } else if (command instanceof StartCommand) {
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

                } else if (command instanceof RTCCommand) {

                    if (((RTCCommand) command).getErrorType() == 0) {
                        ChcBxRTCErrRPPls.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrRPPls.setValue("Сутч. погрешность");
                    }

                    txtFieldRngERPPls.setText(String.valueOf(((RTCCommand) command).getErrorForFalseTest()));
                    txtFldRTCAmtMshRPPls.setText(String.valueOf(((RTCCommand) command).getCountResultTest()));
                    txtFldRTCTimeMshRPPls.setText(String.valueOf(((RTCCommand)command).getPulseForRTC()));

                    ChcBxRTCErrRPPls.setDisable(true);

                    txtFieldRngERPPls.setDisable(true);
                    txtFldRTCAmtMshRPPls.setDisable(true);
                    txtFldRTCTimeMshRPPls.setDisable(true);
                    addTglBtnRTCRPPls.setSelected(true);

                    RTCTogBtnRPPls.setSelected(true);

                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    if (((ConstantCommand) command).isRunTestToTime()) {
                        radBtnConstTimeRPPls.setSelected(true);
                        txtFieldConstTimeRPPls.setText(constantCommand.getStrTimeToTest());
                    } else {
                        radBtnConstEnergyRPPls.setSelected(true);
                        txtFieldEngConstRPPls.setText(String.valueOf(constantCommand.getkWToTest()));
                    }

                    txtFieldConsProcURPPls.setText(String.valueOf(constantCommand.getVoltPer()));
                    txtFieldConsProcIRPPls.setText(String.valueOf(constantCommand.getCurrPer()));
                    txtFieldConsErRPPls.setText(String.valueOf(Math.abs(constantCommand.getEmaxProc())));

                    addTglBtnConstRPPls.setSelected(true);
                    ConstTogBtnRPPls.setSelected(true);

                    radBtnConstTimeRPPls.setDisable(true);
                    txtFieldConstTimeRPPls.setDisable(true);
                    radBtnConstEnergyRPPls.setDisable(true);
                    txtFieldEngConstRPPls.setDisable(true);
                    txtFieldConsProcURPPls.setDisable(true);
                    txtFieldConsProcIRPPls.setDisable(true);
                    txtFieldConsErRPPls.setDisable(true);
                }
            }
        }

        if (!testListForCollumRPMns.isEmpty()) {

            for (Commands command : testListForCollumRPMns) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = ((ErrorCommand) command).getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);

                } else if (command instanceof CreepCommand) {
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

                } else if (command instanceof StartCommand) {
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

                } else if (command instanceof RTCCommand) {

                    if (((RTCCommand) command).getErrorType() == 0) {
                        ChcBxRTCErrRPMns.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrRPMns.setValue("Сутч. погрешность");
                    }

                    txtFieldRngERPMns.setText(String.valueOf(((RTCCommand) command).getErrorForFalseTest()));
                    txtFldRTCAmtMshRPMns.setText(String.valueOf(((RTCCommand) command).getCountResultTest()));
                    txtFldRTCTimeMshRPMns.setText(String.valueOf(((RTCCommand)command).getPulseForRTC()));

                    ChcBxRTCErrRPMns.setDisable(true);

                    txtFieldRngERPMns.setDisable(true);
                    txtFldRTCAmtMshRPMns.setDisable(true);
                    txtFldRTCTimeMshRPMns.setDisable(true);
                    addTglBtnRTCRPMns.setSelected(true);

                    RTCTogBtnRPMns.setSelected(true);

                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    if (((ConstantCommand) command).isRunTestToTime()) {
                        radBtnConstTimeRPMns.setSelected(true);
                        txtFieldConstTimeRPMns.setText(constantCommand.getStrTimeToTest());
                    } else {
                        radBtnConstEnergyRPMns.setSelected(true);
                        txtFieldEngConstRPMns.setText(String.valueOf(constantCommand.getkWToTest()));
                    }

                    txtFieldConsProcURPMns.setText(String.valueOf(constantCommand.getVoltPer()));
                    txtFieldConsProcIRPMns.setText(String.valueOf(constantCommand.getCurrPer()));
                    txtFieldConsErRPMns.setText(String.valueOf(Math.abs(constantCommand.getEmaxProc())));

                    addTglBtnConstRPMns.setSelected(true);
                    ConstTogBtnRPMns.setSelected(true);

                    radBtnConstTimeRPMns.setDisable(true);
                    txtFieldConstTimeRPMns.setDisable(true);
                    radBtnConstEnergyRPMns.setDisable(true);
                    txtFieldEngConstRPMns.setDisable(true);
                    txtFieldConsProcURPMns.setDisable(true);
                    txtFieldConsProcIRPMns.setDisable(true);
                    txtFieldConsErRPMns.setDisable(true);
                }
            }
        }
    }

    //Находит нужный CheckBox и задаёт значение
    private void setTrueOrFalseOnCheckBox(char[] testPointIdArr, Commands commands) {
        //AP+
        if (testPointIdArr[4] == 'A' && testPointIdArr[6] == 'P') {

            if (testPointIdArr[2] == 'H') {

                for (Node checkBox : gridPaneOnePhaseAPPlus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }
            } else if (testPointIdArr[2] == 'A') {

                for (Node checkBox : gridPanePhaseAAPPlus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }

            } else if (testPointIdArr[2] == 'B') {

                for (Node checkBox : gridPanePhaseBAPPlus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }
            }
        //AP-
        } else if (testPointIdArr[4] == 'A' && testPointIdArr[6] == 'N') {

            if (testPointIdArr[2] == 'H') {

                for (Node checkBox : gridPaneOnePhaseAPMinus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    } catch (NullPointerException ignore) {}
                }
            }

            if (testPointIdArr[2] == 'A') {

                for (Node checkBox : gridPanePhaseAAPMinus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    } catch (NullPointerException ignore) {}
                }
            }

            if (testPointIdArr[2] == 'B') {

                for (Node checkBox : gridPanePhaseBAPMinus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }
            }

        //RP+
        } else if (testPointIdArr[4] == 'R' && testPointIdArr[6] == 'P') {

            if (testPointIdArr[2] == 'H') {

                for (Node checkBox : gridPaneOnePhaseRPPlus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }
            }else if (testPointIdArr[2] == 'A') {

                for (Node checkBox : gridPanePhaseARPPlus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }
            }else if (testPointIdArr[2] == 'B') {

                for (Node checkBox : gridPanePhaseBRPPlus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    } catch (NullPointerException ignore) {
                    }
                }
            }
        //RP-
        } else if (testPointIdArr[4] == 'R' && testPointIdArr[6] == 'N') {

            if (testPointIdArr[2] == 'H') {

                for (Node checkBox : gridPaneOnePhaseRPMinus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }
            }else if (testPointIdArr[2] == 'A') {

                for (Node checkBox : gridPanePhaseARPMinus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }
            }else if (testPointIdArr[2] == 'B') {

                for (Node checkBox : gridPanePhaseBRPMinus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    } catch (NullPointerException ignore) {
                    }
                }
            }
        }
    }

//===================================================================================================
    //Метод для перерисовки GridPane после добавления новых параметров для точек
    public void refreshGridPaneAndScrolPane() {
        current = Arrays.asList(ConsoleHelper.properties.getProperty("currentForMethodicPane").split(", "));
        powerFactor = Arrays.asList(ConsoleHelper.properties.getProperty("powerFactorForMetodicPane").split(", "));

        mainAnchorPane.getChildren().removeAll(mainScrollPane, scrollPaneForCurrent, scrollPaneForPowerFactor, stackPaneForGridPane);

        stackPaneForGridPane = new StackPane();
        mainScrollPane = new ScrollPane();
        scrollPaneForCurrent = new ScrollPane();
        scrollPaneForPowerFactor = new ScrollPane();
        gridPaneForCurrent = new GridPane();
        gridPaneForPowerFactor = new GridPane();

        gridPaneOnePhaseAPPlus = new GridPane();
        gridPanePhaseAAPPlus = new GridPane();
        gridPanePhaseBAPPlus = new GridPane();

        gridPaneOnePhaseAPMinus = new GridPane();
        gridPanePhaseAAPMinus = new GridPane();
        gridPanePhaseBAPMinus = new GridPane();

        gridPaneOnePhaseRPPlus = new GridPane();
        gridPanePhaseARPPlus = new GridPane();
        gridPanePhaseBRPPlus = new GridPane();

        gridPaneOnePhaseRPMinus = new GridPane();
        gridPanePhaseARPMinus = new GridPane();
        gridPanePhaseBRPMinus = new GridPane();


        setIdGridPanes();

        createGridPaneAndsetCheckBoxes();

        initMainScrollPane();

        initScrolPaneForCurrentAndPowerFactor();

        createScrollPanesForGridPaneWithoutSquare();

        gridPaneOnePhaseAPPlus.toFront();

        if (mainScrollPane.getSkin() == null || scrollPaneForPowerFactor.getSkin() == null || scrollPaneForCurrent.getSkin() == null) {
            mainScrollPane.skinProperty().addListener(new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                    mainScrollPane.skinProperty().removeListener(this);
                    bindScrollPanesCurrentAndPowerFactorToMainScrollPane();
                }
            });
            scrollPaneForPowerFactor.skinProperty().addListener(new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                    scrollPaneForPowerFactor.skinProperty().removeListener(this);
                    bindScrollPanesCurrentAndPowerFactorToMainScrollPane();
                }
            });

            scrollPaneForCurrent.skinProperty().addListener(new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                    scrollPaneForCurrent.skinProperty().removeListener(this);
                    bindScrollPanesCurrentAndPowerFactorToMainScrollPane();
                }
            });
        } else {
            bindScrollPanesCurrentAndPowerFactorToMainScrollPane();
        }
    }

    @FXML
    void influenceAction(ActionEvent event) {
        if (event.getSource() == influenceBtn) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/OnePhase/influencePointsOnePhaseStendFrame.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            influencePointsOnePhaseStendFrame = fxmlLoader.getController();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    influencePointsOnePhaseStendFrame.myInitInflFrame(methodicForOnePhaseStend);
                    influencePointsOnePhaseStendFrame.bindScrollPanesCurrentAndPowerFactorToMainScrollPane();
                }
            });

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Настройки теста влияния");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();

        }
    }

    @FXML
    void parametersAction(ActionEvent event) {
        if (event.getSource() == parametersBtn) {
            parametersBtn.setSelected(bindParameters);

            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/parametersMethodicFrame.fxml"));
                fxmlLoader.load();

                ParametersMethodicController parametersMethodicController = fxmlLoader.getController();
                parametersMethodicController.setMethodicForStend(methodicForOnePhaseStend);
                parametersMethodicController.setAddEditPointsOnePhaseStendFrameController(this);

                if (bindParameters) {
                    parametersMethodicController.setDisableAllParam();
                }

                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Параметры испытаний");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void saveOrCancelAction(ActionEvent event) {
        if (event.getSource() == SaveBtn) {

            List<Commands> listErrorCommand;
            List<Commands> listCreepStartRTCCommadns;

            listErrorCommand = methodicForOnePhaseStend.getCommandsMap().get(0);
            listCreepStartRTCCommadns = methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(0);

            listErrorCommand.clear();
            listCreepStartRTCCommadns.clear();

            for (Commands command : testListForCollumAPPls) {
                if (command instanceof ErrorCommand) {
                    listErrorCommand.add(command);
                } else {
                    listCreepStartRTCCommadns.add(command);
                }
            }

            listErrorCommand = methodicForOnePhaseStend.getCommandsMap().get(1);
            listCreepStartRTCCommadns = methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(1);

            listErrorCommand.clear();
            listCreepStartRTCCommadns.clear();

            for (Commands command : testListForCollumAPMns) {
                if (command instanceof ErrorCommand) {
                    listErrorCommand.add(command);
                } else {
                    listCreepStartRTCCommadns.add(command);
                }
            }

            listErrorCommand = methodicForOnePhaseStend.getCommandsMap().get(2);
            listCreepStartRTCCommadns = methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(2);

            listErrorCommand.clear();
            listCreepStartRTCCommadns.clear();

            for (Commands command : testListForCollumRPPls) {
                if (command instanceof ErrorCommand) {
                    listErrorCommand.add(command);
                } else {
                    listCreepStartRTCCommadns.add(command);
                }
            }

            listErrorCommand = methodicForOnePhaseStend.getCommandsMap().get(3);
            listCreepStartRTCCommadns = methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(3);

            listErrorCommand.clear();
            listCreepStartRTCCommadns.clear();

            for (Commands command : testListForCollumRPMns) {
                if (command instanceof ErrorCommand) {
                    listErrorCommand.add(command);
                } else {
                    listCreepStartRTCCommadns.add(command);
                }
            }

            if (edit) {
                methodicsAddEditDeleteFrameController.setListsView(methodicForOnePhaseStend);
            }else {
                methodicsAddEditDeleteFrameController.refreshMethodicList();
            }

            metodicsForTest.serializationMetodics();
            Stage stage1 = (Stage) methodicsAddEditDeleteFrameController.getEditMetBtn().getScene().getWindow();
            stage1.show();

            Stage stage = (Stage) SaveBtn.getScene().getWindow();
            stage.close();
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

                txtFieldCRPUProcAPPls.setStyle("");
                txtFieldTimeCRPAPPls.setStyle("");
                txtFieldCRPAmtImpAPPls.setStyle("");

                double procUnom;
                long timeTest;
                int amountImp;

                try {
                    procUnom = Double.parseDouble(txtFieldCRPUProcAPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPUProcAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPPls.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeCRPAPPls.getText().split(":");

                    if (timeArr[0].length() != 2 || timeArr[1].length() != 2 || timeArr[2].length() != 2 || txtFieldTimeCRPAPPls.getText().length() != 8) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0]);
                    int mins = Integer.parseInt(timeArr[1]);
                    int sek = Integer.parseInt(timeArr[2]);

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPPls.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldCRPAmtImpAPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPPls.setSelected(false);
                    return;
                }

                testListForCollumAPPls.add(new CreepCommand(false,false, "Самоход AP+", "CRP;U;A;P",0,
                        timeTest, amountImp, procUnom));

                txtFieldCRPAmtImpAPPls.setDisable(true);
                txtFieldTimeCRPAPPls.setDisable(true);
                txtFieldCRPUProcAPPls.setDisable(true);

                CRPTogBtnAPPls.setSelected(true);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof CreepCommand) {
                        if (command.getName().equals("Самоход AP+")) {
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
                } else {
                    CRPTogBtnAPPls.setSelected(false);
                }
            }
        }

        //Добаление самохода с параметрами по ГОСТу AP+
        if (event.getSource() == addTglBtnCRPAPPlsGOST) {
            if (addTglBtnCRPAPPlsGOST.isSelected()) {
                creepCommand = new CreepCommand(false, true, "Самоход AP+ ГОСТ","CRP;G;A;P", 0);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);

                CRPTogBtnAPPls.setSelected(true);

                testListForCollumAPPls.add(creepCommand);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof CreepCommand) {
                        if (command.getName().equals("Самоход AP+ ГОСТ")) {
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

                txtFieldCRPUProcAPMns.setStyle("");
                txtFieldTimeCRPAPMns.setStyle("");
                txtFieldCRPAmtImpAPMns.setStyle("");

                double procUnom;
                long timeTest;
                int amountImp;

                try {
                    procUnom = Double.parseDouble(txtFieldCRPUProcAPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPUProcAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPMns.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeCRPAPMns.getText().split(":");

                    if (timeArr[0].length() != 2 || timeArr[1].length() != 2 || timeArr[2].length() != 2 || txtFieldTimeCRPAPMns.getText().length() != 8) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0]);
                    int mins = Integer.parseInt(timeArr[1]);
                    int sek = Integer.parseInt(timeArr[2]);

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPMns.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldCRPAmtImpAPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPMns.setSelected(false);
                    return;
                }

                testListForCollumAPMns.add(new CreepCommand(false,false, "Самоход AP-", "CRP;U;A;N",1,
                        timeTest, amountImp, procUnom));

                txtFieldCRPAmtImpAPMns.setDisable(true);
                txtFieldTimeCRPAPMns.setDisable(true);
                txtFieldCRPUProcAPMns.setDisable(true);

                CRPTogBtnAPMns.setSelected(true);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof CreepCommand) {
                        if (command.getName().equals("Самоход AP-")) {
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
                } else {
                    CRPTogBtnAPMns.setSelected(false);
                }
            }
        }

        //Добаление самохода с параметрами по ГОСТу AP-
        if (event.getSource() == addTglBtnCRPAPMnsGOST) {
            if (addTglBtnCRPAPMnsGOST.isSelected()) {
                creepCommand = new CreepCommand(false, true, "Самоход AP- ГОСТ", "CRP;G;A;N",1);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);

                CRPTogBtnAPMns.setSelected(true);

                testListForCollumAPMns.add(creepCommand);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof CreepCommand) {
                        if (command.getName().equals("Самоход AP- ГОСТ")) {
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

                txtFieldCRPUProcRPPls.setStyle("");
                txtFieldTimeCRPRPPls.setStyle("");
                txtFieldCRPAmtImpRPPls.setStyle("");

                double procUnom;
                long timeTest;
                int amountImp;

                try {
                    procUnom = Double.parseDouble(txtFieldCRPUProcRPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPUProcRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPPls.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeCRPRPPls.getText().split(":");

                    if (timeArr[0].length() != 2 || timeArr[1].length() != 2 || timeArr[2].length() != 2 || txtFieldTimeCRPRPPls.getText().length() != 8) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0]);
                    int mins = Integer.parseInt(timeArr[1]);
                    int sek = Integer.parseInt(timeArr[2]);

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPPls.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldCRPAmtImpRPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPPls.setSelected(false);
                    return;
                }

                testListForCollumRPPls.add(new CreepCommand(false,false, "Самоход RP+", "CRP;U;R;P",2,
                        timeTest, amountImp, procUnom));

                txtFieldCRPAmtImpRPPls.setDisable(true);
                txtFieldTimeCRPRPPls.setDisable(true);
                txtFieldCRPUProcRPPls.setDisable(true);

                CRPTogBtnRPPls.setSelected(true);
            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof CreepCommand) {
                        if (command.getName().equals("Самоход RP+")) {
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
                } else {
                    CRPTogBtnRPPls.setSelected(false);
                }
            }
        }

        //Добаление самохода с параметрами по ГОСТу RP+
        if (event.getSource() == addTglBtnCRPRPPlsGOST) {
            if (addTglBtnCRPRPPlsGOST.isSelected()) {
                creepCommand = new CreepCommand(false, true, "Самоход RP+ ГОСТ", "CRP;G;R;P", 2);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);

                CRPTogBtnRPPls.setSelected(true);

                testListForCollumRPPls.add(creepCommand);
            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof CreepCommand) {
                        if (command.getName().equals("Самоход RP+ ГОСТ")) {
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

                txtFieldCRPUProcRPMns.setStyle("");
                txtFieldTimeCRPRPMns.setStyle("");
                txtFieldCRPAmtImpRPMns.setStyle("");

                double procUnom;
                long timeTest;
                int amountImp;

                try {
                    procUnom = Double.parseDouble(txtFieldCRPUProcRPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPUProcRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPMns.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeCRPRPMns.getText().split(":");

                    if (timeArr[0].length() != 2 || timeArr[1].length() != 2 || timeArr[2].length() != 2 || txtFieldTimeCRPRPMns.getText().length() != 8) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0]);
                    int mins = Integer.parseInt(timeArr[1]);
                    int sek = Integer.parseInt(timeArr[2]);

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPMns.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldCRPAmtImpRPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPMns.setSelected(false);
                    return;
                }

                testListForCollumRPMns.add(new CreepCommand(false,false, "Самоход RP-", "CRP;U;R;N",3,
                        timeTest, amountImp, procUnom));

                txtFieldCRPAmtImpRPMns.setDisable(true);
                txtFieldTimeCRPRPMns.setDisable(true);
                txtFieldCRPUProcRPMns.setDisable(true);

                CRPTogBtnRPMns.setSelected(true);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof CreepCommand) {
                        if (command.getName().equals("Самоход RP-")) {
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
                } else {
                    CRPTogBtnRPMns.setSelected(false);
                }
            }
        }

        //Добаление самохода с параметрами по ГОСТу RP-
        if (event.getSource() == addTglBtnCRPRPMnsGOST) {
            if (addTglBtnCRPRPMnsGOST.isSelected()) {
                creepCommand = new CreepCommand(false, true, "Самоход RP- ГОСТ", "CRP;G;R;N",3);

                creepCommand.setPulseValue(2);
                creepCommand.setVoltPer(115.0);

                CRPTogBtnRPMns.setSelected(true);

                testListForCollumRPMns.add(creepCommand);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof CreepCommand) {
                        if (command.getName().equals("Самоход RP- ГОСТ")) {
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
                startCommand = new StartCommand(false, "Чувствительность AP+", "STA;U;A;P", 0, 0, false, txtFieldTimeSRAAPPls.getText());

                startCommand.setPulseValue(Integer.parseInt(txtFieldSTAAmtImpAPPls.getText()));
                startCommand.setRatedCurr(Double.parseDouble(txtFieldSTAIProcAPPls.getText()));

                txtFieldSTAAmtImpAPPls.setDisable(true);
                txtFieldTimeSRAAPPls.setDisable(true);
                txtFieldSTAIProcAPPls.setDisable(true);

                STATogBtnAPPls.setSelected(true);
                testListForCollumAPPls.add(startCommand);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность AP+")) {
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
                startCommand = new StartCommand(false, "Чувствительность ГОСТ AP+", "STA;G;A;P", 0, 0, true);

                STATogBtnAPPls.setSelected(true);
                testListForCollumAPPls.add(startCommand);
            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность ГОСТ AP+")) {
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
                startCommand = new StartCommand(false, "Чувствительность AP-", "STA;G;A;N", 1, 1, false, txtFieldTimeSRAAPMns.getText());

                startCommand.setPulseValue(Integer.parseInt(txtFieldSTAAmtImpAPMns.getText()));
                startCommand.setRatedCurr(Double.parseDouble(txtFieldSTAIProcAPMns.getText()));

                txtFieldSTAAmtImpAPMns.setDisable(true);
                txtFieldTimeSRAAPMns.setDisable(true);
                txtFieldSTAIProcAPMns.setDisable(true);

                STATogBtnAPMns.setSelected(true);
                testListForCollumAPMns.add(startCommand);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность AP-")) {
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
                startCommand = new StartCommand(false, "Чувствительность ГОСТ AP-", "STA;G;A;N", 1, 1, true);

                STATogBtnAPMns.setSelected(true);
                testListForCollumAPMns.add(startCommand);
            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность ГОСТ AP-")) {
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
                startCommand = new StartCommand(false, "Чувствительность RP+", "STA;U;R;P", 0, 2, false, txtFieldTimeSRARPPls.getText());

                startCommand.setPulseValue(Integer.parseInt(txtFieldSTAAmtImpRPPls.getText()));
                startCommand.setRatedCurr(Double.parseDouble(txtFieldSTAIProcRPPls.getText()));

                txtFieldSTAAmtImpRPPls.setDisable(true);
                txtFieldTimeSRARPPls.setDisable(true);
                txtFieldSTAIProcRPPls.setDisable(true);

                STATogBtnRPPls.setSelected(true);
                testListForCollumRPPls.add(startCommand);

            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность RP+")) {
                            testListForCollumRPPls.remove(command);
                            break;
                        }
                    }
                }
                txtFieldSTAAmtImpRPPls.setDisable(false);
                txtFieldTimeSRARPPls.setDisable(false);
                txtFieldSTAIProcRPPls.setDisable(false);

                if (addTglBtnSTARPPlsGOST.isSelected()) {
                    STATogBtnRPPls.setSelected(true);
                } else STATogBtnRPPls.setSelected(false);
            }
        }

        //Добаление теста на чувствительность с параметрами по ГОСТу RP+
        if (event.getSource() == addTglBtnSTARPPlsGOST) {
            if (addTglBtnSTARPPlsGOST.isSelected()) {
                startCommand = new StartCommand(false, "Чувствительность ГОСТ RP+", "STA;G;R;A", 0, 1, true);

                STATogBtnRPPls.setSelected(true);
                testListForCollumRPPls.add(startCommand);
            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность ГОСТ RP+")) {
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
                startCommand = new StartCommand(false, "Чувствительность RP-","STA;U;R;N", 1, 3, false, txtFieldTimeSRARPMns.getText());

                startCommand.setPulseValue(Integer.parseInt(txtFieldSTAAmtImpRPMns.getText()));
                startCommand.setRatedCurr(Double.parseDouble(txtFieldSTAIProcRPMns.getText()));

                txtFieldSTAAmtImpRPMns.setDisable(false);
                txtFieldTimeSRARPMns.setDisable(false);
                txtFieldSTAIProcRPMns.setDisable(false);

                STATogBtnRPMns.setSelected(true);
                testListForCollumRPMns.add(startCommand);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность RP-")) {
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
                startCommand = new StartCommand(false, "Чувствительность ГОСТ RP-", "STA;G;R;N",0, 3, true);

                STATogBtnRPMns.setSelected(true);
                testListForCollumRPMns.add(startCommand);
            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность ГОСТ RP-")) {
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

        //Добаление теста "точность хода часов" AP+
        if(event.getSource() == addTglBtnRTCAPPls) {

            if (addTglBtnRTCAPPls.isSelected()) {

                txtFieldRngEAPPls.setStyle("");
                txtFldRTCAmtMshAPPls.setStyle("");
                txtFldRTCTimeMshAPPls.setStyle("");
                txtFldRTCFrqAPPls.setStyle("");

                double errorRange;
                double freg;
                int anountMeash;
                int timeMeas;

                try {
                    errorRange = Double.parseDouble(txtFieldRngEAPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldRngEAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPPls.setSelected(false);
                    return;
                }

                try {
                    freg = Double.parseDouble(txtFldRTCFrqAPPls.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCFrqAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPPls.setSelected(false);
                    return;
                }

                try {
                    anountMeash = Integer.parseInt(txtFldRTCAmtMshAPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCAmtMshAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPPls.setSelected(false);
                    return;
                }

                try {
                    timeMeas = Integer.parseInt(txtFldRTCTimeMshAPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCTimeMshAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPPls.setSelected(false);
                    return;
                }

                if (ChcBxRTCErrAPPls.getValue().equals("В ед. частоты")) {

                    testListForCollumAPPls.add(new RTCCommand(false, "RTC;A;P", "ТХЧ AP+", timeMeas, freg,
                            anountMeash, 0, errorRange, 0));
                } else {

                    testListForCollumAPPls.add(new RTCCommand(false, "RTC;A;P", "ТХЧ AP+", timeMeas, freg,
                            anountMeash, 1, errorRange, 0));
                }

                ChcBxRTCErrAPPls.setDisable(true);
                txtFldRTCTimeMshAPPls.setDisable(true);
                txtFldRTCAmtMshAPPls.setDisable(true);
                txtFieldRngEAPPls.setDisable(true);
                txtFldRTCFrqAPPls.setDisable(true);

                RTCTogBtnAPPls.setSelected(true);

            } else {
                for (Commands command : testListForCollumAPPls) {
                    if (command instanceof RTCCommand) {
                        if (command.getName().equals("ТХЧ AP+")) {
                            testListForCollumAPPls.remove(command);
                            break;
                        }
                    }
                }

                ChcBxRTCErrAPPls.setDisable(false);
                txtFldRTCTimeMshAPPls.setDisable(false);
                txtFldRTCAmtMshAPPls.setDisable(false);
                txtFieldRngEAPPls.setDisable(false);
                txtFldRTCFrqAPPls.setDisable(true);

                RTCTogBtnAPPls.setSelected(false);
            }
        }

        if(event.getSource() == addTglBtnRTCAPMns) {

            if (addTglBtnRTCAPMns.isSelected()) {

                txtFieldRngEAPMns.setStyle("");
                txtFldRTCAmtMshAPMns.setStyle("");
                txtFldRTCTimeMshAPMns.setStyle("");
                txtFldRTCFrqAPMns.setStyle("");

                double errorRange;
                double freg;
                int anountMeash;
                int timeMeas;

                try {
                    errorRange = Double.parseDouble(txtFieldRngEAPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldRngEAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPMns.setSelected(false);
                    return;
                }

                try {
                    freg = Double.parseDouble(txtFldRTCFrqAPMns.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCFrqAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPMns.setSelected(false);
                    return;
                }

                try {
                    anountMeash = Integer.parseInt(txtFldRTCAmtMshAPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCAmtMshAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPMns.setSelected(false);
                    return;
                }

                try {
                    timeMeas = Integer.parseInt(txtFldRTCTimeMshAPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCTimeMshAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPMns.setSelected(false);
                    return;
                }

                if (ChcBxRTCErrAPMns.getValue().equals("В ед. частоты")) {

                    testListForCollumAPMns.add(new RTCCommand(false, "RTC;A;N", "ТХЧ AP-", timeMeas, freg,
                            anountMeash, 0, errorRange, 1));
                } else {

                    testListForCollumAPMns.add(new RTCCommand(false, "RTC;A;N", "ТХЧ AP-", timeMeas, freg,
                            anountMeash, 1, errorRange, 1));
                }

                ChcBxRTCErrAPMns.setDisable(true);
                txtFldRTCTimeMshAPMns.setDisable(true);
                txtFldRTCAmtMshAPMns.setDisable(true);
                txtFieldRngEAPMns.setDisable(true);
                txtFldRTCFrqAPMns.setDisable(true);

                RTCTogBtnAPMns.setSelected(true);

            } else {
                for (Commands command : testListForCollumAPMns) {
                    if (command instanceof RTCCommand) {
                        if (command.getName().equals("ТХЧ AP-")) {
                            testListForCollumAPMns.remove(command);
                            break;
                        }
                    }
                }

                ChcBxRTCErrAPMns.setDisable(false);
                txtFldRTCTimeMshAPMns.setDisable(false);
                txtFldRTCAmtMshAPMns.setDisable(false);
                txtFieldRngEAPMns.setDisable(false);
                txtFldRTCFrqAPMns.setDisable(true);

                RTCTogBtnAPMns.setSelected(false);
            }
        }

        if(event.getSource() == addTglBtnRTCRPPls) {

            if (addTglBtnRTCRPPls.isSelected()) {

                txtFieldRngERPPls.setStyle("");
                txtFldRTCAmtMshRPPls.setStyle("");
                txtFldRTCTimeMshRPPls.setStyle("");
                txtFldRTCFrqRPPls.setStyle("");

                double errorRange;
                double freg;
                int anountMeash;
                int timeMeas;

                try {
                    errorRange = Double.parseDouble(txtFieldRngERPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldRngERPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPPls.setSelected(false);
                    return;
                }

                try {
                    freg = Double.parseDouble(txtFldRTCFrqRPPls.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCFrqRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPPls.setSelected(false);
                    return;
                }

                try {
                    anountMeash = Integer.parseInt(txtFldRTCAmtMshRPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCAmtMshRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPPls.setSelected(false);
                    return;
                }

                try {
                    timeMeas = Integer.parseInt(txtFldRTCTimeMshRPPls.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCTimeMshRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPPls.setSelected(false);
                    return;
                }

                if (ChcBxRTCErrRPPls.getValue().equals("В ед. частоты")) {

                    testListForCollumRPPls.add(new RTCCommand(false, "RTC;R;P", "ТХЧ RP+", timeMeas, freg,
                            anountMeash, 0, errorRange, 2));
                } else {

                    testListForCollumRPPls.add(new RTCCommand(false, "RTC;R;P", "ТХЧ RP+", timeMeas, freg,
                            anountMeash, 1, errorRange, 2));
                }

                ChcBxRTCErrRPPls.setDisable(true);
                txtFldRTCTimeMshRPPls.setDisable(true);
                txtFldRTCAmtMshRPPls.setDisable(true);
                txtFieldRngERPPls.setDisable(true);
                txtFldRTCFrqRPPls.setDisable(true);

                RTCTogBtnRPPls.setSelected(true);

            } else {
                for (Commands command : testListForCollumRPPls) {
                    if (command instanceof RTCCommand) {
                        if (command.getName().equals("ТХЧ RP+")) {
                            testListForCollumRPPls.remove(command);
                            break;
                        }
                    }
                }

                ChcBxRTCErrRPPls.setDisable(false);
                txtFldRTCTimeMshRPPls.setDisable(false);
                txtFldRTCAmtMshRPPls.setDisable(false);
                txtFieldRngERPPls.setDisable(false);
                txtFldRTCFrqRPPls.setDisable(true);

                RTCTogBtnRPPls.setSelected(false);
            }
        }

        if(event.getSource() == addTglBtnRTCRPMns) {

            if (addTglBtnRTCRPMns.isSelected()) {

                txtFieldRngERPMns.setStyle("");
                txtFldRTCAmtMshRPMns.setStyle("");
                txtFldRTCTimeMshRPMns.setStyle("");
                txtFldRTCFrqRPMns.setStyle("");

                double errorRange;
                double freg;
                int anountMeash;
                int timeMeas;

                try {
                    errorRange = Double.parseDouble(txtFieldRngERPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldRngERPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPMns.setSelected(false);
                    return;
                }

                try {
                    freg = Double.parseDouble(txtFldRTCFrqRPMns.getText());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCFrqRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPMns.setSelected(false);
                    return;
                }

                try {
                    anountMeash = Integer.parseInt(txtFldRTCAmtMshRPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCAmtMshRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPMns.setSelected(false);
                    return;
                }

                try {
                    timeMeas = Integer.parseInt(txtFldRTCTimeMshRPMns.getText());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCTimeMshRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPMns.setSelected(false);
                    return;
                }

                if (ChcBxRTCErrRPMns.getValue().equals("В ед. частоты")) {

                    testListForCollumRPMns.add(new RTCCommand(false, "RTC;R;N", "ТХЧ RP-", timeMeas, freg,
                            anountMeash, 0, errorRange, 3));
                } else {

                    testListForCollumRPMns.add(new RTCCommand(false, "RTC;R;N", "ТХЧ RP-", timeMeas, freg,
                            anountMeash, 1, errorRange, 3));
                }

                ChcBxRTCErrRPMns.setDisable(true);
                txtFldRTCTimeMshRPMns.setDisable(true);
                txtFldRTCAmtMshRPMns.setDisable(true);
                txtFieldRngERPMns.setDisable(true);
                txtFldRTCFrqRPMns.setDisable(true);

                RTCTogBtnRPMns.setSelected(true);

            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof RTCCommand) {
                        if (command.getName().equals("ТХЧ RP-")) {
                            testListForCollumRPMns.remove(command);
                            break;
                        }
                    }
                }

                ChcBxRTCErrRPMns.setDisable(false);
                txtFldRTCTimeMshRPMns.setDisable(false);
                txtFldRTCAmtMshRPMns.setDisable(false);
                txtFieldRngERPMns.setDisable(false);
                txtFldRTCFrqRPMns.setDisable(true);

                RTCTogBtnRPMns.setSelected(false);
            }
        }

        //Добавление теста константы
        if (event.getSource() == addTglBtnConstAPPls) {

            txtFieldConstTimeAPPls.setStyle("");
            txtFieldEngConstAPPls.setStyle("");
            txtFieldConsProcUAPPls.setStyle("");
            txtFieldConsProcIAPPls.setStyle("");
            txtFieldConsErAPPls.setStyle("");

            if (addTglBtnConstAPPls.isSelected()) {
                double Uproc;
                double IbProc;
                double errorRange;

                try {
                     Uproc = Double.parseDouble(txtFieldConsProcUAPPls.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcUAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPPls.setSelected(false);
                    return;
                }

                try {
                     IbProc = Double.parseDouble(txtFieldConsProcIAPPls.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcIAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPPls.setSelected(false);
                    return;
                }

                try {
                    errorRange = Double.parseDouble(txtFieldConsErAPPls.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsErAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPPls.setSelected(false);
                    return;
                }

                if (radBtnConstTimeAPPls.isSelected()) {
                    String time = txtFieldConstTimeAPPls.getText();
                    String[] arrTime = time.split(":");

                    if (time.length() != 8 || arrTime.length != 3) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeAPPls.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
                        addTglBtnConstAPPls.setSelected(false);
                        return;
                    }

                    try {
                        String hours = arrTime[0];
                        String mins = arrTime[1];
                        String seks = arrTime[2];

                        long timeTimeTestToMill = ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;

                        if (timeTimeTestToMill < 60) {
                            ConsoleHelper.infoException("Время теста не должно быть меньше:\n60 секунд");
                            txtFieldConstTimeAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                            addTglBtnConstAPPls.setSelected(false);
                            return;
                        }

                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPPls.setSelected(false);
                        return;
                    }

                    testListForCollumAPPls.add(new ConstantCommand(false, true, time,
                            "CNT;T;A;P", "Сонстанта AP+", Uproc, IbProc, 0, 0, -errorRange, errorRange));

                } else {
                    double testEnergy;
                    try {
                        testEnergy = Double.parseDouble(txtFieldEngConstAPPls.getText());
                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные");
                        txtFieldEngConstAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPPls.setSelected(false);
                        return;
                    }

                    if (testEnergy < 0.1) {
                        ConsoleHelper.infoException("Неверные данные\nэнергия не должна быть меньше 0.1кВ");
                        txtFieldEngConstAPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPPls.setSelected(false);
                        return;
                    }

                    testListForCollumAPPls.add(new ConstantCommand(false, false, testEnergy,
                            "CNT;E;A;P", "Сонстанта AP+", Uproc, IbProc, 0, 0, -errorRange, errorRange));

                }

                txtFieldConstTimeAPPls.setDisable(true);
                txtFieldEngConstAPPls.setDisable(true);
                txtFieldConsProcUAPPls.setDisable(true);
                txtFieldConsProcIAPPls.setDisable(true);
                txtFieldConsErAPPls.setDisable(true);
                radBtnConstTimeAPPls.setDisable(true);
                radBtnConstEnergyAPPls.setDisable(true);

                ConstTogBtnAPPls.setSelected(true);

            } else {
                for (Commands commands : testListForCollumAPPls) {
                    if (commands instanceof ConstantCommand) {
                        if (commands.getName().equals("Сонстанта AP+")) {
                            testListForCollumAPPls.remove(commands);
                            break;
                        }
                    }
                }

                txtFieldConstTimeAPPls.setDisable(false);
                txtFieldEngConstAPPls.setDisable(false);
                txtFieldConsProcUAPPls.setDisable(false);
                txtFieldConsProcIAPPls.setDisable(false);
                txtFieldConsErAPPls.setDisable(false);
                radBtnConstTimeAPPls.setDisable(false);
                radBtnConstEnergyAPPls.setDisable(false);

                ConstTogBtnAPPls.setSelected(false);
            }
        }

        if (event.getSource() == addTglBtnConstAPMns) {

            txtFieldConstTimeAPMns.setStyle("");
            txtFieldEngConstAPMns.setStyle("");
            txtFieldConsProcUAPMns.setStyle("");
            txtFieldConsProcIAPMns.setStyle("");
            txtFieldConsErAPMns.setStyle("");

            if (addTglBtnConstAPMns.isSelected()) {
                double Uproc;
                double IbProc;
                double errorRange;

                try {
                    Uproc = Double.parseDouble(txtFieldConsProcUAPMns.getText());
                } catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcUAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPMns.setSelected(false);
                    return;
                }

                try {
                    IbProc = Double.parseDouble(txtFieldConsProcIAPMns.getText());
                } catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcIAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPMns.setSelected(false);
                    return;
                }

                try {
                    errorRange = Double.parseDouble(txtFieldConsErAPMns.getText());
                } catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsErAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPMns.setSelected(false);
                    return;
                }

                if (radBtnConstTimeAPMns.isSelected()) {
                    String time = txtFieldConstTimeAPMns.getText();
                    String[] arrTime = time.split(":");

                    if (time.length() != 8 || arrTime.length != 3) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeAPMns.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
                        addTglBtnConstAPMns.setSelected(false);
                        return;
                    }

                    try {
                        String hours = arrTime[0];
                        String mins = arrTime[1];
                        String seks = arrTime[2];

                        long timeTimeTestToMill = ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;

                        if (timeTimeTestToMill < 60) {
                            ConsoleHelper.infoException("Время теста не должно быть меньше:\n60 секунд");
                            txtFieldConstTimeAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                            addTglBtnConstAPMns.setSelected(false);
                            return;
                        }

                    } catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPMns.setSelected(false);
                        return;
                    }

                    testListForCollumAPMns.add(new ConstantCommand(false, true, time,
                            "CNT;T;A;N", "Сонстанта AP-", Uproc, IbProc, 0, 0, -errorRange, errorRange));


                } else {
                    double testEnergy;
                    try {
                        testEnergy = Double.parseDouble(txtFieldEngConstAPMns.getText());
                    } catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные");
                        txtFieldEngConstAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPMns.setSelected(false);
                        return;
                    }

                    if (testEnergy < 0.1) {
                        ConsoleHelper.infoException("Неверные данные\nэнергия не должна быть меньше 0.1кВ");
                        txtFieldEngConstAPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPMns.setSelected(false);
                        return;
                    }

                    testListForCollumAPMns.add(new ConstantCommand(false, false, testEnergy,
                            "CNT;E;A;N", "Сонстанта AP-", Uproc, IbProc, 0, 0, -errorRange, errorRange));

                }

                txtFieldConstTimeAPMns.setDisable(true);
                txtFieldEngConstAPMns.setDisable(true);
                txtFieldConsProcUAPMns.setDisable(true);
                txtFieldConsProcIAPMns.setDisable(true);
                txtFieldConsErAPMns.setDisable(true);
                radBtnConstTimeAPMns.setDisable(true);
                radBtnConstEnergyAPMns.setDisable(true);

                ConstTogBtnAPMns.setSelected(true);

            } else {
                for (Commands commands : testListForCollumAPMns) {
                    if (commands instanceof ConstantCommand) {
                        if (commands.getName().equals("Сонстанта AP-")) {
                            testListForCollumAPMns.remove(commands);
                            break;
                        }
                    }
                }

                txtFieldConstTimeAPMns.setDisable(false);
                txtFieldEngConstAPMns.setDisable(false);
                txtFieldConsProcUAPMns.setDisable(false);
                txtFieldConsProcIAPMns.setDisable(false);
                txtFieldConsErAPMns.setDisable(false);
                radBtnConstTimeAPMns.setDisable(false);
                radBtnConstEnergyAPMns.setDisable(false);

                ConstTogBtnAPMns.setSelected(false);
            }
        }

        if (event.getSource() == addTglBtnConstRPPls) {

            txtFieldConstTimeRPPls.setStyle("");
            txtFieldEngConstRPPls.setStyle("");
            txtFieldConsProcURPPls.setStyle("");
            txtFieldConsProcIRPPls.setStyle("");
            txtFieldConsErRPPls.setStyle("");

            if (addTglBtnConstRPPls.isSelected()) {
                double Uproc;
                double IbProc;
                double errorRange;

                try {
                    Uproc = Double.parseDouble(txtFieldConsProcURPPls.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcURPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPPls.setSelected(false);
                    return;
                }

                try {
                    IbProc = Double.parseDouble(txtFieldConsProcIRPPls.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcIRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPPls.setSelected(false);
                    return;
                }

                try {
                    errorRange = Double.parseDouble(txtFieldConsErRPPls.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsErRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPPls.setSelected(false);
                    return;
                }

                if (radBtnConstTimeRPPls.isSelected()) {
                    String time = txtFieldConstTimeRPPls.getText();
                    String[] arrTime = time.split(":");

                    if (time.length() != 8 || arrTime.length != 3) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeRPPls.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
                        addTglBtnConstRPPls.setSelected(false);
                        return;
                    }

                    try {
                        String hours = arrTime[0];
                        String mins = arrTime[1];
                        String seks = arrTime[2];

                        long timeTimeTestToMill = ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;

                        if (timeTimeTestToMill < 60) {
                            ConsoleHelper.infoException("Время теста не должно быть меньше:\n60 секунд");
                            txtFieldConstTimeRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                            addTglBtnConstRPPls.setSelected(false);
                            return;
                        }

                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPPls.setSelected(false);
                        return;
                    }

                    testListForCollumRPPls.add(new ConstantCommand(false, true, time,
                            "const;T;R;P", "Сонстанта RP+", Uproc, IbProc, 0, 0, -errorRange, errorRange));


                } else {
                    double testEnergy;
                    try {
                        testEnergy = Double.parseDouble(txtFieldEngConstRPPls.getText());
                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные");
                        txtFieldEngConstRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPPls.setSelected(false);
                        return;
                    }

                    if (testEnergy < 0.1) {
                        ConsoleHelper.infoException("Неверные данные\nэнергия не должна быть меньше 0.1кВ");
                        txtFieldEngConstRPPls.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPPls.setSelected(false);
                        return;
                    }

                    testListForCollumRPPls.add(new ConstantCommand(false, false, testEnergy,
                            "CNT;E;R;P", "Сонстанта RP+", Uproc, IbProc, 0, 0, -errorRange, errorRange));

                }

                txtFieldConstTimeRPPls.setDisable(true);
                txtFieldEngConstRPPls.setDisable(true);
                txtFieldConsProcURPPls.setDisable(true);
                txtFieldConsProcIRPPls.setDisable(true);
                txtFieldConsErRPPls.setDisable(true);
                radBtnConstTimeRPPls.setDisable(true);
                radBtnConstEnergyRPPls.setDisable(true);

                ConstTogBtnRPPls.setSelected(true);

            } else {
                for (Commands commands : testListForCollumRPPls) {
                    if (commands instanceof ConstantCommand) {
                        if (commands.getName().equals("Сонстанта RP+")) {
                            testListForCollumRPPls.remove(commands);
                            break;
                        }
                    }
                }

                txtFieldConstTimeRPPls.setDisable(false);
                txtFieldEngConstRPPls.setDisable(false);
                txtFieldConsProcURPPls.setDisable(false);
                txtFieldConsProcIRPPls.setDisable(false);
                txtFieldConsErRPPls.setDisable(false);
                radBtnConstTimeRPPls.setDisable(false);
                radBtnConstEnergyRPPls.setDisable(false);

                ConstTogBtnRPPls.setSelected(false);
            }
        }

        if (event.getSource() == addTglBtnConstRPMns) {

            txtFieldConstTimeRPMns.setStyle("");
            txtFieldEngConstRPMns.setStyle("");
            txtFieldConsProcURPMns.setStyle("");
            txtFieldConsProcIRPMns.setStyle("");
            txtFieldConsErRPMns.setStyle("");

            if (addTglBtnConstRPMns.isSelected()) {
                double Uproc;
                double IbProc;
                double errorRange;

                try {
                    Uproc = Double.parseDouble(txtFieldConsProcURPMns.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcURPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPMns.setSelected(false);
                    return;
                }

                try {
                    IbProc = Double.parseDouble(txtFieldConsProcIRPMns.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcIRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPMns.setSelected(false);
                    return;
                }

                try {
                    errorRange = Double.parseDouble(txtFieldConsErRPMns.getText());
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsErRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPMns.setSelected(false);
                    return;
                }

                if (radBtnConstTimeRPMns.isSelected()) {
                    String time = txtFieldConstTimeRPMns.getText();
                    String[] arrTime = time.split(":");

                    if (time.length() != 8 || arrTime.length != 3) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeRPMns.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
                        addTglBtnConstRPMns.setSelected(false);
                        return;
                    }

                    try {
                        String hours = arrTime[0];
                        String mins = arrTime[1];
                        String seks = arrTime[2];

                        long timeTimeTestToMill = ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;

                        if (timeTimeTestToMill < 60) {
                            ConsoleHelper.infoException("Время теста не должно быть меньше:\n60 секунд");
                            txtFieldConstTimeRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                            addTglBtnConstRPMns.setSelected(false);
                            return;
                        }

                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPMns.setSelected(false);
                        return;
                    }

                    testListForCollumRPMns.add(new ConstantCommand(false, true, time,
                            "CNT;T;R;N", "Сонстанта RP-", Uproc, IbProc, 0, 0, -errorRange, errorRange));


                } else {
                    double testEnergy;
                    try {
                        testEnergy = Double.parseDouble(txtFieldEngConstRPMns.getText());
                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные");
                        txtFieldEngConstRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPMns.setSelected(false);
                        return;
                    }

                    if (testEnergy < 0.1) {
                        ConsoleHelper.infoException("Неверные данные\nэнергия не должна быть меньше 0.1кВ");
                        txtFieldEngConstRPMns.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPMns.setSelected(false);
                        return;
                    }

                    testListForCollumRPMns.add(new ConstantCommand(false, false, testEnergy,
                            "CNT;E;R;N", "Сонстанта RP-", Uproc, IbProc, 0, 0, -errorRange, errorRange));

                }

                txtFieldConstTimeRPMns.setDisable(true);
                txtFieldEngConstRPMns.setDisable(true);
                txtFieldConsProcURPMns.setDisable(true);
                txtFieldConsProcIRPMns.setDisable(true);
                txtFieldConsErRPMns.setDisable(true);
                radBtnConstTimeRPMns.setDisable(true);
                radBtnConstEnergyRPMns.setDisable(true);

                ConstTogBtnRPMns.setSelected(true);

            } else {
                for (Commands commands : testListForCollumRPMns) {
                    if (commands instanceof ConstantCommand) {
                        if (commands.getName().equals("Сонстанта RP-")) {
                            testListForCollumRPMns.remove(commands);
                            break;
                        }
                    }
                }

                txtFieldConstTimeRPMns.setDisable(false);
                txtFieldEngConstRPMns.setDisable(false);
                txtFieldConsProcURPMns.setDisable(false);
                txtFieldConsProcIRPMns.setDisable(false);
                txtFieldConsErRPMns.setDisable(false);
                radBtnConstTimeRPMns.setDisable(false);
                radBtnConstEnergyRPMns.setDisable(false);

                ConstTogBtnRPMns.setSelected(true);
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

    //Имитация ToggleGroup
    private void setGropToggleButton(ActionEvent event) {
        if (event.getSource() == onePhaseBtn) {
            onePhaseBtn.setSelected(true);
            APhaseBtn.setSelected(false);
            BPhaseBtn.setSelected(false);
        }

        if (event.getSource() == APhaseBtn) {
            APhaseBtn.setSelected(true);
            onePhaseBtn.setSelected(false);
            BPhaseBtn.setSelected(false);
        }

        if (event.getSource() == BPhaseBtn) {
            BPhaseBtn.setSelected(true);
            onePhaseBtn.setSelected(false);
            APhaseBtn.setSelected(false);
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
        onePhaseBtn.setSelected(true);
        APhaseBtn.setSelected(false);
        BPhaseBtn.setSelected(false);
    }

    private void gridPaneToFront(GridPane pane) {
        pane.toFront();
    }

    //Выдаёт нужный GridPane в зависимости от нажатой кнопки
    private GridPane getGridPane() {
        if (onePhaseBtn.isSelected()) {
            if (APPlus.isSelected()) return gridPaneOnePhaseAPPlus;
            if (APMinus.isSelected()) return gridPaneOnePhaseAPMinus;
            if (RPPlus.isSelected()) return gridPaneOnePhaseRPPlus;
            if (RPMinus.isSelected()) return gridPaneOnePhaseRPMinus;
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
        return null;
    }

    //Добавляет тестовую точку в методику
    private void addTestPointInMethodic(String testPoint) {

        String[] dirCurFactor = testPoint.split(";");

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
            testListForCollumAPPls.add(new ErrorCommand(false, testPoint, phase, current, 0, percent, iABC, powerFactor, 0));
        }
        if (energyType.equals("A") && currentDirection.equals("N")) {
            testListForCollumAPMns.add(new ErrorCommand(false, testPoint, phase, current, 1, percent, iABC, powerFactor, 1));
        }

        if (energyType.equals("R") && currentDirection.equals("P")) {
            testListForCollumRPPls.add(new ErrorCommand(false, testPoint, phase, current, 0, percent, iABC, powerFactor, 2));
        }

        if (energyType.equals("R") && currentDirection.equals("N")) {
            testListForCollumRPMns.add(new ErrorCommand(false, testPoint, phase, current, 1, percent, iABC, powerFactor, 3));
        }
    }

    private void deleteTestPointInMethodic(String idCheckBox) {
        ErrorCommand errorCommand;
        String[] point = idCheckBox.split(";");

        if (point[2].equals("A") && point[3].equals("P")) {

            for (Commands current : testListForCollumAPPls) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getId().equals(idCheckBox)) {
                        testListForCollumAPPls.remove(current);
                        break;
                    }
                }
            }

        } else if (point[2].equals("A") && point[3].equals("N")) {

            for (Commands current : testListForCollumAPMns) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getId().equals(idCheckBox)) {
                        testListForCollumAPMns.remove(current);
                        break;
                    }
                }
            }

        } else if (point[2].equals("R") && point[3].equals("P")) {

            for (Commands current : testListForCollumRPPls) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getId().equals(idCheckBox)) {
                        testListForCollumRPPls.remove(current);
                        break;
                    }
                }
            }
        }

        if (point[2].equals("R") && point[3].equals("N")) {

            for (Commands current : testListForCollumRPMns) {
                if (current instanceof ErrorCommand) {
                    errorCommand = (ErrorCommand) current;
                    if (errorCommand.getId().equals(idCheckBox)) {
                        testListForCollumRPMns.remove(current);
                        break;
                    }
                }
            }
        }
    }

    public void setMethodicNameController(MethodicNameController methodicNameController) {
        this.methodicNameController = methodicNameController;
    }

    public void setMethodicsAddEditDeleteFrameController(MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController) {
        this.methodicsAddEditDeleteFrameController = methodicsAddEditDeleteFrameController;
    }

    public void setMethodicForOnePhaseStend(MethodicForOnePhaseStend methodicForOnePhaseStend) {
        this.methodicForOnePhaseStend = methodicForOnePhaseStend;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public void setBindParameters(boolean bindParameters) {
        this.bindParameters = bindParameters;
    }

    public ToggleButton getParametersBtn() {
        return parametersBtn;
    }

    @Override
    public Stage getStage() {
        return null;
    }
}
