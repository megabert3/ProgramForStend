package stend.model;

import stend.controller.Commands.Commands;

import java.util.HashMap;

public class Methodic {

    private String methodicName;
    private HashMap<Integer, Commands> commandsList = new HashMap<>();

    public Methodic(String methodicName) {
        this.methodicName = methodicName;
    }

    public void addCommandToList(Integer numb, Commands command) {
        commandsList.put(numb, command);
    }
}