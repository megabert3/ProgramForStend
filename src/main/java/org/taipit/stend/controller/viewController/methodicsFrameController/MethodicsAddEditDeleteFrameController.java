package org.taipit.stend.controller.viewController.methodicsFrameController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.stage.WindowEvent;
import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.controller.viewController.YesOrNoFrameController;
import org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController.AddEditPointsOnePhaseStendFrameController;
import org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController.AddEditPointsThreePhaseStendFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.InfoExсeption;
import org.taipit.stend.helper.frameManager.Frame;
import org.taipit.stend.model.metodics.MethodicForOnePhaseStend;
import org.taipit.stend.model.metodics.MethodicForThreePhaseStend;
import org.taipit.stend.model.metodics.Metodic;
import org.taipit.stend.model.metodics.MetodicsForTest;


public class MethodicsAddEditDeleteFrameController implements Frame {

    private MetodicsForTest metodicsForTest = MetodicsForTest.getMetodicsForTestInstance();

    private ObservableList<Metodic> metodicsNameList = FXCollections.observableArrayList(metodicsForTest.getMethodicForStends());

    //Выделенная методикка
    private Metodic focusedMetodic;

    //Листы для точек из методики
    private List<String> comandListAPPls = new ArrayList<>();
    private List<String> comandListAPMns = new ArrayList<>();
    private List<String> comandListRPPls = new ArrayList<>();
    private List<String> comandListRPMns = new ArrayList<>();

    private MethodicNameController methodicNameController;

    private AddEditPointsThreePhaseStendFrameController addEditPointsThreePhaseStendFrameController;
    private AddEditPointsOnePhaseStendFrameController addEditPointsOnePhaseStendFrameController;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private AnchorPane paneWithEdits;

    @FXML
    private Button copyMetBtn;

    @FXML
    private Button editMetBtn;

    @FXML
    private Button deleteMetBtn;

    @FXML
    private Button addMetBtn;

    @FXML
    private ToggleButton tglBtnAPPls;

    @FXML
    private ToggleButton tglBtnAPMns;

    @FXML
    private ToggleButton tglBtnRPPls;

    @FXML
    private ToggleButton tglBtnRPMns;

    @FXML
    private Button BtnGoToStartTest;

    @FXML
    private TableView<Metodic> viewPointTable = new TableView<>();

    @FXML
    private TableColumn<Metodic, String> tabClMethodics;

    @FXML
    private ListView<String> ListViewAPPls;

    @FXML
    private ListView<String> ListViewAPMns;

    @FXML
    private ListView<String> ListViewRPPls;

    @FXML
    private ListView<String> ListViewRPMns;

