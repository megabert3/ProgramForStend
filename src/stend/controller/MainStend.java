package stend.controller;


import stend.helper.ConsoleHelper;

import java.util.Arrays;

public class MainStend {
    public static void main(String[] args) {
        StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
        ErrorCommand errorCommand = new ErrorCommand(stendDLLCommands, 1, 230.0, 5.0, 50.0, 0,
                0, 100.0, 100.0, "H", "1.0", 0, 10);
        try {
            errorCommand.execute();
            for (double[] errMass : errorCommand.getErrorList().values()) {
                ConsoleHelper.getMessage(Arrays.toString(errMass));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
