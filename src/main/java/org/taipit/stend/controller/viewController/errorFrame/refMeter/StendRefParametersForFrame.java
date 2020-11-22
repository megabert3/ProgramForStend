package org.taipit.stend.controller.viewController.errorFrame.refMeter;

import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.model.stend.StendDLLCommands;

/**
 * @autor Albert Khalimov
 *
 * Данный интерфейс отвечает за передачу и отображение параметров полученных от эталонного счётчика в GUI"
 */
public interface StendRefParametersForFrame {

    /**
     * Передвигает окно параметров эталонного счётчика
     * по нажатию мышки на него
     */
    void addMovingActions();

    /**
     * Инициализирует тип эталонного счётчика установк
     * от этого зависит какой порядок параметров передаёт стенд и в каком порядке
     * их необходимо отображать в GUI
     * @param stendDLLCommands
     */
    void initRefType(StendDLLCommands stendDLLCommands);

    /**
     * Получает значения параметров от эталонного счётчика
     * парсит их и выводит в GUI
     * @throws InterruptedException
     */
    void readParameters() throws InterruptedException;


    void transferParameters(Commands command);
    void readParametersWithoutCheckingParan();


    StendRefParametersForFrame getStendRefParametersForFrame();
}
