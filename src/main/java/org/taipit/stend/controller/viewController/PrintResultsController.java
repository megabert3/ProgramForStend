package org.taipit.stend.controller.viewController;


import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.ExcelReport;

import java.awt.event.ActionEvent;
import java.io.File;
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

            dirSaveFileTxtFld.setStyle("");

            Path path = Paths.get(dirSaveFileTxtFld.getText());
            File reportFile = new File(dirSaveFileTxtFld.getText());

            if (reportFile.isFile()) {
                ConsoleHelper.infoException("Необходимо выбрать директорию, а не файл");
                dirSaveFileTxtFld.setStyle("-fx-text-box-border: red;  -fx-focus-color: red;");
                return;
            }

            if (Files.exists(path)) {

                if (Files.isDirectory(path)) {
                    reportFile = new File(dirSaveFileTxtFld.getText() + "\\" + fileNameTxtFld.getText());

                    if (reportFile.exists()) {
                        Boolean answer = ConsoleHelper.yesOrNoFrame("Перезапись файла","Файл под названием \"" + fileNameTxtFld.getText() + "\" уже существует,\nпересохранить его?" );

                        if (answer != null && !answer) {
                            properties.setProperty("printReportPath", dirSaveFileTxtFld.getText());
                            properties.setProperty("printReportName", fileNameTxtFld.getText());

                            ExcelReport excelReport = new ExcelReport();

                            if (excelReport.createExcelReport(helpList, saveAndPrint.getScene().getWindow())) {
                                excelReport.openExcelReport();
                            } else return;
                        }
                    }

                } else {
                    ConsoleHelper.infoException("Указанный путь не является директорией");
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

            File reortFile = new File();



        } else if (e.getSource() == cancelBtn) {

        } else if (e.getSource() == choiseDirBtn) {

        }
    }
}
