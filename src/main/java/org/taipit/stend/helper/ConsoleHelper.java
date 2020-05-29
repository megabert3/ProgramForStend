package org.taipit.stend.helper;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.taipit.stend.controller.viewController.ExceptionFrameController;
import org.taipit.stend.controller.viewController.YesOrNoFrameController;

import java.io.*;
import java.util.Properties;

public class ConsoleHelper {

    //Директроия с файлом пропертиес
    private static final String dir = ".\\src\\main\\resources\\stendProperties.properties";

    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static Properties properties = getProperties();

    private static Properties getProperties() {
        Properties initProperties = new Properties();
        try {
            initProperties.load(new FileInputStream(new File(dir).getCanonicalPath()));
        } catch (IOException e) {
            System.out.println("Указанный файл properties не найден");
            e.printStackTrace();
        }
        return initProperties;
    }

    public static boolean saveProperties() {

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(dir).getCanonicalPath())) {
            properties.store(fileOutputStream, "My comments");

            properties = getProperties();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void infoException(String mess) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(ConsoleHelper.class.getResource("/viewFXML/exceptionFrame.fxml"));
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ExceptionFrameController exceptionFrameController = fxmlLoader.getController();
                exceptionFrameController.getLabel().setText(mess);

                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Ошибка");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);

                stage.show();
            }
        });
    }

    public static boolean yesOrNoFrame(String title, ) {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ConsoleHelper.class.getResource("/viewFXML/yesOrNoFrame.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        YesOrNoFrameController yesOrNoFrameController = fxmlLoader.getController();
        yesOrNoFrameController.setExitSaveResultFrameWithoutSaving(true);
        yesOrNoFrameController.setStageSaveResultTest(stage);
        yesOrNoFrameController.getQuestionTxt().setText("Вы уверены, что хотите выйти \nбез сохранения результатов теста?");
        yesOrNoFrameController.getQuestionTxt().setLayoutX(165);
        yesOrNoFrameController.getQuestionTxt().setLayoutY(30);

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return null;
    }


    public static void getMessage(String mess) {
        System.out.println(mess);
    }

    public static String entString() throws IOException {
        return bufferedReader.readLine();
    }
}