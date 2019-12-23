package stend.controller.viewController;

import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import javax.xml.soap.Text;

public class methodicsFrameController {

    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");
    private List<String> current = Arrays.asList("Imax", "0.5Imax", "Ib", "0.2Ib", "0.1Ib", "0.05Ib");

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
//        setPointGird.getChildren().clear();
        setPointGird.getColumnConstraints().clear();
//        setPointGird.setColumnIndex(first, 0);
//        setPointGird.setColumnIndex(second, 1);
//        setPointGird.setColumnIndex(third, 2);
//
//        setPointGird.getChildren().setAll(first, second, third);
        setPointGird.setGridLinesVisible(true);


        for (int x = 0; x < current.size(); x++) {
            setPointGird.getColumnConstraints().add(new ColumnConstraints(75));
            setPointGird.add(new Label(current.get(x)), x + 1, 0);
        }
    }

}