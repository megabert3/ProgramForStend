package org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;
import org.taipit.stend.model.metodics.Metodic;

import java.util.Properties;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна "parametersMethodicFrame.fxml".
 *
 * Данный класс отвечает за возможность фиксирования параметров при которых будут проходить испытания счётчиков на этапе создания методики поверки.
 * Фиксирование параметров не даёт оператору возможность выбора параметров при выборе методики, что предотвращает совершение ошибки.
 */
public class ParametersMethodicController {

    //Ссылка на объект методики для трёхфазного стенда (из объекта методики для какого стенда вызвано окно фиксирования параметров)
    private AddEditPointsThreePhaseStendFrameController addEditPointsThreePhaseStendFrameController;

    //Ссылка на объект методики для однофазного стенда (из объекта методики для какого стенда вызвано окно фиксирования параметров)
    private AddEditPointsOnePhaseStendFrameController addEditPointsOnePhaseStendFrameController;

    //Ссылка на методику для фиксирования в ней параметров теста
    private Metodic methodicForStend;

    private Properties properties = ConsoleHelper.properties;

    //Для инициализации комбобоксов созданными ранее параметрами
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
    private CheckBox circutChkBx;

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

    /**
     * Действие при нажатии на кнопку Установить параметры
     * @param event
     */
    @FXML
    void setParametersAction(ActionEvent event) {

        //Если пользователь нажал Установить параметры
        if (event.getSource() == tglBtnSetPatemeters) {

            //Если нажал установить = true
            if (tglBtnSetPatemeters.isSelected()) {

                //Отключаю возможность редактирования параметров
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

                //Если это окно вызвано из окна создания методики для трехфазного стенда
                if (addEditPointsThreePhaseStendFrameController != null) {

                    //Меняю состояния флага у методики
                    addEditPointsThreePhaseStendFrameController.setBindParameters(true);

                    //Устанавливаю состояние кнопки при вызове этого окна
                    addEditPointsThreePhaseStendFrameController.getParametersBtn().setSelected(true);

                    //Если это окно вызвано из окна создания методики для однофазного стенда
                } else {

                    //Меняю состояния флага у методики
                    addEditPointsOnePhaseStendFrameController.setBindParameters(true);

                    //Устанавливаю состояние кнопки при вызове этого окна
                    addEditPointsOnePhaseStendFrameController.getParametersBtn().setSelected(true);
                }

                //Устанавливаю флаг у методики для испытаний в которую добавляются точки испытания
                methodicForStend.setBindsParameters(true);

                //Передаю параметры выбранные пользователем в создаваемую методику поверки
                methodicForStend.setUnom(txtFldUnom.getText());
                methodicForStend.setImaxAndInom(txtFldCurrent.getText());
                methodicForStend.setFnom(txtFldFrg.getText());
                methodicForStend.setAccuracyClassMeterAP(txtFldAccuracyAP.getText());
                methodicForStend.setAccuracyClassMeterRP(txtFldAccuracyRP.getText());
                methodicForStend.setTypeMeter(chosBxTypeMeter.getValue());
                methodicForStend.setTypeOfMeasuringElementShunt(chosBxtypeOfMeasuringElement.getValue());
                methodicForStend.setConstantAP(txtFldConstantMeterAP.getText());
                methodicForStend.setConstantRP(txtFldConstantMeterRP.getText());
                methodicForStend.setFactoryManufactuter(txtFldMeterManufacturer.getText());
                methodicForStend.setMeterModel(txtFldMeterModel.getText());

                //Параметр применимый только для однофазного стенда
                if (methodicForStend instanceof MethodicForOnePhaseStend) {

                    ((MethodicForOnePhaseStend) methodicForStend).setTwoCircut(circutChkBx.isSelected());
                    circutChkBx.setDisable(true);
                }

                //Если отжал кнопку Установить параметры
            } else {

                //Даю возможность редактировать параметры
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

                //Меняю состояния флагов у окон и методики
                if (addEditPointsThreePhaseStendFrameController != null) {
                    addEditPointsThreePhaseStendFrameController.setBindParameters(false);
                    addEditPointsThreePhaseStendFrameController.getParametersBtn().setSelected(false);
                } else {
                    addEditPointsOnePhaseStendFrameController.setBindParameters(false);
                    addEditPointsOnePhaseStendFrameController.getParametersBtn().setSelected(false);
                }

                methodicForStend.setBindsParameters(false);

                if (methodicForStend instanceof MethodicForOnePhaseStend) {
                    circutChkBx.setDisable(false);
                }
            }
        }
    }


