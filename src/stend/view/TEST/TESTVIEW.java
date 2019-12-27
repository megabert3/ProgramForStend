package stend.view.TEST;

import java.net.URL;
import java.util.*;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.model.Methodic;

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

    //Значения коэффициента мощности
    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C", "0.14C", "1313.K");

    //Значения выставленного тока
    private List<String> current = Arrays.asList("Imax", "0.5Imax", "0.2Imax", "0.5Ib", "Ib","0.2Ib", "0.1Ib", "0.05Ib", "0.001Ib");

    //Список GridPane для выставления точек поверки
    private List<GridPane> gridPanesEnergyAndPhase;

    //Это трёхфазный стенд?
    private boolean isThrePhaseStend;

    @FXML
    private ResourceBundle resources = ResourceBundle.getBundle("stendProperties");

    @FXML
    private URL location;

    @FXML
    private TableView<?> viewPointTable;

    //-------------------------------------------------------
    @FXML
    private GridPane gridPaneAllPhaseAPPlus;

    @FXML
    private GridPane gridPanePhaseAAPPlus;

    @FXML
    private GridPane gridPanePhaseBAPPlus;

    @FXML
    private GridPane gridPanePhaseCAPPlus;

    @FXML
    private GridPane gridPaneAllPhaseAPMinus;

    @FXML
    private GridPane gridPanePhaseAAPMinus;

    @FXML
    private GridPane gridPanePhaseBAPMinus;

    @FXML
    private GridPane gridPanePhaseCAPMinus;

    @FXML
    private GridPane gridPaneAllPhaseRPPlus;

    @FXML
    private GridPane gridPanePhaseARPPlus;

    @FXML
    private GridPane gridPanePhaseBRPPlus;

    @FXML
    private GridPane gridPanePhaseCRPPlus;

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
    private VBox vBoxForPhaseTgBtn;

    @FXML
    private ToggleButton allPhaseBtn;

    @FXML
    private ToggleButton APhaseBtn;

    @FXML
    private ToggleButton BPhaseBtn;

    @FXML
    private ToggleButton CPhaseBtn;

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

    @FXML
    private Button CancelBtn;

    @FXML
    private Button SaveBtn;

    @FXML
    private ToggleButton CRPBtn;

    @FXML
    private ToggleButton STABtn;

    @FXML
    private ToggleButton RTCBtn;

    @FXML
    private ToggleButton ConstBtn;

    @FXML
    private GridPane gridPaneCreep;

    @FXML
    private GridPane GridPaneStart;

    @FXML
    private GridPane GridPaneRTC;

    @FXML
    private GridPane GridPaneConst;

    @FXML
    void SaveOrCancelAction(ActionEvent event) {

    }

    @FXML
    void setCRPSTAFrameAction(ActionEvent event) {

    }

    @FXML
    void setPointFrameAction(ActionEvent event) {
        setGropToggleButton(event);
        gridPaneToFront(Objects.requireNonNull(getGridPane()));
    }

    @FXML
    void initialize() {
        if (!resources.getString("stendType").equals("OnePhaseStend")) {
            isThrePhaseStend = true;
        }

        initGridPane();
        APPlus.setSelected(true);
        allPhaseBtn.setSelected(true);
        gridPaneAllPhaseAPPlus.toFront();
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
            gridPaneAllPhaseAPPlus.setId("1;H;AP;Pls");
            gridPanePhaseAAPPlus.setId("1;A;AP;Pls");
            gridPanePhaseBAPPlus.setId("1;B;AP;Pls");
            gridPanePhaseCAPPlus.setId("1;C;AP;Pls");
            gridPaneAllPhaseAPMinus.setId("1;H;AP;Mns");
            gridPanePhaseAAPMinus.setId("1;A;AP;Mns");
            gridPanePhaseBAPMinus.setId("1;B;AP;Mns");
            gridPanePhaseCAPMinus.setId("1;C;AP;Mns");
            gridPaneAllPhaseRPPlus.setId("5;H;RP;Pls");
            gridPanePhaseARPPlus.setId("5;A;RP;Pls");
            gridPanePhaseBRPPlus.setId("5;B;RPPls");
            gridPanePhaseCRPPlus.setId("5;C;RPPls");
            gridPaneAllPhaseRPMinus.setId("5;H;RP;Mns");
            gridPanePhaseARPMinus.setId("5;A;RP;Mns");
            gridPanePhaseBRPMinus.setId("5;B;RP;Mns");
            gridPanePhaseCRPMinus.setId("5;C;RP;Mns");
        } else {
            gridPaneAllPhaseAPPlus.setId("H;AP;Pls");
            gridPanePhaseAAPPlus.setId("A;AP;Pls");
            gridPanePhaseBAPPlus.setId("B;AP;Pls");
            gridPanePhaseCAPPlus.setId("C;AP;Pls");
            gridPaneAllPhaseAPMinus.setId("H;AP;Mns");
            gridPanePhaseAAPMinus.setId("A;AP;Mns");
            gridPanePhaseBAPMinus.setId("B;AP;Mns");
            gridPanePhaseCAPMinus.setId("C;AP;Mns");
            gridPaneAllPhaseRPPlus.setId("H;RP;Pls");
            gridPanePhaseARPPlus.setId("A;RP;Pls");
            gridPanePhaseBRPPlus.setId("B;RPPls");
            gridPanePhaseCRPPlus.setId("C;RPPls");
            gridPaneAllPhaseRPMinus.setId("H;RP;Mns");
            gridPanePhaseARPMinus.setId("A;RP;Mns");
            gridPanePhaseBRPMinus.setId("B;RP;Mns");
            gridPanePhaseCRPMinus.setId("C;RP;Mns");
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
                checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                    if (newVal) {
                        System.out.println("Кнопка зажата " + finalCheckBox.getId());
                    } else {
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


        if (event.getSource() == APPlus) {
            setDefPosBtn();
            APPlus.setSelected(true);
            APMinus.setSelected(false);
            RPPlus.setSelected(false);
            RPMinus.setSelected(false);
        }
        if (event.getSource() == APMinus) {
            setDefPosBtn();
            APMinus.setSelected(true);
            APPlus.setSelected(false);
            RPPlus.setSelected(false);
            RPMinus.setSelected(false);
        }
        if (event.getSource() == RPPlus) {
            setDefPosBtn();
            RPPlus.setSelected(true);
            APPlus.setSelected(false);
            APMinus.setSelected(false);
            RPMinus.setSelected(false);
        }
        if (event.getSource() == RPMinus) {
            setDefPosBtn();
            RPMinus.setSelected(true);
            RPPlus.setSelected(false);
            APPlus.setSelected(false);
            APMinus.setSelected(false);
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

    //Добавляет тестовую точку в методику
    public void addTestPointInMethodic(String testPoint) {
        if (isThrePhaseStend) {
            stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
        } else {
            stendDLLCommands = OnePhaseStend.getOnePhaseStendInstance();
        }

        String[] dirCurFactor = testPoint.split(";");
        String digection = dirCurFactor[0];
        String curr = dirCurFactor[1];
        String factor = dirCurFactor[2];

//        public ErrorCommand(StendDLLCommands stendDLLCommands, int phase, String current,
//        int revers, double currPer, String iABC, String cosP, int channelFlag) {

    //ChannelFlag - Режим импульсов:
//		0 - Активная мощность (+)
// 		1 - Активная мощность (-)
// 		2 - Реактивная мощность (+)
// 		3 - Реактивная мощность (-)
// Включить напряжение и ток
// Phase - Режим:
// 		0 - Однофазный
//		1 - Трех-фазный четырех-проводной
//		2 - Трех-фазный трех-проводной
// 		3 - Трех-фазный трех-проводной реактив 90 градусов
// 		4 - Трех-фазный трех-проводной реактив 60 градусов
//		5 - Трех-фазный четырех-проводной (реактив)
//		6 - Трех-фазный трех-проводной (реактив)
//		7 - Однофазный реактив
// Rated_Volt - напряжение
// Rated_Curr - ток
// Rated_Freq - частота
// PhaseSrequence - чередование фаз
//		0 - Прямое
//		1 - Обратное
// Revers - направление тока
//		0 - Прямой
//		1 - Обратный
// Volt_Per - Процент по напряжению (0 - 100)
// Curr_Per - Процент по току (0 - 100)
// IABC - строка, определяющая фазы, по которым пустить ток: A, B, C, H - все
// CosP - строка  с косинусом угла. Например: "1.0", "0.5L", "0.8C"
// SModel - Строка с моделью счетчика:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)
//		HY5101C-22, HY5101C-23, SY3803 (1 фаза)
//		TC-3000C (1 фаза)
//		TC-3000D (3 фазы)
// Dev_Port - номер com-порта
//        boolean Adjust_UI(int Phase,
//        double Rated_Volt,
//        double Rated_Curr,
//        double Rated_Freq,
//        int PhaseSrequence,
//        int Revers,
//        double Volt_Per,
//        double Curr_Per,
//        String IABC,
//        String CosP,
//        String SModel,
//        int Dev_Port);


    }

}
