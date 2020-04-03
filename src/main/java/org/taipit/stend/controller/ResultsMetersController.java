package org.taipit.stend.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.controller.viewController.YesOrNoFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.ResultsTest;

public class ResultsMetersController {

    private ResultsTest resultsTest = ResultsTest.getResultsTestInstance();

    private Properties properties = ConsoleHelper.properties;

    private String[] resultMass = properties.getProperty("restMeterResults").split(", ");

    private ObservableList<Meter> observableList = FXCollections.observableArrayList(resultsTest.getListAllResults());

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private Pane paneForTabView;

    @FXML
    private TableView<Meter> tabViewResults;

    @FXML
    private TableColumn<Meter, Integer> tabColPosition;

    @FXML
    private TableColumn<Meter, String> tabColSerNo;

    @FXML
    private TableColumn<Meter, String> tabColMeterModel;

    @FXML
    private TableColumn<Meter, String> tabColResultVerification;

    @FXML
    private TableColumn<Meter, String> tabColDate;

    @FXML
    private TableColumn<Meter, String> tabColCRPResult;

    @FXML
    private TableColumn<Meter, String> tabColStartResult;

    @FXML
    private TableColumn<Meter, String> tabColStartAPPls;

    @FXML
    private TableColumn<Meter, String> tabColStartAPMns;

    @FXML
    private TableColumn<Meter, String> tabColStartRPPls;

    @FXML
    private TableColumn<Meter, String> tabColStartRPMns;

    @FXML
    private TableColumn<Meter, String> tabColRTCResult;

    @FXML
    private TableColumn<Meter, String> tabColConstantResult;

    @FXML
    private TableColumn<Meter, String> tabColConstantAPPls;

    @FXML
    private TableColumn<Meter, String> tabColConstantAPMns;

    @FXML
    private TableColumn<Meter, String> tabColConstantRPPls;

    @FXML
    private TableColumn<Meter, String> tabColConstantRPMns;

    @FXML
    private TableColumn<Meter, String> tabColInsulationResult;

    @FXML
    private TableColumn<Meter, String> tabColApperianceResult;

    @FXML
    private TableColumn<Meter, String> tabColTemperature;

    @FXML
    private TableColumn<Meter, String> tabColHumidity;

    @FXML
    private TableColumn<Meter, String> tabColOperator;

    @FXML
    private TableColumn<Meter, String> tabColController;

    @FXML
    private TableColumn<Meter, String> tabColWitnes;

    @FXML
    private TableColumn<Meter, String> tabColUn;

    @FXML
    private TableColumn<Meter, String> tabColInomImax;

    @FXML
    private TableColumn<Meter, String> tabColFn;

    @FXML
    private TableColumn<Meter, String> tabColConstants;

    @FXML
    private TableColumn<Meter, String> tabColConstantMeterAPPls;

    @FXML
    private TableColumn<Meter, String> tabColConstantMeterRPMns;

    @FXML
    private TableColumn<Meter, String> tabColTypeMeter;

    @FXML
    private TableColumn<Meter, String> tabColTypeCurrentDetector;

    @FXML
    private TableColumn<Meter, String> tabColfactoryManufacturer;

    @FXML
    private TableColumn<Meter, String> tabColBatсhNo;

