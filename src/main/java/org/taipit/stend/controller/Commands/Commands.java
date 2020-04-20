package org.taipit.stend.controller.Commands;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.util.List;

public interface Commands{

    //Выполнение команды
    boolean execute() throws ConnectForStendExeption;

    void executeForContinuousTest() throws ConnectForStendExeption;

    void setInterrupt(boolean interrupt);

    void setNextCommand(boolean nextCommand);

    void setPulse(String pulse);

    void setCountResult(String countResult);

    void setEmax(String emax);

    void setEmin(String emin);

    //Имя команды
    String getName();

    //Выбрано ли точка испытания
    boolean isActive();
    void setActive(boolean active);
}
