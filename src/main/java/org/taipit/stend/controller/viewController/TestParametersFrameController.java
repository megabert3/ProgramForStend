package org.taipit.stend.controller.viewController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.stage.Stage;
import javafx.util.Callback;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.OnePhaseStend;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.InfoExсeption;
import org.taipit.stend.model.Methodic;
import org.taipit.stend.model.MethodicsForTest;

public class TestParametersFrameController {

    private MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private StendDLLCommands stendDLLCommands;

    private Properties properties = ConsoleHelper.properties;

    private List<Meter> metersList = new ArrayList<>(Integer.parseInt(properties.getProperty("stendAmountPlaces")));

    private ObservableList<Meter> meterObservableList;

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
            try {
                if (chosBxMetodics.getValue() == null) throw new InfoExсeption();

                //Номинальное напряжение
                double Un = Double.parseDouble(txtFldUnom.getText());

                //Частота
                double Fn = Double.parseDouble(txtFldFrg.getText());

                //Класс точности AP
                double accuracyClassAP = Double.parseDouble(txtFldAccuracyAP.getText());

                //Класс точности RP
                double accuracyClassRP = Double.parseDouble(txtFldAccuracyRP.getText());

                StringBuilder stringBuilder = new StringBuilder();
                Pattern pat = Pattern.compile("[0-9]+");
                Matcher mat = pat.matcher(txtFldCurrent.getText());

                while (mat.find()) {
                    stringBuilder.append(mat.group()).append(",");
                }

                String[] current = new String(stringBuilder).split(",");

                //Базовый ток
                double Ib = Double.parseDouble(current[0]);

                //Максимальный ток
                double Imax = Double.parseDouble(current[1]);

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

                //Оставляем только выделенные счётчики
                for (int i = 0; i < metersList.size(); i++) {
                    if (!metersList.get(i).isActiveSeat()) {
                        metersList.remove(i);
                        i--;
                    }
                }

                //Передаю установленные параметры всем счётчикам
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
                }

                /**
                *  Сделать проверки полей
                */

                if (metersList.isEmpty()) throw new InfoExсeption();

                Methodic methodic = MethodicsForTest.getMethodicsForTestInstance().getMethodic(chosBxMetodics.getValue());

                properties.setProperty("lastUnom", txtFldUnom.getText());
                properties.setProperty("lastAccuracyClassMeterAP", txtFldAccuracyAP.getText());
                properties.setProperty("lastAccuracyClassMeterRP", txtFldAccuracyRP.getText());
                properties.setProperty("lastInomAndImax", txtFldCurrent.getText());
                properties.setProperty("lastTypeOfMeasuringElement", chosBxtypeOfMeasuringElement.getValue());
                properties.setProperty("lastTypeCircuit", chosBxPowerType.getValue());
                properties.setProperty("lastFnom", txtFldFrg.getText());
                properties.setProperty("lastMeterManufacturer", metersList.get(0).getFactoryManufacturer());
                properties.setProperty("lastMeterConstantAP", metersList.get(0).getConstantMeterAP());
                properties.setProperty("lastMeterConstantRP", metersList.get(0).getConstantMeterRP());
                properties.setProperty("lastMethodicName", chosBxMetodics.getValue());
                properties.setProperty("lastMeterTypeOnePhaseMultiTarif", chosBxTypeMeter.getValue());

                ConsoleHelper.saveProperties();

                //Загрузка окна испытания
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/viewFXML/testErrorTableFrame.fxml"));

                fxmlLoader.load();

                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Поверка счётчиков");
                stage.setScene(new Scene(root));

                TestErrorTableFrameController testErrorTableFrameController = fxmlLoader.getController();

                //Учтановка и передача параметров
                testErrorTableFrameController.setStendDLLCommands(stendDLLCommands);
                testErrorTableFrameController.setMethodic(methodic);
                testErrorTableFrameController.setUn(Un);
                testErrorTableFrameController.setAccuracyClassAP(accuracyClassAP);
                testErrorTableFrameController.setAccuracyClassRP(accuracyClassRP);
                testErrorTableFrameController.setFn(Fn);
                testErrorTableFrameController.setIb(Ib);
                testErrorTableFrameController.setImax(Imax);
                testErrorTableFrameController.setTypeCircuit(typeCircuitThreePhase);
                testErrorTableFrameController.setTypeOfMeasuringElementShunt(typeOfMeasuringElementShunt);

                //Установка информации в окне теста
                testErrorTableFrameController.getTxtLabUn().setText("Uн = " + Un + " В");
                testErrorTableFrameController.getTxtLabInom().setText("Iн = "+ Ib +" А");
                testErrorTableFrameController.getTxtLabImax().setText("Iмакc = " + Imax + " А");
                testErrorTableFrameController.getTxtLabFn().setText("Fн = " + Fn + " Гц");
                testErrorTableFrameController.getTxtLabTypeCircuit().setText("Тип сети: " + chosBxPowerType.getValue());
                testErrorTableFrameController.getTxtLabAccuracyСlass().setText("Класс точности: " + txtFldAccuracyAP.getText() + "/" + txtFldAccuracyRP.getText()  + " Акт/Реак");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                testErrorTableFrameController.getTxtLabDate().setText("Дата: " + simpleDateFormat.format(new Date()));

                testErrorTableFrameController.setListMetersForTest(metersList);

                testErrorTableFrameController.myInitTestErrorTableFrame();

                stage.show();

                testErrorTableFrameController.initScrolBars();

