package org.taipit.stend.controller.Commands;

import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

public interface Commands{

    //Выполнение команды
    boolean execute() throws ConnectForStendExeption, InterruptedException;

    void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException;

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
