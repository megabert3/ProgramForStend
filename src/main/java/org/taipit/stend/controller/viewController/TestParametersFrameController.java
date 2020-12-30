package org.taipit.stend.controller.viewController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.model.stend.OnePhaseStend;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.Frame;
import org.taipit.stend.helper.frameManager.FrameManager;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;
import org.taipit.stend.model.metodics.Metodic;
import org.taipit.stend.model.metodics.MetodicsForTest;


/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна старта теста "testParametersFrame.fxml".
 *
 * Данный класс отвечает за выставление количества счётчиков для испытания, а так же настраиваемые параметры испытаний
 */
public class TestParametersFrameController implements Frame {

    //Список доступных методик
    private MetodicsForTest metodicsForTest = MetodicsForTest.getMetodicsForTestInstance();

    //Выбранная методика для испытаний
    private Metodic methodicForStend;

    //Стенд для испытаний
    private StendDLLCommands stendDLLCommands;

    //Настройки
    private Properties properties = ConsoleHelper.properties;

    //Количество мест для испытаний у стенда
    private List<Meter> metersList = new ArrayList<>(Integer.parseInt(properties.getProperty("stendAmountPlaces")));

    //Счётчики для испытаний
    private ObservableList<Meter> meterObservableList;

    //Файл с серийными номерами счётчиков
    private File fileMetersNo = new File(properties.getProperty("testParamFrame.fileForSerNo"));

    //Стенд имеет
    private boolean twoCircut;

    @FXML
    private CheckBox checkBoxOnOffMeterChBox;

    @FXML
    private Pane checkBoxPaneOnOff;

    @FXML
    private CheckBox twoCircutChcBox;

    @FXML
    private ComboBox<String> chosBxTypeMeter;

    @FXML
    private ComboBox<String> chosBxMetodics;

    @FXML
    private TableView<Meter> tabVParamMeters;

    @FXML
    private TableColumn<Meter, Boolean> tabColMeterDis;

    @FXML
    private TableColumn<Meter, Integer> tabColIdMeter;

    @FXML
    private TableColumn<Meter, String> tabColSerNoMeter;

    @FXML
    private TableColumn<Meter, String> tabColConstAPMeter;

    @FXML
    private TableColumn<Meter, String> tabColConstRPMeter;

    @FXML
    private TableColumn<Meter, String> tabColModelMeter;

    @FXML
    private TableColumn<Meter, String> tabColManufacturer;

    @FXML
    private Button btnNumbersMe;

    @FXML
    private TextField txtFldUnom;

    @FXML
    private TextField txtFldAccuracyAP;

    @FXML
    private TextField txtFldCurrent;

    @FXML
    private TextField txtFldFrg;

    @FXML
    private TextField txtFldAccuracyRP;

    @FXML
    private Button btnStartTest;

    @FXML
    private ComboBox<String> chosBxAccuracyAP;

    @FXML
    private ComboBox<String> chosBxUnom;

    @FXML
    private ComboBox<String> chosBxAccuracyRP;

    @FXML
    private ComboBox<String> chosBxCurrent;

    @FXML
    private ComboBox<String> chosBxtypeOfMeasuringElement;

    @FXML
    private ComboBox<String> chosBxPowerType;

    @FXML
    private ComboBox<String> chosBxFrg;