                Stage stage1 = (Stage) btnStartTest.getScene().getWindow();
                stage1.close();

            }catch (NumberFormatException e) {
                System.out.println("Произошла ошибка при переносе значений");
                e.printStackTrace();
            }catch (IOException e) {
                System.out.println("Произошла ошибка при загрузке окна");
                e.getStackTrace();
            }catch (InfoExсeption e) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/viewFXML/exceptionFrame.fxml/"));
                try {
                    fxmlLoader.load();
                } catch (IOException er) {
                    e.printStackTrace();
                }

                ExceptionFrameController exceptionFrameController = fxmlLoader.getController();
                exceptionFrameController.getExceptionLabel().setText("Должен быть выбран хотябы\nодин счётчик.");

                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Ошибка");
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }

    @FXML
    void initialize() {
        if (properties.getProperty("stendType").equals("ThreePhaseStend")) {
            stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
        } else stendDLLCommands = OnePhaseStend.getOnePhaseStendInstance();

        initComboBoxesAndAddListerners();
    }

    //Инициирует комбо боксы и добавляет им слушателей
    private void initComboBoxesAndAddListerners() {
        //Переносим данные из проперти файла
        String[] UnomComBox = properties.getProperty("Unom").split(", ");
        String[] FnomComBox = properties.getProperty("Fnom").split(", ");
        String[] InomAndImaxComBox = properties.getProperty("InomAndImax").split(", ");
        String[] accuracyClassMeterAPComBox = properties.getProperty("accuracyClassMeterAP").split(", ");
        String[] accuracyClassMeterRPComBox = properties.getProperty("accuracyClassMeterRP").split(", ");
        String[] typeCircuitComBox = properties.getProperty("typeCircuit").split(", ");
        String[] typeOfMeasuringElementComBox = properties.getProperty("typeOfMeasuringElement").split(", ");
        String[] typeMeter = properties.getProperty("meterTypeOnePhaseMultiTarif").split(", ");

        //Кладём элементы в КомбоБокс
        chosBxUnom.getItems().addAll(UnomComBox);
        chosBxFrg.getItems().addAll(FnomComBox);
        chosBxCurrent.getItems().addAll(InomAndImaxComBox);
        chosBxAccuracyAP.getItems().addAll(accuracyClassMeterAPComBox);
        chosBxAccuracyRP.getItems().addAll(accuracyClassMeterRPComBox);
        chosBxPowerType.getItems().addAll(typeCircuitComBox);
        chosBxtypeOfMeasuringElement.getItems().addAll(typeOfMeasuringElementComBox);
        chosBxTypeMeter.getItems().addAll(typeMeter);

        for (Methodic methodic : methodicsForTest.getMethodics()) {
            chosBxMetodics.getItems().add(methodic.getMethodicName());
        }

        //Задаём текстовым полям последнее сохранённое значение
        txtFldUnom.setText(properties.getProperty("lastUnom"));
        txtFldFrg.setText(properties.getProperty("lastFnom"));
        txtFldCurrent.setText(properties.getProperty("lastInomAndImax"));
        txtFldAccuracyAP.setText(properties.getProperty("lastAccuracyClassMeterAP"));
        txtFldAccuracyRP.setText(properties.getProperty("lastAccuracyClassMeterRP"));
        chosBxPowerType.setValue(properties.getProperty("lastTypeCircuit"));
        chosBxtypeOfMeasuringElement.setValue(properties.getProperty("lastTypeOfMeasuringElement"));
        chosBxTypeMeter.setValue(properties.getProperty("lastMeterTypeOnePhaseMultiTarif"));
        chosBxMetodics.setValue(properties.getProperty("lastMethodicName"));

        //Устанавливаем слушателей
        chosBxUnom.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
           txtFldUnom.setText(newValue);
        });
        chosBxFrg.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldFrg.setText(newValue);
        });
        chosBxCurrent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldCurrent.setText(newValue);
        });
        chosBxAccuracyAP.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldAccuracyAP.setText(newValue);
        });
        chosBxAccuracyRP.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldAccuracyRP.setText(newValue);
        });

        initTableView();
    }

    private void initTableView() {
        for (int i = 1; i <= Integer.parseInt(properties.getProperty("stendAmountPlaces")); i++) {
            Meter me = new Meter();
            me.setId(i);
            me.setConstantMeterAP(properties.getProperty("lastMeterConstantAP"));
            me.setConstantMeterRP(properties.getProperty("lastMeterConstantRP"));
            me.setModelMeter(properties.getProperty("lastMeterModel"));
            me.setFactoryManufacturer(properties.getProperty("lastMeterManufacturer"));
            metersList.add(me);
        }

        meterObservableList = FXCollections.observableArrayList(metersList);

        tabVParamMeters.setEditable(true);

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
        ObservableList<String> meterModelList = FXCollections.observableArrayList(properties.getProperty("meterModel").split(", "));
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
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newSerNo = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setModelMeter(newSerNo);
        });
        tabColModelMeter.setStyle("-fx-alignment: CENTER;");

        tabColManufacturer.setCellValueFactory(new PropertyValueFactory<>("factoryManufacturer"));

        tabColManufacturer.setCellFactory(ComboBoxTableCell.forTableColumn(properties.getProperty("meterManufacturer").split(", ")));

        tabColManufacturer.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String param = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setFactoryManufacturer(param);
        });
        tabColManufacturer.setStyle("-fx-alignment: CENTER;");

        meterObservableList = FXCollections.observableArrayList(metersList);
        tabVParamMeters.setItems(meterObservableList);

        tabColMeterDis.setSortable(false);
        tabColIdMeter.setSortable(false);
        tabColSerNoMeter.setSortable(false);
        tabColConstAPMeter.setSortable(false);
        tabColConstRPMeter.setSortable(false);
        tabColModelMeter.setSortable(false);

        tabVParamMeters.setPlaceholder(new Label("Укажите количество мест в настройках"));
    }

}
