package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;
import org.taipit.stend.helper.frameManager.Frame;
import org.taipit.stend.helper.frameManager.FrameManager;

import java.io.IOException;


public class MainFrameController {

    @FXML
    private Button mainFramePropertiesBtn;

    @FXML
    private Button mainFrameMethodicsBtn;

    @FXML
    private Button mainFrameParamTestBtn;

    @FXML
    private Button mainFrameResultsBtn;

    @FXML
    void mainFrameHandleClicks(ActionEvent event) {
        if (event.getSource() == mainFramePropertiesBtn) {
            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.PROPERTIES);
        }

        if(event.getSource() == mainFrameMethodicsBtn) {
            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.METHODIC);
        }

        if (event.getSource() == mainFrameParamTestBtn) {
            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.PARAMTEST);
        }

        if (event.getSource() == mainFrameResultsBtn) {
            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.RESULTS);
        }
    }
}
