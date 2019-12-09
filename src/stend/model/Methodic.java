package stend.model;

import stend.controller.Commands.Commands;

import java.util.ArrayList;

public class Methodic {

    private String methodicName;
    private ArrayList<Commands> commandsList = new ArrayList<>();

    public Methodic(String methodicName) {
        this.methodicName = methodicName;
    }

    public void addCommandToList(Commands command) {
        commandsList.add(command);
    }
}
