package org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.InfoExсeption;

import java.util.Arrays;
import java.util.Comparator;


public class AddDeleteTestPointInGridPaneController {

    private ObservableList<String> powerFactor = FXCollections.observableArrayList(ConsoleHelper.properties.getProperty("powerFactorForMetodicPane").split(", "));
    private ObservableList<String> current = FXCollections.observableArrayList(ConsoleHelper.properties.getProperty("currentForMethodicPane").split(", "));

    private String[] powerFactorCoef = {"","L","C"};
    private String[] currentImaxIb = {"Imax", "Ib"};

    Comparator<String> comparatorForPowerFactor = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o2.compareTo(o1);
        }
    };

    Comparator<String> comparatorForIbImax = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            String[] first = o1.split(" ");
            String[] second = o2.split(" ");

            if (first[1].compareTo(second[1]) == 0) {
                if (Float.parseFloat(first[0]) > Float.parseFloat(second[0])) {
                    return -1;
                } else {
                    return 1;
                }
            } else if (first[1].compareTo(second[1]) > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    @FXML
    private ListView<String> tabListPowerFactor;

    @FXML
    private ListView<String> tabListCurrent;

    @FXML
    private TextField txtFldPowerFactor;

    @FXML
    private Button btnAddPowerFactor;

    @FXML
    private ChoiceBox<String> chBoxPowerFactor;

    @FXML
    private Button btnDeletePowerFactor;

    @FXML
    private TextField txtFldCurrent;

    @FXML
    private ChoiceBox<String> chBoxCurrent;

    @FXML
    private Button btnAddCurrent;

    @FXML
    private Button btnDeleteCurrent;

    @FXML
    private Button btnSave;

    @FXML
    void initialize() {

        chBoxPowerFactor.getItems().addAll(powerFactorCoef);
        chBoxPowerFactor.setValue(chBoxPowerFactor.getItems().get(0));

        chBoxCurrent.getItems().addAll(currentImaxIb);
        chBoxCurrent.setValue(chBoxCurrent.getItems().get(1));

        if (!powerFactor.isEmpty()) {
            powerFactor.sort(comparatorForPowerFactor);
            tabListPowerFactor.getItems().addAll(powerFactor);
            tabListPowerFactor.getSelectionModel().select(0);
        }
        if (!current.isEmpty()) {
            current.sort(comparatorForIbImax);
            tabListCurrent.getItems().addAll(current);
            tabListCurrent.getSelectionModel().select(0);
        }

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String powerFactor = Arrays.toString(tabListPowerFactor.getItems().toArray());
                String current = Arrays.toString(tabListCurrent.getItems().toArray());

                ConsoleHelper.properties.setProperty("powerFactorForMetodicPane", powerFactor.substring(1, powerFactor.length() - 1));
                ConsoleHelper.properties.setProperty("currentForMethodicPane", current.substring(1, current.length() - 1));

                ConsoleHelper.saveProperties();

                Stage stage = (Stage) btnSave.getScene().getWindow();

                /**
                 * Дописать реализацию перерисовки GridPane
                 */

                stage.close();
            }
        });
    }

    @FXML
    void addOrDeleteAction(ActionEvent event) {
        if (event.getSource() == btnAddPowerFactor) {
            try {
                if (txtFldPowerFactor.getText().isEmpty() || tabListPowerFactor.getItems().contains(txtFldPowerFactor.getText().trim() + chBoxPowerFactor.getValue())) {
                    txtFldPowerFactor.clear();
                    txtFldPowerFactor.setStyle("");
                    return;
                } else {

                    if (Float.parseFloat(txtFldPowerFactor.getText().trim()) > 1.0f || Float.parseFloat(txtFldPowerFactor.getText().trim()) < 0) {
                        throw new InfoExсeption("Значение должно быть в диапазоне\nот 0 до 1.0");
                    }

                    powerFactor.add(txtFldPowerFactor.getText().trim() + chBoxPowerFactor.getValue());
                    powerFactor.sort(comparatorForPowerFactor);
                    tabListPowerFactor.getItems().clear();
                    tabListPowerFactor.getItems().addAll(powerFactor);

                    txtFldPowerFactor.clear();
                    txtFldPowerFactor.setStyle("");
                }
            }catch (NumberFormatException | InfoExсeption e) {
                e.printStackTrace();
                System.out.println("Неверный формат");
                txtFldPowerFactor.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
            }
        }

        if (event.getSource() == btnDeletePowerFactor) {
            powerFactor.remove(tabListPowerFactor.getSelectionModel().getSelectedIndex());
            powerFactor.sort(comparatorForPowerFactor);
            tabListPowerFactor.getItems().clear();
            tabListPowerFactor.getItems().addAll(powerFactor);
        }

        if (event.getSource() == btnAddCurrent) {
            try {
                if (txtFldCurrent.getText().isEmpty() || tabListCurrent.getItems().contains(txtFldCurrent.getText().trim() + " " + chBoxCurrent.getValue())) {
                    txtFldCurrent.clear();
                    txtFldCurrent.setStyle("");
                    return;
                } else {
                    if (Float.parseFloat(txtFldCurrent.getText().trim()) > 100.0f || Float.parseFloat(txtFldCurrent.getText().trim()) < 0) {
                        throw new InfoExсeption("Значение должно быть в диапазоне\nот 0 до 1.0");
                    }
                    Float.parseFloat(txtFldCurrent.getText().trim());

                    current.add(txtFldCurrent.getText().trim() + " " + chBoxCurrent.getValue());
                    current.sort(comparatorForIbImax);
                    tabListCurrent.getItems().clear();
                    tabListCurrent.getItems().addAll(current);

                    txtFldCurrent.clear();
                    txtFldCurrent.setStyle("");
                }
            }catch (NumberFormatException | InfoExсeption e) {
                e.printStackTrace();
                System.out.println("Неверный формат");
                txtFldCurrent.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
            }
        }

        if (event.getSource() == btnDeleteCurrent) {
            tabListCurrent.getItems().remove(tabListCurrent.getSelectionModel().getSelectedItem());
        }
    }
}