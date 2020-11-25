package org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.InfoExсeption;

import java.util.Comparator;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна "addDeleteTestPointInGridPaneFrame.fxml".
 * Данный класс отвечает за возможность добавления новых параметров при создании ErrorCommand"
 */
public class AddDeleteTestPointInGridPaneController {

    //В зависимости от того из какого окна вызывается это, присваиваю объкту ссылку на него
    //для дальнейших операций с ним
    private AddEditPointsThreePhaseStendFrameController addEditPointsThreePhaseStendFrameController;
    private AddEditPointsOnePhaseStendFrameController addEditPointsOnePhaseStendFrameController;

    //Список значений коэффициента активной мощности для редактирования и добавления новых значений
    private ObservableList<String> powerFactor = FXCollections.observableArrayList(ConsoleHelper.properties.getProperty("powerFactorForMetodicPane").split(", "));
    //Список значений тока для редактирования и добавления новых значений
    private ObservableList<String> current = FXCollections.observableArrayList(ConsoleHelper.properties.getProperty("currentForMethodicPane").split(", "));

    //Возможные варианты для правильности ввода значений
    private String[] powerFactorCoef = {"","L","C"};
    private String[] currentImaxIb = {"Imax", "Ib"};

    //Сортирую список значений коэффициента мощности при добавлении
    private Comparator<String> comparatorForPowerFactor = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o2.compareTo(o1);
        }
    };
    //Сортирую список значений тока при добавлении
    private Comparator<String> comparatorForIbImax = new Comparator<String>() {
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
    private ComboBox<String> chBoxPowerFactor;

    @FXML
    private Button btnDeletePowerFactor;

    @FXML
    private TextField txtFldCurrent;

    @FXML
    private ComboBox<String> chBoxCurrent;

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

        txtFldCurrent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnAddCurrent.fire();
            }
        });

        txtFldPowerFactor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnAddPowerFactor.fire();
            }
        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Сохраняю изменения
                ConsoleHelper.saveProperties();

                //В зависимости от того с какого окна вызывалось это, обновляю информацию в GUI'е того окна с учётом новых добавленных значений
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (addEditPointsThreePhaseStendFrameController != null) {
                            addEditPointsThreePhaseStendFrameController.refreshGridPaneAndScrolPane();
                            addEditPointsThreePhaseStendFrameController.addTestPointsOnGreedPane();
                            addEditPointsThreePhaseStendFrameController.addListenerToCheckBoxes();
                        } else {
                            addEditPointsOnePhaseStendFrameController.refreshGridPaneAndScrolPane();
                            addEditPointsOnePhaseStendFrameController.addTestPointsOnGreedPane();
                            addEditPointsOnePhaseStendFrameController.addListenerToCheckBoxes();
                        }
                    }
                });

                Stage stage = (Stage) btnSave.getScene().getWindow();
                stage.close();
            }
        });
    }

    /**
     * Добавляет либо удаляет значение тока или коэффициента мощности из списка возможных
     * @param event
     */
    @FXML
    void addOrDeleteAction(ActionEvent event) {
        if (event.getSource() == btnAddPowerFactor) {
            try {
                //Если пустое значение или введённое значение уже есть в списке
                if (txtFldPowerFactor.getText().isEmpty() || tabListPowerFactor.getItems().contains(txtFldPowerFactor.getText().trim() + chBoxPowerFactor.getValue())) {
                    txtFldPowerFactor.clear();
                    txtFldPowerFactor.setStyle("");
                    return;

                    //Если введённого значения нет в списке
                } else {
                    //Проверка на корректность
                    if (Float.parseFloat(txtFldPowerFactor.getText().trim()) > 1.0f || Float.parseFloat(txtFldPowerFactor.getText().trim()) < 0) {
                        throw new InfoExсeption("Значение должно быть в диапазоне\nот 0 до 1.0");
                    }

                    //добавляю значение в лист
                    powerFactor.add(txtFldPowerFactor.getText().trim() + chBoxPowerFactor.getValue());
                    powerFactor.sort(comparatorForPowerFactor);
                    tabListPowerFactor.getItems().clear();
                    tabListPowerFactor.getItems().addAll(powerFactor);

                    txtFldPowerFactor.clear();
                    txtFldPowerFactor.setStyle("");
                }
            }catch (NumberFormatException | InfoExсeption e) {
                e.printStackTrace();
                txtFldPowerFactor.setStyle("-fx-text-box-border: red ;  -fx-focus-color: red ;");
            }
        }

        //Если необходимо удалить значение из списка
        if (event.getSource() == btnDeletePowerFactor) {
            powerFactor.remove(tabListPowerFactor.getSelectionModel().getSelectedIndex());
            powerFactor.sort(comparatorForPowerFactor);
            tabListPowerFactor.getItems().clear();
            tabListPowerFactor.getItems().addAll(powerFactor);
        }

        //Если пользователь хочет добавить значение тока
        if (event.getSource() == btnAddCurrent) {
            try {
                //Проверка на уникальность значения и на пустоту
                if (txtFldCurrent.getText().isEmpty() || tabListCurrent.getItems().contains(txtFldCurrent.getText().trim() + " " + chBoxCurrent.getValue())) {
                    txtFldCurrent.clear();
                    txtFldCurrent.setStyle("");
                    return;

                    //Проверяю на корректность введённое число
                } else {
                    if (Float.parseFloat(txtFldCurrent.getText().trim()) > 100.0f || Float.parseFloat(txtFldCurrent.getText().trim()) < 0) {
                        throw new InfoExсeption("Значение должно быть в диапазоне\nот 0 до 100.0");
                    }
                    Float.parseFloat(txtFldCurrent.getText().trim());

                    //Добавление значения
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

        //Удаление значения тока
        if (event.getSource() == btnDeleteCurrent) {
            tabListCurrent.getItems().remove(tabListCurrent.getSelectionModel().getSelectedItem());
        }
    }

    public void setAddEditPointsThreePhaseStendFrameController(AddEditPointsThreePhaseStendFrameController addEditPointsThreePhaseStendFrameController) {
        this.addEditPointsThreePhaseStendFrameController = addEditPointsThreePhaseStendFrameController;
    }

    public void setAddEditPointsOnePhaseStendFrameController(AddEditPointsOnePhaseStendFrameController addEditPointsOnePhaseStendFrameController) {
        this.addEditPointsOnePhaseStendFrameController = addEditPointsOnePhaseStendFrameController;
    }
}