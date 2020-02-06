package stend.controller.Commands;


import java.io.Serializable;

public class ConstantCommand implements Commands, Serializable {

    //Включает или выключает в автоматическом режиме точку
    private boolean active = true;

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
