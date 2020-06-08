package org.taipit.stend.controller.Commands;

import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

public interface Commands {

    //Выполнение команды
    void execute() throws ConnectForStendExeption, InterruptedException;

    void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException;

    void setPulse(String pulse);

    void setCountResult(String countResult);

    void setEmax(String emax);

    void setEmin(String emin);

    //Имя команды
    String getName();

    String getId();

    //Выбрано ли точка испытания
    boolean isActive();
    void setActive(boolean active);

    void setPhase(int phase);

    Commands clone() throws CloneNotSupportedException;
}
