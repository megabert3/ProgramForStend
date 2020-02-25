package org.taipit.stend.controller.Commands;


import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;

public class ConstantCommand implements Commands, Serializable {

    //Команда для прерывания метода
    private boolean interrupt;

    //Включает или выключает в автоматическом режиме точку
    private boolean active = true;

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
}
