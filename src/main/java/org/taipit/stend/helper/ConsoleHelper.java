package org.taipit.stend.helper;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.controller.PasswordFrameController;
import org.taipit.stend.controller.viewController.ExceptionFrameController;
import org.taipit.stend.controller.viewController.YesOrNoFrameControllerDialog;

import java.io.*;
import java.util.Properties;

public class ConsoleHelper {

    //Для диалогового окна да или нет
    private static Boolean yesOrNo;
    private static boolean password = false;

    //Директроия с файлом пропертиес
    private static final String dir = ".\\src\\main\\resources\\stendProperties.properties";

    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static Properties properties = getProperties();

    private static Properties getProperties() {
        Properties initProperties = new Properties();
        try {
            initProperties.load(new FileInputStream(new File(dir).getCanonicalPath()));
        } catch (IOException e) {
            ConsoleHelper.infoException("Указанный файл properties не найден");
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

    public static void infoException(String title, String mess) {
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
                stage.setTitle(title);
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);

                stage.show();
            }
        });
    }

    public static Boolean yesOrNoFrame(String title, String textQuestion, double x, double y) {

        yesOrNo = null;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ConsoleHelper.class.getResource("/viewFXML/yesOrNoFrameDialog.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        YesOrNoFrameControllerDialog yesOrNoFrameController = fxmlLoader.getController();
        yesOrNoFrameController.getQuestionTxt().setText(textQuestion);
        yesOrNoFrameController.getQuestionTxt().setLayoutX(x);
        yesOrNoFrameController.getQuestionTxt().setLayoutY(y);

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return yesOrNo;
    }

    public static Boolean yesOrNoFrame(String title, String textQuestion) {

        yesOrNo = null;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ConsoleHelper.class.getResource("/viewFXML/yesOrNoFrameDialog.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        YesOrNoFrameControllerDialog yesOrNoFrameController = fxmlLoader.getController();
        yesOrNoFrameController.getQuestionTxt().setText(textQuestion);

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return yesOrNo;
    }

    public static Object loadFrame(boolean modality, String path, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ConsoleHelper.class.getResource(path));
        try {
            fxmlLoader.load();
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке окна");
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(fxmlLoader.getRoot()));

        return fxmlLoader.getController();
    }

    public static Boolean passwordFrame() {
        password = false;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ConsoleHelper.class.getResource("/viewFXML/passwordFrame.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            exceptionLoadFrame();
            e.printStackTrace();
        }

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Пароль");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return password;
    }

    public static void exceptionLoadFrame() {
        infoException("Ошибка при загрузке окна\n" +
                "проверьте целостность файлов программы\n" +
                "или попробуйте снова");
    }


    public static void getMessage(String mess) {
        System.out.println(mess);
    }

    public static String entString() throws IOException {
        return bufferedReader.readLine();
    }

    public static void setYesOrNo(Boolean yesOrNo) {
        ConsoleHelper.yesOrNo = yesOrNo;
    }

    public static void setPassword(boolean password) {
        ConsoleHelper.password = password;
    }
}