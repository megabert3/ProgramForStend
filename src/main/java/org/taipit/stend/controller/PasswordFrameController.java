package org.taipit.stend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.taipit.stend.helper.ConsoleHelper;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordFrameController {

    @FXML
    private TextField passwordField;

    @FXML
    private Button acceptPasswordBtn;

    @FXML
    void actinonForPaswordFrame(ActionEvent event) {

        if (event.getSource() == acceptPasswordBtn) {
            MessageDigest digester = null;
            try {
                digester = MessageDigest.getInstance("SHA-512");
            } catch (NoSuchAlgorithmException ignored) {
            }

            //Генерирую хэш из пароля
            String digest = DatatypeConverter.printHexBinary(digester.digest(passwordField.getText().getBytes()));

            if (digest.equals(ConsoleHelper.properties.getProperty("config"))) {
                ConsoleHelper.setPassword(true);
                Stage stage = (Stage) acceptPasswordBtn.getScene().getWindow();
                stage.close();
            } else {
                passwordField.setStyle("-fx-border-color: red; -fx-focus-color: red;");
                ConsoleHelper.infoException("Неверный пароль");
            }
        }
    }

    @FXML
    void initialize() {
    }
}
