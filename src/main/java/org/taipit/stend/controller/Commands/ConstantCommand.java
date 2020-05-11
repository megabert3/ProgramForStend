package org.taipit.stend.controller.Commands;

import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;

public class ConstantCommand implements Commands, Serializable {

    //Включает или выключает в автоматическом режиме точку
    private boolean active = true;

    //Эта команда из методики для трёхфазного теста?
    private boolean threePhaseCommand;

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption {

    }

    @Override
    public void setInterrupt(boolean interrupt) {

    }

    @Override
    public void setNextCommand(boolean nextCommand) {

    }

    @Override
    public void setEmax(String emax) {

    }

    @Override
    public void setEmin(String emin) {

    }

    @Override
    public void setPulse(String pulse) {

    }

    @Override
    public void setCountResult(String countResult) {

    }

    @Override
    public boolean execute() {
        return Boolean.parseBoolean(null);
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
