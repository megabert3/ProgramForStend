package stend.view.TEST;

import java.net.URL;
import java.util.*;

import javafx.application.Application;
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

public class TESTVIEW extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/stend/view/TEST/TESTVIEW.fxml"));
        primaryStage.setTitle("Тест");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        initialize();
    }

    private Map<String, CheckBox> checkBoxMap = new HashMap<>();

    //Значения коэффициента мощности
    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");

    //Значения выставленного тока
    private List<String> current = Arrays.asList("Imax", "0.5Imax", "0.2Imax", "0.5Ib", "Ib","0.2Ib", "0.1Ib", "0.05Ib");

    //Список GridPane для выставления точек поверки
    private List<GridPane> gridPanesEnergyAndPhaseList;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> viewPointTable;

    //-------------------------------------------------------
    @FXML
    private GridPane gridPaneAllPhaseAPPlus = new GridPane();

    @FXML
    private GridPane gridPanePhaseAAPPlus = new GridPane();

    @FXML
    private GridPane gridPanePhaseBAPPlus = new GridPane();

    @FXML
    private GridPane gridPanePhaseCAPPlus = new GridPane();

    @FXML
    private GridPane gridPaneAllPhaseAPMinus = new GridPane();

    @FXML
    private GridPane gridPanePhaseAAPMinus = new GridPane();

    @FXML
    private GridPane gridPanePhaseBAPMinus = new GridPane();

    @FXML
    private GridPane gridPanePhaseCAPMinus = new GridPane();

    @FXML
    private GridPane gridPaneAllPhaseRPPlus = new GridPane();

    @FXML
    private GridPane gridPanePhaseARPPlus = new GridPane();

    @FXML
    private GridPane gridPanePhaseBRPPlus = new GridPane();

    @FXML
    private GridPane gridPanePhaseCRPPlus = new GridPane();

    @FXML
    private GridPane gridPaneAllPhaseRPMinus = new GridPane();

    @FXML
    private GridPane gridPanePhaseARPMinus = new GridPane();

    @FXML
    private GridPane gridPanePhaseBRPMinus = new GridPane();

    @FXML
    private GridPane gridPanePhaseCRpMinus = new GridPane();

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
    }

    @FXML
    void initialize() {
        initGridPane();
    }

    //Понять почему ArrayList не инициируется
    private void initGridPane() {
        setBoxAndLabelGridPane(gridPaneAllPhaseAPPlus);
        setBoxAndLabelGridPane(gridPanePhaseAAPPlus);
        setBoxAndLabelGridPane(gridPanePhaseBAPPlus);
        setBoxAndLabelGridPane(gridPanePhaseCAPPlus);
        setBoxAndLabelGridPane(gridPaneAllPhaseAPMinus);
        setBoxAndLabelGridPane(gridPanePhaseAAPMinus);
        setBoxAndLabelGridPane(gridPanePhaseBAPMinus);
        setBoxAndLabelGridPane(gridPanePhaseCAPMinus);
        setBoxAndLabelGridPane(gridPaneAllPhaseRPPlus);
        setBoxAndLabelGridPane(gridPanePhaseARPPlus);
        setBoxAndLabelGridPane(gridPanePhaseBRPPlus);
        setBoxAndLabelGridPane(gridPanePhaseCRPPlus);
        setBoxAndLabelGridPane(gridPaneAllPhaseRPMinus);
        setBoxAndLabelGridPane(gridPanePhaseARPMinus);
        setBoxAndLabelGridPane(gridPanePhaseBRPMinus);
        setBoxAndLabelGridPane(gridPanePhaseCRpMinus);
    }

    private static List<GridPane> initGridPanesEnergyAndPhaseList() {
        return null;
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
            pane.getRowConstraints().add(new RowConstraints(20));
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
            APPlus.setSelected(true);
            APMinus.setSelected(false);
            RPPlus.setSelected(false);
            RPMinus.setSelected(false);
        }
        if (event.getSource() == APMinus) {
            APMinus.setSelected(true);
            APPlus.setSelected(false);
            RPPlus.setSelected(false);
            RPMinus.setSelected(false);
        }
        if (event.getSource() == RPPlus) {
            RPPlus.setSelected(true);
            APPlus.setSelected(false);
            APMinus.setSelected(false);
            RPMinus.setSelected(false);
        }
        if (event.getSource() == RPMinus) {
            RPMinus.setSelected(true);
            RPPlus.setSelected(false);
            APPlus.setSelected(false);
            APMinus.setSelected(false);
        }
    }

    //Выдаёт нужный GridPane в зависимости от нажатой кнопки
    public GridPane getGridPane() {
        if (allPhaseBtn.isSelected() && APPlus.isSelected()) {

        }
        return null;
    }
}
