package org.taipit.stend.controller.viewController;


import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.frameManager.FrameManager;
import org.taipit.stend.model.ExcelReport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна сохранения отчёта "printResultsFrame.fxml".
 * Отвечает за задание директории и имени файла с сохранёнными результатами
 */
public class PrintResultsController {

    //Результаты счётчиков, которые необходимо вывести
    private List<Meter> meterResultSave;

    //Окно вызова
    private Window window;

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
            fileNameTxtFld.setStyle("");

            Path path = Paths.get(dirSaveFileTxtFld.getText());
            File reportFile = new File(dirSaveFileTxtFld.getText());

            if (fileNameTxtFld.getText().trim().isEmpty()) {
                ConsoleHelper.infoException("Название файла не должно быть пустым");
                fileNameTxtFld.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                return;
            }

            if (reportFile.isFile()) {
                ConsoleHelper.infoException("Необходимо выбрать директорию, а не файл");
                dirSaveFileTxtFld.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
                return;
            }

            if (Files.isDirectory(path)) {

                String pathToFile = dirSaveFileTxtFld.getText() + "\\" + fileNameTxtFld.getText();

                if (ConsoleHelper.properties.getProperty("reportType").equals("HSSF")) {
                    pathToFile += ".xls";
                } else {
                    pathToFile += ".xlsx";
                }

                reportFile = new File(pathToFile);

                if (reportFile.exists()) {
                    Boolean answer = ConsoleHelper.yesOrNoFrame("Перезапись файла", "Файл под названием \"" + fileNameTxtFld.getText() + "\" уже существует,\nпересохранить его?");

                    if (answer != null && answer) {
                        properties.setProperty("printReportPath", dirSaveFileTxtFld.getText());
                        properties.setProperty("printReportName", fileNameTxtFld.getText());

                        ConsoleHelper.saveProperties();

                        ExcelReport excelReport = new ExcelReport();

                        if (excelReport.createExcelReport(meterResultSave, window)) {
                            excelReport.openExcelReport();
                            FrameManager.frameManagerInstance().selectCancelInPrintResultFrame = true;
                            Stage thisStage = (Stage) saveBtn.getScene().getWindow();
                            thisStage.close();
                        }
                    }
                } else {
                    properties.setProperty("printReportPath", dirSaveFileTxtFld.getText());
                    properties.setProperty("printReportName", fileNameTxtFld.getText());

                    ConsoleHelper.saveProperties();

                    ExcelReport excelReport = new ExcelReport();

                    if (excelReport.createExcelReport(meterResultSave, window)) {
                        excelReport.openExcelReport();
                        FrameManager.frameManagerInstance().selectCancelInPrintResultFrame = true;
                        Stage thisStage = (Stage) saveBtn.getScene().getWindow();
                        thisStage.close();
                    }
                }

            } else {
                ConsoleHelper.infoException("Указанной директории не существует.\nВыберите директорию для сохранения файла");
                dirSaveFileTxtFld.setStyle("-fx-border-color: red ; -fx-focus-color: red ;");
            }

        } else if (e.getSource() == cancelBtn) {
            FrameManager.selectCancelInPrintResultFrame = false;
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();

        } else if (e.getSource() == choiseDirBtn) {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Сохранение файла");

            File dirFile = new File(ConsoleHelper.properties.getProperty("printReportPath"));
            if (dirFile.isDirectory()) {
                directoryChooser.setInitialDirectory(dirFile);
            } else {
                directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            }

            File dir = directoryChooser.showDialog(choiseDirBtn.getScene().getWindow());

            if (dir != null) {
                dirSaveFileTxtFld.setText(dir.getAbsolutePath());
            }
        }
    }

    public void setMeterResultSave(List<Meter> meterResultSave) {
        this.meterResultSave = meterResultSave;
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}
