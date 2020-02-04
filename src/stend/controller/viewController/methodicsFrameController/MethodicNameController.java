package stend.controller.viewController.methodicsFrameController;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import stend.controller.viewController.methodicsFrameController.addEditFraneController.AddEditFrameController;
import stend.helper.exeptions.InfoExсeption;
import stend.model.Methodic;
import stend.model.MethodicsForTest;

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

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
    }

    @FXML
    private TextField nameField;

    @FXML
    private Button acceptNameBtn;

    @FXML
    private Label labelInfo;

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

    @FXML
    void initialize() {
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
                    fxmlLoader.setLocation(getClass().getResource("/stend/view/method/addEditMet.fxml"));
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
}