    @FXML
    void buttonActionTestFrame(ActionEvent event) {
        if (event.getSource() == btnStartTest) {

            //Проверяю не запущен ли уже тест
            TestErrorTableFrameController testErrorTableFrame = FrameManager.frameManagerInstance().getTestErrorTableFrameController();

            if (testErrorTableFrame != null) {
                testErrorTableFrame.getTestErrorTableFrameStage().toFront();
                testErrorTableFrame.getTestErrorTableFrameStage().setIconified(false);
                ConsoleHelper.infoException("Тест уже запущен");
                return;
            }

            //Напряжение
            float Un;
            //Базовый ток
            float Ib;
            //Максимальный ток
            float Imax;
            //Частота
            float Fn;
            //Класс точности AP
            float accuracyClassAP;
            //Класс точности RP
            float accuracyClassRP;

            txtFldUnom.setStyle("");
            txtFldCurrent.setStyle("");
            txtFldFrg.setStyle("");
            txtFldAccuracyAP.setStyle("");
            txtFldAccuracyRP.setStyle("");

            if (chosBxMetodics.getValue() == null) {
                ConsoleHelper.infoException("Выберите методику");
                return;
            }

            methodicForStend = metodicsForTest.getMetodic(chosBxMetodics.getValue());

            //Проверка на корректность введённых значений пользователем
            try {
                Un = Float.parseFloat(txtFldUnom.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ConsoleHelper.infoException("Недопустимое значение параметра");
                txtFldUnom.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
                return;
            }

            try {
                Fn = Float.parseFloat(txtFldFrg.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ConsoleHelper.infoException("Недопустимое значение параметра");
                txtFldFrg.setStyle("-fx-text-box-border: red;  -fx-focus-color: red;");
                return;
            }

            try {
                accuracyClassAP = Float.parseFloat(txtFldAccuracyAP.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ConsoleHelper.infoException("Недопустимое значение параметра");
                txtFldAccuracyAP.setStyle("-fx-text-box-border: red;  -fx-focus-color: red;");
                return;
            }

            try {
                accuracyClassRP = Float.parseFloat(txtFldAccuracyRP.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                ConsoleHelper.infoException("Недопустимое значение параметра");
                txtFldAccuracyRP.setStyle("-fx-text-box-border: red;  -fx-focus-color: red;");
                return;
            }

            try {
                String[] ibImaxArr = txtFldCurrent.getText().split("\\(");
                Ib = Float.parseFloat(ibImaxArr[0].trim());
                Imax = Float.parseFloat(ibImaxArr[1].substring(0, ibImaxArr[1].length() - 1));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                ConsoleHelper.infoException("Недопустимое значение параметра");
                txtFldCurrent.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
                return;
            }

            try {
                Integer.parseInt(metersList.get(0).getConstantMeterAP());
            } catch (NumberFormatException e) {
                ConsoleHelper.infoException("Недопустимое значение\nПостоянной счётчика А.Э.");
                return;
            }

            try {
                Integer.parseInt(metersList.get(0).getConstantMeterRP());
            } catch (NumberFormatException e) {
                ConsoleHelper.infoException("Недопустимое значение\nПостоянной счётчика Р.Э");
                return;
            }

            //Тип измерительного элемета
            boolean typeOfMeasuringElementShunt = false;

            if (chosBxtypeOfMeasuringElement.getValue().equals("Шунт")) {
                typeOfMeasuringElementShunt = true;
            }

            //Тип сети
            boolean typeCircuitThreePhase = false;
            if (chosBxPowerType.getValue().equals("3P4W")) {
                typeCircuitThreePhase = true;
            }

            //Проверяю есть ли хоть один выбранный счётчик
            boolean selectMeter = false;
            for (Meter meter : metersList) {
                if (meter.isActiveSeat()) {
                    selectMeter = true;
                    break;
                }
            }

            if (!selectMeter) {
                ConsoleHelper.infoException("Должен быть выбран хотя бы\nодин счётчик");
                return;
            }

            //Оставляем только выделенные счётчики
            for (int i = 0; i < metersList.size(); i++) {
                if (!metersList.get(i).isActiveSeat()) {
                    metersList.remove(i);
                    i--;
                }
            }

            //Передаю выбранные пользователем параметры всем счётчикам
            for (Meter meter : metersList) {
                meter.setIb(Ib);
                meter.setImax(Imax);
                meter.setFn(Fn);
                meter.setUn(Un);
                meter.setTypeCircuitThreePhase(typeCircuitThreePhase);
                meter.setTypeOfMeasuringElementShunt(typeOfMeasuringElementShunt);
                meter.setAccuracyClassAP(accuracyClassAP);
                meter.setAccuracyClassRP(accuracyClassRP);
                meter.setInomImax(txtFldCurrent.getText());
                meter.setTypeMeter(chosBxTypeMeter.getValue());
                meter.setTestMode(chosBxPowerType.getValue());

                meter.setMetodic(methodicForStend);
            }

            //Сохраняю последние параметры в файл для дальнейшего выставления их при следующем запуске теста
            //Для трехфазного стенда
            if (stendDLLCommands instanceof ThreePhaseStend) {
                properties.setProperty("threePhaseStand.lastUnom", txtFldUnom.getText());
                properties.setProperty("threePhaseStand.lastAccuracyClassMeterAP", txtFldAccuracyAP.getText());
                properties.setProperty("threePhaseStand.lastAccuracyClassMeterRP", txtFldAccuracyRP.getText());
                properties.setProperty("threePhaseStand.lastInomAndImax", txtFldCurrent.getText());
                properties.setProperty("threePhaseStand.lastTypeOfMeasuringElement", chosBxtypeOfMeasuringElement.getValue());
                properties.setProperty("threePhaseStand.lastTypeCircuit", chosBxPowerType.getValue());
                properties.setProperty("threePhaseStand.lastFnom", txtFldFrg.getText());
                properties.setProperty("threePhaseStand.lastMeterManufacturer", metersList.get(0).getFactoryManufacturer());
                properties.setProperty("threePhaseStand.lastMeterConstantAP", metersList.get(0).getConstantMeterAP());
                properties.setProperty("threePhaseStand.lastMeterConstantRP", metersList.get(0).getConstantMeterRP());
                properties.setProperty("threePhaseStand.lastMethodicName", chosBxMetodics.getValue());
                properties.setProperty("threePhaseStand.lastMeterTypeOnePhaseMultiTarif", chosBxTypeMeter.getValue());

                //Для однофазного стенда
            } else {
                properties.setProperty("onePhaseStand.lastUnom", txtFldUnom.getText());
                properties.setProperty("onePhaseStand.lastAccuracyClassMeterAP", txtFldAccuracyAP.getText());
                properties.setProperty("onePhaseStand.lastAccuracyClassMeterRP", txtFldAccuracyRP.getText());
                properties.setProperty("onePhaseStand.lastInomAndImax", txtFldCurrent.getText());
                properties.setProperty("onePhaseStand.lastTypeOfMeasuringElement", chosBxtypeOfMeasuringElement.getValue());
                properties.setProperty("onePhaseStand.lastTypeCircuit", chosBxPowerType.getValue());
                properties.setProperty("onePhaseStand.lastFnom", txtFldFrg.getText());
                properties.setProperty("onePhaseStand.lastMeterManufacturer", metersList.get(0).getFactoryManufacturer());
                properties.setProperty("onePhaseStand.lastMeterConstantAP", metersList.get(0).getConstantMeterAP());
                properties.setProperty("onePhaseStand.lastMeterConstantRP", metersList.get(0).getConstantMeterRP());
                properties.setProperty("onePhaseStand.lastMetodicName", chosBxMetodics.getValue());
                properties.setProperty("onePhaseStand.lastMeterTypeOnePhaseMultiTarif", chosBxTypeMeter.getValue());

                if (twoCircut) {
                    properties.setProperty("onePhaseStand.lastTwoCircut", "T");
                }else {
                    properties.setProperty("onePhaseStand.lastTwoCircut", "F");
                }
            }

            ConsoleHelper.saveProperties();

            //Загрузка окна испытания
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/testErrorTableFrame.fxml"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
                ConsoleHelper.infoException(e.getMessage() +"\n" +
                        "Не найден файл загрузки окна\nПопробуйте ещё раз или проверьте целостность\n файлов программы");
            }

            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Поверка счётчиков");
            stage.setScene(new Scene(root));

            TestErrorTableFrameController testErrorTableFrameController = fxmlLoader.getController();

            //Установка и передача параметров в окно теста
            testErrorTableFrameController.setStendDLLCommands(stendDLLCommands);
            testErrorTableFrameController.setMethodicForStend(methodicForStend);
            testErrorTableFrameController.setUn(Un);
            testErrorTableFrameController.setAccuracyClassAP(accuracyClassAP);
            testErrorTableFrameController.setAccuracyClassRP(accuracyClassRP);
            testErrorTableFrameController.setFn(Fn);
            testErrorTableFrameController.setIb(Ib);
            testErrorTableFrameController.setImax(Imax);
            testErrorTableFrameController.setTypeOfMeasuringElementShunt(typeOfMeasuringElementShunt);
            testErrorTableFrameController.setTwoCircut(twoCircut);

            //Установка информации в окне теста
            testErrorTableFrameController.getTxtLabUn().setText("Uн = " + Un + " В");
            testErrorTableFrameController.getTxtLabInom().setText("Iн = " + Ib + " А");
            testErrorTableFrameController.getTxtLabImax().setText("Iмакc = " + Imax + " А");
            testErrorTableFrameController.getTxtLabFn().setText("Fн = " + Fn + " Гц");
            testErrorTableFrameController.getTxtLabTypeCircuit().setText("Тип сети: " + chosBxPowerType.getValue());
            testErrorTableFrameController.getTxtLabAccuracyСlass().setText("Класс точности: " + txtFldAccuracyAP.getText() + "/" + txtFldAccuracyRP.getText() + " Акт/Реак");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            testErrorTableFrameController.getTxtLabDate().setText("Дата: " + simpleDateFormat.format(new Date()));

            testErrorTableFrameController.setListMetersForTest(metersList);

            testErrorTableFrameController.myInitTestErrorTableFrame();

            stage.show();

            testErrorTableFrameController.initScrolBars();

            Stage stage1 = (Stage) btnStartTest.getScene().getWindow();
            stage1.close();

            //Устанавливаю в менеджере окон, что окно теста открыто
            FrameManager.frameManagerInstance().setTestErrorTableFrameController(testErrorTableFrameController);

            //Удаляю из менеджера окон, что окно параметров теста открыто
            FrameManager.frameManagerInstance().testParametersFrameController = null;


            //TEST УДАЛИТЬ
            //testErrorTableFrameController.createRandomResults();
        }

        //Действие при нажатии на кнопку "серийные номера"
        if (event.getSource() == btnNumbersMe) {

            if (fileMetersNo == null || !fileMetersNo.exists()) {

                FileChooser fileChooser = new FileChooser();

                fileChooser.setTitle("Выбор файла");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Все файлы", "*.*"),
                        new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt", "*.doc")
                );

                fileMetersNo = fileChooser.showOpenDialog(btnNumbersMe.getScene().getWindow());

                if (fileMetersNo != null) {
                    ConsoleHelper.properties.setProperty("testParamFrame.fileForSerNo", fileMetersNo.toString());
                    ConsoleHelper.saveProperties();

                    addSerNoMeters();
                }
            } else {
                addSerNoMeters();
            }
        }
    }

    @FXML
    void initialize() {
        if (properties.getProperty("stendType").equals("ThreePhaseStend")) {
            stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();

            initTableView();
            initComboBoxesAndAddListerners();

            twoCircutChcBox.setVisible(false);
            initForThreePhaseStend();

        } else {
            stendDLLCommands = OnePhaseStend.getOnePhaseStendInstance();

            initTableView();
            initComboBoxesAndAddListerners();

            initForOnePhaseStend();

            twoCircutChcBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    twoCircut = newValue;
                }
            });
        }
    }

    /**
     * Инициализирует поля окна данными для однофазного стенда, которые были выбраны при прошлом запуске теста
     */
    private void initForOnePhaseStend() {

        for (Metodic onePhaseMethodic : metodicsForTest.getMethodicForStends()) {
            if (onePhaseMethodic instanceof MethodicForOnePhaseStend) {
                chosBxMetodics.getItems().add(onePhaseMethodic.getMetodicName());
            }
        }

        String lastSelectedMetodic = properties.getProperty("onePhaseStand.lastMetodicName");

        Metodic metodic = metodicsForTest.getMetodic(lastSelectedMetodic);

        if (metodic != null) {
            chosBxMetodics.setValue(lastSelectedMetodic);

            if (metodic.isBindsParameters()) {
                setMetodicParameters(metodic);
            } else {
                setDefautParametersForOnePhaseStand();
            }
        } else {
            chosBxMetodics.setValue("");
            setDefautParametersForOnePhaseStand();
        }
    }

    /**
     * Инициализирует поля окна данными для трехфазного стенда, которые были выбраны при прошлом запуске теста
     * для трехфазного стенда
     */
    private void initForThreePhaseStend() {

        for (Metodic metodic : metodicsForTest.getMethodicForStends()) {
            chosBxMetodics.getItems().add(metodic.getMetodicName());
        }

        String lastSelectedMetodic = properties.getProperty("threePhaseStand.lastMetodicName");

        Metodic metodic = metodicsForTest.getMetodic(lastSelectedMetodic);

        if (metodic != null) {
            chosBxMetodics.setValue(lastSelectedMetodic);

            if (metodic instanceof MethodicForOnePhaseStend) {
                chosBxPowerType.setValue(properties.getProperty("1P2W"));
            } else {
                chosBxPowerType.setValue(properties.getProperty("3P4W"));
            }

            if (metodic.isBindsParameters()) {
                setMetodicParameters(metodic);
            } else {
                setDefautParametersForThreePhaseStand();
            }
        } else {
            chosBxMetodics.setValue("");
            setDefautParametersForThreePhaseStand();
        }
    }

    /**
     * Инициализирует поля окна данными для трехфазного стенда, которые были выбраны при прошлом запуске теста
     * для трехфазного стенда
     */
    private void setDefautParametersForThreePhaseStand() {
        txtFldUnom.setText(properties.getProperty("threePhaseStand.lastUnom"));
        txtFldCurrent.setText(properties.getProperty("threePhaseStand.lastInomAndImax"));
        txtFldFrg.setText(properties.getProperty("threePhaseStand.lastFnom"));
        txtFldAccuracyAP.setText(properties.getProperty("threePhaseStand.lastAccuracyClassMeterAP"));
        txtFldAccuracyRP.setText(properties.getProperty("threePhaseStand.lastAccuracyClassMeterRP"));
        chosBxTypeMeter.setValue(properties.getProperty("threePhaseStand.lastMeterTypeOnePhaseMultiTarif"));
        chosBxtypeOfMeasuringElement.setValue(properties.getProperty("threePhaseStand.lastTypeOfMeasuringElement"));

        chosBxPowerType.setValue("3P4W");

        for (Meter meter : metersList) {
            meter.setConstantMeterAP(properties.getProperty("threePhaseStand.lastMeterConstantAP"));
            meter.setConstantMeterRP(properties.getProperty("threePhaseStand.lastMeterConstantRP"));
            meter.setModelMeter(properties.getProperty("threePhaseStand.lastMeterModel"));
            meter.setFactoryManufacturer(properties.getProperty("threePhaseStand.lastMeterManufacturer"));
        }

        tabVParamMeters.refresh();

        tabColConstAPMeter.setEditable(true);
        tabColConstRPMeter.setEditable(true);
        tabColModelMeter.setEditable(true);
        tabColManufacturer.setEditable(true);

        txtFldUnom.setDisable(false);
        chosBxUnom.setDisable(false);
        txtFldCurrent.setDisable(false);
        chosBxCurrent.setDisable(false);
        txtFldFrg.setDisable(false);
        chosBxFrg.setDisable(false);
        txtFldAccuracyAP.setDisable(false);
        chosBxAccuracyAP.setDisable(false);
        txtFldAccuracyRP.setDisable(false);
        chosBxAccuracyRP.setDisable(false);
        chosBxTypeMeter.setDisable(false);
        chosBxtypeOfMeasuringElement.setDisable(false);
        twoCircutChcBox.setDisable(false);
    }

    /**
     * Инициализирует поля окна данными для трехфазного стенда, которые были выбраны при прошлом запуске теста
     * для однофазного стенда
     */
    private void setDefautParametersForOnePhaseStand() {
        txtFldUnom.setText(properties.getProperty("onePhaseStand.lastUnom"));
        txtFldCurrent.setText(properties.getProperty("onePhaseStand.lastInomAndImax"));
        txtFldFrg.setText(properties.getProperty("onePhaseStand.lastFnom"));
        txtFldAccuracyAP.setText(properties.getProperty("onePhaseStand.lastAccuracyClassMeterAP"));
        txtFldAccuracyRP.setText(properties.getProperty("onePhaseStand.lastAccuracyClassMeterRP"));
        chosBxTypeMeter.setValue(properties.getProperty("onePhaseStand.lastMeterTypeOnePhaseMultiTarif"));
        chosBxtypeOfMeasuringElement.setValue(properties.getProperty("onePhaseStand.lastTypeOfMeasuringElement"));
        chosBxPowerType.setValue(properties.getProperty("onePhaseStand.lastTypeCircuit"));

        if (properties.getProperty("onePhaseStand.lastTwoCircut").equals("T")) {
            twoCircutChcBox.setSelected(true);
        } else {
            twoCircutChcBox.setSelected(false);
        }

        for (Meter meter : metersList) {
            meter.setConstantMeterAP(properties.getProperty("onePhaseStand.lastMeterConstantAP"));
            meter.setConstantMeterRP(properties.getProperty("onePhaseStand.lastMeterConstantRP"));
            meter.setModelMeter(properties.getProperty("onePhaseStand.lastMeterModel"));
            meter.setFactoryManufacturer(properties.getProperty("onePhaseStand.lastMeterManufacturer"));
        }

        tabVParamMeters.refresh();

        tabColConstAPMeter.setEditable(true);
        tabColConstRPMeter.setEditable(true);
        tabColModelMeter.setEditable(true);
        tabColManufacturer.setEditable(true);

        twoCircutChcBox.setDisable(false);

        txtFldUnom.setDisable(false);
        chosBxUnom.setDisable(false);
        txtFldCurrent.setDisable(false);
        chosBxCurrent.setDisable(false);
        txtFldFrg.setDisable(false);
        chosBxFrg.setDisable(false);
        txtFldAccuracyAP.setDisable(false);
        chosBxAccuracyAP.setDisable(false);
        txtFldAccuracyRP.setDisable(false);
        chosBxAccuracyRP.setDisable(false);
        chosBxTypeMeter.setDisable(false);
        chosBxtypeOfMeasuringElement.setDisable(false);
    }

    /**
     * Устанавливает параметры полям окна, которые были выбраны при создании методики поверки
     * @param metodic
     */
    private void setMetodicParameters(Metodic metodic) {
        txtFldUnom.setText(metodic.getUnom());
        txtFldCurrent.setText(metodic.getImaxAndInom());
        txtFldFrg.setText(metodic.getFnom());
        txtFldAccuracyAP.setText(metodic.getAccuracyClassMeterAP());
        txtFldAccuracyRP.setText(metodic.getAccuracyClassMeterRP());
        chosBxTypeMeter.setValue(metodic.getTypeMeter());
        chosBxtypeOfMeasuringElement.setValue(metodic.getTypeOfMeasuringElementShunt());

        if (metodic instanceof MethodicForOnePhaseStend) {
            chosBxPowerType.setValue("1P2W");
        } else {
            chosBxPowerType.setValue("3P4W");
        }

        if (stendDLLCommands instanceof OnePhaseStend) {
            if (metodic instanceof MethodicForOnePhaseStend) {
                twoCircutChcBox.setSelected(((MethodicForOnePhaseStend) metodic).isTwoCircut());
            }
        }

        for (Meter meter : metersList) {
            meter.setConstantMeterAP(metodic.getConstantAP());
            meter.setConstantMeterRP(metodic.getConstantRP());
            meter.setModelMeter(metodic.getMeterModel());
            meter.setFactoryManufacturer(metodic.getFactoryManufactuter());
        }

        tabVParamMeters.refresh();

        tabColConstAPMeter.setEditable(false);
        tabColConstRPMeter.setEditable(false);
        tabColModelMeter.setEditable(false);
        tabColManufacturer.setEditable(false);

        txtFldUnom.setDisable(true);
        chosBxUnom.setDisable(true);
        txtFldCurrent.setDisable(true);
        chosBxCurrent.setDisable(true);
        txtFldFrg.setDisable(true);
        chosBxFrg.setDisable(true);
        txtFldAccuracyAP.setDisable(true);
        chosBxAccuracyAP.setDisable(true);
        txtFldAccuracyRP.setDisable(true);
        chosBxAccuracyRP.setDisable(true);
        chosBxTypeMeter.setDisable(true);
        chosBxtypeOfMeasuringElement.setDisable(true);
        twoCircutChcBox.setDisable(true);
    }

    //Инициирует комбо боксы и добавляет им слушателей
    private void initComboBoxesAndAddListerners() {
        //Переносим данные из проперти файла
        String[] UnomComBox = properties.getProperty("Unom").split(", ");
        String[] FnomComBox = properties.getProperty("Fnom").split(", ");
        String[] InomAndImaxComBox = properties.getProperty("InomAndImax").split(", ");
        String[] accuracyClassMeterAPComBox = properties.getProperty("accuracyClassMeterAP").split(", ");
        String[] accuracyClassMeterRPComBox = properties.getProperty("accuracyClassMeterRP").split(", ");
        String[] typeOfMeasuringElementComBox = properties.getProperty("typeOfMeasuringElement").split(", ");
        String[] typeMeter = properties.getProperty("meterTypeOnePhaseMultiTarif").split(", ");

        //Кладём элементы в КомбоБокс
        chosBxUnom.getItems().addAll(UnomComBox);
        chosBxCurrent.getItems().addAll(InomAndImaxComBox);
        chosBxFrg.getItems().addAll(FnomComBox);
        chosBxAccuracyAP.getItems().addAll(accuracyClassMeterAPComBox);
        chosBxAccuracyRP.getItems().addAll(accuracyClassMeterRPComBox);
        chosBxtypeOfMeasuringElement.getItems().addAll(typeOfMeasuringElementComBox);
        chosBxTypeMeter.getItems().addAll(typeMeter);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            chosBxPowerType.getItems().addAll(properties.getProperty("threePhaseStand.typeCircuit").split(", "));
        } else {
            chosBxPowerType.getItems().addAll(properties.getProperty("onePhaseStand.typeCircuit").split(", "));
        }

        //Устанавливаем слушателей
        chosBxUnom.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldUnom.setText(newValue);
        });
        chosBxCurrent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldCurrent.setText(newValue);
        });
        chosBxFrg.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldFrg.setText(newValue);
        });
        chosBxAccuracyAP.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldAccuracyAP.setText(newValue);
        });
        chosBxAccuracyRP.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldAccuracyRP.setText(newValue);
        });

        checkBoxPaneOnOff.toFront();
        checkBoxOnOffMeterChBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                for (Meter meter : metersList) {
                    meter.setActiveSeat(newValue);
                }
                tabVParamMeters.refresh();
            }
        });

        if (stendDLLCommands instanceof ThreePhaseStend) {
            chosBxMetodics.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    Metodic metodic = metodicsForTest.getMetodic(newValue);

                    if (metodic != null) {
                        if (metodic instanceof MethodicForOnePhaseStend) {
                            chosBxPowerType.getItems().setAll(properties.getProperty("onePhaseStand.typeCircuit").split(", "));

                            if (metodic.isBindsParameters()) {
                                setMetodicParameters(metodic);
                            } else {
                                setDefautParametersForOnePhaseStand();
                            }

                        } else {
                            chosBxPowerType.getItems().setAll(properties.getProperty("threePhaseStand.typeCircuit").split(", "));

                            if (metodic.isBindsParameters()) {
                                setMetodicParameters(metodic);
                            } else {
                                setDefautParametersForThreePhaseStand();
                            }
                        }

                    }
                }
            });
        } else {
            chosBxMetodics.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    Metodic metodic = metodicsForTest.getMetodic(newValue);

                    if (metodic != null) {
                        if (metodic.isBindsParameters()) {
                            setMetodicParameters(metodic);
                        } else {
                            setDefautParametersForOnePhaseStand();
                        }
                    }
                }
            });
        }
    }

    /**
     * инициализирует таблицу с отображением параметров счётчиков, для которых проводится испытание
     */
    private void initTableView() {
        for (int i = 1; i <= Integer.parseInt(properties.getProperty("stendAmountPlaces")); i++) {
            Meter me = new Meter();
            me.setId(i);
            metersList.add(me);
        }

        meterObservableList = FXCollections.observableArrayList(metersList);

        //Установка чек боксов и добавление к ним слушателя
        tabColMeterDis.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Meter, Boolean> param) {
                Meter meter = param.getValue();
                SimpleBooleanProperty simpleBooleanProperty = new SimpleBooleanProperty(meter.isActiveSeat());

                simpleBooleanProperty.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        meter.setActiveSeat(newValue);
                    }
                });

                return simpleBooleanProperty;
            }
        });

        tabColMeterDis.setCellFactory(new Callback<TableColumn<Meter, Boolean>,
                TableCell<Meter, Boolean>>() {
            @Override
            public TableCell<Meter, Boolean> call(TableColumn<Meter, Boolean> p) {
                CheckBoxTableCell<Meter, Boolean> cell = new CheckBoxTableCell<Meter, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        //Отображение айди счётчика
        tabColIdMeter.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabColIdMeter.setStyle("-fx-alignment: CENTER;");

        //Отображение серийного номера счётчика
        tabColSerNoMeter.setCellValueFactory(new PropertyValueFactory<>("serNoMeter"));
        tabColSerNoMeter.setCellFactory(TextFieldTableCell.<Meter>forTableColumn());
        tabColSerNoMeter.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

            TablePosition<Meter, String> pos = event.getTablePosition();

            String newSerNo = event.getNewValue();

            int row = pos.getRow();

            Meter meter = event.getTableView().getItems().get(row);

            meter.setSerNoMeter(newSerNo);
        });
        tabColSerNoMeter.setStyle("-fx-alignment: CENTER;");

        //Отображение константы активной энергии
        tabColConstAPMeter.setCellValueFactory(new PropertyValueFactory<>("constantMeterAP"));
        tabColConstAPMeter.setCellFactory(TextFieldTableCell.<Meter>forTableColumn());
        tabColConstAPMeter.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

            String constAP = event.getNewValue();

            Meter meter;

            for (int i = 0; i < metersList.size(); i++) {
                meter = event.getTableView().getItems().get(i);
                meter.setConstantMeterAP(constAP);
            }
            tabVParamMeters.refresh();
        });
        tabColConstAPMeter.setStyle("-fx-alignment: CENTER;");

        //Отображение константы реактивной энергии
        tabColConstRPMeter.setCellValueFactory(new PropertyValueFactory<>("constantMeterRP"));
        tabColConstRPMeter.setCellFactory(TextFieldTableCell.<Meter>forTableColumn());
        tabColConstRPMeter.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

            String constRP = event.getNewValue();

            Meter meter;

            for (int i = 0; i < metersList.size(); i++) {
                meter = event.getTableView().getItems().get(i);
                meter.setConstantMeterRP(constRP);
            }
            tabVParamMeters.refresh();
        });
        tabColConstRPMeter.setStyle("-fx-alignment: CENTER;");

        //Выбор модели счётчика из списка
        ObservableList<String> meterModelList = FXCollections.observableArrayList(properties.getProperty("meterModels").split(", "));
        tabColModelMeter.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                String modelMeter = meter.getModelMeter();
                return new SimpleObjectProperty<>(modelMeter);
            }

        });

        tabColModelMeter.setCellFactory(ComboBoxTableCell.forTableColumn(meterModelList));

        tabColModelMeter.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

            String value = event.getNewValue();

            for (Meter meter : metersList) {
                meter.setModelMeter(value);
            }
            tabVParamMeters.refresh();
        });
        tabColModelMeter.setStyle("-fx-alignment: CENTER;");

        tabColManufacturer.setCellValueFactory(new PropertyValueFactory<>("factoryManufacturer"));

        tabColManufacturer.setCellFactory(ComboBoxTableCell.forTableColumn(properties.getProperty("meterManufacturer").split(", ")));

        tabColManufacturer.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

            String param = event.getNewValue();

            for (Meter meter : metersList) {
                meter.setFactoryManufacturer(param);
            }
            tabVParamMeters.refresh();
        });
        tabColManufacturer.setStyle("-fx-alignment: CENTER;");

        tabVParamMeters.setEditable(true);

        meterObservableList = FXCollections.observableArrayList(metersList);
        tabVParamMeters.setItems(meterObservableList);

        tabColMeterDis.setSortable(false);
        tabColIdMeter.setSortable(false);
        tabColSerNoMeter.setSortable(false);
        tabColConstAPMeter.setSortable(false);
        tabColConstRPMeter.setSortable(false);
        tabColModelMeter.setSortable(false);
        tabColManufacturer.setSortable(false);

        tabColSerNoMeter.setEditable(true);
        tabColConstAPMeter.setEditable(true);
        tabColConstRPMeter.setEditable(true);
        tabColModelMeter.setEditable(true);
        tabColManufacturer.setEditable(true);

        tabVParamMeters.setPlaceholder(new Label("Укажите количество мест в настройках"));
    }

    /**
     * Добавляет серийные номера к счётчикам из выбранного файла
     */
    private void addSerNoMeters() {

        List<String> listNo = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileMetersNo))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                listNo.add(line);
            }
        } catch (FileNotFoundException ignore) {

        } catch (IOException ignore) {

        }

        try {
            for (int i = 0; i < metersList.size(); i++) {
                metersList.get(i).setSerNoMeter(listNo.get(i));
                metersList.get(i).setActiveSeat(true);
            }
        }catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        tabVParamMeters.refresh();
    }

    @Override
    public Stage getStage() {
        return (Stage) btnStartTest.getScene().getWindow();
    }
}
