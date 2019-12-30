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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import stend.controller.Commands.ErrorCommand;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.helper.ConsoleHelper;
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

    //Имя методики поверки
    private String metodicName = "default";

    //Значения коэффициента мощности
    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C", "0.14C", "1313.K");

    //Значения выставленного тока
    private List<String> current = Arrays.asList("Imax", "0.5 Imax", "0.2 Imax", "0.5 Ib", "Ib","0.2 Ib", "0.1 Ib", "0.05 Ib", "0.00 1Ib");

    //Список GridPane для выставления точек поверки
    private List<GridPane> gridPanesEnergyAndPhase;

    //Это трёхфазный стенд?
    private boolean isThrePhaseStend;

    @FXML
    private ResourceBundle resources = ResourceBundle.getBundle("stendProperties");

    @FXML
    private URL location;

    //-------------------------------------------------------

    @FXML
    private TableView<?> viewPointTable;

    @FXML
    private TableColumn<?, ?> loadCurrTabCol;

    @FXML
    private TableColumn<?, ?> eMaxTabCol;

    @FXML
    private TableColumn<?, ?> eMinTabCol;

    @FXML
    private TableColumn<?, ?> amountImplTabCol;
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
    private TextField metodicNameTxtFld;

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
        if (ConsoleHelper.properties.getProperty("stendType").equals("ThreePhaseStend")) {
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
        String percent;
        //Максимальный или минимальный ток.
        String current;

        if (curAndPer.length == 1) {
            percent = "1.0";
            current = curAndPer[0];
        } else {
            percent = curAndPer[0];
            current = curAndPer[1];
        }

        //Коэф мощности
        String powerFactor = dirCurFactor[5];


        if (energyType.equals("A") && currentDirection.equals("P")) {
            methodic.addCommandToList(0, new ErrorCommand(stendDLLCommands, phase, current, 0, percent, iABC, powerFactor, 0));
        }

        if (energyType.equals("A") && currentDirection.equals("N")) {
            methodic.addCommandToList(1, new ErrorCommand(stendDLLCommands, phase, current, 1, percent, iABC, powerFactor, 1));
        }

        if (energyType.equals("R") && currentDirection.equals("P")) {
            methodic.addCommandToList(2, new ErrorCommand(stendDLLCommands, phase, current, 0, percent, iABC, powerFactor, 2));
        }

        if (energyType.equals("R") && currentDirection.equals("N")) {
            methodic.addCommandToList(3, new ErrorCommand(stendDLLCommands, phase, current, 1, percent, iABC, powerFactor, 3));
        }
    }

    private void refreshCollum() {

    }
}
