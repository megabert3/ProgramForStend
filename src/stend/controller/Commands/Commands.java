package stend.controller.Commands;

public interface Commands{
    //Прерывает работу метода
    void setInterrupt(boolean interrupt);

    //Выполнение команды
    void execute();

    //Имя команды
    String getName();

    //Выбрано ли точка испытания
    boolean isActive();
    void setActive(boolean active);
}
