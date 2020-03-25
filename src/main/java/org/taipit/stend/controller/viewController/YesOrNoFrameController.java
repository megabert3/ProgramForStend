package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;
import org.taipit.stend.model.Methodic;
import org.taipit.stend.model.MethodicsForTest;
import org.taipit.stend.model.ResultsTest;

import java.util.List;

public class YesOrNoFrameController {

    MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    private Stage stageSaveResultTest;

    private Methodic methodic;

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

    @FXML
    void yesOrNoAction(ActionEvent event) {

        Stage stageYesOrNo = (Stage) questionTxt.getScene().getWindow();
        //если вызвано с окна удаления методики "Удалить методику"
        if (event.getSource() == yesBtn) {
            if (deliteMethodic) {

                methodicsForTest.deleteMethodic(methodic.getMethodicName());

                methodicsAddEditDeleteFrameController.refreshAfterDelete();

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

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
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
