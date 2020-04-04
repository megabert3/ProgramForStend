package org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.Methodic;

import java.util.Properties;

public class ParametersMethodicController {

    private AddEditFrameController addEditFrameController;

    private Methodic methodic;

    private Properties properties = ConsoleHelper.properties;

    private String[] ImaxIb = properties.getProperty("InomAndImax").split(", ");
    private String[] F = properties.getProperty("Fnom").split(", ");
    private String[] U = properties.getProperty("Unom").split(", ");
    private String[] accuracyClassMeterAP = properties.getProperty("accuracyClassMeterAP").split(", ");
    private String[] accuracyClassMeterRP = properties.getProperty("accuracyClassMeterRP").split(", ");
    private String[] typeMeter = properties.getProperty("meterTypeOnePhaseMultiTarif").split(", ");
    private String[] typeOfMeasuringElement = properties.getProperty("typeOfMeasuringElement").split(", ");
    private String[] constantMeterAP = properties.getProperty("constantMeterAP").split(", ");
    private String[] constantMeterRP = properties.getProperty("constantMeterRP").split(", ");
    private String[] meterModel = properties.getProperty("meterModels").split(", ");
    private String[] factoryManufacturer = properties.getProperty("meterManufacturer").split(", ");

    @FXML
    private ComboBox<String> chosBxUnom;

    @FXML
    private TextField txtFldUnom;

    @FXML
    private ComboBox<String> chosBxAccuracyAP;

    @FXML
    private TextField txtFldAccuracyAP;

    @FXML
    private ComboBox<String> chosBxAccuracyRP;

    @FXML
    private ComboBox<String> chosBxCurrent;

    @FXML
    private ComboBox<String> chosBxtypeOfMeasuringElement;

    @FXML
    private ComboBox<String> chosBxFrg;

    @FXML
    private TextField txtFldAccuracyRP;

    @FXML
    private TextField txtFldCurrent;

    @FXML
    private TextField txtFldFrg;

    @FXML
    private ComboBox<String> chosBxTypeMeter;

    @FXML
    private ComboBox<String> chosBxConstantMeterAP;

    @FXML
    private TextField txtFldConstantMeterAP;

    @FXML
    private ComboBox<String> chosBxConstantMeterRP;

    @FXML
    private TextField txtFldConstantMeterRP;

    @FXML
    private ComboBox<String> chosBxMeterManufacturer;

    @FXML
    private TextField txtFldMeterManufacturer;

    @FXML
    private ComboBox<String> chosBxMeterModel;

    @FXML
    private TextField txtFldMeterModel;

    @FXML
    private ToggleButton tglBtnSetPatemeters;

    @FXML
    void setParametersAction(ActionEvent event) {
        if (event.getSource() == tglBtnSetPatemeters) {

            if (tglBtnSetPatemeters.isSelected()) {
                chosBxUnom.setDisable(true);
                txtFldUnom.setDisable(true);
                chosBxAccuracyAP.setDisable(true);
                txtFldAccuracyAP.setDisable(true);
                chosBxAccuracyRP.setDisable(true);
                chosBxCurrent.setDisable(true);
                chosBxtypeOfMeasuringElement.setDisable(true);
                chosBxFrg.setDisable(true);
                txtFldAccuracyRP.setDisable(true);
                txtFldCurrent.setDisable(true);
                txtFldFrg.setDisable(true);
                chosBxTypeMeter.setDisable(true);
                chosBxConstantMeterAP.setDisable(true);
                txtFldConstantMeterAP.setDisable(true);
                chosBxConstantMeterRP.setDisable(true);
                txtFldConstantMeterRP.setDisable(true);
                chosBxMeterManufacturer.setDisable(true);
                txtFldMeterManufacturer.setDisable(true);
                chosBxMeterModel.setDisable(true);
                txtFldMeterModel.setDisable(true);

                addEditFrameController.setBindParameters(true);
                addEditFrameController.getParametersBtn().setSelected(true);

                methodic.setBindsParameters(true);

                methodic.setUnom(txtFldUnom.getText());
                methodic.setImaxAndInom(txtFldCurrent.getText());
                methodic.setFnom(txtFldFrg.getText());
                methodic.setAccuracyClassMeterAP(txtFldAccuracyAP.getText());
                methodic.setAccuracyClassMeterRP(txtFldAccuracyRP.getText());
                methodic.setTypeMeter(chosBxTypeMeter.getValue());
                methodic.setTypeOfMeasuringElementShunt(chosBxtypeOfMeasuringElement.getValue());
                methodic.setConstantAP(txtFldConstantMeterAP.getText());
                methodic.setConstantRP(txtFldConstantMeterRP.getText());
                methodic.setFactoryManufactuter(txtFldMeterManufacturer.getText());
                methodic.setMeterModel(txtFldMeterModel.getText());
            } else {
                chosBxUnom.setDisable(false);
                txtFldUnom.setDisable(false);
                chosBxAccuracyAP.setDisable(false);
                txtFldAccuracyAP.setDisable(false);
                chosBxAccuracyRP.setDisable(false);
                chosBxCurrent.setDisable(false);
                chosBxtypeOfMeasuringElement.setDisable(false);
                chosBxFrg.setDisable(false);
                txtFldAccuracyRP.setDisable(false);
                txtFldCurrent.setDisable(false);
                txtFldFrg.setDisable(false);
                chosBxTypeMeter.setDisable(false);
                chosBxConstantMeterAP.setDisable(false);
                txtFldConstantMeterAP.setDisable(false);
                chosBxConstantMeterRP.setDisable(false);
                txtFldConstantMeterRP.setDisable(false);
                chosBxMeterManufacturer.setDisable(false);
                txtFldMeterManufacturer.setDisable(false);
                chosBxMeterModel.setDisable(false);
                txtFldMeterModel.setDisable(false);

                addEditFrameController.setBindParameters(false);
                addEditFrameController.getParametersBtn().setSelected(false);

                methodic.setBindsParameters(false);
            }
        }
    }

