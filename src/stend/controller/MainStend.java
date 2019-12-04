package stend.controller;



import stend.controller.Commands.StartCommand;

import java.util.Map;

public class MainStend {
    public static void main(String[] args) {
        //Проверка ErrorCommand
//        StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
//        ErrorCommand errorCommand = new ErrorCommand(stendDLLCommands, 1, 230.0, 5.0, 50.0, 0,
//                0, 100.0, 100.0, "H", "1.0", 0, 10);
//
//        try {
//            errorCommand.execute();
//            for (Map.Entry<Integer, double[]> map : errorCommand.getErrorList().entrySet()) {
//                ConsoleHelper.getMessage(map.getKey() + " : " + Arrays.toString(map.getValue()));
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        //Проверка CreepCommand
//        try {
//            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
//            CreepCommand creepCommand = new CreepCommand(stendDLLCommands, 1, 230.0, 0.0, 50.0, 0,
//                    0, 100.0, 100.0, 0, 2);
//            Thread.sleep(2000);
//            creepCommand.execute();
//
//            for (Map.Entry<Integer, Boolean> map : creepCommand.getCreepCommandResult().entrySet()) {
//                System.out.println(map.getKey() + " : " + map.getValue());
//            }
//        }catch (Exception e) {
//            e.printStackTrace();

        //Проверка StartCommand
//        try {
//            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
//            StartCommand startCommand = new StartCommand(stendDLLCommands, 1, 230.0, 0.0, 50.0, 0,
//                    0, 100.0, 100.0, 0, 2);
//            Thread.sleep(2000);
//            startCommand.execute();
//
//            for (Map.Entry<Integer, Boolean> map : startCommand.getCreepCommandResult().entrySet()) {
//                System.out.println(map.getKey() + " : " + map.getValue());
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
        try {
            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
            stendDLLCommands.getUI(0);
            Thread.sleep(2000);

            stendDLLCommands.setPulseChannel(1, 0);
            stendDLLCommands.setPulseChannel(2, 0);
            stendDLLCommands.setPulseChannel(3, 0);

            stendDLLCommands.clockErrorStart(1, 1.0, 10);
            stendDLLCommands.clockErrorStart(2, 1.0, 10);
            stendDLLCommands.clockErrorStart(3, 1.0, 10);

            while (true) {
                System.out.println(stendDLLCommands.clockErrorRead(1.0, 0, 1));
                System.out.println(stendDLLCommands.clockErrorRead(1.0, 0, 2));
                System.out.println(stendDLLCommands.clockErrorRead(1.0, 0, 3));
                System.out.println();
                Thread.sleep(5000);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