    @FXML
    void initialize() {

        //Добавляю информацию о возможном выборе класса точности активной энергии счётчика
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

        //Добавляю информацию о возможном выборе класса точности реактивной энергии счётчика
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

        //Добавляю информацию о возможном выборе тока счётчика
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

        //Добавляю информацию о возможном выборе типа счётчика
        if (typeMeter.length != 0) {
            chosBxTypeMeter.setItems(FXCollections.observableArrayList(typeMeter));
            chosBxTypeMeter.setValue(chosBxTypeMeter.getItems().get(0));
        }

        //Добавляю информацию о возможном выборе частоты сети счётчика
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

        //Добавляю информацию о возможном выборе постоянной (константы) счётчика для активной энергии
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

        //Добавляю информацию о возможном выборе постоянной (константы) счётчика для реактивной энергии
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

        //Добавляю информацию о возможном выборе завода изготовителя счётчика
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

        //Добавляю информацию о возможном выборе напряжения счётчика
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

        //Добавляю информацию о возможном выборе типа измерительного элемента счётчика
        chosBxtypeOfMeasuringElement.setItems(FXCollections.observableArrayList(typeOfMeasuringElement));
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

        //Действие при выборе количества цепей у однофазного стенда
        circutChkBx.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    ((MethodicForOnePhaseStend) methodicForStend).setTwoCircut(true);
                } else {
                    ((MethodicForOnePhaseStend) methodicForStend).setTwoCircut(false);
                }
            }
        });
    }

    /**
     * Инициализирует все поля параметрами, которые уже были в методике (нажата кнопка редактировать)
     */
    public void setDisableAllParam() {

        //Устанавливаю напряжение
        txtFldUnom.setText(methodicForStend.getUnom());
        chosBxUnom.setDisable(true);
        txtFldUnom.setDisable(true);

        //Класс точности AP
        txtFldAccuracyAP.setText(methodicForStend.getAccuracyClassMeterAP());
        chosBxAccuracyAP.setDisable(true);
        txtFldAccuracyAP.setDisable(true);

        //Класс точности RP
        txtFldAccuracyRP.setText(methodicForStend.getAccuracyClassMeterRP());
        chosBxAccuracyRP.setDisable(true);
        txtFldAccuracyRP.setDisable(true);

        //Ток
        txtFldCurrent.setText(methodicForStend.getImaxAndInom());
        txtFldCurrent.setDisable(true);
        chosBxCurrent.setDisable(true);

        //Тип измерительного элемента
        chosBxtypeOfMeasuringElement.setValue(methodicForStend.getTypeOfMeasuringElementShunt());
        chosBxtypeOfMeasuringElement.setDisable(true);

        //Частота
        txtFldFrg.setText(methodicForStend.getFnom());
        txtFldFrg.setDisable(true);
        chosBxFrg.setDisable(true);

        //Тип счётчика
        chosBxTypeMeter.setValue(methodicForStend.getTypeMeter());
        chosBxTypeMeter.setDisable(true);

        //Постоянная счётчика AP
        txtFldConstantMeterAP.setText(methodicForStend.getConstantAP());
        txtFldConstantMeterAP.setDisable(true);
        chosBxConstantMeterAP.setDisable(true);

        //Постоянная счётчика RP
        txtFldConstantMeterRP.setText(methodicForStend.getConstantRP());
        txtFldConstantMeterRP.setDisable(true);
        chosBxConstantMeterRP.setDisable(true);

        //Завод изготовитель
        txtFldMeterManufacturer.setText(methodicForStend.getFactoryManufactuter());
        txtFldMeterManufacturer.setDisable(true);
        chosBxMeterManufacturer.setDisable(true);


        txtFldMeterModel.setText(methodicForStend.getMeterModel());
        txtFldMeterModel.setDisable(true);
        chosBxMeterModel.setDisable(true);

        tglBtnSetPatemeters.setSelected(true);

        if (methodicForStend instanceof MethodicForOnePhaseStend) {
            circutChkBx.setSelected(((MethodicForOnePhaseStend) methodicForStend).isTwoCircut());
            circutChkBx.setDisable(true);
        }
    }

    public void setAddEditPointsThreePhaseStendFrameController(AddEditPointsThreePhaseStendFrameController addEditPointsThreePhaseStendFrameController) {
        this.addEditPointsThreePhaseStendFrameController = addEditPointsThreePhaseStendFrameController;
    }

    public void setAddEditPointsOnePhaseStendFrameController(AddEditPointsOnePhaseStendFrameController addEditPointsOnePhaseStendFrameController) {
        this.addEditPointsOnePhaseStendFrameController = addEditPointsOnePhaseStendFrameController;
    }

    public void setMethodicForStend(Metodic methodicForStend) {
        this.methodicForStend = methodicForStend;
    }

    public CheckBox getCircutChkBx() {
        return circutChkBx;
    }
}