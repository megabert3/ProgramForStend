package org.taipit.stend.controller.Commands;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.util.List;

public class ConstantCommand implements Commands, Serializable {

    //Команда для прерывания метода
    private boolean interrupt;

    //Включает или выключает в автоматическом режиме точку
    private boolean active = true;

    //Лист со столбами счётчикв для изменения флага и цвета
    private List<TableColumn<Meter.CommandResult, String>> tableColumnError;

    @Override
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void executeForContinuousTest() throws InterruptedTestException, ConnectForStendExeption, InterruptedException {

    }

    @Override
    public void execute() {

    }

    @Override
    public String getName() {
        return null;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTableColumnError(List<TableColumn<Meter.CommandResult, String>> tableColumnError) {
        this.tableColumnError = tableColumnError;
    }
}
