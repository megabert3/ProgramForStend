package org.taipit.stend.controller.viewController.methodicsFrameController;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.controller.viewController.YesOrNoFrameController;
import org.taipit.stend.controller.viewController.methodicsFrameController.addEditFraneController.AddEditFrameController;
import org.taipit.stend.helper.exeptions.InfoExсeption;
import org.taipit.stend.model.Methodic;
import org.taipit.stend.model.MethodicsForTest;


public class MethodicsAddEditDeleteFrameController {

    private MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private ObservableList<Methodic> metodicsNameList = FXCollections.observableArrayList(methodicsForTest.getMethodics());

    //Выделенная методикка
    private Methodic focusedMetodic;

    //Листы для точек из методики
    private ArrayList<String> comandListAPPls = new ArrayList<>();
    private ArrayList<String> comandListAPMns = new ArrayList<>();
    private ArrayList<String> comandListRPPls = new ArrayList<>();
    private ArrayList<String> comandListRPMns = new ArrayList<>();

    private MethodicNameController methodicNameController;

    private AddEditFrameController addEditFrameController;

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
    private TableView<Methodic> viewPointTable = new TableView<>();

    @FXML
    private TableColumn<Methodic, String> tabClMethodics;

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
    void initialize() throws InfoExсeption {
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
            if (focusedMetodic == null) {
                System.out.println("Выберите методику");
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/viewFXML/methodics/addEditMet.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Редактирование методики");
            stage.setScene(new Scene(root));

            addEditFrameController = fxmlLoader.getController();
            addEditFrameController.setMethodic(focusedMetodic);
            addEditFrameController.setEdit(true);
            addEditFrameController.initEditsMetodic();
            //addEditFrameController.addTestPointsOnGreedPane();
            addEditFrameController.setMethodicsAddEditDeleteFrameController(this);
            addEditFrameController.setTextFielMethodicName();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            addEditFrameController.initAndBindUpperScrollPane();

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
            methodicNameController.setMethodic(focusedMetodic);
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
            yesOrNoFrameController.setMethodic(focusedMetodic);
            yesOrNoFrameController.getQuestionTxt().setText("Вы действительно желаете удалить\nметодику: " +
                    focusedMetodic.getMethodicName() + "?");
            yesOrNoFrameController.getQuestionTxt().setLayoutX(150);
            yesOrNoFrameController.getQuestionTxt().setLayoutY(30);
            yesOrNoFrameController.setMethodicsAddEditDeleteFrameController(this);

            stage.show();
        }
    }

    //Инициирует список методик
    private void initMethodicListName() {
        tabClMethodics.setCellValueFactory(new PropertyValueFactory<>("methodicName"));

        viewPointTable.setItems(metodicsNameList);

        viewPointTable.setPlaceholder(new Label("У вас не создано ни одной методики"));

        metodicsNameList = viewPointTable.getSelectionModel().getSelectedItems();

        metodicsNameList.addListener(new ListChangeListener<Methodic>() {
            @Override
            public void onChanged(Change<? extends Methodic> c) {
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

        for (Commands commandsAPMns : focusedMetodic.getCommandsMap().get(1)) {
            comandListAPMns.add(commandsAPMns.getName());
        }

        for (Commands commandsRPPls : focusedMetodic.getCommandsMap().get(2)) {
            comandListRPPls.add(commandsRPPls.getName());
        }

        for (Commands commandsRPMns : focusedMetodic.getCommandsMap().get(3)) {
            comandListRPMns.add(commandsRPMns.getName());
        }

        ListViewAPPls.setItems(FXCollections.observableArrayList(comandListAPPls));
        ListViewAPMns.setItems(FXCollections.observableArrayList(comandListAPMns));
        ListViewRPPls.setItems(FXCollections.observableArrayList(comandListRPPls));
        ListViewRPMns.setItems(FXCollections.observableArrayList(comandListRPMns));
    }

    public void setListsView(Methodic methodic) {
        focusedMetodic = methodic;

        comandListAPPls.clear();
        comandListAPMns.clear();
        comandListRPPls.clear();
        comandListRPMns.clear();

        for (Commands commandsAPPls : focusedMetodic.getCommandsMap().get(0)) {
            comandListAPPls.add(commandsAPPls.getName());
        }

        for (Commands commandsAPMns : focusedMetodic.getCommandsMap().get(1)) {
            comandListAPMns.add(commandsAPMns.getName());
        }

        for (Commands commandsRPPls : focusedMetodic.getCommandsMap().get(2)) {
            comandListRPPls.add(commandsRPPls.getName());
        }

        for (Commands commandsRPMns : focusedMetodic.getCommandsMap().get(3)) {
            comandListRPMns.add(commandsRPMns.getName());
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
        metodicsNameList = FXCollections.observableArrayList(methodicsForTest.getMethodics());

        initMethodicListName();
    }

    //Обновление списка после удаления методики
    public void refreshAfterDelete() {
        metodicsNameList = FXCollections.observableArrayList(methodicsForTest.getMethodics());

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


    private void loadStageWithController(String fxml, String stageName) {

    }

    private void loadStage(String fxml, String stageName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxml));
            fxmlLoader.load();
            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle(stageName);
            stage.setScene(new Scene(root));

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}