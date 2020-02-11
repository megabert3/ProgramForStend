package stend.controller.viewController;

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
        if (event.getSource() == btnStartTest) {
            /**
             * Сделать проверки полей
             */
            try {
                double Un = Double.parseDouble(txtFldUnom.getText());
                double accuracyClassAP = Double.parseDouble(txtFldAccuracyAP.getText());
                double accuracyClassRP = Double.parseDouble(txtFldAccuracyRP.getText());

                StringBuilder stringBuilder = new StringBuilder();
                Pattern pat = Pattern.compile("[0-9]+");
                Matcher mat = pat.matcher(txtFldCurrent.getText());

                while (mat.find()) {
                    stringBuilder.append(mat.group()).append(",");
                }

                String[] current = new String(stringBuilder).split(",");

                double Ib = Double.parseDouble(current[0]);
                double Imax = Double.parseDouble(current[1]);
                boolean typeOfMeasuringElementShunt = false;

                if (chosBxTypeMeter.getValue().equals("Шунт")) {
                    typeOfMeasuringElementShunt = true;
                }

                boolean typeCircuitThreePhase = false;
                if (chosBxPowerType.getValue().equals("3P4W")) {
                    typeCircuitThreePhase = true;
                }

                double Fn = Double.parseDouble(txtFldFrg.getText());

                Methodic methodic = MethodicsForTest.getMethodicsForTestInstance().getMethodic(chosBxMetodics.getValue());

                properties.setProperty("lastUnom", txtFldUnom.getText());
                properties.setProperty("lastAccuracyClassMeterAP", txtFldAccuracyAP.getText());
                properties.setProperty("lastAccuracyClassMeterRP", txtFldAccuracyRP.getText());
                properties.setProperty("InomAndImax", txtFldCurrent.getText());
                properties.setProperty("lastTypeOfMeasuringElement", chosBxTypeMeter.getValue());
                properties.setProperty("lastTypeCircuit", chosBxPowerType.getValue());
                properties.setProperty("lastFnom", txtFldFrg.getText());
                properties.setProperty("lastOperator", txtFldOperator.getText());
                properties.setProperty("lastController", txtFldController.getText());
                properties.setProperty("lastWitness", txtFldWitness.getText());

                //Подумать над этим должна быть ошибка при нулевой методике
                if (chosBxMetodics.getValue() != null) {
                    properties.setProperty("lastMethodicName", chosBxMetodics.getValue());
                }

                //Загрузка окна испытания
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/stend/view/testErrorTableFrame.fxml"));

                fxmlLoader.load();

                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Поверка счётчиков");
                stage.setScene(new Scene(root));

                TestErrorTableFrameController testErrorTableFrameController = fxmlLoader.getController();

                properties.setProperty("lastOperator", txtFldOperator.getText());
                properties.setProperty("lastController", txtFldController.getText());
                properties.setProperty("lastWitness", txtFldWitness.getText());

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

                testErrorTableFrameController.setController(txtFldController.getText());
                testErrorTableFrameController.setOperator(txtFldOperator.getText());
                testErrorTableFrameController.setWitness(txtFldWitness.getText());

                //Установка информации в окне теста
                testErrorTableFrameController.getTxtLabUn().setText("Uн = " + Un + " В");
                testErrorTableFrameController.getTxtLabInom().setText("Iн = "+ Ib +" А");
                testErrorTableFrameController.getTxtLabImax().setText("Iмакc = " + Imax + " А");
                testErrorTableFrameController.getTxtLabFn().setText("Fн = " + Fn + " Гц");
                testErrorTableFrameController.getTxtLabTypeCircuit().setText("Тип сети: " + chosBxPowerType.getValue());
                testErrorTableFrameController.getTxtLabAccuracyСlass().setText("Класс точности: " + txtFldAccuracyAP.getText() + "/" + txtFldAccuracyRP.getText()  + " Акт/Реак");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                testErrorTableFrameController.getTxtLabDate().setText("Дата: " + simpleDateFormat.format(new Date()));

                for (int i = 0; i < metersList.size(); i++) {
                    if (!metersList.get(i).isActive()) {
                        metersList.remove(i);
                        i--;
                    }
                }

                testErrorTableFrameController.setListMetersForTest(metersList);

                testErrorTableFrameController.myInitTestErrorTableFrame();

                stage.show();

                testErrorTableFrameController.initScrolBars();

                Stage stage1 = (Stage) txtFldController.getScene().getWindow();
                stage1.close();

            }catch (NumberFormatException e) {
                System.out.println("Произошла ошибка при переносе значений");
                e.printStackTrace();
            }catch (IOException e) {
                System.out.println("Произошла ошибка при загрузке окна");
                e.getStackTrace();
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
            me.setId(i);
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

            String constAP = event.getNewValue();

            Meter meter;

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

            String constRP = event.getNewValue();

            Meter meter;

            for (int i = 0; i < metersList.size(); i++) {
                meter = event.getTableView().getItems().get(i);
                meter.setConstantMeterRP(constRP);
            }
            tabVParamMeters.refresh();
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
