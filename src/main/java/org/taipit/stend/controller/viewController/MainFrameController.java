package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.FrameManager;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером основного окна "mainFrame.fxml".
 */
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
        //Действие при переходе в настройки программы
        if (event.getSource() == mainFramePropertiesBtn) {

            //Пароль
            if (!ConsoleHelper.properties.getProperty("config").isEmpty()) {
                if (!ConsoleHelper.passwordFrame()) {
                    return;
                }
            }

            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.PROPERTIES);
        }

        //Действие при переходе в окно созданных методик поверки
        if(event.getSource() == mainFrameMethodicsBtn) {

            //Пароль
            if (!ConsoleHelper.properties.getProperty("config").isEmpty()) {
                if (!ConsoleHelper.passwordFrame()) {
                    return;
                }
            }

            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.METHODIC);
        }

        //Действие при переходе в параметры теста
        if (event.getSource() == mainFrameParamTestBtn) {
            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.PARAMTEST);
        }

        //Действие при переходе в окно результаты тестов
        if (event.getSource() == mainFrameResultsBtn) {
            FrameManager.frameManagerInstance().getFrame(FrameManager.FrameType.RESULTS);
        }
    }
}
