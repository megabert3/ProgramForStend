package stend.controller.viewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
//----------------------------------------------------------- Menu
    @FXML
    private Button passwordBtn;

    @FXML
    private Button stendBtn;

    @FXML
    private Button demoBtn;

//----------------------------------------------------------- stendPane
    @FXML
    private AnchorPane stendPane;

    @FXML
    private ChoiceBox<String> standPaneCOMList;

    @FXML
    private ChoiceBox<?> stendPaneStendTypeList;

    @FXML
    private ChoiceBox<?> stendPaneStendModel;

    @FXML
    private TextField standPaneAmoutPlase;

    @FXML
    private ChoiceBox<?> stendPaneRefMetModel;

//----------------------------------------------------------- passwordPane
    @FXML
    private AnchorPane passwordPane;

//----------------------------------------------------------- demoPane
    @FXML
    private AnchorPane demoPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleClicks(javafx.event.ActionEvent actionEvent) {
        if (actionEvent.getSource() == passwordBtn) {
            passwordPane.toFront();
        }
        if (actionEvent.getSource() == stendBtn) {
            stendPane.toFront();
        }
        if (actionEvent.getSource() == demoBtn) {
            demoPane.toFront();
        }
    }
}