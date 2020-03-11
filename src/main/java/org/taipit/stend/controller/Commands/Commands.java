package org.taipit.stend.controller.Commands;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.util.List;

public interface Commands{

    //Выполнение команды
    void execute() throws InterruptedTestException, ConnectForStendExeption;

    void executeForContinuousTest() throws InterruptedTestException, ConnectForStendExeption;

    //Имя команды
    String getName();

    //Выбрано ли точка испытания
    boolean isActive();
    void setActive(boolean active);
}
