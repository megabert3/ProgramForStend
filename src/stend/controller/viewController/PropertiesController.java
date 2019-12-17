package stend.controller.viewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {

    @FXML
    private Button passwordBtn;

    @FXML
    private Button stendBtn;

    @FXML
    private Button demoBtn;

    @FXML
    private AnchorPane stendPane;

    @FXML
    private AnchorPane passwordPane;

    @FXML
    private AnchorPane dmeoPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleClicks(javafx.event.ActionEvent actionEvent) {
        if (actionEvent.getSource() == passwordBtn) {
            stendPane.toFront();
        }
        if (actionEvent.getSource() == stendBtn) {
            passwordPane.toFront();
        }
        if (actionEvent.getSource() == demoBtn) {
            dmeoPane.toFront();
        }
    }
}