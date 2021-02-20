package org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.taipit.stend.controller.сommands.*;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicNameController;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.Frame;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;
import org.taipit.stend.model.metodics.MetodicsForTest;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна "addEditPointsOnePhaseStendMet.fxml".
 * Данный класс отвечает за возможность добавления, редактирования точек испытания в методику поверки для однофазного стенда"
 */
public class AddEditPointsOnePhaseStendFrameController implements  Frame {

    //Ссылка на данный контроллер
    private AddEditPointsOnePhaseStendFrameController addEditPointsOnePhaseStendFrameController = this;

    //Список всех методик поверки
    private MetodicsForTest metodicsForTest = MetodicsForTest.getMetodicsForTestInstance();

    //Ссылка на окно где можно добавить точки испытаний с влиянием частоты сети и напряжения
    private InfluencePointsOnePhaseStendFrame influencePointsOnePhaseStendFrame;

    //Имя созданной методики можно получить из окна
    private MethodicNameController methodicNameController;

    //Окно выбора для создания или редактированя методики
    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    //Объектр данного класса
    private MethodicForOnePhaseStend methodicForOnePhaseStend;

    //Если пользователь вводит параметры испытания на момент создания методики
    private boolean bindParameters;

    //Это окно вызвано кнопкой редактировать (выбрано изменить уже существующую методику)?
    private boolean edit;

    //Значения коэффициента мощности
    private List<String> powerFactor;

    //Значения выставленного тока
    private List<String> current;

    //gridPane выбора точек методики
    private List<GridPane> gridPanesEnergyAndPhase = new ArrayList<>();

    //Сохранены ли изменения методики
    private boolean saveChange = false;

    //Лист с точками общая методика
    //Активная энергия в прямом направлении тока
    private ObservableList<Commands> testListForCollumAPPls = FXCollections.observableArrayList(new ArrayList<>());
    //Активная энергия в обратном направлении тока
    private ObservableList<Commands> testListForCollumAPMns = FXCollections.observableArrayList(new ArrayList<>());
    //Реактивная энергия в прямом направлении тока
    private ObservableList<Commands> testListForCollumRPPls = FXCollections.observableArrayList(new ArrayList<>());
    //Реактивная энергия в обратном направлении тока
    private ObservableList<Commands> testListForCollumRPMns = FXCollections.observableArrayList(new ArrayList<>());

    @FXML
    private AnchorPane mainAnchorPane;

    private ScrollPane mainScrollPane = new ScrollPane();

    private StackPane stackPaneForGridPane = new StackPane();

    private ScrollPane scrollPaneForCurrent = new ScrollPane();
    private ScrollPane scrollPaneForPowerFactor = new ScrollPane();
    private GridPane gridPaneForCurrent = new GridPane();
    private GridPane gridPaneForPowerFactor = new GridPane();

    //Закрашивающий квадрат
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

    @FXML
    private TableColumn<Commands, String> timeStabTabColAPPls;

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

    @FXML
    private TableColumn<Commands, String> timeStabTabColAPMns;

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

    @FXML
    private TableColumn<Commands, String> timeStabTabColRPPls;

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
    private TableColumn<Commands, String> timeStabTabColRPMns;

    //-------------------------------------------------------
    //Данный блок отвечает за сетку выбора точки.
    //Активная энергия в прямом направлении, Одна фаза (H) и если стенд имеет две токовые цепи A B
    private GridPane gridPaneOnePhaseAPPlus = new GridPane();
    private GridPane gridPanePhaseAAPPlus = new GridPane();
    private GridPane gridPanePhaseBAPPlus = new GridPane();

    //Активная энергия в обратном направлении, Одна фаза (H) и если стенд имеет две токовые цепи A B
    private GridPane gridPaneOnePhaseAPMinus = new GridPane();
    private GridPane gridPanePhaseAAPMinus = new GridPane();
    private GridPane gridPanePhaseBAPMinus = new GridPane();

    //Реактивная энергия в прямом направлении, Одна фаза (H) и если стенд имеет две токовые цепи A B
    private GridPane gridPaneOnePhaseRPPlus = new GridPane();
    private GridPane gridPanePhaseARPPlus = new GridPane();
    private GridPane gridPanePhaseBRPPlus = new GridPane();

    //Реактивная энергия в обратном направлении, Одна фаза (H) и если стенд имеет две токовые цепи A B
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
    private Button saveBtn;

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

    //--------------------------------------------------------------- Реле
    @FXML
    private ToggleButton tglBtnRelay;

    @FXML
    private Pane paneRelay;

    @FXML
    private Label labelDirection;

    @FXML
    private TextField txtFieldRelayCurrent;

    @FXML
    private TextField txtFieldRelayTime;

    @FXML
    private TextField txtFieldAmountImpRelay;

    @FXML
    private ToggleButton addTglBtnRelay;


    //Устанавливает имя методики полученное с окна создания и присвоения имени
    public void setTextFielMethodicName() {
        metodicNameTxtFld.setText(methodicForOnePhaseStend.getMetodicName());
    }

    @FXML
    void initialize() {

        //Получаю параметры для построения сетки выбора точки
        current = Arrays.asList(ConsoleHelper.properties.getProperty("currentForMethodicPane").split(", "));
        powerFactor = Arrays.asList(ConsoleHelper.properties.getProperty("powerFactorForMetodicPane").split(", "));

        setIdGridPanes();

        createGridPaneAndSetCheckBoxes();

        initMainScrollPane();

        initScrolPaneForCurrentAndPowerFactor();

        createScrollPanesForGridPane();

        //Редактирование параметров сетки выбора точек (Изменение значений коэффициента мощности и тока)
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

        APPlus.fire();

        //Добавляю в группы выбор режима испытания "Проверка счётного механизма" либо по времени, либо по кол-ву энергии
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

        //Изначальное положение кнопок
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
    //Контейнер, где находятся сетки с выбором точек испытания
    private void initMainScrollPane() {
        mainScrollPane.setPrefHeight(230);
        mainScrollPane.setPrefWidth(643);
        mainScrollPane.setLayoutX(135);
        mainScrollPane.setLayoutY(175);

        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        mainScrollPane.setStyle("-fx-background: #858585;");

        //Устанавливаю стиль
        String cssAdress = getClass().getClassLoader().getResource("styleCSS/scrollPane.css").toString();
        if (cssAdress != null) {
            mainScrollPane.getStylesheets().add(cssAdress);
        }

        mainScrollPane.setContent(stackPaneForGridPane);

        mainAnchorPane.getChildren().add(mainScrollPane);

        stackPaneForGridPane.getChildren().addAll(gridPanesEnergyAndPhase);
    }

    /**
     * Устанавливает id, необходимые в дальней для выбора точки испытания
     */
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

    /**
     * Формирует сетки выбора точек испытаний
     */
    private void createGridPaneAndSetCheckBoxes() {
        createRowAndColumnForGridPane();
        setCheckBoxAndLabelInGridPane();
    }

    /**
     * Создаёт сетку необходимого размера
     */
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

    /**
     * Помещает чек боксы в сетку и задаёт им ID
     */
    private void setCheckBoxAndLabelInGridPane() {
        String cssAdress = getClass().getClassLoader().getResource("styleCSS/addDeleteEditPointsFrame/checkBox.css").toString();

        CheckBox checkBox;

        //Прохожусь по всем сеткам созданным ранее
        for (GridPane gridPane : gridPanesEnergyAndPhase) {

            gridPane.setGridLinesVisible(true);

            for (int x = 0; x < current.size(); x++) {
                for (int y = 0; y < powerFactor.size(); y++) {

                    //Устанавливаю CheckBox в нужную и соответствующую ячейку
                    checkBox = new CheckBox();

                    if (cssAdress != null) {
                        checkBox.getStylesheets().add(cssAdress);
                    }

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

    /**
     * Устанавливает слушатель при изменении значения чек бокса
     * Либо добавляет либо удаляет точку испытания
     */
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

    /**
     * Формирует дополнительные scrollPane
     * это необходимо чтобы при скроллинге основного скролл пэйна смещалась и
     * информация коэф. мощности и тока принадлежащая столбу и строчке
     */
    private void createScrollPanesForGridPane() {
        //Curr
        scrollPaneForCurrent.setMinHeight(0);
        scrollPaneForCurrent.setPrefHeight(24);
        scrollPaneForCurrent.setStyle("-fx-background: #B8B8B8;" +
                "-fx-background-insets: 0, 0 1 1 0;" +
                "-fx-background-color: #B8B8B8;");

        scrollPaneForCurrent.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForCurrent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForCurrent.setLayoutX(135);
        scrollPaneForCurrent.setLayoutY(175);

        scrollPaneForCurrent.setPrefWidth(mainScrollPane.getPrefWidth() - 13);
        mainAnchorPane.getChildren().add(scrollPaneForCurrent);

        //PF
        scrollPaneForPowerFactor.setMinWidth(0);
        scrollPaneForPowerFactor.setPrefWidth(50);
        scrollPaneForPowerFactor.setStyle("-fx-background: #B8B8B8;" +
                "-fx-background-insets: 0, 0 1 1 0;" +
                "-fx-background-color: #B8B8B8;");

        scrollPaneForPowerFactor.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForPowerFactor.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForPowerFactor.setLayoutX(135);
        scrollPaneForPowerFactor.setLayoutY(175);

        scrollPaneForPowerFactor.setPrefHeight(mainScrollPane.getPrefHeight() - 13);
        mainAnchorPane.getChildren().add(scrollPaneForPowerFactor);

        gridPaneForPowerFactor.setPrefHeight(gridPaneOnePhaseAPPlus.getHeight());
        scrollPaneForPowerFactor.setContent(gridPaneForPowerFactor);

        //Закрывающий квадрат(левый верхний угол mainScrollPane)
        fillSquare = new Pane();
        fillSquare.setStyle("-fx-background-color: #B8B8B8;");
        fillSquare.setPrefHeight(23);
        fillSquare.setPrefWidth(50);
        fillSquare.setLayoutX(135);
        fillSquare.setLayoutY(175);
        mainAnchorPane.getChildren().add(fillSquare);

        btnAddDeleteTestPoints.setText("Точки");
        btnAddDeleteTestPoints.setMinHeight(0);
        btnAddDeleteTestPoints.setPrefSize(fillSquare.getPrefWidth(), fillSquare.getPrefHeight());

        String cssAdress = getClass().getClassLoader().getResource("styleCSS/addDeleteEditPointsFrame/buttonSetParam.css").toString();
        if (cssAdress != null) {
            btnAddDeleteTestPoints.getStylesheets().add(cssAdress);
        }

        fillSquare.getChildren().add(btnAddDeleteTestPoints);
    }

    /**
     * Помещаю на созданные скролл пэйны информацию к какому столбу сетки какое принадлежит значение тока
     * и какой строчке какое значение коэф. мощности
     */
    private void initScrolPaneForCurrentAndPowerFactor() {
        gridPaneForCurrent.setPrefWidth(gridPaneOnePhaseAPPlus.getWidth());
        scrollPaneForCurrent.setContent(gridPaneForCurrent);

        gridPaneForCurrent.setGridLinesVisible(true);
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

    /**
     * Формирует дополнительные scrollPane
     * это необходимо чтобы при скроллинге основного скролл пэйна смещалась и
     * информация коэф. мощности и тока принадлежащая столбу и строчке
     * Но без закращивающего смещение квадрата
     */
    private void createScrollPanesForGridPaneWithoutSquare() {
        //Curr
        scrollPaneForCurrent.setMinHeight(0);
        scrollPaneForCurrent.setPrefHeight(24);
        scrollPaneForCurrent.setStyle("-fx-background: #E0E0E0;" +
                "-fx-background-insets: 0, 0 1 1 0;" +
                "-fx-background-color: #E0E0E0;");

        scrollPaneForCurrent.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForCurrent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneForCurrent.setLayoutX(135);
        scrollPaneForCurrent.setLayoutY(175);

        scrollPaneForCurrent.setPrefWidth(mainScrollPane.getPrefWidth() - 13);
        mainAnchorPane.getChildren().add(scrollPaneForCurrent);

        //PF
        scrollPaneForPowerFactor.setMinWidth(0);
        scrollPaneForPowerFactor.setPrefWidth(50);
        scrollPaneForPowerFactor.setStyle("-fx-background: #E0E0E0;" +
                "-fx-background-insets: 0, 0 1 1 0;" +
                "-fx-background-color: #E0E0E0;");

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

    /**
     * Связывает между собой скролл бары основого контейнера, который содержит gridpane's
     * и скрол бары информации о строках и столбах грид пайнес
     */
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

    //======================= Всё что связано с отображением выбранных точек в TableView ===============================
    /**
     * Инициализирует таблицу для отображения выбранных пользователем точек испытания (ErrorCommand's)
     */
    public void initTableView() {
        //Добавляю в лист все колонки с информацией о точке испытания для
        // точек активной энергии в прямом направлении тока (AP+)
        List<TableColumn<Commands, String>> collumnListAPPls = Arrays.asList(
                loadCurrTabColAPPls,
                eMaxTabColAPPls,
                eMinTabColAPPls,
                amountImplTabColAPPls,
                amountMeasTabColAPPls,
                timeStabTabColAPPls
        );

        //Добавляю в лист все колонки с информацией о точке испытания для
        // точек активной энергии в обратном направлении тока (AP-)
        List<TableColumn<Commands, String>> collumnListAPMns = Arrays.asList(
                loadCurrTabColAPMns,
                eMaxTabColAPMns,
                eMinTabColAPMns,
                amountImplTabColAPMns,
                amountMeasTabColAPMns,
                timeStabTabColAPMns
        );

        //Добавляю в лист все колонки с информацией о точке испытания для
        // точек реактивной энергии в прямом направлении тока (RP+)
        List<TableColumn<Commands, String>> collumnListRPPls = Arrays.asList(
                loadCurrTabColRPPls,
                eMaxTabColRPPls,
                eMinTabColRPPls,
                amountImplTabColRPPls,
                amountMeasTabColRPPls,
                timeStabTabColRPPls
        );

        //Добавляю в лист все колонки с информацией о точке испытания для
        // точек реактивной энергии в обратном направлении тока (RP-)
        List<TableColumn<Commands, String>> collumnListRPMns = Arrays.asList(
                loadCurrTabColRPMns,
                eMaxTabColRPMns,
                eMinTabColRPMns,
                amountImplTabColRPMns,
                amountMeasTabColRPMns,
                timeStabTabColRPMns
        );

        //Добавляю все лиcты в мап с индексами напрвления и типа мощности (необходимо для локаничной инициализации)
        Map<Integer, List<TableColumn<Commands, String>>> mapTableColumn = new HashMap<>();
        mapTableColumn.put(0, collumnListAPPls);
        mapTableColumn.put(1, collumnListAPMns);
        mapTableColumn.put(2, collumnListRPPls);
        mapTableColumn.put(3, collumnListRPMns);

        //Инициализация всех TableView
        for (int i = 0; i < mapTableColumn.size(); i++) {
            //Устанавливаем данные для колонок (AP+, AP-, RP+, RP-)
            mapTableColumn.get(i).get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
            mapTableColumn.get(i).get(1).setCellValueFactory(new PropertyValueFactory<>("emax"));
            mapTableColumn.get(i).get(2).setCellValueFactory(new PropertyValueFactory<>("emin"));
            mapTableColumn.get(i).get(3).setCellValueFactory(new PropertyValueFactory<>("pulse"));
            mapTableColumn.get(i).get(4).setCellValueFactory(new PropertyValueFactory<>("countResult"));
            mapTableColumn.get(i).get(5).setCellValueFactory(new PropertyValueFactory<>("pauseForStabilization"));

            //Выставляем отображение информации в колонке "по центру"
            mapTableColumn.get(i).get(1).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(2).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(3).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(4).setStyle( "-fx-alignment: CENTER;");
            mapTableColumn.get(i).get(5).setStyle( "-fx-alignment: CENTER;");

            //Устанавливаем возможность редактирования информации в колонке
            mapTableColumn.get(i).get(1).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(2).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(3).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(4).setCellFactory(TextFieldTableCell.forTableColumn());
            mapTableColumn.get(i).get(5).setCellFactory(TextFieldTableCell.forTableColumn());


            //------------------------------------Действие при изменении информации в колонке

            //Изменение максимальной погрешности Emax
            mapTableColumn.get(i).get(1).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                if (command instanceof ErrorCommand) {
                    //проверка введённого пользователем значения на валидность
                    try {
                        Float.parseFloat(newImpulseValue);
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        ConsoleHelper.infoException("Неверные данные\nЗначение поля должно быть десятичным");
                        event.getTableView().refresh();
                        return;
                    }

                    command.setEmax(newImpulseValue);

                    if (!newImpulseValue.equals(event.getOldValue())) {
                        saveChange = true;
                    }

                } else {
                    event.getTableView().refresh();
                }
            });

            //Изменение минимальной погрешности Emin
            mapTableColumn.get(i).get(2).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                if (command instanceof ErrorCommand) {
                    try {
                        Float.parseFloat(newImpulseValue);
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        ConsoleHelper.infoException("Неверные данные\nЗначение поля должно быть десятичным");
                        event.getTableView().refresh();
                        return;
                    }
                    command.setEmin(newImpulseValue);

                    if (!newImpulseValue.equals(event.getOldValue())) {
                        saveChange = true;
                    }
                } else {
                    event.getTableView().refresh();
                }
            });

            //Изменение количества импульсов измерения
            mapTableColumn.get(i).get(3).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                if (command instanceof ErrorCommand) {
                    try {
                        Float.parseFloat(newImpulseValue);
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        ConsoleHelper.infoException("Неверные данные\nЗначение поля должно быть десятичным");
                        event.getTableView().refresh();
                        return;
                    }
                    command.setPulse(newImpulseValue);

                    if (!newImpulseValue.equals(event.getOldValue())) {
                        saveChange = true;
                    }
                } else {
                    event.getTableView().refresh();
                }
            });

            //Изменение количества измерений
            mapTableColumn.get(i).get(4).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newImpulseValue = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                if (command instanceof ErrorCommand) {
                    command.setCountResult(newImpulseValue);

                    if (!newImpulseValue.equals(event.getOldValue())) {
                        saveChange = true;
                    }
                }
            });

            //Изменение паузы стабилизации
            mapTableColumn.get(i).get(5).setOnEditCommit((TableColumn.CellEditEvent<Commands, String> event) -> {
                TablePosition<Commands, String> pos = event.getTablePosition();

                String newTimeForStabilization = event.getNewValue();

                int row = pos.getRow();
                Commands command = event.getTableView().getItems().get(row);

                if (command instanceof ErrorCommand) {
                    try {
                        command.setPauseForStabilization(Double.parseDouble(newTimeForStabilization));
                        if (!newTimeForStabilization.equals(event.getOldValue())) {
                            saveChange = true;
                        }
                    }catch (NumberFormatException e) {
                        e.printStackTrace();
                        ConsoleHelper.infoException("Неверные данные\nЗначение поля должно быть численным");
                        event.getTableView().refresh();
                    }
                } else {
                    event.getTableView().refresh();
                }
            });
        }

