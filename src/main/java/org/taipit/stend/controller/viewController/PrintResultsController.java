package org.taipit.stend.controller.viewController;


import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.taipit.stend.helper.ConsoleHelper;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PrintResultsController {
    /**
     * @autor Albert Khalimov
     * Данный класс является контроллером окна сохранения отчёта "printResultsFrame.fxml".
     */

    @FXML
    private TextField fileNameTxtFld;

    @FXML
    private TextField dirSaveFileTxtFld;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button choiseDirBtn;

    private Properties properties = ConsoleHelper.properties;

    @FXML
    void initialize() {
        fileNameTxtFld.setText(properties.getProperty("printReportName"));
        dirSaveFileTxtFld.setText(properties.getProperty("printReportPath"));
    }

    @FXML
    void saveCancelChoiseDirAction(ActionEvent e) {

        if (e.getSource() == saveBtn) {

            Path path = Paths.get(dirSaveFileTxtFld.getText());

            if (Files.exists(path)) {

                if (Files.isDirectory(path)) {

                } else {
                    ConsoleHelper.infoException("Ошибка", "Указанный путь не является директорией");
                }
            } else {
                Boolean answer = ConsoleHelper.yesOrNoFrame("Создание директории","Указанной директории не существует.\nСоздать её?");

                if (answer != null) {
                    if (answer) {

                        try {
                            Files.createDirectories(path);
                        } catch (IOException ex) {
                            ConsoleHelper.infoException("При создании директории произошла ошибка\n" + ex.getMessage());
                        }
                    }
                }
            }



        } else if (e.getSource() == cancelBtn) {

        } else if (e.getSource() == choiseDirBtn) {

        }
    }
}
