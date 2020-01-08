package stend.model;

import stend.controller.Commands.Commands;
import stend.controller.Commands.ErrorCommand;
import stend.controller.ThreePhaseStend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Methodic {

    public static Map<Integer, List<Commands>> commandsMap = new HashMap<>(4);

    private static List<Commands> activeEnergyDirect = new ArrayList<>();
    private static List<Commands> activeEnergyRevers = new ArrayList<>();
    private static List<Commands> reactiveEnergyDirect = new ArrayList<>();
    private static List<Commands> reactiveEnergyReverse = new ArrayList<>();

    static {
        //activeEnergyDirect.add(new ErrorCommand(ThreePhaseStend.getThreePhaseStendInstance(), 3, "Ib", 0, "df", "dsf", "fdf", 0));
        commandsMap.put(0, activeEnergyDirect);
        commandsMap.put(1, activeEnergyRevers);
        commandsMap.put(2, reactiveEnergyDirect);
        commandsMap.put(3, reactiveEnergyReverse);
    }

    private String methodicName;


    public void addCommandToList(Integer numb, Commands command) {
        commandsMap.get(numb).add(command);
    }

    public String getMethodicName() {
        return methodicName;
    }

    public void setMethodicName(String methodicName) {
        this.methodicName = methodicName;
    }
}