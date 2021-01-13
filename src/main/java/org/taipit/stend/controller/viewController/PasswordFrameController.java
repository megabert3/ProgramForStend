package org.taipit.stend.controller.viewController;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.taipit.stend.helper.ConsoleHelper;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @autor Albert Khalimov
 *
 * Данный класс является контроллером окна "passwordFrame.fxml"
 *
 * Отвечает за сверку введённого пароля пользователем и в случае чего блокирует или предоставляет доступ к следующему окну.
 */
public class PasswordFrameController {

    @FXML
    PasswordField passwordField;

    @FXML
    private Button acceptPasswordBtn;

    @FXML
    void actinonForPaswordFrame(ActionEvent event) {

        //Если пользователь нажал кнопку ввести пароль
        if (event.getSource() == acceptPasswordBtn) {
            MessageDigest digester = null;
            try {
                digester = MessageDigest.getInstance("SHA-512");
            } catch (NoSuchAlgorithmException ignored) {
            }

            //Генерирую хэш из пароля
            String digest = DatatypeConverter.printHexBinary(digester.digest(passwordField.getText().getBytes()));

            //Сравниваю полученный хэш пальзовательского пароля с установленным в программе
            if (digest.equals(ConsoleHelper.properties.getProperty("config"))) {
                ConsoleHelper.setPassword(true);
                Stage stage = (Stage) acceptPasswordBtn.getScene().getWindow();
                stage.close();

                //Если пароли не совпадают
            } else {
                passwordField.setStyle("-fx-border-color: red; -fx-focus-color: red;");
                ConsoleHelper.infoException("Неверный пароль");
            }
        }
    }

    @FXML
    void initialize() {
        passwordField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                acceptPasswordBtn.fire();
            }
        });
    }
}
