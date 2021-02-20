package org.taipit.stend.controller.сommands;

import org.taipit.stend.helper.exeptions.StendConnectionException;

/**
 * @autor Albert Khalimov
 * Данный интерфейс отвечает за реализацию выполнения команд испытательного стенда для поверки счётчиков
 */

public interface Commands {

    //Однократное выполнение команды с переходом к следующей
    void execute() throws StendConnectionException, InterruptedException;

    //Постоянное выполнение одной команды в цикле без перехода к следующей
    void executeForContinuousTest() throws StendConnectionException, InterruptedException;

    //Устанавливает количество импульсов для расчёта погрешности
    void setPulse(String pulse);

    //Устанавливает количество результатов, которые необходимо получить при выполнении команды
    void setCountResult(String countResult);

    //Верхняя граница погрешности, при которой считается, что счётчик прошёл испытание
    void setEmax(String emax);
    //Нижняя граница погрешности, при которой считается, что счётчик прошёл испытание
    void setEmin(String emin);

    //Имя команды
    String getName();

    //Id команды
    String getId();

    //Выбрано ли точка испытания (необходимо ли выполнять данную команду из общей таблицы команд)
    boolean isActive();
    void setActive(boolean active);

    //Режим работы поверочного стенда подробнее в (StendDLL)
    void setPhase(int phase);

    Commands clone() throws CloneNotSupportedException;

    //Эта комадна будет выполнятся на трехфазном стенде
    boolean isThreePhaseCommand();

    //Устанавливает фазу на которой необходимо выполнить испытание
    String getiABC();
    //Устанавливает напряжение, которое необходимо подать
    double getRatedVolt();
    //Устанавливает ток, который необходимо подать
    double getRatedCurr();
    //Устанавливает напряжение на фазу А
    double getVoltPerA();
    //Устанавливает напряжение на фазу B
    double getVoltPerB();
    //Устанавливает напряжение на фазу C
    double getVoltPerC();
    //Устанавливает процент от тока (ratedCurr)
    double getCurrPer();
    //Устанавливает процент от напряжения (ratedVolt)
    double getVoltPer();
    //Время, которое необходимо чтобы выставить параметры (указанные выше) поверочному стенду
    void setPauseForStabilization(double pauseForStabilization);
}
