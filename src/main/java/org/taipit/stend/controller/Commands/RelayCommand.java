package org.taipit.stend.controller.Commands;

import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

public class RelayCommand implements Commands {

    @Override
    public void execute() throws ConnectForStendExeption, InterruptedException {

    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {

    }

    @Override
    public void setPulse(String pulse) {

    }

    @Override
    public void setCountResult(String countResult) {

    }

    @Override
    public void setEmax(String emax) {

    }

    @Override
    public void setEmin(String emin) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public void setPhase(int phase) {

    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return null;
    }

    @Override
    public boolean isThreePhaseCommand() {
        return false;
    }

    @Override
    public String getiABC() {
        return null;
    }

    @Override
    public double getRatedVolt() {
        return 0;
    }

    @Override
    public double getRatedCurr() {
        return 0;
    }

    @Override
    public double getVoltPerA() {
        return 0;
    }

    @Override
    public double getVoltPerB() {
        return 0;
    }

    @Override
    public double getVoltPerC() {
        return 0;
    }

    @Override
    public double getCurrPer() {
        return 0;
    }

    @Override
    public double getVoltPer() {
        return 0;
    }
}