    @FXML
    void initialize() {
        if (accuracyClassMeterAP.length != 0) {
            chosBxAccuracyAP.setItems(FXCollections.observableArrayList(accuracyClassMeterAP));
            txtFldAccuracyAP.setText(accuracyClassMeterAP[0]);
            chosBxAccuracyAP.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldAccuracyAP.setText(newValue);
                }
            });
        }

        if (accuracyClassMeterRP.length != 0) {
            chosBxAccuracyRP.setItems(FXCollections.observableArrayList(accuracyClassMeterRP));
            txtFldAccuracyRP.setText(accuracyClassMeterRP[0]);
            chosBxAccuracyRP.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldAccuracyRP.setText(newValue);
                }
            });
        }

        if (ImaxIb.length != 0) {
            chosBxCurrent.setItems(FXCollections.observableArrayList(ImaxIb));
            txtFldCurrent.setText(ImaxIb[0]);
            chosBxCurrent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldCurrent.setText(newValue);
                }
            });
        }


        if (typeMeter.length != 0) {
            chosBxTypeMeter.setItems(FXCollections.observableArrayList(typeMeter));
            chosBxTypeMeter.setValue(chosBxTypeMeter.getItems().get(0));
        }

        if (F.length != 0) {
            chosBxFrg.setItems(FXCollections.observableArrayList(F));
            txtFldFrg.setText(F[0]);
            chosBxFrg.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldFrg.setText(newValue);
                }
            });
        }

        if (constantMeterAP.length != 0) {
            chosBxConstantMeterAP.setItems(FXCollections.observableArrayList(constantMeterAP));
            txtFldConstantMeterAP.setText(constantMeterAP[0]);
            chosBxConstantMeterAP.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldConstantMeterAP.setText(newValue);
                }
            });
        }

        if (constantMeterRP.length != 0) {
            chosBxConstantMeterRP.setItems(FXCollections.observableArrayList(constantMeterRP));
            txtFldConstantMeterRP.setText(constantMeterRP[0]);
            chosBxConstantMeterRP.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldConstantMeterRP.setText(newValue);
                }
            });
        }

        if (factoryManufacturer.length != 0) {
            chosBxMeterManufacturer.setItems(FXCollections.observableArrayList(factoryManufacturer));
            txtFldMeterManufacturer.setText(factoryManufacturer[0]);
            chosBxMeterManufacturer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldMeterManufacturer.setText(newValue);
                }
            });
        }

        if (U.length != 0) {
            chosBxUnom.setItems(FXCollections.observableArrayList(U));
            txtFldUnom.setText(U[0]);
            chosBxUnom.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldUnom.setText(newValue);
                }
            });
        }

        chosBxtypeOfMeasuringElement.setItems(FXCollections.observableArrayList(properties.getProperty("typeOfMeasuringElement").split(", ")));
        chosBxtypeOfMeasuringElement.setValue(chosBxtypeOfMeasuringElement.getItems().get(0));

        if (meterModel.length != 0) {
            chosBxMeterModel.setItems(FXCollections.observableArrayList(meterModel));
            txtFldMeterModel.setText(meterModel[0]);
            chosBxMeterModel.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    txtFldMeterModel.setText(newValue);
                }
            });
        }
    }

    public void setDisableAllParam() {
        txtFldUnom.setText(methodic.getUnom());
        chosBxUnom.setDisable(true);
        txtFldUnom.setDisable(true);

        txtFldAccuracyAP.setText(methodic.getAccuracyClassMeterAP());
        chosBxAccuracyAP.setDisable(true);
        txtFldAccuracyAP.setDisable(true);

        txtFldAccuracyRP.setText(methodic.getAccuracyClassMeterRP());
        chosBxAccuracyRP.setDisable(true);
        txtFldAccuracyRP.setDisable(true);

        txtFldCurrent.setText(methodic.getImaxAndInom());
        txtFldCurrent.setDisable(true);
        chosBxCurrent.setDisable(true);

        chosBxtypeOfMeasuringElement.setValue(methodic.getTypeOfMeasuringElementShunt());
        chosBxtypeOfMeasuringElement.setDisable(true);

        txtFldFrg.setText(methodic.getFnom());
        txtFldFrg.setDisable(true);
        chosBxFrg.setDisable(true);

        chosBxTypeMeter.setValue(methodic.getTypeMeter());
        chosBxTypeMeter.setDisable(true);

        txtFldConstantMeterAP.setText(methodic.getConstantAP());
        txtFldConstantMeterAP.setDisable(true);
        chosBxConstantMeterAP.setDisable(true);

        txtFldConstantMeterRP.setText(methodic.getConstantRP());
        txtFldConstantMeterRP.setDisable(true);
        chosBxConstantMeterRP.setDisable(true);

        txtFldMeterManufacturer.setText(methodic.getFactoryManufactuter());
        txtFldMeterManufacturer.setDisable(true);
        chosBxMeterManufacturer.setDisable(true);

        txtFldMeterModel.setText(methodic.getFactoryManufactuter());
        txtFldMeterModel.setDisable(true);
        chosBxMeterModel.setDisable(true);

        tglBtnSetPatemeters.setSelected(true);
    }

    public void setAddEditFrameController(AddEditFrameController addEditFrameController) {
        this.addEditFrameController = addEditFrameController;
    }

    public void setMethodic(Methodic methodic) {
        this.methodic = methodic;
    }
}