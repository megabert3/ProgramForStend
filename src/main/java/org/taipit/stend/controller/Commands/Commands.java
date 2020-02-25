package org.taipit.stend.controller.Commands;

import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

public interface Commands{
    //Прерывает работу метода
    void setInterrupt(boolean interrupt);

    //Выполнение команды
    void execute() throws InterruptedTestException, ConnectForStendExeption, InterruptedException;

    void executeForContinuousTest() throws InterruptedTestException, ConnectForStendExeption, InterruptedException;

    //Имя команды
    String getName();

    //Выбрано ли точка испытания
    boolean isActive();
    void setActive(boolean active);
}