        //Действие при перетаскивании строки
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

        //Компараторы для сортировки точек испытания (ПЕРЕПИСАТЬ)
        Comparator<String> comparatorForCommands = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                //1.0; 1.0 Imax
                //A; 1.0; 1.0 Imax

                String[] arrO1 = o1.split(";");
                String[] arrO2 = o2.split(";");

                if (arrO1.length == 2 && arrO2.length != 2) {
                    return -1;
                } else if (arrO1.length != 2 && arrO2.length == 2) {
                    return 1;
                } else if (arrO1.length == 2 && arrO2.length == 2) {

                    String[] curArr1 = arrO1[1].trim().split(" ");
                    String[] curArr2 = arrO2[1].trim().split(" ");

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

                            String pf1 = arrO1[0].trim();
                            String pf2 = arrO2[0].trim();

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

                } else if (arrO1.length == 3 && arrO2.length != 3) {
                    return -1;
                } else if (arrO1.length != 3 && arrO2.length == 3) {
                    return 1;
                } else if (arrO1.length == 3 && arrO2.length == 3) {

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

                        String[] curArr1 = newArrO1[1].trim().split(" ");
                        String[] curArr2 = newArrO2[1].trim().split(" ");

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

                                String pf1 = newArrO1[0].trim();
                                String pf2 = newArrO2[0].trim();

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
                } else return 0;
                return 0;
            }
        };

        //Устанавливаю компаратор таблицам
        loadCurrTabColAPPls.setComparator(comparatorForCommands);
        loadCurrTabColAPMns.setComparator(comparatorForCommands);
        loadCurrTabColRPPls.setComparator(comparatorForCommands);
        loadCurrTabColRPMns.setComparator(comparatorForCommands);

        //Оставляю сортировку только по имени точки
        eMaxTabColAPPls.setSortable(false);
        eMinTabColAPPls.setSortable(false);
        amountImplTabColAPPls.setSortable(false);
        amountMeasTabColAPPls.setSortable(false);
        timeStabTabColAPPls.setSortable(false);

        eMaxTabColAPMns.setSortable(false);
        eMinTabColAPMns.setSortable(false);
        amountImplTabColAPMns.setSortable(false);
        amountMeasTabColAPMns.setSortable(false);
        timeStabTabColAPMns.setSortable(false);

        eMaxTabColRPPls.setSortable(false);
        eMinTabColRPPls.setSortable(false);
        amountImplTabColRPPls.setSortable(false);
        amountMeasTabColRPPls.setSortable(false);
        timeStabTabColRPPls.setSortable(false);

        eMaxTabColRPMns.setSortable(false);
        eMinTabColRPMns.setSortable(false);
        amountImplTabColRPMns.setSortable(false);
        amountMeasTabColRPMns.setSortable(false);
        timeStabTabColRPMns.setSortable(false);

        //Устанавливаю возможность переноса строк
        viewPointTableAPPls.setRowFactory(dragAndRow);
        viewPointTableAPMns.setRowFactory(dragAndRow);
        viewPointTableRPPls.setRowFactory(dragAndRow);
        viewPointTableRPMns.setRowFactory(dragAndRow);

        //Возможность редактирования таблиц
        viewPointTableAPPls.setEditable(true);
        viewPointTableAPMns.setEditable(true);
        viewPointTableRPPls.setEditable(true);
        viewPointTableRPMns.setEditable(true);

        //Информация если таблица пустая
        viewPointTableAPPls.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableAPMns.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableRPPls.setPlaceholder(new Label("Не выбрано ни одной точки"));
        viewPointTableRPMns.setPlaceholder(new Label("Не выбрано ни одной точки"));

        //Добавляю точки испытаний в таблицу
        viewPointTableAPPls.setItems(testListForCollumAPPls);
        viewPointTableAPMns.setItems(testListForCollumAPMns);
        viewPointTableRPPls.setItems(testListForCollumRPPls);
        viewPointTableRPMns.setItems(testListForCollumRPMns);
    }

    //========== Всё для инициализации уже ранее созданной методики (нажата кнопка редактирование) ============
    //
    //Необходимо для команды Редактирования методики

    /**
     * Проверияет нет ли данных с полученной методики и если у неё есть данные, то клонирует точки в это окно.
     * Клонирование для того чтобы вернуться к исходной методике если пользователь нажмёт кнопку отмены изменений
     */
    public void initEditsMetodic() {
        try {
            //Добавление точек (ErrorCommands)
            for (Commands command : methodicForOnePhaseStend.getCommandsMap().get(0)) {
                testListForCollumAPPls.add(command.clone());
            }

            for (Commands command : methodicForOnePhaseStend.getCommandsMap().get(1)) {
                testListForCollumAPMns.add(command.clone());
            }

            for (Commands command : methodicForOnePhaseStend.getCommandsMap().get(2)) {
                testListForCollumRPPls.add(command.clone());
            }

            for (Commands command : methodicForOnePhaseStend.getCommandsMap().get(3)) {
                testListForCollumRPMns.add(command.clone());
            }

            //Добавлений других команд
            for (Commands command : methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(0)) {
                testListForCollumAPPls.add(command.clone());
            }

            for (Commands command : methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(1)) {
                testListForCollumAPMns.add(command.clone());
            }

            for (Commands command : methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(2)) {
                testListForCollumRPPls.add(command.clone());
            }

            for (Commands command : methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(3)) {
                testListForCollumRPMns.add(command.clone());
            }

        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
            ConsoleHelper.infoException(e.getMessage());
        }
    }

    /**
     * Данный метод используется если нажата кнопка редактирования методики.
     * В данном случае выставляется значение true или false checkBox'у в зависимости от того есть ли в редактируетмой методике
     * та или иная точка, если команда не относится к ErrorCommand, то устанавливаются параметры других параметров в соответствующей панели
     */
    public void addTestPointsOnGreedPane() {
        char[] testPointIdArr;

        //Если лист с точками AP+ не пустой
        if (!testListForCollumAPPls.isEmpty()) {

            for (Commands command : testListForCollumAPPls) {

                //Если это ErrorCommand
                if (command instanceof ErrorCommand) {
                    testPointIdArr = command.getId().toCharArray();

                    //Ищу нужный checkBox в сетке и ставлю ему значение true
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);

                //Если команда относится к Самоходу
                } else if (command instanceof CreepCommand) {

                    CreepCommand creepCommand = (CreepCommand) command;

                    //Если время исполнения этой команды расчитывается не по госту
                    if (!creepCommand.isGostTest()) {

                        //Инициализирую поля окна параметрами команды
                        txtFieldCRPUProcAPPls.setText(String.valueOf(creepCommand.getVoltPer()));
                        txtFieldTimeCRPAPPls.setText(getTime(creepCommand.getUserTimeTest()));
                        txtFieldCRPAmtImpAPPls.setText(String.valueOf(creepCommand.getPulseValue()));

                        addTglBtnCRPAPPls.setSelected(true);

                        txtFieldCRPUProcAPPls.setDisable(true);
                        txtFieldTimeCRPAPPls.setDisable(true);
                        txtFieldCRPAmtImpAPPls.setDisable(true);

                    //Если по ГОСТУ
                    } else {
                        addTglBtnCRPAPPlsGOST.setSelected(true);
                    }

                    CRPTogBtnAPPls.setSelected(true);

                //Если команда относится к Чувтсвительности
                } else if (command instanceof StartCommand) {
                    StartCommand startCommand = (StartCommand) command;

                    //Если время исполнения этой команды расчитывается не по госту
                    if (!startCommand.isGostTest()) {

                        //Инициализирую поля окна параметрами команды
                        txtFieldSTAIProcAPPls.setText(String.valueOf(startCommand.getRatedCurr()));
                        txtFieldTimeSRAAPPls.setText(getTime(startCommand.getUserTimeTest()));
                        txtFieldSTAAmtImpAPPls.setText(String.valueOf(startCommand.getPulseValue()));

                        addTglBtnSTAAPPls.setSelected(true);

                        txtFieldSTAIProcAPPls.setDisable(true);
                        txtFieldTimeSRAAPPls.setDisable(true);
                        txtFieldSTAAmtImpAPPls.setDisable(true);

                    //Если по ГОСТУ
                    }else {
                        addTglBtnSTAAPPlsGOST.setSelected(true);
                    }

                    STATogBtnAPPls.setSelected(true);

                //Если команда относится к точности хода часов
                } else if (command instanceof RTCCommand) {
                    RTCCommand rtcCommand = (RTCCommand) command;

                    //Инициализирую поля окна параметрами команды
                    if (rtcCommand.getErrorType() == 0) {
                        ChcBxRTCErrAPPls.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrAPPls.setValue("Сутч. погрешность");
                    }

                    txtFieldRngEAPPls.setText(String.format(Locale.ROOT,"%.7f", rtcCommand.getErrorForFalseTest()));
                    txtFldRTCFrqAPPls.setText(String.valueOf(rtcCommand.getFreg()));
                    txtFldRTCAmtMshAPPls.setText(String.valueOf(rtcCommand.getCountResultTest()));
                    txtFldRTCTimeMshAPPls.setText(String.valueOf(rtcCommand.getPulseForRTC()));

                    ChcBxRTCErrAPPls.setDisable(true);

                    txtFieldRngEAPPls.setDisable(true);
                    txtFldRTCFrqAPPls.setDisable(true);
                    txtFldRTCAmtMshAPPls.setDisable(true);
                    txtFldRTCTimeMshAPPls.setDisable(true);

                    addTglBtnRTCAPPls.setSelected(true);

                    RTCTogBtnAPPls.setSelected(true);

                //Если команда относится к Проверке счётного механизма
                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    //Если команда выполняется по времени
                    if (((ConstantCommand) command).isRunTestToTime()) {
                        radBtnConstTimeAPPls.setSelected(true);
                        txtFieldConstTimeAPPls.setText(getTime(constantCommand.getTimeTheTest()));

                    //Если команда выполняется по энергии
                    } else {
                        radBtnConstEnergyAPPls.setSelected(true);
                        txtFieldEngConstAPPls.setText(String.valueOf(constantCommand.getkWToTest()));
                    }

                    //Инициализирую поля окна параметрами команды
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

        //Далее идёт такой же механизм инициализации, но только для ругих направлений тока
        //и других типов мощности

        //Если лист с точками AP- не пустой
        if (!testListForCollumAPMns.isEmpty()) {

            for (Commands command : testListForCollumAPMns) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = command.getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);

                } else if (command instanceof CreepCommand) {
                    CreepCommand creepCommand = (CreepCommand) command;

                    if (!creepCommand.isGostTest()) {

                        txtFieldCRPUProcAPMns.setText(String.valueOf(creepCommand.getVoltPer()));
                        txtFieldTimeCRPAPMns.setText(getTime(creepCommand.getUserTimeTest()));
                        txtFieldCRPAmtImpAPMns.setText(String.valueOf(creepCommand.getPulseValue()));

                        addTglBtnCRPAPMns.setSelected(true);

                        txtFieldCRPUProcAPMns.setDisable(true);
                        txtFieldTimeCRPAPMns.setDisable(true);
                        txtFieldCRPAmtImpAPMns.setDisable(true);
                    } else {
                        addTglBtnCRPAPMnsGOST.setSelected(true);
                    }
                    CRPTogBtnAPMns.setSelected(true);

                } else if (command instanceof StartCommand) {
                    StartCommand startCommand = (StartCommand) command;

                    if (!startCommand.isGostTest()) {
                        txtFieldSTAIProcAPMns.setText(String.valueOf(startCommand.getRatedCurr()));
                        txtFieldTimeSRAAPMns.setText(getTime(startCommand.getUserTimeTest()));
                        txtFieldSTAAmtImpAPMns.setText(String.valueOf(startCommand.getPulseValue()));

                        addTglBtnSTAAPMns.setSelected(true);

                        txtFieldSTAIProcAPMns.setDisable(true);
                        txtFieldTimeSRAAPMns.setDisable(true);
                        txtFieldSTAAmtImpAPMns.setDisable(true);
                    }else {
                        addTglBtnSTAAPMnsGOST.setSelected(true);
                    }
                    STATogBtnAPMns.setSelected(true);

                } else if (command instanceof RTCCommand) {
                    RTCCommand rtcCommand = (RTCCommand) command;

                    if (rtcCommand.getErrorType() == 0) {
                        ChcBxRTCErrAPMns.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrAPMns.setValue("Сутч. погрешность");
                    }

                    txtFieldRngEAPMns.setText(String.valueOf(rtcCommand.getErrorForFalseTest()));
                    txtFldRTCFrqAPMns.setText(String.valueOf(rtcCommand.getFreg()));
                    txtFldRTCAmtMshAPMns.setText(String.valueOf(rtcCommand.getCountResultTest()));
                    txtFldRTCTimeMshAPMns.setText(String.valueOf(rtcCommand.getPulseForRTC()));

                    ChcBxRTCErrAPMns.setDisable(true);

                    txtFieldRngEAPMns.setDisable(true);
                    txtFldRTCFrqAPMns.setDisable(true);
                    txtFldRTCAmtMshAPMns.setDisable(true);
                    txtFldRTCTimeMshAPMns.setDisable(true);

                    addTglBtnRTCAPMns.setSelected(true);

                    RTCTogBtnAPMns.setSelected(true);

                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    if (constantCommand.isRunTestToTime()) {
                        radBtnConstTimeAPMns.setSelected(true);
                        txtFieldConstTimeAPMns.setText(getTime(constantCommand.getTimeTheTest()));
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

        //Если лист с точками RP+ не пустой
        if (!testListForCollumRPPls.isEmpty()) {

            for (Commands command : testListForCollumRPPls) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = command.getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);

                } else if (command instanceof CreepCommand) {
                    CreepCommand creepCommand = (CreepCommand) command;

                    if (!creepCommand.isGostTest()) {

                        txtFieldCRPUProcRPPls.setText(String.valueOf(creepCommand.getVoltPer()));
                        txtFieldTimeCRPRPPls.setText(getTime(creepCommand.getUserTimeTest()));
                        txtFieldCRPAmtImpRPPls.setText(String.valueOf(creepCommand.getPulseValue()));

                        addTglBtnCRPRPPls.setSelected(true);

                        txtFieldCRPUProcRPPls.setDisable(true);
                        txtFieldTimeCRPRPPls.setDisable(true);
                        txtFieldCRPAmtImpRPPls.setDisable(true);
                    } else {
                        addTglBtnCRPRPPlsGOST.setSelected(true);
                    }
                    CRPTogBtnRPPls.setSelected(true);

                } else if (command instanceof StartCommand) {
                    StartCommand startCommand = (StartCommand) command;

                    if (!startCommand.isGostTest()) {
                        txtFieldSTAIProcRPPls.setText(String.valueOf(startCommand.getRatedCurr()));
                        txtFieldTimeSRARPPls.setText(getTime(startCommand.getUserTimeTest()));
                        txtFieldSTAAmtImpRPPls.setText(String.valueOf(startCommand.getPulseValue()));

                        addTglBtnSTARPPls.setSelected(true);

                        txtFieldSTAIProcRPPls.setDisable(true);
                        txtFieldTimeSRARPPls.setDisable(true);
                        txtFieldSTAAmtImpRPPls.setDisable(true);
                    }else {
                        addTglBtnSTARPPlsGOST.setSelected(true);
                    }
                    STATogBtnRPPls.setSelected(true);

                } else if (command instanceof RTCCommand) {
                    RTCCommand rtcCommand = (RTCCommand) command;

                    if (rtcCommand.getErrorType() == 0) {
                        ChcBxRTCErrRPPls.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrRPPls.setValue("Сутч. погрешность");
                    }

                    txtFieldRngERPPls.setText(String.valueOf(rtcCommand.getErrorForFalseTest()));
                    txtFldRTCFrqRPPls.setText(String.valueOf(rtcCommand.getFreg()));
                    txtFldRTCAmtMshRPPls.setText(String.valueOf(rtcCommand.getCountResultTest()));
                    txtFldRTCTimeMshRPPls.setText(String.valueOf(rtcCommand.getPulseForRTC()));

                    ChcBxRTCErrRPPls.setDisable(true);

                    txtFieldRngERPPls.setDisable(true);
                    txtFldRTCFrqRPPls.setDisable(true);
                    txtFldRTCAmtMshRPPls.setDisable(true);
                    txtFldRTCTimeMshRPPls.setDisable(true);

                    addTglBtnRTCRPPls.setSelected(true);

                    RTCTogBtnRPPls.setSelected(true);

                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    if (((ConstantCommand) command).isRunTestToTime()) {
                        radBtnConstTimeRPPls.setSelected(true);
                        txtFieldConstTimeRPPls.setText(getTime(constantCommand.getTimeTheTest()));
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

        //Если лист с точками RP- не пустой
        if (!testListForCollumRPMns.isEmpty()) {

            for (Commands command : testListForCollumRPMns) {
                if (command instanceof ErrorCommand) {
                    testPointIdArr = command.getId().toCharArray();
                    setTrueOrFalseOnCheckBox(testPointIdArr, command);

                } else if (command instanceof CreepCommand) {
                    CreepCommand creepCommand = (CreepCommand) command;

                    if (!creepCommand.isGostTest()) {

                        txtFieldCRPUProcRPMns.setText(String.valueOf(creepCommand.getVoltPer()));
                        txtFieldTimeCRPRPMns.setText(getTime(creepCommand.getUserTimeTest()));
                        txtFieldCRPAmtImpRPMns.setText(String.valueOf(creepCommand.getPulseValue()));

                        addTglBtnCRPRPMns.setSelected(true);

                        txtFieldCRPUProcRPMns.setDisable(true);
                        txtFieldTimeCRPRPMns.setDisable(true);
                        txtFieldCRPAmtImpRPMns.setDisable(true);
                    } else {
                        addTglBtnCRPRPMnsGOST.setSelected(true);
                    }
                    CRPTogBtnRPMns.setSelected(true);

                } else if (command instanceof StartCommand) {
                    StartCommand startCommand = (StartCommand) command;

                    if (!startCommand.isGostTest()) {
                        txtFieldSTAIProcRPMns.setText(String.valueOf(startCommand.getRatedCurr()));
                        txtFieldTimeSRARPMns.setText(getTime(startCommand.getUserTimeTest()));
                        txtFieldSTAAmtImpRPMns.setText(String.valueOf(startCommand.getPulseValue()));

                        addTglBtnSTARPMns.setSelected(true);

                        txtFieldSTAIProcRPMns.setDisable(true);
                        txtFieldTimeSRARPMns.setDisable(true);
                        txtFieldSTAAmtImpRPMns.setDisable(true);
                    }else {
                        addTglBtnSTARPMnsGOST.setSelected(true);
                    }
                    STATogBtnRPMns.setSelected(true);

                } else if (command instanceof RTCCommand) {
                    RTCCommand rtcCommand = (RTCCommand) command;

                    if (rtcCommand.getErrorType() == 0) {
                        ChcBxRTCErrRPMns.setValue("В ед. частоты");
                    } else {
                        ChcBxRTCErrRPMns.setValue("Сутч. погрешность");
                    }

                    txtFieldRngERPMns.setText(String.valueOf(rtcCommand.getErrorForFalseTest()));
                    txtFldRTCFrqRPMns.setText(String.valueOf(rtcCommand.getFreg()));
                    txtFldRTCAmtMshRPMns.setText(String.valueOf(rtcCommand.getCountResultTest()));
                    txtFldRTCTimeMshRPMns.setText(String.valueOf(rtcCommand.getPulseForRTC()));

                    ChcBxRTCErrRPMns.setDisable(true);

                    txtFieldRngERPMns.setDisable(true);
                    txtFldRTCFrqRPMns.setDisable(true);
                    txtFldRTCAmtMshRPMns.setDisable(true);
                    txtFldRTCTimeMshRPMns.setDisable(true);

                    addTglBtnRTCRPMns.setSelected(true);

                    RTCTogBtnRPMns.setSelected(true);

                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    if (((ConstantCommand) command).isRunTestToTime()) {
                        radBtnConstTimeRPMns.setSelected(true);
                        txtFieldConstTimeRPMns.setText(getTime(constantCommand.getTimeTheTest()));
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

        //Отдельный метод для инициализации проверки Реле (был добавлен позже)
        initRelayTest();
    }

    /**
     * Находит нужный checkBox в сетке выбора точек по ID и задаёт ему значение true
     * @param testPointIdArr
     * @param commands
     */
    private void setTrueOrFalseOnCheckBox(char[] testPointIdArr, Commands commands) {
        //поиск в сетке AP+
        if (testPointIdArr[4] == 'A' && testPointIdArr[6] == 'P') {

            //В сетке точек для стенда с одной токовой цепью
            if (testPointIdArr[2] == 'H') {

                for (Node checkBox : gridPaneOnePhaseAPPlus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }

            //В сетке точек для стенда с двумя токовыми цепями, для первой цепи (A)
            } else if (testPointIdArr[2] == 'A') {

                for (Node checkBox : gridPanePhaseAAPPlus.getChildren()) {
                    try {
                        if (((ErrorCommand) commands).getId().equals(checkBox.getId())) {
                            ((CheckBox) checkBox).setSelected(true);
                            break;
                        }
                    }catch (NullPointerException ignore) {}
                }

            //В сетке точек для стенда с двумя токовыми цепями, для второй цепи (B)
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

            /**
             * Далее идёт такой же механизм поиска, но только для ругих направлений тока и типа энергии
             */
        //поиск в сетке AP-
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
        }

        //поиск в сетке RP+
        else if (testPointIdArr[4] == 'R' && testPointIdArr[6] == 'P') {

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
        }

        //поиск в сетке RP-
        else if (testPointIdArr[4] == 'R' && testPointIdArr[6] == 'N') {

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


    /**
     * Метод для перерисовки GridPane после добавления новых параметров для точек.
     * Новые значения коэф. мощности (PF) и тока
     */
    public void refreshGridPaneAndScrolPane() {

        //Получаю новые значения
        current = Arrays.asList(ConsoleHelper.properties.getProperty("currentForMethodicPane").split(", "));
        powerFactor = Arrays.asList(ConsoleHelper.properties.getProperty("powerFactorForMetodicPane").split(", "));

        //Удаляю страную сетку с точками
        mainAnchorPane.getChildren().removeAll(mainScrollPane, scrollPaneForCurrent, scrollPaneForPowerFactor, stackPaneForGridPane);

        //Создаю новую
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

        createGridPaneAndSetCheckBoxes();

        initMainScrollPane();

        initScrolPaneForCurrentAndPowerFactor();

        createScrollPanesForGridPaneWithoutSquare();

        APPlus.fire();

        //Привязываю скролл бары друг у другу
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

    /**
     * Действие если пользователь хочет добавить точки влияния
     * @param event
     */
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

    /**
     * Если пользователь захочет установить фиксированные параметры при которых должны проводиться испытания
     * @param event
     */
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

    /**
     * Действие при нажатии кнопки сохранить
     * @param event
     */
    @FXML
    void saveOrCancelAction(ActionEvent event) {

        if (event.getSource() == saveBtn) {

            List<Commands> listErrorCommand;
            List<Commands> listCreepStartRTCCommadns;

            //Сохраняю точки для AP+
            listErrorCommand = methodicForOnePhaseStend.getCommandsMap().get(0);
            listCreepStartRTCCommadns = methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(0);

            //Удаляю прошлые точки испытний
            listErrorCommand.clear();
            listCreepStartRTCCommadns.clear();

            //Добавляю новые
            for (Commands command : testListForCollumAPPls) {
                if (command instanceof ErrorCommand) {
                    listErrorCommand.add(command);

                } else {
                    listCreepStartRTCCommadns.add(command);
                }
            }

            //Сохраняю точки для AP-
            listErrorCommand = methodicForOnePhaseStend.getCommandsMap().get(1);
            listCreepStartRTCCommadns = methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(1);

            //Удаляю прошлые точки испытний
            listErrorCommand.clear();
            listCreepStartRTCCommadns.clear();

            //Добавляю новые
            for (Commands command : testListForCollumAPMns) {
                if (command instanceof ErrorCommand) {
                    listErrorCommand.add(command);
                } else {
                    listCreepStartRTCCommadns.add(command);
                }
            }

            //Сохраняю точки для RP+
            listErrorCommand = methodicForOnePhaseStend.getCommandsMap().get(2);
            listCreepStartRTCCommadns = methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(2);

            //Удаляю прошлые точки испытний
            listErrorCommand.clear();
            listCreepStartRTCCommadns.clear();

            //Добавляю новые
            for (Commands command : testListForCollumRPPls) {
                if (command instanceof ErrorCommand) {
                    listErrorCommand.add(command);
                } else {
                    listCreepStartRTCCommadns.add(command);
                }
            }

            //Сохраняю точки для RP-
            listErrorCommand = methodicForOnePhaseStend.getCommandsMap().get(3);
            listCreepStartRTCCommadns = methodicForOnePhaseStend.getCreepStartRTCConstCommandsMap().get(3);

            //Удаляю прошлые точки испытний
            listErrorCommand.clear();
            listCreepStartRTCCommadns.clear();

            //Добавляю новые
            for (Commands command : testListForCollumRPMns) {
                if (command instanceof ErrorCommand) {
                    listErrorCommand.add(command);
                } else {
                    listCreepStartRTCCommadns.add(command);
                }
            }

            //Сохраняю в память изменения
            metodicsForTest.serializationMetodics();

            //Отображаю методику в окне выбора методик
            if (edit) {
                methodicsAddEditDeleteFrameController.setListsView(methodicForOnePhaseStend);
            }else {
                methodicsAddEditDeleteFrameController.refreshMethodicList();
            }

            //Открываю окно выбора методики
            Stage stage1 = (Stage) methodicsAddEditDeleteFrameController.getEditMetBtn().getScene().getWindow();
            stage1.show();

            //Закрываю это окно
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * В зависимости от нажатой кнопки выставляет нужные состояния другим и выводит нужный Pane вперёд
     * @param event
     */
    @FXML
    void setPointFrameAction(ActionEvent event) {
        setGropToggleButton(event);
        gridPaneToFront(Objects.requireNonNull(getGridPane()));
    }

    /**
     * Имитация ToggleGroup, но более расширенная
     * @param event
     */
    private void setGropToggleButton(ActionEvent event) {

        //Если пользователь использует кнопки смены токовых цепей
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

        //Если пользователь нажимает на кнопки смены направления тока и смены типа мощности
        //AP+
        if (event.getSource() == APPlus || event.getSource() == APPlusCRPSTA) {
            setDefPosBtn();
            viewPointTableAPPls.toFront();
            APPlsPane.toFront();
            paneCRPAPPls.toFront();
            paneRelay.toBack();

            APPlus.setSelected(true);
            APMinus.setSelected(false);
            RPPlus.setSelected(false);
            RPMinus.setSelected(false);

            APPlusCRPSTA.setSelected(true);
            APMinusCRPSTA.setSelected(false);
            RPPlusCRPSTA.setSelected(false);
            RPMinusCRPSTA.setSelected(false);

            initRelayTest();
        }
        //AP-
        if (event.getSource() == APMinus || event.getSource() == APMinusCRPSTA) {
            setDefPosBtn();
            viewPointTableAPMns.toFront();
            APMnsPane.toFront();
            paneCRPAPMns.toFront();
            paneRelay.toBack();

            APMinus.setSelected(true);
            APPlus.setSelected(false);
            RPPlus.setSelected(false);
            RPMinus.setSelected(false);

            APPlusCRPSTA.setSelected(false);
            APMinusCRPSTA.setSelected(true);
            RPPlusCRPSTA.setSelected(false);
            RPMinusCRPSTA.setSelected(false);

            initRelayTest();
        }
        //RP+
        if (event.getSource() == RPPlus || event.getSource() == RPPlusCRPSTA) {
            setDefPosBtn();
            viewPointTableRPPls.toFront();
            RPPlsPane.toFront();
            paneCRPRPPls.toFront();
            paneRelay.toBack();

            RPPlus.setSelected(true);
            APPlus.setSelected(false);
            APMinus.setSelected(false);
            RPMinus.setSelected(false);

            APPlusCRPSTA.setSelected(false);
            APMinusCRPSTA.setSelected(false);
            RPPlusCRPSTA.setSelected(true);
            RPMinusCRPSTA.setSelected(false);

            initRelayTest();
        }
        //RP-
        if (event.getSource() == RPMinus || event.getSource() == RPMinusCRPSTA) {
            setDefPosBtn();
            viewPointTableRPMns.toFront();
            RPMnsPane.toFront();
            paneCRPRPMns.toFront();
            paneRelay.toBack();

            RPMinus.setSelected(true);
            RPPlus.setSelected(false);
            APPlus.setSelected(false);
            APMinus.setSelected(false);

            APPlusCRPSTA.setSelected(false);
            APMinusCRPSTA.setSelected(false);
            RPPlusCRPSTA.setSelected(false);
            RPMinusCRPSTA.setSelected(true);

            initRelayTest();
        }


        //Переключение между выборором добавления команд Самоход, чувствительно, ТЧХ, реле, Проверка счётного механизма
        //AP+
        if (event.getSource() == CRPTogBtnAPPls) {
            paneCRPAPPls.toFront();
            paneRelay.toBack();
            if (addTglBtnCRPAPPls.isSelected() || addTglBtnCRPAPPlsGOST.isSelected()) {
                CRPTogBtnAPPls.setSelected(true);
            }else CRPTogBtnAPPls.setSelected(false);
        }

        if (event.getSource() == STATogBtnAPPls) {
            paneSTAAPPls.toFront();
            if (addTglBtnSTAAPPls.isSelected() || addTglBtnSTAAPPlsGOST.isSelected()) {
                STATogBtnAPPls.setSelected(true);
                paneRelay.toBack();
            }else STATogBtnAPPls.setSelected(false);
        }

        if (event.getSource() == RTCTogBtnAPPls) {
            paneRTCAPPls.toFront();
            paneRelay.toBack();
            RTCTogBtnAPPls.setSelected(addTglBtnRTCAPPls.isSelected());
        }

        if (event.getSource() == ConstTogBtnAPPls) {
            paneConstAPPls.toFront();
            paneRelay.toBack();
            ConstTogBtnAPPls.setSelected(addTglBtnConstAPPls.isSelected());
        }
        //AP-
        if (event.getSource() == CRPTogBtnAPMns) {
            paneCRPAPMns.toFront();
            paneRelay.toBack();
            if (addTglBtnCRPAPMns.isSelected() || addTglBtnCRPAPMnsGOST.isSelected()) {
                CRPTogBtnAPMns.setSelected(true);
            } else CRPTogBtnAPMns.setSelected(false);
        }
        if (event.getSource() == STATogBtnAPMns) {
            paneSTAAPMns.toFront();
            paneRelay.toBack();
            if (addTglBtnSTAAPMns.isSelected() || addTglBtnSTAAPMnsGOST.isSelected()) {
                STATogBtnAPMns.setSelected(true);
            } else STATogBtnAPMns.setSelected(false);
        }
        if (event.getSource() == RTCTogBtnAPMns) {
            RTCTogBtnAPMns.setSelected(addTglBtnRTCAPMns.isSelected());
            paneRTCAPMns.toFront();
            paneRelay.toBack();
        }
        if (event.getSource() == ConstTogBtnAPMns) {
            ConstTogBtnAPMns.setSelected(addTglBtnConstAPMns.isSelected());
            paneConstAPMns.toFront();
            paneRelay.toBack();
        }

        //RP+
        if (event.getSource() == CRPTogBtnRPPls) {
            CRPTogBtnRPPls.setSelected(addTglBtnCRPRPPls.isSelected() || addTglBtnCRPRPPlsGOST.isSelected());
            paneCRPRPPls.toFront();
            paneRelay.toBack();
        }
        if (event.getSource() == STATogBtnRPPls) {
            STATogBtnRPPls.setSelected(addTglBtnSTARPPls.isSelected() || addTglBtnSTARPPlsGOST.isSelected());
            paneSTARPPls.toFront();
            paneRelay.toBack();
        }
        if (event.getSource() == RTCTogBtnRPPls) {
            RTCTogBtnRPPls.setSelected(addTglBtnRTCRPPls.isSelected());
            paneRTCRPPls.toFront();
            paneRelay.toBack();
        }
        if (event.getSource() == ConstTogBtnRPPls) {
            ConstTogBtnRPPls.setSelected(addTglBtnConstRPPls.isSelected());
            paneConstRPPls.toFront();
            paneRelay.toBack();
        }

        //RP-
        if (event.getSource() == CRPTogBtnRPMns) {
            CRPTogBtnRPMns.setSelected(addTglBtnCRPRPMns.isSelected() || addTglBtnCRPRPMnsGOST.isSelected());
            paneCRPRPMns.toFront();
            paneRelay.toBack();
        }
        if (event.getSource() == STATogBtnRPMns) {
            STATogBtnRPMns.setSelected(addTglBtnSTARPMns.isSelected() || addTglBtnSTARPMnsGOST.isSelected());
            paneSTARPMns.toFront();
            paneRelay.toBack();
        }
        if (event.getSource() == RTCTogBtnRPMns) {
            RTCTogBtnRPMns.setSelected(addTglBtnRTCRPMns.isSelected());
            paneRTCRPMns.toFront();
            paneRelay.toBack();
        }
        if (event.getSource() == ConstTogBtnRPMns) {
            ConstTogBtnRPMns.setSelected(addTglBtnConstRPMns.isSelected());
            paneConstRPMns.toFront();
            paneRelay.toBack();
        }

        //------------------------------------------------ Реле
        if (event.getSource() == tglBtnRelay) {
            tglBtnRelay.setSelected(addTglBtnRelay.isSelected());

            paneRelay.toFront();
        }
    }

    /**
     * Действие при добавлении команд
     * Самоход, чувствительность, точность хода часов, проверка счётного механизма, проверка реле
     * @param event
     */
    @FXML
    void addSTAcRPrTCcOnst(ActionEvent event) {

        //------------------------------------------------------ Самоход
        CreepCommand creepCommand;

        //Добаление самохода с параметрами пользователя AP+
        if (event.getSource() == addTglBtnCRPAPPls) {

            if (addTglBtnCRPAPPls.isSelected()) {

                txtFieldCRPUProcAPPls.setStyle("");
                txtFieldTimeCRPAPPls.setStyle("");
                txtFieldCRPAmtImpAPPls.setStyle("");

                double procUnom;
                long timeTest;
                int amountImp;

                //Проверка валидности выбранного пользователем напряжения
                try {
                    procUnom = Double.parseDouble(txtFieldCRPUProcAPPls.getText());
                    if (procUnom < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPUProcAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выставленного пользователем времени теста
                try {
                    String[] timeArr = txtFieldTimeCRPAPPls.getText().trim().split(":");

                    if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0].trim());
                    if (hour < 0) throw new NumberFormatException();

                    int mins = Integer.parseInt(timeArr[1].trim());
                    if (mins < 0) throw new NumberFormatException();

                    if (mins > 59) {
                        ConsoleHelper.infoException("Количество минут не может быть больше 59");
                        txtFieldTimeCRPAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnCRPAPPls.setSelected(false);
                        return;
                    }

                    int sek = Integer.parseInt(timeArr[2].trim());
                    if (sek < 0) throw new NumberFormatException();

                    if (sek > 59) {
                        ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                        txtFieldTimeCRPAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnCRPAPPls.setSelected(false);
                        return;
                    }

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выбранных пользователем импульсов
                try {
                    amountImp = Integer.parseInt(txtFieldCRPAmtImpAPPls.getText());
                    if (amountImp < 0) throw new NumberFormatException();

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPAmtImpAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPPls.setSelected(false);
                    return;
                }

                //Добавление команды в общий список команд
                testListForCollumAPPls.add(new CreepCommand(false,false, "Самоход AP+", "CRP;U;A;P",0,
                        timeTest, amountImp, procUnom));

                //Отключаю редактирование
                txtFieldCRPAmtImpAPPls.setDisable(true);
                txtFieldTimeCRPAPPls.setDisable(true);
                txtFieldCRPUProcAPPls.setDisable(true);

                CRPTogBtnAPPls.setSelected(true);

            //Удаление команды из списка команд
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

                CRPTogBtnAPPls.setSelected(true);

                testListForCollumAPPls.add(creepCommand);

            //Удаление команды самоход из списка
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
                    if (procUnom < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPUProcAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPMns.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeCRPAPMns.getText().trim().split(":");

                    if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0].trim());
                    if (hour < 0) throw new NumberFormatException();

                    int mins = Integer.parseInt(timeArr[1].trim());
                    if (mins < 0) throw new NumberFormatException();

                    if (mins > 59) {
                        ConsoleHelper.infoException("Количество минут не может быть больше 59");
                        txtFieldTimeCRPAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnCRPAPMns.setSelected(false);
                        return;
                    }

                    int sek = Integer.parseInt(timeArr[2].trim());
                    if (sek < 0) throw new NumberFormatException();

                    if (sek > 59) {
                        ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                        txtFieldTimeCRPAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnCRPAPMns.setSelected(false);
                        return;
                    }

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPAPMns.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldCRPAmtImpAPMns.getText());
                    if (amountImp < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPAmtImpAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
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
                    if (procUnom < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPUProcRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPPls.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeCRPRPPls.getText().trim().split(":");

                    if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0].trim());
                    if (hour < 0) throw new NumberFormatException();

                    int mins = Integer.parseInt(timeArr[1].trim());
                    if (mins < 0) throw new NumberFormatException();

                    if (mins > 59) {
                        ConsoleHelper.infoException("Количество минут не может быть больше 59");
                        txtFieldTimeCRPRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnCRPRPPls.setSelected(false);
                        return;
                    }

                    int sek = Integer.parseInt(timeArr[2].trim());
                    if (sek < 0) throw new NumberFormatException();

                    if (sek > 59) {
                        ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                        txtFieldTimeCRPRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnCRPRPPls.setSelected(false);
                        return;
                    }

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPPls.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldCRPAmtImpRPPls.getText());
                    if (amountImp < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPAmtImpRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
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
                    if (procUnom < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPUProcRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPMns.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeCRPRPMns.getText().trim().split(":");

                    if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0].trim());
                    if (hour < 0) throw new NumberFormatException();

                    int mins = Integer.parseInt(timeArr[1].trim());
                    if (mins < 0) throw new NumberFormatException();

                    if (mins > 59) {
                        ConsoleHelper.infoException("Количество минут не может быть больше 59");
                        txtFieldTimeCRPRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnCRPRPMns.setSelected(false);
                        return;
                    }

                    int sek = Integer.parseInt(timeArr[2].trim());
                    if (sek < 0) throw new NumberFormatException();

                    if (sek > 59) {
                        ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                        txtFieldTimeCRPRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnCRPRPMns.setSelected(false);
                        return;
                    }

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeCRPRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnCRPRPMns.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldCRPAmtImpRPMns.getText());
                    if (amountImp < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldCRPAmtImpRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
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

        //------------------------------------------------------ Чувствительность
        //Добаление теста на чувствительность с параметрами пользователя AP+
        if (event.getSource() == addTglBtnSTAAPPls) {
            if (addTglBtnSTAAPPls.isSelected()) {

                txtFieldSTAIProcAPPls.setStyle("");
                txtFieldTimeSRAAPPls.setStyle("");
                txtFieldSTAAmtImpAPPls.setStyle("");

                double current;
                long timeTest;
                int amountImp;

                //Проверка валидности выбранного пользователем значения тока
                try {
                    current = Double.parseDouble(txtFieldSTAIProcAPPls.getText());
                    if (current < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldSTAIProcAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTAAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выбранного пользователем значения времени
                try {
                    String[] timeArr = txtFieldTimeSRAAPPls.getText().trim().split(":");

                    if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0].trim());
                    if (hour < 0) throw new NumberFormatException();

                    int mins = Integer.parseInt(timeArr[1].trim());
                    if (mins < 0) throw new NumberFormatException();
                    if (mins > 59) {
                        ConsoleHelper.infoException("Количество минут не может быть больше 59");
                        txtFieldTimeSRAAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnSTAAPPls.setSelected(false);
                        return;
                    }

                    int sek = Integer.parseInt(timeArr[2].trim());
                    if (sek < 0) throw new NumberFormatException();
                    if (sek > 59) {
                        ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                        txtFieldTimeSRAAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnSTAAPPls.setSelected(false);
                        return;
                    }

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeSRAAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTAAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выбранного пользователем значения количества импульсов для теста
                try {
                    amountImp = Integer.parseInt(txtFieldSTAAmtImpAPPls.getText());
                    if (amountImp < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldSTAAmtImpAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTAAPPls.setSelected(false);
                    return;
                }

                //Добавление команды в список команд
                testListForCollumAPPls.add(new StartCommand(false, "Чувствительность AP+", "STA;U;A;P", 0,
                        0,false, timeTest, amountImp, current));


                txtFieldSTAAmtImpAPPls.setDisable(true);
                txtFieldTimeSRAAPPls.setDisable(true);
                txtFieldSTAIProcAPPls.setDisable(true);

                STATogBtnAPPls.setSelected(true);

            //Удаление команды из списка
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
                testListForCollumAPPls.add(new StartCommand(false, "Чувствительность ГОСТ AP+", "STA;G;A;P", 0,
                        0, true));

                STATogBtnAPPls.setSelected(true);

            //Удаление команды из списка
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

                txtFieldSTAIProcAPMns.setStyle("");
                txtFieldTimeSRAAPMns.setStyle("");
                txtFieldSTAAmtImpAPMns.setStyle("");

                double current;
                long timeTest;
                int amountImp;

                try {
                    current = Double.parseDouble(txtFieldSTAIProcAPMns.getText());
                    if (current < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldSTAIProcAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTAAPMns.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeSRAAPMns.getText().trim().split(":");

                    if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0].trim());
                    if (hour < 0) throw new NumberFormatException();

                    int mins = Integer.parseInt(timeArr[1].trim());
                    if (mins < 0) throw new NumberFormatException();
                    if (mins > 59) {
                        ConsoleHelper.infoException("Количество минут не может быть больше 59");
                        txtFieldTimeSRAAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnSTAAPMns.setSelected(false);
                        return;
                    }

                    int sek = Integer.parseInt(timeArr[2].trim());
                    if (sek < 0) throw new NumberFormatException();
                    if (sek > 59) {
                        ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                        txtFieldTimeSRAAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnSTAAPMns.setSelected(false);
                        return;
                    }

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeSRAAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTAAPMns.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldSTAAmtImpAPMns.getText());
                    if (amountImp < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldSTAAmtImpAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTAAPMns.setSelected(false);
                    return;
                }

                testListForCollumAPMns.add(new StartCommand(false, "Чувствительность AP-", "STA;U;A;N", 1,
                        1,false, timeTest, amountImp, current));


                txtFieldSTAAmtImpAPMns.setDisable(true);
                txtFieldTimeSRAAPMns.setDisable(true);
                txtFieldSTAIProcAPMns.setDisable(true);

                STATogBtnAPMns.setSelected(true);

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
                testListForCollumAPMns.add(new StartCommand(false, "Чувствительность ГОСТ AP-", "STA;G;A;N", 1, 1, true));

                STATogBtnAPMns.setSelected(true);
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

                txtFieldSTAIProcRPPls.setStyle("");
                txtFieldTimeSRARPPls.setStyle("");
                txtFieldSTAAmtImpRPPls.setStyle("");

                double current;
                long timeTest;
                int amountImp;

                try {
                    current = Double.parseDouble(txtFieldSTAIProcRPPls.getText());
                    if (current < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldSTAIProcRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTARPPls.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeSRARPPls.getText().trim().split(":");

                    if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0].trim());
                    if (hour < 0) throw new NumberFormatException();

                    int mins = Integer.parseInt(timeArr[1].trim());
                    if (mins < 0) throw new NumberFormatException();
                    if (mins > 59) {
                        ConsoleHelper.infoException("Количество минут не может быть больше 59");
                        txtFieldTimeSRARPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnSTARPPls.setSelected(false);
                        return;
                    }

                    int sek = Integer.parseInt(timeArr[2].trim());
                    if (sek < 0) throw new NumberFormatException();
                    if (sek > 59) {
                        ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                        txtFieldTimeSRARPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnSTARPPls.setSelected(false);
                        return;
                    }

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeSRARPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTARPPls.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldSTAAmtImpRPPls.getText());
                    if (amountImp < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldSTAAmtImpRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTARPPls.setSelected(false);
                    return;
                }

                testListForCollumRPPls.add(new StartCommand(false, "Чувствительность RP+", "STA;U;R;P", 0,
                        2,false, timeTest, amountImp, current));


                txtFieldSTAAmtImpRPPls.setDisable(true);
                txtFieldTimeSRARPPls.setDisable(true);
                txtFieldSTAIProcRPPls.setDisable(true);

                STATogBtnRPPls.setSelected(true);

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
                testListForCollumRPPls.add(new StartCommand(false, "Чувствительность ГОСТ RP+", "STA;G;R;A", 0, 2, true));

                STATogBtnRPPls.setSelected(true);
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

                txtFieldSTAIProcRPMns.setStyle("");
                txtFieldTimeSRARPMns.setStyle("");
                txtFieldSTAAmtImpRPMns.setStyle("");

                double current;
                long timeTest;
                int amountImp;

                try {
                    current = Double.parseDouble(txtFieldSTAIProcRPMns.getText());
                    if (current < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldSTAIProcRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTARPMns.setSelected(false);
                    return;
                }

                try {
                    String[] timeArr = txtFieldTimeSRARPMns.getText().trim().split(":");

                    if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                        throw new NumberFormatException();
                    }

                    int hour = Integer.parseInt(timeArr[0].trim());
                    if (hour < 0) throw new NumberFormatException();

                    int mins = Integer.parseInt(timeArr[1].trim());
                    if (mins < 0) throw new NumberFormatException();
                    if (mins > 59) {
                        ConsoleHelper.infoException("Количество минут не может быть больше 59");
                        txtFieldTimeSRARPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnSTARPMns.setSelected(false);
                        return;
                    }

                    int sek = Integer.parseInt(timeArr[2].trim());
                    if (sek < 0) throw new NumberFormatException();
                    if (sek > 59) {
                        ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                        txtFieldTimeSRARPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnSTARPMns.setSelected(false);
                        return;
                    }

                    timeTest = (3600 * hour + 60 * mins + sek) * 1000;

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldTimeSRARPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTARPMns.setSelected(false);
                    return;
                }

                try {
                    amountImp = Integer.parseInt(txtFieldSTAAmtImpRPMns.getText());
                    if (amountImp < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldSTAAmtImpRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnSTARPMns.setSelected(false);
                    return;
                }

                testListForCollumRPMns.add(new StartCommand(false, "Чувствительность RP-", "STA;U;R;N", 1,
                        3,false, timeTest, amountImp, current));


                txtFieldSTAAmtImpRPMns.setDisable(true);
                txtFieldTimeSRARPMns.setDisable(true);
                txtFieldSTAIProcRPMns.setDisable(true);

                STATogBtnRPMns.setSelected(true);

            } else {
                for (Commands command : testListForCollumRPMns) {
                    if (command instanceof StartCommand) {
                        if (command.getName().equals("Чувствительность RP-")) {
                            testListForCollumRPMns.remove(command);
                            break;
                        }
                    }
                }

                txtFieldSTAAmtImpRPMns.setDisable(false);
                txtFieldTimeSRARPMns.setDisable(false);
                txtFieldSTAIProcRPMns.setDisable(false);

                if (addTglBtnSTARPMnsGOST.isSelected()) {
                    STATogBtnRPMns.setSelected(true);
                } else STATogBtnRPMns.setSelected(false);
            }
        }
        //Добаление теста на чувствительность с параметрами по ГОСТу RP-
        if (event.getSource() == addTglBtnSTARPMnsGOST) {
            if (addTglBtnSTARPMnsGOST.isSelected()) {
                testListForCollumRPMns.add(new StartCommand(false, "Чувствительность ГОСТ RP-", "STA;G;R;N",1, 3, true));

                STATogBtnRPMns.setSelected(true);
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

        //------------------------------------------------------ Точность хода часов
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

                //Проверка валидности выбранного пользователем диапазона погрешностей для измерения
                try {
                    errorRange = Double.parseDouble(txtFieldRngEAPPls.getText());
                    if (errorRange < 0) throw new NumberFormatException();

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldRngEAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выбранного пользователем значения частоты
                try {
                    freg = Double.parseDouble(txtFldRTCFrqAPPls.getText());
                    if (freg < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCFrqAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выбранного пользователем значения количества измерений
                try {
                    anountMeash = Integer.parseInt(txtFldRTCAmtMshAPPls.getText());
                    if (anountMeash < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCAmtMshAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выбранного пользователем значения количества импульсов для измерения
                try {
                    timeMeas = Integer.parseInt(txtFldRTCTimeMshAPPls.getText());
                    if (timeMeas < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCTimeMshAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPPls.setSelected(false);
                    return;
                }

                //Добавления точки испытная
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

            //Удаление точки испытная
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
                txtFldRTCFrqAPPls.setDisable(false);

                RTCTogBtnAPPls.setSelected(false);
            }
        }

        //Добаление теста "точность хода часов" AP-
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
                    if (errorRange < 0) throw new NumberFormatException();

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldRngEAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPMns.setSelected(false);
                    return;
                }

                try {
                    freg = Double.parseDouble(txtFldRTCFrqAPMns.getText());
                    if (freg < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCFrqAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPMns.setSelected(false);
                    return;
                }

                try {
                    anountMeash = Integer.parseInt(txtFldRTCAmtMshAPMns.getText());
                    if (anountMeash < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCAmtMshAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCAPMns.setSelected(false);
                    return;
                }

                try {
                    timeMeas = Integer.parseInt(txtFldRTCTimeMshAPMns.getText());
                    if (timeMeas < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCTimeMshAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
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
                txtFldRTCFrqAPMns.setDisable(false);

                RTCTogBtnAPMns.setSelected(false);
            }
        }
        //Добаление теста "точность хода часов" RP+
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
                    if (errorRange < 0) throw new NumberFormatException();

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldRngERPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPPls.setSelected(false);
                    return;
                }

                try {
                    freg = Double.parseDouble(txtFldRTCFrqRPPls.getText());
                    if (freg < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCFrqRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPPls.setSelected(false);
                    return;
                }

                try {
                    anountMeash = Integer.parseInt(txtFldRTCAmtMshRPPls.getText());
                    if (anountMeash < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCAmtMshRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPPls.setSelected(false);
                    return;
                }

                try {
                    timeMeas = Integer.parseInt(txtFldRTCTimeMshRPPls.getText());
                    if (timeMeas < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCTimeMshRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
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
                txtFldRTCFrqRPPls.setDisable(false);

                RTCTogBtnRPPls.setSelected(false);
            }
        }
        //Добаление теста "точность хода часов" RP-
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
                    if (errorRange < 0) throw new NumberFormatException();

                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldRngERPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPMns.setSelected(false);
                    return;
                }

                try {
                    freg = Double.parseDouble(txtFldRTCFrqRPMns.getText());
                    if (freg < 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCFrqRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPMns.setSelected(false);
                    return;
                }

                try {
                    anountMeash = Integer.parseInt(txtFldRTCAmtMshRPMns.getText());
                    if (anountMeash < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCAmtMshRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnRTCRPMns.setSelected(false);
                    return;
                }

                try {
                    timeMeas = Integer.parseInt(txtFldRTCTimeMshRPMns.getText());
                    if (timeMeas < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Неверные данные");
                    txtFldRTCTimeMshRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
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
                txtFldRTCFrqRPMns.setDisable(false);

                RTCTogBtnRPMns.setSelected(false);
            }
        }

        //------------------------------------------------------ Проверка счётного механизма
        //Проверка счётного механизма AP+
        if (event.getSource() == addTglBtnConstAPPls) {

            if (addTglBtnConstAPPls.isSelected()) {

                txtFieldConstTimeAPPls.setStyle("");
                txtFieldEngConstAPPls.setStyle("");
                txtFieldConsProcUAPPls.setStyle("");
                txtFieldConsProcIAPPls.setStyle("");
                txtFieldConsErAPPls.setStyle("");

                double Uproc;
                double IbProc;
                double errorRange;
                long timeTestToMill;

                //Проверка валидности выбранного пользователем значения напряжения для испытания
                try {
                     Uproc = Double.parseDouble(txtFieldConsProcUAPPls.getText());
                     if (Uproc < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcUAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выбранного пользователем значения тока для испытания
                try {
                     IbProc = Double.parseDouble(txtFieldConsProcIAPPls.getText());
                    if (IbProc < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcIAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPPls.setSelected(false);
                    return;
                }

                //Проверка валидности выбранного пользователем значения погрешности для испытания
                try {
                    errorRange = Double.parseDouble(txtFieldConsErAPPls.getText());
                    if (errorRange < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsErAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPPls.setSelected(false);
                    return;
                }

                //Если тест по количеству времени
                if (radBtnConstTimeAPPls.isSelected()) {

                    //Проверка валидности выбранного пользователем значения времени для испытания

                    String[] arrTime = txtFieldConstTimeAPPls.getText().trim().split(":");

                    if (arrTime.length != 3 || arrTime[0].trim().length() > 2 || arrTime[1].trim().length() > 2 || arrTime[2].trim().length() > 2) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeAPPls.setStyle("-fx-border-color: red ;  -fx-focus-color: red ;");
                        addTglBtnConstAPPls.setSelected(false);
                        return;
                    }

                    try {

                        int hours = Integer.parseInt(arrTime[0].trim());
                        if (hours < 0) throw new NumberFormatException();

                        int mins = Integer.parseInt(arrTime[1].trim());
                        if (mins < 0) throw new NumberFormatException();

                        if (mins > 59) {
                            ConsoleHelper.infoException("Количество минут не может быть больше 59");
                            txtFieldConstTimeAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstAPPls.setSelected(false);
                            return;
                        }

                        int sec = Integer.parseInt(arrTime[2].trim());
                        if (sec < 0) throw new NumberFormatException();

                        if (sec > 59) {
                            ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                            txtFieldConstTimeAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstAPPls.setSelected(false);
                            return;
                        }

                        timeTestToMill = (hours * 3600 + mins * 60 + sec) * 1000;

                        if (timeTestToMill < 60000) {
                            ConsoleHelper.infoException("Время теста не должно быть меньше:\n60 секунд");
                            txtFieldConstTimeAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstAPPls.setSelected(false);
                            return;
                        }

                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPPls.setSelected(false);
                        return;
                    }

                    //Добавление точки испытания
                    testListForCollumAPPls.add(new ConstantCommand(false, true, timeTestToMill,
                            "CNT;T;A;P", "Сонстанта AP+", Uproc, IbProc, 0, 0, -errorRange, errorRange));

                //Если тест по количеству энергии
                } else {
                    double testEnergy;

                    //Проверка валидности выбранного пользователем значения количества энергии для испытания
                    try {
                        testEnergy = Double.parseDouble(txtFieldEngConstAPPls.getText());
                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные");
                        txtFieldEngConstAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPPls.setSelected(false);
                        return;
                    }

                    if (testEnergy < 0.1) {
                        ConsoleHelper.infoException("Неверные данные\nэнергия не должна быть меньше 0.1кВ");
                        txtFieldEngConstAPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPPls.setSelected(false);
                        return;
                    }

                    //Добавление точки испытания
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

            //Удаление команды из списка
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

        //Проверка счётного механизма AP-
        if (event.getSource() == addTglBtnConstAPMns) {

            if (addTglBtnConstAPMns.isSelected()) {

                txtFieldConstTimeAPMns.setStyle("");
                txtFieldEngConstAPMns.setStyle("");
                txtFieldConsProcUAPMns.setStyle("");
                txtFieldConsProcIAPMns.setStyle("");
                txtFieldConsErAPMns.setStyle("");

                double Uproc;
                double IbProc;
                double errorRange;
                long timeTestToMill;

                try {
                    Uproc = Double.parseDouble(txtFieldConsProcUAPMns.getText());
                    if (Uproc < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcUAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPMns.setSelected(false);
                    return;
                }

                try {
                    IbProc = Double.parseDouble(txtFieldConsProcIAPMns.getText());
                    if (IbProc < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcIAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPMns.setSelected(false);
                    return;
                }

                try {
                    errorRange = Double.parseDouble(txtFieldConsErAPMns.getText());
                    if (errorRange < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsErAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstAPMns.setSelected(false);
                    return;
                }

                if (radBtnConstTimeAPMns.isSelected()) {

                    String[] arrTime = txtFieldConstTimeAPMns.getText().trim().split(":");

                    if (arrTime.length != 3 || arrTime[0].trim().length() > 2 || arrTime[1].trim().length() > 2 || arrTime[2].trim().length() > 2) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeAPMns.setStyle("-fx-border-color: red ;  -fx-focus-color: red ;");
                        addTglBtnConstAPMns.setSelected(false);
                        return;
                    }

                    try {

                        int hours = Integer.parseInt(arrTime[0].trim());
                        if (hours < 0) throw new NumberFormatException();

                        int mins = Integer.parseInt(arrTime[1].trim());
                        if (mins < 0) throw new NumberFormatException();

                        if (mins > 59) {
                            ConsoleHelper.infoException("Количество минут не может быть больше 59");
                            txtFieldConstTimeAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstAPMns.setSelected(false);
                            return;
                        }

                        int sec = Integer.parseInt(arrTime[2].trim());
                        if (sec < 0) throw new NumberFormatException();

                        if (sec > 59) {
                            ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                            txtFieldConstTimeAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstAPMns.setSelected(false);
                            return;
                        }

                        timeTestToMill = (hours * 3600 + mins * 60 + sec) * 1000;

                        if (timeTestToMill < 60000) {
                            ConsoleHelper.infoException("Время теста не должно быть меньше:\n60 секунд");
                            txtFieldConstTimeAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstAPMns.setSelected(false);
                            return;
                        }

                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPMns.setSelected(false);
                        return;
                    }

                    testListForCollumAPMns.add(new ConstantCommand(false, true, timeTestToMill,
                            "CNT;T;A;N", "Сонстанта AP-", Uproc, IbProc, 1, 1, -errorRange, errorRange));

                } else {
                    double testEnergy;
                    try {
                        testEnergy = Double.parseDouble(txtFieldEngConstAPMns.getText());
                    } catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные");
                        txtFieldEngConstAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPMns.setSelected(false);
                        return;
                    }

                    if (testEnergy < 0.1) {
                        ConsoleHelper.infoException("Неверные данные\nэнергия не должна быть меньше 0.1кВ");
                        txtFieldEngConstAPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstAPMns.setSelected(false);
                        return;
                    }

                    testListForCollumAPMns.add(new ConstantCommand(false, false, testEnergy,
                            "CNT;E;A;N", "Сонстанта AP-", Uproc, IbProc, 1, 1, -errorRange, errorRange));

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
        //Проверка счётного механизма RP+
        if (event.getSource() == addTglBtnConstRPPls) {

            if (addTglBtnConstRPPls.isSelected()) {

                txtFieldConstTimeRPPls.setStyle("");
                txtFieldEngConstRPPls.setStyle("");
                txtFieldConsProcURPPls.setStyle("");
                txtFieldConsProcIRPPls.setStyle("");
                txtFieldConsErRPPls.setStyle("");

                double Uproc;
                double IbProc;
                double errorRange;
                long timeTestToMill;

                try {
                    Uproc = Double.parseDouble(txtFieldConsProcURPPls.getText());
                    if (Uproc < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcURPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPPls.setSelected(false);
                    return;
                }

                try {
                    IbProc = Double.parseDouble(txtFieldConsProcIRPPls.getText());
                    if (IbProc < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcIRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPPls.setSelected(false);
                    return;
                }

                try {
                    errorRange = Double.parseDouble(txtFieldConsErRPPls.getText());
                    if (errorRange < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsErRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPPls.setSelected(false);
                    return;
                }

                if (radBtnConstTimeRPPls.isSelected()) {

                    String[] arrTime = txtFieldConstTimeRPPls.getText().trim().split(":");

                    if (arrTime.length != 3 || arrTime[0].trim().length() > 2 || arrTime[1].trim().length() > 2 || arrTime[2].trim().length() > 2) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeRPPls.setStyle("-fx-border-color: red ;  -fx-focus-color: red ;");
                        addTglBtnConstRPPls.setSelected(false);
                        return;
                    }

                    try {

                        int hours = Integer.parseInt(arrTime[0].trim());
                        if (hours < 0) throw new NumberFormatException();

                        int mins = Integer.parseInt(arrTime[1].trim());
                        if (mins < 0) throw new NumberFormatException();

                        if (mins > 59) {
                            ConsoleHelper.infoException("Количество минут не может быть больше 59");
                            txtFieldConstTimeRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstRPPls.setSelected(false);
                            return;
                        }

                        int sec = Integer.parseInt(arrTime[2].trim());
                        if (sec < 0) throw new NumberFormatException();

                        if (sec > 59) {
                            ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                            txtFieldConstTimeRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstRPPls.setSelected(false);
                            return;
                        }

                        timeTestToMill = (hours * 3600 + mins * 60 + sec) * 1000;

                        if (timeTestToMill < 60000) {
                            ConsoleHelper.infoException("Время теста не должно быть меньше:\n60 секунд");
                            txtFieldConstTimeRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstRPPls.setSelected(false);
                            return;
                        }

                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPPls.setSelected(false);
                        return;
                    }

                    testListForCollumRPPls.add(new ConstantCommand(false, true, timeTestToMill,
                            "CNT;T;R;P", "Сонстанта RP+", Uproc, IbProc, 0, 2, -errorRange, errorRange));


                } else {
                    double testEnergy;
                    try {
                        testEnergy = Double.parseDouble(txtFieldEngConstRPPls.getText());
                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные");
                        txtFieldEngConstRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPPls.setSelected(false);
                        return;
                    }

                    if (testEnergy < 0.1) {
                        ConsoleHelper.infoException("Неверные данные\nэнергия не должна быть меньше 0.1кВ");
                        txtFieldEngConstRPPls.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPPls.setSelected(false);
                        return;
                    }

                    testListForCollumRPPls.add(new ConstantCommand(false, false, testEnergy,
                            "CNT;E;R;P", "Сонстанта RP+", Uproc, IbProc, 0, 2, -errorRange, errorRange));

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
        //Проверка счётного механизма RP-
        if (event.getSource() == addTglBtnConstRPMns) {

            if (addTglBtnConstRPMns.isSelected()) {

                txtFieldConstTimeRPMns.setStyle("");
                txtFieldEngConstRPMns.setStyle("");
                txtFieldConsProcURPMns.setStyle("");
                txtFieldConsProcIRPMns.setStyle("");
                txtFieldConsErRPMns.setStyle("");

                double Uproc;
                double IbProc;
                double errorRange;
                long timeTestToMill;

                try {
                    Uproc = Double.parseDouble(txtFieldConsProcURPMns.getText());
                    if (Uproc < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcURPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPMns.setSelected(false);
                    return;
                }

                try {
                    IbProc = Double.parseDouble(txtFieldConsProcIRPMns.getText());
                    if (IbProc < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsProcIRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPMns.setSelected(false);
                    return;
                }

                try {
                    errorRange = Double.parseDouble(txtFieldConsErRPMns.getText());
                    if (errorRange < 0) throw new NumberFormatException();
                }catch (NumberFormatException e) {
                    ConsoleHelper.infoException("Неверные данные");
                    txtFieldConsErRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                    addTglBtnConstRPMns.setSelected(false);
                    return;
                }

                if (radBtnConstTimeRPMns.isSelected()) {

                    String[] arrTime = txtFieldConstTimeRPMns.getText().trim().split(":");

                    if (arrTime.length != 3 || arrTime[0].trim().length() > 2 || arrTime[1].trim().length() > 2 || arrTime[2].trim().length() > 2) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeRPMns.setStyle("-fx-border-color: red ;  -fx-focus-color: red ;");
                        addTglBtnConstRPMns.setSelected(false);
                        return;
                    }

                    try {

                        int hours = Integer.parseInt(arrTime[0].trim());
                        if (hours < 0) throw new NumberFormatException();

                        int mins = Integer.parseInt(arrTime[1].trim());
                        if (mins < 0) throw new NumberFormatException();

                        if (mins > 59) {
                            ConsoleHelper.infoException("Количество минут не может быть больше 59");
                            txtFieldConstTimeRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstRPMns.setSelected(false);
                            return;
                        }

                        int sec = Integer.parseInt(arrTime[2].trim());
                        if (sec < 0) throw new NumberFormatException();

                        if (sec > 59) {
                            ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                            txtFieldConstTimeRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstRPMns.setSelected(false);
                            return;
                        }

                        timeTestToMill = (hours * 3600 + mins * 60 + sec) * 1000;

                        if (timeTestToMill < 60000) {
                            ConsoleHelper.infoException("Время теста не должно быть меньше:\n60 секунд");
                            txtFieldConstTimeRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                            addTglBtnConstRPMns.setSelected(false);
                            return;
                        }

                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные\nДолжен быть формат: чч:мм:cc");
                        txtFieldConstTimeRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPMns.setSelected(false);
                        return;
                    }

                    testListForCollumRPMns.add(new ConstantCommand(false, true, timeTestToMill,
                            "CNT;T;R;N", "Сонстанта RP-", Uproc, IbProc, 1, 3, -errorRange, errorRange));

                } else {
                    double testEnergy;
                    try {
                        testEnergy = Double.parseDouble(txtFieldEngConstRPMns.getText());
                    }catch (NumberFormatException e) {
                        ConsoleHelper.infoException("Неверные данные");
                        txtFieldEngConstRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPMns.setSelected(false);
                        return;
                    }

                    if (testEnergy < 0.1) {
                        ConsoleHelper.infoException("Неверные данные\nэнергия не должна быть меньше 0.1кВ");
                        txtFieldEngConstRPMns.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                        addTglBtnConstRPMns.setSelected(false);
                        return;
                    }

                    testListForCollumRPMns.add(new ConstantCommand(false, false, testEnergy,
                            "CNT;E;R;N", "Сонстанта RP-", Uproc, IbProc, 1, 3, -errorRange, errorRange));

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

        //------------------------------------------------------ Проверка работоспособности реле
        if (event.getSource() == addTglBtnRelay) {

            //Добавление команды
            if (addTglBtnRelay.isSelected()) {

                //Проверка работоспособности реле AP+
                if (APPlusCRPSTA.isSelected()) {
                    addRelayCommand(0);

                    //Проверка работоспособности реле AP-
                } else if (APMinusCRPSTA.isSelected()) {
                    addRelayCommand(1);

                    //Проверка работоспособности реле RP+
                } else if (RPPlusCRPSTA.isSelected()) {
                    addRelayCommand(2);

                    //Проверка работоспособности реле RP-
                } else if (RPMinusCRPSTA.isSelected()) {
                    addRelayCommand(3);
                }

            //Удаление команды
            } else {
                //AP+
                if (APPlusCRPSTA.isSelected()) {
                    deleteRelayCommand(testListForCollumAPPls);

                    //AP-
                } else if (APMinusCRPSTA.isSelected()) {
                    deleteRelayCommand(testListForCollumAPMns);

                    //RP+
                } else if (RPPlusCRPSTA.isSelected()) {
                    deleteRelayCommand(testListForCollumRPPls);

                    //RP-
                } else if (RPMinusCRPSTA.isSelected()) {
                    deleteRelayCommand(testListForCollumRPMns);
                }
            }
        }
    }

    /**
     * Инициализирует ChoiseBox возможными типами  измерения точности хода часов
     */
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

    /**
     * Выставляет дефолдное состояние кнопок выбора токовой цепи при переключении между вкладками
     * смены направления тока и типа мощности
     */
    private void setDefPosBtn() {
        onePhaseBtn.setSelected(true);
        APhaseBtn.setSelected(false);
        BPhaseBtn.setSelected(false);
    }

    /**
     * Выводит выбранную пользователем сетку с точками на передний план
     * @param pane
     */
    private void gridPaneToFront(GridPane pane) {
        for (GridPane gridPane : gridPanesEnergyAndPhase) {
            if (pane.equals(gridPane)) {
                gridPane.setVisible(true);
            } else {
                gridPane.setVisible(false);
            }
        }
        pane.toFront();
    }

    /**
     * передаёт нужную сетку в зависимости от состояния кнопок
     * @return
     */
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

    /**
     * Добавляет точку в список точки методики по сформированному ID
     * Параметры сформированного ID:
     *
     * 1) U - точка для влияния по напряжению
     *    F - точка для влияния по частоте
     *
     * 2) Режим см. интерфейс StendDLL метод Adjust_UI параметр phase (Данный параметр зарезервирован и не используется, а формируется на этапе испытаний)
     *
     * 3) H - для однофазного стенда с одной токовой цепью
     *    A - для однофазного стенда с двумя токовыми цепями, первая токовая цепь
     *    B - для однофазного стенда с двумя токовыми цепями, вторая токовая цепь
     *
     * 4) A - активная энергия
     *    R - реактивная энергия
     *
     * 5) P - прямое направление тока (positive)
     *    N - обратное направление тока (negative)
     *
     * 6) ток процент (цифра) от значения базового тока (Ib)
     *    ток процент (цифра) от значения максимального тока (Imax)
     *
     * 7) коэфициент мощности значение (цифра) индуктивный (L)
     *    коэфициент мощности значение (цифра) емкостной (С)
     *    коэфициент мощности значение 1.0 ни индуктивный и не емкостной
     *
     * Пример: 1;H;A;P;0.2 Ib;0.5C
     *
     * @param testPoint ID - для добавления точки в методику
     */
    private void addTestPointInMethodic(String testPoint) {

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

        //Добавление точки AP+
        if (energyType.equals("A") && currentDirection.equals("P")) {
            testListForCollumAPPls.add(new ErrorCommand(false, testPoint, phase, current, 0, percent, iABC, powerFactor, 0));
        }

        //Добавление точки AP-
        if (energyType.equals("A") && currentDirection.equals("N")) {
            testListForCollumAPMns.add(new ErrorCommand(false, testPoint, phase, current, 1, percent, iABC, powerFactor, 1));
        }

        //Добавление точки RP+
        if (energyType.equals("R") && currentDirection.equals("P")) {
            testListForCollumRPPls.add(new ErrorCommand(false, testPoint, phase, current, 0, percent, iABC, powerFactor, 2));
        }

        //Добавление точки RP-
        if (energyType.equals("R") && currentDirection.equals("N")) {
            testListForCollumRPMns.add(new ErrorCommand(false, testPoint, phase, current, 1, percent, iABC, powerFactor, 3));
        }
    }

    /**
     * Удаляет команду ErrorCommadn по её ID из списка выполняемых команд
     * @param idCheckBox - ID команды
     */
    private void deleteTestPointInMethodic(String idCheckBox) {
        ErrorCommand errorCommand;
        String[] point = idCheckBox.split(";");

        //Удаление точки из AP+
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

        //Удаление точки из AP-
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

        //Удаление точки из RP+
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

        //Удаление точки из RP-
        } else if (point[2].equals("R") && point[3].equals("N")) {

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

    //-------------------------------------------------------- Реле
    /**
     * Общий метод инициализации полей и надписей в Pane для выбора точки испытания "Реле"
     */
    private void initRelayTest() {

        txtFieldRelayCurrent.setText("");
        txtFieldRelayTime.setText("");
        txtFieldAmountImpRelay.setText("");

        addTglBtnRelay.setSelected(false);
        tglBtnRelay.setSelected(false);

        txtFieldRelayCurrent.setDisable(false);
        txtFieldRelayTime.setDisable(false);
        txtFieldAmountImpRelay.setDisable(false);

        if (APPlusCRPSTA.isSelected()) {

            labelDirection.setText("Активная энергия в прямом направлении тока");

            selectReleyDirection(testListForCollumAPPls);

        } else if (APMinusCRPSTA.isSelected()) {

            labelDirection.setText("Активная энергия в обратном направлении тока");

            selectReleyDirection(testListForCollumAPMns);

        } else if (RPPlusCRPSTA.isSelected()) {

            labelDirection.setText("Реактивная энергия в прямом направлении тока");

            selectReleyDirection(testListForCollumRPPls);

        } else if (RPMinusCRPSTA.isSelected()) {

            labelDirection.setText("Реактивная энергия в обратном направлении тока");

            selectReleyDirection(testListForCollumRPMns);
        }
    }

    /**
     * Присваивает полям панели выбора теста реле значения согласно направлению тока и типу мощности
     * @param testListForColumn
     */
    private void selectReleyDirection(List<Commands> testListForColumn) {

        for (Commands commands : testListForColumn) {
            if (commands instanceof RelayCommand) {

                txtFieldRelayCurrent.setText(String.valueOf(commands.getRatedCurr()));
                txtFieldRelayTime.setText(getTime(((RelayCommand) commands).getUserTimeTest()));
                txtFieldAmountImpRelay.setText(String.valueOf(((RelayCommand) commands).getPulseValue()));

                addTglBtnRelay.setSelected(true);
                tglBtnRelay.setSelected(true);

                txtFieldRelayCurrent.setDisable(true);
                txtFieldRelayTime.setDisable(true);
                txtFieldAmountImpRelay.setDisable(true);
                break;
            }
        }
    }

    /**
     * Добавляет команду реле в общий список исполняемых команд
     * @param chanelFlag
     */
    private void addRelayCommand(int chanelFlag) {

        txtFieldRelayCurrent.setStyle("");
        txtFieldRelayTime.setStyle("");
        txtFieldAmountImpRelay.setStyle("");

        double current;
        long timeTest;
        int amountImp;

        //Проверка валидности выбранного пользователем значения тока для испытания
        try {
            current = Double.parseDouble(txtFieldRelayCurrent.getText());
            if (current < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ConsoleHelper.infoException("Неверные данные");
            txtFieldRelayCurrent.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
            addTglBtnRelay.setSelected(false);
            return;
        }

        //Проверка валидности выбранного пользователем значения времени для испытания
        try {
            String[] timeArr = txtFieldRelayTime.getText().trim().split(":");

            if (timeArr.length != 3 || timeArr[0].trim().length() > 2 || timeArr[1].trim().length() > 2 || timeArr[2].trim().length() > 2) {
                throw new NumberFormatException();
            }

            int hour = Integer.parseInt(timeArr[0].trim());
            if (hour < 0) throw new NumberFormatException();

            int mins = Integer.parseInt(timeArr[1].trim());
            if (mins < 0) throw new NumberFormatException();
            if (mins > 59) {
                ConsoleHelper.infoException("Количество минут не может быть больше 59");
                txtFieldRelayTime.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                addTglBtnRelay.setSelected(false);
                return;
            }

            int sek = Integer.parseInt(timeArr[2].trim());
            if (sek < 0) throw new NumberFormatException();
            if (sek > 59) {
                ConsoleHelper.infoException("Количество секунд не может быть больше 59");
                txtFieldRelayTime.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                addTglBtnRelay.setSelected(false);
                return;
            }

            timeTest = (3600 * hour + 60 * mins + sek) * 1000;

        } catch (NumberFormatException e) {
            e.printStackTrace();
            ConsoleHelper.infoException("Неверные данные");
            txtFieldRelayTime.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
            addTglBtnRelay.setSelected(false);
            return;
        }

        //Проверка валидности выбранного пользователем значения количества импульсов для испытания
        try {
            amountImp = Integer.parseInt(txtFieldAmountImpRelay.getText());
            if (amountImp < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ConsoleHelper.infoException("Неверные данные");
            txtFieldAmountImpRelay.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
            addTglBtnRelay.setSelected(false);
            return;
        }

        //В зависимости от chanelFlag добавляет команду в нужный список команд
        switch (chanelFlag) {
            case 0: {
                testListForCollumAPPls.add(new RelayCommand(true, "Реле AP+", "RLY;A;P", chanelFlag, timeTest, amountImp, current));
            }break;
            case 1: {
                testListForCollumAPMns.add(new RelayCommand(true, "Реле AP-", "RLY;A;N", chanelFlag, timeTest, amountImp, current));
            }break;
            case 2: {
                testListForCollumRPPls.add(new RelayCommand(true, "Реле RP+", "RLY;R;P", chanelFlag, timeTest, amountImp, current));
            }break;
            case 3: {
                testListForCollumRPMns.add(new RelayCommand(true, "Реле RP-", "RLY;R;N", chanelFlag, timeTest, amountImp, current));
            }break;
        }

        txtFieldRelayCurrent.setDisable(true);
        txtFieldRelayTime.setDisable(true);
        txtFieldAmountImpRelay.setDisable(true);

        tglBtnRelay.setSelected(true);

    }

    /**
     * Удаляет команду проверки реле из общего списка исполняемых команд
     * @param testListForCollum
     */
    private void deleteRelayCommand (List<Commands> testListForCollum) {

        for (Commands command : testListForCollum) {
            if (command instanceof RelayCommand) {
                testListForCollum.remove(command);
                break;
            }
        }

        txtFieldRelayCurrent.setDisable(false);
        txtFieldRelayTime.setDisable(false);
        txtFieldAmountImpRelay.setDisable(false);

        tglBtnRelay.setSelected(false);
    }

    /**
     * Действие при изменении списка команд (необходимо для отслеживания изменений и предложения сохранить изменения)
     */
    public void addListenerInTestPointList() {
        testListForCollumAPPls.addListener(new ListChangeListener<Commands>() {
            @Override
            public void onChanged(Change<? extends Commands> c) {
                while (c.next()) {
                    if (c.wasAdded() || c.wasRemoved() || c.wasPermutated()) {
                        saveChange = true;
                    }
                }
            }
        });

        testListForCollumAPMns.addListener(new ListChangeListener<Commands>() {
            @Override
            public void onChanged(Change<? extends Commands> c) {
                while (c.next()) {
                    if (c.wasAdded() || c.wasRemoved() || c.wasPermutated()) {
                        saveChange = true;
                    }
                }
            }
        });

        testListForCollumRPPls.addListener(new ListChangeListener<Commands>() {
            @Override
            public void onChanged(Change<? extends Commands> c) {
                while (c.next()) {
                    if (c.wasAdded() || c.wasRemoved() || c.wasPermutated()) {
                        saveChange = true;
                    }
                }
            }
        });

        testListForCollumRPMns.addListener(new ListChangeListener<Commands>() {
            @Override
            public void onChanged(Change<? extends Commands> c) {
                while (c.next()) {
                    if (c.wasAdded() || c.wasRemoved() || c.wasPermutated()) {
                        saveChange = true;
                    }
                }
            }
        });

        actionOnCloseWindow();
    }

    /**
     * Действие при закрытии окна, предложение сохранить изменения
     */
    private void actionOnCloseWindow() {
        Stage mainStage = (Stage) saveBtn.getScene().getWindow();

        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();

                if (saveChange) {
                    Boolean b = ConsoleHelper.yesOrNoFrame("Сохранение изменений", "Сохранить изменения?");

                    if (b != null) {
                        if (b) {
                            saveBtn.fire();
                        } else {
                            Stage stage1 = (Stage) methodicsAddEditDeleteFrameController.getEditMetBtn().getScene().getWindow();
                            stage1.show();
                            mainStage.close();
                        }
                    }
                } else {
                    Stage stage1 = (Stage) methodicsAddEditDeleteFrameController.getEditMetBtn().getScene().getWindow();
                    stage1.show();
                    mainStage.close();
                }
            }
        });
    }

    /**
     * Преобразует миллисекунды в формат hh:mm:ss
     * @param mlS
     * @return
     */
    private String getTime(long mlS){
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mlS),
                TimeUnit.MILLISECONDS.toMinutes(mlS) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(mlS) % TimeUnit.MINUTES.toSeconds(1));
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
