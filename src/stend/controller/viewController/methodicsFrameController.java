package stend.controller.viewController;

import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public class methodicsFrameController {

    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");
    private List<String> current = Arrays.asList();

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

    }




}