    @FXML
    void editDeleteAction(ActionEvent event) {
        if (event.getSource() == btnEdit) {
            List<Meter> listSelectedMeters = new ArrayList<>();

            for (Integer index : tabViewResults.getSelectionModel().getSelectedIndices()) {
                listSelectedMeters.add(ResultsTest.getResultsTestInstance().getListAllResults().get(index));
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/editResultsMetersFrame.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            EditResultsMetersController editResultsMetersController = fxmlLoader.getController();
            editResultsMetersController.setResultsMetersController(this);
            editResultsMetersController.setSelectedMetersForEdit(listSelectedMeters);
            editResultsMetersController.setIndexcesList(tabViewResults.getSelectionModel().getSelectedIndices());
            editResultsMetersController.initColumsEditFrame();

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Редактирование результатов");
            stage.setScene(new Scene(root));
            stage.show();
        }

        if (event.getSource() == btnDelete) {
            if (ResultsTest.getResultsTestInstance().getListAllResults().isEmpty()) {
                System.out.println("Нет результата.");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/yesOrNoFrame.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            YesOrNoFrameController yesOrNoFrameController = fxmlLoader.getController();
            yesOrNoFrameController.setResultsMeters(true);
            yesOrNoFrameController.setListIndexces(tabViewResults.getSelectionModel().getSelectedIndices());
            yesOrNoFrameController.getQuestionTxt().setText("Вы уверены, что хотите удалить выбранные записи?");
            yesOrNoFrameController.getQuestionTxt().setLayoutX(165);
            yesOrNoFrameController.getQuestionTxt().setLayoutY(30);

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Удаление результатов");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            tabViewResults.getItems().setAll(FXCollections.observableArrayList(resultsTest.getListAllResults()));
        }
    }

    @FXML
    void initialize() {
        tabColPosition.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabColPosition.setStyle( "-fx-alignment: CENTER;");

        tabColSerNo.setCellValueFactory(new PropertyValueFactory<>("serNoMeter"));
        tabColSerNo.setStyle( "-fx-alignment: CENTER;");

        tabColMeterModel.setCellValueFactory(new PropertyValueFactory<>("modelMeter"));
        tabColMeterModel.setStyle( "-fx-alignment: CENTER;");

        tabColResultVerification.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getFinalAllTestResult() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getFinalAllTestResult()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getFinalAllTestResult()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColResultVerification.setStyle( "-fx-alignment: CENTER;");

        tabColDate.setCellValueFactory(new PropertyValueFactory<>("verificationDate"));
        tabColDate.setStyle( "-fx-alignment: CENTER;");

        tabColCRPResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getCreepTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getCreepTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getCreepTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColCRPResult.setStyle( "-fx-alignment: CENTER;");

        tabColStartAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getStartTestAPPls() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getStartTestAPPls()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getStartTestAPPls()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });

        tabColStartAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getStartTestAPMns() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getStartTestAPMns()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getStartTestAPMns()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColStartAPMns.setStyle( "-fx-alignment: CENTER;");

        tabColStartRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getStartTestRPPls() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getStartTestRPPls()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getStartTestRPPls()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColStartRPPls.setStyle( "-fx-alignment: CENTER;");

        tabColStartRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getStartTestRPMns() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getStartTestRPMns()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getStartTestRPMns()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColStartRPMns.setStyle( "-fx-alignment: CENTER;");

        tabColRTCResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getRTCTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getRTCTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getRTCTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColRTCResult.setStyle( "-fx-alignment: CENTER;");

        tabColConstantAPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getConstantTestAPPls() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getConstantTestAPPls()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getConstantTestAPPls()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColConstantAPPls.setStyle( "-fx-alignment: CENTER;");

        tabColConstantAPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getConstantTestAPMns() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getConstantTestAPMns()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getConstantTestAPMns()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColConstantAPMns.setStyle( "-fx-alignment: CENTER;");

        tabColConstantRPPls.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getConstantTestRPPls() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getConstantTestRPPls()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getConstantTestRPPls()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColConstantRPPls.setStyle( "-fx-alignment: CENTER;");

        tabColConstantRPMns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getConstantTestRPMns() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getConstantTestRPMns()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getConstantTestRPMns()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColConstantRPMns.setStyle( "-fx-alignment: CENTER;");

        tabColInsulationResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getInsulationTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getInsulationTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getInsulationTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColInsulationResult.setStyle( "-fx-alignment: CENTER;");


        tabColApperianceResult.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result = null;

                if (meter.getAppearensTest() == null) {
                    result = new SimpleStringProperty(resultMass[0]);
                } else if (meter.getAppearensTest()) {
                    result = new SimpleStringProperty(resultMass[1]);
                } else if (!meter.getAppearensTest()) {
                    result = new SimpleStringProperty(resultMass[2]);
                }
                return result;
            }
        });
        tabColApperianceResult.setStyle( "-fx-alignment: CENTER;");

        tabColTemperature.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        tabColTemperature.setStyle( "-fx-alignment: CENTER;");

        tabColHumidity.setCellValueFactory(new PropertyValueFactory<>("humidity"));
        tabColHumidity.setStyle( "-fx-alignment: CENTER;");

        tabColOperator.setCellValueFactory(new PropertyValueFactory<>("operator"));
        tabColOperator.setStyle( "-fx-alignment: CENTER;");

        tabColController.setCellValueFactory(new PropertyValueFactory<>("controller"));
        tabColController.setStyle( "-fx-alignment: CENTER;");

        tabColWitnes.setCellValueFactory(new PropertyValueFactory<>("witness"));
        tabColWitnes.setStyle( "-fx-alignment: CENTER;");

        tabColUn.setCellValueFactory(new PropertyValueFactory<>("Un"));
        tabColUn.setStyle( "-fx-alignment: CENTER;");

        tabColInomImax.setCellValueFactory(new PropertyValueFactory<>("InomImax"));
        tabColInomImax.setStyle( "-fx-alignment: CENTER;");

        tabColFn.setCellValueFactory(new PropertyValueFactory<>("Fn"));
        tabColFn.setStyle( "-fx-alignment: CENTER;");

        tabColConstantMeterAPPls.setCellValueFactory(new PropertyValueFactory<>("constantMeterAP"));
        tabColConstantMeterAPPls.setStyle( "-fx-alignment: CENTER;");

        tabColConstantMeterRPMns.setCellValueFactory(new PropertyValueFactory<>("constantMeterRP"));
        tabColConstantMeterRPMns.setStyle( "-fx-alignment: CENTER;");

        tabColTypeMeter.setCellValueFactory(new PropertyValueFactory<>("typeMeter"));
        tabColTypeMeter.setStyle( "-fx-alignment: CENTER;");

        tabColTypeCurrentDetector.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                SimpleStringProperty result;

                if (meter.isTypeOfMeasuringElementShunt()) {
                    result = new SimpleStringProperty("Шунт");
                } else {
                    result = new SimpleStringProperty("Трансформатор");
                }
                return result;
            }
        });
        tabColTypeCurrentDetector.setStyle( "-fx-alignment: CENTER;");

        tabColfactoryManufacturer.setCellValueFactory(new PropertyValueFactory<>("factoryManufacturer"));
        tabColfactoryManufacturer.setStyle( "-fx-alignment: CENTER;");

        tabColBatсhNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        tabColBatсhNo.setStyle( "-fx-alignment: CENTER;");

        tabViewResults.setItems(FXCollections.observableArrayList(observableList));

        tabViewResults.setPrefWidth(paneForTabView.getPrefWidth());
        tabViewResults.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public TableView<Meter> getTabViewResults() {
        return tabViewResults;
    }
}