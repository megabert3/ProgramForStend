package stend.controller.viewController.methodicsFrameController;

import java.io.IOException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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
import stend.controller.Commands.ErrorCommand;
import stend.helper.exeptions.InfoExeption;
import stend.model.Methodic;
import stend.model.MethodicsForTest;


public class MethodicsAddEditDeleteFrameController {

    private MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    private ObservableList<Methodic> metodicsNameList = FXCollections.observableArrayList(new ArrayList<>());

    @FXML
    private Button copyMetBtn;

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
    private ListView<String> ListViewAPMns;

    @FXML
    private ListView<String> ListViewAPPls;

    @FXML
    private ListView<String> ListViewRPPls;

    @FXML
    private ListView<String> ListViewRPMns;

    @FXML
    private Button editMetBtn;

    @FXML
    void tglBtnsEnegyViewAction(ActionEvent event) {
        if (event.getSource() == tglBtnAPPls) {
            tglBtnAPPls.setSelected(true);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
            ListViewAPPls.toFront();
        }

        if (event.getSource() == tglBtnAPMns) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(true);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(false);
            ListViewAPMns.toFront();
        }

        if (event.getSource() == tglBtnRPPls) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(true);
            tglBtnRPMns.setSelected(false);
            ListViewRPPls.toFront();
        }

        if (event.getSource() == tglBtnRPMns) {
            tglBtnAPPls.setSelected(false);
            tglBtnAPMns.setSelected(false);
            tglBtnRPPls.setSelected(false);
            tglBtnRPMns.setSelected(true);
            ListViewRPMns.toFront();
        }
    }

    @FXML
    void initialize() throws InfoExeption {
        methodicsForTest.addMethodicListToMap("Test1", new Methodic());
        methodicsForTest.addMethodicListToMap("Test2", new Methodic());
        methodicsForTest.addMethodicListToMap("Test3", new Methodic());

        methodicsForTest.getMethodicsMap().get("Test1").setMethodicName("Test1");
        methodicsForTest.getMethodicsMap().get("Test2").setMethodicName("Test2");
        methodicsForTest.getMethodicsMap().get("Test3").setMethodicName("Test3");
        initMethodicListName();

        tabClMethodics.setCellValueFactory(new PropertyValueFactory<>("methodicName"));

        viewPointTable.setItems(metodicsNameList);

        Methodic selectedBook = viewPointTable.getSelectionModel().getSelectedItem();

        tabClMethodics.setOnEditCommit((TableColumn.CellEditEvent<Methodic, String> event) -> {
            TablePosition<Methodic, String> pos = event.getTablePosition();

            Methodic selectedBook1 = viewPointTable.getSelectionModel().getSelectedItem();

            System.out.println(selectedBook1);

            System.out.println(pos);

            //String newImpulseValue = event.getNewValue();

            int row = pos.getRow();

            System.out.println(row);

            Methodic methodic = event.getTableView().getItems().get(row);

            System.out.println(methodic.getMethodicName());

            //((ErrorCommand) command).setEmax(newImpulseValue);

        });

        for (Methodic met : metodicsNameList) {
            System.out.println(met.getMethodicName());
        }

    }

    @FXML
    void actinonForMethodicsFrame(ActionEvent event) {
        if (event.getSource() == addMetBtn) {
            loadStage("/stend/view/method/metodicName.fxml", "Имя методики методики");
        }
    }

    private void initMethodicListName() {
        metodicsNameList.clear();

        for (Map.Entry<String, Methodic> keyNames : methodicsForTest.getMethodicsMap().entrySet()) {
            metodicsNameList.add(keyNames.getValue());
        }
    }


    private void loadStage(String fxml, String stageName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(stageName);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}