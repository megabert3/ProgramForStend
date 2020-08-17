package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.FrameManager;

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

            //Пароль
            if (!ConsoleHelper.properties.getProperty("config").isEmpty()) {
                if (!ConsoleHelper.passwordFrame()) {
                    return;
                }
            }

            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.PROPERTIES);
        }

        if(event.getSource() == mainFrameMethodicsBtn) {

            //Пароль
            if (!ConsoleHelper.properties.getProperty("config").isEmpty()) {
                if (!ConsoleHelper.passwordFrame()) {
                    return;
                }
            }

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
