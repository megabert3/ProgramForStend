package stend.model;

import stend.controller.Commands.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Methodic {

    private Map<Integer, List<Commands>> commandsMap = new HashMap<>(4);

    public Methodic() {
        commandsMap.put(0, new ArrayList<Commands>());
        commandsMap.put(1, new ArrayList<Commands>());
        commandsMap.put(2, new ArrayList<Commands>());
        commandsMap.put(3, new ArrayList<Commands>());
    }

    private String methodicName;

    public boolean addCommandToList(Integer numb, ArrayList<Commands> list) {
        if (numb > 4 || numb < 0) return false;
        else {
            commandsMap.get(numb).addAll(list);
        }
        return true;
    }

    public Map<Integer, List<Commands>> getCommandsMap() {
        return commandsMap;
    }

    public String getMethodicName() {
        return methodicName;
    }

    public void setMethodicName(String methodicName) {
        this.methodicName = methodicName;
    }
}