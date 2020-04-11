package org.taipit.stend.controller.viewController.methodicsFrameController;
import java.io.IOException;
import java.util.TreeSet;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController.AddEditFrameController;
import org.taipit.stend.helper.exeptions.InfoExсeption;
import org.taipit.stend.model.Methodic;
import org.taipit.stend.model.MethodicsForTest;

public class MethodicNameController {

    private AddEditFrameController addEditFrameController;

    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    private MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private Methodic methodic;

    private String name;

    //Это окно вызнано из кнопки копирования?
    private boolean clone;

    //Это окно вызвано из окна "Добавить"
    private boolean add;

    @FXML
    private TextField nameField;

    @FXML
    private Button acceptNameBtn;

    @FXML
    private Label labelInfo;

    @FXML
    void initialize() {
        nameField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                acceptNameBtn.fire();
            }
        });
    }

    public String getName() {
        return name;
    }

    @FXML
    void actinonForNameFrame(ActionEvent event) {
        if (event.getSource() == acceptNameBtn) {
            //Если нажата кнопка "Копировать"
            if (clone) {
                name = nameField.getText();
                MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

                try {
                    methodicsForTest.addMethodicToList(name, (Methodic) methodic.clone());

                    methodicsAddEditDeleteFrameController.refreshMethodicList();

                    Stage stage = (Stage) nameField.getScene().getWindow();
                    stage.close();

                    clone = false;

                }catch (InfoExсeption | CloneNotSupportedException e) {
                    labelInfo.setText("Методика с таким именем уже существует");
                }

            //Если нажата кнопка "Добавить"
            } else if (add){
                try {
                    name = nameField.getText();

                    methodicsForTest.addMethodicToList(name, new Methodic());

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/addEditMet.fxml"));
                    fxmlLoader.load();
                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setTitle("Добавление методики");
                    stage.setScene(new Scene(root));

                    addEditFrameController = fxmlLoader.getController();

                    addEditFrameController.setMethodicNameController(this);
                    addEditFrameController.setMethodicsAddEditDeleteFrameController(methodicsAddEditDeleteFrameController);
                    addEditFrameController.setMethodic(methodicsForTest.getMethodic(name));
                    addEditFrameController.setTextFielMethodicName();

                    stage.show();

                    addEditFrameController.bindScrollPanesCurrentAndPowerFactorToMainScrollPane();
                    Stage methodicsAddEditDeleteFrameControllerStage = (Stage) methodicsAddEditDeleteFrameController.getEditMetBtn().getScene().getWindow();
                    methodicsAddEditDeleteFrameControllerStage.hide();

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            event.consume();
                            stage.close();
                            methodicsAddEditDeleteFrameController.refreshMethodicList();
                            methodicsAddEditDeleteFrameControllerStage.show();
                        }
                    });

                    add = false;
                    Stage stageMetodicName = (Stage) nameField.getScene().getWindow();
                    stageMetodicName.close();

                } catch (InfoExсeption e) {
                    labelInfo.setText("Методика с таким именем уже существует");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setMethodicsAddEditDeleteFrameController(MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController) {
        this.methodicsAddEditDeleteFrameController = methodicsAddEditDeleteFrameController;
    }

    public void setClone(boolean clone) {
        this.clone = clone;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public MethodicNameController getMethodicNameController() {
        return this;
    }

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
    }
}