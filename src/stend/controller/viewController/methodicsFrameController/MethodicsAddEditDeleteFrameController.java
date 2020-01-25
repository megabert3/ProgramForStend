package stend.controller.viewController.methodicsFrameController;

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

import stend.controller.Commands.Commands;
import stend.controller.viewController.methodicsFrameController.addEditFraneController.AddEditFrameController;
import stend.helper.exeptions.InfoExeption;
import stend.model.Methodic;
import stend.model.MethodicsForTest;


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


    //Имитацию TglGroup
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

    public TableView<Methodic> getViewPointTable() {
        return viewPointTable;
    }

    @FXML
    void initialize() throws InfoExeption {
        methodicsForTest.addMethodicToList("Test1", new Methodic());
        methodicsForTest.addMethodicToList("Test2", new Methodic());
        methodicsForTest.addMethodicToList("Test3", new Methodic());
        metodicsNameList = FXCollections.observableArrayList(methodicsForTest.getMethodics());

        tglBtnAPPls.setSelected(true);

        initMethodicListName();

    }

    @FXML
    void actinonForMethodicsFrame(ActionEvent event) {
        if (event.getSource() == addMetBtn) {
            loadStageWithController("/stend/view/method/metodicName.fxml", "Имя методики методики");
        }

        if (event.getSource() == editMetBtn) {
            if (focusedMetodic == null) {
                System.out.println("Выберите методику");
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/stend/view/method/addEditMet.fxml"));
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
            addEditFrameController.initEditsMetodic();
            addEditFrameController.addTestPointsOnGreedPane();
            addEditFrameController.setTextFielMethodicName();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
    }

    private void initMethodicListName() {
        tabClMethodics.setCellValueFactory(new PropertyValueFactory<>("methodicName"));

        viewPointTable.setItems(metodicsNameList);

        viewPointTable.setPlaceholder(new Label("У вас не создано ни одоной методики"));

        metodicsNameList = viewPointTable.getSelectionModel().getSelectedItems();


        metodicsNameList.addListener(new ListChangeListener<Methodic>() {
            @Override
            public void onChanged(Change<? extends Methodic> c) {
                tglBtnAPPls.setSelected(true);
                focusedMetodic = c.getList().get(0);

                setListsView();

                ListViewAPPls.toFront();
            }
        });
    }

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
        ListViewRPPls.setItems(FXCollections.observableArrayList(comandListRPMns));
        ListViewRPMns.setItems(FXCollections.observableArrayList(comandListRPMns));
    }

    private void toFront() {
        if (tglBtnAPPls.isSelected()) ListViewAPPls.toFront();
        if (tglBtnAPMns.isSelected()) ListViewAPMns.toFront();
        if (tglBtnRPPls.isSelected()) ListViewRPPls.toFront();
        if (tglBtnRPMns.isSelected()) ListViewRPMns.toFront();
    }


    private void loadStageWithController(String fxml, String stageName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxml));
            fxmlLoader.load();
            Parent root = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setTitle(stageName);
            stage.setScene(new Scene(root));

            methodicNameController = fxmlLoader.getController();
            methodicNameController.setMethodicsAddEditDeleteFrameController(this);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
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