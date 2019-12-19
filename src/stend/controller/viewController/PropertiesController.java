package stend.controller.viewController;

import com.sun.javafx.scene.control.skin.SpinnerSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import stend.controller.StendDLLCommands;
import stend.model.StendDLL;


import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    private ObservableList<String> list = FXCollections.observableArrayList();
//----------------------------------------------------------- Menu
    @FXML
    private Button passwordBtn;

    @FXML
    private Button stendBtn;

    @FXML
    private Button demoBtn;

//----------------------------------------------------------- stendPane
    @FXML
    private AnchorPane stendPane;

    @FXML
    private ChoiceBox<String> standPaneCOMList;

    @FXML
    private ChoiceBox<String> stendPaneStendTypeList;

    @FXML
    private ChoiceBox<String> stendPaneStendModel;

    @FXML
    private TextField standPaneAmoutPlase;

    @FXML
    private ChoiceBox<String> stendPaneRefMetModel;

    @FXML
    private Button stendPaneSave;

//----------------------------------------------------------- passwordPane
    @FXML
    private AnchorPane passwordPane;

//----------------------------------------------------------- demoPane
    @FXML
    private AnchorPane demoPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties stendProperties = getProperties();
        for (Map.Entry<Object, Object> o : stendProperties.entrySet()) {
            System.out.println(o.getKey() + " = " + o.getValue());
        }
        //Тип установки (одно/трех фазная)
        stendPaneStendTypeList.getItems().addAll("Однофазный", "Трехфазный");

        stendPaneStendTypeList.getSelectionModel().select(stendProperties.getProperty("stendType"));

        //Список ком портов
        standPaneCOMList.getItems().addAll(FXCollections.observableArrayList(StendDLLCommands.massPort()));

    }

    public void handleClicks(javafx.event.ActionEvent actionEvent) {
        //Переключение на вкладку Пароль
        if (actionEvent.getSource() == passwordBtn) {
            passwordPane.toFront();
        }

        //Переключение на вкладку Установка
        if (actionEvent.getSource() == stendBtn) {
            stendPane.toFront();
        }

        //Переключение на вкладку Демо
        if (actionEvent.getSource() == demoBtn) {
            demoPane.toFront();
        }
        if (actionEvent.getSource() == stendPaneSave) {

        }
    }

    //Загружает выбранные настройки
    private Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("stendProperties.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}