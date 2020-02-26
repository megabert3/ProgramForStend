package org.taipit.stend.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("viewFXML/mainFrame.fxml"));
        primaryStage.setTitle("НЕВА СТЕНД");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}