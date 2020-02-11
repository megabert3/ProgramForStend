package stend.controller.Commands;


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
