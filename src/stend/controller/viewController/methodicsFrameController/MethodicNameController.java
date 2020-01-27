package stend.controller.viewController.methodicsFrameController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import stend.helper.exeptions.InfoExeption;
import stend.model.Methodic;
import stend.model.MethodicsForTest;

public class MethodicNameController {

    private AddEditFrameController addEditFrameController;

    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    private Methodic methodic;

    private String name;

    //Это окно вызнано из кнопки копирования?
    private boolean clone;

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

                }catch (InfoExeption | CloneNotSupportedException e) {
                    labelInfo.setText("Методика с таким именем уже существует");
                }

            } else {
                //Если нажата кнопка "Добавить"
                try {
                    name = nameField.getText();

                    for (Methodic methodic : MethodicsForTest.getMethodicsForTestInstance().getMethodics()) {
                        if (methodic.getMethodicName().equals(name)) throw new InfoExeption();
                    }

                    loadStage("/stend/view/method/addEditMet.fxml", "Добавление методики");
                    Stage stage = (Stage) nameField.getScene().getWindow();
                    stage.close();
                } catch (InfoExeption e) {
                    labelInfo.setText("Методика с таким именем уже существует");
                }
            }
        }
    }

    private void loadStage(String fxml, String stageName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxml));
            fxmlLoader.load();
            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle(stageName);
            stage.setScene(new Scene(root));

            addEditFrameController = fxmlLoader.getController();
            addEditFrameController.setMethodicNameController(this);
            addEditFrameController.setMethodicsAddEditDeleteFrameController(methodicsAddEditDeleteFrameController);
            addEditFrameController.setTextFielMethodicName();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}