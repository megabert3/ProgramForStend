package org.taipit.stend.helper.frameManager;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.taipit.stend.controller.ResultsMetersController;
import org.taipit.stend.controller.viewController.PropertiesController;
import org.taipit.stend.controller.viewController.TestParametersFrameController;
import org.taipit.stend.controller.viewController.methodicsFrameController.MethodicsAddEditDeleteFrameController;

import java.io.IOException;

public class FrameManager {

    private static FrameManager frameManager;

    private FrameManager(){
    }

    public static FrameManager frameManagerInstance() {
        if (frameManager == null) {
            frameManager = new FrameManager();
        }
        return frameManager;
    }

    //Окно настроек
    private PropertiesController propertiesController;

    //Окно создания или редактирования методики
    private MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController;

    //Окно сохранённых результатов
    private ResultsMetersController resultsMetersController;

    //Окно настройки и запуска теста
    public TestParametersFrameController testParametersFrameController;

    public enum FrameType {
        PROPERTIES,
        METHODIC,
        RESULTS,
        PARAMTEST
    }

    public void getFrame(FrameType frame) {
        switch (frame) {
            case PROPERTIES: {

                if (propertiesController == null) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/properties.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    propertiesController = fxmlLoader.getController();

                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setTitle("Настройки");
                    stage.setScene(new Scene(root));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            event.consume();
                            propertiesController = null;
                            stage.close();
                        }
                    });

                    stage.show();
                } else {
                    propertiesController.getStage().setIconified(false);
                    propertiesController.getStage().toFront();
                }
            } break;
            case METHODIC: {

                if (methodicsAddEditDeleteFrameController == null) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/methodicsAddEditDeleteFrame.fxml"));
                        fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Методики");
                        stage.setScene(new Scene(fxmlLoader.getRoot()));
                        methodicsAddEditDeleteFrameController = fxmlLoader.getController();

                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                event.consume();
                                methodicsAddEditDeleteFrameController = null;
                                stage.close();
                            }
                        });

                        stage.show();

                        methodicsAddEditDeleteFrameController.addListenerForResizeFrame();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    methodicsAddEditDeleteFrameController.getStage().setIconified(false);
                    methodicsAddEditDeleteFrameController.getStage().toFront();
                }
            }break;

            case RESULTS: {

                if (resultsMetersController == null) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/resultsMetersFrame.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    resultsMetersController = fxmlLoader.getController();

                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setTitle("Результат");
                    stage.setScene(new Scene(root));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            event.consume();
                            resultsMetersController = null;
                            stage.close();
                        }
                    });

                    stage.show();
                } else {
                    resultsMetersController.getStage().setIconified(false);
                    resultsMetersController.getStage().toFront();
                }
            }break;

            case PARAMTEST: {
                if (testParametersFrameController == null) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/viewFXML/testParametersFrame.fxml"));
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    testParametersFrameController = fxmlLoader.getController();

                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setTitle("Параметры теста");
                    stage.setScene(new Scene(root));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            event.consume();
                            testParametersFrameController = null;
                            stage.close();
                        }
                    });

                    stage.show();
                } else {
                    testParametersFrameController.getStage().setIconified(false);
                    testParametersFrameController.getStage().toFront();
                }
            }
        }
    }

    public void setMethodicsAddEditDeleteFrameController(MethodicsAddEditDeleteFrameController methodicsAddEditDeleteFrameController) {
        this.methodicsAddEditDeleteFrameController = methodicsAddEditDeleteFrameController;
    }

    public MethodicsAddEditDeleteFrameController getMethodicsAddEditDeleteFrameController() {
        return methodicsAddEditDeleteFrameController;
    }
}
