package stend.controller.viewController;

import java.util.*;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import stend.controller.Meter;
import stend.controller.OnePhaseStend;
import stend.controller.StendDLLCommands;
import stend.controller.ThreePhaseStend;
import stend.helper.ConsoleHelper;
import stend.model.Methodic;
import stend.model.MethodicsForTest;

public class TestParametersFrameController {

    private MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private StendDLLCommands stendDLLCommands;

    private Properties properties = ConsoleHelper.properties;

    private List<Meter> metersList = new ArrayList<>(Integer.parseInt(properties.getProperty("stendAmountPlaces")));

    private ObservableList<Meter> meterObservableList;

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
    private Button btnNumbersMe;

    @FXML
    private TextField txtFldOperator;

    @FXML
    private TextField txtFldController;

    @FXML
    private TextField txtFldWitness;

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
    private ComboBox<String> chosBxTypeMeter;

    @FXML
    private ComboBox<String> chosBxPowerType;

    @FXML
    private ComboBox<String> chosBxFrg;

    @FXML
    private ComboBox<String> chosBxOperator;

    @FXML
    private ComboBox<String> chosBxController;

    @FXML
    private ComboBox<String> chosBxWitness;


    @FXML
    void buttonActionTestFrame(ActionEvent event) {

    }

    @FXML
    void initialize() {

        /**
         * Продолжи инициировать таблицу настроек для испытания счётчиков
         * Количество посадочных мест
         * И новые объекты счётчиков
         */

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
        String[] operatorsComBox = properties.getProperty("Operators").split(", ");
        String[] WitnessComBox = properties.getProperty("Witness").split(", ");
        String[] controllerComBox = properties.getProperty("Controller").split(", ");

        //Кладём элементы в КомбоБокс
        chosBxUnom.getItems().addAll(UnomComBox);
        chosBxFrg.getItems().addAll(FnomComBox);
        chosBxCurrent.getItems().addAll(InomAndImaxComBox);
        chosBxAccuracyAP.getItems().addAll(accuracyClassMeterAPComBox);
        chosBxAccuracyRP.getItems().addAll(accuracyClassMeterRPComBox);
        chosBxPowerType.getItems().addAll(typeCircuitComBox);
        chosBxTypeMeter.getItems().addAll(typeOfMeasuringElementComBox);
        chosBxOperator.getItems().addAll(operatorsComBox);
        chosBxWitness.getItems().addAll(WitnessComBox);
        chosBxController.getItems().addAll(controllerComBox);

        for (Methodic methodic : methodicsForTest.getMethodics()) {
            chosBxMetodics.getItems().addAll(methodic.getMethodicName());
        }

        //Задаём текстовым полям последнее сохранённое значение
        txtFldUnom.setText(properties.getProperty("lastUnom"));
        txtFldFrg.setText(properties.getProperty("lastFnom"));
        txtFldCurrent.setText(properties.getProperty("lastInomAndImax"));
        txtFldAccuracyAP.setText(properties.getProperty("lastAccuracyClassMeterAP"));
        txtFldAccuracyRP.setText(properties.getProperty("lastAccuracyClassMeterRP"));
        chosBxPowerType.setValue(properties.getProperty("lastTypeCircuit"));
        chosBxTypeMeter.setValue(properties.getProperty("lastTypeOfMeasuringElement"));
        txtFldOperator.setText(properties.getProperty("lastOperator"));
        txtFldController.setText(properties.getProperty("lastController"));
        txtFldWitness.setText(properties.getProperty("lastWitness"));

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
        chosBxOperator.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldOperator.setText(newValue);
        });
        chosBxController.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldController.setText(newValue);
        });
        chosBxWitness.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txtFldWitness.setText(newValue);
        });

        initTableView();
    }

    private void initTableView() {
        for (int i = 1; i <= Integer.parseInt(properties.getProperty("stendAmountPlaces")); i++) {
            Meter me = new Meter();
            me.setId(String.valueOf(i));
            me.setConstantMeterAP(properties.getProperty("meterLastConstantAP"));
            me.setConstantMeterRP(properties.getProperty("meterLastConstantRP"));
            me.setModelMeter(properties.getProperty("lastMeterModel"));
            metersList.add(me);
        }

        meterObservableList = FXCollections.observableArrayList(metersList);

        tabVParamMeters.setEditable(true);

        //Установка чек боксов и добавление к ним слушателя
        tabColMeterDis.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Meter, Boolean> param) {
                Meter meter = param.getValue();
                SimpleBooleanProperty simpleBooleanProperty = new SimpleBooleanProperty(meter.isActive());

                simpleBooleanProperty.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        meter.setActive(newValue);
                    }
                });

                return simpleBooleanProperty;
            }
        });

        tabColMeterDis.setCellFactory(new Callback<TableColumn<Meter, Boolean>, //
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

        //Отображение константы активной энергии
        tabColConstAPMeter.setCellValueFactory(new PropertyValueFactory<>("constantMeterAP"));
        tabColConstAPMeter.setCellFactory(TextFieldTableCell.<Meter>forTableColumn());
        tabColConstAPMeter.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

//            TablePosition<Meter, String> pos = event.getTablePosition();
//
            String constAP = event.getNewValue();
//
//            int row = pos.getRow();
            Meter meter;
//
//            meter.setConstantMeterAP(constAP);
            for (int i = 0; i < metersList.size(); i++) {
                meter = event.getTableView().getItems().get(i);
                meter.setConstantMeterAP(constAP);
            }
            tabVParamMeters.refresh();
        });

        //Отображение константы реактивной энергии
        tabColConstRPMeter.setCellValueFactory(new PropertyValueFactory<>("constantMeterRP"));
        tabColConstRPMeter.setCellFactory(TextFieldTableCell.<Meter>forTableColumn());
        tabColConstRPMeter.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {

            TablePosition<Meter, String> pos = event.getTablePosition();

            String constRP = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setConstantMeterRP(constRP);
        });

        //Выбор модели счётчика из списка
        ObservableList<String> meterModelList = FXCollections.observableArrayList(properties.getProperty("meterModel").split(", "));
        tabColModelMeter.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Meter, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Meter, String> param) {
                Meter meter = param.getValue();

                String modelMeter = meter.getModelMeter();
                return new SimpleObjectProperty<>(modelMeter);
            }

//            @Override
//            public ObservableValue<String> call(CellDataFeatures<Person, Gender> param) {
//                Person person = param.getValue();
//                // F,M
//                String genderCode = person.getGender();
//                Gender gender = Gender.getByCode(genderCode);
//                return new SimpleObjectProperty<Gender>(gender);
//            }
        });

        tabColModelMeter.setCellFactory(ComboBoxTableCell.forTableColumn(meterModelList));

        tabColModelMeter.setOnEditCommit((TableColumn.CellEditEvent<Meter, String> event) -> {
            TablePosition<Meter, String> pos = event.getTablePosition();

            String newSerNo = event.getNewValue();

            int row = pos.getRow();
            Meter meter = event.getTableView().getItems().get(row);

            meter.setModelMeter(newSerNo);
        });

        meterObservableList = FXCollections.observableArrayList(metersList);
        tabVParamMeters.setItems(meterObservableList);

        tabColMeterDis.setSortable(false);
        tabColIdMeter.setSortable(false);
        tabColSerNoMeter.setSortable(false);
        tabColConstAPMeter.setSortable(false);
        tabColConstRPMeter.setSortable(false);
        tabColModelMeter.setSortable(false);

        tabVParamMeters.setPlaceholder(new Label("Укажите количество мест в настрйках"));
    }
}
