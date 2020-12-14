package org.taipit.stend.helper;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.taipit.stend.controller.viewController.ExceptionFrameController;
import org.taipit.stend.controller.viewController.YesOrNoFrameControllerDialog;

import java.io.*;
import java.util.Properties;

/**
 * @autor Albert Khalimov
 *
 * Данный класс является вспомогательным и упрощает вывод служебной информации.
 */
public class ConsoleHelper {

    //Для диалогового окна да или нет (ответ)
    private static Boolean yesOrNo;

    //Для окна запроса пароля (правильный пароль или нет)
    private static boolean password = false;

    //Директроия файла настроек (properties)
    private static final String dir = ".\\src\\main\\resources\\stendProperties.properties";

    //Для считывания информации с консоли (использовался для тестов)
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    //Файл с настройками программы
    public static Properties properties = getProperties();

    /**
     * Возвращает объект файла с настройками
     * @return
     */
    private static Properties getProperties() {

        Properties initProperties = new Properties();

        try {
            initProperties.load(new FileInputStream(new File(dir).getCanonicalPath()));
        } catch (IOException e) {
            ConsoleHelper.infoException("Файл с настройками программы не найден.\nПроверте целостность программы или переустановите её и попробуйте снова");
        }

        return initProperties;
    }

    /**
     * Сохраняет все изменения произведенённые в файле
     * @return
     */
    public static boolean saveProperties() {

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(dir).getCanonicalPath())) {
            properties.store(fileOutputStream, "My comments");

            properties = getProperties();
            return true;
        } catch (IOException e) {
            infoException(e.getMessage());
            return false;
        }
    }

    /**
     * Выводит информационное окно с ошибкой (аналог диалог. сообщения)
     * @param mess
     */
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

    /**
     * Выводит информационное окно с возможностью редактировать загловок (аналог диалог. сообщения)
     * @param mess
     */
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

    /**
     * Выводит диалоговое окно (да/нет) с запросом и регулировкой отобращения сообщения(аналог диалог. сообщения)
     * @param title
     * @param textQuestion
     * @param x - местополжение сообщения по оси X
     * @param y - местополжение сообщения по оси Y
     * @return
     */
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

    /**
     * Выводит диалоговое окно (да/нет) с запросом (аналог диалог. сообщения)
     * @param title
     * @param textQuestion
     * @return
     */
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

    /**
     * Метод апредназначенный для загрузки окна (Не используется)
     * @param modality
     * @param path
     * @param title
     * @return
     */
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

    /**
     * Загрущает окно ввода пароля и проверяет его валидность
     * @return
     */
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

    /**
     * Сообщение об ощибке возникающей при загрузке окна
     */
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