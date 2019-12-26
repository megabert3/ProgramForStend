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
import javafx.scene.Node;
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
    }

    private Map<String, CheckBox> checkBoxMap = new HashMap<>();

    //Значения коэффициента мощности
    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");

    //Значения выставленного тока
    private List<String> current = Arrays.asList("Imax", "0.5Imax", "0.2Imax", "0.5Ib", "Ib","0.2Ib", "0.1Ib", "0.05Ib");

    //Список GridPane для выставления точек поверки
    private List<GridPane> gridPanesEnergyAndPhase;

    @FXML
    private ResourceBundle resources;

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
        initGridPane();
        APPlus.setSelected(true);
        allPhaseBtn.setSelected(true);
        gridPaneAllPhaseAPPlus.toFront();

    }

    private void initGridPane() {
        initGridPanesEnergyAndPhase();

        gridPaneAllPhaseAPPlus.setId("gridPaneAllPhaseAPPlus");
        gridPanePhaseAAPPlus.setId("gridPanePhaseAAPPlus");
        gridPanePhaseBAPPlus.setId("gridPanePhaseBAPPlus");
        gridPanePhaseCAPPlus.setId("gridPanePhaseCAPPlus");
        gridPaneAllPhaseAPMinus.setId("gridPaneAllPhaseAPMinus");
        gridPanePhaseAAPMinus.setId("gridPanePhaseAAPMinus");
        gridPanePhaseBAPMinus.setId("gridPanePhaseBAPMinus");
        gridPanePhaseCAPMinus.setId("gridPanePhaseCAPMinus");
        gridPaneAllPhaseRPPlus.setId("gridPaneAllPhaseRPPlus");
        gridPanePhaseARPPlus.setId("gridPanePhaseARPPlus");
        gridPanePhaseBRPPlus.setId("gridPanePhaseBRPPlus");
        gridPanePhaseCRPPlus.setId("gridPanePhaseCRPPlus");
        gridPanePhaseCRPPlus.setId("gridPanePhaseCRPPlus");
        gridPaneAllPhaseRPMinus.setId("gridPaneAllPhaseRPMinus");
        gridPanePhaseARPMinus.setId("gridPanePhaseARPMinus");
        gridPanePhaseBRPMinus.setId("gridPanePhaseBRPMinus");
        gridPanePhaseCRPMinus.setId("gridPanePhaseCRPMinus");

        for (GridPane pane : gridPanesEnergyAndPhase) {
            setBoxAndLabelGridPane(pane);
        }
    }

    //Добавляет все панели в лист
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
                checkBox.setId("Поле " + pane.getId() + " x: " + (x + 1) + " y: " + (y + 1));
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
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
