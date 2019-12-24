package stend.controller.viewController;

import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class methodicsFrameController {

    private Map<String, CheckBox> checkBoxMap = new HashMap<>();

    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");
    private List<String> current = Arrays.asList("Imax", "0.5Imax", "0.2Imax", "0.5Ib", "Ib","0.2Ib", "0.1Ib", "0.05Ib");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addMetBtn;

    @FXML
    private TableView<?> viewPointTable;

    @FXML
    private GridPane setPointGird;

    @FXML
    private Button saveMetBtn;

    @FXML
    private Button addMetBtn2;

    @FXML
    private Button deleteMetBtn;

    @FXML
    void initialize() {
        setPointGird.setGridLinesVisible(true);

        initGridPane();
        Label label;
        CheckBox checkBox = new CheckBox();
        for (int x = 0; x < current.size(); x++) {
            for (int y = 0; y < powerFactor.size(); y++) {

                //Устанавливаю значения строки тока
                if (y == 0) {
                    label = new Label(current.get(x));
                    GridPane.setRowIndex(label, y);
                    GridPane.setColumnIndex(label, x + 1);
                    GridPane.setHalignment(label, HPos.CENTER);
                    GridPane.setValignment(label, VPos.CENTER);
                    setPointGird.getChildren().add(label);

                //Устанавливаю значения строки PowerFactor
                }else if (x == 0) {
                    label = new Label(powerFactor.get(y));
                    GridPane.setRowIndex(label, y + 1);
                    GridPane.setColumnIndex(label, x);
                    GridPane.setHalignment(label, HPos.CENTER);
                    GridPane.setValignment(label, VPos.CENTER);
                    setPointGird.getChildren().add(label);
                }

//                if (x == current.size() - 1) {
//                    setPointGird.getColumnConstraints().add(new ColumnConstraints(50));
//                    setPointGird.getRowConstraints().add(new RowConstraints(20));
//                }
            }
        }
    }

    //Создаёт поле нужной величины
    private void initGridPane() {
        for (int i = 0; i < current.size() + 1; i++) {
            setPointGird.getColumnConstraints().add(new ColumnConstraints(50));
        }
        for (int j = 0; j < powerFactor.size() + 1; j++) {
            setPointGird.getRowConstraints().add(new RowConstraints(20));
        }
    }

}