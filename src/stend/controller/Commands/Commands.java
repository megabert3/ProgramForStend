package stend.controller.Commands;

import stend.helper.exeptions.ConnectForStendExeption;
import stend.helper.exeptions.InterruptedTestException;

public interface Commands{
    //Прерывает работу метода
    void setInterrupt(boolean interrupt);

    //Выполнение команды
    void execute() throws InterruptedTestException, ConnectForStendExeption, InterruptedException;

    //Имя команды
    String getName();

    //Выбрано ли точка испытания
    boolean isActive();
    void setActive(boolean active);
}