    //Дейстаие при нажатии выбора направления энергии
    @FXML
    void tglBtnsEnegyViewAction(ActionEvent event) {
        if (event.getSource() == tglBtnAPPls) {
            tglBtnAPPls.setSelected(true);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnAPMns) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(true);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnRPPls) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(true);
            tglBtnRPMns.setSelected(false);
        }

        if (event.getSource() == tglBtnRPMns) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(true);
        }
        toFront();
    }

    //Имитирует Tgl group
    private void imitationTglGroup() {
        if (tglBtnAPPls.isSelected()) {
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (tglBtnAPMns.isSelected()) {
            tglBtnAPPls.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (tglBtnRPPls.isSelected()) {
            tglBtnAPMns.setSelected(false);
            tglBtnAPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
        }

        if (tglBtnRPMns.isSelected()) {
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnAPPls.setSelected(false);
        }
    }


    @FXML
    void initialize() {
        tglBtnAPPls.setSelected(true);

        initMethodicListName();
    }

    @FXML
    void actinonForMethodicsFrame(ActionEvent event) {
        //Добавление методики
        if (event.getSource() == addMetBtn) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/metodicName.fxml"));
                fxmlLoader.load();
                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Имя методики методики");
                stage.setScene(new Scene(root));

                methodicNameController = fxmlLoader.getController();
                methodicNameController.setMethodicsAddEditDeleteFrameController(this);
                methodicNameController.setAdd(true);


                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Редактирование методики
        if (event.getSource() == editMetBtn) {
            try {
                if (focusedMetodic == null) throw new InfoExсeption("Не выбрана методика");

            }catch (InfoExсeption e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ConsoleHelper.infoException("Выберите методику");
                    }
                });
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            Stage stage = new Stage();

            if (focusedMetodic instanceof MethodicForThreePhaseStend) {

                fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/ThreePhase/addEditPointsThreePhaseStendMet.fxml"));
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = fxmlLoader.getRoot();

                stage.setTitle("Редактирование методики");
                stage.setScene(new Scene(root));

                addEditPointsThreePhaseStendFrameController = fxmlLoader.getController();
                addEditPointsThreePhaseStendFrameController.setMethodicForThreePhaseStend((MethodicForThreePhaseStend) focusedMetodic);
                addEditPointsThreePhaseStendFrameController.setEdit(true);

                if (focusedMetodic.isBindsParameters()) {
                    addEditPointsThreePhaseStendFrameController.setBindParameters(true);
                    addEditPointsThreePhaseStendFrameController.getParametersBtn().setSelected(true);
                }

                addEditPointsThreePhaseStendFrameController.initEditsMetodic();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        addEditPointsThreePhaseStendFrameController.addTestPointsOnGreedPane();
                        addEditPointsThreePhaseStendFrameController.addListenerToCheckBoxes();
                    }
                });


                addEditPointsThreePhaseStendFrameController.setMethodicsAddEditDeleteFrameController(this);
                addEditPointsThreePhaseStendFrameController.setTextFielMethodicName();

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

                addEditPointsThreePhaseStendFrameController.bindScrollPanesCurrentAndPowerFactorToMainScrollPane();

            } else {

                fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/OnePhase/addEditPointsOnePhaseStendMet.fxml"));
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = fxmlLoader.getRoot();

                stage.setTitle("Редактирование методики");
                stage.setScene(new Scene(root));

                addEditPointsOnePhaseStendFrameController = fxmlLoader.getController();
                addEditPointsOnePhaseStendFrameController.setMethodicForOnePhaseStend((MethodicForOnePhaseStend) focusedMetodic);
                addEditPointsOnePhaseStendFrameController.setEdit(true);

                if (focusedMetodic.isBindsParameters()) {
                    addEditPointsOnePhaseStendFrameController.setBindParameters(true);
                    addEditPointsOnePhaseStendFrameController.getParametersBtn().setSelected(true);
                }

                addEditPointsOnePhaseStendFrameController.initEditsMetodic();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        addEditPointsOnePhaseStendFrameController.addTestPointsOnGreedPane();
                        addEditPointsOnePhaseStendFrameController.addListenerToCheckBoxes();
                    }
                });

                addEditPointsOnePhaseStendFrameController.setMethodicsAddEditDeleteFrameController(this);
                addEditPointsOnePhaseStendFrameController.setTextFielMethodicName();

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

                addEditPointsOnePhaseStendFrameController.bindScrollPanesCurrentAndPowerFactorToMainScrollPane();

            }

            Stage stage1 = (Stage) editMetBtn.getScene().getWindow();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                    stage1.show();
                    stage.close();
                }
            });

            stage1.hide();
        }

        //Если нажата кнопка копирования методики
        if (event.getSource() == copyMetBtn) {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/metodicName.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Копирование методики");
            stage.setScene(new Scene(root));

            MethodicNameController methodicNameController = fxmlLoader.getController();
            methodicNameController.setClone(true);
            methodicNameController.setMethodicForStend(focusedMetodic);
            methodicNameController.setMethodicsAddEditDeleteFrameController(this);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }

        //Если нажата кнопка "Удалить"
        if (event.getSource() == deleteMetBtn) {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/yesOrNoFrame.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Удаление методики");
            stage.setScene(new Scene(root));

            YesOrNoFrameController yesOrNoFrameController = fxmlLoader.getController();
            yesOrNoFrameController.setDeliteMethodic(true);
            yesOrNoFrameController.setMethodicForStend(focusedMetodic);
            yesOrNoFrameController.getQuestionTxt().setText("Вы действительно желаете удалить\nметодику: " +
                    focusedMetodic.getMetodicName() + "?");
            yesOrNoFrameController.getQuestionTxt().setLayoutX(150);
            yesOrNoFrameController.getQuestionTxt().setLayoutY(30);
            yesOrNoFrameController.setMethodicsAddEditDeleteFrameController(this);

            stage.show();
        }
    }

    //Инициирует список методик
    private void initMethodicListName() {
        tabClMethodics.setCellValueFactory(new PropertyValueFactory<>("metodicName"));

        tabClMethodics.setCellFactory(TextFieldTableCell.forTableColumn());

        tabClMethodics.setOnEditCommit((TableColumn.CellEditEvent<Metodic, String> event) -> {
            TablePosition<Metodic, String> pos = event.getTablePosition();

            String newImpulseValue = event.getNewValue();

            if (newImpulseValue.trim().isEmpty()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ConsoleHelper.infoException("Название не должно быть пустым");
                    }
                });

                viewPointTable.refresh();
                return;
            }

            int row = pos.getRow();

            Metodic methodicForStend = event.getTableView().getItems().get(row);
            try {
                for (Metodic methodicForStendName : MetodicsForTest.getMetodicsForTestInstance().getMethodicForStends()) {
                    if (methodicForStendName.getMetodicName().equals(newImpulseValue)) throw new InfoExсeption();
                }

                methodicForStend.setMetodicName(newImpulseValue);

            }catch (InfoExсeption e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ConsoleHelper.infoException("Методика с таким именем уже существует");
                    }
                });

                viewPointTable.refresh();
            }
        });

        viewPointTable.setEditable(true);
        viewPointTable.setItems(metodicsNameList);

        viewPointTable.setPlaceholder(new Label("У вас не создано ни одной методики"));

        metodicsNameList = viewPointTable.getSelectionModel().getSelectedItems();

        metodicsNameList.addListener(new ListChangeListener<Metodic>() {
            @Override
            public void onChanged(Change<? extends Metodic> c) {
                tglBtnAPPls.setSelected(true);
                focusedMetodic = c.getList().get(0);

                setListsView();

                ListViewAPPls.toFront();
                imitationTglGroup();
            }
        });
    }

    //Инициализирует список команд в окне отображения команд
    private void setListsView() {
        if (focusedMetodic == null) return;

        comandListAPPls.clear();
        comandListAPMns.clear();
        comandListRPPls.clear();
        comandListRPMns.clear();

        for (Commands commandsAPPls : focusedMetodic.getCommandsMap().get(0)) {
            comandListAPPls.add(commandsAPPls.getName());
        }

        for (Commands inflAPPls : focusedMetodic.getSaveInflListForCollumAPPls()) {
            comandListAPPls.add(inflAPPls.getName());
        }

        for (Commands creepStaConsAPPls : focusedMetodic.getCreepStartRTCConstCommandsMap().get(0)) {
            comandListAPPls.add(creepStaConsAPPls.getName());
        }

        for (Commands commandsAPMns : focusedMetodic.getCommandsMap().get(1)) {
            comandListAPMns.add(commandsAPMns.getName());
        }

        for (Commands inflAPMns : focusedMetodic.getSaveInflListForCollumAPMns()) {
            comandListAPMns.add(inflAPMns.getName());
        }

        for (Commands creepStaConsAPMns : focusedMetodic.getCreepStartRTCConstCommandsMap().get(1)) {
            comandListAPMns.add(creepStaConsAPMns.getName());
        }

        for (Commands commandsRPPls : focusedMetodic.getCommandsMap().get(2)) {
            comandListRPPls.add(commandsRPPls.getName());
        }

        for (Commands inflRPPls : focusedMetodic.getSaveInflListForCollumRPPls()) {
            comandListRPPls.add(inflRPPls.getName());
        }

        for (Commands creepStaConsRPPls : focusedMetodic.getCreepStartRTCConstCommandsMap().get(2)) {
            comandListRPPls.add(creepStaConsRPPls.getName());
        }

        for (Commands commandsRPMns : focusedMetodic.getCommandsMap().get(3)) {
            comandListRPMns.add(commandsRPMns.getName());
        }

        for (Commands inflRPMns : focusedMetodic.getSaveInflListForCollumRPMns()) {
            comandListRPMns.add(inflRPMns.getName());
        }

        for (Commands creepStaConsRPMns : focusedMetodic.getCreepStartRTCConstCommandsMap().get(3)) {
            comandListRPMns.add(creepStaConsRPMns.getName());
        }

        ListViewAPPls.setItems(FXCollections.observableArrayList(comandListAPPls));
        ListViewAPMns.setItems(FXCollections.observableArrayList(comandListAPMns));
        ListViewRPPls.setItems(FXCollections.observableArrayList(comandListRPPls));
        ListViewRPMns.setItems(FXCollections.observableArrayList(comandListRPMns));
    }

    public void setListsView(Metodic methodicForThreePhaseStend) {
        focusedMetodic = methodicForThreePhaseStend;

        comandListAPPls.clear();
        comandListAPMns.clear();
        comandListRPPls.clear();
        comandListRPMns.clear();

        for (Commands commandsAPPls : focusedMetodic.getCommandsMap().get(0)) {
            comandListAPPls.add(commandsAPPls.getName());
        }

        for (Commands inflAPPls : focusedMetodic.getSaveInflListForCollumAPPls()) {
            comandListAPPls.add(inflAPPls.getName());
        }

        for (Commands creepStaConsAPPls : focusedMetodic.getCreepStartRTCConstCommandsMap().get(0)) {
            comandListAPPls.add(creepStaConsAPPls.getName());
        }

        for (Commands commandsAPMns : focusedMetodic.getCommandsMap().get(1)) {
            comandListAPMns.add(commandsAPMns.getName());
        }

        for (Commands inflAPMns : focusedMetodic.getSaveInflListForCollumAPMns()) {
            comandListAPMns.add(inflAPMns.getName());
        }

        for (Commands creepStaConsAPMns : focusedMetodic.getCreepStartRTCConstCommandsMap().get(1)) {
            comandListAPMns.add(creepStaConsAPMns.getName());
        }

        for (Commands commandsRPPls : focusedMetodic.getCommandsMap().get(2)) {
            comandListRPPls.add(commandsRPPls.getName());
        }

        for (Commands inflRPPls : focusedMetodic.getSaveInflListForCollumRPPls()) {
            comandListRPPls.add(inflRPPls.getName());
        }

        for (Commands creepStaConsRPPls : focusedMetodic.getCreepStartRTCConstCommandsMap().get(2)) {
            comandListRPPls.add(creepStaConsRPPls.getName());
        }

        for (Commands commandsRPMns : focusedMetodic.getCommandsMap().get(3)) {
            comandListRPMns.add(commandsRPMns.getName());
        }

        for (Commands inflRPMns : focusedMetodic.getSaveInflListForCollumRPMns()) {
            comandListRPMns.add(inflRPMns.getName());
        }

        for (Commands creepStaConsRPMns : focusedMetodic.getCreepStartRTCConstCommandsMap().get(3)) {
            comandListRPMns.add(creepStaConsRPMns.getName());
        }

        ListViewAPPls.setItems(FXCollections.observableArrayList(comandListAPPls));
        ListViewAPMns.setItems(FXCollections.observableArrayList(comandListAPMns));
        ListViewRPPls.setItems(FXCollections.observableArrayList(comandListRPPls));
        ListViewRPMns.setItems(FXCollections.observableArrayList(comandListRPMns));
    }

    private void toFront() {
        if (tglBtnAPPls.isSelected()) ListViewAPPls.toFront();
        if (tglBtnAPMns.isSelected()) ListViewAPMns.toFront();
        if (tglBtnRPPls.isSelected()) ListViewRPPls.toFront();
        if (tglBtnRPMns.isSelected()) ListViewRPMns.toFront();
    }

    //Обновление списка методик после добавления методики
    public void refreshMethodicList() {
        metodicsNameList = FXCollections.observableArrayList(metodicsForTest.getMethodicForStends());

        initMethodicListName();
    }

    //Обновление списка после удаления методики
    public void refreshAfterDelete() {
        metodicsNameList = FXCollections.observableArrayList(metodicsForTest.getMethodicForStends());

        initMethodicListName();

        comandListAPPls.clear();
        comandListAPMns.clear();
        comandListRPPls.clear();
        comandListRPMns.clear();

        ListViewAPPls.setItems(FXCollections.observableArrayList(comandListAPPls));
        ListViewAPMns.setItems(FXCollections.observableArrayList(comandListAPMns));
        ListViewRPPls.setItems(FXCollections.observableArrayList(comandListRPPls));
        ListViewRPMns.setItems(FXCollections.observableArrayList(comandListRPMns));
    }

    public void addListenerForResizeFrame() {
        mainAnchorPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double x = paneWithEdits.getLayoutX() + (((Double) newValue - (Double) oldValue) / 2);
                paneWithEdits.setLayoutX(x);
            }
        });
    }

    public Button getEditMetBtn() {
        return editMetBtn;
    }

    @Override
    public Stage getStage() {
        return (Stage) addMetBtn.getScene().getWindow();
    }
}