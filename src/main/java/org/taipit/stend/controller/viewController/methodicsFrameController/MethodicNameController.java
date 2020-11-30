package org.taipit.stend.controller.viewController.methodicsFrameController;
import java.io.IOException;

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
import org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController.AddEditPointsOnePhaseStendFrameController;
import org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController.AddEditPointsThreePhaseStendFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.InfoExсeption;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;
import org.taipit.stend.model.metodics.MethodicForThreePhaseStend;
import org.taipit.stend.model.metodics.Metodic;
import org.taipit.stend.model.metodics.MetodicsForTest;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна "metodicName.fxml".
 *
 * Данный класс отвечает за присваивание имени создаваемой или клонируемой методике.
 * Проверяет уникальность имени и отвечает за передачу имени в окно добавления точек.
 */
public class MethodicNameController {

    //Окно для добавления точек испытаний для трехфазного теста
    private AddEditPointsThreePhaseStendFrameController addEditPointsThreePhaseStendFrameController;

    //Окно для добавления точек испытаний для однофазного теста
    private AddEditPointsOnePhaseStendFrameController addEditPointsOnePhaseStendFrameController;

    //Окно для отображения уже созданных методик поверки
    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    //Хранилище всех уже созданных методик
    private MetodicsForTest metodicsForTest = MetodicsForTest.getMetodicsForTestInstance();

    //Создаваемая методика
    private Metodic methodicForStend;

    //Имя создаваемой методики
    private String name;

    //Это окно вызнано кнопкой "Копировать"?
    private boolean clone;

    //Это окно вызвано кнопкой "Добавить"?
    private boolean add;

    @FXML
    private TextField nameField;

    @FXML
    private Button acceptNameBtn;

    @FXML
    private Label labelInfo;

    @FXML
    void initialize() {
        //Действие при нажатии Enter
        nameField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                acceptNameBtn.fire();
            }
        });
    }

    @FXML
    void actinonForNameFrame(ActionEvent event) {

        if (event.getSource() == acceptNameBtn) {

            //Если вызвано кнопкой "Копировать"
            if (clone) {

                name = nameField.getText();
                MetodicsForTest metodicsForTest = MetodicsForTest.getMetodicsForTestInstance();

                try {
                    //Проверяю уникальность присвоенного имени и если оно уникально, то добавляю его к остальным
                    metodicsForTest.addMethodicToList(name, (Metodic) methodicForStend.clone());

                    //Обновляю список отображения методик с учётом уже добавленной
                    methodicsAddEditDeleteFrameController.refreshMethodicList();

                    Stage stage = (Stage) nameField.getScene().getWindow();
                    stage.close();

                    clone = false;

                }catch (InfoExсeption | CloneNotSupportedException e) {
                    e.printStackTrace();
                    ConsoleHelper.infoException("Методика с таким именем уже существует");
                }

            //Если нажата кнопка "Добавить"
            } else if (add){
                try {
                    name = nameField.getText().trim();

                    //Проверка на пустое название
                    if (name.isEmpty()) {
                        ConsoleHelper.infoException("Поле \"Название методики\"\nне должно быть пустым");
                        return;
                    }

                    Stage stage = new Stage();

                    //Если стенд трехфазный, то открою окно для добавления точек для трехфазного стенда
                    if (ConsoleHelper.properties.getProperty("stendType").equals("ThreePhaseStend")) {

                        //Добавляю методику к имеющимся
                        metodicsForTest.addMethodicToList(name, new MethodicForThreePhaseStend());

                        //Открываю окно для добавления точек в методику для трехфазного стенда
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/ThreePhase/addEditPointsThreePhaseStendMet.fxml"));
                        fxmlLoader.load();
                        Parent root = fxmlLoader.getRoot();
                        stage.setTitle("Добавление методики");
                        stage.setScene(new Scene(root));

                        addEditPointsThreePhaseStendFrameController = fxmlLoader.getController();
                        addEditPointsThreePhaseStendFrameController.setMethodicNameController(this);
                        addEditPointsThreePhaseStendFrameController.setMethodicsAddEditDeleteFrameController(methodicsAddEditDeleteFrameController);

                        //Присваиваю имя методики в окне
                        addEditPointsThreePhaseStendFrameController.setMethodicForThreePhaseStend((MethodicForThreePhaseStend) metodicsForTest.getMetodic(name));
                        addEditPointsThreePhaseStendFrameController.setTextFielMethodicName();
                        addEditPointsThreePhaseStendFrameController.addListenerToCheckBoxes();

                        stage.show();

                        addEditPointsThreePhaseStendFrameController.bindScrollPanesCurrentAndPowerFactorToMainScrollPane();

                        //Если стенд однофазный, то открою окно для добавления точек для однофазного стенда
                    } else {

                        metodicsForTest.addMethodicToList(name, new MethodicForOnePhaseStend());


                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/OnePhase/addEditPointsOnePhaseStendMet.fxml"));
                        fxmlLoader.load();
                        Parent root = fxmlLoader.getRoot();
                        stage.setTitle("Добавление методики");
                        stage.setScene(new Scene(root));

                        addEditPointsOnePhaseStendFrameController = fxmlLoader.getController();

                        addEditPointsOnePhaseStendFrameController.setMethodicNameController(this);
                        addEditPointsOnePhaseStendFrameController.setMethodicsAddEditDeleteFrameController(methodicsAddEditDeleteFrameController);
                        addEditPointsOnePhaseStendFrameController.setMethodicForOnePhaseStend((MethodicForOnePhaseStend) metodicsForTest.getMetodic(name));
                        addEditPointsOnePhaseStendFrameController.setTextFielMethodicName();
                        addEditPointsOnePhaseStendFrameController.addListenerToCheckBoxes();

                        stage.show();

                        addEditPointsOnePhaseStendFrameController.bindScrollPanesCurrentAndPowerFactorToMainScrollPane();
                    }

                    Stage methodicsAddEditDeleteFrameControllerStage = (Stage) methodicsAddEditDeleteFrameController.getEditMetBtn().getScene().getWindow();
                    methodicsAddEditDeleteFrameControllerStage.hide();

                    //Действие при закрытии окна добавления точек испытания в методику
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
                    Stage stageMetodicName = (Stage) acceptNameBtn.getScene().getWindow();
                    stageMetodicName.close();

                } catch (InfoExсeption e) {
                    ConsoleHelper.infoException("Методика с таким именем уже существует");
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

    public void setMethodicForStend(Metodic methodicForStend) {
        this.methodicForStend = methodicForStend;
    }

    public String getName() {
        return name;
    }
}