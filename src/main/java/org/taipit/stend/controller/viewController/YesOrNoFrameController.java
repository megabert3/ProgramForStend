package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;
import org.taipit.stend.model.metodics.Metodic;
import org.taipit.stend.model.metodics.MetodicsForTest;
import org.taipit.stend.model.result.ResultsTest;

import java.util.List;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна "yesOrNoFrame.fxml".
 *
 * Данный класс отвечает за выполнение сценариев в зависимости от ответа пользователя и является
 * собственной реализацией dialog.
 * Этот класс использовался только для случая удаления методики, далее я додумался сделать статически диалог.
 */
public class YesOrNoFrameController {

    //Кранилище с методиками поверки для счётчиков
    MetodicsForTest metodicsForTest = MetodicsForTest.getMetodicsForTestInstance();

    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    private Stage stageSaveResultTest;

    private Metodic methodicForStend;

    //Запрос с окна удаления методики
    private boolean deliteMethodic;

    //Запрос с окна сохранения результатов
    private boolean exitSaveResultFrameWithoutSaving;

    //Запрос с окна результатов
    private boolean resultsMeters;

    //Лист выбранных индексов для удаления
    private List<Integer> listIndexces;

    @FXML
    private Button noBtn;

    @FXML
    private Label questionTxt;

    @FXML
    private Button yesBtn;

    @FXML
    void initialize() {
    }

    /**
     * Действие при нажате кнопки да или нет
     * @param event
     */
    @FXML
    void yesOrNoAction(ActionEvent event) {

        Stage stageYesOrNo = (Stage) questionTxt.getScene().getWindow();

        //если вызвано с окна удаления методики "Удалить методику"
        if (event.getSource() == yesBtn) {
            if (deliteMethodic) {

                metodicsForTest.deleteMethodic(methodicForStend.getMetodicName());

                methodicsAddEditDeleteFrameController.refreshAfterDelete();

                metodicsForTest.serializationMetodics();

                stageYesOrNo.close();

                deliteMethodic = false;
            }

            if (exitSaveResultFrameWithoutSaving) {
                stageYesOrNo.close();
                stageSaveResultTest.close();
                exitSaveResultFrameWithoutSaving = false;
            }

            if (resultsMeters) {
                int forDelete = 0;

                for (Integer listIndexce : listIndexces) {
                    ResultsTest.getResultsTestInstance().getListAllResults().remove(listIndexce - forDelete);
                    forDelete++;
                }

                ResultsTest.getResultsTestInstance().serializationResults();

                stageYesOrNo.close();
                resultsMeters = false;
            }


        }

        if (event.getSource() == noBtn) {
            stageYesOrNo.close();
        }
    }

    public void setDeliteMethodic(boolean deliteMethodic) {
        this.deliteMethodic = deliteMethodic;
    }

    public void setMethodicForStend(Metodic methodicForStend) {
        this.methodicForStend = methodicForStend;
    }

    public Label getQuestionTxt() {
        return questionTxt;
    }

    public void setMethodicsAddEditDeleteFrameController(MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController) {
        this.methodicsAddEditDeleteFrameController = methodicsAddEditDeleteFrameController;
    }

    public void setExitSaveResultFrameWithoutSaving(boolean exitSaveResultFrameWithoutSaving) {
        this.exitSaveResultFrameWithoutSaving = exitSaveResultFrameWithoutSaving;
    }

    public void setStageSaveResultTest(Stage stageSaveResultTest) {
        this.stageSaveResultTest = stageSaveResultTest;
    }

    public void setResultsMeters(boolean resultsMeters) {
        this.resultsMeters = resultsMeters;
    }

    public void setListIndexces(List<Integer> listIndexces) {
        this.listIndexces = listIndexces;
    }
}
