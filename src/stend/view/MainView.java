package stend.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stend.model.Methodic;
import stend.model.MethodicsForTest;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainFrame.fxml"));
        primaryStage.setTitle("НЕВА СТЕНД");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        MethodicsForTest methodicsForTest = MethodicsForTest.getMethodicsForTestInstance();

    }
}